package com.darkxell.client.state.dialog.storage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.ComplexDialog;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.menu.item.ItemContainersMenuState;
import com.darkxell.client.state.menu.item.ItemSelectionListener;
import com.darkxell.client.state.menu.item.MultipleItemsSelectionListener;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class StorageDialog extends ComplexDialog implements ItemSelectionListener, MultipleItemsSelectionListener
{
	private static final byte ACTION = 1;
	@SuppressWarnings("unused")
	private static final int DEPOSIT = 0, WITHDRAW = 1, EXIT = 2;

	private int selectedAction;
	private PokemonSpecies shopkeeper;

	public StorageDialog(AbstractGraphiclayer background)
	{
		super(background);
		this.shopkeeper = PokemonRegistry.find(115);
	}

	private DialogState actionSelection(boolean isFirst)
	{
		return this.newDialog(new OptionDialogScreen(this.shopkeeper, new Message("dialog.storage.intro"), new Message("dialog.storage.deposit"),
				new Message("dialog.storage.withdraw"), new Message("dialog.storage.exit")).setID(ACTION)).setOpaque(true);
	}

	@Override
	public DialogState firstState()
	{
		return this.actionSelection(true);
	}

	@Override
	public void itemSelected(ItemStack item, int index)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void itemsSelected(ItemStack[] items, int[] indexes)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public ComplexDialogAction nextAction(DialogState previous)
	{
		return ComplexDialogAction.TERMINATE;
	}

	@Override
	public DialogState nextState(DialogState previous)
	{
		return this.actionSelection(false);
	}

	@Override
	protected void onDialogFinished(DialogState dialog)
	{
		DialogScreen screen = dialog.currentScreen();
		if (screen.id != ACTION) this.selectedAction = ((OptionDialogScreen) screen).chosenIndex();
	}

	@Override
	public AbstractState onFinish(DialogState lastState)
	{
		if (this.selectedAction == DEPOSIT)
		{
			ItemContainersMenuState s = new ItemContainersMenuState(null, this.background, this, false, Persistance.player.inventory());
			s.setMultipleSelectionListener(this);
		}
		return (AbstractState) this.background;
	}

}
