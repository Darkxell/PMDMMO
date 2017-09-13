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
					MenuHudSpriteset.instance.cornerSize.height * 2 + TextRenderer.CHAR_HEIGHT*2);
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
