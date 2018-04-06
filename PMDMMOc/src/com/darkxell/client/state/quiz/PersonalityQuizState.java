package com.darkxell.client.state.quiz;

import java.awt.Graphics2D;
import java.util.Arrays;

import com.darkxell.client.renderers.layers.BackgroundLsdLayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogEndListener;
import com.darkxell.client.state.DialogState.DialogScreen;
import com.darkxell.common.util.language.Message;

public class PersonalityQuizState extends AbstractState implements DialogEndListener
{

	private BackgroundLsdLayer background;
	private DialogState currentDialog;
	private int[] currentNature;
	private int currentQuestion;
	private QuizQuestion[] questions;
	private QuizData quizData;

	public PersonalityQuizState()
	{
		this.background = new BackgroundLsdLayer();
		this.quizData = new QuizData();
		this.quizData.chooseQuestions(8);
		this.questions = this.quizData.askedQuestions();
		this.currentNature = new int[Nature.values().length];
		this.currentQuestion = 0;

		this.loadNewDialog();
	}

	private void loadNewDialog()
	{
		if (this.currentQuestion >= this.questions.length)
			this.currentDialog = new DialogState(null, this, Arrays.asList(new DialogScreen(new Message("This is the end of this test.", false))));
		else this.currentDialog = new DialogState(null, this, Arrays.asList(new DialogScreen(this.questions[this.currentQuestion].name)));
	}

	@Override
	public void onDialogEnd(DialogState dialog)
	{
		++this.currentQuestion;
		this.loadNewDialog();
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (this.currentDialog != null) this.currentDialog.onKeyPressed(key);
	}

	@Override
	public void onKeyReleased(short key)
	{
		if (this.currentDialog != null) this.currentDialog.onKeyReleased(key);
	}

	@Override
	public void onMouseClick(int x, int y)
	{
		super.onMouseClick(x, y);
		if (this.currentDialog != null) this.currentDialog.onMouseClick(x, y);
	}

	@Override
	public void onMouseMove(int x, int y)
	{
		super.onMouseMove(x, y);
		if (this.currentDialog != null) this.currentDialog.onMouseMove(x, y);
	}

	@Override
	public void onMouseRightClick(int x, int y)
	{
		super.onMouseRightClick(x, y);
		if (this.currentDialog != null) this.currentDialog.onMouseRightClick(x, y);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		this.background.render(g, width, height);
		if (this.currentDialog != null) this.currentDialog.render(g, width, height);
	}

	@Override
	public void update()
	{
		this.background.update();
		if (this.currentDialog != null) this.currentDialog.update();
	}

}
