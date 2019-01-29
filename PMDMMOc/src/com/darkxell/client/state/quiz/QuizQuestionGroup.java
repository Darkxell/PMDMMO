package com.darkxell.client.state.quiz;

import java.util.List;

import org.jdom2.Element;

import com.darkxell.common.util.xml.XMLUtils;

public class QuizQuestionGroup
{

	public final int id;
	public final QuizQuestion[] questions;

	public QuizQuestionGroup(Element xml)
	{
		this.id = XMLUtils.getAttribute(xml, "id", 0);

		List<Element> questions = xml.getChildren("question", xml.getNamespace());
		this.questions = new QuizQuestion[questions.size()];
		for (int i = 0; i < this.questions.length; ++i)
			this.questions[i] = new QuizQuestion(questions.get(i), "quiz." + this.id + "." + i);
	}

}
