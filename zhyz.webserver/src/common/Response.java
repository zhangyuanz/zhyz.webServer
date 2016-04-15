package common;

import java.io.OutputStream;
import java.io.PrintStream;

public interface Response {
	/**
	 * 获取socket的打印输出流
	 * 
	 * @return
	 */
	public PrintStream getPrintStream();

	/**
	 * 关闭socket，释放资源
	 */
	public void close();

	/**
	 * 获取socket的输出流
	 * 
	 * @return
	 */
	public OutputStream getOutputStream();

}
