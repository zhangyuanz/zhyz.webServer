package webServer;

import static org.junit.Assert.*;

import org.junit.Test;

public class RequestAnalyzeTest {

	@Test
	public void test() {
		RequestAnalyze requestAly  = new RequestAnalyze();
		requestAly.analyzeFirstLine("GET /d/a/b.txt HTTP/1.1");
		assertEquals("GET", requestAly.getMethod());
		assertEquals("/d/a/b.txt", requestAly.getRequestURL());
		assertEquals("HTTP/1.1", requestAly.getProtocol());
	}

}
