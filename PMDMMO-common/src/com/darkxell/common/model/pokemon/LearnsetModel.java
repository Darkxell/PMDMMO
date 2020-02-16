package com.darkxell.common.model.pokemon;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.darkxell.common.util.XMLUtils.IntegerListAdapter;

@XmlRootElement(name = "learnset")
@XmlAccessorType(XmlAccessType.FIELD)
public class LearnsetModel {

    /** Level for this learnset. */
    @XmlAttribute
    private int level;

    /** Moves learned at this level. */
    @XmlAttribute(name = "moves")
    @XmlJavaTypeAdapter(IntegerListAdapter.class)
    private ArrayList<Integer> learnedMoves;

    public LearnsetModel() {
    }

    public LearnsetModel(int level, ArrayList<Integer> learnedMoves) {
        this.level = level;
        this.learnedMoves = learnedMoves;
    }

    public LearnsetModel copy() {
        return new LearnsetModel(this.level, new ArrayList<>(this.learnedMoves));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LearnsetModel))
            return false;
        LearnsetModel o = (LearnsetModel) obj;
        return this.level == o.level && this.learnedMoves.equals(o.learnedMoves);
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

}
