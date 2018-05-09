package com.darkxell.client.mechanics.cutscene;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.end.EnterDungeonCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.LoadFreezoneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.PlayCutsceneCutsceneEnd;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;
import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;

public class Cutscene
{

	public static abstract class CutsceneEnd
	{
		public static CutsceneEnd create(Cutscene cutscene, Element xml)
		{
			if (xml.getChild("enterdungeon", xml.getNamespace()) != null) return new EnterDungeonCutsceneEnd(cutscene, xml);
			if (xml.getChild("playcutscene", xml.getNamespace()) != null) return new PlayCutsceneCutsceneEnd(cutscene, xml);
			if (xml.getChild("loadfreezone", xml.getNamespace()) != null) return new LoadFreezoneCutsceneEnd(cutscene, xml);
			return null;
		}

		public final Cutscene cutscene;

		public CutsceneEnd(Cutscene cutscene)
		{
			this.cutscene = cutscene;
		}

		public void onCutsceneEnd()
		{
			Persistance.freezoneCamera = new FreezoneCamera(Persistance.currentplayer);
		}
	}

	public final CutsceneCreation creation;
	public final ArrayList<CutsceneEvent> events;
	public final CutsceneEnd onFinish;
	public final CutscenePlayer player;

	public Cutscene(Element xml)
	{
		this.creation = new CutsceneCreation(this, xml.getChild("creation", xml.getNamespace()));
		this.onFinish = CutsceneEnd.create(this, xml.getChild("onfinish", xml.getNamespace()));

		this.events = new ArrayList<>();
		for (Element event : xml.getChild("events", xml.getNamespace()).getChildren())
		{
			CutsceneEvent e = CutsceneEvent.create(event, this);
			if (e != null) this.events.add(e);
		}
		
		this.player = new CutscenePlayer(this);
	}

	public CutsceneEvent getEvent(int id)
	{
		for (CutsceneEvent e : this.events)
			if (e.id == id) return e;
		return null;
	}

	public FreezoneMap loadMap()
	{
		String baseName = BaseFreezone.class.getName();
		Class<?> c;
		try
		{
			c = Class.forName(baseName.substring(0, baseName.length() - "BaseFreezone".length()) + this.creation.freezoneID + "Freezone");
			if (c == null) return null;
			FreezoneMap map = (FreezoneMap) c.getConstructor().newInstance();
			return map;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
