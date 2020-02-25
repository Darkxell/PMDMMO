package com.darkxell.client.model.cutscene.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.darkxell.client.resources.image.pokemon.portrait.PortraitEmotion;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;

@XmlRootElement(name = "dialogscreen")
@XmlAccessorType(XmlAccessType.FIELD)
public class CutsceneDialogScreenModel {

    @XmlValue
    private String text;

    @XmlAttribute
    private Boolean translate;

    @XmlAttribute
    private PortraitEmotion emotion;

    @XmlAttribute
    private Integer target;

    @XmlAttribute(name = "portrait-location")
    private DialogPortraitLocation portraitLocation;

    public CutsceneDialogScreenModel() {
    }

    public CutsceneDialogScreenModel(String text, Boolean translate, Integer target, PortraitEmotion emotion,
            DialogPortraitLocation portraitLocation) {
        this.text = text;
        this.translate = translate;
        this.target = target;
        this.emotion = emotion;
        this.portraitLocation = portraitLocation;
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

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public PortraitEmotion getEmotion() {
        return emotion;
    }

    public void setEmotion(PortraitEmotion emotion) {
        this.emotion = emotion;
    }

    public DialogPortraitLocation getPortraitLocation() {
        return portraitLocation;
    }

    public void setPortraitLocation(DialogPortraitLocation portraitLocation) {
        this.portraitLocation = portraitLocation;
    }

    public CutsceneDialogScreenModel copy() {
        CutsceneDialogScreenModel clone = new CutsceneDialogScreenModel();
        clone.setText(this.text);
        clone.setTranslate(this.translate);
        clone.setTarget(this.target);
        clone.setEmotion(this.emotion);
        clone.setPortraitLocation(this.portraitLocation);
        return clone;
    }

}
