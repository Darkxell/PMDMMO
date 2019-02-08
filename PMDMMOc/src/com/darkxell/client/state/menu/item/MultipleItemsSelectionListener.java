package com.darkxell.client.state.menu.item;

import com.darkxell.common.item.ItemStack;

public interface MultipleItemsSelectionListener {

    /**
     * Called when the Items have been selected or the player cancelled.
     *
     * @param items - The Items that were selected. null if cancelled.
     */
    public void itemsSelected(ItemStack[] items);

}
