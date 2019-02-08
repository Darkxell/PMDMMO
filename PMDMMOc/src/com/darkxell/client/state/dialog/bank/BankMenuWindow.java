package com.darkxell.client.state.dialog.bank;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.common.util.language.Message;

public class BankMenuWindow extends MenuWindow {

    public long bag, bank;
    private final Message mBag, mBank;

    public BankMenuWindow(Rectangle dimensions, long bag, long bank) {
        super(dimensions);
        this.bag = bag;
        this.bank = bank;
        this.mBag = new Message("dialog.bank.summary.bag");
        this.mBank = new Message("dialog.bank.summary.bank");
        this.isOpaque = true;
    }

    @Override
    public void render(Graphics2D g, Message name, int width, int height) {
        super.render(g, name, width, height);

        Rectangle inside = this.inside();
        int y = (int) (inside.y + inside.getHeight() / 2 - TextRenderer.height() / 2);

        Message bg = new Message(String.valueOf(this.bag), false).addPrefix("<blue>").addSuffix("</color>");
        Message bk = new Message(String.valueOf(this.bank), false).addPrefix("<blue>").addSuffix("</color>");
        TextRenderer.render(g, this.mBag, inside.x + MenuWindow.MARGIN_X, y);
        TextRenderer.render(g, bg, (int) (inside.x + inside.getWidth() / 4), y);
        TextRenderer.render(g, this.mBank, (int) (inside.x + inside.getWidth() / 2), y);
        TextRenderer.render(g, bk, (int) (inside.x + inside.getWidth() * 3 / 4), y);
    }

}
