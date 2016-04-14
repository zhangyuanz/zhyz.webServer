package junt4;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

public class ClientKeepAliveTest {

	private Socket client;

	/**
	 * 在一次个TCP链接中发送两次请求，仍能得到正确结果
	 * 特殊值测试，指定请求的内容，/d/test.txt;并硬性合并两次请求的响应体字符串为断言实际值，不符一般性原则
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testKeepAlive() throws UnknownHostException, IOException,
			InterruptedException {
		client = new Socket("127.0.0.1", 80);
		PrintStream pw = new PrintStream(client.getOutputStream(), true);
		BufferedReader is = new BufferedReader(new InputStreamReader(
				client.getInputStream()));

		pw.println("GET /d/test.txt HTTP/1.1");
		pw.println("Range: bytes=0-12");
		pw.println();

		String response1;
		while (true) {
			response1 = is.readLine();
			System.out.println(response1);
			if (response1.isEmpty())
				break;
		}
		String body1 = is.readLine();
		System.out.println(body1);

		Thread.sleep(200);
		pw.println("GET /d/test.txt HTTP/1.1");
		pw.println("Range: bytes=12-114");
		pw.println();

		String response2;
		while (true) {
			response2 = is.readLine();
			System.out.println(response2);
			if (response2.isEmpty())
				break;
		}

		String body2 = is.readLine();
		System.out.println(body2);

		client.close();

		String atual = body1 + body2;
		String text = "﻿这是一个测试用的文本，是为了测试java基础练习题一的file2buf函数的实现而存在的。";
		assertEquals(text, atual);

	}

}