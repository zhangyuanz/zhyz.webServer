package util;

import server.Config;

/**
 * URL是一种特殊的字符串，定义以表示却别，同时提供相关方法，方便操作 设计此类，一是想解决有关“/”硬编码,二是解决用户模糊输入时也能得到正确结果
 * issues：2016-4-5仍然有问题
 * 
 * @author Administrator
 *
 */
public class URL {
	private String url;

	public URL(String str) {
		this.url = str;
	}

	/**
	 * 获得URL对应的字符串
	 * 
	 * @return
	 */
	public String getString() {
		if (url != null)
			return url.substring(1, url.length());
		return null;
	}

	/**
	 * 获得url请求的资源根目录
	 * 
	 */
	public String getRoot() {
		if (url != null)
			if (url.length() >= 2)
				return url.substring(1, 2);
			else
				// 默认根
				return Config.ROOT;
		return null;
	}

	/**
	 * 将一个URL转换为Windows能够识别的路劲 并允许用户输入如果是空，即只输入主机ip，则访问默认跟目录，如果是根目录没有添加“/”自动添加
	 * 
	 * <pre>
	 * null =  "d:/"
	 * 或者/ = "d:/"
	 * /d = "d:/"
	 * /d/ = "d:/"
	 * /d/user = "d:/user"
	 * </pre>
	 * 
	 * @param url是http请求经过封装的url
	 */
	public String toPath() {
		if (this.url == null || this.getString().equals(Config.ROOT)) {
			return Config.ROOT_PATH;
		}
		return Config.ROOT_PATH + url.substring(3, url.length());
	}
}
