package com.darkxell.client.state.menu.components;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.image.Sprites.HudSprites;
import com.darkxell.client.state.menu.AbstractMenuState.MenuOption;
import com.darkxell.client.state.menu.AbstractMenuState.MenuTab;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.util.language.Message;

public class OptionSelectionWindow extends MenuWindow {

    protected int cursor = 0;
    protected final OptionSelectionMenuState menu;

    public OptionSelectionWindow(OptionSelectionMenuState menu, Rectangle dimensions) {
        super(dimensions);
        this.menu = menu;
    }

    protected void drawOption(Graphics2D g, MenuOption option, int x, int y) {
        TextRenderer.render(g, option.name, x, y);
        if ((this.cursor > 9 || !this.menu.isMain()) && this.menu.currentOption() == option)
            g.drawImage(
                    this.menu.isMain() ? HudSprites.menuHud.selectionArrow()
                            : HudSprites.menuHud.selectedArrow(),
                    x - HudSprites.menuHud.selectionArrow().getWidth() - 4,
                    y + TextRenderer.height() / 2 - HudSprites.menuHud.selectedArrow().getHeight() / 2, null);
    }

    public MenuOption optionAt(int x, int y) {
        int xCurrent = MARGIN_X + this.dimensions.x;
        int yCurrent = MARGIN_Y + this.dimensions.y + TextRenderer.lineSpacing() / 2;
        for (MenuOption option : this.menu.currentTab().options()) {
            if (new Rectangle(xCurrent - 1, yCurrent - 1, this.dimensions.width - MARGIN_X * 2 + 2,
                    TextRenderer.height() + 2).contains(x, y))
                return option;
            yCurrent += TextRenderer.height() + TextRenderer.lineSpacing();
        }
        return null;
    }

    @Override
    public void render(Graphics2D g, Message name, int width, int height) {
        super.render(g, name, width, height); // If changing here, check
                                              // MoveSelectionWindow

        // Tabs
        MenuTab[] tabs = this.menu.tabs();
        if (tabs.length != 0) {
            boolean left = tabs[0] != this.menu.currentTab();
            boolean right = tabs[tabs.length - 1] != this.menu.currentTab();
            if (right)
                g.drawImage(HudSprites.menuHud.tabRight(),
                        (int) this.inside.getMaxX() - HudSprites.menuHud.tabRight().getWidth(),
                        this.dimensions.y - HudSprites.menuHud.tabRight().getHeight() / 3, null);
            if (left)
                g.drawImage(HudSprites.menuHud.tabLeft(),
                        (int) this.inside.getMaxX() - HudSprites.menuHud.tabLeft().getWidth()
                                - HudSprites.menuHud.tabRight().getWidth() - 5,
                        this.dimensions.y - HudSprites.menuHud.tabLeft().getHeight() / 3, null);

            // Text
            int x = MARGIN_X + this.dimensions.x;
            int y = MARGIN_Y + this.dimensions.y + TextRenderer.lineSpacing() / 2;
            for (MenuOption option : this.menu.currentTab().options()) {
                if (this.menu.getHoveredOption() == option) {
                    g.setColor(Palette.MENU_HOVERED);
                    g.fillRect(x - 1, y - 1, this.dimensions.width - MARGIN_X * 2 + 2, TextRenderer.height() + 2);
                }
                this.drawOption(g, option, x, y);
                y += TextRenderer.height() + TextRenderer.lineSpacing();
            }
        }
    }

    public void update() {
        ++this.cursor;
        if (this.cursor > 20)
            this.cursor = 0;
    }

}
