/**
 * Exception thrown when the data is bad.
 * @author Mukund Venkatesh
 * @version November 1, 2024
 */
public class BadDataException extends Exception implements ExceptionInterface {
    public BadDataException(String message) {
        super(message);
    }
}
