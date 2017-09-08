package com.darkxell.client.renderers;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.common.event.ItemUseEvent;

public class ItemRenderer
{

	/** Creates an Animation for using an Item and returns it.
	 * 
	 * @param listener - The listener to call at the end of the Animation. */
	public static AbstractAnimation createItemAnimation(ItemUseEvent event, AnimationEndListener listener)
	{
		return new AbstractAnimation(60, listener);
	}

}
