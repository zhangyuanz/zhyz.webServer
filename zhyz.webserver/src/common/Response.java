package common;

import java.io.OutputStream;
import java.io.PrintStream;

public interface Response {
	
	public PrintStream getPrintStream();

	public void close();
	
	public OutputStream getOutputStream();

}
