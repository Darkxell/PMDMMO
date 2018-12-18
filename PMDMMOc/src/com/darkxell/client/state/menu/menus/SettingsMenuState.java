package com.darkxell.client.state.menu.menus;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.ClientSettings.Setting;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.util.language.Message;

public class SettingsMenuState extends OptionSelectionMenuState
{

	private MenuOption controls, hpBars, back;
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
		tab.addOption(this.hpBars = new MenuOption("menu.settings.hp_bars.on"));
		tab.addOption(this.back = new MenuOption("general.back"));
		this.tabs.add(tab);

		this.updateSettingValues();
	}

	@Override
	protected void onExit()
	{
		Persistence.stateManager.setState(this.parent);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		if (option == this.back) this.onExit();
		else if (option == this.hpBars)
		{
			ClientSettings.setSetting(Setting.HP_BARS, !ClientSettings.getBooleanSetting(Setting.HP_BARS));
			this.updateSettingValues();
		} else if (option == this.controls) Persistence.stateManager.setState(new ControlsMenuState(this, this.background).setOpaque(this.isOpaque));
	}

	private void updateSettingValues()
	{
		this.hpBars.name = new Message("menu.settings.hp_bars." + (ClientSettings.getBooleanSetting(Setting.HP_BARS) ? "on"
				: "off"));
	}

}
