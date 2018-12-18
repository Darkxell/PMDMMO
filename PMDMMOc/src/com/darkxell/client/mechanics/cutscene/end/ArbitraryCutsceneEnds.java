package com.darkxell.client.mechanics.cutscene.end;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.mechanics.cutscene.end.arbitrary.StartNameAsk;
import com.darkxell.client.state.OpenningState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ArbitraryCutsceneEnds
{

	private static void defaultFunction(String function, Cutscene cutscene)
	{
		Logger.w("Tried to execute function '" + function + "' but wouldn't find it :(");
	}

	public static void execute(String function, Cutscene cutscene)
	{
		JsonObject mess;
		switch (function)
		{
			case "startnameask":
				StartNameAsk.startNameAsk();
				break;
			case "enterwoods":
				mess = Json.object().add("action", "storyadvance").add("target", 2);
				Persistence.socketendpoint.sendMessage(mess.toString());
				break;
			case "magnemiteaccept":
				mess = Json.object().add("action", "storyadvance").add("target", 5);
				Persistence.socketendpoint.sendMessage(mess.toString());
				break;
			case "magnemiteend":
				mess = Json.object().add("action", "storyadvance").add("target", 8);
				Persistence.socketendpoint.sendMessage(mess.toString());
				Persistence.player.setStoryPosition(8);
				break;
			case "openingstate":
				mess = Json.object().add("action", "storyadvance").add("target", 4);
				Persistence.socketendpoint.sendMessage(mess.toString());
				Persistence.player.setStoryPosition(4);
				if (Persistence.stateManager instanceof PrincipalMainState)
				{
					((PrincipalMainState) Persistence.stateManager).setState(new OpenningState());
				}
				break;
			case "predream":
				switch (Persistence.player.storyPosition())
				{
					case 4:
						CutsceneManager.playCutscene("base/dream1", true);
					case 10:
						CutsceneManager.playCutscene("skarmory/dream", true);
				}
			default:
				defaultFunction(function, cutscene);
				break;
		}
	}

}
