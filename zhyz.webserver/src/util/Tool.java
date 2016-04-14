package util;

import expection.IllegalStringTypeException;

public class Tool {

	/**
	 * 判断文件是否包含在一个文件列表当中
	 * 
	 * @param fame
	 *            是文件名
	 * @return
	 */
	public static boolean contain(String[] fileLastNames, String fileName) {
		if (fileLastNames == null || fileName == null)
			return false;
		String lastName = fileName.substring(fileName.lastIndexOf('.') + 1,
				fileName.length());
		for (String temp : fileLastNames) {
			if (temp.equals(lastName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 忽略大小写字母条件下，判断一个文件是否包含在一个文件列表当中
	 * @param fileLastNames
	 * @param fileName
	 * @return
	 */
	public static boolean containIgnoreCaps(String[] fileLastNames,
			String fileName) {
		if (fileLastNames == null || fileName == null)
			return false;
		String lastName = fileName.substring(fileName.lastIndexOf('.') + 1,
				fileName.length());
		lastName = toSmall(lastName);
		for (String temp : fileLastNames) {
			if (temp.equals(lastName)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 将一个字符串转换成小写的
	 * 
	 * <pre>
	 * HTML = html
	 * HtmL = html
	 * JPG = jpg
	 * jPg = jpg
	 * </pre>
	 * 
	 * @param lastName
	 * @return
	 */
	public static String toSmall(String lastName) {
		StringBuilder sb = new StringBuilder();
		if (lastName != null) {
			char c;
			for (int i = 0; i < lastName.length(); i++) {
				c = lastName.charAt(i);
				if (Character.isUpperCase(c)) {
					sb.append(Character.toLowerCase(c));
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 解析Range头信息的start
	 * 
	 * @param range
	 *            是形如Range: bytes=10-80的字符串
	 * @throws IllegalStringTypeException
	 */
	public static int getRangeStart(String range)
			throws IllegalStringTypeException {

		if (range.startsWith("Range"))
			throw new IllegalStringTypeException("非Range头");
		int x = range.indexOf('=');
		int y = range.lastIndexOf('-');

		if (x + 1 == y)
			return -1;

		if (range.substring(range.indexOf(',') + 1, range.length())
				.equals("-1"))
			return 0;

		return Integer.valueOf(range.substring(x + 1, y));

	}

	/*
	 * 表示头500个字节：bytes=0-499 表示第二个500字节：bytes=500-999 表示最后500个字节：bytes=-500
	 * 表示500字节以后的范围：bytes=500- 第一个和最后一个字节：bytes=0-0,-1
	 * 同时指定几个范围：bytes=500-600,601-999//同时指定几个范围的先不考虑
	 */

	/**
	 * 解析Range头信息的end
	 * 
	 * @param range
	 *            是形如Range: bytes=10-80的字符串
	 * @throws IllegalStringTypeException
	 */
	public static int getRangeEnd(String range)
			throws IllegalStringTypeException {
		if (range.startsWith("Range"))
			throw new IllegalStringTypeException("非Range头");
		int y = range.lastIndexOf('-');

		if (y + 1 == range.length())
			return -1;

		if (range.substring(range.indexOf(',') + 1, range.length())
				.equals("-1"))
			return -1;

		return Integer.valueOf(range.substring(y + 1, range.length()));

	}
}
