package com.darkxell.common.zones;

import com.darkxell.common.util.language.Message;

/** Enum that holds informations about all the localmap locations in the game */
public enum LocalMapLocation {

	BASE(340, 160, new Message("zone.base"), true),
	BASEINSIDE(340, 150, new Message("zone.base"), false),
	SQUARE(355, 165, new Message("zone.square"), false),
	DOJO(353, 180, new Message("zone.dojo"), false),
	POND(350, 147, new Message("zone.wpond"), false),
	OFFICE(372, 160, new Message("zone.office"), false),
	STARTFOREST(320, 150, new Message("zone.startforest"), false),
	LUMINOUSCAVE(355, 137, new Message("zone.lumiouscave"), false),

	DUNGEON_MT_STEEL_TOP(-1, -1, new Message("zone.dungeon.mt_steel_top", true), false),
	MT_STEEL(-1, -1, new Message("zone.dungeon.mt_steel", true), false),
	TINYWOODS(-1, -1, new Message("zone.dungeon.tiny_woods", true), false),
	THUNDERWAVE(-1, -1, new Message("zone.dungeon.thunderwave_cave", true), false),

	ZONE_PLAINS(350, 119, new Message("zone.plains"), true),
	ZONE_CAVERN(355, 70, new Message("zone.cavern"), true),
	ZONE_NORTHENLAKE(384, 31, new Message("zone.northenlake"), true),
	ZONE_SWAMP(414, 69, new Message("zone.swamp"), true),
	ZONE_GLACIER(454, 21, new Message("zone.glacier"), true),
	ZONE_MOUNTAINS(455, 113, new Message("zone.mountains"), true),
	ZONE_ORIENTALFOREST(460, 162, new Message("zone.orientalforest"), true),
	ZONE_RIVER(400, 158, new Message("zone.river"), true),
	ZONE_EASTERNLAKESIDE(402, 206, new Message("zone.easternlakeside"), true),
	ZONE_ORIENTALLAKE(452, 206, new Message("zone.orientallake"), true),
	ZONE_DESERT(450, 309, new Message("zone.desert"), true),
	ZONE_PLATEAU(403, 256, new Message("zone.plateau"), true),
	ZONE_SOUTHERNPLAINS(350, 310, new Message("zone.southernplains"), true),
	ZONE_JUNGLE(343, 260, new Message("zone.jungle"), true),
	ZONE_RUINS(300, 260, new Message("zone.ruins"), true),
	ZONE_VOLCANO(267, 204, new Message("zone.volcano"), true),
	ZONE_OCCIDENTALFOREST(265, 154, new Message("zone.occidentalforest"), true),
	ZONE_POND(308, 126, new Message("zone.pond"), true),
	ZONE_REMAINS(320, 24, new Message("zone.remains"), true),
	ZONE_NORTHENISLAND(263, 74, new Message("zone.northenisland"), true),
	ZONE_NORTHENSEA(212, 25, new Message("zone.northensea"), true),
	ZONE_SKY(30, 25, new Message("zone.sky"), true),
	ZONE_SEASIDE(145, 75, new Message("zone.seaside"), true),
	ZONE_WESTERNISLAND(49, 116, new Message("zone.westernisland"), true),
	ZONE_SOUTHERNISLAND(91, 265, new Message("zone.southernisland"), true),
	ZONE_SOUTHWESTISLAND(37, 207, new Message("zone.southwestisland"), true),
	ZONE_SOUTHERNSEA(147, 292, new Message("zone.southernsea"), true);

	public final int x;
	public final int y;
	public final Message displayname;
	public final boolean showsonfriendsmap;

	private LocalMapLocation(int x, int y, Message name, boolean showonfriendsmap) {
		this.x = x;
		this.y = y;
		this.displayname = name;
		this.showsonfriendsmap = showonfriendsmap;
	}

}
