package com.darkxell.client.model.cutscene.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageModel {

    @XmlAttribute(name = "value")
    private String text;

    @XmlAttribute
    private Boolean translate;

    public MessageModel() {
    }

    public MessageModel(String text, Boolean translate) {
        this.text = text;
        this.translate = translate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getTranslate() {
        return translate;
    }

    public void setTranslate(Boolean translate) {
        this.translate = translate;
    }

    public MessageModel copy() {
        return new MessageModel(this.text, this.translate);
    }

}
