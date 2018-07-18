package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.PlayerLoadingState;
import com.darkxell.client.state.dialog.storage.StorageDialog;
import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Inventory;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class InventoryRequestHandler extends MessageHandler
{

	public static Inventory readInventory(JsonObject message)
	{
		DBInventory dbi = new DBInventory();
		dbi.read(message.get("object").asObject());
		Inventory i = new Inventory(dbi);

		if (message.get("items") != null && message.get("items").isArray())
		{
			for (JsonValue it : message.get("items").asArray())
				if (it.isObject())
				{
					DBItemstack item = new DBItemstack();
					item.read(it.asObject());
					i.addReadItem(new ItemStack(item));
				}
		}

		return i;
	}

	@Override
	public void handleMessage(JsonObject message)
	{
		AbstractState state = Persistance.stateManager.getCurrentState();
		if (state != null && state instanceof PlayerLoadingState) ((PlayerLoadingState) state).onInventoryReceived(message);
		else if (Persistance.currentDialog != null && Persistance.currentDialog instanceof StorageDialog)
			((StorageDialog) Persistance.currentDialog).onInventoryReceived(message);
	}

}
