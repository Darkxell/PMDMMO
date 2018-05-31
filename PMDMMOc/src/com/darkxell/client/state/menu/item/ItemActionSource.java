package com.darkxell.client.state.menu.item;

import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;

public interface ItemActionSource
{

	public void performAction(ItemAction action);

	public ItemStack selectedItem();

}
