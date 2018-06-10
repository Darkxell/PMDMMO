package com.darkxell.client.state.menu.dungeon;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.components.MoveSelectionWindow;
import com.darkxell.client.state.menu.components.OptionSelectionWindow;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.event.move.MoveEnabledEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveSwitchedEvent;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class MovesMenuState extends OptionSelectionMenuState
{

	public static class MoveMenuOption extends MenuOption
	{

		public final LearnedMove move;

		public MoveMenuOption(LearnedMove move, boolean isMain)
		{
			super((move == null ? new Message("", false) : move.move().name().addPrefix(isMain || !move.isEnabled() ? "  " : "<star> ")));
			this.move = move;
		}
	}

	/** True if moves can be ordered in this State. */
	protected boolean canOrder = true;
	private Pokemon[] pokemon;
	private MoveSelectionWindow window;
	private TextWindow windowInfo;

	public MovesMenuState(DungeonState parent, Pokemon... pokemon)
	{
		super(parent);
		this.pokemon = pokemon;
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		for (Pokemon pokemon : this.pokemon)
		{
			MenuTab moves = new MenuTab(new Message("moves.title").addReplacement("<pokemon>", pokemon.getNickname()));
			for (int i = 0; i < 4; ++i)
				if (pokemon.move(i) != null) moves.addOption(new MoveMenuOption(pokemon.move(i), pokemon == Persistance.player.getTeamLeader()));
			this.tabs.add(moves);
		}
	}

	@Override
	public OptionSelectionWindow getMainWindow()
	{
		return this.window;
	}

	protected Message infoText()
	{
		return new Message(this.isMainSelected() ? "moves.info.main" : "moves.info.ally");
	}

	private boolean isMainSelected()
	{
		return this.selectedPokemon() == Persistance.player.getTeamLeader();
	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		Rectangle r = super.mainWindowDimensions();
		return new Rectangle(r.x, r.y, r.width + MoveSelectionWindow.ppLength,
				r.height + (4 - this.currentTab().options().length) * (TextRenderer.height() + TextRenderer.lineSpacing()));
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(new DungeonMenuState(this.backgroundState));
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (!this.canOrder || !Keys.isPressed(Keys.KEY_DIAGONAL))
		{
			if (this.tabs.size() != 0)
			{
				if (key == Keys.KEY_LEFT && this.tab > 0) --this.tab;
				else if (key == Keys.KEY_RIGHT && this.tab < this.tabs.size() - 1) ++this.tab;
				else if (key == Keys.KEY_UP) --this.selection;
				else if (key == Keys.KEY_DOWN) ++this.selection;
				else if (key == Keys.KEY_ATTACK) this.onOptionSelected(this.currentOption());
				else if (key == Keys.KEY_ROTATE)
				{
					this.onOptionInfo(this.currentOption());
					SoundManager.playSound("ui-select");
				}

				if (key == Keys.KEY_LEFT || key == Keys.KEY_RIGHT)
				{
					if (this.selection >= this.currentTab().options().length) this.selection = this.currentTab().options().length - 1;
					this.onTabChanged(this.currentTab());
					SoundManager.playSound("ui-move");
				} else if (key == Keys.KEY_UP || key == Keys.KEY_DOWN)
				{
					if (this.selection == -1) this.selection = this.currentTab().options().length - 1;
					else if (this.selection == this.currentTab().options().length) this.selection = 0;
					this.onOptionChanged(this.currentOption());
					SoundManager.playSound("ui-move");
				}
			}
			if (key == Keys.KEY_MENU || key == Keys.KEY_RUN)
			{
				this.onExit();
				SoundManager.playSound("ui-back");
			}
		} else
		{
			int from = -1, to = -1;

			boolean success = false;
			if (key == Keys.KEY_UP && this.selection > 0)
			{
				from = this.selection;
				to = this.selection - 1;
				--this.selection;
				success = true;
				SoundManager.playSound("ui-sort");
			} else if (key == Keys.KEY_DOWN && this.selection < this.currentTab().options().length - 1)
			{
				from = this.selection;
				to = this.selection + 1;
				++this.selection;
				success = true;
				SoundManager.playSound("ui-sort");
			}

			if (success)
			{
				Persistance.eventProcessor().processEvent(new MoveSwitchedEvent(Persistance.floor, this.selectedPokemon(), from, to).setPAE());

				MovesMenuState s = new MovesMenuState(Persistance.dungeonState, this.pokemon);
				s.selection = this.selection;
				s.tab = this.tab;
				Persistance.stateManager.setState(s);
			}
		}
	}

	@Override
	public void onMouseRightClick(int x, int y)
	{
		super.onMouseRightClick(x, y);
		if (this.getHoveredOption() != null) this.onOptionInfo(this.getHoveredOption());
	}

	private void onOptionInfo(MenuOption option)
	{
		DungeonState s = Persistance.dungeonState;
		Persistance.stateManager.setState(new MoveInfoState(((MoveMenuOption) option).move.move(), s, this));
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		DungeonState s = Persistance.dungeonState;
		LearnedMove move = ((MoveMenuOption) option).move;

		if (this.isMainSelected())
		{
			Persistance.stateManager.setState(s);
			Persistance.eventProcessor().processEvent(new MoveSelectionEvent(Persistance.floor, move, Persistance.player.getDungeonLeader()).setPAE());
		} else
		{
			Persistance.eventProcessor().processEvent(new MoveEnabledEvent(Persistance.floor, move, !move.isEnabled()).setPAE());
			MovesMenuState state = new MovesMenuState(s, this.pokemon);
			state.tab = this.tab;
			state.selection = this.selection;
			Persistance.stateManager.setState(state);
		}
	}

	@Override
	protected void onTabChanged(MenuTab tab)
	{
		super.onTabChanged(tab);
		this.windowInfo = null;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.window == null) this.window = new MoveSelectionWindow(this, this.mainWindowDimensions());
		super.render(g, width, height);

		if (this.windowInfo == null)
		{
			Rectangle r = new Rectangle(this.window.dimensions.x, (int) (this.window.dimensions.getMaxY() + 20), width - 40,
					MenuHudSpriteset.cornerSize.height * 2 + TextRenderer.height() * 4 + TextRenderer.lineSpacing() * 2);
			this.windowInfo = new TextWindow(r, this.infoText(), false);
		}
		this.windowInfo.render(g, null, width, height);
	}

	private Pokemon selectedPokemon()
	{
		return this.pokemon[this.tabIndex()];
	}

}
