package com.darkxell.client.state.menu.dungeon.item;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.client.state.menu.dungeon.DungeonMenuState;
import com.darkxell.client.state.menu.dungeon.TeamMenuState;
import com.darkxell.client.state.menu.dungeon.TeamMenuState.TeamMemberSelectionListener;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ItemSwappedEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class ItemContainersMenuState extends OptionSelectionMenuState implements ItemActionSource, ItemSelectionListener, TeamMemberSelectionListener
{
	private static final int MAX_HEIGHT = 10;

	private final ItemContainer[] containers;
	private ItemAction currentAction = null;
	private final int[] indexOffset;
	private final ItemSelectionListener listener;

	public ItemContainersMenuState(DungeonState parent, ItemContainer... containers)
	{
		this(parent, null, containers);
	}

	public ItemContainersMenuState(DungeonState parent, ItemSelectionListener listener, ItemContainer... containers)
	{
		super(parent);
		this.listener = listener;

		ArrayList<ItemContainer> c = new ArrayList<ItemContainer>();
		ArrayList<Integer> of = new ArrayList<Integer>();
		for (ItemContainer container : containers)
		{
			int s = container.size();
			int o = 0;
			do
			{
				c.add(container);
				of.add(o);
				o += MAX_HEIGHT;
				s -= MAX_HEIGHT;
			} while (s > 0);
		}

		this.containers = new ItemContainer[c.size()];
		this.indexOffset = new int[of.size()];
		for (int i = 0; i < this.containers.length; ++i)
		{
			this.containers[i] = c.get(i);
			this.indexOffset[i] = of.get(i);
		}

		this.createOptions();
	}

	private ItemContainer container()
	{
		return this.containers[this.tabIndex()];
	}

	@Override
	protected void createOptions()
	{
		int inv = 1;
		for (int c = 0; c < this.containers.length; ++c)
		{
			ItemContainer container = this.containers[c];
			if (c != 0 && container == this.containers[c - 1]) ++inv;
			else inv = 1;
			MenuTab tab = new MenuTab(container.containerName().addReplacement("<index>", Integer.toString(inv)));
			this.tabs.add(tab);
			for (int i = 0; i < MAX_HEIGHT && this.indexOffset[c] + i < container.size(); ++i)
				tab.addOption(new MenuOption(container.getItem(this.indexOffset[c] + i).name()));
		}
	}

	private int itemIndex()
	{
		return this.optionIndex() + this.indexOffset[this.tabIndex()];
	}

	@Override
	public void itemSelected(ItemStack item, int index)
	{
		if (item == null) Persistance.stateManager.setState(this);
		else
		{
			Persistance.stateManager.setState(Persistance.dungeonState);
			Persistance.eventProcessor.processEvent(new ItemSwappedEvent(Persistance.floor, ItemAction.SWAP, Persistance.player.getDungeonLeader(),
					Persistance.player.inventory(), index, Persistance.player.getDungeonLeader().tile(), 0));
		}
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(new DungeonMenuState(this.backgroundState));
	}

	@Override
	public void onKeyPressed(short key)
	{
		super.onKeyPressed(key);
		if (key == Keys.KEY_MAP_RESET && this.container() == Persistance.player.inventory())
		{
			SoundManager.playSound("ui-sort");
			Persistance.player.inventory().sort();
			ArrayList<ItemContainer> containers = new ArrayList<ItemContainer>();
			for (ItemContainer c : this.containers)
				if (!containers.contains(c)) containers.add(c);
			Persistance.stateManager.setState(new ItemContainersMenuState(Persistance.dungeonState, containers.toArray(new ItemContainer[containers.size()])));
		}
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		ItemContainer container = this.container();
		ItemStack i = container.getItem(this.itemIndex());

		if (this.listener == null)
		{
			ArrayList<ItemAction> actions = container.legalItemActions();
			actions.addAll(i.item().getLegalActions(true));
			actions.remove(Persistance.player.getDungeonLeader().tile().getItem() == null ? ItemAction.SWITCH : ItemAction.PLACE);
			if (Persistance.player.inventory().isFull())
			{
				actions.remove(ItemAction.GET);
				actions.remove(ItemAction.TAKE);
			}
			ItemAction.sort(actions);

			Persistance.stateManager.setState(new ItemActionSelectionState(this, this, actions));
		} else this.listener.itemSelected(i, this.itemIndex());
	}

	@Override
	public void performAction(ItemAction action)
	{
		DungeonState s = Persistance.dungeonState;
		Persistance.stateManager.setState(s);
		ItemContainer container = this.container();
		int index = this.itemIndex();
		ItemStack i = container.getItem(index);
		DungeonPokemon user = Persistance.player.getDungeonLeader();

		this.currentAction = action;
		if (action == ItemAction.USE)
		{
			if (i.item().usedOnTeamMember()) Persistance.stateManager.setState(new TeamMenuState(s, this));
			else Persistance.eventProcessor.processEvent(new ItemSelectionEvent(Persistance.floor, i.item(), user, null, container, index));
		} else if (action == ItemAction.GET || action == ItemAction.TAKE) Persistance.eventProcessor.processEvent(
				new ItemMovedEvent(Persistance.floor, action, user, container, 0, user.player().inventory(), user.player().inventory().canAccept(i)));
		else if (action == ItemAction.GIVE) Persistance.stateManager.setState(new TeamMenuState(s, this));
		else if (action == ItemAction.PLACE)
			Persistance.eventProcessor.processEvent(new ItemMovedEvent(Persistance.floor, action, user, container, index, user.tile(), 0));
		else if (action == ItemAction.SWITCH)
			Persistance.eventProcessor.processEvent(new ItemSwappedEvent(Persistance.floor, action, user, container, index, user.tile(), 0));
		else if (action == ItemAction.SWAP) Persistance.stateManager.setState(new ItemContainersMenuState(s, this, Persistance.player.inventory()));
		else if (action == ItemAction.INFO)
			Persistance.stateManager.setState(new InfoState(s, this, new Message[] { i.item().name() }, new Message[] { i.info() }));
	}

	@Override
	public ItemStack selectedItem()
	{
		return this.container().getItem(this.optionIndex());
	}

	@Override
	public void teamMemberSelected(Pokemon pokemon)
	{
		Persistance.stateManager.setState(Persistance.dungeonState);
		DungeonState s = Persistance.dungeonState;
		Persistance.stateManager.setState(s);
		ItemContainer container = this.container();
		int index = this.itemIndex();
		ItemStack i = container.getItem(index);
		DungeonPokemon user = Persistance.player.getDungeonLeader();

		switch (this.currentAction)
		{
			case GIVE:
				if (pokemon.getItem() != null) Persistance.eventProcessor.processEvent(new ItemSwappedEvent(Persistance.floor, ItemAction.GIVE,
						Persistance.player.getDungeonLeader(), Persistance.player.inventory(), this.itemIndex(), pokemon, 0));

				else Persistance.eventProcessor.processEvent(new ItemMovedEvent(Persistance.floor, ItemAction.GIVE, Persistance.player.getDungeonLeader(),
						Persistance.player.inventory(), this.itemIndex(), pokemon, 0));
				break;

			case USE:
				Persistance.eventProcessor
						.processEvent(new ItemSelectionEvent(Persistance.floor, i.item(), user, pokemon.getDungeonPokemon(), container, index));
				break;

			default:
				break;
		}

	}

}
