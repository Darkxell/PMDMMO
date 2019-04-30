package com.darkxell.client.state.menu.freezone;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.graphics.TextRenderer;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezone.entity.FriendPokemonEntity;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.menus.MovesMenuState;
import com.darkxell.client.state.menu.menus.TeamMenuState;
import com.darkxell.common.util.language.Message;

public class FriendActionSelectionState extends OptionSelectionMenuState {

	public FriendPokemonEntity friendPokemonEntity;
	private MenuOption join, leave, farewell, summary, moves, back;
	public final AbstractState parent;
	private MenuWindow summaryWindow;

	public FriendActionSelectionState(AbstractState parent, FriendPokemonEntity friendPokemonEntity) {
		super(parent, true);
		this.friendPokemonEntity = friendPokemonEntity;
		this.parent = parent;
		this.createOptions();
	}

	@Override
	protected void createOptions() {
		MenuTab tab = new MenuTab();
		if (Persistence.player.pokemonInZones.containsKey(this.friendPokemonEntity.pokemon.id()))
			tab.addOption(this.join = new MenuOption("ui.friend.join"));
		else
			tab.addOption(this.leave = new MenuOption("ui.friend.leave"));
		tab.addOption(this.farewell = new MenuOption("ui.friend.farewell"));
		tab.addOption(this.summary = new MenuOption("friendareas.summary"));
		tab.addOption(this.moves = new MenuOption("menu.moves"));
		tab.addOption(this.back = new MenuOption("general.back"));
		this.tabs.add(tab);
	}

	@Override
	protected void onExit() {
		Persistence.stateManager.setState(this.parent);
		this.friendPokemonEntity.release();
	}

	@Override
	protected void onOptionSelected(MenuOption option) {
		if (option == this.summary)
			Persistence.stateManager.setState(TeamMenuState
					.createSummaryState(this.background, this, null, this.friendPokemonEntity.pokemon).setOpaque(true));
		else if (option == this.moves)
			Persistence.stateManager.setState(
					new MovesMenuState(this, this.background, false, this.friendPokemonEntity.pokemon).setOpaque(true));
		else if (option == this.back)
			this.onExit();
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		super.render(g, width, height);

		{
			if (this.summaryWindow == null) {
				Rectangle r = this.mainWindowDimensions();
				this.summaryWindow = new MenuWindow(
						new Rectangle((int) (r.getMaxX() + 10), r.y, (int) (width - r.getMaxX() - 20), r.height));
				this.summaryWindow.isOpaque = this.isOpaque;
			}
			this.summaryWindow.render(g, null, width, height);

			int x = this.summaryWindow.inside().x + 10, y = this.summaryWindow.inside().y + 5;
			TextRenderer.render(g, this.friendPokemonEntity.pokemon.getNickname(), x, y);
			y += TextRenderer.height() + TextRenderer.lineSpacing();
			TextRenderer.render(g, new Message("team.level").addReplacement("<lvl>",
					String.valueOf(this.friendPokemonEntity.pokemon.level())), x, y);

			if (this.friendPokemonEntity.pokemon.hasItem()) {
				y += TextRenderer.height() + TextRenderer.lineSpacing();
				TextRenderer.render(g, new Message("ui.friend.item").addReplacement("<item>",
						this.friendPokemonEntity.pokemon.getItem().name()), x, y);
			}
		}
	}

}
