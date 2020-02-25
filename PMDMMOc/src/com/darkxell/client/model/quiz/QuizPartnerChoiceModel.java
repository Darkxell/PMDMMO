package com.darkxell.client.model.quiz;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QuizPartnerChoiceModel {

    @XmlAttribute
    private int id;

    /** This Partner will be prevented from appearing if one of these IDs is the player's Pokemon. */
    @XmlElement(name = "invalidates")
    private ArrayList<Integer> invalidators;

    public QuizPartnerChoiceModel() {}

    /** @return True if this Partner is valid for the input leader choice. */
    public boolean isValid(int choice) {
        return this.getID() != choice && !this.invalidators.contains(choice);
    }

    public int getID() {
        return id;
    }

}
