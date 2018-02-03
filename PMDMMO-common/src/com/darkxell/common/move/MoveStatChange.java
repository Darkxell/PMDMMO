package com.darkxell.common.move;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class MoveStatChange extends Move
{

	/** The stats this Move changes, and how. ID of the stat and stages to add/remove. */
	private int[][] statChanges;

	public MoveStatChange(Element xml)
	{
		super(xml);
		if (xml.getChild("statchanges") == null) this.statChanges = new int[0][0];
		else
		{
			List<Element> s = xml.getChild("statchanges").getChildren("statchange");
			this.statChanges = new int[s.size()][3];
			for (int i = 0; i < s.size(); ++i)
			{
				this.statChanges[i][0] = Integer.parseInt(s.get(i).getAttributeValue("stat"));
				this.statChanges[i][1] = Integer.parseInt(s.get(i).getAttributeValue("stage"));
				this.statChanges[i][2] = s.get(i).getAttribute("self") == null ? 0 : 1; // Force self if 1
			}
		}
	}

	public MoveStatChange(int id, PokemonType type, int pp, int power, int accuracy, MoveRange range, MoveTarget targets, int priority,
			int additionalEffectChance, boolean makesContact, int[][] statChanges)
	{
		super(id, type, -1, Move.STATUS, pp, power, accuracy, range, targets, priority, additionalEffectChance, makesContact);
		this.statChanges = statChanges;
	}

	@Override
	public ArrayList<DungeonEvent> additionalEffects(DungeonPokemon user, DungeonPokemon target, Floor floor)
	{
		ArrayList<DungeonEvent> e = super.additionalEffects(user, target, floor);
		for (int[] statChange : this.statChanges)
			e.add(new StatChangedEvent(floor, statChange[2] == 1 ? user : target, statChange[0], statChange[1]));
		return e;
	}

	@Override
	public Element toXML()
	{
		if (this.statChanges.length == 0) return super.toXML();
		Element root = super.toXML();
		Element stages = new Element("statchanges");
		for (int[] s : this.statChanges)
		{
			Element e = new Element("statchange").setAttribute("stat", Integer.toString(s[0])).setAttribute("stage", Integer.toString(s[1]));
			if (s[2] == 1) e.setAttribute("self", "1");
			stages.addContent(e);
		}
		root.addContent(stages);
		return root;
	}

}
