package mancala;

import java.io.Serializable;

/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable{
    private static final long serialVersionUID = 4212384230347169112L;

    final private MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)
    private Boolean freeTurn;

    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;

    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        gameBoard = new MancalaDataStructure();
        freeTurn = false;
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    /* default */ MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    /* default */ boolean isSideEmpty(final int pitNum){
        //player1's side
        boolean isEmpty = true;
        if (pitNum >= 1 && pitNum <=6){
            for (int i = 1; i <= 6; i++){
                if (gameBoard.getNumStones(i) > 0){
                    // return false;
                    isEmpty = false;
                }
            }
        } else{
            for (int i = 7; i <= 12; i++){
                if (gameBoard.getNumStones(i) > 0){
                    // return false;
                    isEmpty = false;
                }
            }
        }

        return isEmpty;
        // This method can be implemented in the abstract class.

    }

    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    public void setPlayer(final int playerNum) {
        currentPlayer = playerNum;
    }


    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    /* default */ public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    /* default */ abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    /* default */ abstract int captureStones(int stoppingPoint);

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    //!
    public void registerPlayers(final Player one, final Player two) {
        // this method can be implemented in the abstract class.
        final Store storeOne = new Store();
        final Store storeTwo = new Store();

        one.setStore(storeOne);
        two.setStore(storeTwo);

        /* make a new store in this method, set the owner
         then use the setStore(store,playerNum) method of the data structure*/

         gameBoard.setStore(storeOne, 1);
         gameBoard.setStore(storeTwo, 2);
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        for (int i = 1; i <= 12; i++){
            gameBoard.removeStones(i);
        }
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    public int getSideStones(final int player){
        int numStones = 0;
        for (int i = 1; i <= 6; i++){
            if (player == PLAYER_ONE){
                //gets the number of stones in each pit
                // numStones += pits.get(i).removeStones();
                numStones += gameBoard.removeStones(i);
            } else if (player == PLAYER_TWO){
                numStones += gameBoard.removeStones(i+6);
                // numStones += pits.get(i+6).removeStones();
            }
        }
        return numStones;
    }

    /**
     * sets the freeTurn variable.
     *
     * @param checkFreeTurn used to set the boolean variable freeTurn.
     */
    public void setFreeTurn(final boolean checkFreeTurn){
        freeTurn = checkFreeTurn;
    }
    /**
     * gets the freeTurn variable.
     *
     * @return the value of the freeTurn variable.
     */
    public boolean isFreeTurn(){
        return freeTurn;
    }

    /* default */ void moveEmptySide(){
        //if player 1's side is empty, all the stones go to player 2's store
        if (isSideEmpty(1)){
            gameBoard.addToStore(2, getSideStones(2));
        } else if (isSideEmpty(7)){
            gameBoard.addToStore(1, getSideStones(1));
        }
    }

    /**
     * Get the Store count from MancalaDataStructure class.
     *
     * @param playerNum The player number for the corresponding store (1 or 2).
     * @return The number of stones in the store.
     */
    public int getStoreCount(final int playerNum) {
        return gameBoard.getStoreCount(playerNum);
    }

    

    /**
     * Represents the Data Structure.
     *
     * @return A string representation of the Data Structure.
     */
    @Override
    public String toString() {
        final MancalaDataStructure gameBoard = getDataStructure(); // Uncomment this line
        final StringBuilder returnString = new StringBuilder();
    
        returnString.append("\nPlayer 2's side\n\n");
        for (int i = 12; i > 6; i--) {
            returnString.append("\t" + gameBoard.getNumStones(i));
        }
        returnString.append("\n");
        returnString.append(gameBoard.getStoreCount(2));
        returnString.append("\t\t\t\t\t\t\t");
        returnString.append(gameBoard.getStoreCount(1));
        returnString.append("\n");
    
        for (int i = 1; i < 7; i++) {
            returnString.append("\t" + gameBoard.getNumStones(i));
        }
        returnString.append("\n\nPlayer 1's side\n");
    
        return returnString.toString();
    }
    
}
