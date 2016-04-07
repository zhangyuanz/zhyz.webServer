package server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.Tool;

/**
 * 文件操作类，提供多种文件读写的借口 file2OutputStream方法将文件内容写入到输出流
 * fileList2OutputStream方法将文件夹的文件列表以超链接的形式输出到输出流中
 * 
 * @author xmubaga
 *
 */
public class Response0 {
	final Logger logger = LoggerFactory.getLogger(Response0.class);
	private Socket clsk;
	PrintStream pw;
	private Response0(Socket client){
		this.clsk = client;
		this.pw = Tool.getPrint(client);
	}
	public static  Response0 getInstance(Socket client){
		return new Response0(client);
	}
	/**
	 * 下載文件
	 * @param file
	 */
	public void downloadFile(File file) {
		logger.info("开始download文件" + file.getName());
		pw.println("HTTP/1.1 200 OK");
		pw.print("Content-Disposition:attachment;filename=");
		pw.println(file.getName());
		pw.println("Content-Type:application/octet-stream;charset=UTF-8");
		pw.print("Content-Length:");
		pw.println(file.length());
		pw.println();
		this.file2Socket(file);
		pw.println();
		logger.info("download完毕！");
	}
	/**
	 * 预览文件
	 * @param file
	 */
	public void privewFile(File file){
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		pw.println();
		this.file2Socket(file);
		pw.println();
	}
	/**
	 * 预览图片
	 */
	public void privewImage(File file){
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		pw.println();
		pw.print("<img src='../");
		pw.print(file.getName());
		pw.println("'/>");
		pw.println();
	}
	/**
	 * 将文件写入socket
	 * @param file
	 */
	private void file2Socket(File file){
		if (file == null || !file.exists() || file.isDirectory())
			return;
		if(clsk == null || clsk.isClosed())
			return;
		
		FileInputStream in = null;
		DataOutputStream dis = null;
		try {
			in = new FileInputStream(file);
			dis = new DataOutputStream(clsk.getOutputStream());
			byte[] bytes = new byte[4096];
			
			while (in.read(bytes) != -1) {
				dis.write(bytes);
			}

		} catch (FileNotFoundException e) {
			logger.info("文件未找到异常" + e.getLocalizedMessage());
		} catch (IOException e) {
			logger.info(e.getLocalizedMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.info("文件流关闭异常" + e.getLocalizedMessage());
				}
			}
		}
	}
	/**
	 * 显示目录下的文件列表
	 * @param file
	 */
 	public void listOfFile(File file) {
		if (file == null || !file.exists() || !file.isDirectory()) 
			return;
		logger.info("开始响应文件列表");
		pw.println("HTTP/1.1 200 OK");
		pw.println("Content-Type:text/html;charset=UTF-8");
		pw.println();
		pw.println("<a href='javascript:history.go(-1)'>返回上级</a><br>");
		for (String str : file.list()) {
			String thisPath = file.toString().replace("\\", "/") + '/' + str;
			if (new File(thisPath).isDirectory()) {
				pw.print("<a href='");
				pw.print(str);
				pw.print("/'><font color = 'red'>");
				pw.print(str);
				pw.print("</font></a><br>");
				pw.println();
			} else {
				pw.print("<a href='");
				pw.print(str);
				pw.print("/'>");
				pw.print(str);
				pw.print("</a><br>");
				pw.println();
			}
		}
		logger.info("响应完毕！");
	}

}