package net.zoostar.hw.validate;

public class MissingRequiredFieldException extends ValidatorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MissingRequiredFieldException(String message) {
		super(message);
	}

}
