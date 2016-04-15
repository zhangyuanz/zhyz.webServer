package common;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public interface Response {
	/**
	 * 获取socket的打印输出流
	 * 
	 * @return
	 * @throws IOException 
	 */
	public PrintStream getPrintStream() throws IOException;

	/**
	 * 关闭socket，释放资源
	 */
	public void close();

	/**
	 * 获取socket的输出流
	 * 
	 * @return
	 * @throws IOException 
	 */
	public OutputStream getOutputStream() throws IOException;

}
