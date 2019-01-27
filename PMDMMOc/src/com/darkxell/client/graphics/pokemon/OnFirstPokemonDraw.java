package com.darkxell.client.renderers.pokemon;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.resources.images.pokemon.PokemonSpriteFrame;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.pokemon.DungeonPokemon;

public class OnFirstPokemonDraw
{

	private static final HashSet<DungeonPokemon> seen = new HashSet<>();

	public static boolean isNotVisible(DungeonPokemon pokemon)
	{
		DungeonPokemonRenderer renderer = Persistence.dungeonState.pokemonRenderer.getRenderer(pokemon);
		PokemonSpriteFrame frame = renderer.sprite.getCurrentFrame();
		BufferedImage sprite = renderer.sprite.getCurrentSprite();
		Point camera = Persistence.dungeonState.camera();

		Rectangle screen = new Rectangle(camera.x, camera.y, PrincipalMainState.displayWidth, PrincipalMainState.displayHeight);
		Rectangle spriteDim = new Rectangle((int) (renderer.drawX() - sprite.getWidth() / 2 + frame.spriteX),
				(int) (renderer.drawY() - sprite.getHeight() / 2 + frame.spriteY), sprite.getWidth(), sprite.getHeight());

		return !screen.contains(spriteDim);
	}

	public static void newDungeon()
	{
		seen.clear();
	}

	public static void onFirstDraw(DungeonPokemon pokemon)
	{
		Persistence.soundmanager.playSound(SoundsHolder.getSfx("cry-" + pokemon.species().parent().id));
		if (pokemon.usedPokemon.isShiny())
		{
			PokemonAnimation a = Animations.getCustomAnimation(pokemon, 7, null);
			if (a != null) a.start();
		}
	}

	public static void reset()
	{
		seen.removeIf(new Predicate<DungeonPokemon>() {
			@Override
			public boolean test(DungeonPokemon t)
			{
				return !Persistence.player.isAlly(t);
			}
		});
	}

	public static void update()
	{
		ArrayList<DungeonPokemon> pokemons = Persistence.floor.listPokemon();
		pokemons.removeAll(seen);
		pokemons.removeIf(OnFirstPokemonDraw::isNotVisible);

		seen.addAll(pokemons);
		pokemons.forEach(OnFirstPokemonDraw::onFirstDraw);
	}

}
