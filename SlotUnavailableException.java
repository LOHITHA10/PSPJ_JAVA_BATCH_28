package clinic;

// Module 4: another checked exception, used with multi-catch in the caller
public class SlotUnavailableException extends Exception {
    public SlotUnavailableException(String message) {
        super(message);
    }
}
