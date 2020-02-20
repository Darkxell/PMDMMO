package com.darkxell.client.model.quiz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QuizAnswerRewardModel {

    @XmlAttribute
    public Nature nature;

    @XmlAttribute(name = "value")
    public int points;

    public QuizAnswerRewardModel() {
    }

}
