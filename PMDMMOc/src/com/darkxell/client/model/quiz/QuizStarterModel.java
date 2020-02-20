package com.darkxell.client.model.quiz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.quiz.QuizModel.QuizGender;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QuizStarterModel {

    @XmlAttribute
    private Nature nature;

    @XmlAttribute
    private QuizGender gender;

    @XmlAttribute(name = "pokemon")
    private int pokemonID;

    public QuizStarterModel() {
    }

    public int getPokemonID() {
        return this.pokemonID;
    }

    public boolean matches(Nature nature, QuizGender gender) {
        return this.nature == nature && this.gender == gender;
    }

}
