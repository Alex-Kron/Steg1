package marker;

public class MarkerException extends RuntimeException{
    public MarkerException() {
        super("The length of the message is greater than the capacity of the container.");
    }
}
