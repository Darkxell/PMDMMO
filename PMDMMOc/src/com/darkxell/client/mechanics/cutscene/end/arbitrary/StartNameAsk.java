package com.darkxell.client.mechanics.cutscene.end.arbitrary;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.state.dialog.TextinputState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.util.Callbackable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class StartNameAsk implements Callbackable {

	@Override
	public void callback(String s) {
		// Sends the nickname payload to the server.
		JsonObject mess = Json.object().add("action", "nickname")
				.add("pokemon", Persistance.player.getData().mainpokemon.id).add("nickname", s);
		Persistance.socketendpoint.sendMessage(mess.toString());
		// Changes the nickname locally
		Persistance.player.getTeamLeader().setNickname(s);
		CutsceneManager.playCutscene("startingwoods/problem");
	}

	public static void startNameAsk() {
		((PrincipalMainState) Persistance.stateManager)
				.setState(new TextinputState(((PrincipalMainState) Persistance.stateManager).getCurrentState(),
						"ui.textinput.name", new StartNameAsk()));
	}

}
