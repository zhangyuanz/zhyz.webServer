package webServer;

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
	public void outNotSupportVersion(){
		pw.println("HTTP/1.1 505 Version Not Supported ");
		pw.println();
	}
	/**
	 * 向客服端反馈请求的目录下的列表文件
	 * 
	 */
	public void outFileList() {
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		pw.println();
	}

	public void outFile() {
	}

	public void outPreviewFile() {
	}

}
