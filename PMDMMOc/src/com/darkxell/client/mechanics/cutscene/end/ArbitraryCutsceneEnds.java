package com.darkxell.client.mechanics.cutscene.end;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.common.util.Logger;

public class ArbitraryCutsceneEnds
{

	private static void defaultFunction(String function, Cutscene cutscene)
	{
		Logger.w("Tried to execute function '" + function + "' but wouldn't find it :(");
	}

	public static void execute(String function, Cutscene cutscene)
	{
		switch (function)
		{

			default:
				defaultFunction(function, cutscene);
				break;
		}
	}

}