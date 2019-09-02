package com.darkxell.client.state.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.resources.image.hud.MenuStateHudSpriteset;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Message;

public abstract class AbstractMenuState<T extends MenuOption> extends AbstractState {

    public static class MenuTab<T extends MenuOption> {
        public final Message name;
        private ArrayList<T> options;

        public MenuTab() {
            this((Message) null);
        }

        public MenuTab(Message name) {
            this.name = name;
            this.options = new ArrayList<T>();
        }

        public MenuTab(String nameID) {
            this(new Message(nameID));
        }

        public MenuTab<T> addOption(T option) {
            this.options.add(option);
            return this;
        }

        public int height() {
            return this.options.size() * (TextRenderer.height() + TextRenderer.lineSpacing());
        }

        @SuppressWarnings("unchecked")
        public ArrayList<T> options() {
            return (ArrayList<T>) this.options.clone();
        }

        public int width() {
            int width = 0;
            for (MenuOption option : this.options)
                width = Math.max(width, TextRenderer.width(option.name));
            width = Math.max(width, TextRenderer.width(this.name) + MenuStateHudSpriteset.cornerSize.width * 2);
            return width;
        }

    }

    /** The state to draw behind this menu State. */
    public final AbstractGraphiclayer background;
    protected int selection = 0;
    /** The currently selected option. */
    protected int tab = 0;
    /** The tabs of this Menu. */
    protected ArrayList<MenuTab<T>> tabs;

    public AbstractMenuState(AbstractGraphiclayer background) {
        this.background = background;
        this.tabs = new ArrayList<MenuTab<T>>();
    }

    /** Creates this Menu's options. */
    protected abstract void createOptions();

    public T currentOption() {
        if (this.tabs.size() == 0)
            return null;
        return this.currentTab().options.get(this.selection);
    }

    public MenuTab<T> currentTab() {
        if (this.tabs.size() == 0)
            return null;
        return this.tabs.get(this.tab);
    }

    /** @return This Window's dimensions. */
    protected Rectangle mainWindowDimensions() {
        int width = this.currentTab().width(), height = this.currentTab().height();
        width += OptionSelectionWindow.MARGIN_X * 2;
        height += OptionSelectionWindow.MARGIN_Y * 2;

        return new Rectangle(16, 32, width, height);
    }

    /*
     * protected Rectangle mainWindowDimensions() { int width = 0, height = 0; for (MenuTab tab : this.tabs) { width =
     * Math.max(width, tab.width()); height = Math.max(height, tab.height()); } width += OptionSelectionWindow.MARGIN_X
     * * 2; height += OptionSelectionWindow.MARGIN_Y * 2; return new Rectangle(16, 32, width, height); }
     */

    /** Called when the player presses the "back" button. */
    protected abstract void onExit();

    @Override
    public void onKeyPressed(Key key) {
        if (this.tabs.size() != 0) {
            if ((key == Key.LEFT || key == Key.PAGE_LEFT) && this.tab > 0)
                --this.tab;
            else if ((key == Key.RIGHT || key == Key.PAGE_RIGHT) && this.tab < this.tabs.size() - 1)
                ++this.tab;
            else if (key == Key.UP)
                --this.selection;
            else if (key == Key.DOWN)
                ++this.selection;
            else if (key == Key.ATTACK) {
                if (this.currentOption().isEnabled) {
                    this.onOptionSelected(this.currentOption());
                    SoundManager.playSound("ui-select");
                } else
                    SoundManager.playSound("ui-back");
            }

            if (key == Key.LEFT || key == Key.RIGHT || key == Key.PAGE_RIGHT || key == Key.PAGE_RIGHT) {
                if (this.selection >= this.currentTab().options.size())
                    this.selection = this.currentTab().options.size() - 1;
                this.onTabChanged(this.currentTab());
                this.onOptionChanged(this.currentOption());
                SoundManager.playSound("ui-move");
            } else if (key == Key.UP || key == Key.DOWN) {
                if (this.selection == -1)
                    this.selection = this.currentTab().options.size() - 1;
                else if (this.selection == this.currentTab().options.size())
                    this.selection = 0;
                this.onOptionChanged(this.currentOption());
                SoundManager.playSound("ui-move");
            }
        }
        if (key == Key.MENU || key == Key.RUN) {
            SoundManager.playSound("ui-back");
            this.onExit();
        }
    }

    @Override
    public void onKeyReleased(Key key) {
    }

    protected void onOptionChanged(T option) {
    }

    /** Called when the player chooses the input Option. */
    protected abstract void onOptionSelected(T option);

    protected void onTabChanged(MenuTab<T> tab) {
    }

    public int optionIndex() {
        return this.selection;
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        if (this.background != null)
            this.background.render(g, width, height);
    }

    public int tabIndex() {
        return this.tab;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<MenuTab<T>> tabs() {
        return (ArrayList<MenuTab<T>>) this.tabs.clone();
    }

    @Override
    public void update() {
        if (this.background != null)
            this.background.update();
    }

}
