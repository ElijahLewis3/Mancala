package mancala;
import java.io.Serializable;
import java.util.ArrayList;


public class Board implements Serializable{
    private static final int PLAYER_ONE_STORE = 6;
    private static final int PLAYER_TWO_STORE = 13;

    final private ArrayList<Pit> pits;
    final private ArrayList<Store> stores;
    final private Player playerOne;
    final private Player playerTwo;
    private Player currentPlayer;
    private Boolean freeTurn;

    public Board() {
        pits = new ArrayList<>();
        stores = new ArrayList<>();
        setUpPits();
        setUpStores();
    
        UserProfile userProfileOne = new UserProfile("Player 1");
        UserProfile userProfileTwo = new UserProfile("Player 2");
    
        playerOne = new Player(userProfileOne);
        playerTwo = new Player(userProfileTwo);
    
        currentPlayer = playerOne;
        registerPlayers(playerOne, playerTwo);
        freeTurn = false;
        initializeBoard();
    }

    //Helper method that Captures stones from the opponent's pits
    /* default */ int captureStones(final int stoppingPoint) throws PitNotFoundException{
        if (stoppingPoint < 1 || stoppingPoint > 12){
            throw new PitNotFoundException();
        }else{
            //if it the last pit isn't empty return 0
            if (pits.get(stoppingPoint - 1).getStoneCount() != 1) {
                return 0;
            }

                //else, capture the stones

            int numStonesCaptured = 0;
            final int oppositePitNumIndex = 12 - stoppingPoint;

            
            //representation of pits
            // 12 11 10 9 8 7
            // 01 02 3 4 5 6

            //get the stones from the opposite pit
            
            if (currentPlayer.equals(playerOne)){
                //ensure it is on playerOne's side
                if (stoppingPoint >= 1 && stoppingPoint <=6){
                    //get the stones from the opposite pit
                    numStonesCaptured = pits.get(oppositePitNumIndex).removeStones();
                    numStonesCaptured += pits.get(stoppingPoint-1).removeStones();
                    stores.get(0).addStones(numStonesCaptured);
                }
            } else {
                if (stoppingPoint >= 7 && stoppingPoint <=12){
                    //get the stones from the opposite pit
                    numStonesCaptured = pits.get(oppositePitNumIndex).removeStones();
                    numStonesCaptured += pits.get(stoppingPoint-1).removeStones();
                    stores.get(1).addStones(numStonesCaptured);
                }
            }

            return numStonesCaptured;
        }
    }

    //Helper method that distributes stones into pits and stores, skipping the opponent's store
    /* default */ int distributeStones(final int startingPoint) throws PitNotFoundException {
        if (startingPoint < 1 || startingPoint > 12) {
            throw new PitNotFoundException();
        } else {
            setFreeTurn(false);
            int currentPitIndex = startingPoint - 1;
            int storePitIndex = currentPitIndex;

            if (startingPoint >= 1 && startingPoint <= 6) {
                currentPlayer = playerOne;
            } else {
                currentPlayer = playerTwo;
                storePitIndex += 1;
            }

            int numOfStones = pits.get(currentPitIndex).removeStones();
            // if (numOfStones == 0) {
            //     return -1;
            // }

            int stonesAdded = 0;
            while (numOfStones > 0) {
                storePitIndex = (storePitIndex + 1) % 14;
                currentPitIndex = (currentPitIndex + 1) % 12;

                if (storePitIndex == PLAYER_ONE_STORE) {
                    if (currentPlayer.equals(playerOne)) {
                        stores.get(0).addStones(1);
                    } else {
                        pits.get(currentPitIndex).addStone();
                    }
                    currentPitIndex--;
                } else if (storePitIndex == PLAYER_TWO_STORE) {
                    if (currentPlayer.equals(playerTwo)) {
                        stores.get(1).addStones(1);
                    } else {
                        pits.get(currentPitIndex).addStone();
                    }
                    currentPitIndex--;
                } else {
                    pits.get(currentPitIndex).addStone();
                }
                stonesAdded++;
                numOfStones--;
            }
            if (currentPitIndex == -1){
                currentPitIndex += 1;
            }
            freeTurnCheck(storePitIndex);

            captureHelp(currentPitIndex, storePitIndex);
            return stonesAdded;
        }
    }

    private void freeTurnCheck(final int index) throws PitNotFoundException {
        try {
            if (isSideEmpty(1) || isSideEmpty(7)) {
                setFreeTurn(false);
            } else if (index == 6 && currentPlayer.equals(playerOne)) {
                setFreeTurn(true);
            } else if (index == 13 && currentPlayer.equals(playerTwo)) {
                setFreeTurn(true);
            }
        } catch (PitNotFoundException e) {

        }
    }

    private void captureHelp(int currentPitIndex, final int storePitIndex) {
        // int newIndex = storePitIndex;
        int newIndex = currentPitIndex;
        if (currentPitIndex > -1) {
            // if (storePitIndex > PLAYER_ONE_STORE) {
            //     newIndex--;
            // }
            try {
                captureStones(newIndex + 1);
            } catch (PitNotFoundException e) {

            }
        }
    }


