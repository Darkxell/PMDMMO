package com.darkxell.client.state.menu;

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

    public static interface ActionSelectionCallback {
        /**
         * Called when the player selects an action.
         * 
         * @param option - The option that was selected. Get its index to test its value. May be null if the player
         *               exited the menu.
         */
        public void actionSelected(ActionMenuOption option);
    }

    private static Message[] optionMessagesFromString(String[] optionIDs) {
        Message[] options = new Message[optionIDs.length];
        for (int i = 0; i < options.length; ++i)
            options[i] = new Message(optionIDs[i]);
        return options;
    }

    public final ActionSelectionCallback callback;
    private final Message[] options;

    public SimpleActionSelectionMenuState(AbstractMenuState<?> parent, ActionSelectionCallback callback,
            Message... options) {
        super(parent);
        if (callback == null)
            throw new NullPointerException("Callback can't be null");
        if (options == null || options.length == 0)
            throw new IllegalArgumentException("Options can't be null and must have at least one option.");
        this.callback = callback;
        this.options = options;
    }

    public SimpleActionSelectionMenuState(AbstractMenuState<?> parent, ActionSelectionCallback callback,
            String... optionIDs) {
        this(parent, callback, optionMessagesFromString(optionIDs));
    }

    @Override
    protected void createOptions() {
        MenuTab<ActionMenuOption> tab = new MenuTab<>();
        for (int i = 0; i < this.options.length; ++i)
            tab.addOption(new ActionMenuOption(this.options[i], i));
        this.tabs.add(tab);
    }

    @Override
    protected void onExit() {
        this.callback.actionSelected(null);
    }

    @Override
    protected void onOptionSelected(ActionMenuOption option) {
        this.callback.actionSelected(option);
    }

}
