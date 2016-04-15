package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.URL;

/**
 * 将http请求信息封装打包，以便后续使用
 * 
 * @author zhyz
 *
 */
public class Request implements common.Request {
	final Logger logger = LoggerFactory.getLogger(Request.class);

	private String method = null;// 请求方法
	private String protocol = null;// 协议版本
	private URL requestURL = null;// 请求的URI地址 在HTTP请求的第一行的请求方法后面

	private HashMap<String, String> head = new HashMap<String, String>();

	/**
	 * 静态工厂方法，根据不同的socket，可获得不同的request对象
	 * 
	 * @param client
	 * @return
	 */
	public static Request getInstance(Socket client) {

		return new Request(client);
	}

	/**
	 * 通过socket对象构造RequestAnalyze对象，构造的对象将拥有http请求信息的封装信息
	 * 
	 * @param clientsocket
	 */
	private Request(Socket client) {
		this.analyze(client);

	}

	/**
	 * 将客服端的socket连接解析打包，封装在此对象中
	 * 
	 * @param clientSocket
	 *            是客服端的socket连接
	 * @throws IOException
	 */
	private void analyze(Socket clientSocket) {
		if (clientSocket == null || clientSocket.isClosed())
			return;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream(), "UTF-8"));

			String firstLine = null;
			try {// 超时返回null
				firstLine = in.readLine();
			} catch (IOException e) {
				logger.info("超时，关闭socket，原因：" + e.getLocalizedMessage());
				clientSocket.close();
				return;
			}

			if (firstLine == null) {
				// 请求行为null，并不一定是超时，说明是客服端自己关闭了socket，所以这里需被动关闭本次链接，避免httpServer循环等max减为0时才跳出循环
				logger.warn("客服端端关闭了socket。");
				clientSocket.close();
				return;
			} else
				logger.info(firstLine);

			firstLine = URLDecoder.decode(firstLine, "UTF-8");
			analyze(firstLine);

			String headLine = null;
			while (true) {
				headLine = in.readLine();
				logger.info(headLine);
				if (headLine.isEmpty() || headLine == null) {
					break;
				}
				String key = headLine.substring(0, headLine.indexOf(':'));
				String value = headLine.substring(headLine.indexOf(':') + 2,
						headLine.length());
				head.put(key, value);
			}

			logger.info("请求已封装完成");

		} catch (IOException e) {
			logger.error("IO：" + e.getLocalizedMessage());
		}
	}

	/**
	 * 解析請求行
	 * 
	 * @param firstLine
	 */
	private void analyze(String firstLine) {
		int x = firstLine.indexOf('/');
		int y = firstLine.lastIndexOf('/');
		this.setMethod(firstLine.substring(0, x - 1));
		this.setRequestURL(new URL(firstLine.substring(x, y - 5)));
		this.setProtocol(firstLine.substring(y - 4, firstLine.length()));
	}

	/**
	 * @return the method
	 */
	@Override
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	private void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the protocol
	 */
	@Override
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	private void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the requestURL
	 */
	@Override
	public URL getRequestURL() {
		return requestURL;
	}

	/**
	 * @param requestURL
	 *            the requestURL to set
	 */
	private void setRequestURL(URL requestURL) {
		this.requestURL = requestURL;
	}

	@Override
	public HashMap<String, String> getHead() {
		return head;
	}

}