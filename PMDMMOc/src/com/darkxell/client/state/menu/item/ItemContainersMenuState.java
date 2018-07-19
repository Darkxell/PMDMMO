package com.darkxell.client.state.menu.item;

import static com.darkxell.client.resources.images.tilesets.ItemsSpriteset.ITEM_SIZE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.launchable.messagehandlers.ItemActionHandler.ItemActionMessageHandler;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.resources.images.MenuHudSpriteset;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.DialogState.DialogEndListener;
import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.client.state.menu.components.InventoryWindow;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.components.TextWindow;
import com.darkxell.client.state.menu.menus.TeamMenuState;
import com.darkxell.client.state.menu.menus.TeamMenuState.TeamMemberSelectionListener;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ItemSwappedEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ItemContainersMenuState extends AbstractMenuState
		implements ItemActionSource, ItemSelectionListener, TeamMemberSelectionListener, ItemActionMessageHandler
{
	public static class MenuItemOption extends MenuOption
	{
		public final ItemStack item;

		public MenuItemOption(ItemStack item)
		{
			super(item.name());
			this.item = item;
		}
	}

	public static final int LIST_ITEM_WIDTH = 5, LIST_ITEM_HEIGHT = 8, MAX_ITEM_COUNT = LIST_ITEM_WIDTH * LIST_ITEM_HEIGHT;
	public static final int LIST_OFFSET = 5, ITEM_SLOT = ITEM_SIZE + 6, ITEM_OFFSET = (ITEM_SLOT - ITEM_SIZE) / 2;
	public static final int WIDTH = (ITEM_SLOT + LIST_OFFSET) * LIST_ITEM_WIDTH + LIST_OFFSET + MenuWindow.MARGIN_X,
			HEIGHT = (ITEM_SLOT + LIST_OFFSET) * LIST_ITEM_HEIGHT + LIST_OFFSET + MenuWindow.MARGIN_Y;

	public boolean allowMultipleSelection = false;
	private int[] containerOffset;
	private final ItemContainer[] containers;
	private ItemAction currentAction = null;
	private final int[] indexOffset;
	public final boolean inDungeon;
	public boolean isOpaque;
	private final ItemSelectionListener listener;
	private MultipleItemsSelectionListener multipleListener = null;
	/** Maximum number of items that can be selected (when multiple selection is allowed.) */
	public int multipleMax = Integer.MAX_VALUE;
	public ArrayList<ItemStack> multipleSelection = new ArrayList<>();
	private TextWindow multipleWindowInfo, multipleWindowMax;
	private MenuWindow nameWindow;
	private final AbstractState parent;
	private InventoryWindow window;

	public ItemContainersMenuState(AbstractState parent, AbstractGraphiclayer background, boolean inDungeon, ItemContainer... containers)
	{
		this(parent, background, null, inDungeon, containers);
	}

	public ItemContainersMenuState(AbstractState parent, AbstractGraphiclayer background, ItemSelectionListener listener, boolean inDungeon,
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
				o += MAX_ITEM_COUNT;
				s -= MAX_ITEM_COUNT;
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

	public Point actionSelectionWindowLocation()
	{
		return new Point(this.nameWindow.dimensions.x, (int) (this.nameWindow.dimensions.getMaxY() + 5));
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
			for (int i = 0; i < MAX_ITEM_COUNT && this.indexOffset[c] + i < container.size(); ++i)
				tab.addOption(new MenuItemOption(container.getItem(this.indexOffset[c] + i)));
		}

		this.containerOffset = new int[containerOffsets.size()];
		for (int i = 0; i < this.containerOffset.length; ++i)
			this.containerOffset[i] = containerOffsets.get(i);
	}

	public InventoryWindow getMainWindow()
	{
		return this.window;
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
		} else if (result.equals("givesuccess")) this.itemgiveSuccess(message.getLong("item", -1), message.getLong("pokemon", -1));
		else if (result.equals("takesuccess")) this.itemTakeSuccess(message.getLong("item", -1), message.getLong("pokemon", -1));
		else if (result.equals("trashsuccess")) this.itemTrashSuccess(message.getLong("item", -1));
		else if (result.equals("pokemonhasitem")) this.itemGiveFailureHasItem(message.getLong("pokemon", -1));
		else if (result.equals("pokemonhasnoitem")) this.itemTakeFailureHasNoItem(message.getLong("pokemon", -1));
		else if (result.equals("inventoryfull")) this.itemTakeFailureInvFull(message.getLong("pokemon", -1));
		else if (result.equals("cantbetrashed")) this.itemCantBeTrashed();
		else Logger.e("Invalid itemaction result: " + result);
	}

	private void itemCantBeTrashed()
	{
		ItemContainersMenuState nextState = this;
		DialogEndListener listener = new DialogEndListener() {
			@Override
			public void onDialogEnd(DialogState dialog)
			{
				Persistance.stateManager.setState(nextState);
			}
		};
		Persistance.stateManager.setState(new DialogState(this.background, listener, new DialogScreen(new Message("item.trash.impossible"))));
	}

	private void itemGiveFailureHasItem(long pokemonid)
	{
		Pokemon pokemon = null;
		for (int i = 0; i < 3; ++i)
			if (Persistance.player.getMember(i).getData().id == pokemonid)
			{
				pokemon = Persistance.player.getMember(i);
				break;
			}

		if (pokemon != null)
		{
			ItemContainersMenuState nextState = this;
			DialogEndListener listener = new DialogEndListener() {
				@Override
				public void onDialogEnd(DialogState dialog)
				{
					Persistance.stateManager.setState(nextState);
				}
			};
			Persistance.stateManager.setState(new DialogState(this.background, listener,
					new DialogScreen(new Message("inventory.give.alreadyhasitem").addReplacement("<pokemon>", pokemon.getNickname()))));
		} else Persistance.stateManager.setState(this);
	}

	private void itemgiveSuccess(long itemid, long pokemonid)
	{
		Inventory inv = Persistance.player.inventory();
		int index = -1;
		for (int i = 0; i < inv.size(); ++i)
			if (inv.getItem(i).getData().id == itemid)
			{
				index = i;
				break;
			}

		Pokemon pokemon = null;
		for (int i = 0; i < 3; ++i)
			if (Persistance.player.getMember(i).getData().id == pokemonid)
			{
				pokemon = Persistance.player.getMember(i);
				break;
			}

		if (index != -1 && pokemon != null)
		{
			ItemStack item = inv.getItem(index);
			pokemon.setItem(inv.getItem(index));
			inv.deleteItem(index);

			ItemContainersMenuState nextState = this;
			DialogEndListener listener = new DialogEndListener() {
				@Override
				public void onDialogEnd(DialogState dialog)
				{
					nextState.reloadContainers();
				}
			};

			Persistance.stateManager.setState(new DialogState(this.background, listener,
					new DialogScreen(new Message("inventory.give").addReplacement("<item>", item.name()).addReplacement("<pokemon>", pokemon.getNickname()))));
		} else Persistance.stateManager.setState(this);
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
			Persistance.eventProcessor().processEvent(new ItemSwappedEvent(Persistance.floor, ItemAction.SWAP, Persistance.player.getDungeonLeader(),
					Persistance.player.inventory(), index, Persistance.player.getDungeonLeader().tile(), 0));
		}
	}

	private void itemTakeFailureHasNoItem(long pokemonid)
	{
		Pokemon pokemon = null;
		for (int i = 0; i < 3; ++i)
			if (Persistance.player.getMember(i).getData().id == pokemonid)
			{
				pokemon = Persistance.player.getMember(i);
				break;
			}

		if (pokemon != null)
		{
			ItemContainersMenuState nextState = this;
			DialogEndListener listener = new DialogEndListener() {
				@Override
				public void onDialogEnd(DialogState dialog)
				{
					Persistance.stateManager.setState(nextState);
				}
			};
			Persistance.stateManager.setState(new DialogState(this.background, listener,
					new DialogScreen(new Message("inventory.take.hasnoitem").addReplacement("<pokemon>", pokemon.getNickname()))));
		} else Persistance.stateManager.setState(this);
	}

	private void itemTakeFailureInvFull(long pokemonid)
	{
		Pokemon pokemon = null;
		for (int i = 0; i < 3; ++i)
			if (Persistance.player.getMember(i).getData().id == pokemonid)
			{
				pokemon = Persistance.player.getMember(i);
				break;
			}

		if (pokemon != null)
		{
			ItemContainersMenuState nextState = this;
			DialogEndListener listener = new DialogEndListener() {
				@Override
				public void onDialogEnd(DialogState dialog)
				{
					Persistance.stateManager.setState(nextState);
				}
			};
			Persistance.stateManager.setState(new DialogState(this.background, listener,
					new DialogScreen(new Message("inventory.take.full").addReplacement("<pokemon>", pokemon.getNickname()))));
		} else Persistance.stateManager.setState(this);
	}

	private void itemTakeSuccess(long itemid, long pokemonid)
	{
		Inventory inv = Persistance.player.inventory();

		Pokemon pokemon = null;
		for (int i = 0; i < 3; ++i)
			if (Persistance.player.getMember(i).getData().id == pokemonid)
			{
				pokemon = Persistance.player.getMember(i);
				break;
			}

		if (pokemon != null)
		{
			ItemStack item = pokemon.getItem();
			inv.addItem(item);
			pokemon.deleteItem(0);

			ItemContainersMenuState nextState = this;
			DialogEndListener listener = new DialogEndListener() {
				@Override
				public void onDialogEnd(DialogState dialog)
				{
					nextState.reloadContainers();
				}
			};

			Persistance.stateManager.setState(new DialogState(this.background, listener,
					new DialogScreen(new Message("inventory.taken").addReplacement("<item>", item.name()).addReplacement("<pokemon>", pokemon.getNickname()))));
		} else Persistance.stateManager.setState(this);
	}

	private void itemTrashSuccess(long itemid)
	{
		Inventory inv = Persistance.player.inventory();
		int index = -1;
		for (int i = 0; i < inv.size(); ++i)
			if (inv.getItem(i).getData().id == itemid)
			{
				index = i;
				break;
			}

		if (index != -1)
		{
			ItemStack item = inv.getItem(index);
			inv.deleteItem(index);

			ItemContainersMenuState nextState = this;
			DialogEndListener listener = new DialogEndListener() {
				@Override
				public void onDialogEnd(DialogState dialog)
				{
					nextState.reloadContainers();
				}
			};

			Persistance.stateManager.setState(
					new DialogState(this.background, listener, new DialogScreen(new Message("item.trash.success").addReplacement("<item>", item.name()))));
		} else Persistance.stateManager.setState(this);
	}

	@Override
	protected Rectangle mainWindowDimensions()
	{
		Rectangle superRect = super.mainWindowDimensions();
		return new Rectangle(superRect.x, superRect.y - (this.allowMultipleSelection ? 8 : 0), WIDTH, HEIGHT);
	}

	@Override
	protected void onExit()
	{
		if (this.listener != null) this.listener.itemSelected(null, -1);
		else if (this.allowMultipleSelection) this.multipleListener.itemsSelected(null);
		else Persistance.stateManager.setState(this.parent);
	}

	@Override
	public void onKeyPressed(Key key)
	{
		if (this.tabs.size() != 0)
		{
			if (key == Key.PAGE_LEFT && this.tab > 0) --this.tab;
			else if (key == Key.PAGE_RIGHT && this.tab < this.tabs.size() - 1) ++this.tab;
			else if (key == Key.LEFT) --this.selection;
			else if (key == Key.RIGHT) ++this.selection;
			else if (key == Key.UP) this.selection -= LIST_ITEM_WIDTH;
			else if (key == Key.DOWN) this.selection += LIST_ITEM_WIDTH;
			else if (key == Key.ATTACK)
			{
				this.onOptionSelected(this.currentOption());
				SoundManager.playSound("ui-select");
			} else if (key == Key.ROTATE && this.allowMultipleSelection)
			{
				ItemStack i = this.selectedItem();
				if (this.multipleSelection.contains(i))
				{
					SoundManager.playSound("ui-select");
					this.multipleSelection.remove(i);
				} else if (this.multipleSelection.size() < this.multipleMax)
				{
					SoundManager.playSound("ui-select");
					this.multipleSelection.add(i);
				}
			}

			if (key == Key.PAGE_LEFT || key == Key.PAGE_RIGHT)
			{
				if (this.selection >= this.currentTab().options().length) this.selection = this.currentTab().options().length - 1;
				this.onTabChanged(this.currentTab());
				SoundManager.playSound("ui-move");
			} else if (key == Key.UP || key == Key.DOWN || key == Key.LEFT || key == Key.RIGHT)
			{
				if (this.selection >= this.currentTab().options().length) this.selection %= LIST_ITEM_WIDTH;
				while (this.selection < 0)
					this.selection += this.currentTab().options().length;
				while (this.selection >= this.currentTab().options().length)
					--this.selection;
				this.onOptionChanged(this.currentOption());
				SoundManager.playSound("ui-move");
			}
		}
		if (key == Key.MENU || key == Key.RUN)
		{
			SoundManager.playSound("ui-back");
			this.onExit();
		}
		if (key == Key.MAP_RESET && this.container() == Persistance.player.inventory())
		{
			SoundManager.playSound("ui-sort");
			Persistance.player.inventory().sort();
			this.reloadContainers();
		}
	}

	@Override
	public void onMouseClick(int x, int y)
	{
		super.onMouseClick(x, y);
		if (this.window != null)
		{
			int option = this.window.optionAt(x, y);
			if (option != -1)
			{
				this.selection = option;
				this.onOptionSelected(this.currentOption());
			} else if (!this.window.dimensions.contains(new Point(x, y)))
			{
				SoundManager.playSound("ui-back");
				this.onExit();
			}
		}
	}

	@Override
	public void onMouseMove(int x, int y)
	{
		super.onMouseMove(x, y);
		if (this.window != null)
		{
			int option = this.window.optionAt(x, y);
			if (option != -1) this.selection = option;
		}
	}

	@Override
	protected void onOptionSelected(MenuOption option)
	{
		ItemContainer container = this.container();
		ItemStack i = container.getItem(this.itemIndex());

		if (this.listener == null && !this.allowMultipleSelection)
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

			Persistance.stateManager.setState(new ItemActionSelectionState(this, this, actions).setOpaque(this.isOpaque));
		} else if (this.allowMultipleSelection && (this.multipleSelection.size() > 1 || this.listener == null))
		{
			ItemStack[] items = new ItemStack[this.multipleSelection.size()];
			for (int s = 0; s < items.length; ++s)
				items[s] = this.multipleSelection.get(s);
			this.multipleListener.itemsSelected(items);
		} else
		{
			if (this.allowMultipleSelection && this.multipleSelection.size() == 1) i = this.multipleSelection.get(0);
			this.listener.itemSelected(i, this.itemIndex());
		}
	}

	@Override
	protected void onTabChanged(MenuTab tab)
	{
		super.onTabChanged(tab);
		this.updateNameWindow();
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

		this.currentAction = action;
		if (action == ItemAction.USE)
		{
			if (i.item().usedOnTeamMember()) nextState = new TeamMenuState(this, dungeonState).setOpaque(this.isOpaque);
			else Persistance.eventProcessor().processEvent(new ItemSelectionEvent(Persistance.floor, i.item(), user, null, container, index));
		} else if (action == ItemAction.TRASH)
		{
			nextState = null;
			Persistance.isCommunicating = true;

			JsonObject payload = Json.object();
			payload.add("action", "itemaction");
			payload.add("value", "trash");
			payload.add("item", i.getData().id);

			Persistance.socketendpoint.sendMessage(payload.toString());
		} else if (action == ItemAction.GET || action == ItemAction.TAKE)
		{
			if (this.inDungeon && user.player().inventory().canAccept(i) != -1)
				Persistance.eventProcessor().processEvent(new ItemMovedEvent(Persistance.floor, action, user, container, 0, user.player().inventory(), -1));
			else if (action == ItemAction.TAKE)
			{
				nextState = null;
				Persistance.isCommunicating = true;

				JsonObject payload = Json.object();
				payload.add("action", "itemaction");
				payload.add("value", "take");
				payload.add("item", i.getData().id);
				payload.add("pokemon", ((Pokemon) container).getData().id);

				Persistance.socketendpoint.sendMessage(payload.toString());
			}
		} else if (action == ItemAction.GIVE) nextState = new TeamMenuState(this, this.background, this).setOpaque(this.isOpaque);
		else if (action == ItemAction.PLACE)
			Persistance.eventProcessor().processEvent(new ItemMovedEvent(Persistance.floor, action, user, container, index, user.tile(), 0));
		else if (action == ItemAction.SWITCH)
			Persistance.eventProcessor().processEvent(new ItemSwappedEvent(Persistance.floor, action, user, container, index, user.tile(), 0));
		else if (action == ItemAction.SWAP)
			nextState = new ItemContainersMenuState(this, dungeonState, true, Persistance.player.inventory()).setOpaque(this.isOpaque);
		else if (action == ItemAction.INFO)
			nextState = new InfoState(this.background, this, new Message[] { i.item().name() }, new Message[] { i.info() }).setOpaque(this.isOpaque);

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
		if (found) Persistance.stateManager
				.setState(new ItemContainersMenuState(this.parent, this.background, this.inDungeon, containers.toArray(new ItemContainer[containers.size()]))
						.setOpaque(this.isOpaque));
		else Persistance.stateManager.setState(this.parent);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);

		if (this.window == null)
		{
			this.window = new InventoryWindow(this, this.mainWindowDimensions());
			this.window.isOpaque = this.isOpaque;
		}
		if (this.nameWindow == null) this.updateNameWindow();
		if (this.allowMultipleSelection && this.multipleWindowInfo == null)
		{
			Rectangle r = this.window.dimensions;
			this.multipleWindowInfo = new TextWindow(new Rectangle(r.x, (int) (r.getMaxY() + 5), r.width, TextRenderer.height() + 30),
					new Message("inventory.multiple.info").addReplacement("<key-rotate>", KeyEvent.getKeyText(Key.ROTATE.keyValue())), false);
			this.multipleWindowInfo.isOpaque = this.isOpaque;
		}
		if (this.allowMultipleSelection && this.multipleSelection.size() == this.multipleMax && this.multipleWindowMax == null)
		{
			Rectangle r = this.multipleWindowInfo.dimensions;
			this.multipleWindowMax = new TextWindow(new Rectangle((int) (r.getMaxX() + 5), r.y, r.width, TextRenderer.height() + 30),
					new Message("inventory.multiple.max"), false);
			this.multipleWindowMax.isOpaque = this.isOpaque;
		}

		this.window.render(g, this.currentTab().name, width, height);
		this.nameWindow.render(g, null, width, height);
		if (this.allowMultipleSelection) this.multipleWindowInfo.render(g, null, width, height);
		if (this.allowMultipleSelection && this.multipleSelection.size() == this.multipleMax) this.multipleWindowMax.render(g, null, width, height);

		Message name = ((MenuItemOption) this.currentOption()).item.name();
		Rectangle inside = this.nameWindow.inside();
		TextRenderer.render(g, name, this.nameWindow.inside().x + 5, inside.y + inside.height / 2 - TextRenderer.height() / 2);
	}

	@Override
	public ItemStack selectedItem()
	{
		return ((MenuItemOption) this.tabs.get(this.tabIndex()).options()[this.itemIndex()]).item;
	}

	public void setMultipleSelectionListener(MultipleItemsSelectionListener listener)
	{
		this.allowMultipleSelection = true;
		this.multipleListener = listener;
	}

	public ItemContainersMenuState setOpaque(boolean isOpaque)
	{
		this.isOpaque = isOpaque;
		return this;
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
					if (pokemon.getItem() != null) Persistance.eventProcessor().processEvent(new ItemSwappedEvent(Persistance.floor, ItemAction.GIVE,
							Persistance.player.getDungeonLeader(), Persistance.player.inventory(), this.itemIndex(), pokemon, 0));
					else Persistance.eventProcessor().processEvent(new ItemMovedEvent(Persistance.floor, ItemAction.GIVE, Persistance.player.getDungeonLeader(),
							Persistance.player.inventory(), this.itemIndex(), pokemon, 0));
				} else
				{
					nextState = null;

					Persistance.isCommunicating = true;

					JsonObject payload = Json.object();
					payload.add("action", "itemaction");
					payload.add("value", "give");
					payload.add("item", i.getData().id);
					payload.add("pokemon", pokemon.getData().id);

					Persistance.socketendpoint.sendMessage(payload.toString());
				}
				break;

			case USE:
				Persistance.eventProcessor()
						.processEvent(new ItemSelectionEvent(Persistance.floor, i.item(), user, pokemon.getDungeonPokemon(), container, index));
				break;

			default:
				break;
		}

		if (nextState == this) this.reloadContainers();
		else if (nextState != null) Persistance.stateManager.setState(nextState);

	}

	private void updateNameWindow()
	{
		int maxWidth = 0;
		for (int i = 0; i < this.currentTab().options().length; ++i)
			maxWidth = Math.max(maxWidth, TextRenderer.width(((MenuItemOption) this.currentTab().options()[i]).item.name()));
		Rectangle main = this.mainWindowDimensions();
		this.nameWindow = new MenuWindow(
				new Rectangle((int) main.getMaxX() + 5, (int) main.getMinY(), maxWidth + MenuWindow.MARGIN_X + MenuHudSpriteset.cornerSize.width,
						TextRenderer.height() + MenuWindow.MARGIN_Y + MenuHudSpriteset.cornerSize.height * 3 / 2));
		this.nameWindow.isOpaque = this.isOpaque;
	}

}
