package com.darkxell.client.resources.images.pokemon;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.util.Direction;

import javafx.util.Pair;

public class PokemonSpriteset extends RegularSpriteSet
{

	public final PokemonSpritesetData data;

	protected PokemonSpriteset(String path, PokemonSpritesetData data)
	{
		super(path, data.spriteWidth, data.spriteHeight, -1, -1);
		this.data = data;
	}

	public PokemonSpriteSequence getSequence(PokemonSpriteState state, Direction direction)
	{
		return this.data.sequences.get(this.data.states.get(new Pair<>(state, direction)));
	}

	public PokemonSpriteFrame getFrame(PokemonSpriteState state, Direction direction, int tick)
	{
		return this.getSequence(state, direction).getFrame(tick);
	}

}
