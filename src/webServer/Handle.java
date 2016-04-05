package webServer;

import java.io.File;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		logger.info("封装的url：" + requesturl);
		if (requesturl == null || requesturl.getUrl()) {
			// 输入空路径，默认显示d盘目录,或者忘记末尾/自动添加
			response.outFileList(new File(Config.ROOT));
			return;
		}
		if (requesturl.equals("/" + Config.SERVER_ID)) {
			response.outFile(new File(Config.ROOT + Config.SERVER_ID));
			return;
		}
		// 非D盘
		String disk = requesturl.substring(1, 2);
		logger.info("disk：" + disk);
		if (!disk.equals(Config.DISK)) {
			logger.info("访问非D盘");
			response.outNoPower();
			return;
		} else {
			String path = URLtoPath(requesturl);
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
					// 非静态文件
					if (!contain(Config.STATIC_FILES, file.getName())) {
						response.outIllegalType();
					} else {
						response.outFile(file);
					}
				}
			}
		}
	}

	/**
	 * 将一个URL转换为Windows能够识别的路劲 并允许用户输入如果是空，即只输入主机ip，则访问默认跟目录，如果是根目录没有添加“/”自动添加
	 * 
	 * <pre>
	 * null =  "d:/"
	 * /d = "d:/"
	 * /d/ = "d:/"
	 * /d/user = "d:/user"
	 * </pre>
	 * 
	 * @param url是http请求经过封装的url
	 */
	private String URLtoPath(String url) {
		if (url.isEmpty() || url.equals("/")) {
			return Config.ROOT;
		}
		return Config.ROOT + url.substring(3, url.length());
	}

	/**
	 * 判断文件是否为静态文件
	 * 
	 * @param str
	 *            是文件名
	 * @return
	 */
	private boolean contain(String[] fileLastNames, String str) {
		String lastName = str.substring(str.indexOf(".") + 1, str.length());
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