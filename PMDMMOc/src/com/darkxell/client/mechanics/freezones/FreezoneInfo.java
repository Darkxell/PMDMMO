package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;
import com.darkxell.client.mechanics.freezones.zones.DojoFreezone;
import com.darkxell.client.mechanics.freezones.zones.ForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.LumiousCaveFreezone;
import com.darkxell.client.mechanics.freezones.zones.OfficeFreezone;
import com.darkxell.client.mechanics.freezones.zones.PokemonSquareFreezone;
import com.darkxell.client.mechanics.freezones.zones.PondFreezone;
import com.darkxell.common.util.language.Message;

public enum FreezoneInfo
{

	BASE(LocalMapLocation.BASE, "base"),
	SQUARE(LocalMapLocation.SQUARE, "square"),
	DOJO(LocalMapLocation.DOJO, "dojo"),
	POND(LocalMapLocation.POND, "wpond"),
	OFFICE(LocalMapLocation.OFFICE, "office"),
	STARTFOREST(LocalMapLocation.STARTFOREST, "startforest"),
	LUMINOUSCAVE(LocalMapLocation.LUMINOUSCAVE, "lumiouscave"),

	FRIEND_AGEDCHAMBER1(LocalMapLocation.ZONE_RUINS, "agedchamber1"),
	FRIEND_AGEDCHAMBER2(LocalMapLocation.ZONE_RUINS, "agedchamber2"),
	FRIEND_ANCIENTRELIC(LocalMapLocation.ZONE_RUINS, "ancientrelic"),
	FRIEND_BEAUPLAINS(LocalMapLocation.ZONE_PLAINS, "beauplains"),
	FRIEND_BOULDERCAVE(LocalMapLocation.ZONE_CAVERN, "bouldercave"),
	FRIEND_BOUNTIFULSEA(LocalMapLocation.ZONE_NORTHENSEA, "bountifulsea"),
	FRIEND_CRATER(LocalMapLocation.ZONE_VOLCANO, "crater"),
	FRIEND_CRYPTICCAVE(LocalMapLocation.ZONE_CAVERN, "crypticcave"),
	FRIEND_DARKNESSRIDGE(LocalMapLocation.ZONE_MOUNTAINS, "darknessridge"),
	FRIEND_DECREPITLAB(LocalMapLocation.ZONE_REMAINS, "decrepitlab"),
	FRIEND_DEEPSEACURRENT(LocalMapLocation.ZONE_NORTHENSEA, "deepseacurrent"),
	FRIEND_DEEPSEAFLOOR(LocalMapLocation.ZONE_SOUTHERNSEA, "deepseafloor"),
	FRIEND_DRAGONCAVE(LocalMapLocation.ZONE_CAVERN, "dragoncave"),
	FRIEND_ECHOCAVE(LocalMapLocation.ZONE_CAVERN, "echocave"),
	FRIEND_ENCLOSEDISLAND(LocalMapLocation.ZONE_SOUTHWESTISLAND, "enclosedisland"),
	FRIEND_ENERGETICFOREST(LocalMapLocation.ZONE_ORIENTALFOREST, "energeticforest"),
	FRIEND_FINALISLAND(LocalMapLocation.ZONE_WESTERNISLAND, "finalisland"),
	FRIEND_FLYAWAYFOREST(LocalMapLocation.ZONE_ORIENTALFOREST, "flyawayforest"),
	FRIEND_FRIGIDCAVERN(LocalMapLocation.ZONE_GLACIER, "frigidcavern"),
	FRIEND_FURNACEDESERT(LocalMapLocation.ZONE_DESERT, "furnacedesert"),
	FRIEND_HEALINGFOREST(LocalMapLocation.ZONE_OCCIDENTALFOREST, "healingforest"),
	FRIEND_ICEFLOEBEACH(LocalMapLocation.ZONE_SEASIDE, "icefloebeach"),
	FRIEND_JUNGLE(LocalMapLocation.ZONE_JUNGLE, "jungle"),
	FRIEND_LEGENDARYISLAND(LocalMapLocation.ZONE_NORTHENISLAND, "legendaryisland"),
	FRIEND_MAGNETICQUARRY(LocalMapLocation.ZONE_PLATEAU, "magneticquarry"),
	FRIEND_MISTRISEFOREST(LocalMapLocation.ZONE_ORIENTALFOREST, "mistriseforest"),
	FRIEND_MTCLEFT(LocalMapLocation.ZONE_MOUNTAINS, "mtcleft"),
	FRIEND_MTDEEPGREEN(LocalMapLocation.ZONE_MOUNTAINS, "mtdeepgreen"),
	FRIEND_MTDISCIPLINE(LocalMapLocation.ZONE_MOUNTAINS, "mtdiscipline"),
	FRIEND_MTMOONVIEW(LocalMapLocation.ZONE_MOUNTAINS, "mtmoonview"),
	FRIEND_MUSHROOMFOREST(LocalMapLocation.ZONE_OCCIDENTALFOREST, "mushroomforest"),
	FRIEND_MYSTICLAKE(LocalMapLocation.ZONE_NORTHENLAKE, "mysticlake"),
	FRIEND_OVERGROWNFOREST(LocalMapLocation.ZONE_ORIENTALFOREST, "overgrownforest"),
	FRIEND_PEANUTSWAMP(LocalMapLocation.ZONE_SWAMP, "peanutswamp"),
	FRIEND_POISONSWAMP(LocalMapLocation.ZONE_SWAMP, "poisonswamp"),
	FRIEND_POWERPLANT(LocalMapLocation.ZONE_REMAINS, "powerplant"),
	FRIEND_RAINBOWPEAK(LocalMapLocation.ZONE_MOUNTAINS, "rainbowpeak"),
	FRIEND_RAVAGEDFIELD(LocalMapLocation.ZONE_PLATEAU, "ravagedfield"),
	FRIEND_RUBADUBRIVER(LocalMapLocation.ZONE_RIVER, "rubadubriver"),
	FRIEND_SACREDFIELD(LocalMapLocation.ZONE_PLAINS, "sacredfield"),
	FRIEND_SAFARI(LocalMapLocation.ZONE_SOUTHERNPLAINS, "safari"),
	FRIEND_SCORCHEDPLAINS(LocalMapLocation.ZONE_VOLCANO, "scorchedplains"),
	FRIEND_SEAFLOORCAVE(LocalMapLocation.ZONE_SOUTHERNSEA, "seafloorcave"),
	FRIEND_SECRETIVEFOREST(LocalMapLocation.ZONE_OCCIDENTALFOREST, "secretiveforest"),
	FRIEND_SERENESEA(LocalMapLocation.ZONE_NORTHENSEA, "serenesea"),
	FRIEND_SHALLOWBEACH(LocalMapLocation.ZONE_SEASIDE, "shallowbeach"),
	FRIEND_SKYBLUEPLAINS(LocalMapLocation.ZONE_PLAINS, "skyblueplains"),
	FRIEND_SOUTHERNISLAND(LocalMapLocation.ZONE_SOUTHERNISLAND, "southernisland"),
	FRIEND_STRATOSLOOKOUT(LocalMapLocation.ZONE_SKY, "stratoslookout"),
	FRIEND_TADPOLEPOND(LocalMapLocation.ZONE_EASTERNLAKESIDE, "tadpolepond"),
	FRIEND_THUNDERMEADOW(LocalMapLocation.ZONE_PLATEAU, "thundermeadow"),
	FRIEND_TRANSFORMFOREST(LocalMapLocation.ZONE_OCCIDENTALFOREST, "transformforest"),
	FRIEND_TREASURESEA(LocalMapLocation.ZONE_SOUTHERNSEA, "treasuresea"),
	FRIEND_TURTLESHELLPOND(LocalMapLocation.ZONE_POND, "turtleshellpond"),
	FRIEND_VOLCANICPIT(LocalMapLocation.ZONE_VOLCANO, "volcanicpit"),
	FRIEND_WATERFALLLAKE(LocalMapLocation.ZONE_EASTERNLAKESIDE, "waterfalllake"),
	FRIEND_WILDPLAINS(LocalMapLocation.ZONE_SOUTHERNPLAINS, "wildplains");

	public String id;
	public LocalMapLocation maplocation;

	private FreezoneInfo(LocalMapLocation maplocation, String id)
	{
		this.id = id;
		this.maplocation = maplocation;
	}

	/** Creates a new instance of the desired freezone map. */
	public static FreezoneMap loadMap(FreezoneInfo freezone)
	{
		// FIXME : switchcase here.
		switch (freezone)
		{
			case BASE:
				return new BaseFreezone();

			case SQUARE:
				return new PokemonSquareFreezone();

			case DOJO:
				return new DojoFreezone();

			case POND:
				return new PondFreezone();

			case OFFICE:
				return new OfficeFreezone();

			case STARTFOREST:
				return new ForestFreezone();

			case LUMINOUSCAVE:
				return new LumiousCaveFreezone();

			default:
				return null;
		}
	}

	public static FreezoneInfo find(String id)
	{
		for (FreezoneInfo f : values())
			if (f.id.equals(id)) return f;
		return null;
	}

	public FreezoneMap getMap()
	{
		return loadMap(this);
	}

	public Message getName()
	{
		return new Message("zone." + this.id);
	}
}
