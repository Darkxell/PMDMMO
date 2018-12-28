package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.mechanics.freezones.cutscenemaps.MtSteelTopFreezone;
import com.darkxell.client.mechanics.freezones.cutscenemaps.MtsteelEntranceFreezone;
import com.darkxell.client.mechanics.freezones.cutscenemaps.ThunderwaveClearFreezone;
import com.darkxell.client.mechanics.freezones.cutscenemaps.ThunderwaveEntranceFreezone;
import com.darkxell.client.mechanics.freezones.cutscenemaps.TinywoodsClearFreezone;
import com.darkxell.client.mechanics.freezones.zones.*;
import com.darkxell.client.mechanics.freezones.zones.friend.*;
import com.darkxell.common.zones.FreezoneInfo;

public class Freezones {

	/** Creates a new instance of the desired freezone map. */
	public static FreezoneMap loadMap(FreezoneInfo freezone) {
		FreezoneMap map;
		switch (freezone) {
		case BASEINSIDE:
			map = new BaseInsideFreezone();
			break;

		case DREAM:
			map = new DreamFreezone();
			break;
			
		case SQUARE:
			map = new PokemonSquareFreezone();
			break;

		case DOJO:
			map = new DojoFreezone();
			break;

		case POND:
			map = new PondFreezone();
			break;

		case OFFICE:
			map = new OfficeFreezone();
			break;

		case OFFICEINSIDE:
			map = new OfficeinsideFreezone();
			break;

		case STARTFOREST:
			map = new ForestFreezone();
			break;

		case LUMINOUSCAVE:
			map = new LumiousCaveFreezone();
			break;
			
		case MTSTEEL_ENTRANCE:
			map = new MtsteelEntranceFreezone();
			break;
			
		case THUNDERWAVE_ENTRANCE:
			map = new ThunderwaveEntranceFreezone();
			break;
			
		case THUNDERWAVE_CLEAR:
			map = new ThunderwaveClearFreezone();
			break;
			
		case TINYWOODS_CLEAR:
			map = new TinywoodsClearFreezone();
			break;
			
		case DUNGEON_MT_STEEL_TOP:
			map = new MtSteelTopFreezone();
			break;

		case FRIEND_AGEDCHAMBER1:
			map = new AgedChamber1Freezone();
			break;

		case FRIEND_AGEDCHAMBER2:
			map = new AgedChamber2Freezone();
			break;

		case FRIEND_ANCIENTRELIC:
			map = new AncientRelicFreezone();
			break;

		case FRIEND_BEAUPLAINS:
			map = new BeauPlainsFreezone();
			break;

		case FRIEND_BOULDERCAVE:
			map = new BoulderCaveFreezone();
			break;

		case FRIEND_BOUNTIFULSEA:
			map = new BountifulSeaFreezone();
			break;

		case FRIEND_CRATER:
			map = new CraterFreezone();
			break;

		case FRIEND_CRYPTICCAVE:
			map = new CrypticCaveFreezone();
			break;

		case FRIEND_DARKNESSRIDGE:
			map = new DarknessRidgeFreezone();
			break;

		case FRIEND_DECREPITLAB:
			map = new DecrepitLabFreezone();
			break;

		case FRIEND_DEEPSEACURRENT:
			map = new DeepseaCurrentFreezone();
			break;

		case FRIEND_DEEPSEAFLOOR:
			map = new DeepseaFloorFreezone();
			break;

		case FRIEND_DRAGONCAVE:
			map = new DragonCaveFreezone();
			break;

		case FRIEND_ECHOCAVE:
			map = new EchoCaveFreezone();
			break;

		case FRIEND_ENCLOSEDISLAND:
			map = new EnclosedIslandFreezone();
			break;

		case FRIEND_ENERGETICFOREST:
			map = new EnergeticForestFreezone();
			break;

		case FRIEND_FINALISLAND:
			map = new FinalIslandFreezone();
			break;

		case FRIEND_FLYAWAYFOREST:
			map = new FlyawayForestFreezone();
			break;

		case FRIEND_FRIGIDCAVERN:
			map = new FrigidCavernFreezone();
			break;

		case FRIEND_FURNACEDESERT:
			map = new FurnaceDesertFreezone();
			break;

		case FRIEND_HEALINGFOREST:
			map = new HealingForestFreezone();
			break;

		case FRIEND_ICEFLOEBEACH:
			map = new IceFloeBeachFreezone();
			break;

		case FRIEND_JUNGLE:
			map = new JungleFreezone();
			break;

		case FRIEND_LEGENDARYISLAND:
			map = new LegendaryIslandFreezone();
			break;

		case FRIEND_MAGNETICQUARRY:
			map = new MagneticQuarryFreezone();
			break;

		case FRIEND_MISTRISEFOREST:
			map = new MistRiseForestFreezone();
			break;

		case FRIEND_MTCLEFT:
			map = new MtCleftFreezone();
			break;

		case FRIEND_MTDEEPGREEN:
			map = new MtDeepgreenFreezone();
			break;

		case FRIEND_MTDISCIPLINE:
			map = new MtDisciplineFreezone();
			break;

		case FRIEND_MTMOONVIEW:
			map = new MtMoonviewFreezone();
			break;

		case FRIEND_MUSHROOMFOREST:
			map = new MushroomForestFreezone();
			break;

		case FRIEND_MYSTICLAKE:
			map = new MysticLakeFreezone();
			break;

		case FRIEND_OVERGROWNFOREST:
			map = new OvergrownForestFreezone();
			break;

		case FRIEND_PEANUTSWAMP:
			map = new PeanutSwampFreezone();
			break;

		case FRIEND_POISONSWAMP:
			map = new PoisonSwampFreezone();
			break;

		case FRIEND_POWERPLANT:
			map = new PowerPlantFreezone();
			break;

		case FRIEND_RAINBOWPEAK:
			map = new RainbowPeakFreezone();
			break;

		case FRIEND_RAVAGEDFIELD:
			map = new RavagedFieldFreezone();
			break;

		case FRIEND_RUBADUBRIVER:
			map = new RubadubRiverFreezone();
			break;

		case FRIEND_SACREDFIELD:
			map = new SacredFieldFreezone();
			break;

		case FRIEND_SAFARI:
			map = new SafariFreezone();
			break;

		case FRIEND_SCORCHEDPLAINS:
			map = new ScorchedPlainsFreezone();
			break;

		case FRIEND_SEAFLOORCAVE:
			map = new SeafloorCaveFreezone();
			break;

		case FRIEND_SECRETIVEFOREST:
			map = new SecretiveForestFreezone();
			break;

		case FRIEND_SERENESEA:
			map = new SereneSeaFreezone();
			break;

		case FRIEND_SHALLOWBEACH:
			map = new ShallowBeachFreezone();
			break;

		case FRIEND_SKYBLUEPLAINS:
			map = new SkyBluePlainsFreezone();
			break;

		case FRIEND_SOUTHERNISLAND:
			map = new SouthernIslandFreezone();
			break;

		case FRIEND_STRATOSLOOKOUT:
			map = new StratosLookoutFreezone();
			break;

		case FRIEND_TADPOLEPOND:
			map = new TadpolePondFreezone();
			break;

		case FRIEND_THUNDERMEADOW:
			map = new ThunderMeadowFreezone();
			break;

		case FRIEND_TRANSFORMFOREST:
			map = new TransformForestFreezone();
			break;

		case FRIEND_TREASURESEA:
			map = new TreasureSeaFreezone();
			break;

		case FRIEND_TURTLESHELLPOND:
			map = new TurtleshellPondFreezone();
			break;

		case FRIEND_VOLCANICPIT:
			map = new VolcanicPitFreezone();
			break;

		case FRIEND_WATERFALLLAKE:
			map = new WaterfallLakeFreezone();
			break;

		case FRIEND_WILDPLAINS:
			map = new WildPlainsFreezone();
			break;

		case BASE:
			map = new BaseFreezone();
			break;
			
		default:
			map = new BaseInsideFreezone();
		}
		return map;
	}

}
