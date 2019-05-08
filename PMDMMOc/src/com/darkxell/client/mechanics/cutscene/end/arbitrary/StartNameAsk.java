package com.darkxell.client.mechanics.cutscene.end.arbitrary;

import com.darkxell.client.launchable.Persistence;
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
				.add("pokemonid", Persistence.player.getData().mainpokemon.id).add("nickname", s);
		Persistence.socketendpoint.sendMessage(mess.toString());
		// Changes the nickname locally
		Persistence.player.getTeamLeader().setNickname(s);
		CutsceneManager.playCutscene("startingwoods/problem", false);
	}

	public static void startNameAsk() {
		((PrincipalMainState) Persistence.stateManager)
				.setState(new TextinputState(((PrincipalMainState) Persistence.stateManager).getCurrentState(),
						"ui.textinput.name", new StartNameAsk()));
	}

}
