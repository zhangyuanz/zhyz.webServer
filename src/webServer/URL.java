package webServer;

/**
 * URL是一种特殊的字符串，定义以表示却别，同时提供相关方法，方便操作
 * @author Administrator
 *
 */
public class URL {
	private String url ;
	
	public URL(String str){
		this.url = str;
	}
	/**
	 * 获得URL对应的字符串
	 * @return
	 */
	public String getUrl(){
		return this.url;
	}
	/**
	 * 获得url请求的资源根目录
	 * 
	 */
	public String getRoot(){
		if(url != null)
			return url.substring(1, 2);
		return null;
	}
	
}

