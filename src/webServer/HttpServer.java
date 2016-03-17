package webServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer implements Runnable{
	private final int PORT = 80;
    private ExecutorService pool;
    private ServerSocket serverSocket;
   
    private  void init(){
    	try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("无法启动HTTP服务器:"+e.getLocalizedMessage());
		}
    	pool = Executors.newFixedThreadPool(64);
    }
	public HttpServer(){
		init();
		new Thread(this).start();
        System.out.println("HTTP服务器正在运行,端口:"+PORT);
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Socket client = serverSocket.accept();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println(df.format(new Date())+"有客服端连接进来了..");
				Handle handle = new Handle(client);
				pool.execute(handle);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	protected void close(){
		
	}
}
