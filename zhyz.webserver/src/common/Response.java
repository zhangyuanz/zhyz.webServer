package common;

import java.io.File;
import java.io.PrintStream;

public interface Response {
	public PrintStream getPrintStream();

	public void closeSocket();

	public void file2Socket(File file);

	public void file2Socket(File file, long start, long end);
}
