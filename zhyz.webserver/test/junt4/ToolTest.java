package junt4;

import static org.junit.Assert.*;

import org.junit.Test;

import expection.IllegalStringTypeException;
import util.Tool;

public class ToolTest {

	@Test
	public void testContain() {
		String[] files = { "jpg", "txt", "zip" };
		assertTrue(Tool.contain(files, "zhyz.jpg"));
		assertTrue(Tool.contain(files, "zhyz.34.jpg"));
		assertTrue(Tool.contain(files, "zhyz.txt"));
		assertTrue(Tool.contain(files, "zhyz.zip"));
	}
	/**
	 * 测试contain方法的空参数
	 */
	@Test
	public void testContainNull() {
		String[] files = { "doc", "exe" };
		assertTrue(!Tool.contain(null, "zhyz.jpg"));
		assertTrue(!Tool.contain(files, null));
	}
	/**
	 * 测试忽略大小写的判断文件名是否为指定文件后缀名列表当中的方法
	 */
	@Test
	public void testContainLgnoreCaps(){
		String[] files = { "jpg", "txt", "zip" };
		assertTrue(Tool.containIgnoreCaps(files, "zhyz.jpg"));
		assertTrue(Tool.containIgnoreCaps(files, "zhyz.Jpg"));
		assertTrue(Tool.containIgnoreCaps(files, "zhyz.JPG"));
		assertTrue(Tool.containIgnoreCaps(files, "succez.zhyz.jpg"));
		assertTrue(Tool.containIgnoreCaps(files, "zhyz.txt"));
		assertTrue(Tool.containIgnoreCaps(files, "zhyz.Zip"));
	}
	/**
	 * 测试工具类中getRangeStart方法
	 * @throws IllegalStringTypeException
	 */
	@Test
	public void testStart() throws IllegalStringTypeException {
		/*
		 * 表示头500个字节：bytes=0-499 表示第二个500字节：bytes=500-999 表示最后500个字节：bytes=-500
		 * 表示500字节以后的范围：bytes=500- 第一个和最后一个字节：bytes=0-0,-1
		 * 同时指定几个范围：bytes=500-600,601-999
		 */
		String s1 = "bytes=0-499";
		String s2 = "bytes=-500";
		String s3 = "bytes=500-";
		String s4 = "bytes=0-0,-1";

		int start1 = Tool.getRangeStart(s1);
		int start2 = Tool.getRangeStart(s2);
		int start3 = Tool.getRangeStart(s3);
		int start4 = Tool.getRangeStart(s4);

		assertEquals(0, start1);
		assertEquals(-1, start2);// -1代表未指定start
		assertEquals(500, start3);
		assertEquals(0, start4);
	}
	/**
	 * 测试工具类中的gerRangeEnd方法
	 * @throws IllegalStringTypeException
	 */
	@Test
	public void testEnd() throws IllegalStringTypeException {
		String s1 = "bytes=0-499";
		String s2 = "bytes=-500";
		String s3 = "bytes=500-";
		String s4 = "bytes=0-0,-1";

		int end1 = Tool.getRangeEnd(s1);
		int end2 = Tool.getRangeEnd(s2);
		int end3 = Tool.getRangeEnd(s3);
		int end4 = Tool.getRangeEnd(s4);

		assertEquals(499, end1);
		assertEquals(500, end2);
		assertEquals(-1, end3);// -1代表到文件末尾截止
		assertEquals(-1, end4);

	}
}
