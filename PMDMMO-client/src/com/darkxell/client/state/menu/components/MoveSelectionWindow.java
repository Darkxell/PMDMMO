package com.darkxell.client.state.menu.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.image.Sprites.HudSprites;
import com.darkxell.client.state.menu.menus.MovesMenuState;
import com.darkxell.client.state.menu.menus.MovesMenuState.MoveMenuOption;
import com.darkxell.common.util.language.Message;

public class MoveSelectionWindow extends OptionSelectionWindow<MoveMenuOption> {

    public static final int ppLength = TextRenderer.width("00/00");

    public MoveSelectionWindow(MovesMenuState menu, Rectangle dimensions) {
        super(menu, dimensions);
    }

    @Override
    protected void drawOption(Graphics2D g, MoveMenuOption option, int x, int y) {
        TextRenderer.render(g, option.name, x, y);
        String pp = option.move.pp() + "/" + option.move.maxPP();
        if (option.move.pp() == 0)
            pp = "<red>" + pp + "</color>";
        TextRenderer.render(g, pp, (int) (this.dimensions.getMaxX() - ppLength - MARGIN_X), y);
        TextRenderer.setColor(null);

        if ((this.cursor > 9 || !this.menu.isMain()) && this.menu.currentOption() == option)
            g.drawImage(this.menu.isMain() ? HudSprites.menuHud.selectionArrow() : HudSprites.menuHud.selectedArrow(),
                    x - HudSprites.menuHud.selectionArrow().getWidth() - 4,
                    y + TextRenderer.height() / 2 - HudSprites.menuHud.selectedArrow().getHeight() / 2, null);

        y += TextRenderer.height() + TextRenderer.lineSpacing();
    }

    @Override
    public void render(Graphics2D g, Message name, int width, int height) {
        super.render(g, name, width, height);

        int x = MARGIN_X + this.dimensions.x - 1;
        int y = MARGIN_Y + this.dimensions.y + TextRenderer.lineSpacing() / 2;
        ArrayList<MoveMenuOption> options = this.menu.currentTab().options();
        for (MoveMenuOption option : options)
            if (options.indexOf(option) != options.size() - 1) {
                y += TextRenderer.height();
                g.setColor(Color.WHITE);
                g.drawLine(x, y, (int) this.dimensions.getMaxX() - MARGIN_X, y);
                g.setColor(Color.BLACK);
                g.drawLine(x, y + 1, (int) this.dimensions.getMaxX() - MARGIN_X, y + 1);
                y += TextRenderer.lineSpacing();
            }
    }

}
