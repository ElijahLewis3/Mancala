package mancala;

public class KalahRules extends GameRules{
    private static final long serialVersionUID = -6280391982381637717L;

    private static final int PLAYER_ONE_STORE = 6;
    private static final int PLAYER_TWO_STORE = 13;
    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;

    private int currentPitIndex;
    private int storePitIndex;
    final private MancalaDataStructure gameBoard = getDataStructure();



    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException{

        if (startPit < 1 || startPit > 12){
            throw new InvalidMoveException();
        }

        int numOfStonesBefore = 0;
        int numOfStonesAfter = 0;

        if (playerNum == PLAYER_ONE){  
            setPlayer(1);
            numOfStonesBefore = gameBoard.getStoreCount(1);  
            if (distributeStones(startPit) == -1){
                return -1;
            }
            numOfStonesAfter = gameBoard.getStoreCount(1);     

            
        } else if (playerNum == PLAYER_TWO){
            setPlayer(2);
            numOfStonesBefore = gameBoard.getStoreCount(2);     
            if (distributeStones(startPit+6) == -1){
                return -1;
            }
            numOfStonesAfter = gameBoard.getStoreCount(2);     
        } 

        //call the method to check if game is over and stones should be moved
        moveEmptySide();
        return numOfStonesAfter - numOfStonesBefore;
    }


    @Override
    /* default */ int distributeStones(final int startPit){
        currentPitIndex = startPit - 1;
        storePitIndex = currentPitIndex;
        setFreeTurn(false);

        final int stonesAdded = distributeLogic(startPit,currentPitIndex,storePitIndex);
        freeTurnCheck(storePitIndex);

        captureHelp(currentPitIndex);
        return stonesAdded;
    }

    @Override
    /* default */ int captureStones(final int stoppingPoint){

        if (gameBoard.getNumStones(stoppingPoint) != PLAYER_ONE){
            return 0;
        }

        final int currentPlayer = getCurrentPlayer();

        //else, capture the stones

        int numStonesCaptured = 0;
        final int oppositePitIndex = 13 - stoppingPoint;

        
        //representation of pits
        // 12 11 10 9 8 7
        // 01 02 3 4 5 6

        //get the stones from the opposite pit
        
        if (currentPlayer == PLAYER_ONE){
            //ensure it is on playerOne's side
            if (stoppingPoint >= 1 && stoppingPoint <=6 && gameBoard.getNumStones(oppositePitIndex) > 0){
                //get the stones from the opposite pit
                numStonesCaptured = gameBoard.removeStones(oppositePitIndex);
                numStonesCaptured += gameBoard.removeStones(stoppingPoint);
                gameBoard.addToStore(1, numStonesCaptured);

            }
        } else if (currentPlayer == PLAYER_TWO && stoppingPoint >= 7 && stoppingPoint <=12){
            //get the stones from the opposite pit
            if (gameBoard.getNumStones(oppositePitIndex) > 0){
                numStonesCaptured = gameBoard.removeStones(oppositePitIndex);
                numStonesCaptured += gameBoard.removeStones(stoppingPoint);
                gameBoard.addToStore(2, numStonesCaptured);
            }
        }

        return numStonesCaptured;
    }


    private void freeTurnCheck(final int index) {
        final int currentPlayer = getCurrentPlayer();

        if (isSideEmpty(1) || isSideEmpty(7)) {
            setFreeTurn(false);
        } else if (index == 6 && currentPlayer == 1) {
            setFreeTurn(true);
        } else if (index == 13 && currentPlayer == 2) {
            setFreeTurn(true);
        }

    }

    private void captureHelp(final int currIndex) {
        if (currIndex > -1) {
            captureStones(currIndex + 1);

        }
    }

    /* default */ int distributeLogic(final int startPit, int currentIndex, final int storeIndex){
        if (storePitIndex >= PLAYER_ONE_STORE){
            storePitIndex++;
        }

        int numOfStones = gameBoard.removeStones(startPit);
        if (numOfStones == 0){
            return -1;
        }

        int stonesAdded = 0;
        while (numOfStones > 0) {
            storePitIndex = (storePitIndex + 1) % 14;
            currentIndex = (currentIndex + 1) % 12;

        
            if (storePitIndex == PLAYER_ONE_STORE) {
                if (startPit >= 1 && startPit <= 6) {
                // if (currentPlayer == PLAYER_ONE) {
                    gameBoard.addToStore(1, 1);
                } else {
                    gameBoard.addStones(currentIndex+1, 1);
                }
                currentIndex--;
            } else if (storePitIndex == PLAYER_TWO_STORE) {
                if (startPit >= 7 && startPit <= 12) {
                // if (currentPlayer == PLAYER_TWO) {
                    gameBoard.addToStore(2, 1);
                } else {
                    gameBoard.addStones(currentIndex+1, 1);
                }
                currentIndex--;
            } else {
                gameBoard.addStones(currentIndex+1, 1);

            }
            stonesAdded++;
            numOfStones--;
        }
        if (currentIndex == -1){
            currentIndex += 1;
        }
        currentPitIndex = currentIndex;
        return stonesAdded;
    }



}