package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Handle implements Runnable {
	private Socket clientSocket;

	public Handle(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			handleRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleRequest() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String line = null;
		while ((line = in.readLine()) != null) {
			System.out.println("客服端请求的数据是：" + line);
			if (line.equals(""))
				break;
		}
		in.close();
		clientSocket.close();

	}
}
