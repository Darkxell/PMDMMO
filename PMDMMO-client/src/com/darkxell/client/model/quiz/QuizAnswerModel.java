package com.darkxell.client.model.quiz;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.darkxell.common.util.language.Message;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QuizAnswerModel {

    @XmlAttribute
    private int id;

    @XmlElement(name = "reward")
    public ArrayList<QuizAnswerRewardModel> rewards;

    @XmlTransient
    public int questionID, questionGroupID;

    public QuizAnswerModel() {
    }

    public Message name() {
        return new Message("quiz." + this.questionGroupID + "." + this.questionID + "." + this.id);
    }
}
