package com.darkxell.common.model.pokemon;

import java.util.ArrayList;

public class LearnsetModel {

    /** Level for this learnset. */
    private int level;

    /** Moves learned at this level. */
    private ArrayList<Integer> learnedMoves;

    public LearnsetModel() {
    }

    public LearnsetModel(int level, ArrayList<Integer> learnedMoves) {
        this.level = level;
        this.learnedMoves = learnedMoves;
    }

    public ArrayList<Integer> getLearnedMoves() {
        return learnedMoves;
    }

    public int getLevel() {
        return level;
    }

    public void setLearnedMoves(ArrayList<Integer> learnedMoves) {
        this.learnedMoves = learnedMoves;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public LearnsetModel copy() {
        return new LearnsetModel(this.level, new ArrayList<>(this.learnedMoves));
    }

}
