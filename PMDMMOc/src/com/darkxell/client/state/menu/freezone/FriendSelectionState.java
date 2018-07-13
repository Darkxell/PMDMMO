package com.darkxell.client.state.menu.freezone;

import static com.darkxell.client.resources.images.pokemon.PokemonPortrait.PORTRAIT_SIZE;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.freezone.FreezoneExploreState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Direction;

public class FriendSelectionState extends AbstractMenuState
{

	public static class FriendMenuOption extends MenuOption
	{

		Pokemon pokemon = null;
		public final long pokemonid;

		public FriendMenuOption(long pokemonid)
		{
			super("");
			this.pokemonid = pokemonid;
		}

		public boolean isLoaded()
		{
			return this.pokemon != null;
		}

	}

	public static final int LIST_FRIEND_WIDTH = 5, LIST_FRIEND_HEIGHT = 8, MAX_FRIEND_COUNT = LIST_FRIEND_WIDTH * LIST_FRIEND_HEIGHT;
	public static final int LIST_OFFSET = 5, FRIEND_SLOT = PORTRAIT_SIZE + 6, FRIEND_OFFSET = (FRIEND_SLOT - PORTRAIT_SIZE) / 2;
	public static final int WIDTH = (FRIEND_SLOT + LIST_OFFSET) * LIST_FRIEND_WIDTH + LIST_OFFSET + MenuWindow.MARGIN_X,
			HEIGHT = (FRIEND_SLOT + LIST_OFFSET) * LIST_FRIEND_HEIGHT + LIST_OFFSET + MenuWindow.MARGIN_Y;

	private MenuOption mapOption;
	private int selection;

	public FriendSelectionState(AbstractState backgroundState)
	{
		super(backgroundState);
	}

	@Override
	protected void createOptions()
	{
		ArrayList<DatabaseIdentifier> mons = Persistance.player.getData().pokemonsinzones;
		this.mapOption = new MenuOption("friendareas.gotomap");
		MenuTab current = new MenuTab("friendareas.title");
		int indexInTab = 0, index = 0;
		for (int i = 0; i < mons.size(); ++i)
		{
			MenuOption option;
			if (indexInTab == 0) option = this.mapOption;
			else
			{
				long id = mons.get(index++).id;
				option = new FriendMenuOption(id);
				((FriendMenuOption) option).pokemon = Persistance.player.pokemonInZones.get(id);
			}
			current.addOption(option);

			if (indexInTab >= MAX_FRIEND_COUNT + 1)
			{
				indexInTab = 0;
				this.tabs.add(current);
				current = new MenuTab("friendareas.title");
			}
		}
	}

	@Override
	protected void onExit()
	{
		Persistance.currentplayer.x += 1;
		Persistance.currentplayer.renderer().sprite().setFacingDirection(Direction.EAST);
		Persistance.stateManager.setState(new FreezoneExploreState());
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (this.tabs.size() != 0)
		{
			if (key == Keys.KEY_PAGE_LEFT && this.tab > 0) --this.tab;
			else if (key == Keys.KEY_PAGE_RIGHT && this.tab < this.tabs.size() - 1) ++this.tab;
			else if (key == Keys.KEY_LEFT) --this.selection;
			else if (key == Keys.KEY_RIGHT) ++this.selection;
			else if (key == Keys.KEY_UP) this.selection -= LIST_FRIEND_WIDTH;
			else if (key == Keys.KEY_DOWN) this.selection += LIST_FRIEND_WIDTH;
			else if (key == Keys.KEY_ATTACK)
			{
				this.onOptionSelected(this.currentOption());
				SoundManager.playSound("ui-select");
			}

			if (key == Keys.KEY_PAGE_LEFT || key == Keys.KEY_PAGE_RIGHT)
			{
				if (this.selection >= this.currentTab().options().length) this.selection = this.currentTab().options().length - 1;
				this.onTabChanged(this.currentTab());
				SoundManager.playSound("ui-move");
			} else if (key == Keys.KEY_UP || key == Keys.KEY_DOWN || key == Keys.KEY_LEFT || key == Keys.KEY_RIGHT)
			{
				if (this.selection >= this.currentTab().options().length) this.selection %= LIST_FRIEND_WIDTH;
				while (this.selection < 0)
					this.selection += this.currentTab().options().length;
				while (this.selection >= this.currentTab().options().length)
					--this.selection;
				this.onOptionChanged(this.currentOption());
				SoundManager.playSound("ui-move");
			}
		}
		if (key == Keys.KEY_MENU || key == Keys.KEY_RUN)
		{
			SoundManager.playSound("ui-back");
			this.onExit();
		}
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		if (option == this.mapOption) System.out.println("Going to map");
		else
		{
			FriendMenuOption o = (FriendMenuOption) option;
			if (o.isLoaded()) System.out.println("Visiting " + o.pokemon.getNickname());
		}
	}

}
