package webServer;
/**
 * 服务器的配置文件，包括服务端口及服务目录
 * @author xmubaga
 *
 */
public class Config {
	//服务器端口
	public static final int PORT = 80;
	//服务器根
	public static final String ROOT = "d";
	//服务器服务目录的windows格式
	public static final String ROOT_PATH = ROOT + ":/";
	//服务器服务目录的url格式
	//public static final String ROOT_URL[] = {"/" + ROOT + "/","/" + ROOT,"/"};
	//服务器的身份验证信息
	public static final String SERVER_ID = "favicon.ico";
	//服务器判断允许访问的文件类型
	public static final String[] STATIC_FILES = 
		{"jpg","png","txt","doc","docx","xls","pdf","ini","xml","html","zip","rar",
		"JPG","PNG","TXT","DOC","DOCX","XLS","PDF","INI","XML","HTML","ZIP","RAR"};
	/**
	 * 测试通过的文件类型：txt,xsl,ini,xml,zip
	 * isuess：存在乱码问题
	 */
	
}