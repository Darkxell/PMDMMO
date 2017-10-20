package com.darkxell.common.move;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.pokemon.ability.AbilityTypeBoost;
import com.darkxell.common.util.Directions;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.Weather;

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
		this.behaviorID = XMLUtils.getAttribute(xml, "behavior", -1);
		this.category = Byte.parseByte(xml.getAttributeValue("category"));
		this.pp = Integer.parseInt(xml.getAttributeValue("pp"));
		this.power = Integer.parseInt(xml.getAttributeValue("power"));
		this.accuracy = XMLUtils.getAttribute(xml, "accuracy", 100);
		this.targets = Byte.parseByte(xml.getAttributeValue("targets"));
		this.priority = XMLUtils.getAttribute(xml, "priority", 0);
		this.additionalEffectChance = XMLUtils.getAttribute(xml, "random", 100);
		this.makesContact = XMLUtils.getAttribute(xml, "contact", false);
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

	/** @return True if this Move's additional effects land. */
	public boolean additionalEffectLands(DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		return floor.random.nextInt(100) < this.additionalEffectChance;
	}

	public ArrayList<DungeonEvent> additionalEffects(DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		return new ArrayList<DungeonEvent>();
	}

	/** @param user - The Pokémon using the move.
	 * @param target - The Pokémon receiving the move.
	 * @param floor - The floor context.
	 * @param events - The list of Events created by this Move.
	 * @return The damage dealt by this move. */
	public int damageDealt(DungeonPokemon user, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events)
	{
		int atk = this.category == PHYSICAL ? user.stats.getAttack() : user.stats.getSpecialAttack() + this.power;
		int level = user.pokemon.getLevel();
		int def = this.category == PHYSICAL ? target.stats.getDefense() : target.stats.getSpecialDefense();
		float constant = ((atk - def) * 1f / 8) + (level * 2 / 3);

		// Damage modification
		float d = (((constant * 2) - def) + 10) + ((constant * constant) / 20);
		if (d < 1) d = 1;
		else if (d > 999) d = 999;

		// Abilities
		if (user.pokemon.getAbility() instanceof AbilityTypeBoost && user.getHp() <= Math.floor(user.getMaxHP() / 4)
				&& this.type == ((AbilityTypeBoost) user.pokemon.getAbility()).type)
		{
			events.add(new TriggeredAbilityEvent(floor, user));
			d *= 2;
		}

		// Weather
		{
			Weather w = floor.currentWeather().weather;
			if (w == Weather.SUNNY)
			{
				if (this.type == PokemonType.FIRE) d *= 1.5;
				else if (this.type == PokemonType.WATER) d *= 0.5;
			} else if (w == Weather.RAIN)
			{
				if (this.type == PokemonType.FIRE) d *= 0.5;
				else if (this.type == PokemonType.WATER) d *= 1.5;
			} else if (w == Weather.CLOUDS && this.type != PokemonType.NORMAL) d *= 0.75;
			else if (w == Weather.FOG && this.type == PokemonType.ELECTR) d *= 0.5;
		}

		// Critical hit ?
		boolean crit = false;
		{
			double c = 0.12;
			if (floor.random.nextDouble() < c) crit = true;
		}
		if (crit) d *= 1.5;

		// Damage multiplier
		{
			float multiplier = this.type == null ? 1 : this.type.effectivenessOn(target.pokemon.species);
			if (user.pokemon.species.type1 == this.type || user.pokemon.species.type2 == this.type) multiplier *= 1.5;

			d *= multiplier;
		}

		// Damage randomness
		d *= (57344 + Math.floor(floor.random.nextDouble() * 16384)) / 65536;

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
			case ALL_ALLIES:
				Room r = floor.roomAt(user.tile.x, user.tile.y);
				if (r == null) for (short d : Directions.directions())
				{
					Tile t = user.tile.adjacentTile(d);
					if (t.getPokemon() != null && user.pokemon.isAlliedWith(t.getPokemon().pokemon)) targets.add(t.getPokemon());
				}
				else for (Tile t : r.listTiles())
					if (t.getPokemon() != null && t.getPokemon() != user && user.pokemon.isAlliedWith(t.getPokemon().pokemon)) targets.add(t.getPokemon());
			case SELF:
				targets.add(user);
				break;

			case ALL_ENEMIES:
			case ALL_ROOM:
				r = floor.roomAt(user.tile.x, user.tile.y);
				if (r == null) for (short d : Directions.directions())
				{
					Tile t = user.tile.adjacentTile(d);
					if (t.getPokemon() != null && (this.targets == ALL_ROOM || !user.pokemon.isAlliedWith(t.getPokemon().pokemon))) targets.add(t.getPokemon());
				}
				else for (Tile t : r.listTiles())
					if (t.getPokemon() != null && t.getPokemon() != user)
					{
						if (this.targets == ALL_ROOM || !user.pokemon.isAlliedWith(t.getPokemon().pokemon)) targets.add(t.getPokemon());
					}
				break;

			default:
				DungeonPokemon front = user.tile.adjacentTile(user.facing()).getPokemon();
				if (front != null) targets.add(front);
		}

		return targets.toArray(new DungeonPokemon[targets.size()]);
	}

	/** @param usedMove - The Move used.
	 * @param target - The Pokémon receiving the Move.
	 * @param floor - The Floor context.
	 * @return True if this Move misses. */
	public boolean misses(MoveUse usedMove, DungeonPokemon target, Floor floor)
	{
		return false;
	}

	/** @return This Move's name. */
	public Message name()
	{
		return new Message("move." + this.id).addPrefix("<type-" + this.type.id + "> ");
	}

	/** @param move - The used move.
	 * @param floor - The Floor context.
	 * @return The Events created by this selection. Creates MoveUseEvents, distributing this Move on targets. */
	public final ArrayList<DungeonEvent> prepareUse(MoveUse move, Floor floor)
	{
		DungeonPokemon[] pokemon = this.getTargets(move.user, floor);
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		for (int i = 0; i < pokemon.length; ++i)
			events.add(new MoveUseEvent(floor, move, pokemon[i]));

		if (events.size() == 0 && this != MoveRegistry.ATTACK) events.add(new MessageEvent(floor, new Message("move.no_target")));
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
		XMLUtils.setAttribute(root, "accuracy", this.accuracy, 100);
		root.setAttribute("targets", Byte.toString(this.targets));
		XMLUtils.setAttribute(root, "priority", this.priority, 0);
		XMLUtils.setAttribute(root, "random", this.additionalEffectChance, 100);
		XMLUtils.setAttribute(root, "contact", this.makesContact, false);
		return root;
	}

	/** Applies this Move's effects to a Pokémon.
	 * 
	 * @param usedMove - The Move instance that was selected.
	 * @param target - The Pokémon the Move is being used on.
	 * @param floor - The Floor context.
	 * @return The events resulting from this Move. They typically include damage, healing, stat changes... */
	public ArrayList<DungeonEvent> useOn(MoveUse usedMove, DungeonPokemon target, Floor floor)
	{
		ArrayList<DungeonEvent> events = new ArrayList<DungeonEvent>();
		boolean missed = this.misses(usedMove, target, floor);

		float effectiveness = this.type == null ? PokemonType.NORMALLY_EFFECTIVE : this.type.effectivenessOn(target.pokemon.species);
		if (effectiveness == PokemonType.NO_EFFECT) events.add(new MessageEvent(floor, new Message("move.effectiveness.none").addReplacement("<pokemon>",
				target.pokemon.getNickname())));
		else
		{
			if (!missed && this != MoveRegistry.ATTACK) target.receiveMove(usedMove.move.isLinked() ? DungeonPokemon.LINKED_MOVES : DungeonPokemon.MOVES);
			if (this.power != -1)
			{
				if (effectiveness == PokemonType.SUPER_EFFECTIVE) events.add(new MessageEvent(floor, new Message("move.effectiveness.super").addReplacement(
						"<pokemon>", target.pokemon.getNickname())));
				else if (effectiveness == PokemonType.NOT_VERY_EFFECTIVE) events.add(new MessageEvent(floor, new Message("move.effectiveness.not_very")
						.addReplacement("<pokemon>", target.pokemon.getNickname())));
				events.add(new DamageDealtEvent(floor, target, usedMove, missed ? 0 : this.damageDealt(usedMove.user, target, floor, events)));
			}
			if (!missed && this.additionalEffectLands(usedMove.user, target, floor)) events.addAll(this.additionalEffects(usedMove.user, target, floor));
		}

		return events;
	}
}
