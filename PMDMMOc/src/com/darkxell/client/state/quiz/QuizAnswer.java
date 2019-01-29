package com.darkxell.client.state.quiz;

import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.util.xml.XMLUtils;
import com.darkxell.common.util.language.Message;

public class QuizAnswer
{

	public final int id;
	public final Message name;
	/** The nature points this Answer gives. */
	public final Nature[] natures;
	public final int[] points;

	public QuizAnswer(Element xml, String nameID)
	{
		this.id = XMLUtils.getAttribute(xml, "id", 0);
		this.name = new Message(nameID);

		List<Element> rewards = xml.getChildren("reward", xml.getNamespace());
		this.natures = new Nature[rewards.size()];
		this.points = new int[rewards.size()];
		for (int i = 0; i < rewards.size(); ++i)
		{
			this.natures[i] = Nature.valueOf(XMLUtils.getAttribute(rewards.get(i), "nature", Nature.Brave.name()));
			this.points[i] = XMLUtils.getAttribute(rewards.get(i), "value", 1);
		}
	}
}
