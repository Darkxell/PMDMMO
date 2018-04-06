package com.darkxell.client.state.quiz;

import java.awt.Graphics2D;

import com.darkxell.client.renderers.layers.BackgroundLsdLayer;
import com.darkxell.client.state.AbstractState;

public class PersonalityQuizState extends AbstractState
{

	private BackgroundLsdLayer background;
	private QuizData quizData;

	public PersonalityQuizState()
	{
		this.background = new BackgroundLsdLayer();
		this.quizData = new QuizData();
		this.quizData.chooseQuestions(8);
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void onMouseClick(int x, int y)
	{
		super.onMouseClick(x, y);
	}

	@Override
	public void onMouseMove(int x, int y)
	{
		super.onMouseMove(x, y);
	}

	@Override
	public void onMouseRightClick(int x, int y)
	{
		super.onMouseRightClick(x, y);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		this.background.render(g, width, height);
	}

	@Override
	public void update()
	{
		this.background.update();
	}

}
