package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.PokemonFreezoneEntity;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.resources.images.PokemonSpritesets;
import com.darkxell.common.util.DoubleRectangle;

public class PokemonSquareFreezone extends FreezoneMap {

	public PokemonSquareFreezone() {
		super("resources\\freezones\\square.xml");
		this.freezonebgm = "14 Pokemon Square.mp3";
		this.entities.add(new PokemonFreezoneEntity(71, 34, new PokemonSprite(PokemonSpritesets.getSpriteset(1))));
		this.warpzones.add(new WarpZone(63, 40, new DoubleRectangle(0, 38, 2, 5)) {
			@Override
			public FreezoneMap getDestination() {
				return new BaseFreezone();
			}
		});
	}

}
