package com.darkxell.client.mechanics.freezones.zones;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.mechanics.freezones.entities.PokemonFreezoneEntity;
import com.darkxell.client.mechanics.freezones.entities.SignSoulEntity;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.language.Message;

public class PokemonSquareFreezone extends FreezoneMap {

	public PokemonSquareFreezone() {
		super("resources\\freezones\\square.xml");
		this.freezonebgm = "14 Pokemon Square.mp3";
		this.warpzones.add(new WarpZone(63, 40, new DoubleRectangle(0, 38, 2, 5)) {
			@Override
			public FreezoneMap getDestination() {
				return new BaseFreezone();
			}
		});
		this.warpzones.add(new WarpZone(42, 4, new DoubleRectangle(63, 87, 9, 2)) {
			@Override
			public FreezoneMap getDestination() {
				return new DojoFreezone();
			}
		});
		this.warpzones.add(new WarpZone(29, 60, new DoubleRectangle(61, 0, 8, 2)) {
			@Override
			public FreezoneMap getDestination() {
				return new PondFreezone();
			}
		});
		this.warpzones.add(new WarpZone(4, 30, new DoubleRectangle(118, 38, 2, 5)) {
			@Override
			public FreezoneMap getDestination() {
				return new OfficeFreezone();
			}
		});

		this.entities.add(new AnimatedFlowerEntity(10, 30, false));
		this.entities.add(new AnimatedFlowerEntity(10, 37, false));
		this.entities.add(new AnimatedFlowerEntity(33, 45, false));
		this.entities.add(new AnimatedFlowerEntity(41, 74, false));
		this.entities.add(new AnimatedFlowerEntity(55, 73, false));
		this.entities.add(new AnimatedFlowerEntity(89, 61, false));
		this.entities.add(new AnimatedFlowerEntity(107, 55, false));
		this.entities.add(new AnimatedFlowerEntity(107, 48, false));
		this.entities.add(new AnimatedFlowerEntity(57, 31, false));
		this.entities.add(new AnimatedFlowerEntity(70, 23, false));
		this.entities.add(new AnimatedFlowerEntity(76, 22, false));
		this.entities.add(new AnimatedFlowerEntity(60, 14, false));
		this.entities.add(new AnimatedFlowerEntity(60, 10, false));

		this.entities.add(new AnimatedFlowerEntity(6, 31, true));
		this.entities.add(new AnimatedFlowerEntity(14, 31, true));
		this.entities.add(new AnimatedFlowerEntity(11, 34, true));
		this.entities.add(new AnimatedFlowerEntity(29, 45, true));
		this.entities.add(new AnimatedFlowerEntity(45, 73, true));
		this.entities.add(new AnimatedFlowerEntity(43, 77, true));
		this.entities.add(new AnimatedFlowerEntity(56, 70, true));
		this.entities.add(new AnimatedFlowerEntity(60, 72, true));
		this.entities.add(new AnimatedFlowerEntity(93, 66, true));
		this.entities.add(new AnimatedFlowerEntity(108, 58, true));
		this.entities.add(new AnimatedFlowerEntity(93, 30, true));
		this.entities.add(new AnimatedFlowerEntity(70, 26, true));
		this.entities.add(new AnimatedFlowerEntity(70, 18, true));
		this.entities.add(new AnimatedFlowerEntity(69, 13, true));
		this.entities.add(new AnimatedFlowerEntity(60, 18, true));
		this.entities.add(new AnimatedFlowerEntity(57, 21, true));
		this.entities.add(new AnimatedFlowerEntity(49, 37, true));

		this.entities.add(new PokemonFreezoneEntity(71, 34, new PokemonSprite(PokemonSpritesets.getSpriteset(69))));

		this.entities.add(new SignSoulEntity(13.5, 37, new Message("sign.pokemonsquare.west")));
		this.entities.add(new SignSoulEntity(68.5, 10, new Message("sign.pokemonsquare.north")));
		this.entities.add(new SignSoulEntity(114.5, 36, new Message("sign.pokemonsquare.east")));

	}

	@Override
	public LOCALMAPLOCATION getMapLocation() {
		return LOCALMAPLOCATION.SQUARE;
	}

}
