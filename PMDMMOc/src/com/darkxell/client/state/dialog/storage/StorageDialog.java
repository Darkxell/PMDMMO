package com.darkxell.client.state.dialog.storage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.ComplexDialog;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.dialog.PokemonDialogScreen;
import com.darkxell.client.state.menu.components.IntegerSelectionState;
import com.darkxell.client.state.menu.components.IntegerSelectionState.IntegerSelectionListener;
import com.darkxell.client.state.menu.item.ItemContainersMenuState;
import com.darkxell.client.state.menu.item.ItemSelectionListener;
import com.darkxell.client.state.menu.item.MultipleItemsSelectionListener;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class StorageDialog extends ComplexDialog implements ItemSelectionListener, MultipleItemsSelectionListener, IntegerSelectionListener
{
	private static final byte ACTION = 1, QUANTITY = 2, CONFIRM = 3;
	private static final int DEPOSIT = 0, WITHDRAW = 1, EXIT = 2;

	private byte dialogToShow;
	private long max;
	private long quantity;
	private String result;
	private int selectedAction;
	private ItemStack[] selection;
	private PokemonSpecies shopkeeper;

	public StorageDialog(AbstractGraphiclayer background)
	{
		super(background);
		this.shopkeeper = PokemonRegistry.find(115);
	}

	private DialogState actionSelection(boolean isFirst)
	{
		return this
				.newDialog(new OptionDialogScreen(this.shopkeeper, new Message("dialog.storage." + (isFirst ? "intro" : "intro2")),
						new Message("dialog.storage.deposit"), new Message("dialog.storage.withdraw"), new Message("dialog.storage.exit")).setID(ACTION))
				.setOpaque(true);
	}

	@Override
	public DialogState firstState()
	{
		return this.actionSelection(true);
	}

	@Override
	public void itemSelected(ItemStack item, int index)
	{
		this.selection = item == null ? null : new ItemStack[] { item };
		this.onItemsSelected();
	}

	@Override
	public void itemsSelected(ItemStack[] items)
	{
		this.selection = items;
		this.onItemsSelected();
	}

	@Override
	public ComplexDialogAction nextAction(DialogState previous)
	{
		if (previous.currentScreen().id == QUANTITY)
		{
			this.dialogToShow = 0;
			if (this.quantity == -1) return ComplexDialogAction.NEW_DIALOG;
			else return ComplexDialogAction.PAUSE;
		} else if (previous.currentScreen().id == CONFIRM) return ComplexDialogAction.NEW_DIALOG;
		if (this.selectedAction == EXIT) return ComplexDialogAction.TERMINATE;
		return ComplexDialogAction.PAUSE;
	}

	@Override
	public DialogState nextState(DialogState previous)
	{
		if (this.dialogToShow == QUANTITY)
			return this
					.newDialog(new PokemonDialogScreen(this.shopkeeper,
							new Message("dialog.storage.howmany." + (this.selectedAction == WITHDRAW ? "withdraw" : "deposit"))).setID(QUANTITY))
					.setOpaque(true);
		else if (this.dialogToShow == CONFIRM)
		{
			this.dialogToShow = 0;
			String message = "dialog.storage." + this.result;
			if (this.result.equals("ok")) message += "." + (this.selectedAction == WITHDRAW ? "withdraw" : "deposit");
			return this.newDialog(new PokemonDialogScreen(this.shopkeeper, new Message(message)).setID(CONFIRM)).setOpaque(true);
		}
		return this.actionSelection(false);
	}

	public void onConfirmReceived(JsonObject message)
	{
		this.result = message.getString("result", null);
		this.dialogToShow = CONFIRM;
		this.unpause();
	}

	@Override
	protected void onDialogFinished(DialogState dialog)
	{
		DialogScreen screen = dialog.currentScreen();
		if (screen.id == QUANTITY) Persistance.stateManager.setState(new IntegerSelectionState(dialog, dialog, this, 1l, this.max, 1l));
		else if (screen.id == ACTION)
		{
			this.selectedAction = ((OptionDialogScreen) screen).chosenIndex();
			if (this.selectedAction == DEPOSIT)
			{
				ItemContainersMenuState s = new ItemContainersMenuState(dialog, this.background, this, false, Persistance.player.inventory());
				s.setMultipleSelectionListener(this);
				s.isOpaque = true;
				Persistance.stateManager.setState(s);
			}
		}
	}

	@Override
	public AbstractState onFinish(DialogState lastState)
	{
		return (AbstractState) this.background;
	}

	@Override
	public void onIntegerSelected(long selection)
	{
		if (selection == -1) this.unpause();
		else
		{
			this.quantity = selection;
			this.sendRequestOne(this.selectedAction == WITHDRAW ? "withdrawone" : "depositone", this.selection[0], this.quantity);
		}
	}

	private void onItemsSelected()
	{
		if (this.selection == null)
		{
			this.unpause();
			return;
		}
		if (this.selection.length == 1)
		{
			ItemStack i = this.selection[0];
			this.max = i.quantity();
			if (this.selectedAction == WITHDRAW)
				this.max = Math.max(this.max, Persistance.player.inventory().maxSize() - Persistance.player.inventory().size());
			if ((i.item().isStackable || this.selectedAction == WITHDRAW) && max != 1)
			{
				this.dialogToShow = QUANTITY;
				this.unpause();
			}
		} else
		{
			this.sendRequestMany(this.selectedAction == WITHDRAW ? "withdrawmany" : "depositmany", this.selection);
		}
	}

	private void sendRequestMany(String action, ItemStack[] selection)
	{
		JsonArray itemids = new JsonArray();
		for (ItemStack stack : selection)
			itemids.add(stack.id());

		JsonObject root = Json.object();
		root.set("action", action);
		root.set("items", itemids);
		System.out.println(root.toString());
		Persistance.socketendpoint.sendMessage(root.toString());
		Persistance.isCommunicating = true;
	}

	private void sendRequestOne(String action, ItemStack item, long quantity)
	{
		JsonObject root = Json.object();
		root.set("action", action);
		root.set("item", item.id());
		root.set("quantity", quantity);
		System.out.println(root.toString());
		Persistance.socketendpoint.sendMessage(root.toString());
		Persistance.isCommunicating = true;
	}

}
