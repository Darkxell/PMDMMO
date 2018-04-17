package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.common.util.XMLUtils;

public class CutsceneCreation
{

	public final Cutscene cutscene;
	private final ArrayList<CutsceneEntity> entities;
	public final boolean fading;
	public final String freezoneID;
	public final CutscenePokemon player;

	public CutsceneCreation(Cutscene cutscene, Element xml)
	{
		this.cutscene = cutscene;
		this.freezoneID = XMLUtils.getAttribute(xml, "freezone", null);
		this.fading = XMLUtils.getAttribute(xml, "fade", true);
		this.player = new CutscenePokemon(xml.getChild("player", xml.getNamespace()));
		this.entities = new ArrayList<>();
		for (Element pokemon : xml.getChildren("pokemon", xml.getNamespace()))
			this.entities.add(new CutscenePokemon(pokemon));
		for (Element entity : xml.getChildren("entity", xml.getNamespace()))
			this.entities.add(new CutsceneEntity(entity));
	}

}
