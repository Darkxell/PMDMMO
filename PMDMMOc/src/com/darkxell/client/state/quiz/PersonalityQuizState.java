package com.darkxell.client.state.quiz;

import java.awt.Graphics2D;
import java.util.Arrays;

import com.darkxell.client.renderers.layers.BackgroundLsdLayer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogEndListener;
import com.darkxell.client.state.DialogState.DialogScreen;
import com.darkxell.client.state.quiz.QuizData.QuizGender;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class PersonalityQuizState extends AbstractState implements DialogEndListener
{

	private BackgroundLsdLayer background;
	private DialogState currentDialog;
	private int[] currentNature;
	private int currentQuestion;
	private Nature finalNature;
	private QuizGender gender;
	private QuizQuestion[] questions;
	private QuizData quizData;
	private PokemonSpecies starter;

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
		if (this.currentQuestion > this.questions.length) this.currentDialog = new DialogState(null, this,
				Arrays.asList(new DialogScreen(new Message(
						"This is the end of this test. Your nature is: " + this.finalNature + ". Your starter is: " + this.starter.speciesName() + ".",
						false))));
		else if (this.currentQuestion == this.questions.length)
			this.currentDialog = new DialogState(null, this, Arrays.asList(new DialogScreen(new Message("quiz.gender"))));
		else this.currentDialog = new DialogState(null, this, Arrays.asList(new DialogScreen(this.questions[this.currentQuestion].name)));
	}

	@Override
	public void onDialogEnd(DialogState dialog)
	{
		if (this.currentQuestion < this.questions.length)
		{// TODO: actually let the user choose an answer
			QuizAnswer answer = this.questions[this.currentQuestion].answers[(int) (Math.random() * this.questions[this.currentQuestion].answers.length)];
			for (int i = 0; i < answer.natures.length; ++i)
				this.currentNature[answer.natures[i].id] += answer.points[i];
		}

		++this.currentQuestion;

		if (this.currentQuestion == this.questions.length)
		{
			this.finalNature = Nature.Brave;
			for (int i = 0; i < this.currentNature.length; ++i)
				if (this.currentNature[this.finalNature.id] < this.currentNature[i]) this.finalNature = Nature.get(i);
		} else if (this.currentQuestion == this.questions.length + 1)
		{
			this.gender = Math.random() >= .5 ? QuizGender.Boy : QuizGender.Girl;
			int starterID = this.quizData.starters[this.finalNature.id][this.gender == QuizGender.Boy ? 0 : 1];
			this.starter = PokemonRegistry.find(starterID);
		}

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
