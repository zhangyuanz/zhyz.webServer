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

	/**
	 * 将文件部分内容以自由读写方式，通过DataOutputStream形式写入输出流
	 * 
	 * @param file
	 * @param start
	 * @param end
	 * @param os
	 */
	public static boolean file2Socket(File file, long start, long end,OutputStream os) {
		if (os == null)
			return false;
		
		if (file == null || !file.exists() || file.isDirectory())
			return false;
		
		if (start < 0 || start > end || end < 0 )
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
			byte[] buffer;
			// total肯定是小于文件长度的数值，因为在一开始参数检查就排除了total大于文件长度情况
			if (total <= Integer.MAX_VALUE) {
				buffer = new byte[(int) total];
				raf.read(buffer);
				dis.write(buffer);
			} else {
				buffer = new byte[Integer.MAX_VALUE];
				raf.read(buffer);
				dis.write(buffer);
				raf.read(buffer, 0, (int) (total - Integer.MAX_VALUE));
				dis.write(buffer);
			}

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
	public static boolean file2Socket(File file, OutputStream os) {
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
			ByteBuffer buffer = ByteBuffer.allocate(1024);

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
