package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.common.util.XMLUtils;

public class MusicCutsceneEvent extends CutsceneEvent
{

	public final String soundtrackID;

	public MusicCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.soundtrackID = XMLUtils.getAttribute(xml, "music", null);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (this.soundtrackID != null) Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong(this.soundtrackID));
	}

}
