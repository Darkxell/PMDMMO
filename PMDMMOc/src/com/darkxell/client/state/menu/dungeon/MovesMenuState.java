package com.darkxell.client.state.menu.dungeon;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.components.MoveSelectionWindow;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonMove;
import com.darkxell.common.util.Message;

public class MovesMenuState extends AbstractMenuState
{

	public static class MoveMenuOption extends MenuOption
	{

		public final PokemonMove move;

		public MoveMenuOption(PokemonMove move)
		{
			super(move == null ? new Message("", false) : move.move().name());
			this.move = move;
		}

	}

	private Pokemon pokemon;
	private MoveSelectionWindow window;
	private TextWindow windowInfo;

	public MovesMenuState(DungeonState parent)
	{
		super(parent);
		this.pokemon = parent.player.getPokemon();
		this.createOptions();
	}

	@Override
	protected void createOptions()
	{
		MenuTab player = new MenuTab(new Message("moves.title").addReplacement("<pokemon>", this.pokemon.getNickname()));
		for (int i = 0; i < 4; ++i)
			player.addOption(new MoveMenuOption(this.pokemon.move(i)));
		this.tabs.add(player);
	}

	@Override
	protected void onExit()
	{
		Launcher.stateManager.setState(new DungeonMenuState(this.backgroundState));
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (!Keys.isPressed(Keys.KEY_DIAGONAL))
		{
			if (this.tabs.size() != 0)
			{
				if (key == Keys.KEY_LEFT && this.tab > 0) --this.tab;
				else if (key == Keys.KEY_RIGHT && this.tab < this.tabs.size() - 1) ++this.tab;
				else if (key == Keys.KEY_UP) --this.selection;
				else if (key == Keys.KEY_DOWN) ++this.selection;
				else if (key == Keys.KEY_ATTACK) this.onOptionSelected(this.currentOption());

				if (key == Keys.KEY_LEFT || key == Keys.KEY_RIGHT)
				{
					if (this.selection >= this.currentTab().options().length) this.selection = this.currentTab().options().length - 1;
				} else if (key == Keys.KEY_UP || key == Keys.KEY_DOWN)
				{
					if (this.selection == -1) this.selection = this.currentTab().options().length - 1;
					else if (this.selection == this.currentTab().options().length) this.selection = 0;
				}
			}
			if (key == Keys.KEY_MENU || key == Keys.KEY_RUN) this.onExit();
		} else
		{
			boolean success = false;
			if (key == Keys.KEY_UP && this.selection > 0)
			{
				this.pokemon.switchMoves(this.selection, this.selection - 1);
				--this.selection;
				success = true;
			} else if (key == Keys.KEY_DOWN && this.selection < this.currentTab().options().length - 1)
			{
				this.pokemon.switchMoves(this.selection, this.selection + 1);
				++this.selection;
				success = true;
			}

			if (success)
			{
				MovesMenuState s = new MovesMenuState((DungeonState) this.backgroundState);
				s.selection = this.selection;
				s.tab = this.tab;
				Launcher.stateManager.setState(s);
			}
		}
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		System.out.println(option.name);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);

		if (this.window == null) this.window = new MoveSelectionWindow(this, this.mainWindowDimensions(g));
		this.window.render(g, this.currentTab().name, width, height);

		if (this.windowInfo == null)
		{
			Rectangle r = new Rectangle(this.window.dimensions.x, (int) (this.window.dimensions.getMaxY() + 20), width - 40,
					MenuHudSpriteset.instance.cornerSize.height * 2 + TextRenderer.CHAR_HEIGHT * 2);
			this.windowInfo = new TextWindow(r, new Message("moves.info"), false);
		}
		this.windowInfo.render(g, null, width, height);
	}

	@Override
	public void update()
	{
		super.update();
		if (this.window != null) this.window.update();
	}

}
