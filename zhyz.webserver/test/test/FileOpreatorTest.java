package test;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import util.FileOpreator;

public class FileOpreatorTest {

	@Test
	public void testFile2Socket() throws IOException {
		File file = new File("D:/test.txt");
		
		File test = new File("D:/test3.txt");	
		OutputStream os = new FileOutputStream(test);
		FileOpreator.file2Socket(file, os);	
		
		@SuppressWarnings("resource")
		DataInputStream dis = new DataInputStream(new FileInputStream(test));
		byte[] buffer = new byte[114];
		dis.read(buffer);
		String text = new String(buffer);
		String expect = "﻿这是一个测试用的文本，是为了测试java基础练习题一的file2buf函数的实现而存在的。";
		System.out.println(text);
		assertEquals(expect, text);
	}
	@Test
	public void testFile2SocketByPart() throws IOException{
		File file = new File("D:/test.txt");
		File test = new File("D:/test3.txt");
		OutputStream os = new FileOutputStream(test);
		FileOpreator.file2Socket(file, 0, 20, os);
		FileOpreator.file2Socket(file, 20, 114, os);
		@SuppressWarnings("resource")
		DataInputStream dis = new DataInputStream(new FileInputStream(test));
		byte[] buffer = new byte[114];
		dis.read(buffer);
		String text = new String(buffer);
		String expect = "﻿这是一个测试用的文本，是为了测试java基础练习题一的file2buf函数的实现而存在的。";
		System.out.println(text);
		assertEquals(expect, text);
	}
}