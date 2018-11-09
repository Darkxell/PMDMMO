package com.darkxell.client.mechanics.cutscene.end;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.end.arbitrary.StartNameAsk;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ArbitraryCutsceneEnds {

	private static void defaultFunction(String function, Cutscene cutscene) {
		Logger.w("Tried to execute function '" + function + "' but wouldn't find it :(");
	}

	public static void execute(String function, Cutscene cutscene) {
		switch (function) {
		case "startnameask":
			StartNameAsk.startNameAsk();
			break;
		case "enterwoods":
			JsonObject mess = Json.object().add("action", "storyadvance").add("target", 2);
			Persistance.socketendpoint.sendMessage(mess.toString());
			break;
		default:
			defaultFunction(function, cutscene);
			break;
		}
	}

}
