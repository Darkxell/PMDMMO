package com.darkxell.common.move;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.pokemon.ability.AbilityTypeBoost;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.Weather;

public class Move
{
	public static enum MoveCategory
	{
		Physical,
		Special,
		Status
	}

	/** Move range.<br />
	 * <ul>
	 * <li>FRONT = 0 ; The Pokémon on the Tile in front of the user.</li>
	 * <li>FRONT_ROW = 1 ; The Pokémon on the Tiles in front and diagonals of the user.</li>
	 * <li>AROUND = 2 ; All Pokémon within a 1-Tile range of the user.</li>
	 * <li>ROOM = 3 ; All Pokémon in a room (or visible, if not in a room.)</li>
	 * <li>TWO_TILES = 4 ; The Pokémon on the Tile in front of the user, or if no Pokémon, the one on the second Tile in front.</li>
	 * <li>LINE = 5 ; The first Pokémon in the user's direction (up to ten tiles), cuts corners.</li>
	 * <li>FLOOR = 6 ; All Pokémon on the Floor.</li>
	 * <li>USER = 7 ; Only the user.</li>
	 * <li>FRONT_CORNERS = 8 ; The Pokémon on the Tile in front of the user, cuts corners.</li>
	 * <li>AMBIENT = 9 ; Does not target any Pokémon.</li>
	 * </ul>
	 */
	public static enum MoveRange
	{
		/** Does not target any Pokémon. */
		Ambient,
		/** */
		Around,
		/** All Pokémon within a 1-Tile range of the user. */
		Floor,
		/** The Pokémon on the Tile in front of the user. */
		Front,
		/** The Pokémon on the Tile in front of the user, cuts corners. */
		Front_corners,
		/** The Pokémon on the Tiles in front and diagonals of the user. */
		Front_row,
		/** The first Pokémon in the user's direction (up to ten tiles), cuts corners. */
		Line,
		/** All Pokémon in a room (or visible, if not in a room.) */
		Room,
		/** Only the user. */
		Self,
		/** The Pokémon on the Tile in front of the user, or if no Pokémon, the one on the second Tile in front. */
		Two_tiles;
	}

	public static enum MoveTarget
	{
		/** Any Pokémon. */
		All,
		/** The user's allies. */
		Allies,
		/** Only foes. */
		Foes,
		/** Any Pokémon except the user. */
		Others,
		/** The user and its allies. */
		Team,
		/** Only the user. */
		User;
	}

	/** This move's accuracy. */
	public final int accuracy;
	/** If this move has an additional effect, its chance to happen. */
	public final int additionalEffectChance;
	/** This move's behavior type. -1 if already replaced with a proper class. */
	public final int behaviorID;
	/** This move's category. See {@link Move#PHYSICAL}. */
	public final MoveCategory category;
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
	/** This move's range. */
	public final MoveRange range;
	/** This move's targets. */
	public final MoveTarget targets;
	/** This move's type. */
	public final PokemonType type;

	public Move(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.type = PokemonType.valueOf(xml.getAttributeValue("type"));
		this.behaviorID = XMLUtils.getAttribute(xml, "behavior", -1);
		this.category = MoveCategory.valueOf(xml.getAttributeValue("category"));
		this.pp = Integer.parseInt(xml.getAttributeValue("pp"));
		this.power = Integer.parseInt(xml.getAttributeValue("power"));
		this.accuracy = XMLUtils.getAttribute(xml, "accuracy", 100);
		this.range = MoveRange.valueOf(XMLUtils.getAttribute(xml, "range", MoveRange.Front.name()));
		this.targets = MoveTarget.valueOf(XMLUtils.getAttribute(xml, "targets", MoveTarget.Foes.name()));
		this.priority = XMLUtils.getAttribute(xml, "priority", 0);
		this.additionalEffectChance = XMLUtils.getAttribute(xml, "random", 100);
		this.makesContact = XMLUtils.getAttribute(xml, "contact", false);
	}

	public Move(int id, PokemonType type, int behaviorID, MoveCategory category, int pp, int power, int accuracy, MoveRange range, MoveTarget targets,
			int priority, int additionalEffectChance, boolean makesContact)
	{
		this.id = id;
		this.type = type;
		this.behaviorID = behaviorID;
		this.category = category;
		this.pp = pp;
		this.power = power;
		this.accuracy = accuracy;
		this.range = range;
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
		int atk = this.category == MoveCategory.Physical ? user.stats.getAttack() : user.stats.getSpecialAttack() + this.power;
		int level = user.pokemon.getLevel();
		int def = this.category == MoveCategory.Physical ? target.stats.getDefense() : target.stats.getSpecialDefense();
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
				if (this.type == PokemonType.Fire) d *= 1.5;
				else if (this.type == PokemonType.Water) d *= 0.5;
			} else if (w == Weather.RAIN)
			{
				if (this.type == PokemonType.Fire) d *= 0.5;
				else if (this.type == PokemonType.Water) d *= 1.5;
			} else if (w == Weather.CLOUDS && this.type != PokemonType.Normal) d *= 0.75;
			else if (w == Weather.FOG && this.type == PokemonType.Electric) d *= 0.5;
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

	/** @return This Move's description. */
	public Message description()
	{
		return new Message("move.info." + this.id);
	}

