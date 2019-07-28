package com.darkxell.client.resources.image.hud;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;

public class ChatFooterSpriteset extends PMDSpriteset {

    public ChatFooterSpriteset() {
        super("/hut/chat/chatfooter.png");
        this.createSprite("left", 0, 0, 43, 35);
        this.createSprite("right", 171, 0, 39, 35);
        this.createSprite("center", 43, 0, 125, 35);
    }

    public BufferedImage center() {
        return this.getSprite("center");
    }

    public BufferedImage left() {
        return this.getSprite("left");
    }

    public BufferedImage right() {
        return this.getSprite("right");
    }

}
