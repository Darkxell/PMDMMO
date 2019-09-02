package com.darkxell.client.state.menu;

import com.darkxell.common.util.language.Message;

public class MenuOption {
    /** True if this Option can be selected. */
    public boolean isEnabled;
    /** The name of this Option. */
    public Message name;

    public MenuOption(Message name) {
        this(name, true);
    }

    public MenuOption(Message name, boolean isEnabled) {
        this.name = name;
        this.isEnabled = isEnabled;
    }

    public MenuOption(String nameID) {
        this(new Message(nameID));
    }
}