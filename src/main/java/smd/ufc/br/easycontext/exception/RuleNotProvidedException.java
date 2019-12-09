package smd.ufc.br.easycontext.exception;

public class RuleNotProvidedException extends RuntimeException {
    public RuleNotProvidedException() {
    }

    public RuleNotProvidedException(String message) {
        super(message);
    }

    public RuleNotProvidedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleNotProvidedException(Throwable cause) {
        super(cause);
    }
}
