package com.darkxell.client.state.menu;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;

public abstract class OptionSelectionMenuState<T extends MenuOption> extends AbstractMenuState<T> {

    private T hovered;
    private boolean isOpaque;
    /** The main window to display the options in. */
    private OptionSelectionWindow<T> mainWindow;

    public OptionSelectionMenuState(AbstractGraphiclayer backgroundState) {
        this(backgroundState, false);
    }

    public OptionSelectionMenuState(AbstractGraphiclayer backgroundState, boolean isOpaque) {
        super(backgroundState);
        this.isOpaque = isOpaque;
    }

    protected OptionSelectionWindow<T> createWindow() {
        OptionSelectionWindow<T> window = new OptionSelectionWindow<>(this, this.mainWindowDimensions());
        window.isOpaque = this.isOpaque;
        return window;
    }

    public T getHoveredOption() {
        return this.hovered;
    }

    public OptionSelectionWindow<T> getMainWindow() {
        return this.mainWindow;
    }

    public boolean isOpaque() {
        return this.isOpaque;
    }

    @Override
    public void onMouseClick(int x, int y) {
        super.onMouseClick(x, y);
        boolean inside = this.getMainWindow().inside().contains(x, y);
        if (inside) {
            if (this.getHoveredOption() != null)
                this.onOptionSelected(this.getHoveredOption());
        } else
            this.onExit();
    }

    @Override
    public void onMouseMove(int x, int y) {
        super.onMouseMove(x, y);
        if (this.getMainWindow() == null)
            return;
        this.hovered = this.getMainWindow().optionAt(x, y);
        if (this.hovered != null) {
            ArrayList<T> options = this.currentTab().options();
            for (int i = 0; i < options.size(); i++)
                if (options.get(i) == this.hovered) {
                    this.selection = i;
                    this.onOptionChanged(this.hovered);
                    break;
                }
        }
    }

    @Override
    protected void onTabChanged(MenuTab<T> tab) {
        super.onTabChanged(tab);
        this.mainWindow = this.createWindow();
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        super.render(g, width, height);

        if (this.mainWindow == null)
            this.mainWindow = this.createWindow();
        if (this.tabs.size() != 0)
            this.getMainWindow().render(g, this.currentTab().name, width, height);
    }

    public OptionSelectionMenuState<T> setOpaque(boolean isOpaque) {
        this.isOpaque = isOpaque;
        return this;
    }

    @Override
    public void update() {
        super.update();
        if (this.getMainWindow() != null)
            this.getMainWindow().update();
    }

}
