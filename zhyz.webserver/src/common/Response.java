package common;

import java.io.PrintStream;
import java.net.Socket;

public interface Response {
	
	public PrintStream getPrintStream();

	public void close();
	
	public Socket getSocket();

}
