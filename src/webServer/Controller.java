package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该类用于启动或停止服务器
 * 
 * @author xmubaga
 *
 */
public class Controller {

	/**
	 * 控制器自启动main方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(Controller.class);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command = null;
		HttpServer httpServer = null;
		try {
			while (true) {
				command = reader.readLine();
				if (!command.equals("start") && !command.equals("exit")) {
					logger.info("非指令输入！启动请输入start，关闭请输入exit。");
				}
				if (command.equals("start")) {
					if (httpServer == null) {
						httpServer = new HttpServer();
					} else {
						logger.info("服务器已经启动，请勿重复此操作！");
					}
				}
				if (command.equals("exit")) {
					if (httpServer != null) {
						// 释放资源
						httpServer.close();
						logger.info("服务器停止了...");
						// 等待已有任务结束，退出
						break;
					} else {
						logger.info("服务器还没启动，请先输入start");
					}
				}
			}
		} catch (IOException e) {
			logger.error("控制台输入异常");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.warn("流关闭异常");
				}

			}
		}

	}
}