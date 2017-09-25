package com.darkxell.client.state.menu.dungeon;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.client.state.menu.components.MoveSelectionWindow;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Message;

public class MovesMenuState extends AbstractMenuState {

	public static class MoveMenuOption extends MenuOption {

		public final LearnedMove move;

		public MoveMenuOption(LearnedMove move, boolean isMain) {
			super(move == null ? new Message("", false)
					: move.move().name().addPrefix(isMain || !move.isEnabled ? "  " : "<star> "));
			this.move = move;
		}

	}

	private Pokemon[] pokemon;
	private MoveSelectionWindow window;
	private TextWindow windowInfo;

	public MovesMenuState(DungeonState parent) {
		super(parent);
		this.pokemon = Persistance.player.getTeam();
		this.createOptions();
	}

	@Override
	protected void createOptions() {
		for (Pokemon pokemon : this.pokemon) {
			MenuTab moves = new MenuTab(new Message("moves.title").addReplacement("<pokemon>", pokemon.getNickname()));
			for (int i = 0; i < 4; ++i)
				if (pokemon.move(i) != null)
					moves.addOption(
							new MoveMenuOption(pokemon.move(i), pokemon == Persistance.player.getPokemon()));
			this.tabs.add(moves);
		}
	}

	private boolean isMainSelected() {
		return this.selectedPokemon() == Persistance.player.getPokemon();
	}

	@Override
	protected void onExit() {
		Persistance.stateManager.setState(new DungeonMenuState(this.backgroundState));
	}

	@Override
	public void onKeyPressed(short key) {
		if (!Keys.isPressed(Keys.KEY_DIAGONAL)) {
			if (this.tabs.size() != 0) {
				if (key == Keys.KEY_LEFT && this.tab > 0)
					--this.tab;
				else if (key == Keys.KEY_RIGHT && this.tab < this.tabs.size() - 1)
					++this.tab;
				else if (key == Keys.KEY_UP)
					--this.selection;
				else if (key == Keys.KEY_DOWN)
					++this.selection;
				else if (key == Keys.KEY_ATTACK)
					this.onOptionSelected(this.currentOption());
				else if (key == Keys.KEY_ROTATE)
					this.onOptionInfo(this.currentOption());

				if (key == Keys.KEY_LEFT || key == Keys.KEY_RIGHT) {
					if (this.selection >= this.currentTab().options().length)
						this.selection = this.currentTab().options().length - 1;
					this.onTabChanged(this.currentTab());
				} else if (key == Keys.KEY_UP || key == Keys.KEY_DOWN) {
					if (this.selection == -1)
						this.selection = this.currentTab().options().length - 1;
					else if (this.selection == this.currentTab().options().length)
						this.selection = 0;
					this.onOptionChanged(this.currentOption());
				}
			}
			if (key == Keys.KEY_MENU || key == Keys.KEY_RUN)
				this.onExit();
		} else {
			boolean success = false;
			if (key == Keys.KEY_UP && this.selection > 0) {
				this.selectedPokemon().switchMoves(this.selection, this.selection - 1);
				--this.selection;
				success = true;
			} else if (key == Keys.KEY_DOWN && this.selection < this.currentTab().options().length - 1) {
				this.selectedPokemon().switchMoves(this.selection, this.selection + 1);
				++this.selection;
				success = true;
			}

			if (success) {
				MovesMenuState s = new MovesMenuState(Persistance.dungeonState);
				s.selection = this.selection;
				s.tab = this.tab;
				Persistance.stateManager.setState(s);
			}
		}
	}

	private void onOptionInfo(MenuOption option) {
		DungeonState s = Persistance.dungeonState;
		Persistance.stateManager
				.setState(new InfoState(s, this, new Message[] { ((MoveMenuOption) option).move.move().name() },
						new Message[] { ((MoveMenuOption) option).move.move().name() }));
	}

	@Override
	protected void onOptionSelected(MenuOption option) {
		DungeonState s = Persistance.dungeonState;
		LearnedMove move = ((MoveMenuOption) option).move;

		if (this.isMainSelected()) {
			Persistance.stateManager.setState(s);
			ClientEventProcessor.processEvent(new MoveSelectionEvent(Persistance.floor, move,
					Persistance.player.getDungeonPokemon()));
		} else {
			move.isEnabled = !move.isEnabled;
			MovesMenuState state = new MovesMenuState(s);
			state.tab = this.tab;
			state.selection = this.selection;
			Persistance.stateManager.setState(state);
		}
	}

	@Override
	protected void onTabChanged(MenuTab tab) {
		super.onTabChanged(tab);
		this.windowInfo = null;
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		super.render(g, width, height);

		if (this.window == null)
			this.window = new MoveSelectionWindow(this, this.mainWindowDimensions());
		this.window.render(g, this.currentTab().name, width, height);

		if (this.windowInfo == null) {
			Rectangle r = new Rectangle(this.window.dimensions.x, (int) (this.window.dimensions.getMaxY() + 20),
					width - 40, MenuHudSpriteset.instance.cornerSize.height * 2 + TextRenderer.CHAR_HEIGHT * 4
							+ TextRenderer.LINE_SPACING * 2);
			this.windowInfo = new TextWindow(r,
					new Message(this.isMainSelected() ? "moves.info.main" : "moves.info.ally"), false);
		}
		this.windowInfo.render(g, null, width, height);
	}

	private Pokemon selectedPokemon() {
		return this.pokemon[this.tabIndex()];
	}

	@Override
	public void update() {
		super.update();
		if (this.window != null)
			this.window.update();
	}

}
