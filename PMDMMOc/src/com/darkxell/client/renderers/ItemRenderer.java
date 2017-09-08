package com.darkxell.client.renderers;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;

public class ItemRenderer
{

	/** Creates an Animation for using an Item and returns it.
	 * 
	 * @param item- The Item being used.
	 * @param user - The Pokémon using the Item.
	 * @param floor - The current Floor.
	 * @param listener - The listener to call at the end of the Animation. */
	public static AbstractAnimation createItemAnimation(Item item, DungeonPokemon user, Floor floor, AnimationEndListener listener)
	{
		return new AbstractAnimation(60, listener);
	}

}
