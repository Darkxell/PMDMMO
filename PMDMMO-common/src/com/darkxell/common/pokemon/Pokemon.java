package com.darkxell.common.pokemon;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.dungeon.TempIDRegistry.HasID;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveDiscoveredEvent;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.event.stats.LevelupEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.move.Move;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class Pokemon implements ItemContainer, HasID
{
	/** Pokémon gender.
	 * <ul>
	 * <li>MALE = 0</li>
	 * <li>FEMALE = 1</li>
	 * <li>GENDERLESS = 2</li>
	 * </ul>
	 */
	public static final int MALE = 0, FEMALE = 1, GENDERLESS = 2;
	public static final String XML_ROOT = "pokemon";

	private static ArrayList<DatabaseIdentifier> createMovesList(LearnedMove... moves)
	{
		ArrayList<DatabaseIdentifier> ids = new ArrayList<>();
		for (LearnedMove move : moves)
			if (move != null) ids.add(new DatabaseIdentifier(move.getData().id));
		return ids;
	}

	/** This Pokémon's ability's ID. */
	private Ability ability;

	private DBPokemon data;

	/** A reference to the Dungeon entity of this Pokémon if in a Dungeon. null else. */
	DungeonPokemon dungeonPokemon;

	/** This Pokémon's held Item's ID. -1 for no Item. */
	private ItemStack item;

	/** This Pokémon's moves. */
	private LearnedMove[] moves;

	/** The Player controlling this Pokémon. null if it's an NPC. */
	private Player player;

	/** This Pokémon's species. */
	private PokemonSpecies species;

	/** This Pokémon's stats. */
	private BaseStats stats;

	public Pokemon(DBPokemon data)
	{
		this.setData(data);
	}

	public Pokemon(long id, PokemonSpecies species, String nickname, ItemStack item, BaseStats stats, int abilityid, long experience, int level,
			LearnedMove move1, LearnedMove move2, LearnedMove move3, LearnedMove move4, int gender, int iq, boolean shiny)
	{
		this(new DBPokemon(0, species.id, species.formID, abilityid, gender, nickname, level, experience, iq, shiny, stats.attack, stats.defense,
				stats.specialAttack, stats.specialDefense, stats.health, item == null ? null : new DatabaseIdentifier(item.getData().id),
				createMovesList(move1, move2, move3, move4)));
		this.item = item;
		this.moves = new LearnedMove[] { move1, move2, move3, move4 };
	}

	public Pokemon(Pokemon pokemon)
	{
		this(pokemon.getData().id, pokemon.species(), pokemon.nickname(), pokemon.item(), pokemon.stats(), pokemon.abilityID(), pokemon.experience(),
				pokemon.level(), pokemon.move(0), pokemon.move(1), pokemon.move(2), pokemon.move(3), pokemon.gender(), pokemon.iq(), pokemon.isShiny());
	}

	public Ability ability()
	{
		return this.ability;
	}

	public int abilityID()
	{
		return this.data.abilityid;
	}

	@Override
	public void addItem(ItemStack item)
	{
		this.setItem(item);
	}

	@Override
	public int canAccept(ItemStack item)
	{
		return (this.getItem() == null || (item.item().isStackable && this.getItem().item().id == item.item().id)) ? 0 : -1;
	}

	@Override
	public long containerID()
	{
		return this.id();
	}

	@Override
	public Message containerName()
	{
		return new Message("inventory.held").addReplacement("<pokemon>", this.getNickname());
	}

	@Override
	public ItemContainerType containerType()
	{
		return ItemContainerType.POKEMON;
	}

	public void createDungeonPokemon()
	{
		new DungeonPokemon(this);
	}

	@Override
	public void deleteItem(int index)
	{
		this.setItem(null);
	}

	public Message evolutionStatus()
	{
		Evolution[] evolutions = this.species().evolutions();
		if (evolutions.length == 0) return new Message("evolve.none");
		for (Evolution evolution : evolutions)
		{
			if (evolution.method == Evolution.LEVEL && this.level() >= evolution.value) return new Message("evolve.possible");
			if (evolution.method == Evolution.ITEM) return new Message("evolve.item");
		}
		return new Message("evolve.not_now");
	}

	public long experience()
	{
		return this.data.experience;
	}

	/** @return The amount of experience to gain in order to level up. */
	public long experienceLeftNextLevel()
	{
		return this.species().experienceToNextLevel(this.level()) - this.experience();
	}

	public long experienceToNextLevel()
	{
		return this.species().experienceToNextLevel(this.level());
	}

	/** @param amount - The amount of experience gained.
	 * @return The number of levels this experience granted. */
	public ArrayList<DungeonEvent> gainExperience(ExperienceGainedEvent event)
	{
		ArrayList<DungeonEvent> events = new ArrayList<>();

		int amount = event.experience;
		int levelsup = 0;
		long next = this.experienceLeftNextLevel();
		while (amount != 0)
		{
			if (next <= amount)
			{
				amount -= next;
				this.setExperience(0);
				events.add(new LevelupEvent(event.floor, this));
			} else
			{
				this.setExperience(this.experience() + amount);
				amount = 0;
			}
			++levelsup;
			next = this.species().experienceToNextLevel(this.level() + levelsup);
		}

		return events;
	}

	public int gender()
	{
		return this.data.gender;
	}

	public BaseStats getBaseStats()
	{
		return this.stats;
	}

	public DBPokemon getData()
	{
		return this.data;
	}

	public DungeonPokemon getDungeonPokemon()
	{
		return this.dungeonPokemon;
	}

	public int getIQLevel()
	{
		final int[] levels = { 10, 50, 100, 150, 200, 300, 400, 500, 600, 700, 990, Integer.MAX_VALUE };
		for (int i = 0; i < levels.length; ++i)
			if (levels[i] > this.iq()) return i + 1;
		return 1;
	}

	public ItemStack getItem()
	{
		return this.item;
	}

	@Override
	public ItemStack getItem(int index)
	{
		return this.getItem();
	}

	public Message getNickname()
	{
		return (this.nickname() == null ? this.species().speciesName() : new Message(this.nickname(), false))
				.addPrefix(this.player == null ? "<blue>" : "<yellow>").addSuffix("</color>");
	}

	public String getRawNickname()
	{
		return this.nickname();
	}

	@Override
	public long id()
	{
		return this.data.id;
	}

	public void increaseIQ(int iq)
	{
		this.setIq(this.iq() + iq);
	}

	public int iq()
	{
		return this.data.iq;
	}

	public boolean isAlliedWith(Pokemon pokemon)
	{
		return this.player() != null && this.player().isAlly(pokemon);
	}

	public boolean isShiny()
	{
		return this.data.isshiny;
	}

	public ItemStack item()
	{
		return this.item;
	}

	@Override
	public ArrayList<ItemAction> legalItemActions(boolean inDungeon)
	{
		ArrayList<ItemAction> actions = new ArrayList<ItemAction>();
		actions.add(ItemAction.TAKE);
		return actions;
	}

	public int level()
	{
		return this.data.level;
	}

	public void levelUp(Floor floor, ArrayList<DungeonEvent> events)
	{
		this.setLevel(this.level() + 1);
		BaseStats stats = this.species().baseStatsIncrease(this.level() - 1);
		this.stats.add(stats);
		if (this.dungeonPokemon != null) this.dungeonPokemon.stats.onStatChange();

		ArrayList<Move> moves = this.species().learnedMoves(this.level());
		for (Move move : moves)
			events.add(new MoveDiscoveredEvent(floor, this, move));
	}

	public LearnedMove move(int slot)
	{
		if (slot < 0 || slot >= this.moves.length) return null;
		return this.moves[slot];
	}

	public int moveCount()
	{
		if (this.moves[3] != null) return 4;
		if (this.moves[2] != null) return 3;
		if (this.moves[1] != null) return 2;
		return 1;
	}

	private String nickname()
	{
		return this.data.nickname;
	}

	public Player player()
	{
		return this.player;
	}

	public void setData(DBPokemon data)
	{
		this.data = data;
		this.ability = Ability.find(this.abilityID());
		this.item = null;
		this.moves = new LearnedMove[4];
		this.species = PokemonRegistry.find(this.data.specieid);
		this.stats = new BaseStats(this, this.data.stat_atk, this.data.stat_def, this.data.stat_hp, this.data.stat_speatk, this.data.stat_spedef, 1);
	}

	private void setExperience(long experience)
	{
		this.data.experience = experience;
	}

	@Override
	public void setId(long id)
	{
		this.data.id = id;
		if (this.player != null)
		{
			if (this.player.getTeamLeader() == this) this.player.getData().mainpokemon = new DatabaseIdentifier(id);
			else this.player.getData().pokemonsinparty.set(this.player.allies.indexOf(this), new DatabaseIdentifier(id));
		}
	}

	private void setIq(int iq)
	{
		this.data.iq = iq;
	}

	@Override
	public void setItem(int index, ItemStack item)
	{
		this.setItem(item);
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
		if (item == null) this.data.holdeditem = null;
		else this.data.holdeditem = new DatabaseIdentifier(item.getData().id);
	}

	private void setLevel(int level)
	{
		this.data.level = level;
	}

	public void setMove(int slot, LearnedMove move)
	{
		if (move != null && slot >= 0 && slot < this.moves.length)
		{
			while (slot > this.moveCount())
				--slot;
			this.moves[slot] = move;
			this.moves[slot].setSlot(slot);
		}
	}

	public void setNickname(String nickname)
	{
		this.data.nickname = nickname;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	@Override
	public int size()
	{
		return this.getItem() == null ? 0 : 1;
	}

	public PokemonSpecies species()
	{
		return this.species;
	}

	public BaseStats stats()
	{
		return this.stats;
	}

	public void switchMoves(int slot1, int slot2)
	{
		if (slot1 < 0 || slot1 >= this.moves.length || slot2 < 0 || slot2 >= this.moves.length) return;
		LearnedMove temp = this.move(slot1);
		this.setMove(slot1, this.move(slot2));
		this.setMove(slot2, temp);
	}

	@Override
	public String toString()
	{
		return this.getNickname().toString();
	}

	public long totalExperience()
	{
		long xp = this.experience();
		for (int lvl = 1; lvl < this.level(); ++lvl)
			xp += this.species().experienceToNextLevel(lvl);
		return xp;
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("pk-id", Long.toString(this.id()));
		root.setAttribute("id", Integer.toString(this.species().compoundID()));
		if (this.nickname() != null) root.setAttribute("nickname", this.nickname());
		if (this.item != null) root.addContent(this.item.toXML());
		root.setAttribute("level", Integer.toString(this.level()));
		root.addContent(this.stats.toXML());
		root.setAttribute("ability", Integer.toString(this.abilityID()));
		XMLUtils.setAttribute(root, "xp", this.experience(), 0);
		root.setAttribute("gender", Integer.toString(this.gender()));
		XMLUtils.setAttribute(root, "iq", this.iq(), 0);
		XMLUtils.setAttribute(root, "shiny", this.isShiny(), false);
		this.moves = new LearnedMove[4];
		for (int i = 0; i < this.moves.length; ++i)
			if (this.moves[i] != null) root.addContent(this.moves[i].toXML());

		return root;
	}

}
