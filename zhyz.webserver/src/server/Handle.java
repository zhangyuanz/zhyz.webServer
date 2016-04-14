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
			handle(request, response);
		} catch (MethodError e) {
			logger.error(e.getLocalizedMessage());
			return;
		}
	}

	/**
	 * 业务逻辑处理函数 非get的请求抛出异常 用户直接输入主机，url为null则返回默认根目录 favicon.ico定位在根目录下
	 * 访问非根目录，则返回无权限 访问文件则返回文件下载，目录则返回文件列表 区分文件的类型，决定响应的内容
	 * 
	 * @throws MethodError
	 * @throws IOException
	 * 
	 */
	private void handle(Request request, Response response) throws MethodError {
		if (request.getMethod() != null && !(request.getMethod().equals("GET"))) {
			throw new MethodError("非GET方法");
		}

		URL url = request.getRequestURL();
		if (url == null) {
			logger.info("请求的url是null！");
			return;
		}

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
			}
		}
	}

	private boolean isHTML(String name) {
		return Tool.contain(Config.PAGES, name);
	}

	private boolean isImage(String name) {
		return Tool.contain(Config.IMAGES, name);
	}

	private boolean isStaticFile(String name) {
		return Tool.contain(Config.STATIC_FILES, name);
	}

	/**
	 * 向浏览器用户输出提示信息
	 * 
	 * @param info
	 */
	private void warning(String info) {
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		pw.println();
		pw.println(info);
		pw.println();
		pw.close();// 关闭socket连接
	}

	/**
	 * 一次性全部下載文件
	 * 
	 * @param file
	 *            待下载的文件
	 */
	private void downloadFile(File file) {
		logger.info("开始download文件" + file.getName());
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.print("Content-Disposition:attachment;filename=");
		pw.println(file.getName());
		pw.println("Content-Type:application/octet-stream;charset=UTF-8");
		pw.print("Content-Length:");
		pw.println(file.length());

		pw.println();
		FileOpreator.file2Socket(file,response.getSocket());
		pw.println();
		logger.info("download完毕！");

	}

	/**
	 * 实现分块传输下载文件
	 * 
	 * @param file
	 *            待下载的文件
	 * @param start
	 *            开始下载的位置
	 * @param end
	 *            下载结束的位置
	 */
	private void downloadFile(File file, long start, long end) {
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
		FileOpreator.file2Socket(file, start, end,response.getSocket());
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
		pw.print(Config.TIME_OUT / 1000);
		pw.print(",max=");
		pw.println(Config.MAX);
	}

	/**
	 * 预览文件
	 * 
	 * @param file
	 */

	private void privewFile(File file) {
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type: text/html; charset=UTF-8");
		setKeepAlive(pw);
		pw.print("Content-Length:");
		pw.println(file.length());

		pw.println();
		FileOpreator.file2Socket(file,response.getSocket());
		pw.println();

	}

	/**
	 * 预览图片
	 * 
	 * @param file
	 */
	private void privewImage(File file) {
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:image/jpeg;charset=UTF-8");
		setKeepAlive(pw);
		pw.print("Content-Length:");
		pw.println(file.length());
		pw.println();
		FileOpreator.file2Socket(file,response.getSocket());
		pw.println();
		// logger.info("privewImage关闭socket了。");
		// pw.close();
	}

	/**
	 * 显示目录下的文件列表
	 * 
	 * @param file
	 */

	private void listOfFile(File file) {
		if (file == null || !file.exists() || !file.isDirectory())
			return;
		logger.info("开始响应目录文件列表");
		PrintStream pw = response.getPrintStream();
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		setKeepAlive(pw);
		pw.println();
		pw.println("<a href='javascript:history.go(-1)'>返回上级</a><br>");
		for (String str : file.list()) {
			String thisPath = file.toString().replace("\\", "/") + '/' + str;
			if (new File(thisPath).isDirectory()) {
				pw.print("<a href='");
				pw.print(str);
				pw.print("/'><font color = 'red'>");
				pw.print(str);
				pw.print("</font></a><br>");
				pw.println();
			} else {
				pw.print("<a href='");
				pw.print(str);
				pw.print("/'>");
				pw.print(str);
				pw.print("</a><br>");
				pw.println();
			}
		}
		logger.info("目录文件列表响应完毕！");
	}
}