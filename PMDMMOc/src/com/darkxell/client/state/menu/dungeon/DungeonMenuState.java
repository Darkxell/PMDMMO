package com.darkxell.client.state.menu.dungeon;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.item.ItemContainersMenuState;
import com.darkxell.client.state.menu.menus.SettingsMenuState;
import com.darkxell.client.state.menu.menus.TeamMenuState;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class DungeonMenuState extends OptionSelectionMenuState
{

	private MenuOption moves, items, team, settings, ground;

	public DungeonMenuState(AbstractGraphiclayer background)
	{
		super(background);
		this.createOptions();
	}

	public ItemContainersMenuState createInventoryState()
	{
		DungeonState s = Persistance.dungeonState;
		ArrayList<ItemContainer> containers = new ArrayList<ItemContainer>();
		containers.add(Persistance.player.inventory());
		containers.add(Persistance.player.getDungeonLeader().tile());
		for (Pokemon pokemon : Persistance.player.getTeam())
			containers.add(pokemon);

		boolean found = false;
		for (ItemContainer container : containers)
			if (container.size() != 0)
			{
				found = true;
				break;
			}

		if (!found)
		{
			this.onExit();
			s.logger.showMessage(new Message("inventory.empty"));
			return null;
		} else
		{
			return new ItemContainersMenuState(this, s, true, containers.toArray(new ItemContainer[containers.size()]));
		}
	}

	@Override
	protected void createOptions()
	{
		MenuTab tab = new MenuTab();
		tab.addOption((this.moves = new MenuOption("menu.moves")));
		tab.addOption((this.items = new MenuOption("menu.items")));
		tab.addOption((this.team = new MenuOption("menu.team")));
		tab.addOption((this.settings = new MenuOption("menu.settings")));
		tab.addOption((this.ground = new MenuOption("menu.ground")));
		this.tabs.add(tab);
	}

	public TeamMenuState createPartyState()
	{
		return new TeamMenuState(this, Persistance.dungeonState);
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState((AbstractState) this.background);
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		DungeonState s = Persistance.dungeonState;
		if (option == this.moves) Persistance.stateManager.setState(new MovesMenuState(s, Persistance.player.getTeam()));
		else if (option == this.items)
		{} else if (option == this.team) Persistance.stateManager.setState(this.createPartyState());
		else if (option == this.settings) Persistance.stateManager.setState(new SettingsMenuState(this, this.background));
		else if (option == this.ground)
		{
			this.onExit();
			if (Persistance.player.getDungeonLeader().tile().type() == TileType.STAIR) Persistance.stateManager.setState(new StairMenuState());
			else if (Persistance.player.getDungeonLeader().tile().getItem() == null) s.logger.showMessage(new Message("ground.empty"));
			else
			{
				Persistance.stateManager.setState(new ItemContainersMenuState(this, s, true, Persistance.player.getDungeonLeader().tile()));
			}
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		TextRenderer.render(g, "Belly: " + Persistance.player.getDungeonLeader().getBelly() + "/" + Persistance.player.getDungeonLeader().getBellySize(), 0, 0);
	}
}
