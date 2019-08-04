package com.darkxell.client.state.dialog.friendarea;

import java.awt.Rectangle;

import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.common.zones.FriendArea;

public class FriendAreaSummaryWindow extends MenuWindow {

    public final FriendArea area;

    public FriendAreaSummaryWindow(FriendArea area, Rectangle dimensions) {
        super(dimensions);
        this.area = area;
    }

}
