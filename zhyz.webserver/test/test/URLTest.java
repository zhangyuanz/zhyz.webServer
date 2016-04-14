package test;

import static org.junit.Assert.*;

import org.junit.Test;

import util.URL;

public class URLTest {
	private URL url1 = new URL("/d/temp.jpg");
	private URL url2 = new URL();
	private URL url3 = new URL("/");

	@Test
	public void testGetString() {
		assertEquals("d/temp.jpg", url1.getString());
		assertNull(url2.getString());
		assertEquals("", url3.getString());
	}

	@Test
	public void testGetRoot() {
		assertEquals("d", url1.getRoot());
		assertNull(url2.getRoot());
		assertEquals("d", url3.getRoot());
	}

	@Test
	public void testToPath() {
		assertEquals("d:/temp.jpg", url1.toPath());
		assertNull(url2.toPath());
		assertEquals("d:/", url3.toPath());
	}
}
