package com.darkxell.client.state.menu.dungeon;

import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Message;

public class MovesMenuState extends OptionSelectionMenuState
{
	private Pokemon pokemon;

	public MovesMenuState(DungeonState parent)
	{
		super(parent);
		this.pokemon = parent.player.getPokemon();
	}

	@Override
	protected void createOptions()
	{
		MenuTab player = new MenuTab(new Message("moves.title").addReplacement("<pokemon>", this.pokemon.getNickname()));
		this.tabs.add(player);
	}

	@Override
	protected void onExit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		// TODO Auto-generated method stub

	}

}
