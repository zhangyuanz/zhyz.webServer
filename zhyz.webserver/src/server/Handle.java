package server;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import expection.IllegalStringTypeException;
import expection.MethodError;
import util.FileOpreator;
import util.Tool;
import util.URL;

/**
 * 该类是完成逻辑控制，业务流程处理，实现runable接口，专门执行逻辑任务
 * 
 * @author xmubaga
 *
 */
public class Handle implements Runnable {
	final Logger logger = LoggerFactory.getLogger(Handle.class);
	private Request request;
	private Response response;

	/**
	 * 构造方法，初始化客服端socket对象
	 * 
	 * @param clientSocket
	 */
	public Handle(Request request, Response response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public void run() {
		try {
			handle();
		} catch (MethodError e) {
			logger.error(e.getLocalizedMessage());
			return;
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
			return;
		}
	}

	/**
	 * 核心业务逻辑处理函数,
	 * 非get的请求抛出异常 用户直接输入主机，url为null则返回默认根目录 favicon.ico定位在根目录下
	 * 访问非根目录，则返回无权限 访问文件则返回文件下载，目录则返回文件列表 区分文件的类型，决定响应的内容
	 * 
	 * @throws MethodError
	 * @throws IOException
	 * 
	 */
	private void handle() throws MethodError, IOException {
		if (request.getMethod() == null || !request.getMethod().equals("GET")) {
			throw new MethodError("非GET方法");
		}

		URL url = request.getRequestURL();
		if (url == null) {
			//此情况是用户只输入了主机，没有输入url，应该为用户显示index.html
			logger.info("请求的url是null！");
			return;
		}
		// favicon.ico
		if (url.getString().equals(Config.SERVER_TAG)) {
			downloadFile(new File(Config.ROOT_PATH + Config.SERVER_TAG));
			return;
		}

		// 非根目录,没有访问权限
		if (!url.getRoot().equals(Config.ROOT)) {
			logger.info("用户试图访问非根目录资源");
			warning("没有权限");
			return;
		} else {
			doLegalUrl(url);
		}
	}

	/**
	 * 处理合法的url输入，即：请求的方法是GET，请求的目录是根目录
	 * 
	 * @param url
	 * @throws IOException 
	 */
	private void doLegalUrl(URL url) throws IOException {
		String path = url.toPath();
		logger.info("访问路劲（本机windows能够接受的路劲）:" + path);
		File file = new File(path);

		if (file.isDirectory()) {
			logger.info("访问的资源是一个目录");
			listOfFile(file);
		} else {
			if (!file.exists()) {
				logger.info("访问的文件不存在");
				warning("找不到文件");
			} else {
				doFileExist(file);
			}
		}
	}

	/**
	 * 处理url请求的是某一文件的情况
	 * 
	 * @param file
	 * @throws IOException 
	 */
	private void doFileExist(File file) throws IOException {
		if (!isStaticFile(file.getName())) {
			warning("不支持该文件类型");
			return;
		}
		if (isImage(file.getName())) {
			privewImage(file);
			return;
		}
		if (isHTML(file.getName())) {
			privewFile(file);
			return;
		}
		String range = request.getHead().get("Range");
		if (range == null) {
			downloadFile(file);
			return;
		} else {
			long start = 0;
			long end = 0;
			try {
				start = Tool.getRangeStart(range);
				end = Tool.getRangeEnd(range);
			} catch (IllegalStringTypeException e) {
				logger.warn("Range解析失败");
			}
			downloadFile(file, start, end);
		}
	}

	/**
	 * 判断一个文件是否为web页面
	 * 
	 * @param name
	 * @return
	 */
	private boolean isHTML(String name) {
		return Tool.containIgnoreCaps(Config.WEB_PAGES, name);
	}

	/**
	 * 判断一个文件是否为图片文件
	 * 
	 * @param name
	 * @return
	 */
	private boolean isImage(String name) {
		return Tool.containIgnoreCaps(Config.IMAGES, name);
	}

	/**
	 * 判断一个文件是否为静态文件
	 * 
	 * @param name
	 * @return
	 */
	private boolean isStaticFile(String name) {
		return Tool.containIgnoreCaps(Config.STATIC_FILES, name);
	}

	/**
	 * 向浏览器用户输出提示信息
	 * 
	 * @param info
	 * @throws IOException 
	 */
	private void warning(String info) throws IOException {
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		pw.println();
		pw.println(info);
		pw.println();
		pw.close();// 关闭socket连接
	}

	/**
	 * 为用户一次性全部下載文件
	 * 
	 * @param file
	 *            待下载的文件
	 * @throws IOException 
	 */
	private void downloadFile(File file) throws IOException {
		logger.info("开始download文件" + file.getName());
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.print("Content-Disposition:attachment;filename=");
		pw.println(file.getName());
		pw.println("Content-Type:application/octet-stream;charset=UTF-8");
		pw.print("Content-Length:");
		pw.println(file.length());
		setKeepAlive(pw);
		pw.println();
		FileOpreator.file2OutputStream(file, response.getOutputStream());
		pw.println();
		logger.info("download完毕！");

	}

	/**
	 * 为用户实现分块传输下载文件
	 * 
	 * @param file
	 *            待下载的文件
	 * @param start
	 *            开始下载的位置
	 * @param end
	 *            下载结束的位置
	 * @throws IOException 
	 */
	private void downloadFile(File file, long start, long end) throws IOException {
		PrintStream pw = response.getPrintStream();
		logger.info("开始download文件的一部分" + file.getName());
		pw.println("HTTP/1.1 200 OK");

		pw.print("Content-Disposition:attachment;filename=");
		pw.println(file.getName());

		pw.println("Content-Type:application/octet-stream;charset=UTF-8");

		setKeepAlive(pw);

		pw.print("Content-Length:");
		pw.println(file.length());

		pw.print("Content-Range:bytes ");
		pw.print(start);
		pw.print('-');
		pw.print(end);
		pw.print('/');
		pw.println(file.length());

		pw.println();
		FileOpreator.file2OutputStream(file, start, end, response.getOutputStream());
		pw.println();
		logger.info("此部分文件download完毕！");

	}

	/**
	 * 添加响应体的Connection头，和keep-alive设置
	 * 
	 * @param pw
	 */
	private void setKeepAlive(PrintStream pw) {
		pw.println("Connection: keep-alive");
		pw.print("Keep-Alive: timeout=");
		pw.print(Config.KEEP_ALIVE_TIME_OUT / 1000);
		pw.print(",max=");
		pw.println(Config.SOCKET_REUSE_MAX_TIMES);
	}

	/**
	 * 预览文件
	 * 
	 * @param file
	 * @throws IOException 
	 */

	private void privewFile(File file) throws IOException {
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type: text/html; charset=UTF-8");
		setKeepAlive(pw);
		pw.print("Content-Length:");
		pw.println(file.length());

		pw.println();
		FileOpreator.file2OutputStream(file, response.getOutputStream());
		pw.println();

	}

	/**
	 * 预览图片
	 * 
	 * @param file
	 * @throws IOException 
	 */
	private void privewImage(File file) throws IOException {
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:image/jpeg;charset=UTF-8");
		setKeepAlive(pw);
		pw.print("Content-Length:");
		pw.println(file.length());
		pw.println();
		FileOpreator.file2OutputStream(file, response.getOutputStream());
		pw.println();

	}

	/**
	 * 显示目录下的文件列表
	 * 
	 * @param file
	 * @throws IOException 
	 */

	private void listOfFile(File file) throws IOException {
		if (file == null || !file.exists() || !file.isDirectory())
			return;
		logger.info("开始响应目录文件列表");
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		setKeepAlive(pw);
		pw.println();
		pw.println("<a href='javascript:history.go(-1)'>返回上级</a><br>");
		//String parentPath = file.getPath().replace('\\', '/') + '/';
		for (File child : file.listFiles()) {
			pw.print("<a href='");
			pw.print(child.getName());
			pw.print("/'><font ");
			if (child.isDirectory())
				pw.print(" color = 'red'");
			pw.print('>');
			pw.print(child.getName());
			pw.print("</font>");
			pw.print("</a><br>");
			pw.println();
		}
		pw.println();
		logger.info("目录文件列表响应完毕！");
	}
}