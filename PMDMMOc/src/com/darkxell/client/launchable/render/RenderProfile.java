package com.darkxell.client.launchable.render;

/**
 * Marker for different kinds of render and update loops.
 */
public enum RenderProfile {/**
 * Synchronized render loop. Should be default.
 */
PROFILE_SYNCHRONIZED,
    /**
     * Render and update are in separate threads. Do not use at the moment - gets really choppy!
     */
    PROFILE_UNCAPPED,
    /**
     * Default value.
     */
    PROFILE_UNDEFINED}
