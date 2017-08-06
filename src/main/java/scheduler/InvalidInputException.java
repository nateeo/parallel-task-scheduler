package scheduler;

/**
 * Created by edisonrho on 6/08/17.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException() {}

    public InvalidInputException(String message){
        super(message);
    }
}
