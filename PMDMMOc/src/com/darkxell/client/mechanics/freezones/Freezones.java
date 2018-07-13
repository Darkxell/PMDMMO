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
import com.darkxell.client.mechanics.freezones.zones.friend.MtMoonviewFreezone;
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

			case FRIEND_MTMOONVIEW:
				d = NORTH;
				map = new MtMoonviewFreezone();
				break;

			default:
				d = null;
				map = null;
		}
		if (d != null) Persistance.currentplayer.renderer().sprite().setFacingDirection(d);
		return map;
	}

}
