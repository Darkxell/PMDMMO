package com.darkxell.client.state.quiz;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.layers.BackgroundLsdLayer;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.AbstractDialogState;
import com.darkxell.client.state.dialog.AbstractDialogState.DialogEndListener;
import com.darkxell.client.state.dialog.AbstractDialogState.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.client.state.dialog.NarratorDialogState;
import com.darkxell.client.state.dialog.OptionDialogState;
import com.darkxell.client.state.quiz.QuizData.QuizGender;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.language.Message;

public class PersonalityQuizState extends AbstractState implements DialogEndListener
{
	private static final int startDialogScreens = 7;

	private BackgroundLsdLayer background;
	private DialogState currentDialog;
	private int[] currentNature;
	private int currentQuestion;
	private Nature finalNature;
	private QuizGender gender;
	private QuizQuestion[] questions;
	private QuizData quizData;
	private Pokemon starter;
	private int tick = 0;

	public PersonalityQuizState()
	{
		this.background = new BackgroundLsdLayer();
		this.quizData = new QuizData();
		this.quizData.chooseQuestions(8);
		this.questions = this.quizData.askedQuestions();
		this.currentNature = new int[Nature.values().length];
		this.currentQuestion = -1;
	}

	private void loadNewDialog()
	{
		if (this.currentQuestion >= this.questions.length + 2) this.currentDialog = new DialogState(this, this, true,
				new DialogScreen(new Message("quiz.starter").addReplacement("<pokemon>", this.starter.getNickname())));
		else if (this.currentQuestion == this.questions.length + 1) this.currentDialog = new DialogState(this, this, this.finalNature.description());
		else if (this.currentQuestion == this.questions.length) this.currentDialog = new OptionDialogState(this, this,
				Arrays.asList(new DialogScreen(new Message("quiz.gender"))), new Message("quiz.gender.boy"), new Message("quiz.gender.girl"));
		else this.currentDialog = new OptionDialogState(this, this, Arrays.asList(new DialogScreen(this.questions[this.currentQuestion].name)),
				this.questions[this.currentQuestion].options());

		Persistance.stateManager.setState(this.currentDialog);
	}

	@Override
	public void onDialogEnd(AbstractDialogState dialog)
	{

		if (this.currentQuestion != -1 && this.currentQuestion < this.questions.length)
		{
			QuizAnswer answer = this.questions[this.currentQuestion].answers[((OptionDialogState) dialog).chosenIndex()];
			for (int i = 0; i < answer.natures.length; ++i)
				this.currentNature[answer.natures[i].id] += answer.points[i];
		}

		++this.currentQuestion;
		Persistance.stateManager.setState(this);

		if (this.currentQuestion == this.questions.length)
		{
			this.finalNature = Nature.Brave;
			for (int i = 0; i < this.currentNature.length; ++i)
				if (this.currentNature[this.finalNature.id] < this.currentNature[i]) this.finalNature = Nature.get(i);
		} else if (this.currentQuestion == this.questions.length + 1)
		{
			this.gender = ((OptionDialogState) dialog).chosenIndex() == 0 ? QuizGender.Boy : QuizGender.Girl;
			int starterID = this.quizData.starters[this.finalNature.id][this.gender == QuizGender.Boy ? 0 : 1];
			this.starter = PokemonRegistry.find(starterID).generate(new Random(), 5);
		}

		if (this.currentQuestion > 0) this.loadNewDialog();
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void onStart()
	{
		super.onStart();

		if (this.currentQuestion == -1)
		{
			Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong("04 Personality Test.mp3"));
			ArrayList<DialogScreen> screens = new ArrayList<>();
			for (int i = 0; i < startDialogScreens; ++i)
				screens.add(new DialogScreen(new Message("quiz.start_dialog." + i)));
			Persistance.stateManager.setState(new NarratorDialogState(this, screens));
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		this.background.render(g, width, height);
		if (this.currentQuestion == this.questions.length + 2)
			PokemonPortrait.drawPortrait(g, this.starter, width / 2 - PokemonPortrait.PORTRAIT_SIZE / 2, height / 2 - PokemonPortrait.PORTRAIT_SIZE / 2);

		if (this.tick <= NarratorDialogState.FADETIME)
		{
			double alpha = 255 - (this.tick * 1. / NarratorDialogState.FADETIME) * 255;
			g.setColor(new Color(0, 0, 0, (int) alpha));
			g.fillRect(0, 0, width, height);
		}
	}

	@Override
	public void update()
	{
		if (this.isMain() && this.currentQuestion == 0)
		{
			if (this.tick <= NarratorDialogState.FADETIME) ++this.tick;
			if (this.tick == NarratorDialogState.FADETIME) this.loadNewDialog();
		}
		this.background.update();
	}

}
