package clinic;

// Module 4: user-defined CHECKED exception (extends Exception, not RuntimeException)
public class InvalidAppointmentException extends Exception {
    public InvalidAppointmentException(String message) {
        super(message);
    }
}
