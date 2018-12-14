package com.darkxell.common;

/**
 * Simple extension of {@link Comparable} to accommodate for an id. Primarily used for {@link Registry} classes.
 *
 * @param <T> They type of object this object can be compared to
 */
public interface Registrable<T> extends Comparable<T> {
    /**
     * Get registered ID for this class. Should be implemented as final, or else things may break silently.
     *
     * @return Registry ID.
     */
    int getID();
}
