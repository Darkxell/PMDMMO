package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;
import com.darkxell.client.mechanics.freezones.zones.DojoFreezone;
import com.darkxell.client.mechanics.freezones.zones.ForestFreezone;
import com.darkxell.client.mechanics.freezones.zones.LumiousCaveFreezone;
import com.darkxell.client.mechanics.freezones.zones.OfficeFreezone;
import com.darkxell.client.mechanics.freezones.zones.PokemonSquareFreezone;
import com.darkxell.client.mechanics.freezones.zones.PondFreezone;
import com.darkxell.client.mechanics.freezones.zones.friend.AgedChamber1Freezone;
import com.darkxell.common.zones.FreezoneInfo;

public class Freezones
{

	/** Creates a new instance of the desired freezone map. */
	public static FreezoneMap loadMap(FreezoneInfo freezone)
	{
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
				
			case FRIEND_AGEDCHAMBER1:
				return new AgedChamber1Freezone();

			default:
				return null;
		}
	}

}
