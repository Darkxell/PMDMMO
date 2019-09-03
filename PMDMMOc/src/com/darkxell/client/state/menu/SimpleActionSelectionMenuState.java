package com.darkxell.client.state.menu;

import java.awt.Rectangle;

import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.menu.SimpleActionSelectionMenuState.ActionMenuOption;
import com.darkxell.common.util.language.Message;

/**
 * Menu State listing simple actions to choose from. Intended to be used as a submenu of another menu state.
 */
public class SimpleActionSelectionMenuState extends OptionSelectionMenuState<ActionMenuOption> {

    public static class ActionMenuOption extends MenuOption {
        public final int index;

        public ActionMenuOption(Message name, int index) {
            super(name);
            this.index = index;
        }
    }

    public static interface ActionSelectionParent {
        /**
         * Called when the player selects an action.
         * 
         * @param option - The option that was selected. Get its index to test its value. May be null if the player
         *               exited the menu.
         */
        public void actionSelected(ActionMenuOption option);

        /**
         * @return                   The dimensions of the action selection window.
         * 
         * @param  defaultDimensions - The dimensions by default. May be reused for x, y, width or height values,
         *                           computed as if it were the only option selection menu.
         */
        public Rectangle actionSelectionWindowDimensions(Rectangle defaultDimensions);
    }

    private static Message[] optionMessagesFromString(String[] optionIDs) {
        Message[] options = new Message[optionIDs.length];
        for (int i = 0; i < options.length; ++i)
            options[i] = new Message(optionIDs[i]);
        return options;
    }

    private final Message[] options;
    public final ActionSelectionParent parent;

    public SimpleActionSelectionMenuState(AbstractGraphiclayer background, ActionSelectionParent parent,
            Message... options) {
        super(background);
        if (parent == null)
            throw new NullPointerException("Callback can't be null.");
        if (options == null || options.length == 0)
            throw new IllegalArgumentException("Options can't be null and must have at least one option.");
        this.parent = parent;
        this.options = options;
        this.createOptions();
    }

    public SimpleActionSelectionMenuState(AbstractGraphiclayer background, ActionSelectionParent callback,
            String... optionIDs) {
        this(background, callback, optionMessagesFromString(optionIDs));
    }

    @Override
    protected void createOptions() {
        MenuTab<ActionMenuOption> tab = new MenuTab<>();
        for (int i = 0; i < this.options.length; ++i)
            tab.addOption(new ActionMenuOption(this.options[i], i));
        this.tabs.add(tab);
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        return this.parent.actionSelectionWindowDimensions(super.mainWindowDimensions());
    }

    @Override
    protected void onExit() {
        this.parent.actionSelected(null);
    }

    @Override
    protected void onOptionSelected(ActionMenuOption option) {
        this.parent.actionSelected(option);
    }

}
