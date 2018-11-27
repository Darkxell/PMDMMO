package com.darkxell.client.mechanics.cutscene.end;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.end.arbitrary.StartNameAsk;
import com.darkxell.client.state.OpenningState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ArbitraryCutsceneEnds {

	private static void defaultFunction(String function, Cutscene cutscene) {
		Logger.w("Tried to execute function '" + function + "' but wouldn't find it :(");
	}

	public static void execute(String function, Cutscene cutscene) {
		JsonObject mess;
		switch (function) {
		case "startnameask":
			StartNameAsk.startNameAsk();
			break;
		case "enterwoods":
			mess = Json.object().add("action", "storyadvance").add("target", 2);
			Persistance.socketendpoint.sendMessage(mess.toString());
			break;
		case "magnemiteaccept":
			mess = Json.object().add("action", "storyadvance").add("target", 5);
			Persistance.socketendpoint.sendMessage(mess.toString());
			break;
		case "magnemiteend":
			mess = Json.object().add("action", "storyadvance").add("target", 8);
			Persistance.socketendpoint.sendMessage(mess.toString());
			Persistance.player.setStoryPosition(8);
			break;
		case "openingstate":
			mess = Json.object().add("action", "storyadvance").add("target", 4);
			Persistance.socketendpoint.sendMessage(mess.toString());
			Persistance.player.setStoryPosition(4);
			if(Persistance.stateManager instanceof PrincipalMainState) {
				((PrincipalMainState)Persistance.stateManager).setState(new OpenningState());
			}
			break;
		default:
			defaultFunction(function, cutscene);
			break;
		}
	}

}
