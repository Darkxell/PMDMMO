package com.darkxell.client.mechanics.cutscene.end;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.mechanics.cutscene.end.arbitrary.StartNameAsk;
import com.darkxell.client.state.OpeningState;
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
            Persistence.socketendpoint.sendMessage(mess.toString());
            break;
        case "magnemiteaccept":
            mess = Json.object().add("action", "storyadvance").add("target", 5);
            Persistence.socketendpoint.sendMessage(mess.toString());
            break;
        case "magnemiteend":
            stryAdvance(8);
            break;
        case "openingstate":
            mess = Json.object().add("action", "storyadvance").add("target", 4);
            Persistence.socketendpoint.sendMessage(mess.toString());
            Persistence.player.setStoryPosition(4);
            if (Persistence.stateManager instanceof PrincipalMainState)
                Persistence.stateManager.setState(new OpeningState());
            break;
        case "predream":
            switch (Persistence.player.storyPosition()) {
            case 6:
            case 4:
                CutsceneManager.playCutscene("base/dream1", true);
                break;
            case 10:
                CutsceneManager.playCutscene("skarmory/dream", true);
                break;
            case 13: 
                CutsceneManager.playCutscene("wigglytuff/dream", true);
                break;
            }
            break;
        case "squaretutorial":
            stryAdvance(10);
            break;
        case "dugrequest":
            stryAdvance(11);
            break;
        case "skarmoryend":
            stryAdvance(13);
            break;
        case "wigglytuffintroduced":
            stryAdvance(16);
            break;
        default:
            defaultFunction(function, cutscene);
            break;
        }
    }

    /**
     * Shortcut procedure to send a storyadvance payload to the server and set the clientside storyposition to a
     * specific value.
     */
    private static void stryAdvance(int to) {
        JsonObject mess;
        mess = Json.object().add("action", "storyadvance").add("target", to);
        Persistence.socketendpoint.sendMessage(mess.toString());
        Persistence.player.setStoryPosition(to);
    }

}
