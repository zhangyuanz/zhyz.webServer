package expection;

public class IllegalStringTypeException extends Throwable {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认构造函数
	 */
	public IllegalStringTypeException() {
		super();
	}

	/**
	 * 构造函数
	 * 
	 * @param arg
	 */
	public IllegalStringTypeException(String arg) {
		super(arg);
	}
}
