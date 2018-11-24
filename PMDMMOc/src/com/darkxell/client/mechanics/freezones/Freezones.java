package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.mechanics.freezones.cutscenemaps.MtSteelTopFreezone;
import com.darkxell.client.mechanics.freezones.cutscenemaps.ThunderwaveEntranceFreezone;
import com.darkxell.client.mechanics.freezones.cutscenemaps.TinywoodsClearFreezone;
import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;
import com.darkxell.client.mechanics.freezones.zones.BaseInsideFreezone;
import com.darkxell.client.mechanics.freezones.zones.DojoFreezone;
import com.darkxell.client.mechanics.freezones.zones.ForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.LumiousCaveFreezone;
import com.darkxell.client.mechanics.freezones.zones.OfficeFreezone;
import com.darkxell.client.mechanics.freezones.zones.OfficeinsideFreezone;
import com.darkxell.client.mechanics.freezones.zones.PokemonSquareFreezone;
import com.darkxell.client.mechanics.freezones.zones.PondFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.AgedChamber1Freezone;
import com.darkxell.client.mechanics.freezones.zones.friend.AgedChamber2Freezone;
import com.darkxell.client.mechanics.freezones.zones.friend.AncientRelicFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.BeauPlainsFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.BoulderCaveFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.BountifulSeaFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.CraterFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.CrypticCaveFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.DarknessRidgeFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.DecrepitLabFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.DeepseaCurrentFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.DeepseaFloorFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.DragonCaveFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.EchoCaveFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.EnclosedIslandFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.EnergeticForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.FinalIslandFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.FlyawayForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.FrigidCavernFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.FurnaceDesertFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.HealingForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.IceFloeBeachFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.JungleFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.LegendaryIslandFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.MagneticQuarryFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.MistRiseForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.MtCleftFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.MtDeepgreenFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.MtDisciplineFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.MtMoonviewFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.MushroomForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.MysticLakeFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.OvergrownForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.PeanutSwampFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.PoisonSwampFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.PowerPlantFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.RainbowPeakFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.RavagedFieldFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.RubadubRiverFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.SacredFieldFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.SafariFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.ScorchedPlainsFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.SeafloorCaveFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.SecretiveForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.SereneSeaFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.ShallowBeachFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.SkyBluePlainsFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.SouthernIslandFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.StratosLookoutFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.TadpolePondFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.ThunderMeadowFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.TransformForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.TreasureSeaFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.TurtleshellPondFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.VolcanicPitFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.WaterfallLakeFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.WildPlainsFreezone;
import com.darkxell.common.zones.FreezoneInfo;

public class Freezones {

	/** Creates a new instance of the desired freezone map. */
	public static FreezoneMap loadMap(FreezoneInfo freezone) {
		FreezoneMap map;
		switch (freezone) {
		case BASEINSIDE:
			map = new BaseInsideFreezone();
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
			
		case THUNDERWAVE_ENTRANCE:
			map = new ThunderwaveEntranceFreezone();
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
		default:
			map = new BaseFreezone();
		}
		return map;
	}

}
