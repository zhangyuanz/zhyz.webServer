package webServer;

import java.io.File;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.URL;

/**
 * 该类是完成逻辑控制，业务流程处理，实现runable接口，专门执行逻辑任务
 * 
 * @author xmubaga
 *
 */
public class Handle implements Runnable {
	final Logger logger = LoggerFactory.getLogger(Handle.class);
	private Socket clientSocket;

	/**
	 * 构造方法，初始化客服端socket对象
	 * 
	 * @param clientSocket
	 */
	public Handle(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		handleRequest();
	}

	private void handleRequest() {

		RequestAnalyze request = new RequestAnalyze(clientSocket);
		Response response = new Response(clientSocket);

		if (request.getMethod() != null && !(request.getMethod().equals("GET"))) {
			logger.info("请求是方法不是GET");
			response.outNotGet();
			// thow 异常？自定义异常
			return;
		}

		
		URL requesturl = request.getRequestURL();
		if (requesturl == null) {
			logger.info("请求的url是null！");
			response.outFileList(new File(Config.ROOT_PATH));
			return;
		}
		logger.info("请求的URL：/"+requesturl.getString());
		if (requesturl.getString().equals(Config.SERVER_TAG)) {
			response.outFile(new File(Config.ROOT_PATH + Config.SERVER_TAG));
			return;
		}
		
		// 非根目录,没有访问权限
		if (!requesturl.getRoot().equals(Config.ROOT)) {
			logger.info("用户试图访问非根目录资源");
			response.outNoPower();
			return;
		} else {
			String path = requesturl.toPath();
			logger.info("访问路劲（本机windows能够接受的路劲）:" + path);
			File file = new File(path);
			// 只是一个目录
			if (file.isDirectory()) {
				logger.info("访问的资源是一个目录");
				response.outFileList(file);
			} else {
				// 文件不存在
				if (!file.exists()) {
					logger.info("访问的文件不存在");
					response.outNotExist();
				} else {
					//html页面，图片文件，则提供预览，静态文件提供下载，否则非法类型
					if (contain(Config.PAGES, file.getName())) {
						response.outFilePrivew(file);
					} else if (!contain(Config.STATIC_FILES, file.getName())) {
						response.outIllegalType();
					} else {
						response.outFile(file);
					}
				}
			}
		}
	}

	/**
	 * 判断文件是否为静态文件
	 * 
	 * @param str
	 *            是文件名
	 * @return
	 */
	private boolean contain(String[] fileLastNames, String str) {
		String lastName = str.substring(str.lastIndexOf('.') + 1, str.length());
		logger.info("文件后缀名为：" + lastName);
		for (String temp : fileLastNames) {
			if (temp.equals(lastName)) {
				logger.info("是静态文件");
				return true;
			}
		}
		logger.info("不是静态文件");
		return false;
	}
}