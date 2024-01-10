package mancala;

import java.io.Serializable;
import java.util.ArrayList;

public class MancalaGame implements Serializable{
    private static final long serialVersionUID = -1632353211000768053L;

    private GameRules gameBoard;
    private Player currentPlayer;
    final private ArrayList<Player> players;
    private Player playerOne;
    private Player playerTwo;
    private UserProfile userOne;
    private UserProfile userTwo;
    private int gameRule;

    private static final int AYO_RULES = 2;

    private static final int PLAYER_ONE = 1;


    public MancalaGame(final Player onePlayer, final Player twoPlayer, int gameVersion){
        players = new ArrayList<>();
        players.add(onePlayer);
        players.add(twoPlayer);

        //defaults to kalah
        if (gameVersion == AYO_RULES){
            gameBoard = new AyoRules();
            onePlayer.getProfile().incrementNumAyoGames();
            twoPlayer.getProfile().incrementNumAyoGames();
        } else{
            gameBoard = new KalahRules();
            onePlayer.getProfile().incrementNumKalahGames();
            twoPlayer.getProfile().incrementNumKalahGames();
            setGameRules(1);
        }
        // gameBoard.initializeBoard();
        gameBoard.resetBoard();

        setPlayers(onePlayer, twoPlayer);
        gameBoard.registerPlayers(onePlayer, twoPlayer);

        currentPlayer = onePlayer;
    }

    public MancalaGame() {
        this(new Player(new UserProfile("User One")), new Player(new UserProfile("User Two")),1);
    }
    
    

    //gets the board for the game
    public GameRules getBoard(){
        return gameBoard;
    }

    //Gets the current player
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    //Gets the number of stones in a specific pit
    public int getNumStones(final int pitNum) throws PitNotFoundException {
        if (pitNum < 1 || pitNum > 12){
            throw new PitNotFoundException();
        }     
        return gameBoard.getNumStones(pitNum);
    }
    

    //Gets the players for the game
    public ArrayList<Player> getPlayers(){
        return players;
    }

    //Gets the total number of stones in a player's store
    public int getStoreCount(final Player player){
        if (player.equals(playerTwo)){
            return gameBoard.getStoreCount(2);
        } else {
            return gameBoard.getStoreCount(1);
        }
    }

    //Gets the winner of the game
    public Player getWinner() throws GameNotOverException {
        if (isGameOver()) {
            final int storeOneCount = gameBoard.getStoreCount(1);
            final int storeTwoCount = gameBoard.getStoreCount(2);

            //playerOne
            if (storeOneCount > storeTwoCount) {
                if (gameRule == PLAYER_ONE){
                    playerOne.getProfile().incrementNumKalahGamesWon();
                } else {
                    playerOne.getProfile().incrementNumAyoGamesWon();
                }
                return players.get(0); // Player 1 is the winner

            //playerTwo
            } else if (storeTwoCount > storeOneCount) {
                //Kalah
                if (gameRule == PLAYER_ONE){
                    playerTwo.getProfile().incrementNumKalahGamesWon();
                } else {
                    playerTwo.getProfile().incrementNumAyoGamesWon();
                }
                return players.get(1); // Player 2 is the winner

            } else {
                return null; // It's a tie
            }
        }else{
            throw new GameNotOverException();
        }
    }


    //Checks if the game is over
    public boolean isGameOver() {

        // Check if all pits on one side of the board are empty
        final boolean playerOneEmpty = gameBoard.isSideEmpty(1);
        final boolean playerTwoEmpty = gameBoard.isSideEmpty(7);

        // The game is over if one side is empty
        return playerOneEmpty || playerTwoEmpty;
    }


    //Makes a move for the current player
    public int move(final int startPit) throws InvalidMoveException {

        if (startPit < 1 || startPit > 12) {
            throw new InvalidMoveException();

        } else {
            int numOfStonesOnSide = 0;
            try {
                if (currentPlayer.equals(playerOne)) {
                    if (gameBoard.moveStones(startPit, 1) == -1){
                        //indicate that there is not an open spot
                        return -1;
                    }
                    numOfStonesOnSide = getSideStones(1);

                } else if (currentPlayer.equals(playerTwo)) {
                    if (gameBoard.moveStones(startPit, 2) == -1){
                        // if (gameBoard.moveStones(startPit + 6, 2) == -1){
                        //indicate that there is not an open spot
                        return -1;
                    }
                    numOfStonesOnSide = getSideStones(2);

                }

            } catch (InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
            return numOfStonesOnSide;
        }
    }

    //Sets the board for the game
    public void setBoard(final GameRules theBoard){
        gameBoard = theBoard;
    }

    //Sets the current player
    public void setCurrentPlayer(final Player player){
        currentPlayer = player;
    }

    //Sets the players for the game
    private void setPlayers(final Player onePlayer, final Player twoPlayer){
        playerOne = onePlayer;
        playerTwo = twoPlayer;

        currentPlayer = playerOne;
    }

    //Starts a new game by resetting the board
    public void startNewGame(){
        // gameBoard.registerPlayers(playerOne, playerTwo);
        if (gameRule == AYO_RULES){
            gameBoard = new AyoRules();
            playerOne.getProfile().incrementNumAyoGames();
            playerTwo.getProfile().incrementNumAyoGames();

        } else {
            gameBoard = new KalahRules();
            playerOne.getProfile().incrementNumKalahGames();
            playerTwo.getProfile().incrementNumKalahGames();

        }
        gameBoard.resetBoard();
        currentPlayer = playerOne;

    }

    //sets the game rule. 1 = Kalah, 2 = Ayo
    public void setGameRules(final int gameNum){
        gameRule = gameNum;
    }

    public boolean isFreeTurn(){
        return gameBoard.isFreeTurn();
    }

    public void switchPlayer() {
        if (currentPlayer.equals(playerOne)) {
            currentPlayer = playerTwo;
        } else {
            currentPlayer = playerOne;
        }
    }

    int getSideStones(final int playerNum){
        int numOfStones = 0;
        for (int i = 1; i < 7; i++){
            if (playerNum == PLAYER_ONE){
                numOfStones += gameBoard.getNumStones(i);
            } else if (playerNum == AYO_RULES){
                numOfStones += gameBoard.getNumStones(i + 6);
            }
        }
        return numOfStones;
    }


    //Generates a string representation of the game
    @Override
    public String toString() {
    final StringBuilder gameState = new StringBuilder();

    gameState.append(gameBoard);

    return gameState.toString();
    }


}
