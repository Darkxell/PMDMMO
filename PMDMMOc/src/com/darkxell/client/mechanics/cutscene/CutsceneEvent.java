package com.darkxell.client.mechanics.cutscene;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.event.AnimateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.CameraCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DelayCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DespawnCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.MoveCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.MusicCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.RotateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SetAnimatedCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SetStateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SoundCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SpawnCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.WaitCutsceneEvent;
import com.darkxell.common.util.XMLUtils;

public abstract class CutsceneEvent
{

	public static CutsceneEvent create(Element xml, Cutscene cutscene)
	{
		// Remember to add to Editor aswell (SelectEventTypeController#CutsceneEventType).
		switch (xml.getName())
		{
			case "animate":
				return new AnimateCutsceneEvent(xml, cutscene);

			case "camera":
				return new CameraCutsceneEvent(xml, cutscene);

			case "delay":
				return new DelayCutsceneEvent(xml, cutscene);

			case "despawn":
				return new DespawnCutsceneEvent(xml, cutscene);

			case "dialog":
				return new DialogCutsceneEvent(xml, cutscene);

			case "move":
				return new MoveCutsceneEvent(xml, cutscene);

			case "music":
				return new MusicCutsceneEvent(xml, cutscene);

			case "rotate":
				return new RotateCutsceneEvent(xml, cutscene);

			case "setstate":
				return new SetStateCutsceneEvent(xml, cutscene);

			case "setanimated":
				return new SetAnimatedCutsceneEvent(xml, cutscene);

			case "sound":
				return new SoundCutsceneEvent(xml, cutscene);

			case "spawn":
			case "spawnpokemon":
				return new SpawnCutsceneEvent(xml, cutscene);

			case "wait":
				return new WaitCutsceneEvent(xml, cutscene);

			default:
				return null;
		}
	}

	public final Cutscene cutscene;
	int id;

	public CutsceneEvent(Element xml, Cutscene cutscene)
	{
		this.id = XMLUtils.getAttribute(xml, "eventid", -1);
		this.cutscene = cutscene;
	}

	public boolean isOver()
	{
		return true;
	}

	public void onFinish()
	{}

	public void onStart()
	{}

	public abstract Element toXML();

	public void update()
	{}

	public abstract String getIconPath();

}
