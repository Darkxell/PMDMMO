package com.darkxell.client.mechanics.freezones;

import com.darkxell.common.util.language.Message;

/** Enum that holds informations about all the localmap locations in the game */
public enum LocalMapLocation {

	BASE(340, 160, new Message("zone.base")),
	SQUARE(355, 165, new Message("zone.square")),
	DOJO(353, 180, new Message("zone.dojo")),
	POND(350, 147, new Message("zone.wpond")),
	OFFICE(372, 160, new Message("zone.office")),
	STARTFOREST(320, 150, new Message("zone.startforest")),
	LUMINOUSCAVE(355, 137, new Message("zone.lumiouscave")),

	ZONE_PLAINS(350, 119, new Message("zone.plains")),
	ZONE_CAVERN(355, 70, new Message("zone.cavern")),
	ZONE_NORTHENLAKE(384, 31, new Message("zone.northenlake")),
	ZONE_SWAMP(414, 69, new Message("zone.swamp")),
	ZONE_GLACIER(454, 21, new Message("zone.glacier")),
	ZONE_MOUNTAINS(455, 113, new Message("zone.mountains")),
	ZONE_ORIENTALFOREST(460, 162, new Message("zone.orientalforest")),
	ZONE_RIVER(400, 158, new Message("zone.river")),
	ZONE_EASTERNLAKESIDE(402, 206, new Message("zone.easternlakeside")),
	ZONE_ORIENTALLAKE(452, 206, new Message("zone.orientallake")),
	ZONE_DESERT(450, 309, new Message("zone.desert")),
	ZONE_PLATEAU(403, 256, new Message("zone.plateau")),
	ZONE_SOUTHERNPLAINS(350, 310, new Message("zone.southernplains")),
	ZONE_JUNGLE(343, 260, new Message("zone.jungle")),
	ZONE_RUINS(300, 260, new Message("zone.ruins")),
	ZONE_VOLCANO(267, 204, new Message("zone.volcano")),
	ZONE_OCCIDENTALFOREST(265, 154, new Message("zone.occidentalforest")),
	ZONE_POND(308, 126, new Message("zone.pond")),
	ZONE_REMAINS(320, 24, new Message("zone.remains")),
	ZONE_NORTHENISLAND(263, 74, new Message("zone.northenisland")),
	ZONE_NORTHENSEA(212, 25, new Message("zone.northensea")),
	ZONE_SKY(30, 25, new Message("zone.sky")),
	ZONE_SEASIDE(145, 75, new Message("zone.seaside")),
	ZONE_WESTERNISLAND(49, 116, new Message("zone.westernisland")),
	ZONE_SOUTHERNISLAND(91, 265, new Message("zone.southernisland")),
	ZONE_SOUTHWESTISLAND(37, 207, new Message("zone.southwestisland")),
	ZONE_SOUTHERNSEA(147, 292, new Message("zone.southernsea"));

	public final int x;
	public final int y;
	public final Message displayname;

	private LocalMapLocation(int x, int y, Message name) {
		this.x = x;
		this.y = y;
		this.displayname = name;
	}

}
