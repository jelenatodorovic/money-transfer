package exception;

public class NotEnoughAccountBalanceException extends Exception {

    public NotEnoughAccountBalanceException(String message) {
        super(message);
    }

    public NotEnoughAccountBalanceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
