package common;

import java.util.HashMap;

import util.URI;

public interface Request {
	/**
	 * 获取http请求的请求方法 获取请求的方法
	 * 
	 * @return
	 */
	public String getMethod();

	/**
	 * 获取请求的协议
	 * 
	 * @return
	 */
	public String getProtocol();

	/**
	 * 获取请求的url
	 * 
	 * @return
	 */
	public URI getRequestURI();

	/**
	 * 获取请求的头信息
	 * 
	 * @return
	 */
	public HashMap<String, String> getHead();
}
