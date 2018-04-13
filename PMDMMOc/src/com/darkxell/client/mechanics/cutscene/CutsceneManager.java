package com.darkxell.client.mechanics.cutscene;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.XMLUtils;

public class CutsceneManager
{

	public static Cutscene loadCutscene(String id)
	{
		return new Cutscene(XMLUtils.readFile(Res.getFile("/cutscenes/" + id + ".xml")));
	}

}
