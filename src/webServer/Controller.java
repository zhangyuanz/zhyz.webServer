package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Controller {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command = null;
		HttpServer httpServer = null;
		while (true) {
			try {
				command = reader.readLine();
				if (command.equals("start")) {
					if (httpServer == null) {
						httpServer = new HttpServer();
					} else {
						System.out.println("服务器已经启动，请勿重复此操作！");
					}
				}
				command = reader.readLine();
				if (command.equals("exit")) {
					if (httpServer != null) {
						// 释放资源
						httpServer.close();
					}
					// 等待已有任务结束，退出
					break;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("服务器停止了...");
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
