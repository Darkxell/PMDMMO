package com.darkxell.client.mechanics.freezone;

import java.io.IOException;

import org.jdom2.JDOMException;

import com.darkxell.common.zones.FreezoneInfo;

public class CutsceneFreezoneMap extends FreezoneMap {
	
	public CutsceneFreezoneMap(String xmlPath, FreezoneInfo info, int width, int height) {
		super(xmlPath, info);
		this.terrain = new FreezoneTerrain(xmlPath, width, height);
	}

	@Override
	protected void load(String xmlPath, boolean isRoot) throws IOException, JDOMException {
	}
}
