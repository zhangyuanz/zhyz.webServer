package junt4;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;

public class ClientDownloadByPartsTest {
	@Test
	public void client() throws HttpException, IOException {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod("http://localhost/d/test.txt");
		byte[] bytes = new byte[114];
		method.setRequestHeader("Range", "bytes=0-10");
		client.executeMethod(method);
		method.getResponseBodyAsStream().read(bytes, 0, 10);
		method.releaseConnection();

		method.setRequestHeader("Range", "bytes=10-80");
		client.executeMethod(method);
		method.getResponseBodyAsStream().read(bytes, 10, 80 - 10);
		method.releaseConnection();

		method.setRequestHeader("Range", "bytes=80-114");
		client.executeMethod(method);
		method.getResponseBodyAsStream().read(bytes, 80, 114 - 80);
		method.releaseConnection();
		// 打印服务器返回的状态
		// System.out.println(method.getStatusLine());;
		String text = "﻿这是一个测试用的文本，是为了测试java基础练习题一的file2buf函数的实现而存在的。";
		assertEquals(text, new String(bytes));

	}
}
