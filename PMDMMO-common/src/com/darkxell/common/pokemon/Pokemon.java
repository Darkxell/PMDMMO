package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.Random;

import org.jdom2.Element;

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

public class Pokemon implements ItemContainer
{
	/** Pokémon gender.
	 * <ul>
	 * <li>MALE = 0</li>
	 * <li>FEMALE = 1</li>
	 * <li>GENDERLESS = 2</li>
	 * </ul>
	 */
	public static final byte MALE = 0, FEMALE = 1, GENDERLESS = 2;
	public static final String XML_ROOT = "pokemon";

	/** This Pokémon's ability's ID. */
	public final int abilityID;
	/** A reference to the Dungeon entity of this Pokémon if in a Dungeon. null else. */
	DungeonPokemon dungeonPokemon;
	/** The current amount of experience of this Pokémon (for this level). */
	private int experience;
	/** This Pokémon's gender. See {@link Pokemon#MALE}. */
	public final byte gender;
	/** This Pokémon's ID. */
	public final int id;
	/** This Pokémon's IQ. */
	private int iq;
	/** True if this Pokémon is shiny. */
	public final boolean isShiny;
	/** This Pokémon's held Item's ID. -1 for no Item. */
	private ItemStack item;
	/** This Pokémon's level. */
	private int level;
	/** This Pokémon's moves. */
	private LearnedMove[] moves;
	/** This Pokémon's nickname. If null, use the species' name. */
	private String nickname;
	/** The Player controlling this Pokémon. null if it's an NPC. */
	public Player player;
	/** This Pokémon's species. */
	public final PokemonSpecies species;
	/** This Pokémon's stats. */
	private PokemonStats stats;

	public Pokemon(Element xml)
	{
		// todo: handle ID of null.
		Random r = new Random();
		this.id = XMLUtils.getAttribute(xml, "pk-id", 0);
		this.species = PokemonRegistry.find(Integer.parseInt(xml.getAttributeValue("id")));
		this.nickname = xml.getAttributeValue("nickname");
		this.item = xml.getChild(ItemStack.XML_ROOT) == null ? null : new ItemStack(xml.getChild(ItemStack.XML_ROOT));
		this.level = Integer.parseInt(xml.getAttributeValue("level"));
		this.stats = xml.getChild(PokemonStats.XML_ROOT) == null ? this.species.statsForLevel(this.level)
				: new PokemonStats(xml.getChild(PokemonStats.XML_ROOT));
		this.abilityID = XMLUtils.getAttribute(xml, "ability", this.species.randomAbility(r));
		this.experience = XMLUtils.getAttribute(xml, "xp", 0);
		this.gender = XMLUtils.getAttribute(xml, "gender", this.species.randomGender(r));
		this.iq = XMLUtils.getAttribute(xml, "iq", 0);
		this.isShiny = XMLUtils.getAttribute(xml, "shiny", false);
		this.moves = new LearnedMove[4];
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (Element move : xml.getChildren("move"))
		{
			int slot = Integer.parseInt(move.getAttributeValue("slot"));
			if (slot < 0 || slot >= this.moves.length) continue;
			this.moves[slot] = new LearnedMove(move);
			this.moves[slot].setSlot(slot);
			moves.add(this.moves[slot].id);
		}

		for (int i = 0; i < this.moves.length; ++i)
			if (this.moves[i] == null)
			{
				int id = this.species.latestMove(this.level, moves);
				if (id == -1) break;
				this.moves[i] = new LearnedMove(id);
				moves.add(this.moves[i].id);
				this.moves[i].setSlot(i);
			}

	}

	public Pokemon(int id, PokemonSpecies species, String nickname, ItemStack item, PokemonStats stats, int ability, int experience, int level,
			LearnedMove move1, LearnedMove move2, LearnedMove move3, LearnedMove move4, byte gender, int iq, boolean shiny)
	{
		this.id = id;
		this.species = species;
		this.nickname = nickname;
		this.item = item;
		this.stats = stats;
		this.abilityID = ability;
		this.experience = experience;
		this.level = level;
		this.moves = new LearnedMove[] { move1, move2, move3, move4 };
		this.gender = gender;
		this.iq = iq;
		this.isShiny = shiny;
	}

	@Override
	public void addItem(ItemStack item)
	{
		this.setItem(item);
	}

	@Override
	public int canAccept(ItemStack item)
	{
		return (this.getItem() == null || (item.item().isStackable && this.getItem().id == item.id)) ? 0 : -1;
	}

	@Override
	public Message containerName()
	{
		return new Message("inventory.held").addReplacement("<pokemon>", this.getNickname());
	}

