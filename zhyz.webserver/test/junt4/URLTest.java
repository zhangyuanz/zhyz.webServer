package junt4;

import static org.junit.Assert.*;

import org.junit.Test;

import util.URI;

public class URLTest {
	private URI url1 = new URI("/d/temp.jpg");
	private URI url2 = new URI();
	private URI url3 = new URI("/");

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
