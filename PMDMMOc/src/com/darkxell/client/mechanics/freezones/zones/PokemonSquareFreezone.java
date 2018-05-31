package com.darkxell.client.mechanics.freezones.zones;

import java.util.ArrayList;
import java.util.Random;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.WarpZone;
import com.darkxell.client.mechanics.freezones.entities.AnimatedFlowerEntity;
import com.darkxell.client.mechanics.freezones.entities.PokemonFreezoneEntity;
import com.darkxell.client.mechanics.freezones.entities.SignSoulEntity;
import com.darkxell.client.mechanics.freezones.entities.WatersparklesEntity;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.language.Message;

public class PokemonSquareFreezone extends FreezoneMap {

	public PokemonSquareFreezone() {
		super("/freezones/square.xml");
		this.freezonebgm = "town.mp3";
		this.warpzones.add(new WarpZone(63, 40, new DoubleRectangle(0, 38, 2, 5)) {
			@Override
			public FreezoneMap getDestination() {
				return new BaseFreezone();
			}
		});
		this.warpzones.add(new WarpZone(-1, -1, new DoubleRectangle(63, 87, 9, 2)) {
			@Override
			public FreezoneMap getDestination() {
				return new DojoFreezone();
			}
		});
		this.warpzones.add(new WarpZone(-1, -1, new DoubleRectangle(61, 0, 8, 2)) {
			@Override
			public FreezoneMap getDestination() {
				return new PondFreezone();
			}
		});
		this.warpzones.add(new WarpZone(-1, -1, new DoubleRectangle(118, 38, 2, 5)) {
			@Override
			public FreezoneMap getDestination() {
				return new OfficeFreezone();
			}
		});

		this.addEntity(new AnimatedFlowerEntity(10, 30, false));
		this.addEntity(new AnimatedFlowerEntity(10, 37, false));
		this.addEntity(new AnimatedFlowerEntity(33, 45, false));
		this.addEntity(new AnimatedFlowerEntity(41, 74, false));
		this.addEntity(new AnimatedFlowerEntity(55, 73, false));
		this.addEntity(new AnimatedFlowerEntity(89, 61, false));
		this.addEntity(new AnimatedFlowerEntity(107, 55, false));
		this.addEntity(new AnimatedFlowerEntity(107, 48, false));
		this.addEntity(new AnimatedFlowerEntity(57, 31, false));
		this.addEntity(new AnimatedFlowerEntity(70, 23, false));
		this.addEntity(new AnimatedFlowerEntity(76, 22, false));
		this.addEntity(new AnimatedFlowerEntity(60, 14, false));
		this.addEntity(new AnimatedFlowerEntity(60, 10, false));

		this.addEntity(new AnimatedFlowerEntity(6, 31, true));
		this.addEntity(new AnimatedFlowerEntity(14, 31, true));
		this.addEntity(new AnimatedFlowerEntity(11, 34, true));
		this.addEntity(new AnimatedFlowerEntity(29, 45, true));
		this.addEntity(new AnimatedFlowerEntity(45, 73, true));
		this.addEntity(new AnimatedFlowerEntity(43, 77, true));
		this.addEntity(new AnimatedFlowerEntity(56, 70, true));
		this.addEntity(new AnimatedFlowerEntity(60, 72, true));
		this.addEntity(new AnimatedFlowerEntity(93, 66, true));
		this.addEntity(new AnimatedFlowerEntity(108, 58, true));
		this.addEntity(new AnimatedFlowerEntity(93, 30, true));
		this.addEntity(new AnimatedFlowerEntity(70, 26, true));
		this.addEntity(new AnimatedFlowerEntity(70, 18, true));
		this.addEntity(new AnimatedFlowerEntity(69, 13, true));
		this.addEntity(new AnimatedFlowerEntity(60, 18, true));
		this.addEntity(new AnimatedFlowerEntity(57, 21, true));
		this.addEntity(new AnimatedFlowerEntity(49, 37, true));

		this.addEntity(new SignSoulEntity(13.5, 37, new Message("sign.pokemonsquare.west")));
		this.addEntity(new SignSoulEntity(68.5, 10, new Message("sign.pokemonsquare.north")));
		this.addEntity(new SignSoulEntity(114.5, 36, new Message("sign.pokemonsquare.east")));

		this.addEntity(new WatersparklesEntity(6.5, 8, WatersparklesEntity.TYPE_SIDE));
		this.addEntity(new WatersparklesEntity(17.5, 25, WatersparklesEntity.TYPE_SIDE));
		this.addEntity(new WatersparklesEntity(17.5, 35, WatersparklesEntity.TYPE_BOT));
		this.addEntity(new WatersparklesEntity(17.5, 46, WatersparklesEntity.TYPE_TOP));
		this.addEntity(new WatersparklesEntity(17.5, 55, WatersparklesEntity.TYPE_SIDE));
		this.addEntity(new WatersparklesEntity(17.5, 62, WatersparklesEntity.TYPE_SIDE));
		this.addEntity(new WatersparklesEntity(25, 69, WatersparklesEntity.TYPE_LONG));
		this.addEntity(new WatersparklesEntity(32.5, 70, WatersparklesEntity.TYPE_SIDE));
		this.addEntity(new WatersparklesEntity(32.5, 79, WatersparklesEntity.TYPE_SIDE));

		// Add the shopkeepers
		this.addEntity(new PokemonFreezoneEntity(36, 30, 352));
		this.addEntity(new PokemonFreezoneEntity(39, 30, -352));
		this.addEntity(new PokemonFreezoneEntity(82, 33, 53));
		this.addEntity(new PokemonFreezoneEntity(97, 33, 40));
		this.addEntity(new PokemonFreezoneEntity(41, 61, 115));
		this.addEntity(new PokemonFreezoneEntity(82, 60, 316));
		

		// Add the interactible pokemons
		ArrayList<DialogScreen> s = new ArrayList<>(2);
		s.add(new DialogScreen(PokemonRegistry.find(6).generate(new Random(), 0),
				new Message("dialog.place.charizard.1")));
		s.add(new DialogScreen(PokemonRegistry.find(6).generate(new Random(), 0),
				new Message("dialog.place.charizard.2")));
		this.addEntity(new PokemonFreezoneEntity(57, 33, 6, Direction.EAST, s));
		s = new ArrayList<>(1);
		s.add(new DialogScreen(PokemonRegistry.find(248).generate(new Random(), 0),
				new Message("dialog.place.tyranitar")));
		this.addEntity(new PokemonFreezoneEntity(61, 33, 248, Direction.WEST, s));
		s = new ArrayList<>(2);
		s.add(new DialogScreen(PokemonRegistry.find(275).generate(new Random(), 0),
				new Message("dialog.place.shiftry.1")));
		s.add(new DialogScreen(PokemonRegistry.find(275).generate(new Random(), 0),
				new Message("dialog.place.shiftry.2")));
		this.addEntity(new PokemonFreezoneEntity(69, 35, 275, Direction.SOUTHWEST, s));

	}

	@Override
	public LOCALMAPLOCATION getMapLocation() {
		return LOCALMAPLOCATION.SQUARE;
	}

	@Override
	public int defaultX() {
		return 4;
	}

	@Override
	public int defaultY() {
		return 40;
	}

}
