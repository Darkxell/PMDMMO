package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.common.util.Logger;

public class Debugentity extends FreezoneEntity
{

	public Debugentity(double x, double y)
	{
		super(false, true, x, y);
	}

	@Override
	public void onInteract()
	{
		Logger.d("Triggered debug entity script.");
		// Do your debug shit here.
		// TRIGGERED politeness police!!! --Cubi
		// ((PrincipalMainState) Persistance.stateManager).setState(new TextinputState(((PrincipalMainState) Persistance.stateManager).getCurrentState()));
		CutsceneManager.playCutscene("startingwoods/found", true);
		// Persistance.stateManager.setState(new PersonalityQuizDialog().getLoadingState());
	}

	@Override
	public void print(Graphics2D g)
	{
		g.setColor(Color.BLUE);
		g.fillRect((int) (super.posX * 8) - 7, (int) (super.posY * 8) - 7, 15, 15);
		g.setColor(Color.RED);
		g.drawString("D", (int) (super.posX * 8) - 6, (int) (super.posY * 8) + 5);
	}

	@Override
	public void update()
	{}

}
