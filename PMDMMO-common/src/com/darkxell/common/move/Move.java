package com.darkxell.common.move;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DamageDealtEvent;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.Message;

public class Move
{
	/** Move targets.<br />
	 * <ul>
	 * <li>FRONT = 0</li>
	 * <li>ONE_AUTO = 1</li>
	 * <li>SELF = 2</li>
	 * <li>ALL_ENEMIES = 3</li>
	 * <li>ALL_ALLIES = 4</li>
	 * <li>ALL_ROOM = 5</li>
	 * </ul> */
	public static final byte FRONT = 0, ONE_AUTO = 1, SELF = 2, ALL_ENEMIES = 3, ALL_ALLIES = 4, ALL_ROOM = 5;
	/** Move categories.<br />
	 * <ul>
	 * <li>PHYSICAL = 0</li>
	 * <li>SPECIAL = 1</li>
	 * <li>STATUS = 2</li>
	 * </ul> */
	public static final byte PHYSICAL = 0, SPECIAL = 1, STATUS = 2;

	/** This move's accuracy. */
	public final int accuracy;
	/** If this move has an additional effect, its chance to happen. */
	public final int additionalEffectChance;
	/** This move's behavior type. -1 if already replaced with a proper class. */
	public final int behaviorID;
	/** This move's category. See {@link Move#PHYSICAL}. */
	public final byte category;
	/** This move's ID. */
	public final int id;
	/** True if this move makes contact. */
	public final boolean makesContact;
	/** This move's power. */
	public final int power;
	/** This move's default Power Points. */
	public final int pp;
	/** This move's priority. */
	public final int priority;
	/** This move's targets. See {@link Move#SINGLE}. */
	public final byte targets;
	/** This move's type. */
	public final PokemonType type;

	public Move(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.type = PokemonType.find(Integer.parseInt(xml.getAttributeValue("type")));
		this.behaviorID = xml.getAttribute("behavior") == null ? -1 : Integer.parseInt(xml.getAttributeValue("behavior"));
		this.category = Byte.parseByte(xml.getAttributeValue("category"));
		this.pp = Integer.parseInt(xml.getAttributeValue("pp"));
		this.power = Integer.parseInt(xml.getAttributeValue("power"));
		this.accuracy = xml.getAttribute("accuracy") == null ? 100 : Integer.parseInt(xml.getAttributeValue("accuracy"));
		this.targets = Byte.parseByte(xml.getAttributeValue("targets"));
		this.priority = xml.getAttribute("priority") == null ? 0 : Integer.parseInt(xml.getAttributeValue("priority"));
		this.additionalEffectChance = xml.getAttribute("random") == null ? 0 : Integer.parseInt(xml.getAttributeValue("random"));
		this.makesContact = xml.getAttribute("contact") != null;
	}

	public Move(int id, PokemonType type, int behaviorID, byte category, int pp, int power, int accuracy, byte targets, int priority,
			int additionalEffectChance, boolean makesContact)
	{
		this.id = id;
		this.type = type;
		this.behaviorID = behaviorID;
		this.category = category;
		this.pp = pp;
		this.power = power;
		this.accuracy = accuracy;
		this.targets = targets;
		this.priority = priority;
		this.additionalEffectChance = additionalEffectChance;
		this.makesContact = makesContact;
	}

