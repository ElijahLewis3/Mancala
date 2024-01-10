package mancala;

public class GameNotOverException extends Exception {
    private static final long serialVersionUID = -1894883535467494497L;
    public GameNotOverException() {
        super("Game not over.");
    }
    
    public GameNotOverException(final String message) {
        super(message);
    }
}