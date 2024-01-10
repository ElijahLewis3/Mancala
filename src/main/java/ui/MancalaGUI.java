package ui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;




import java.util.ArrayList;

import mancala.PitNotFoundException;
import mancala.GameNotOverException;
import mancala.InvalidMoveException;
import java.io.IOException;
import mancala.MancalaGame;
import mancala.Player;
import mancala.Saver;
import mancala.UserProfile;


public class MancalaGUI extends JFrame{

    private JPanel gameContainer;
    private JLabel messageLabel;
    private JMenuBar menuBar;
    private JFileChooser fileChooser;
    private PositionAwareButton[][] buttons;
    private MancalaGame game;
    private Player playerOne;
    private Player playerTwo;
    private UserProfile userOne;
    private UserProfile userTwo;
    private JLabel playerOneStoreLabel;
    private JLabel playerTwoStoreLabel;
    private JPanel playerOneStatsPanel;
    private JPanel playerTwoStatsPanel;


    public MancalaGUI(String title){
        super();
        userOne = new UserProfile("User One");
        userTwo = new UserProfile("User Two");
        playerOne = new Player(userOne);
        playerTwo = new Player(userTwo);
        // game = new MancalaGame();
        game = new MancalaGame(playerOne,playerTwo,1);
        basicSetUp(title);
        setUpGameContainer();
        add(gameContainer,BorderLayout.CENTER);
        messageLabel = new JLabel("Welcome to Mancala.");
        gameContainer.add(messageLabel, BorderLayout.SOUTH);
        fileChooser = new JFileChooser();
        setUpMenuBar();
        updateView();
        pack();
    }

    public void basicSetUp(String title){
        this.setTitle(title);
        gameContainer = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    public void setUpGameContainer() {
        playerTwoStoreLabel = new JLabel("Player 2's Store: ");
        playerOneStoreLabel = new JLabel("Player 1's Store: ");
    
        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        // Create a nested panel for the left side (WEST)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(playerTwoStore());
        leftPanel.add(leftButtonPanel());
        // mainPanel.add(playerTwoStore(),BorderLayout.BEFORE_LINE_BEGINS);

    
        // Create a nested panel for the right side (EAST)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(playerOneStore(),BorderLayout.AFTER_LINE_ENDS);
        rightPanel.add(makeButtonPanel());
    
        // Add the left and right panels to the main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);
    
        // Add the game grid to the center
        mainPanel.add(makeMancalaGrid(6, 2), BorderLayout.CENTER);
    
        // Create stats panels and add them to the main panel at the bottom (SOUTH)
        playerOneStatsPanel = createPlayerStatsPanel(playerOne);
        playerTwoStatsPanel = createPlayerStatsPanel(playerTwo);
        JPanel statsPanel = new JPanel();
        statsPanel.add(playerOneStatsPanel);
        statsPanel.add(playerTwoStatsPanel);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);
    
        // Add the main panel to gameContainer
        gameContainer.add(mainPanel);
    }
    

    /********************************
    **********  BUTTONS  ************
    ********************************/
    private JPanel makeButtonPanel(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        buttonPanel.add(makeAboutKalahButton());
        buttonPanel.add(makeAboutAyoButton());
        return buttonPanel;
    }

    private JPanel leftButtonPanel(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        // buttonPanel.add(makeSaveGameButton());
        // buttonPanel.add(makeLoadGameButton());
        buttonPanel.add(makeNewKalahGameButton());
        buttonPanel.add(makeNewAyoGameButton());
        return buttonPanel;
    }

    private JButton makeAboutKalahButton() {
        JButton button = new JButton("About Kalah");
        button.addActionListener(e -> kalah());
        return button;
    }


