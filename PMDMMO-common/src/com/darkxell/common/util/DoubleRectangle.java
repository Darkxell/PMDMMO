package com.darkxell.common.util;

/**
 * Represents a rectangle.
 */
public class DoubleRectangle {

    /**
     * The x position of the left border of the rectangle.
     */
    public double x;

    /**
     * The y position of the top border of the rectangle.
     */
    public double y;

    /**
     * The width of the rectangle.
     */
    public double width;

    /**
     * The height of the rectangle.
     */
    public double height;

    /**
     * Constructs a new DoubleRectangle.
     */
    public DoubleRectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Construct a new rectangle but with the option of using the origin coordinate as the center.
     */
    public DoubleRectangle(double x, double y, double width, double height, boolean center) {
        this(x, y, width, height);

        if (center) {
            this.x -= (width / 2);
            this.y -= (height / 2);
        }
    }

    public DoubleRectangle(DoubleRectangle other) {
        this(other.x, other.y, other.width, other.height);
    }

    /**
     * Does this rectangle overlap the other?
     */
    public boolean intersects(DoubleRectangle other) {
        double tw = this.width;
        double th = this.height;
        double rw = other.width;
        double rh = other.height;

        // sanity check
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0)
            return false;

        double tx = this.x;
        double ty = this.y;
        double rx = other.x;
        double ry = other.y;

        // get far corners
        tw += tx;
        th += ty;
        rw += rx;
        rh += ry;

        // check that each point pair has arithmetic overflow or intersect
        return ((rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry));
    }

    /**
     * Is the position inside the rectangle?
     */
    public boolean isInside(Position p) {
        return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
    }

    /**
     * Get positions of the corners of the box.
     *
     * @return Array of top left, top right, bottom left, bottom right.
     */
    public Position[] getCorners() {
        return new Position[] { new Position(x, y), new Position(x + width, y), new Position(x, y + height),
                new Position(x + width, y + height) };
    }

    @Override
    public boolean equals(Object test) {
        if (!(test instanceof DoubleRectangle))
            return false;
        DoubleRectangle t = (DoubleRectangle) test;
        return x == t.x && y == t.y && width == t.width && height == t.height;
    }

    @Override
    public String toString() {
        return "[" + this.x + "," + this.y + "]\n" + "[" + (this.x + this.width) + "," + (this.y + this.height) + "]";
    }
}