    //Gets the number of stones in a specific pit
    public int getNumStones(final int pitNum) throws PitNotFoundException{
        if (pitNum >= 1 && pitNum <= 12){ //in range 1-12
            
            return pits.get(pitNum-1).getStoneCount();
        } else {
            throw new PitNotFoundException();
        }
    }

    //Returns the list of pits in the board, not including the stores
    public ArrayList<Pit> getPits(){
        return pits;
    }

    //Returns the list of stores in the board, not including the stores
    public ArrayList<Store> getStores(){
        return stores;
    }

    //Initializes the board by distributing stones
    private void initializeBoard(){
        for (final Pit pit: pits){
            pit.removeStones();
            for (int j = 0; j < 4; j++){
                pit.addStone();
            }
        }
    }

    //Indicates whether one side of the board is empty
    public boolean isSideEmpty(final int pitNum) throws PitNotFoundException{
        if (pitNum < 1 || pitNum > 12){
            throw new PitNotFoundException();
        }else {

            if (pitNum < 7 && pitNum > 0){ //Player 1's side{
                for (int i = 0; i < 6; i++){
                    if (pits.get(i).getStoneCount() > 0){ //a number other than 0 (hence not empty)
                        return false;
                    }
                }
            } else if (pitNum < 13 && pitNum > 6){ //Player 2's side{
                for (int i = 6; i < 12; i++){
                    if (pits.get(i).getStoneCount() > 0){ //a number other than 0 (hence not empty)
                        return false;
                    }
                }
            }
            return true;
        }
    }

    //Moves stones for the player starting from a specific pit
    public int moveStones(final int startPit, final Player player) throws InvalidMoveException{
        if (startPit < 1 || startPit > 12){
            throw new InvalidMoveException();
        }else {

            int numOfStonesBefore = 0;
            int numOfStonesAfter = 0;

            try{
                if (startPit >= 1 && startPit <=6){  
                    numOfStonesBefore = stores.get(0).getTotalStones();      
                    //user picked an empty spot
                    if (distributeStones(startPit) == -1){
                        return -1;
                    }
                    numOfStonesAfter = stores.get(0).getTotalStones();
                    
                } else if (startPit >= 7 && startPit <= 12){
                    numOfStonesBefore = stores.get(1).getTotalStones();      
                    //user picked an empty spot
                    if (distributeStones(startPit) == -1){
                        return -1;
                    }
                    numOfStonesAfter = stores.get(1).getTotalStones();

                } 
            }catch(PitNotFoundException e) {
            }

            try{
                // if player 1's side is empty, add all the stones to player 2's store
                if (isSideEmpty(1)){
                    //adds the stones on player 2's side and adds it to their store
                    stores.get(1).addStones(getSideStones(playerTwo));
                } else if (isSideEmpty(7)){
                    stores.get(0).addStones(getSideStones(playerOne));
                }
            } catch(PitNotFoundException e){

            }
    
            return numOfStonesAfter - numOfStonesBefore;
        }

    }

    public int getSideStones(final Player player){
        int numStones = 0;
        for (int i = 0; i < 6; i++){
            if (player.equals(playerOne)){
                //gets the number of stones in each pit
                numStones += pits.get(i).removeStones();
            } else if (player.equals(playerTwo)){
                numStones += pits.get(i+6).removeStones();
            }
        }
        return numStones;
    }

    //Connects Players to their Stores
    private void registerPlayers(final Player one, final Player two){
        one.setStore(stores.get(0));
        two.setStore(stores.get(1));
    }

    //Resets the board by redistributing stones but retains the players
    public void resetBoard(){
        for (final Pit pit: pits){
            pit.removeStones();
            for (int i = 0; i < 4; i++){
                pit.addStone();
            }
        }

        for (final Store store: stores){
            store.removeStones();
        }
    }

    //Establishes 12 empty Pits in the board
    private void setUpPits() {
        for (int i = 0; i < 12; i++) {
            pits.add(new Pit());
        }
    }

    private void setUpStores() {
        for (int i = 0; i < 2; i++) {
            stores.add(new Store());
        }
    }

    public boolean isFreeTurn(){
        return freeTurn;
    }

    public void setFreeTurn(final boolean checkFreeTurn){
        freeTurn = checkFreeTurn;
    }


    @Override
    public String toString(){
    final StringBuilder returnString = new StringBuilder();

    returnString.append("\n" + playerTwo + "'s side\n\n");
    for (int i = 11; i > 5; i--) {
        returnString.append("\t" + pits.get(i));
    }
    returnString.append("\n");
    returnString.append(stores.get(1));
    returnString.append("\t\t\t\t\t\t\t");
    returnString.append(stores.get(0));
    returnString.append("\n");

    for (int i = 0; i < 6; i++) {
        returnString.append("\t" + pits.get(i));
    }
    returnString.append("\n\n"+ playerOne + "'s side\n");


    return returnString.toString();
    }

}