    private void kalah(){
        String message = "Kalah is the most commonly played version of Mancala, the rules are pretty simple and straightforward.\n" +
        "The game begins with one player picking up all of the pieces in any one of the holes on their side.\n" +
        "Moving counter-clockwise, the player deposits one stone in each hole until the stones run out.\n\n" +
        "Rules:\n\n" +
        "1. If you run into your own store, deposit one piece in it. If you run into your opponent's store, skip it.\n" +
        "2. If the last piece you drop is in your own store, you get a free turn.\n" +
        "3. If the last piece you drop is in an empty hole on your side, you capture that piece and any pieces in the hole directly opposite.\n" +
        "4. Always place all captured pieces in your store.\n\n" +
        "Winning the Game\n\n" +
        "The game ends when all six spaces on one side of the Mancala board are empty. The player who still has pieces\n" +
        "on their side of the board when the game ends captures all of those pieces. Count all the pieces in each store.\n" +
        "The winner is the player with the most pieces.";
        JOptionPane.showMessageDialog(null,message);
    }

    private JButton makeAboutAyoButton() {
        JButton button = new JButton("About Ayo");
        button.addActionListener(e -> ayo());
        return button;
    }

    private void ayo(){
        String message = "This is a simplified set of rules for a version of Mancala known as Ayoayo.\n" + 
        "The game starts in a manner similar to Kalah, and the conditions for ending the game are the same.\n" +
        "There are some differences in how stones are distributed in Ayoayo:\n\n" +
        
        "1. When sowing the seeds, the starting pit is excluded. If there are enough stones in a pit to travel around the\n" +
        "board without reaching the starting pit on the second pass, no stone is placed in the original starting pit.\n" +
        "In other words, the starting pit always remains empty after a move.\n\n" + 
        
        "2. After distributing the stones, if the last stone lands in a pit on either side of the board that already contains\n" + 
        "stones, all the stones in that pit are collected and redistributed. This multi-lap play continues until the last\n" + 
        "seed falls into an empty pit, which concludes the turn unless a capture occurs.\n\n" +
        
        "3. A capture takes place when the last stone lands in an empty pit on the player's side, and there are stones in\n" +
        "the pit directly opposite. In such a case, all of the opponent's stones from the opposite pit are captured and\n" +
        "placed in the player's store. The final stone from the player's own side is not captured; it remains in play.\n" +
        "If the opposite pit has no stones, the move ends, and no stones are captured. Additionally, if the final stone\n" +
        "lands on the opponent's side, no stones are captured. If the last stone ends in the store, the player's turn is over\n";
        JOptionPane.showMessageDialog(null,message);
    }


    /********************************
    *****  Left Side BUTTONS  *******
    ********************************/
    // private JButton makeSaveGameButton() {
    //     JButton button = new JButton("Save Current Game");
    //     button.addActionListener(e -> saveGame(""));
    //     return button;
    // }

    // private JButton makeLoadGameButton() {
    //     JButton button = new JButton("Load existing Game");
    //     button.addActionListener(e -> loadGame());
    //     return button;
    // }

    private JButton makeNewKalahGameButton() {
        JButton button = new JButton("New Kalah Game");
        button.addActionListener(e -> {
            game.setGameRules(1);
            game.startNewGame();
            // System.out.println(game);
            updateView();
        });
        return button;
    }

    private JButton makeNewAyoGameButton() {
        JButton button = new JButton("New Ayo Game (Press Twice)");
        button.addActionListener(e -> {
            // game.setGameRules(2);
            game.startNewGame();
            game.setGameRules(2);
            updateView();
            });
        return button;
    }



    protected void updateView(){
        Player currentPlayer = game.getCurrentPlayer();

        //player 2
        for (int x = 0; x <= 5; x++){
            try{
                buttons[0][x].setText(String.valueOf(game.getNumStones(12-x)));
            } catch (PitNotFoundException e){
                System.out.println(e.getMessage());
            }
        }

        //player 1
        for (int x = 0; x <= 5; x++){
            try{
                buttons[1][x].setText(String.valueOf(game.getNumStones(x+1)));
            } catch (PitNotFoundException e){
                System.out.println(e.getMessage());
            }
        }

        updateStoreLabels();
        showMessage(currentPlayer + "'s turn");

    }

