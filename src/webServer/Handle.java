package webServer;

import java.net.Socket;

public class Handle implements Runnable {
	private Socket clientSocket;

	public Handle(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		handleRequest();	
	}

	private void handleRequest(){
		RequestAnalyze request = new RequestAnalyze(clientSocket);
		if(!request.getMethod().equals("GET")){
			//outNotGet();
		}

	}
}