	/** Removes all Pokémon this move is not supposed to target. */
	private void filterTargets(Floor floor, DungeonPokemon user, ArrayList<DungeonPokemon> targets)
	{
		switch (this.targets)
		{
			case All:
				break;

			case Allies:
				targets.removeIf((DungeonPokemon p) -> p == user || !p.pokemon.isAlliedWith(user.pokemon));
				break;

			case Foes:
				targets.removeIf((DungeonPokemon p) -> p == user || p.pokemon.isAlliedWith(user.pokemon));
				break;

			case Others:
				targets.remove(user);
				break;

			case Team:
				targets.removeIf((DungeonPokemon p) -> !p.pokemon.isAlliedWith(user.pokemon));
				break;

			case User:
				targets.removeIf((DungeonPokemon p) -> p != user);
				break;
		}
	}

	/** @param user - The Pokémon using this Move.
	 * @param floor - The Floor context.
	 * @return The Pokémon affected by this Move. */
	public DungeonPokemon[] getTargets(DungeonPokemon user, Floor floor)
	{
		ArrayList<DungeonPokemon> targets = new ArrayList<DungeonPokemon>();
		Tile t = user.tile(), front = t.adjacentTile(user.facing());

		switch (this.range)
		{
			case Ambient:
				break;

			case Around:
				for (Direction d : Direction.directions)
					if (t.adjacentTile(d).getPokemon() != null) targets.add(t.adjacentTile(d).getPokemon());
				break;

			case Floor:
				targets.addAll(floor.listPokemon());
				break;

			case Front_row:
				for (Direction d : new Direction[] { user.facing().rotateCounterClockwise(), user.facing(), user.facing().rotateClockwise() })
					if (t.adjacentTile(d).getPokemon() != null) targets.add(t.adjacentTile(d).getPokemon());
				break;

			case Line:
				int distance = 0;
				boolean done;
				Tile current = t;
				do
				{
					current = current.adjacentTile(user.facing());
					if (current.getPokemon() != null) targets.add(current.getPokemon());
					++distance;
					done = !targets.isEmpty() || distance > 10 || current.type() == TileType.WALL || current.type() == TileType.WALL_END;
				} while (!done);
				break;

			case Room:
				Room r = floor.roomAt(user.tile().x, user.tile().y);
				if (r == null)
				{
					for (Tile tile : AIUtils.visibleTiles(floor, user))
						if (tile.getPokemon() != null) targets.add(tile.getPokemon());
				} else for (Tile t2 : r.listTiles())
					if (t2.getPokemon() != null) targets.add(t2.getPokemon());
				break;

			case Self:
				targets.add(user);
				break;

			case Two_tiles:
				if (front.getPokemon() != null) targets.add(front.getPokemon());
				else if (front.type().canWalkOn(user))
				{
					Tile behind = front.adjacentTile(user.facing());
					if (behind.getPokemon() != null) targets.add(behind.getPokemon());
				}
				break;

			case Front:
			case Front_corners:
			default:
				DungeonPokemon f = user.tile().adjacentTile(user.facing()).getPokemon();
				if (f != null)
				{
					boolean valid = true;
					if (user.facing().isDiagonal() && this.range != MoveRange.Front_corners)
					{
						Tile t1 = user.tile().adjacentTile(user.facing().rotateClockwise());
						if (t1.type() == TileType.WALL || t1.type() == TileType.WALL_END) valid = false;
						t1 = user.tile().adjacentTile(user.facing().rotateCounterClockwise());
						if (t1.type() == TileType.WALL || t1.type() == TileType.WALL_END) valid = false;
					}
					if (valid) targets.add(f);
				}
		}

		this.filterTargets(floor, user, targets);
		if (this.range == MoveRange.Room || this.range == MoveRange.Floor)
			targets.sort((DungeonPokemon p1, DungeonPokemon p2) -> floor.dungeon.compare(p1, p2));

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
		if (this.range == MoveRange.Ambient) events.add(new MoveUseEvent(floor, move, null));

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
		root.setAttribute("category", this.category.name());
		root.setAttribute("pp", Integer.toString(this.pp));
		root.setAttribute("power", Integer.toString(this.power));
		XMLUtils.setAttribute(root, "accuracy", this.accuracy, 100);
		XMLUtils.setAttribute(root, "range", this.range.name(), MoveRange.Front.name());
		XMLUtils.setAttribute(root, "targets", this.targets.name(), MoveTarget.Foes.name());
		XMLUtils.setAttribute(root, "priority", this.priority, 0);
		XMLUtils.setAttribute(root, "random", this.additionalEffectChance, 100);
		XMLUtils.setAttribute(root, "contact", this.makesContact, false);
		return root;
	}

	protected Message unaffectedMessage(DungeonPokemon target)
	{
		return new Message("move.effectiveness.none").addReplacement("<pokemon>", target.pokemon.getNickname());
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
		if (effectiveness == PokemonType.NO_EFFECT) events.add(new MessageEvent(floor, this.unaffectedMessage(target)));
		else
		{
			if (!missed && this != MoveRegistry.ATTACK) target.receiveMove(usedMove.move.isLinked() ? DungeonPokemon.LINKED_MOVES : DungeonPokemon.MOVES);
			if (this.power != -1)
			{
				if (effectiveness == PokemonType.SUPER_EFFECTIVE)
					events.add(new MessageEvent(floor, new Message("move.effectiveness.super").addReplacement("<pokemon>", target.pokemon.getNickname())));
				else if (effectiveness == PokemonType.NOT_VERY_EFFECTIVE)
					events.add(new MessageEvent(floor, new Message("move.effectiveness.not_very").addReplacement("<pokemon>", target.pokemon.getNickname())));
				events.add(new DamageDealtEvent(floor, target, usedMove, missed ? 0 : this.damageDealt(usedMove.user, target, floor, events)));
			}
			if (!missed && this.additionalEffectLands(usedMove.user, target, floor)) events.addAll(this.additionalEffects(usedMove.user, target, floor));
		}

		return events;
	}
}
