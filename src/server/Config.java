package server;

/**
 * 服务器的配置文件，包括服务端口及服务目录
 * 
 * @author xmubaga
 *
 */
public class Config {
	// 服务器端口
	public static final int PORT = 80;
	// 服务器根
	public static final String ROOT = "d";
	// 服务器服务目录的windows格式
	public static final String ROOT_PATH = ROOT + ":/";
	// 服务器的身份验证信息
	public static final String SERVER_TAG = "favicon.ico";
	// 服务器判断允许访问的文件类型
	public static final String[] STATIC_FILES = { "txt", "doc", "docx", "xlsx", "pdf", "ini", "xml", "zip", "rar", "jar",
													"TXT", "DOC", "DOCX", "XLSX", "PDF", "INI", "XML", "ZIP", "RAR", "JAR" };
	// 可以直至预览的文件
	public static final String[] PAGES = { "html", "HTML", "html5", "HTML5" };
	// 图片文件,这里只列举一部分
	public static final String[] IMAGES = { "JPG", "jpg", "PNG", "png", "bmp", "BMP" };
}