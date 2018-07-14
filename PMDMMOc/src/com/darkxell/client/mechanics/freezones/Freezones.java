package com.darkxell.client.mechanics.freezones;

import static com.darkxell.common.util.Direction.EAST;
import static com.darkxell.common.util.Direction.NORTH;
import static com.darkxell.common.util.Direction.SOUTH;
import static com.darkxell.common.util.Direction.WEST;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;
import com.darkxell.client.mechanics.freezones.zones.DojoFreezone;
import com.darkxell.client.mechanics.freezones.zones.ForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.LumiousCaveFreezone;
import com.darkxell.client.mechanics.freezones.zones.OfficeFreezone;
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
import com.darkxell.common.util.Direction;
import com.darkxell.common.zones.FreezoneInfo;

public class Freezones
{

	/** Creates a new instance of the desired freezone map. */
	public static FreezoneMap loadMap(FreezoneInfo freezone)
	{
		FreezoneMap map;
		Direction d;
		switch (freezone)
		{
			case BASE:
				d = SOUTH;
				map = new BaseFreezone();
				break;

			case SQUARE:
				d = EAST;
				map = new PokemonSquareFreezone();
				break;

			case DOJO:
				d = SOUTH;
				map = new DojoFreezone();
				break;

			case POND:
				d = NORTH;
				map = new PondFreezone();
				break;

			case OFFICE:
				d = EAST;
				map = new OfficeFreezone();
				break;

			case STARTFOREST:
				d = WEST;
				map = new ForestFreezone();
				break;

			case LUMINOUSCAVE:
				d = NORTH;
				map = new LumiousCaveFreezone();
				break;

			case FRIEND_AGEDCHAMBER1:
				d = NORTH;
				map = new AgedChamber1Freezone();
				break;

			case FRIEND_AGEDCHAMBER2:
				d = NORTH;
				map = new AgedChamber2Freezone();
				break;

			case FRIEND_ANCIENTRELIC:
				d = NORTH;
				map = new AncientRelicFreezone();
				break;

			case FRIEND_BEAUPLAINS:
				d = NORTH;
				map = new BeauPlainsFreezone();
				break;

			case FRIEND_BOULDERCAVE:
				d = NORTH;
				map = new BoulderCaveFreezone();
				break;

			case FRIEND_BOUNTIFULSEA:
				d = NORTH;
				map = new BountifulSeaFreezone();
				break;

			case FRIEND_CRATER:
				d = NORTH;
				map = new CraterFreezone();
				break;

			case FRIEND_CRYPTICCAVE:
				d = NORTH;
				map = new CrypticCaveFreezone();
				break;

			case FRIEND_DARKNESSRIDGE:
				d = NORTH;
				map = new DarknessRidgeFreezone();
				break;

			case FRIEND_DECREPITLAB:
				d = NORTH;
				map = new DecrepitLabFreezone();
				break;

			case FRIEND_DEEPSEACURRENT:
				d = NORTH;
				map = new DeepseaCurrentFreezone();
				break;

			case FRIEND_DEEPSEAFLOOR:
				d = NORTH;
				map = new DeepseaFloorFreezone();
				break;

			case FRIEND_DRAGONCAVE:
				d = NORTH;
				map = new DragonCaveFreezone();
				break;

			case FRIEND_ECHOCAVE:
				d = NORTH;
				map = new EchoCaveFreezone();
				break;

			case FRIEND_ENCLOSEDISLAND:
				d = NORTH;
				map = new EnclosedIslandFreezone();
				break;

			case FRIEND_ENERGETICFOREST:
				d = NORTH;
				map = new EnergeticForestFreezone();
				break;

			case FRIEND_FINALISLAND:
				d = NORTH;
				map = new FinalIslandFreezone();
				break;

			case FRIEND_FLYAWAYFOREST:
				d = NORTH;
				map = new FlyawayForestFreezone();
				break;

			case FRIEND_FRIGIDCAVERN:
				d = NORTH;
				map = new FrigidCavernFreezone();
				break;

			case FRIEND_FURNACEDESERT:
				d = NORTH;
				map = new FurnaceDesertFreezone();
				break;

			case FRIEND_HEALINGFOREST:
				d = NORTH;
				map = new HealingForestFreezone();
				break;

			case FRIEND_ICEFLOEBEACH:
				d = NORTH;
				map = new IceFloeBeachFreezone();
				break;

			case FRIEND_JUNGLE:
				d = NORTH;
				map = new JungleFreezone();
				break;

			case FRIEND_LEGENDARYISLAND:
				d = NORTH;
				map = new LegendaryIslandFreezone();
				break;

			case FRIEND_MAGNETICQUARRY:
				d = NORTH;
				map = new MagneticQuarryFreezone();
				break;

			case FRIEND_MISTRISEFOREST:
				d = NORTH;
				map = new MistRiseForestFreezone();
				break;

			case FRIEND_MTCLEFT:
				d = NORTH;
				map = new MtCleftFreezone();
				break;

			case FRIEND_MTDEEPGREEN:
				d = NORTH;
				map = new MtDeepgreenFreezone();
				break;

			case FRIEND_MTDISCIPLINE:
				d = NORTH;
				map = new MtDisciplineFreezone();
				break;

			case FRIEND_MTMOONVIEW:
				d = NORTH;
				map = new MtMoonviewFreezone();
				break;

			case FRIEND_MUSHROOMFOREST:
				d = NORTH;
				map = new MushroomForestFreezone();
				break;

			case FRIEND_MYSTICLAKE:
				d = NORTH;
				map = new MysticLakeFreezone();
				break;

			case FRIEND_OVERGROWNFOREST:
				d = NORTH;
				map = new OvergrownForestFreezone();
				break;

			case FRIEND_PEANUTSWAMP:
				d = NORTH;
				map = new PeanutSwampFreezone();
				break;

			case FRIEND_POISONSWAMP:
				d = NORTH;
				map = new PoisonSwampFreezone();
				break;

			case FRIEND_POWERPLANT:
				d = NORTH;
				map = new PowerPlantFreezone();
				break;

			case FRIEND_RAINBOWPEAK:
				d = NORTH;
				map = new RainbowPeakFreezone();
				break;

			case FRIEND_RAVAGEDFIELD:
				d = NORTH;
				map = new RavagedFieldFreezone();
				break;

			case FRIEND_RUBADUBRIVER:
				d = NORTH;
				map = new RubadubRiverFreezone();
				break;

			case FRIEND_SACREDFIELD:
				d = NORTH;
				map = new SacredFieldFreezone();
				break;

			case FRIEND_SAFARI:
				d = NORTH;
				map = new SafariFreezone();
				break;

			case FRIEND_SCORCHEDPLAINS:
				d = NORTH;
				map = new ScorchedPlainsFreezone();
				break;

			case FRIEND_SEAFLOORCAVE:
				d = NORTH;
				map = new SeafloorCaveFreezone();
				break;

			case FRIEND_SECRETIVEFOREST:
				d = NORTH;
				map = new SecretiveForestFreezone();
				break;

			case FRIEND_SERENESEA:
				d = NORTH;
				map = new SereneSeaFreezone();
				break;

			case FRIEND_SHALLOWBEACH:
				d = NORTH;
				map = new ShallowBeachFreezone();
				break;

			case FRIEND_SKYBLUEPLAINS:
				d = NORTH;
				map = new SkyBluePlainsFreezone();
				break;

			case FRIEND_SOUTHERNISLAND:
				d = NORTH;
				map = new SouthernIslandFreezone();
				break;

			case FRIEND_STRATOSLOOKOUT:
				d = NORTH;
				map = new StratosLookoutFreezone();
				break;

			case FRIEND_TADPOLEPOND:
				d = NORTH;
				map = new TadpolePondFreezone();
				break;

			case FRIEND_THUNDERMEADOW:
				d = NORTH;
				map = new ThunderMeadowFreezone();
				break;

			case FRIEND_TRANSFORMFOREST:
				d = NORTH;
				map = new TransformForestFreezone();
				break;

			case FRIEND_TREASURESEA:
				d = NORTH;
				map = new TreasureSeaFreezone();
				break;

			case FRIEND_TURTLESHELLPOND:
				d = NORTH;
				map = new TurtleshellPondFreezone();
				break;

			case FRIEND_VOLCANICPIT:
				d = NORTH;
				map = new VolcanicPitFreezone();
				break;

			case FRIEND_WATERFALLLAKE:
				d = NORTH;
				map = new WaterfallLakeFreezone();
				break;

			case FRIEND_WILDPLAINS:
				d = NORTH;
				map = new WildPlainsFreezone();
				break;

			default:
				d = null;
				map = null;
		}
		if (d != null) Persistance.currentplayer.renderer().sprite().setFacingDirection(d);
		return map;
	}

}
