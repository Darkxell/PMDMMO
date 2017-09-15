package com.darkxell.client.renderers;

import java.awt.Point;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.StaticAnimation;
import com.darkxell.client.resources.images.AnimationSpriteset;
import com.darkxell.client.resources.images.tilesets.FloorDungeonTileset;
import com.darkxell.common.event.item.ItemUseSelectionEvent;
import com.darkxell.common.item.ItemEscapeOrb;

public class ItemRenderer
{

	/** Creates an Animation for using an Item and returns it.
	 * 
	 * @param listener - The listener to call at the end of the Animation. */
	public static AbstractAnimation createItemAnimation(ItemUseSelectionEvent event, AnimationEndListener listener)
	{
		if (event.item instanceof ItemEscapeOrb) return new StaticAnimation(listener,
				AnimationSpriteset.getSpriteset("resources/items/escape_orb.png", 64, 120), new Point(event.user.tile.x * FloorDungeonTileset.TILE_SIZE - 64
						/ 4, event.user.tile.y * FloorDungeonTileset.TILE_SIZE - 90), 10);

		return new AbstractAnimation(60, listener);
	}

}
