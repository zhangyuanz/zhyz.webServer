package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 将http请求信息封装打包，以便后续使用
 * 
 * @author zhyz
 *
 */
public class RequestAnalyze {
	// 以下属性只提供getter方法不提供setter方法
	private String method = null;// 请求方法
	private String protocol = null;// 协议版本
	private String requestURL = null;// 请求的URI地址 在HTTP请求的第一行的请求方法后面
	private String fileType = null;

	private String host = null;// 请求的主机信息
	private String connection = null;// Http请求连接状态信息 对应HTTP请求中的Connection
	private String agent = null;// 代理，用来标识代理的浏览器信息 ,对应HTTP请求中的User-Agent:
	private String language = null;// 对应Accept-Language
	private String encoding = null;// 请求的编码方式 对应HTTP请求中的Accept-Encoding
	private String accept = null;// 对应HTTP请求中的Accept;

	private String paramers = null;

	/**
	 * 默认构造函数
	 */
	public RequestAnalyze() {

	}

	/**
	 * 通过socket对象构造RequestAnalyze对象，构造的对象将拥有http请求信息的封装信息
	 * 
	 * @param clientsocket
	 */
	public RequestAnalyze(Socket clientsocket) {
		try {
			this.analyze(clientsocket);
		} catch (IOException e) {
			// 此处可添加日志记录解析失败
			e.printStackTrace();
		}
	}

	/**
	 * 将客服端的socket连接解析打包，封装在此对象中
	 * 
	 * @param clientSocket
	 *            是客服端的socket连接
	 * @throws IOException
	 */
	public void analyze(Socket clientSocket) throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

		String firstLine = in.readLine();// 请求行
		analyzeFirstLine(firstLine);

		String headLine = null;
		for (int i = 0; i < 8; i++) {
			headLine = in.readLine();
			System.out.println("headLine:" + headLine);
			analyzeHeadLine(headLine);
			if (headLine == "\n\r") {
				break;
			}
			if (headLine.startsWith("Connection")) {
				break;
			}
		}
		System.out.println("处理完请求头了");
		// String lastLine = in.readLine();
		// analyzeLastLine(lastLine);

	}

	/**
	 * 解析http请求的请求行信息
	 * 
	 * @param firstLine
	 */
	public void analyzeFirstLine(String firstLine) {
		int x = firstLine.indexOf('/');
		int y = firstLine.lastIndexOf('/');
		method = firstLine.substring(0, x - 1);
		System.out.println("请求方法是:" + method);
		requestURL = firstLine.substring(x, y - 5);
		System.out.println("请求URI是:" + requestURL);
		
		protocol = firstLine.substring(y - 4, firstLine.length());
		System.out.println("请求协议是:" + requestURL);
	}

	/**
	 * 解析http请求头信息
	 * 
	 * @param headLine
	 */
	public void analyzeHeadLine(String headLine) {
		int max = headLine.length();
		if (headLine.startsWith("Host")) {
			host = headLine.substring(6, max);
			System.out.println("请求的主机是:" + host);
		}
		if (headLine.startsWith("User-Agent")) {
			agent = headLine.substring(12, max);
			System.out.println("代理：" + agent);
		}
		if (headLine.startsWith("Accept:")) {
			accept = headLine.substring(8, max);
			System.out.println("接受的类型：" + accept);
		}
		if (headLine.startsWith("Accept-Language")) {
			language = headLine.substring(17, max);
			System.out.println("接受的语言:" + language);
		}
		if (headLine.startsWith("Accept-Encoding")) {
			encoding = headLine.substring(17, max);
			System.out.println("接受的编码:" + encoding);
		}
	}

	/**
	 * 解析http请求的附加参数
	 * 
	 * @param lastLine
	 */
	private void analyzeLastLine(String lastLine) {

	}

	// 一下是封装信息的getter和setter方法

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
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
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the requestURI
	 */
	public String getRequestURL() {
		return requestURL;
	}

	/**
	 * @param requestURI
	 *            the requestURI to set
	 */
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	/**
	 * @return the connection
	 */
	public String getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	public void setConnection(String connection) {
		this.connection = connection;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return
	 */
	public String getParamers() {
		return paramers;
	}

	/**
	 * @param paramers
	 */
	public void setParam(String paramers) {
		this.paramers = paramers;
	}

	/**
	 * @return the accept
	 */
	public String getAccept() {
		return accept;
	}

	/**
	 * @param accept
	 *            the accept to set
	 */
	public void setAccept(String accept) {
		this.accept = accept;
	}

}
