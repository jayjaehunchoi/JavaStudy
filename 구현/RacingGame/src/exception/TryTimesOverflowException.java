package exception;

public class TryTimesOverflowException extends Exception{
	private String message;
	
	public TryTimesOverflowException(String message) {
		super(message);
	}

}
