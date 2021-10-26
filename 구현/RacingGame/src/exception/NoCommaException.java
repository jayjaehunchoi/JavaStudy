package exception;

public class NoCommaException extends Exception{
	private String message;
	
	public NoCommaException(String message) {
		super(message);
	}

}
