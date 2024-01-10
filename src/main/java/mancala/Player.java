package mancala;

import java.io.Serializable;

public class Player implements Serializable{
    private static final long serialVersionUID = 5381502920481750442L;
    private UserProfile playerProfile;
    private Store playerStore;
    private String playerName;

    public Player(final UserProfile profile) {
        playerProfile = profile;
        playerStore = new Store();
        playerName = profile.getUserName();
    }

    public Player() {
        this(new UserProfile("DefaultUser"));
    }

    //Gets the name of the player
    public String getName(){
        return playerProfile.getUserName();
    }

    //Gets the player's store where they collect stones
    public Store getStore(){
        return playerStore;
    }

    //Gets the count of the number of stones in the player's store where they collect stones
    public int getStoreCount(){
        return playerStore.getTotalStones();
    }

    //Sets the player's name
    public void setName(final String name){
        playerName = name;
    }

    //Sets the player's store
    public void setStore(final Store store){
        playerStore = store;
    }

    public UserProfile getProfile(){
        return playerProfile;
    }

    @Override
    public String toString(){
        return "" + playerName;
    }
}
