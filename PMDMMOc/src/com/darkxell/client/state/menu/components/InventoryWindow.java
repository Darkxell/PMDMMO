package com.darkxell.client.state.menu.components;

import static com.darkxell.client.state.menu.item.ItemContainersMenuState.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.image.Sprites.DungeonSprites;
import com.darkxell.client.resources.image.Sprites.HudSprites;
import com.darkxell.client.state.menu.AbstractMenuState.MenuTab;
import com.darkxell.client.state.menu.item.ItemContainersMenuState;
import com.darkxell.client.state.menu.item.ItemContainersMenuState.MenuItemOption;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.language.Message;

public class InventoryWindow extends MenuWindow {

    public final ItemContainersMenuState state;

    public InventoryWindow(ItemContainersMenuState state, Rectangle dimensions) {
        super(dimensions);
        this.state = state;
    }

    public int optionAt(int xPos, int yPos) {
        int startX = this.inside().x + LIST_OFFSET, startY = this.inside().y + LIST_OFFSET;
        for (int x = 0; x < LIST_ITEM_WIDTH; ++x)
            for (int y = 0; y < LIST_ITEM_HEIGHT; ++y) {
                int index = y * LIST_ITEM_WIDTH + x;
                if (index < this.state.currentTab().options().size()) {
                    int X = startX + x * (ITEM_SLOT + LIST_OFFSET), Y = startY + y * (ITEM_SLOT + LIST_OFFSET);

                    if (xPos >= X && xPos <= X + ITEM_SLOT && yPos >= Y && yPos <= Y + ITEM_SLOT)
                        return index;

                }
            }
        return -1;
    }

    @Override
    public void render(Graphics2D g, Message name, int width, int height) {
        super.render(g, name, width, height);

        int startX = this.inside().x + LIST_OFFSET, startY = this.inside().y + LIST_OFFSET;
        for (int x = 0; x < LIST_ITEM_WIDTH; ++x)
            for (int y = 0; y < LIST_ITEM_HEIGHT; ++y) {
                int index = y * LIST_ITEM_WIDTH + x;
                if (index < this.state.currentTab().options().size()) {
                    ItemStack item = this.state.currentTab().options().get(index).item;
                    int X = startX + x * (ITEM_SLOT + LIST_OFFSET), Y = startY + y * (ITEM_SLOT + LIST_OFFSET);

                    if (index == this.state.optionIndex()) {
                        g.setColor(Palette.MENU_HOVERED);
                        g.fillRect(X + 1, Y + 1, ITEM_SLOT - 1, ITEM_SLOT - 1);
                    }

                    g.setColor(Palette.TRANSPARENT_GRAY);
                    if (this.state.multipleSelection.contains(item)) {
                        g.setColor(Palette.MENU_HOVERED);
                        g.drawRect(X - 2, Y - 2, ITEM_SLOT + 4, ITEM_SLOT + 4);
                        g.drawRect(X - 1, Y - 1, ITEM_SLOT + 2, ITEM_SLOT + 2);
                        g.drawRect(X + 1, Y + 1, ITEM_SLOT - 2, ITEM_SLOT - 2);
                    }
                    g.drawRect(X, Y, ITEM_SLOT, ITEM_SLOT);

                    BufferedImage sprite = DungeonSprites.items.sprite(item);
                    g.drawImage(sprite, X + ITEM_OFFSET, Y + ITEM_OFFSET, null);

                    if (item.item().isStackable || item.quantity() != 1) {
                        Message quantity = new Message(String.valueOf(item.quantity()), false);
                        int mWidth = TextRenderer.width(quantity);
                        TextRenderer.render(g, quantity, X + ITEM_SLOT - mWidth - 1,
                                Y + ITEM_SLOT - TextRenderer.height() - 1);
                    }
                }
            }

        // Tabs
        ArrayList<MenuTab<MenuItemOption>> tabs = this.state.tabs();
        if (tabs.size() != 0) {
            boolean left = tabs.get(0) != this.state.currentTab();
            boolean right = tabs.get(tabs.size() - 1) != this.state.currentTab();
            if (right)
                g.drawImage(HudSprites.menuHud.pageRight(),
                        (int) this.inside.getMaxX() - HudSprites.menuHud.pageRight().getWidth(),
                        this.dimensions.y - HudSprites.menuHud.pageRight().getHeight() / 3, null);
            if (left)
                g.drawImage(HudSprites.menuHud.pageLeft(),
                        (int) this.inside.getMaxX() - HudSprites.menuHud.pageLeft().getWidth()
                                - HudSprites.menuHud.pageRight().getWidth() - 5,
                        this.dimensions.y - HudSprites.menuHud.pageLeft().getHeight() / 3, null);
        }
    }
}
