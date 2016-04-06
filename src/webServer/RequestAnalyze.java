package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.URL;

/**
 * 将http请求信息封装打包，以便后续使用
 * 
 * @author zhyz
 *
 */
public class RequestAnalyze {
	final Logger logger = LoggerFactory.getLogger(RequestAnalyze.class);
	// 以下属性只提供getter方法不提供setter方法
	private String method = null;// 请求方法
	private String protocol = null;// 协议版本
	private URL requestURL = null;// 请求的URI地址 在HTTP请求的第一行的请求方法后面

	private Map<String, String> head = new HashMap<String, String>();

	//private String body = null;

	/**
	 * 通过socket对象构造RequestAnalyze对象，构造的对象将拥有http请求信息的封装信息
	 * 
	 * @param clientsocket
	 */
	public RequestAnalyze(Socket clientsocket) {
		this.analyze(clientsocket);
		
	}

	/**
	 * 将客服端的socket连接解析打包，封装在此对象中
	 * 
	 * @param clientSocket
	 *            是客服端的socket连接
	 * @throws IOException
	 */
	private void analyze(Socket clientSocket){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			logger.info("开始封装客服端请求了...");
			String firstLine = in.readLine();// 请求行
			logger.info("firstLine:" + firstLine);
			if (firstLine == null)
				return;
			firstLine = URLDecoder.decode(firstLine, "UTF-8");
			analyzeFirstLine(firstLine);

			String headLine = null;
			while (true) {
				headLine = in.readLine();
				if (headLine.isEmpty() || headLine == null) {
					break;
				}
				analyzeHeadLine(headLine);
			}
			logger.info("请求已封装完成");
		} catch (UnsupportedEncodingException e) {
			logger.info("url解码失败"+e.getLocalizedMessage());
		} catch (IOException e) {
			logger.info("IO："+e.getLocalizedMessage());
		} finally{
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.info("流关闭异常"+e.getLocalizedMessage());
				}
		}
	}
	/**
	 * 解析http请求头信息
	 * 
	 * @param headLine
	 */
	void analyzeHeadLine(String headLine) {
		String key = headLine.substring(0, headLine.indexOf(':'));
		String value = headLine.substring(headLine.indexOf(':'+2), headLine.length());
		head.put(key, value);
	}

	/**
	 * 解析http请求的请求行信息
	 * 
	 * @param firstLine
	 */
	void analyzeFirstLine(String firstLine) {
		if (firstLine == null) {
			return;
		}
		String sps[] = firstLine.split(" ");
		setMethod(sps[0]);
		setRequestURL(new URL(sps[1]));
		setProtocol(sps[2]);
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the requestURL
	 */
	public URL getRequestURL() {
		return requestURL;
	}

	/**
	 * @param requestURL the requestURL to set
	 */
	public void setRequestURL(URL requestURL) {
		this.requestURL = requestURL;
	}
	

	/**
	 * 截取字符串中关键字以后字符串
	 * 
	 * @param line
	 * @param key
	 * @return
	 */
	/*
	private String cut(String str, String line, String key) {
		if (line.startsWith(key)) {
			return line.substring(line.indexOf(":") + 2, line.length());
		} else {
		}
		return str;
	}
	*/

	

}