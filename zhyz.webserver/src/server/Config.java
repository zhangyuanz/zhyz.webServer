package server;

/**
 * 服务器的配置文件，包括服务端口及服务目录
 * 
 * @author xmubaga
 *
 */
public class Config {
	/**
	 * 服务器端口
	 * 
	 */
	public static final int PORT = 80;
	/**
	 * 服务器根
	 */
	public static final String ROOT = "d";
	/**
	 * 服务器服务目录的windows格式
	 */
	public static final String ROOT_PATH = ROOT + ":/";
	/**
	 * 服务器的身份验证信息
	 */
	public static final String SERVER_TAG = "favicon.ico";
	/**
	 * 服务器判断允许访问的文件类型
	 */
	public static final String[] STATIC_FILES = { "txt", "doc", "docx", "xlsx",
			"pdf", "ini", "xml", "zip", "rar", "jar", "html", "html5", "jpg",
			"png", "bmp" };
	/**
	 * 可以直至预览的文件
	 */
	public static final String[] WEB_PAGES = { "html", "html5"};
	/**
	 * 图片文件,这里只列举一部分
	 */
	public static final String[] IMAGES = { "jpg", "png", "bmp"};

	/**
	 * 最大复用socket长连接的次数
	 */
	public static final int SOCKET_REUSE_MAX_TIMES = 100;
	/**
	 * 保持keep-alive的时长，单位为毫秒
	 */
	public static final int KEEP_ALIVE_TIME_OUT = 2000;
}