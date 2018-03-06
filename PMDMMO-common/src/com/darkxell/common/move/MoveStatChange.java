package com.darkxell.common.move;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.XMLUtils;

public class MoveStatChange extends Move
{

	private boolean[] self;
	private int[] statChanges;
	private Stat[] stats;

	public MoveStatChange(Element xml)
	{
		super(xml);
		List<Element> s = xml.getChildren("statchange", xml.getNamespace());
		int count = s.size();
		this.self = new boolean[count];
		this.statChanges = new int[count];
		this.stats = new Stat[count];
		for (int i = 0; i < count; ++i)
		{
			this.self[i] = XMLUtils.getAttribute(s.get(i), "self", false);
			this.statChanges[i] = XMLUtils.getAttribute(s.get(i), "stage", 0);
			this.stats[i] = Stat.valueOf(s.get(i).getAttributeValue("stat"));
		}
	}

	public MoveStatChange(int id, PokemonType type, MoveCategory category, int pp, int power, int accuracy, MoveRange range, MoveTarget targets, int priority,
			int additionalEffectChance, boolean makesContact, int[] statChanges, Stat[] stats, boolean[] self)
	{
		super(id, type, -1, category, pp, power, accuracy, range, targets, priority, additionalEffectChance, makesContact);
		this.statChanges = statChanges;
		this.stats = stats;
		this.self = self;
	}

	@Override
	public void addAdditionalEffects(DungeonPokemon user, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events)
	{
		for (int i = 0; i < this.self.length; ++i)
			events.add(new StatChangedEvent(floor, this.self[i] ? user : target, this.stats[i], this.statChanges[i]));
	}

	@Override
	public Element toXML()
	{
		if (this.statChanges.length == 0) return super.toXML();
		Element root = super.toXML();
		for (int i = 0; i < this.self.length; ++i)
		{
			Element e = new Element("statchange").setAttribute("stat", this.stats[i].name()).setAttribute("stage", Integer.toString(this.statChanges[i]));
			if (this.self[i]) e.setAttribute("self", "true");
			root.addContent(e);
		}
		return root;
	}

}
