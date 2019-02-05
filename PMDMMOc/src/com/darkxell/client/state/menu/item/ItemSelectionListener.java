package com.darkxell.client.state.menu.item;

import com.darkxell.common.item.ItemStack;

public interface ItemSelectionListener {

    /**
     * Called when the Item has been selected or the player cancelled.
     * 
     * @param item  - The Item that was selected. null if cancelled.
     * @param index - The index of the Item in the inventory. -1 if cancelled.
     */
    public void itemSelected(ItemStack item, int index);

}
