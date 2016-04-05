package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			logger.info("请求信息封装失败！"+e.getLocalizedMessage());
		}
	}

	/**
	 * 将客服端的socket连接解析打包，封装在此对象中
	 * 
	 * @param clientSocket
	 *            是客服端的socket连接
	 * @throws IOException
	 */
	private void analyze(Socket clientSocket) throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		logger.info("开始封装客服端请求了...");
		String firstLine = in.readLine();// 请求行
		logger.info("firstLine:"+firstLine);
		if(firstLine == null)
			return;
		firstLine = URLDecoder.decode(firstLine, "UTF-8");
		analyzeFirstLine(firstLine);

		String headLine = null;
		while (true) {
			headLine = in.readLine();
			analyzeHeadLine(headLine);
			if (headLine.isEmpty() || headLine == null) {
				break;
			}
		}
		
		// String lastLine = in.readLine();
		//analyzeLastLine(lastLine);
		logger.info("请求已封装完成");
	}

	/**
	 * 解析http请求的请求行信息
	 * 
	 * @param firstLine
	 */
	void analyzeFirstLine(String firstLine) {
		if(firstLine == null){
			return;
		}
		int x = firstLine.indexOf('/');
		int y = firstLine.lastIndexOf('/');
		method = firstLine.substring(0, x - 1);
		logger.info("请求方法是:" + method);
		requestURL = new URL(firstLine.substring(x, y - 5));
		logger.info("请求URI是:" + requestURL);
		protocol = firstLine.substring(y - 4, firstLine.length());
		logger.info("请求协议是:" + protocol);
	}

	/**
	 * 解析http请求头信息
	 * 
	 * @param headLine
	 */
	private void analyzeHeadLine(String headLine) {
		logger.info("HeadLine内容："+headLine);
		if(headLine == null){
			return;
		}
		host = cut(host,headLine,"Host:");
		agent = cut(agent,headLine,"User-Agent:");
		accept = cut(accept,headLine,"Accept:");
		language  = cut(language,headLine,"Accept-Language:");
		encoding = cut(encoding,headLine,"Accept-Encoding:");
		connection = cut(connection,headLine,"Connection:");
		
	}
	/**
	 * 截取字符串中关键字以后字符串
	 * @param line
	 * @param key
	 * @return
	 */
	private String cut(String str,String line,String key){
		if(line.startsWith(key)){
			return line.substring(line.indexOf(":")+2, line.length());
		}else{}
		return str;
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
	 * @return the requestURI
	 */
	public URL getRequestURL() {
		return requestURL;
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