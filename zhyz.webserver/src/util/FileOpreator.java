package util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileOpreator {
	final static Logger logger = LoggerFactory.getLogger(FileOpreator.class);
	final static int bufferSize = 4096;
	/**
	 * 将文件部分内容以自由读写方式，通过DataOutputStream形式写入输出流
	 * 
	 * @param file
	 * @param start
	 * @param end
	 * @param os
	 */
	public static boolean file2OutputStream(File file, long start, long end,
			OutputStream os) {
		if (os == null)
			return false;

		if (file == null || !file.exists() || file.isDirectory())
			return false;

		if (start < 0 || start > end || end < 0)
			return false;

		long length = file.length();
		if (start > length || end > length || end - start > length)
			return false;

		// 例如：range:bytes=-500代表最后500个字节
		if (start == -1) {
			start = file.length() - end;
			end = file.length();
		} else // 例如：range:bytes=400-代表400字节以后的全部数据，range:bytes=0-0,-1代表全部字节
		if (end == -1)
			end = file.length();

		RandomAccessFile raf = null;
		DataOutputStream dis = null;
		try {
			raf = new RandomAccessFile(file, "r");
			dis = new DataOutputStream(os);
			raf.seek(start);
			long total = end - start;
			//经调查研究，read(b)原则上每次一定会读满，返回值等于b的长度，只是在文件末尾时，可能读不满，返回实际读的长度，如果恰巧最后一次也读满，则再次读取会返回-1
			//误区：以为每次read的时候都有可能得到小于b长度的返回值，而实际上只有到达文件末尾时，才有能出现小于b长度的返回值
			byte[] buffer = new byte[bufferSize];
			int readTimes = (int) (total / bufferSize);
			for (int i = 0; i < readTimes; i++) {
				raf.read(buffer);
				dis.write(buffer);
			}
			raf.read(buffer);
			dis.write(buffer, 0, (int) (total % bufferSize));
			/*
			long n = 0;
			long sum = 0;
			while((n = raf.read(buffer)) != -1){
				sum = sum + n;
				dis.write(buffer);
				if(sum + 4096 >total){}
					break;
			}
			int readBytes = raf.read(buffer);
			System.out.println("readBytes:"+readBytes);
			dis.write(buffer, 0, (int) (total - sum));
			*/
			/*
			 * 20160415-am-11:40
			 * issues：read(b),read(b,off,len)方法返回int代表实际读取的字节数，也就是说在一开始，无法确定读取的字节数
			 * 每次向4096个字节数组中读取入数据，而实际读取的字节个数n并不确定是多少，
			 * sum的值并不一定能够=total，可能直到文件结束，while才结束，这样就失去了end的意义，出错
			 * 改为一个字节一个字节的读取
			byte[] buffer = new byte[4096];
			long n = 0;
			long sum = 0;
			while((n = raf.read(buffer)) != -1){
				sum = sum +n;
				dis.write(buffer);
				if(sum == total)
					break;
			}
			 */
			/*
			int b = 0;
			int sum = 0;
			while(b!=-1){
				b =raf.read();
				dis.write(b);
				sum++;
				if(sum == total)
					break;
			}
			*/
			return true;
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
			return false;
		} finally {
			try {
				if (raf != null)
					raf.close();
			} catch (IOException e) {
				logger.warn(e.getLocalizedMessage());
			}
		}

	}

	/**
	 * 将文件以NIO的方式通过DataOutputStream形式写入输出流
	 * 
	 * @param file
	 * @param os
	 * @return
	 */
	public static boolean file2OutputStream(File file, OutputStream os) {
		if (file == null || !file.exists() || file.isDirectory())
			return false;
		if (os == null)
			return false;

		FileInputStream in = null;
		DataOutputStream dis = null;
		FileChannel fcin = null;
		try {
			in = new FileInputStream(file);
			fcin = in.getChannel();
			dis = new DataOutputStream(os);
			ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
			while (fcin.read(buffer) != -1) {
				dis.write(buffer.array());
				buffer.clear();
			}
			return true;
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.warn("文件流关闭异常" + e.getLocalizedMessage());
				}
			}
		}
	}
}
