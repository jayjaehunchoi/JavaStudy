package study.validation.util.exception;

public class DuplicateIdException extends RuntimeException{
    public DuplicateIdException(String message) {
        super(message);
    }
}