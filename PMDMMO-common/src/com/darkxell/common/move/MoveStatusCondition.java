package com.darkxell.common.move;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditionInstance;
import com.darkxell.common.util.XMLUtils;

public class MoveStatusCondition extends Move
{

	/** The ID of the status condition this Move inflicts. */
	public final int condition;

	public MoveStatusCondition(Element xml)
	{
		super(xml);
		this.condition = XMLUtils.getAttribute(xml, "status", -1);
	}

	public MoveStatusCondition(int id, PokemonType type, int pp, int power, int accuracy, byte targets, int priority, int additionalEffectChance,
			boolean makesContact, int condition)
	{
		super(id, type, -1, Move.STATUS, pp, power, accuracy, targets, priority, additionalEffectChance, makesContact);
		this.condition = condition;
	}

	@Override
	public ArrayList<DungeonEvent> additionalEffects(DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		ArrayList<DungeonEvent> e = super.additionalEffects(user, target, floor);
		e.add(new StatusConditionCreatedEvent(floor, new StatusConditionInstance(StatusCondition.find(this.condition), target)));
		return e;
	}

	@Override
	public Element toXML()
	{
		Element root = super.toXML();
		XMLUtils.setAttribute(root, "status", this.condition, -1);
		return root;
	}

}
