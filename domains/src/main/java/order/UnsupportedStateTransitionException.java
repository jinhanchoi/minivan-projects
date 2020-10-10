package order;

public class UnsupportedStateTransitionException extends RuntimeException {
    public UnsupportedStateTransitionException(Enum state){
        super("Current state: " + state);
    }
}
