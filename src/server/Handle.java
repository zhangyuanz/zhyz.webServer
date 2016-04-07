package server;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import expection.MethodError;
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
	private Socket client;
	/**
	 * 构造方法，初始化客服端socket对象
	 * 
	 * @param clientSocket
	 */
	public Handle(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		Request request = Request.getInstance(client);
		Response0 response = Response0.getInstance(client);
		try {
			handle(request,response);
		} catch (MethodError e) {
			logger.error(e.getLocalizedMessage());
			return;
		}
	}

	/**
	 * 业务逻辑处理函数
	 * 非get的请求抛出异常
	 * 用户直接输入主机，url为null则返回默认根目录
	 * favicon.ico定位在根目录下
	 * 访问非根目录，则返回无权限
	 * 访问文件则返回文件下载，目录则返回文件列表
	 * 区分文件的类型，决定响应的内容
	 * @throws MethodError 
	 * @throws IOException 
	 * 
	 */
	private void handle(Request request,Response0 response) throws MethodError {
		if (request.getMethod() != null && !(request.getMethod().equals("GET"))) {
			throw new MethodError("非GET方法");
		}

		URL requesturl = request.getRequestURL();
		if (requesturl == null) {
			logger.info("请求的url是null！");
			response.listOfFile(new File(Config.ROOT_PATH));
			return;
		}

		if (requesturl.getString().equals(Config.SERVER_TAG)) {
			//favicon.ico
			response.downloadFile(new File(Config.ROOT_PATH + Config.SERVER_TAG));
			return;
		}

		// 非根目录,没有访问权限
		if (!requesturl.getRoot().equals(Config.ROOT)) {
			logger.info("用户试图访问非根目录资源");
			warning("没有权限");
			return;
		} else {
			String path = requesturl.toPath();
			logger.info("访问路劲（本机windows能够接受的路劲）:" + path);
			File file = new File(path);
	
			if (file.isDirectory()) {
				logger.info("访问的资源是一个目录");
				response.listOfFile(file);	
			} else {
				
				if (!file.exists()) {
					logger.info("访问的文件不存在");
					warning("找不到文件");
				} else {
					if (!isStaticFile(file.getName())){
						warning("不支持该文件类型");
						return;
					}
					if(isHTML(file.getName())){
						response.privewFile(file);
						return;
					} else {
						response.downloadFile(file);		
					}					
				}
			}
		}
	}
	
	private boolean isHTML(String name){
		return Tool.contain(Config.PAGES,name);
	}
	private boolean isImage(String name){
		return Tool.contain(Config.IMAGES,name);
	}
	private boolean isStaticFile(String name){
		return Tool.contain(Config.STATIC_FILES,name);
	}
	
	/**
	 * 向浏览器用户输出提示信息
	 * @param info
	 */
	private void warning(String info){
		PrintStream pw = Tool.getPrint(this.client);
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		pw.println();
		pw.println(info);
		pw.println();
	}
	
}