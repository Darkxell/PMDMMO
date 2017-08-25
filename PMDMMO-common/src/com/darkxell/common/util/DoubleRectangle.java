package com.darkxell.common.util;

/**
 * Similar properties as <code>Rectangtle</code>, but accepts decimal
 * <code>double</code> values.
 */
public class DoubleRectangle {

    /** The horisontal position of the rectangle */
    public double x;
    /** The vertical position of the rectangle */
    public double y;
    /** The width of the rectangle */
    public double width;
    /** The height of the rectangle */
    public double height;

    /** Constructs a Rectangle with the wanted values. */
    public DoubleRectangle(double x, double y, double width, double height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }

    /**
     * Predicate that returns true if and only if the position in parametters is
     * inside the rectangle.
     */
    public boolean isInside(Position p) {
	return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
    }

}