package test;

import static org.junit.Assert.*;

import org.junit.Test;

import webServer.RequestAnalyze;

public class RequestAnalyzeTest {
	RequestAnalyze requestAly  = new RequestAnalyze();
	@Test
	public void test() {
		
		//requestAly.analyzeFirstLine("GET /d/a/b.txt HTTP/1.1");
		assertEquals("GET", requestAly.getMethod());
		assertEquals("/d/a/b.txt", requestAly.getRequestURL());
		assertEquals("HTTP/1.1", requestAly.getProtocol());
	}
	@Test
	public void testCut(){
		;
		//assertEquals("localhost", requestAly.cut("Host: localhost", "Host:"));
	}
}
