package com.darkxell.client.state.quiz;

import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

public class QuizQuestion
{

	public final QuizAnswer[] answers;
	public final int id;
	public final Message name;

	public QuizQuestion(Element xml, String nameID)
	{
		this.id = XMLUtils.getAttribute(xml, "id", 0);
		this.name = new Message(nameID);
		boolean yesno = XMLUtils.getAttribute(xml, "yesno", false);

		List<Element> answers = xml.getChildren("answer", xml.getNamespace());
		this.answers = new QuizAnswer[answers.size()];
		for (int i = 0; i < this.answers.length; ++i)
		{
			String name = nameID + "." + i;
			if (yesno)
			{
				if (i == 0) name = "quiz.yes";
				else name = "quiz.no";
			}
			this.answers[i] = new QuizAnswer(answers.get(i), name);
		}
	}

}
