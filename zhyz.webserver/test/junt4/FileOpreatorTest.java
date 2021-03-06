package junt4;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.junit.Test;

import util.FileOpreator;

public class FileOpreatorTest {
	/**
	 * 测试把文件内容写入到输出流中的方法file2OutputStream(file,os)
	 * 
	 * <pre>
	 * 通过一个文件来构造输入流，传入file2OutputStream中，这样不必构造socket
	 * 然后再把文件内容读取出来，和源文件内容断言
	 * </pre>
	 * 
	 * @throws IOException
	 */
	@Test
	public void testFile2Socket() throws IOException {
		File file = new File("D:/test.txt");

		File test = new File("D:/test2.txt");
		OutputStream os = new FileOutputStream(test);
		FileOpreator.file2OutputStream(file, os);
		DataInputStream dis = new DataInputStream(new FileInputStream(test));
		byte[] buffer = new byte[114];
		dis.read(buffer);

		dis.close();
		os.close();

		String text = new String(buffer);
		String expect = "﻿这是一个测试用的文本，是为了测试java基础练习题一的file2buf函数的实现而存在的。";
		System.out.println(text);
		assertEquals(expect, text);
	}

	/**
	 * 测试把文件部分数据写入到输出流的函数file2OutputStream(file,start,end,os)
	 * 将一个文件的内容分两次写入到os输出流到test3文件， 然后再把其内容读取出来，和源文件内容进行断言
	 * 
	 * @throws IOException
	 */
	@Test
	public void testFile2SocketByPart() throws IOException {
		File file = new File("D:/test.txt");
		File test = new File("D:/test2.txt");
		OutputStream os = new FileOutputStream(test);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(test)));
		FileOpreator.file2OutputStream(file, 0, 12, os);
		System.out.println();
		String body1 = br.readLine();
		System.out.println(body1);
		FileOpreator.file2OutputStream(file, 12, 114, os);
		System.out.println();
		String body2 = br.readLine();
		System.out.println(body2);

		br.close();
		os.close();

		String text = body1.substring(0, 4) + body2.substring(0, 42);
		String expect = "﻿这是一个测试用的文本，是为了测试java基础练习题一的file2buf函数的实现而存在的。";
		System.out.println(text);
		assertEquals(expect, text);
	}

	/**
	 * 测试不合法的输入参数
	 * 
	 * @throws IOException
	 */
	@Test
	public void testIllegalParam() throws IOException {
		File file;
		File test = new File("D:/test2.txt");
		OutputStream os = new FileOutputStream(test);
		/************************* 文件找不到 **************************/
		file = new File("D:/eeeeeeee.txt");
		assertTrue(!FileOpreator.file2OutputStream(file, os));
		/************************* 文件只是一个目录 **********************/
		file = new File("D:/");
		assertTrue(!FileOpreator.file2OutputStream(file, os));
		/************************* file空参数 *************************/
		assertTrue(!FileOpreator.file2OutputStream(null, os));
		/************************* os空参数 ***************************/
		assertTrue(!FileOpreator.file2OutputStream(file, null));
		/********************* start,end 自身通不过检查 *****************/
		file = new File("D:/test.txt");
		assertTrue(!FileOpreator.file2OutputStream(file, -1, 10, null));
		assertTrue(!FileOpreator.file2OutputStream(file, -2, 10, null));
		assertTrue(!FileOpreator.file2OutputStream(file, 5, -1, null));
		assertTrue(!FileOpreator.file2OutputStream(file, 10, 5, null));
		/********************* start，end与file.length比较 ************/
		assertTrue(!FileOpreator.file2OutputStream(file, 10, 10000, null));
		assertTrue(!FileOpreator.file2OutputStream(file, 10000, 10, null));
	}
}