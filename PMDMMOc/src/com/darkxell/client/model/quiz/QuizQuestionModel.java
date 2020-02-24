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
public class QuizQuestionModel {

    @XmlAttribute
    private int id;

    @XmlElement(name = "answer")
    public ArrayList<QuizAnswerModel> answers;

    @XmlAttribute
    private boolean yesno = false;

    @XmlTransient
    public int questionGroupID;

    public QuizQuestionModel() {
    }

    public Message[] options() {
        Message[] options = new Message[this.answers.size()];
        for (int i = 0; i < options.length; ++i) {
            if (yesno && i < 2) {
                if (i == 0)
                    options[i] = new Message("quiz.yes");
                else
                    options[i] = new Message("quiz.no");
            } else
                options[i] = this.answers.get(i).name();
        }
        return options;
    }

    public Message name() {
        return new Message("quiz." + this.questionGroupID + "." + this.id);
    }

    public int getID() {
        return this.id;
    }

}
