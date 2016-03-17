package webServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Response {
	private Socket clientSocket;
	private PrintWriter pw;

	/**
	 * 此构造方法注入客服端socket对象
	 * 
	 * @param clsk
	 */
	public Response(Socket clsk) {
		this.setClientSocket(clsk);
		try {
			pw = new PrintWriter(clsk.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the clientSocket
	 */
	public Socket getClientSocket() {
		return clientSocket;
	}

	/**
	 * 使用private，是为了让其socket只能同构构造方法构造注入
	 * 
	 * @param clientSocket
	 *            the clientSocket to set
	 */
	private void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * 向客服端反馈请求方法不接受的信息
	 * 
	 */
	public void outNotGet() {
		pw.println("HTTP/1.1 405 Method Not Allowed");
		pw.println();
	}

	/**
	 * 响应文件类型不支持信息
	 */
	public void outIllegalType() {
		pw.println("HTTP/1.1 404 Not Supported fileType");
		pw.println();
	}

	/**
	 * 向客服端反馈权限受限信息
	 * 
	 */
	public void outNoPower() {
		pw.println("HTTP/1.1 403 Forbidden");
		pw.println();
	}

	/**
	 * 向客服端反馈资源找不到信息
	 * 
	 */
	public void outNotExist() {
		pw.println("HTTP/1.1 404 Not Found");
		pw.println();
	}

	/**
	 * 向客服端反馈不支持的版本协议
	 * 
	 */
	public void outNotSupportVersion() {
		pw.println("HTTP/1.1 505 Version Not Supported ");
		pw.println();
	}

	/**
	 * 向客服端反馈请求的目录下的列表文件
	 * 
	 */
	public void outFileList(File file) {
		System.out.println("开始处理的filePath：" + file.getPath());
		System.out.println("开始处理的fileName：" + file.getName());
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		pw.println();
		pw.println("您好！您访问的目录下的所有文件如下:<br>");
		pw.println("<a href='javascript:history.go(-1)'>返回上级</a><br>");
		for (String str : file.list()) {
			
			String gen = "D:/";
			String href = gen ;
			if(str.indexOf('.')<0){
				href = str +  "/";
				pw.println("<a href='" + href + "'><font color = 'red'>" + str + "</font></a><br>");
			}else{
				href = str;
				pw.println("<a href='" + href + "'>" + str + "</a><br>");
			}			
			//拓展为有超链接的，点击可直接下载
		}
		pw.close();
	}

	public void outFile(File file) throws IOException {
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:application/x-msdownload;charset=UTF-8");
		pw.println();
		//打开此文件，将类容写入socket输出流
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] bytes = new byte[4096];
			while( in.read(bytes) != -1){
				System.out.println("读取的内容是："+new String(bytes));
				pw.println(new String(bytes));
			}
			pw.println();
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		}finally{
			if(pw!=null){
				pw.close();	
			}		
		}
	}

	public void outPreviewFile() {
	}

}
