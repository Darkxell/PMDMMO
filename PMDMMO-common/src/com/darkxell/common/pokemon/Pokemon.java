package com.darkxell.common.pokemon;

import java.util.ArrayList;
import java.util.Random;

import org.jdom2.Element;

import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.util.Message;

public class Pokemon implements ItemContainer
{
	/** Pokémon gender.
	 * <ul>
	 * <li>MALE = 0</li>
	 * <li>FEMALE = 1</li>
	 * <li>GENDERLESS = 2</li>
	 * </ul> */
	public static final byte MALE = 0, FEMALE = 1, GENDERLESS = 2;
	public static final String XML_ROOT = "pokemon";

	/** This Pokémon's ability's ID. */
	public final int abilityID;
	/** The current amount of experience of this Pokémon (for this level). */
	private int experience;
	/** This Pokémon's gender. See {@link Pokemon#MALE}. */
	public final byte gender;
	/** This Pokémon's ID. */
	public final int id;
	/** This Pokémon's held Item's ID. -1 for no Item. */
	private ItemStack item;
	/** This Pokémon's level. */
	private int level;
	/** This Pokémon's moves. */
	private LearnedMove[] moves;
	/** This Pokémon's nickname. If null, use the species' name. */
	private String nickname;
	/** This Pokémon's species. */
	public final PokemonSpecies species;
	/** This Pokémon's stats. */
	private PokemonStats stats;

	public Pokemon(Element xml)
	{
		// todo: handle ID of null.
		Random r = new Random();
		this.id = xml.getAttribute("pk-id") == null ? 0 : Integer.parseInt(xml.getAttributeValue("pk-id"));
		this.species = PokemonRegistry.find(Integer.parseInt(xml.getAttributeValue("id")));
		this.nickname = xml.getAttributeValue("nickname");
		this.item = xml.getChild(ItemStack.XML_ROOT) == null ? null : new ItemStack(xml.getChild(ItemStack.XML_ROOT));
		this.level = Integer.parseInt(xml.getAttributeValue("level"));
		this.stats = xml.getChild(PokemonStats.XML_ROOT) == null ? this.species.statsForLevel(this.level) : new PokemonStats(
				xml.getChild(PokemonStats.XML_ROOT));
		this.abilityID = xml.getAttribute("ability") == null ? this.species.randomAbility(r) : Integer.parseInt(xml.getAttributeValue("ability"));
		this.experience = xml.getAttribute("xp") == null ? 0 : Integer.parseInt(xml.getAttributeValue("xp"));
		this.gender = xml.getAttribute("gender") == null ? this.species.randomGender(r) : Byte.parseByte(xml.getAttributeValue("gender"));
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
			LearnedMove move1, LearnedMove move2, LearnedMove move3, LearnedMove move4, byte gender)
	{
		this.id = id;
		this.species = species;
		this.nickname = nickname;
		this.item = item;
		this.stats = stats;
		this.abilityID = ability;
		this.experience = experience;
		this.level = level;
		this.moves = new LearnedMove[]
		{ move1, move2, move3, move4 };
		this.gender = gender;
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
	public int experienceToNextLevel()
	{
		return this.species.experienceToNextLevel(this.level) - this.experience;
	}

	/** @param amount - The amount of experience gained.
	 * @return The number of levels this experience granted. */
	public int gainExperience(int amount)
	{
		int levelups = 0;

		while (amount != 0)
		{
			int next = this.experienceToNextLevel();
			if (next <= amount)
			{
				amount -= next;
				this.experience = 0;
				++levelups;
				this.levelUp();
			} else
			{
				this.experience += amount;
				amount = 0;
			}
		}

		return levelups;
	}

	public Ability getAbility()
	{
		return Ability.find(this.abilityID);
	}

	public int getExperience()
	{
		return this.experience;
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
		if (this.nickname == null) return this.species.name();
		return new Message(this.nickname, false);
	}

	public PokemonStats getStats()
	{
		return this.stats;
	}

	@Override
	public ArrayList<ItemAction> legalItemActions()
	{
		ArrayList<ItemAction> actions = new ArrayList<ItemAction>();
		actions.add(ItemAction.TAKE);
		return actions;
	}

	private void levelUp()
	{
		++this.level;
		this.stats.add(this.species.baseStatsIncrease(this.level - 1));
	}

	public LearnedMove move(int slot)
	{
		if (slot < 0 || slot >= this.moves.length) return null;
		return this.moves[slot];
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
		root.setAttribute("id", Integer.toString(this.species.id));
		if (this.nickname != null) root.setAttribute("nickname", this.nickname);
		if (this.item != null) root.addContent(this.item.toXML());
		root.setAttribute("level", Integer.toString(this.level));
		root.addContent(this.stats.toXML());
		root.setAttribute("ability", Integer.toString(this.abilityID));
		if (this.experience != 0) root.setAttribute("xp", Integer.toString(this.experience));
		root.setAttribute("gender", Byte.toString(this.gender));
		this.moves = new LearnedMove[4];
		for (int i = 0; i < this.moves.length; ++i)
			if (this.moves[i] != null) root.addContent(this.moves[i].toXML());

		return root;
	}

}