	/** @param user - The Pokémon using the move.
	 * @param target - The Pokémon receiving the move.
	 * @param floor - The floor context.
	 * @return The damage dealt by this move. */
	public int damageDealt(DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		int atk = this.category == PHYSICAL ? user.stats.getAttack() : user.stats.getSpecialAttack() + this.power;
		int level = user.pokemon.getLevel();
		int def = this.category == PHYSICAL ? target.stats.getDefense() : target.stats.getSpecialDefense();
		float constant = ((atk - def) * 1f / 8) + (level * 2 / 3);

		// Damage modification
		float d = (((constant * 2) - def) + 10) + ((constant * constant) / 20);
		if (d < 1) d = 1;
		else if (d > 999) d = 999;

		// Critical hit ?
		boolean crit = false;
		{
			double c = 0.12;
			if (Math.random() < c) crit = true;
		}
		if (crit) d *= 1.5;

		// Damage multiplier
		{
			float multiplier = this.type == null ? 1 : this.type.effectivenessOn(target.pokemon.species);
			if (user.pokemon.species.type1 == this.type || user.pokemon.species.type2 == this.type) multiplier *= 1.5;

			d *= multiplier;
		}

		// Damage randomness
		d *= (57344 + Math.floor(Math.random() * 16384)) / 65536;

		return (int) d;
	}

	/** @param user - The Pokémon using this Move.
	 * @param floor - The Floor context.
	 * @return The Pokémon affected by this Move. */
	public DungeonPokemon[] getTargets(DungeonPokemon user, Floor floor)
	{
		ArrayList<DungeonPokemon> targets = new ArrayList<DungeonPokemon>();
		switch (this.targets)
		{
			default:
				DungeonPokemon front = user.tile.adjacentTile(user.facing()).getPokemon();
				if (front != null) targets.add(front);
		}

		return targets.toArray(new DungeonPokemon[targets.size()]);
	}

	/** @param user - The Pokémon using the Move.
	 * @param target - The Pokémon receiving the Move.
	 * @param floor - The Floor context.
	 * @return True if this Move misses. */
	public boolean misses(DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		return false;
	}

	/** @return This Move's name. */
	public Message name()
	{
		return new Message("move." + this.id).addPrefix("<type-" + this.type.id + "> ");
	}

	/** @param user - The Pokémon using the Move.
	 * @param move - The Learned move.
	 * @param floor - The Floor context.
	 * @return The Events created by this selection. Creates MoveUseEvents, distributing this Move on targets. */
	public final DungeonEvent[] prepareUse(DungeonPokemon user, LearnedMove move, Floor floor)
	{
		DungeonPokemon[] pokemon = this.getTargets(user, floor);
		MoveUseEvent[] events = new MoveUseEvent[pokemon.length];
		for (int i = 0; i < pokemon.length; ++i)
			events[i] = new MoveUseEvent(move, user, pokemon[i], floor);

		if (events.length == 0 && this != MoveRegistry.ATTACK) return new DungeonEvent[]
		{ new MessageEvent(new Message("move.no_target")) };
		return events;
	}

	public Element toXML()
	{
		String className = this.getClass().getName().substring(Move.class.getName().length());

		Element root = new Element("move");
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("type", Integer.toString(this.type.id));
		if (this.behaviorID > 0) root.setAttribute("behavior", Integer.toString(this.behaviorID));
		else root.setAttribute("movetype", className);
		root.setAttribute("category", Byte.toString(this.category));
		root.setAttribute("pp", Integer.toString(this.pp));
		root.setAttribute("power", Integer.toString(this.power));
		if (this.accuracy != 100) root.setAttribute("accuracy", Integer.toString(this.accuracy));
		root.setAttribute("targets", Byte.toString(this.targets));
		if (this.priority != 0) root.setAttribute("priority", Integer.toString(this.priority));
		if (this.additionalEffectChance != 0) root.setAttribute("random", Integer.toString(this.additionalEffectChance));
		if (this.makesContact) root.setAttribute("contact", Boolean.toString(this.makesContact));
		return root;
	}

	/** Applies this Move's effects to a Pokémon.
	 * 
	 * @param user - The Pokémon using the Move.
	 * @param target - The Pokémon the Move is being used on.
	 * @param floor - The Floor context.
	 * @return The events resulting from this Move. They typically include damage, healing, stat changes... */
	public DungeonEvent[] useOn(DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		boolean missed = this.misses(user, target, floor);
		return new DungeonEvent[]
		{ new DamageDealtEvent(target, missed ? 0 : this.damageDealt(user, target, floor)) };
	}
}
