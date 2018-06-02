package com.darkxell.client.state.menu.item;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.launchable.messagehandlers.ItemActionHandler.ItemActionMessageHandler;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
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
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ItemContainersMenuState extends OptionSelectionMenuState
		implements ItemActionSource, ItemSelectionListener, TeamMemberSelectionListener, ItemActionMessageHandler
{
	private static final int MAX_HEIGHT = 10;

	private int[] containerOffset;
	private final ItemContainer[] containers;
	private ItemAction currentAction = null;
	private final int[] indexOffset;
	public final boolean inDungeon;
	private final ItemSelectionListener listener;
	private final AbstractState parent;

	// Server communication fields
	private ItemAction selectedAction;
	private ItemContainer selectedContainer;
	private int selectedItemIndex;
	private Pokemon selectedPokemon;

	public ItemContainersMenuState(AbstractState parent, AbstractState background, boolean inDungeon, ItemContainer... containers)
	{
		this(parent, background, null, inDungeon, containers);
	}

	public ItemContainersMenuState(AbstractState parent, AbstractState background, ItemSelectionListener listener, boolean inDungeon,
			ItemContainer... containers)
	{
		super(background);
		this.parent = parent;
		this.listener = listener;
		this.inDungeon = inDungeon;

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
		return this.containers[this.tabIndex() + this.containerOffset[this.tabIndex()]];
	}

	@Override
	protected void createOptions()
	{
		ArrayList<Integer> containerOffsets = new ArrayList<>();
		int inv = 1, offset = 0;
		for (int c = 0; c < this.containers.length; ++c)
		{
			ItemContainer container = this.containers[c];
			if (c != 0 && container == this.containers[c - 1]) ++inv;
			else inv = 1;
			MenuTab tab = new MenuTab(container.containerName().addReplacement("<index>", Integer.toString(inv)));
			if (container.size() == 0)
			{
				++offset;
				continue;
			} else containerOffsets.add(offset);
			this.tabs.add(tab);
			for (int i = 0; i < MAX_HEIGHT && this.indexOffset[c] + i < container.size(); ++i)
				tab.addOption(new MenuOption(container.getItem(this.indexOffset[c] + i).name()));
		}

		this.containerOffset = new int[containerOffsets.size()];
		for (int i = 0; i < this.containerOffset.length; ++i)
			this.containerOffset[i] = containerOffsets.get(i);
	}

	@Override
	public void handleMessage(JsonObject message)
	{
		if (!Persistance.isCommunicating) return;
		Persistance.isCommunicating = false;
		String result = message.getString("value", null);
		if (result == null)
		{
			Logger.e("Invalid itemaction result: " + result);
			return;
		}
		System.out.println("Received result: " + result);
	}

	private int itemIndex()
	{
		return this.optionIndex() + this.indexOffset[this.tabIndex()];
	}

	@Override
	public void itemSelected(ItemStack item, int index)
	{
		if (item == null) Persistance.stateManager.setState(this);
		else if (this.inDungeon)
		{
			Persistance.stateManager.setState(Persistance.dungeonState);
			Persistance.eventProcessor.processEvent(new ItemSwappedEvent(Persistance.floor, ItemAction.SWAP, Persistance.player.getDungeonLeader(),
					Persistance.player.inventory(), index, Persistance.player.getDungeonLeader().tile(), 0));
		}
	}

	@Override
	protected void onExit()
	{
		Persistance.stateManager.setState(this.parent);
	}

	@Override
	public void onKeyPressed(short key)
	{
		super.onKeyPressed(key);
		if (key == Keys.KEY_MAP_RESET && this.container() == Persistance.player.inventory())
		{
			SoundManager.playSound("ui-sort");
			Persistance.player.inventory().sort();
			this.reloadContainers();
		}
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		ItemContainer container = this.container();
		ItemStack i = container.getItem(this.itemIndex());

		if (this.listener == null)
		{
			ArrayList<ItemAction> actions = container.legalItemActions(this.inDungeon);
			actions.addAll(i.item().getLegalActions(this.inDungeon));
			if (this.inDungeon) actions.remove(Persistance.player.getDungeonLeader().tile().getItem() == null ? ItemAction.SWITCH : ItemAction.PLACE);
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
		DungeonState dungeonState = Persistance.dungeonState;
		AbstractState nextState = this;
		if (this.inDungeon) nextState = dungeonState;
		ItemContainer container = this.container();
		int index = this.itemIndex();
		ItemStack i = container.getItem(index);
		DungeonPokemon user = Persistance.player.getDungeonLeader();

		this.selectedAction = null;
		this.selectedContainer = null;
		this.selectedItemIndex = -1;
		this.selectedPokemon = null;

		this.currentAction = action;
		if (action == ItemAction.USE)
		{
			if (i.item().usedOnTeamMember()) nextState = new TeamMenuState(this, dungeonState);
			else Persistance.eventProcessor.processEvent(new ItemSelectionEvent(Persistance.floor, i.item(), user, null, container, index));
		} else if (action == ItemAction.TRASH)
		{
			Persistance.isCommunicating = true;
			nextState = null;

			JsonObject payload = Json.object();
			payload.add("action", "itemaction");
			payload.add("value", "trash");
			payload.add("item", i.getData().id);

			Persistance.socketendpoint.sendMessage(payload.toString());
			this.selectedAction = action;
			this.selectedContainer = container;
			this.selectedItemIndex = index;

			// container.deleteItem(index);
		} else if (action == ItemAction.GET || action == ItemAction.TAKE)
		{
			if (this.inDungeon) Persistance.eventProcessor.processEvent(
					new ItemMovedEvent(Persistance.floor, action, user, container, 0, user.player().inventory(), user.player().inventory().canAccept(i)));
			else if (action == ItemAction.TAKE)
			{
				Persistance.isCommunicating = true;
				nextState = null;

				JsonObject payload = Json.object();
				payload.add("action", "itemaction");
				payload.add("value", "take");
				payload.add("item", i.getData().id);
				payload.add("pokemon", ((Pokemon) container).getData().id);

				Persistance.socketendpoint.sendMessage(payload.toString());
				this.selectedAction = action;
				this.selectedContainer = container;
				this.selectedItemIndex = index;
				this.selectedPokemon = (Pokemon) container;
				/* user.player().inventory().addItem(container.getItem(0)); container.deleteItem(0); */
			}
		} else if (action == ItemAction.GIVE) nextState = new TeamMenuState(this, this.backgroundState, this);
		else if (action == ItemAction.PLACE)
			Persistance.eventProcessor.processEvent(new ItemMovedEvent(Persistance.floor, action, user, container, index, user.tile(), 0));
		else if (action == ItemAction.SWITCH)
			Persistance.eventProcessor.processEvent(new ItemSwappedEvent(Persistance.floor, action, user, container, index, user.tile(), 0));
		else if (action == ItemAction.SWAP) nextState = new ItemContainersMenuState(this, dungeonState, true, Persistance.player.inventory());
		else if (action == ItemAction.INFO)
			nextState = new InfoState(this.backgroundState, this, new Message[] { i.item().name() }, new Message[] { i.info() });

		if (nextState == this) this.reloadContainers();
		else if (nextState != null) Persistance.stateManager.setState(nextState);
	}

	private void reloadContainers()
	{
		boolean found = false;
		ArrayList<ItemContainer> containers = new ArrayList<ItemContainer>();
		for (ItemContainer c : this.containers)
		{
			if (!containers.contains(c)) containers.add(c);
			if (c.size() != 0) found = true;
		}
		if (found) Persistance.stateManager.setState(
				new ItemContainersMenuState(this.parent, this.backgroundState, this.inDungeon, containers.toArray(new ItemContainer[containers.size()])));
		else Persistance.stateManager.setState(this.parent);
	}

	@Override
	public ItemStack selectedItem()
	{
		return this.container().getItem(this.optionIndex());
	}

	@Override
	public void teamMemberSelected(Pokemon pokemon)
	{
		AbstractState nextState = this;
		if (this.inDungeon) nextState = Persistance.dungeonState;
		ItemContainer container = this.container();
		int index = this.itemIndex();
		ItemStack i = container.getItem(index);
		DungeonPokemon user = Persistance.player.getDungeonLeader();

		switch (this.currentAction)
		{
			case GIVE:
				if (this.inDungeon)
				{
					if (pokemon.getItem() != null) Persistance.eventProcessor.processEvent(new ItemSwappedEvent(Persistance.floor, ItemAction.GIVE,
							Persistance.player.getDungeonLeader(), Persistance.player.inventory(), this.itemIndex(), pokemon, 0));
					else Persistance.eventProcessor.processEvent(new ItemMovedEvent(Persistance.floor, ItemAction.GIVE, Persistance.player.getDungeonLeader(),
							Persistance.player.inventory(), this.itemIndex(), pokemon, 0));
				} else
				{
					Persistance.isCommunicating = true;
					nextState = null;

					JsonObject payload = Json.object();
					payload.add("action", "itemaction");
					payload.add("value", "give");
					payload.add("item", i.getData().id);
					payload.add("pokemon", pokemon.getData().id);

					Persistance.socketendpoint.sendMessage(payload.toString());
					this.selectedAction = this.currentAction;
					this.selectedContainer = container;
					this.selectedItemIndex = index;
					this.selectedPokemon = pokemon;

					/* container.deleteItem(index); pokemon.setItem(i); */
				}
				break;

			case USE:
				Persistance.eventProcessor
						.processEvent(new ItemSelectionEvent(Persistance.floor, i.item(), user, pokemon.getDungeonPokemon(), container, index));
				break;

			default:
				break;
		}

		if (nextState == this) this.reloadContainers();
		else if (nextState != null) Persistance.stateManager.setState(nextState);

	}

}
