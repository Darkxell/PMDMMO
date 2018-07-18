package com.darkxell.client.state.menu.menus;

import java.awt.Rectangle;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dialog.OptionDialogScreen;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.components.ControlsWindow;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.language.Message;

public class ControlsMenuState extends OptionSelectionMenuState implements DialogEndListener
{

	public static class ControlMenuOption extends MenuOption
	{
		public final Key key;
		int newValue;
		public final int oldValue;

		public ControlMenuOption(Key key)
		{
			super(key.getName());
			this.key = key;
			this.newValue = this.oldValue = key.keyValue();
		}

		public int newValue()
		{
			return this.newValue;
		}
	}

	private static final String[] tabNames = { "movement", "actions", "menus", "map" };
	private static final Key[][] tabStructure = { { Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT, Key.RUN, Key.DIAGONAL, Key.ROTATE },
			{ Key.ATTACK, Key.MOVE_1, Key.MOVE_2, Key.MOVE_3, Key.MOVE_4, Key.ITEM_1, Key.ITEM_2 },
			{ Key.MENU, Key.INVENTORY, Key.PARTY, Key.PAGE_LEFT, Key.PAGE_RIGHT }, { Key.MAP_UP, Key.MAP_DOWN, Key.MAP_LEFT, Key.MAP_RIGHT, Key.MAP_RESET } };

	public final AbstractMenuState parent;

	public ControlsMenuState(AbstractMenuState parent, AbstractGraphiclayer background)
	{
		super(background);
		this.parent = parent;

		this.createOptions();
	}

	@Override
	protected void createOptions()
	{

		for (int tab = 0; tab < tabStructure.length; ++tab)
		{
			MenuTab t = new MenuTab("key.tab." + tabNames[tab]);
			t.name.addSuffix(" (" + (tab + 1) + "/" + tabStructure.length + ")");

			for (int menu = 0; menu < tabStructure[tab].length; ++menu)
				t.addOption(new ControlMenuOption(tabStructure[tab][menu]));

			this.tabs.add(t);
		}
	}

	@Override
	protected OptionSelectionWindow createWindow()
	{
		ControlsWindow window = new ControlsWindow(this, this.mainWindowDimensions());
		window.isOpaque = this.isOpaque;
		return window;
	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		Rectangle r = super.mainWindowDimensions();
		return new Rectangle(r.x, r.y, PrincipalMainState.displayWidth * 3 / 4, r.height);
	}

	@Override
	public void onDialogEnd(DialogState dialog)
	{
		int selection = ((OptionDialogScreen) dialog.getScreen(1)).chosenIndex();
		if (selection == 0)
		{
			Persistance.stateManager.setState(this.parent);
			for (MenuTab tab : this.tabs())
				for (MenuOption option : tab.options())
				{
					ControlMenuOption o = (ControlMenuOption) option;
					o.key.setValue(o.newValue);
				}
		} else if (selection == 1) Persistance.stateManager.setState(this.parent);
		else if (selection == 2) Persistance.stateManager.setState(this);
	}

	@Override
	protected void onExit()
	{
		OptionDialogScreen confirm = new OptionDialogScreen(new Message("key.save"), new Message("ui.yes"), new Message("ui.no"), new Message("ui.cancel"));
		confirm.id = 1;
		DialogState dialog = new DialogState(this.background, this, confirm);
		dialog.setOpaque(this.isOpaque);
		Persistance.stateManager.setState(dialog);
	}

	public void onKeyValueSelected(ControlMenuOption option, int value)
	{
		option.newValue = value;
		Persistance.stateManager.setState(this);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		Persistance.stateManager.setState(new EditControlState(this.background, this, (ControlMenuOption) option).setOpaque(this.isOpaque));
	}

}
