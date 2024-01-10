import mancala.MancalaGame;
import mancala.Player;
import mancala.UserProfile;
import mancala.GameNotOverException;
import mancala.InvalidMoveException;

import java.io.IOException;
import java.util.Scanner;

public class TextUI {
    private MancalaGame game;
    private Scanner scanner;
    private Player playerOne;
    private Player playerTwo;

    public TextUI() {
        scanner = new Scanner(System.in);

        System.out.println("Welcome to Mancala!");

        System.out.print("Enter the name for Player One: ");
        UserProfile userProfileOne = new UserProfile(scanner.nextLine());
        playerOne = new Player(userProfileOne);

        System.out.print("Enter the name for Player Two: ");
        UserProfile userProfileTwo = new UserProfile(scanner.nextLine());
        playerTwo = new Player(userProfileTwo);

        // Choose the game mode
        int gameMode = getGameMode();
        // Create the MancalaGame with the player names and selected game mode
        game = new MancalaGame(playerOne, playerTwo,gameMode);
        // game.setGameRules(gameMode);
    }

    private int getGameMode() {
        int gameMode;
        while (true) {
            System.out.print("Select the game mode (1 for Kalah, 2 for Ayo): ");
            if (scanner.hasNextInt()) {
                gameMode = scanner.nextInt();
                if (gameMode == 1 || gameMode == 2) {
                    break;
                } else {
                    System.out.println("Invalid game mode. Please enter 1 or 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
            }
        }
        return gameMode;
    }

    public void startGame() {
        System.out.println("Welcome to Mancala!\n");
        displayBoard();
        while (!game.isGameOver()) {
            Player currentPlayer = game.getCurrentPlayer();
            System.out.println(currentPlayer + ", it's your turn.");

            int startPit = getUserMove(currentPlayer);
            if (startPit == 0) {
                // Save the game
                saveGame();
                continue;
            } else if (startPit == -1) {
                // Load a game
                loadGame();
                continue;
            }

            try {
                int stonesOnSide = game.move(startPit);
                // if the player got a free turn
                while (game.getBoard().isFreeTurn()) {
                    displayBoard();
                    System.out.print(currentPlayer.getName());
                    System.out.println(", your last pit landed in your store. You get a free turn");
                    startPit = getUserMove(currentPlayer);
                    stonesOnSide = game.move(startPit);
                } // if the player entered an invalid pit
                while (stonesOnSide == -1) {
                    System.out.println("You picked an empty spot, pick another one");
                    startPit = getUserMove(currentPlayer);
                    stonesOnSide = game.move(startPit);
                }
                System.out.println(currentPlayer.getName() + " has " + stonesOnSide + " stones on their side.");
                displayBoard();

                // Toggle the current player's turn
                if (currentPlayer == playerOne) {
                    game.setCurrentPlayer(playerTwo);
                } else {
                    game.setCurrentPlayer(playerOne);
                }
                System.out.println("CURRENT PLAYER = " + currentPlayer);
            } catch (InvalidMoveException e) {
                System.out.println("Invalid move: " + e.getMessage());
            }
        }

        try {
            Player winner = game.getWinner();
            if (winner != null) {
                System.out.println("Game over! " + winner.getName() + " is the winner!");
            } else {
                System.out.println("Game over! It's a tie!");
            }
        } catch (GameNotOverException e) {
            System.out.println(e.getMessage());
        }
        scanner.close();
    }

    private void displayBoard() {
        System.out.println(game);
    }

    private int getUserMove(Player currentPlayer) {
        int startPit = -1;
        boolean validMove = false;

        while (!validMove) {
            System.out.print("Choose a pit (1-6), 0 to save the game, -1 to load a game: ");
            if (scanner.hasNextInt()) {
                startPit = scanner.nextInt();
                if (startPit >= -1 && startPit <= 6) {
                    validMove = true;
                } else {
                    System.out.println("Invalid pit number. Please choose a pit from -1 to 6.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
            }
        }

        return startPit;
    }

    private void saveGame() {
        System.out.print("Enter a filename to save the game: ");
        String filename = scanner.next();
        try {
            mancala.Saver.saveObject(game, filename);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving the game: " + e.getMessage());
        }
    }

    private void loadGame() {
        System.out.print("Enter a filename to load the game: ");
        String filename = scanner.next();
        try {
            MancalaGame loadedGame = (MancalaGame) mancala.Saver.loadObject(filename);
            game = loadedGame;
            System.out.println("Game loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading the game: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TextUI textUI = new TextUI();
        textUI.startGame();
    }
}
