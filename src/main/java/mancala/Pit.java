package mancala;

import java.io.Serializable;

public class Pit implements Countable,Serializable {
    private static final long serialVersionUID = -7088824814414870199L;
    private int pitStones;

    public Pit() {
        pitStones = 0;
    }

    @Override
    public void addStone() {
        pitStones = pitStones + 1;
    }

    @Override
    public int getStoneCount() {
        return pitStones;
    }

    @Override
    public void addStones(final int numToAdd) {
        pitStones += numToAdd;
    }

    @Override
    public int removeStones() {
        final int temp = pitStones;
        pitStones = 0;
        return temp;
    }


    @Override
    public String toString() {
        return "" + pitStones;
    }
}
