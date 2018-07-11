package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.resources.music.SoundManager;
import com.darkxell.common.util.XMLUtils;

public class SoundCutsceneEvent extends CutsceneEvent
{

	public final boolean playOverMusic;
	public final String soundID;

	public SoundCutsceneEvent(Element xml, Cutscene cutscene)
	{
		super(xml, cutscene);
		this.soundID = XMLUtils.getAttribute(xml, "music", null);
		this.playOverMusic = XMLUtils.getAttribute(xml, "overmusic", false);
	}

	@Override
	public String getIconPath()
	{
		return "/icons/events/sound.png";
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (this.soundID != null)
		{
			if (this.playOverMusic) SoundManager.playSoundOverMusic(this.soundID);
			else SoundManager.playSound(this.soundID);
		}
	}

	@Override
	public String toString()
	{
		return "Play " + this.soundID + (this.playOverMusic ? " over music" : "");
	}

	@Override
	public Element toXML()
	{
		return new Element("sound");
	}

}
