package com.darkxell.common.move;

import java.util.List;

import org.jdom2.Element;

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
			this.statChanges = new int[s.size()][2];
			for (int i = 0; i < s.size(); ++i)
			{
				this.statChanges[i][0] = Integer.parseInt(s.get(i).getAttributeValue("stat"));
				this.statChanges[i][1] = Integer.parseInt(s.get(i).getAttributeValue("stage"));
			}
		}
	}

	public MoveStatChange(int id, PokemonType type, int pp, int power, int accuracy, byte targets, int priority, int additionalEffectChance,
			boolean makesContact, int[][] statChanges)
	{
		super(id, type, -1, Move.STATUS, pp, power, accuracy, targets, priority, additionalEffectChance, makesContact);
		this.statChanges = statChanges;
	}

	@Override
	public Element toXML()
	{
		if (this.statChanges.length == 0) return super.toXML();
		Element root = super.toXML();
		Element stages = new Element("statchanges");
		for (int[] s : this.statChanges)
			stages.addContent(new Element("statchange").setAttribute("stat", Integer.toString(s[0])).setAttribute("stage", Integer.toString(s[1])));
		root.addContent(stages);
		return root;
	}

}
