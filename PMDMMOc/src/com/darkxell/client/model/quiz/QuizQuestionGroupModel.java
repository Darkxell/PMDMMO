package com.darkxell.client.model.quiz;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "questiongroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuizQuestionGroupModel {

    @XmlAttribute
    private int id;

    @XmlElement(name = "question")
    public ArrayList<QuizQuestionModel> questions;

    public QuizQuestionGroupModel() {}

    public int getID() {
        return this.id;
    }

}
