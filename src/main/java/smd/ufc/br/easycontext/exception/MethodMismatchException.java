package smd.ufc.br.easycontext.exception;

public class MethodMismatchException extends RuntimeException {
    public MethodMismatchException() {
        super();
    }

    public MethodMismatchException(String message) {
        super(message);
    }

    public MethodMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodMismatchException(Throwable cause) {
        super(cause);
    }
}
