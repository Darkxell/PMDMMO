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
		super(xml, CutsceneEventType.music, cutscene);
		this.soundtrackID = XMLUtils.getAttribute(xml, "music", null);
	}

	public MusicCutsceneEvent(int id, String soundtrackID)
	{
		super(id, CutsceneEventType.music);
		this.soundtrackID = soundtrackID;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (this.soundtrackID != null) Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong(this.soundtrackID));
	}

	@Override
	public String toString()
	{
		return "Music set to " + this.soundtrackID;
	}

	@Override
	public Element toXML()
	{
		return super.toXML().setAttribute("music", this.soundtrackID);
	}

}
