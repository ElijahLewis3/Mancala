package mancala;
import java.io.Serializable;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 4552785281373402655L;
    
    private String userName;
    private int numKalahGames;
    private int numAyoGames;
    private int numKalahGamesWon;
    private int numAyoGamesWon;

    // Constructors, getters, setters

    public UserProfile(final String name) {
        userName = name;
        numKalahGames = 0;
        numAyoGames = 0;
        numKalahGamesWon = 0;
        numAyoGamesWon = 0;
    }

    public UserProfile(){
        this("User Profile");
    }

    public String getUserName() {
        return userName;
    }

    public int getNumKalahGames() {
        return numKalahGames;
    }

    public void incrementNumKalahGames() {
        numKalahGames++;
    }

    public int getNumAyoGames() {
        return numAyoGames;
    }

    public void incrementNumAyoGames() {
        numAyoGames++;
    }

    public int getNumKalahGamesWon() {
        return numKalahGamesWon;
    }

    public void incrementNumKalahGamesWon() {
        numKalahGamesWon++;
    }

    public int getNumAyoGamesWon() {
        return numAyoGamesWon;
    }

    public void incrementNumAyoGamesWon() {
        numAyoGamesWon++;
    }

    public void setKalahGames(final int num) {
        numKalahGames = num;
    }

    public void setKalahGamesWon(final int num) {
        numKalahGamesWon = num;
    }

    public void setAyoGames(final int num) {
        numAyoGames = num;
    }

    public void setAyoGamesWon(final int num) {
        numAyoGamesWon = num;
    }

}
