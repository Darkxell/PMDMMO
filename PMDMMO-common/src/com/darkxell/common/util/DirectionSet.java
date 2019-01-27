package com.darkxell.common.util;

import static com.darkxell.common.util.Direction.*;

public class DirectionSet {
    private short value = 0;

    public DirectionSet(Direction... directions) {
        for (Direction d : directions) {
            this.add(d);
        }
    }

    /**
     * Adds the input Direction to this set.
     */
    public void add(Direction direction) {
        this.value |= 1 << direction.value;
    }

    public void clear() {
        this.value = 0;
    }

    /**
     * @return Does this set contain the input direction?
     */
    public boolean contains(Direction direction) {
        return (this.value & (1 << direction.value)) != 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DirectionSet && ((DirectionSet) obj).value == this.value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    /**
     * Removes the input Direction from this set.
     */
    public void remove(Direction direction) {
        this.value &= ~(1 << direction.value);
    }

    /**
     * Removes corners whose neighbors are not both set.
     */
    public void removeFreeCorners() {
        DIRECTIONS.stream().filter(d -> !d.cardinal).forEach(d -> {
            if (!this.contains(d.rotateClockwise()) || !this.contains(d.rotateCounterClockwise())) {
                this.remove(d);
            }
        });
    }

    @Override
    public String toString() {
        return String.join(", ",
                DIRECTIONS.stream().filter(this::contains).map(Direction::lowercaseName).toArray(String[]::new));
    }
}
