package webServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
/**
 * 文件操作类，提供多种文件读写的借口
 * file2OutputStream方法将文件内容写入到输出流
 * fileList2OutputStream方法将文件夹的文件列表以超链接的形式输出到输出流中
 * @author xmubaga
 *
 */
public class FileOperator {
	final Logger logger = LoggerFactory.getLogger(FileOperator.class);
	/**
	 * 将文件的内容写入到一个socket
	 */
	public void file2Socket(File file,Socket clsk){
		FileInputStream in = null;
		DataOutputStream dis = null;
		try {
			in = new FileInputStream(file);
			dis = new DataOutputStream(clsk.getOutputStream());
			byte[] bytes = new byte[4096];
			logger.info("开始读取文件"+file.getName());
			while( in.read(bytes) != -1){
				dis.write(bytes);
			}
		} catch (FileNotFoundException e) {
			logger.info("文件未找到异常"+e.getLocalizedMessage());
		} catch (IOException e){
			logger.info(e.getLocalizedMessage());
		} finally{
			if (in != null){
				try{
					in.close();
				}catch(IOException e){
					logger.info("文件流关闭异常"+e.getLocalizedMessage());
				}
			}
		}		
	}

	public void fileList2Socket(File file,PrintStream pw){
		if(!file.isDirectory()){
			return;
		}
		
		logger.info("上级路径："+file.getPath());
		pw.println("<a href='javascript:history.go(-1)'>返回上级</a><br>");
		for (String str : file.list()) {
			String thisPath = file.toString().replace("\\", "/")+'/'+str;
			/*
			 * 在new File(path)中，
			 * D:/// = D:// = D:/;他们最后使用用getPath都是一样的D:\
			 * d:/kankan/ = d:/kankan ;他们使用getPath都是一样的d:\kankan
			 * d：/soft.txt = d:/soft.txt;他们最后的file或者file.toString 都是d：\soft.txt
			 * 就是说自动忽略多余的/
			 */
			
			if(new File(thisPath).isDirectory()){
				pw.println("<a href='" + str + "/'><font color = 'red'>" + str + "</font></a><br>");
			}else{
				pw.println("<a href='" + str + "/'>" + str + "</a><br>");
			}			
		}
	}

}