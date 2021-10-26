package exception;

public class NameDuplicatedException extends Exception{
	private String message;
	
	public NameDuplicatedException(String message) {
		super(message);
	}

}
