package exception;

public class NameOverflowException extends Exception{
	private String message;
	
	public NameOverflowException(String message) {
		super(message);
	}

}
