package mancala;

import java.io.Serializable;

public class Store implements Countable, Serializable {
    private static final long serialVersionUID = -1652390430108192993L;
    private Player storeOwner;
    private int storeCount;

    public Store() {
        storeCount = 0;
        storeOwner = null;
    }

    @Override
    public void addStone() {
        storeCount += 1;
    }

    @Override
    public int getStoneCount() {
        return storeCount;
    }

    @Override
    public void addStones(final int numToAdd) {
        storeCount += numToAdd;
    }

    @Override
    public int removeStones() {
        final int temp = storeCount;
        storeCount = 0;
        return temp;
    }

    public Player getOwner() {
        return storeOwner;
    }

    public int getTotalStones() {
        return storeCount;
    }

    public void setOwner(final Player player) {
        storeOwner = player;
    }

    @Override
    public String toString() {
        return "" + storeCount;
    }
}
