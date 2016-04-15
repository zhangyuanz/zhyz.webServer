package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作类，提供多种文件读写的借口 file2OutputStream方法将文件内容写入到输出流
 * fileList2OutputStream方法将文件夹的文件列表以超链接的形式输出到输出流中
 * 
 * @author xmubaga
 *
 */
public class Response implements common.Response {
	final Logger logger = LoggerFactory.getLogger(Response.class);
	private Socket clsk = null;
	private PrintStream pw = null;
	private OutputStream os = null;

	private Response(Socket client) {
		this.clsk = client;
	}

	public static Response getInstance(Socket client) {
		return new Response(client);
	}

	/**
	 * 获取socket的打印流
	 * 
	 * @return
	 * @throws IOException 
	 */
	@Override
	public PrintStream getPrintStream() throws IOException {
		if (this.pw == null)
			return new PrintStream(this.clsk.getOutputStream());
		else
			return this.pw;
	}

	/**
	 * 关闭客服端socket
	 */
	@Override
	public void close() {
		try {
			if (clsk != null)
				clsk.close();
		} catch (IOException e) {
			logger.warn("服務器未能关闭socket");
		}

	}

	/**
	 * 获取客服端socket的输出流
	 * 
	 * @return
	 * @throws IOException
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		if (this.os == null)
			return this.clsk.getOutputStream();
		else
			return this.os;
	}

}