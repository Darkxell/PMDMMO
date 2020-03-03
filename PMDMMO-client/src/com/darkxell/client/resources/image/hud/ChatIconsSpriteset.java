package com.darkxell.client.resources.image.hud;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;

public class ChatIconsSpriteset extends PMDSpriteset {

    public ChatIconsSpriteset() {
        super("/hud/chat/icons.png");
        this.createSprite("global", 0, 0, 32, 32);
        this.createSprite("guild", 32, 0, 32, 32);
        this.createSprite("private", 64, 0, 32, 32);
        this.createSprite("send", 96, 0, 32, 32);
    }

    public BufferedImage global() {
        return this.getSprite("global");
    }

    public BufferedImage guild() {
        return this.getSprite("guild");
    }

    public BufferedImage privateIcon() {
        return this.getSprite("private");
    }

    public BufferedImage send() {
        return this.getSprite("send");
    }

}
