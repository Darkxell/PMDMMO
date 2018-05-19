package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.quiz.PersonalityQuizState;
import com.eclipsesource.json.JsonObject;

public class TestResultConfirmHandler extends MessageHandler
{

	@Override
	public void handleMessage(JsonObject message)
	{
		AbstractState state = Persistance.stateManager.getCurrentState();
		if (state != null && state instanceof PersonalityQuizState) ((PersonalityQuizState) state).onResultConfirmed();
	}

}
