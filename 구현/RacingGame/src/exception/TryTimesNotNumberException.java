package exception;

public class TryTimesNotNumberException extends Exception{
	private String message;
	
	public TryTimesNotNumberException(String message) {
		super(message);
	}

}
