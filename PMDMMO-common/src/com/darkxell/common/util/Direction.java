package com.darkxell.common.util;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Directions used in Dungeons.
 */
public enum Direction {
    // cardinal directions
    EAST(2),
    NORTH(0),
    SOUTH(4),
    WEST(6),

    // primary intermediate directions
    NORTHEAST(1, NORTH, EAST),
    NORTHWEST(7, NORTH, WEST),
    SOUTHEAST(3, SOUTH, EAST),
    SOUTHWEST(5, SOUTH, WEST);

    public static final Comparator<Direction> COMPARATOR = Comparator.comparingInt(d -> d.value);

    /**
     * List of all directions in clockwise order.
     */
    public static final List<Direction> DIRECTIONS = Collections
            .unmodifiableList(Arrays.stream(Direction.values()).sorted(COMPARATOR).collect(Collectors.toList()));

    /**
     * List of orthogonal directions, in clockwise order.
     */
    public static final List<Direction> CARDINAL = Collections
            .unmodifiableList(DIRECTIONS.stream().filter(d -> d.cardinal).collect(Collectors.toList()));

    /**
     * @return A random Direction.
     */
    public static Direction randomDirection(Random random) {
        return DIRECTIONS.get(random.nextInt(8));
    }

    /**
     * Order in clockwise rotation.
     */
    public final int value;

    /**
     * Is it one of {@link #NORTH}, {@link #SOUTH}, {@link #EAST}, or {@link #WEST}?
     */
    public final boolean cardinal;

    private Direction[] parents;

    /**
     * @param value   Ordinal values.
     * @param parents Which directions that this direction is composed of (e.g. Northeast has North and East).
     */
    Direction(int value, Direction... parents) {
        this.value = value;
        this.parents = parents;
        this.cardinal = this.parents.length == 0;
    }

    /**
     * @return Does this direction contain another direction? (e.g. Northwest contains North, West)
     */
    public boolean contains(Direction direction) {
        if (direction == this)
            return true;
        return Arrays.asList(this.parents).contains(direction);
    }

    /**
     * @return How many times this direction needs to rotate (in any way) to match the input Direction.
     */
    public int distance(Direction direction) {
        return Math.abs(this.value - direction.value);
    }

    /**
     * @return This Direction's name in lowercase.
     */
    public String lowercaseName() {
        return this.name().toLowerCase();
    }

    /**
     * @return True if this Direction is diagonal.
     */
    public boolean isDiagonal() {
        return !this.cardinal;
    }

    /**
     * @return The coordinates given when moving from the input X,Y coordinates along this Direction.
     */
    public Point2D move(double x, double y) {
        if (this.contains(WEST))
            x--;
        else if (this.contains(EAST))
            x++;

        if (this.contains(NORTH))
            y--;
        else if (this.contains(SOUTH))
            y++;

        return new Point2D.Double(x, y);
    }

    /**
     * @return The coordinates given when moving from the input coordinates along this Direction.
     */
    public Point2D move(Point2D origin) {
        return this.move(origin.getX(), origin.getY());
    }

    /**
     * Rotate number of steps clockwise.
     *
     * @param  steps Steps to take. Must be positive.
     * @return       New direction.
     */
    private Direction rotateSteps(int steps) {
        return DIRECTIONS.get((this.value + steps) % DIRECTIONS.size());
    }

    /**
     * @return The Direction opposite to this Direction.
     */
    public Direction opposite() {
        return this.rotateSteps(DIRECTIONS.size() / 2);
    }

    /**
     * @return The Direction corresponding to this Direction rotated 45 degrees.
     */
    public Direction rotateClockwise() {
        return this.rotateSteps(1);
    }

    /**
     * @return The Direction corresponding to this Direction rotated -45 degrees.
     */
    public Direction rotateCounterClockwise() {
        return this.rotateSteps(DIRECTIONS.size() - 1);
    }

    /**
     * @return                               The pair of Directions forming this diagonal Direction.
     * @throws UnsupportedOperationException If the direction is not a primary intermediate direction.
     */
    public Pair<Direction, Direction> splitDiagonal() {
        if (this.cardinal)
            throw new UnsupportedOperationException("Cannot split a cardinal direction");
        else if (this.parents.length != 2)
            throw new UnsupportedOperationException("Cannot split a non-primary intermediate direction");

        return new Pair<>(this.parents[0], this.parents[1]);
    }
}
