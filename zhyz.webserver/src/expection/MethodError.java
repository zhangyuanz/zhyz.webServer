package expection;

public class MethodError extends Throwable {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认构造函数
	 */
	public MethodError() {
		super();
	}

	/**
	 * 构造函数
	 * 
	 * @param arg
	 */
	public MethodError(String arg) {
		super(arg);
	}
}
