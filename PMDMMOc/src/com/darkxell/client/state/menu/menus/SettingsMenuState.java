package com.darkxell.client.state.menu.menus;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;

public class SettingsMenuState extends OptionSelectionMenuState
{

	private MenuOption controls, back;
	public final AbstractMenuState parent;

	public SettingsMenuState(AbstractMenuState parent, AbstractGraphiclayer background)
	{
		super(background);
		this.parent = parent;

		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		MenuTab tab = new MenuTab("menu.settings");
		tab.addOption(this.controls = new MenuOption("menu.controls"));
		tab.addOption(this.back = new MenuOption("general.back"));
		this.tabs.add(tab);
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(this.parent);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		if (option == this.back) this.onExit();
		else if (option == this.controls) Persistance.stateManager.setState(new ControlsMenuState(this, this.background));
	}

}
