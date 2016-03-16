package webServer;

import java.io.File;
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

	private void handleRequest() {
		RequestAnalyze request = new RequestAnalyze(clientSocket);
		Response response = new Response(clientSocket);
		// 非GET方法
		if (!request.getMethod().equals("GET")) {
			response.outNotGet();
			// thow 异常？
			return;
		}
		String uri = request.getRequestURI();
		// 非D盘
		if (!(uri.substring(1, 2).equals('d'))) {
			response.outNoPower();
			return;
		}
		uri = uri.replace("/d/", "d:/");
		File file = new File(uri);
		// 只是一个目录
		if (file.isDirectory()) {
			response.outFileList();
		} else {
			// 文件不存在
			if (!file.exists()) {
				response.outNotExist();
			} else {
				// 非静态文件
				if (!isStaticFile(file.getName())) {
					response.outIllegalType();
				} else {
					//if(预览参数为ture) 输入预览信息outPreview
					//否则输入下载信息outFile
					response.outFile();
				}
			}
		}

	}
	/**
	 * 判断文件是否为静态文件
	 * 
	 * @param str
	 * @return
	 */
	private boolean isStaticFile(String str) {
		if (str.endsWith(".txt") || str.endsWith(".docx")
				|| str.endsWith(".zip") || str.endsWith(".html")
				|| str.endsWith(".xls") || str.endsWith(".pdf")
				|| str.endsWith(".jpg") || str.endsWith(".png")
				|| str.endsWith(".rar")) {
			return true;
		} else {
			return false;
		}
	}
}
