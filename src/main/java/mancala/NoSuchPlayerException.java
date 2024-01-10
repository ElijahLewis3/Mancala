package mancala;

public class NoSuchPlayerException extends Exception {
    private static final long serialVersionUID = 7791337417655979794L;
    public NoSuchPlayerException() {
        super("Player not found.");
    }

    public NoSuchPlayerException(final String message) {
        super(message);
    }
}