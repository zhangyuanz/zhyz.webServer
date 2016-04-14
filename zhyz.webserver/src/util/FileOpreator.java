package util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileOpreator {
	final static Logger logger = LoggerFactory.getLogger(FileOpreator.class);
	/**
	 * 此处buffer的大小恐怕不够客服端请求的字节长度，明日需优化 并且要对start，end进行检查 20160408
	 */
	public static void file2Socket(File file, long start, long end, Socket clsk) {
		if (file == null || !file.exists() || file.isDirectory())
			return;
		if (clsk == null || clsk.isClosed())
			return;
		if (clsk == null || clsk.isClosed())
			return;
		if (start < 0 || end > file.length() || end - start > file.length())
			return;
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
			dis = new DataOutputStream(clsk.getOutputStream());
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

		} catch (FileNotFoundException e) {
			logger.error("文件未找到" + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	/**
	 * 将文件写入socket
	 * 
	 * @param file
	 */
	public static void file2Socket(File file, Socket clsk) {
		if (file == null || !file.exists() || file.isDirectory())
			return;
		if (clsk == null || clsk.isClosed())
			return;
		if (clsk == null || clsk.isClosed())
			return;

		FileInputStream in = null;
		DataOutputStream dis = null;
		FileChannel fcin = null;
		try {
			in = new FileInputStream(file);
			fcin = in.getChannel();
			dis = new DataOutputStream(clsk.getOutputStream());
			// byte[] bytes = new byte[4096];
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (fcin.read(buffer) != -1) {
				dis.write(buffer.array());
				buffer.clear();
			}

		} catch (FileNotFoundException e) {
			logger.error("文件未找到异常" + e.getLocalizedMessage());
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
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
