package com.darkxell.client.mechanics.animation;

public interface AnimationEndListener {

    /**
     * Called when the input Animation is over.
     * 
     * @param animation - The ending Animation.
     */
    public void onAnimationEnd(AbstractAnimation animation);

}