	@Override
	public void deleteItem(int index)
	{
		this.setItem(null);
	}

	public Message evolutionStatus()
	{
		Evolution[] evolutions = this.species.evolutions();
		if (evolutions.length == 0) return new Message("evolve.none");
		for (Evolution evolution : evolutions)
		{
			if (evolution.method == Evolution.LEVEL && this.getLevel() >= evolution.value) return new Message("evolve.possible");
			if (evolution.method == Evolution.ITEM) return new Message("evolve.item");
		}
		return new Message("evolve.not_now");
	}

	/** @return The amount of experience to gain in order to level up. */
	public int experienceLeftNextLevel()
	{
		return this.species.experienceToNextLevel(this.level) - this.experience;
	}

	public int experienceToNextLevel()
	{
		return this.species.experienceToNextLevel(this.level);
	}

	/** @param amount - The amount of experience gained.
	 * @return The number of levels this experience granted. */
	public ArrayList<DungeonEvent> gainExperience(ExperienceGainedEvent event)
	{
		ArrayList<DungeonEvent> events = new ArrayList<>();

		int amount = event.experience, next = this.experienceLeftNextLevel();
		if (next <= amount)
		{
			amount -= next;
			this.experience = 0;
			events.add(new LevelupEvent(event.floor, this));
			events.add(new ExperienceGainedEvent(event.floor, this, amount, false));
		} else
		{
			this.experience += amount;
			amount = 0;
		}

		return events;
	}

	public Ability getAbility()
	{
		return Ability.find(this.abilityID);
	}

	public DungeonPokemon getDungeonPokemon()
	{
		return this.dungeonPokemon;
	}

	public int getExperience()
	{
		return this.experience;
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

	public int getLevel()
	{
		return this.level;
	}

	public Message getNickname()
	{
		return (this.nickname == null ? this.species.speciesName() : new Message(this.nickname, false)).addPrefix(this.player == null ? "<blue>" : "<yellow>")
				.addSuffix("</color>");
	}

	public PokemonStats getStats()
	{
		return this.stats;
	}

	public void increaseIQ(int iq)
	{
		this.iq += iq;
	}

	public int iq()
	{
		return this.iq;
	}

	public boolean isAlliedWith(Pokemon pokemon)
	{
		if (this.player == null && pokemon.player == null) return true;
		return this.player != null && this.player.isAlly(pokemon);
	}

	@Override
	public ArrayList<ItemAction> legalItemActions()
	{
		ArrayList<ItemAction> actions = new ArrayList<ItemAction>();
		actions.add(ItemAction.TAKE);
		return actions;
	}

	public ArrayList<DungeonEvent> levelUp()
	{
		ArrayList<DungeonEvent> events = new ArrayList<>();
		++this.level;
		this.experience = 0;
		PokemonStats stats = this.species.baseStatsIncrease(this.level - 1);
		this.stats.add(stats);
		if (this.dungeonPokemon != null) this.dungeonPokemon.stats.onStatChange();

		ArrayList<Move> moves = this.species.learnedMoves(this.level);
		for (Move move : moves)
			events.add(new MoveDiscoveredEvent(this, move));

		return events;
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

	@Override
	public void setItem(int index, ItemStack item)
	{
		this.setItem(item);
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
	}

	public void setMove(int slot, LearnedMove move)
	{
		if (slot >= 0 && slot < this.moves.length)
		{
			this.moves[slot] = move;
			this.moves[slot].setSlot(slot);
		}
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	@Override
	public int size()
	{
		return this.getItem() == null ? 0 : 1;
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

	public int totalExperience()
	{
		int xp = this.experience;
		for (int lvl = 1; lvl < this.level; ++lvl)
			xp += this.species.experienceToNextLevel(lvl);
		return xp;
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("pk-id", Integer.toString(this.id));
		root.setAttribute("id", Integer.toString(this.species.compoundID()));
		if (this.nickname != null) root.setAttribute("nickname", this.nickname);
		if (this.item != null) root.addContent(this.item.toXML());
		root.setAttribute("level", Integer.toString(this.level));
		root.addContent(this.stats.toXML());
		root.setAttribute("ability", Integer.toString(this.abilityID));
		XMLUtils.setAttribute(root, "xp", this.experience, 0);
		root.setAttribute("gender", Byte.toString(this.gender));
		XMLUtils.setAttribute(root, "iq", this.iq, 0);
		XMLUtils.setAttribute(root, "shiny", this.isShiny, false);
		this.moves = new LearnedMove[4];
		for (int i = 0; i < this.moves.length; ++i)
			if (this.moves[i] != null) root.addContent(this.moves[i].toXML());

		return root;
	}

}
