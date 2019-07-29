package com.darkxell.client.state.menu.components;

import static com.darkxell.client.state.menu.freezone.FriendSelectionState.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.image.pokemon.portrait.Portraits;
import com.darkxell.client.state.menu.freezone.FriendSelectionState;
import com.darkxell.client.state.menu.freezone.FriendSelectionState.FriendMenuOption;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class FriendsWindow extends MenuWindow {

    public final FriendSelectionState state;

    public FriendsWindow(FriendSelectionState state, Rectangle dimensions) {
        super(dimensions);
        this.state = state;
    }

    private void drawGoToMap(Graphics2D g, boolean selected) {
        Message m = new Message(FriendSelectionState.GOTOMAP);
        int x = this.inside().x + this.inside().width / 2, y = this.inside().y + LIST_OFFSET * 3 / 2;
        int boxX = x - TextRenderer.width(m) / 2 - LIST_OFFSET * 2;
        int width = TextRenderer.width(m) + LIST_OFFSET * 4;
        int boxY = y - 2;
        int height = TextRenderer.height() + 4;

        if (selected)
            g.setColor(Palette.MENU_HOVERED);
        else
            g.setColor(Palette.TRANSPARENT_GRAY);
        g.fillRoundRect(boxX, boxY, width, height, 2, 2);

        g.setColor(Palette.TRANSPARENT_GRAY);
        g.drawRoundRect(boxX, boxY, width, height, 2, 2);
        g.drawRoundRect(boxX, boxY, width, height, 2, 2);

        TextRenderer.render(g, m, x - TextRenderer.width(m) / 2, y);

    }

    private void drawPokemon(Graphics2D g, Pokemon pokemon, int x, int y, boolean selected) {
        // If change positions here, check #optionAt(x, y)

        int startX = this.inside().x + LIST_OFFSET, startY = this.inside().y + LIST_OFFSET;
        int realX = x - 1;
        int realY = y;
        if (x == 0) {
            --realY;
            realX += LIST_FRIEND_WIDTH;
        }
        int X = startX + realX * (FRIEND_SLOT_WIDTH + LIST_OFFSET),
                Y = startY + realY * (FRIEND_SLOT_HEIGHT + LIST_OFFSET) + GOTOMAP_HEIGHT;

        if (pokemon != null) {
            BufferedImage sprite = Portraits.portrait(pokemon);
            g.drawImage(sprite, X + FRIEND_OFFSET, Y + FRIEND_OFFSET, FRIEND_SIZE, FRIEND_SIZE, null);
        }

        if (selected)
            g.setColor(Palette.MENU_HOVERED);
        else
            g.setColor(Palette.TRANSPARENT_GRAY);
        g.drawRect(X, Y, FRIEND_SLOT_WIDTH, FRIEND_SLOT_HEIGHT);
        if (selected) {
            g.drawRect(X - 1, Y - 1, FRIEND_SLOT_WIDTH + 2, FRIEND_SLOT_HEIGHT + 2);
            for (int i = 1; i < 2; ++i)
                g.drawRect(X + i, Y + i, FRIEND_SLOT_WIDTH - i * 2, FRIEND_SLOT_HEIGHT - i * 2);
        }
    }

    public int optionAt(int xPos, int yPos) {

        if (this.state.isLoaded()) {
            Message m = new Message(FriendSelectionState.GOTOMAP);
            int x = this.inside().x + this.inside().width / 2, y = this.inside().y + LIST_OFFSET * 3 / 2;
            int boxX = x - TextRenderer.width(m) / 2 - LIST_OFFSET * 2;
            int width = TextRenderer.width(m) + LIST_OFFSET * 4;
            int boxY = y - 2;
            int height = TextRenderer.height() + 4;

            Rectangle r = new Rectangle(boxX, boxY, width, height);
            if (r.contains(xPos, yPos))
                return 0;

            for (x = 0; x < LIST_FRIEND_WIDTH; ++x)
                for (y = 0; y < LIST_FRIEND_HEIGHT; ++y) {
                    int index = y * LIST_FRIEND_WIDTH + x;
                    if (index < this.state.currentTab().options().length
                            && this.state.currentTab().options()[index] != this.state.mapOption) {
                        int startX = this.inside().x + LIST_OFFSET, startY = this.inside().y + LIST_OFFSET;
                        int realX = x - 1;
                        int realY = y;
                        if (x == 0) {
                            --realY;
                            realX += LIST_FRIEND_WIDTH;
                        }
                        int X = startX + realX * (FRIEND_SLOT_WIDTH + LIST_OFFSET),
                                Y = startY + realY * (FRIEND_SLOT_HEIGHT + LIST_OFFSET) + GOTOMAP_HEIGHT;
                        Rectangle poke = new Rectangle(X, Y, FRIEND_SLOT_WIDTH, FRIEND_SLOT_HEIGHT);
                        if (poke.contains(xPos, yPos))
                            return index;
                    }
                }

            x = LIST_FRIEND_WIDTH;
            y = LIST_FRIEND_HEIGHT - 1;
            int index = y * LIST_FRIEND_WIDTH + x;
            if (index < this.state.currentTab().options().length) {
                int startX = this.inside().x + LIST_OFFSET, startY = this.inside().y + LIST_OFFSET;
                int realX = x - 1;
                int realY = y;
                if (x == 0) {
                    --realY;
                    realX += LIST_FRIEND_WIDTH;
                }
                int X = startX + realX * (FRIEND_SLOT_WIDTH + LIST_OFFSET),
                        Y = startY + realY * (FRIEND_SLOT_HEIGHT + LIST_OFFSET) + GOTOMAP_HEIGHT;
                Rectangle poke = new Rectangle(X, Y, FRIEND_SLOT_WIDTH, FRIEND_SLOT_HEIGHT);
                if (poke.contains(xPos, yPos))
                    return index;
            }
        }
        return -1;
    }

    @Override
    public void render(Graphics2D g, Message name, int width, int height) {
        super.render(g, name, width, height);

        if (this.state.isLoaded()) {
            this.drawGoToMap(g, this.state.optionIndex() == 0);

            for (int x = 0; x < LIST_FRIEND_WIDTH; ++x)
                for (int y = 0; y < LIST_FRIEND_HEIGHT; ++y) {
                    int index = y * LIST_FRIEND_WIDTH + x;
                    if (index < this.state.currentTab().options().length
                            && this.state.currentTab().options()[index] != this.state.mapOption) {
                        Pokemon pokemon = ((FriendMenuOption) this.state.currentTab().options()[index]).pokemon;
                        this.drawPokemon(g, pokemon, x, y, index == this.state.optionIndex());
                    }
                }

            int x = LIST_FRIEND_WIDTH, y = LIST_FRIEND_HEIGHT - 1;
            int index = y * LIST_FRIEND_WIDTH + x;
            if (index < this.state.currentTab().options().length) {
                Pokemon pokemon = ((FriendMenuOption) this.state.currentTab().options()[index]).pokemon;
                this.drawPokemon(g, pokemon, x, y, index == this.state.optionIndex());
            }
        } else {
            Message loading = new Message("general.loading")
                    .addSuffix("(" + (this.state.startLoadingCount - this.state.loadingPokemon.size()) + "/"
                            + this.state.startLoadingCount + ")");
            Rectangle inside = this.inside();
            TextRenderer.render(g, loading, inside.x + inside.width / 2 - TextRenderer.width(loading) / 2,
                    inside.y + inside.height / 2 - TextRenderer.height() / 2);
        }

    }

}
