package mancala;

public class AyoRules extends GameRules{
    private static final long serialVersionUID = -5213113814871006795L;
    final private MancalaDataStructure gameBoard = getDataStructure();
    private static final int PLAYER_ONE_STORE = 6;
    private static final int PLAYER_TWO_STORE = 13;

    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;


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
            if (distributeStones(startPit) == -1){
                return -1;
            }
            numOfStonesAfter = gameBoard.getStoreCount(2);     
        } 

        //call the method to check if game is over and stones should be moved
        moveEmptySide();

        return numOfStonesAfter - numOfStonesBefore;
    }

    @Override
    /* default */ int distributeStones(int startPit) {

        final int currentPlayer = getCurrentPlayer();
    
        //skip Player 2's store as well as the start pit
        gameBoard.setIterator(startPit, currentPlayer, true);


        if (gameBoard.getNumStones(startPit) == 0){
            return -1;
        }

        Countable pit;
        int numOfStonesAdded = 0;
        boolean capture = true;

        //distribute stones until the last pit is empty
        do {
            final int numOfStones = gameBoard.removeStones(startPit);
    
            for (int i = 0; i < numOfStones; i++) {
                pit = gameBoard.next();
                pit.addStone();
                numOfStonesAdded++;
            }
            startPit = gameBoard.getCurrentIteratorPos();


            //if the last pit goes into the store
            if (currentPlayer == PLAYER_ONE && startPit == PLAYER_ONE_STORE){
                capture = false;
                break;

            } else if (currentPlayer == PLAYER_TWO && startPit == PLAYER_TWO_STORE){
                capture = false;
                break;

            }
        } while (gameBoard.getNumStones(startPit) != 1);
        if (capture){
            captureStones(startPit);
        }
    
        return numOfStonesAdded;
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

        //get the stones from the opposite pit
        if (currentPlayer == PLAYER_ONE){
            //ensure it is on playerOne's side
            if (stoppingPoint >= 1 && stoppingPoint <=6){
                //get the stones from the opposite pit
                numStonesCaptured = gameBoard.removeStones(oppositePitIndex);
                // numStonesCaptured += gameBoard.removeStones(stoppingPoint);
                gameBoard.addToStore(1, numStonesCaptured);
            }
        } else if (currentPlayer == PLAYER_TWO && stoppingPoint >= 7 && stoppingPoint <=12){
            //get the stones from the opposite pit
            numStonesCaptured = gameBoard.removeStones(oppositePitIndex);
            // numStonesCaptured += gameBoard.removeStones(stoppingPoint);
            gameBoard.addToStore(2, numStonesCaptured);

        }
        return numStonesCaptured;
    }
}