package exception;

public class NameNotExistedException extends Exception{
	private String message;
	
	public NameNotExistedException(String message) {
		super(message);
	}

}
