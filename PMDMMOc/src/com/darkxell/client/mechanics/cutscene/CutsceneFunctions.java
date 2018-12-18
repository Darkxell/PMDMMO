package com.darkxell.client.mechanics.cutscene;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.event.FunctionCutsceneEvent;
import com.darkxell.common.util.Logger;

public class CutsceneFunctions {

	public static void call(String functionID, Cutscene parent, FunctionCutsceneEvent sourceEvent) {
		switch (functionID) {
			case "earthquake(0)":
				earthquake(parent, 0);
				break;
			case "earthquake(1)":
				earthquake(parent, 1);
				break;
			case "earthquake(2)":
				earthquake(parent, 2);
				break;
			default:
				defaultFunction(functionID, parent, sourceEvent);
				break;
		}
	}

	private static void defaultFunction(String functionID, Cutscene parent, FunctionCutsceneEvent sourceEvent) {
		Logger.w("Cutscene function '" + functionID + "' is unknown!");
	}

	private static void earthquake(Cutscene parent, int strength) {
		Persistence.freezoneCamera.setShaking(strength);
	}

}
