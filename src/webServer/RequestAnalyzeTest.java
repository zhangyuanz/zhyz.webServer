package webServer;

import static org.junit.Assert.*;

import org.junit.Test;

public class RequestAnalyzeTest {

	@Test
	public void test() {
		RequestAnalyze requestAly  = new RequestAnalyze();
		requestAly.analyzeFirstLine("GET /gxy3509394/article/details/7899923 HTTP/1.1");
		assertEquals("GET", requestAly.getMethod());
		assertEquals("/gxy3509394/article/details/7899923", requestAly.getRequestURI());
		assertEquals("HTTP/1.1", requestAly.getProtocol());
	}

}
