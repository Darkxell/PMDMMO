package com.darkxell.common.move;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditionInstance;
import com.darkxell.common.util.RandomUtil;
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

	public MoveStatusCondition(int id, PokemonType type, int pp, MoveCategory category, int power, int accuracy, MoveRange range, MoveTarget targets,
			int priority, int additionalEffectChance, boolean makesContact, int condition)
	{
		super(id, type, -1, category, pp, power, accuracy, range, targets, priority, additionalEffectChance, makesContact);
		this.condition = condition;
	}

	@Override
	public ArrayList<DungeonEvent> additionalEffects(DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		ArrayList<DungeonEvent> e = super.additionalEffects(user, target, floor);
		if (this.condition != -1)
		{
			StatusCondition c = StatusCondition.find(this.condition);
			if (c.affects(target)) e.add(new StatusConditionCreatedEvent(floor,
					new StatusConditionInstance(c, target, RandomUtil.nextIntInBounds(c.durationMin, c.durationMax + 1, floor.random))));
			else e.add(new MessageEvent(floor, this.unaffectedMessage(target)));
		}
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
