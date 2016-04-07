package util;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Tool {
	/**
	 * 从一个socket获取其打印流
	 * @param client
	 * @return
	 */
	public static PrintStream getPrint(Socket client){	
		try {
			return new PrintStream(client.getOutputStream());
		} catch (IOException e) {
			return null;
		}
	}
	/**
	 * 判断文件是否包含在一个文件列表当中
	 * 
	 * @param fame
	 *            是文件名
	 * @return
	 */
	public static boolean contain(String[] fileLastNames, String fileName) {
		String lastName = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
		for (String temp : fileLastNames) {
			if (temp.equals(lastName)) {
				return true;
			}
		}
		return false;
	}
	
}
