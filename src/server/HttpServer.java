package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器监听端口的线程类，在创建的同时开始执行run 拥有一个线程池和一个serversocket属性 init方法初始化线程池大小，并绑定配置端口
 * 
 * @author xmubaga
 *
 */
public class HttpServer implements Runnable {
	final Logger logger = LoggerFactory.getLogger(HttpServer.class);
	private ExecutorService pool;
	private ServerSocket serverSocket;
	private boolean flag = true;

	/**
	 * 构造方法，先初始化，然后开始运行服务线程
	 */
	public HttpServer() {
		try {
			serverSocket = new ServerSocket(Config.PORT);
			pool = Executors.newFixedThreadPool(10);
			new Thread(this).start();
			logger.info("HTTP服务器正在运行,端口:" + Config.PORT);
		} catch (IOException e) {
			logger.error("无法启动HTTP服务器:" + e.getLocalizedMessage());
		}	
	}

	/**
	 * 该方法轮询端口是否有客服端连接进来，有链接进来则提交给线程池处理
	 */
	@Override
	public void run() {
		while (flag) {
			try {
				Socket client = serverSocket.accept();
				logger.info("有客服端链接进来了：" + client.toString());	
				Handle handle = new Handle(client);
				pool.execute(handle);
			} catch (IOException e) {
				//出现异常时及时关闭线程池，避免占用资源
				pool.shutdown();
				logger.error(e.getLocalizedMessage());
			}
		}
	}

	/**
	 * 结束服务器线程
	 */
	protected void close() {
		pool.shutdown();
		while (!pool.isTerminated())
			;// 等待线程池任务完成
		try {
			serverSocket.close();
		} catch (IOException e) {
			logger.warn("serverSocket关闭异常" + e.getLocalizedMessage());
			flag = false;
		} finally {
			flag = false;
		}
	}
}