    private void updateStoreLabels() {
        playerOneStoreLabel.setText("Player 1's Store: " + game.getStoreCount(playerOne));
        playerTwoStoreLabel.setText("Player 2's Store: " + game.getStoreCount(playerTwo));
        // System.out.println("PLAYER 2's Store = " + game.getStoreCount(playerTwo));
    }

    private JPanel playerOneStore() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
    
        // Add the label to the panel
        panel.add(playerOneStoreLabel, BorderLayout.NORTH);
    
        return panel;
    }
    
    private JPanel playerTwoStore() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
    
        // Add the label to the panel
        panel.add(playerTwoStoreLabel, BorderLayout.EAST);
    
        return panel;
    }


    /********************************
    ********  GAME  LOGIC  **********
    ********************************/


    /********************************
    *********  GAME GRID  ***********
    ********************************/
    private JPanel makeMancalaGrid(int height, int width) {
        //width and height assumed to be 8 and 3
        /*
         * 4 4 4 4 4 4 *
         0 * * * * * * 0
         * 4 4 4 4 4 4 *
         */
        JPanel panel = new JPanel();
        buttons = new PositionAwareButton[width][height];

        panel.setLayout(new GridLayout(width, height));
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                buttons[y][x] = new PositionAwareButton();
                buttons[y][x].setAcross(x + 1);
                buttons[y][x].setDown(y + 1);

                final int finalX = x;
                final int finalY = y;

                // buttons[y][x].addActionListener(e -> moveStones((PositionAwareButton) e.getSource()));
                buttons[y][x].addActionListener(e -> {
                    //player2
                    if (finalY == 0){
                        moveStones(12 - (finalX));
                        checkGameState();
                    } else{
                        moveStones(finalX + 1);
                        checkGameState();
                    }
                });

                panel.add(buttons[y][x]);
            }
        }
        return panel;
    }

    private void checkGameState(){
        if (game.isGameOver()){
            try{
                Player winner = game.getWinner();
                updatePlayerStats();
                if (game.getWinner() == null){
                    showMessage("GAME OVER. THE GAME IS A TIE");
                } else{
                    showMessage("GAME OVER " + winner + " WINS");
                }

            }catch(GameNotOverException e){
                System.err.println(e.getMessage());
            }
        }
    }

    private void moveStones(int pitNum) {
        try {
            Player currentPlayer = game.getCurrentPlayer();
            // System.out.println("PIT NUM = " + (pitNum));
            if (checkValidMove(pitNum,currentPlayer)){
                game.move(pitNum);

                // Check if the current player has another turn
                if (!game.isFreeTurn()) {
                    // Switch to the next player
                    game.switchPlayer();
                }
        

                // Update the view after the move
                updateView();

                updatePlayerStats();
            }

            // System.out.println(game);

        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
        }
    }
    

    private boolean checkValidMove(int pitNum, Player currentPlayer) {
        //checks if the user entered the right side
        if (currentPlayer == playerOne){
            if (pitNum >= 7 && pitNum <= 12){
                showMessage("Invalid move.");
                return false;
            }
        } else if (currentPlayer == playerTwo){
            if (pitNum >= 1 && pitNum <= 6){
                showMessage("Invalid move.");
                return false;
            }
        }

        return true;
    }

    private JPanel createPlayerStatsPanel(Player player) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create labels for player name and games won
        JLabel nameLabel = new JLabel("Name: " + player);
        JLabel kalahGamesLabel = new JLabel("Kalah Games Played: " + player.getProfile().getNumKalahGames()); 
        JLabel kalahGamesWonLabel = new JLabel("Kalah Games Won: " + player.getProfile().getNumKalahGamesWon()); 
        JLabel ayoGamesLabel = new JLabel("Ayo Games Played: " + player.getProfile().getNumAyoGames()); 
        JLabel ayoGamesWonLabel = new JLabel("Ayo Games Won: " + player.getProfile().getNumAyoGamesWon()); 

        // Add labels to the panel
        panel.add(nameLabel);
        panel.add(kalahGamesLabel);
        panel.add(kalahGamesWonLabel);
        panel.add(ayoGamesLabel);
        panel.add(ayoGamesWonLabel);


        return panel;
    }

    private void updatePlayerStats() {
        // Update stats for Player 1
        ((JLabel) playerOneStatsPanel.getComponent(1)).setText("Kalah Games Played: " + userOne.getNumKalahGames());
        ((JLabel) playerOneStatsPanel.getComponent(2)).setText("Kalah Games Won: " + userOne.getNumKalahGamesWon());
        ((JLabel) playerOneStatsPanel.getComponent(3)).setText("Ayo Games Played: " + userOne.getNumAyoGames());
        ((JLabel) playerOneStatsPanel.getComponent(4)).setText("Ayo Games Won: " + userOne.getNumAyoGamesWon());

        ((JLabel) playerTwoStatsPanel.getComponent(1)).setText("Kalah Games Played: " + userTwo.getNumKalahGames() );
        ((JLabel) playerTwoStatsPanel.getComponent(2)).setText("Kalah Games Won: " + userTwo.getNumKalahGamesWon());
        ((JLabel) playerTwoStatsPanel.getComponent(3)).setText("Ayo Games Played: " + userTwo.getNumAyoGames());
        ((JLabel) playerTwoStatsPanel.getComponent(4)).setText("Ayo Games Won: " + userTwo.getNumAyoGamesWon());
    }

    /********************************
    **********  MENU  BAR  **********
    ********************************/
    private void setUpMenuBar() {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem saveMenuItem = new JMenuItem("Save Game");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGameWithFileChooser();
            }
        });

        JMenuItem loadMenuItem = new JMenuItem("Load Game");
        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGameWithFileChooser();
            }
        });

        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    private void saveGameWithFileChooser() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            saveGame(filePath);
        }
    }

    private void loadGameWithFileChooser() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            loadGame(filePath);
        }
    }

    /********************************
    *********  SAVE/LOAD  ***********
    ********************************/
    private void loadGame(String filepath) {
        try {
            String lastSavedFile = getLastSavedFile();
            if (lastSavedFile != null) {
                MancalaGame loadedGame = (MancalaGame) Saver.loadObject(lastSavedFile);
                game = loadedGame;
                updateView();
                // updatePlayerStats();
                showMessage("Last game successfully loaded.");
            } else {
                showMessage("No saved games found.");
            }
        } catch (IOException | ClassNotFoundException e) {
            showMessage("Error loading the last saved game: " + e.getMessage());
        }
    }
    
    private String saveGame(String filepath) {
        // Create the assets folder if it doesn't exist
        File assetsFolder = new File("assets");
        if (!assetsFolder.exists()) {
            assetsFolder.mkdirs();
        }
    
        try {

            String timestamp = String.valueOf(System.currentTimeMillis());
            String filename = "saved_game_" + timestamp + ".dat";
            Saver.saveObject(game, filename);
            showMessage("Game saved.");
    
            return filename;
        } catch (IOException e) {
            showMessage("Error saving: " + e.getMessage());
            return null;
        }
    }

    //The method below was written by ChatGPT in order to get the last saved file
    private String getLastSavedFile() {
        File assetsFolder = new File("assets");
        File[] files = assetsFolder.listFiles();
    
        if (files != null && files.length > 0) {
            // Sort files by last modified timestamp to get the last saved file
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            return files[files.length - 1].getName();
        } else {
            showMessage("No games found.");
            return null;
        }
    }

    private void showMessage(String message) {
        // Update the messageLabel with the provided message
        messageLabel.setText(message);
    }






    public static void main(String[] args) {
        MancalaGUI example = new MancalaGUI("Mancala");
        example.setVisible(true);
    }

}