package com.darkxell.common.util;

/** Represents a rectangle. */
public class DoubleRectangle {

	/** The x position of the left border of the rectangle. */
	public double x;
	/** The y position of the top border of the rectangle. */
	public double y;
	/** The width of the rectangle. */
	public double width;
	/** The height of the rectangle. */
	public double height;

	/** Constructs a new DoubleRectangle. */
	public DoubleRectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Constructs a new DoubleRectangle. The rectangle can be constructed from
	 * it's center point.
	 */
	public DoubleRectangle(double x, double y, double width, double height, boolean fromcenter) {
		if (fromcenter) {
			this.x = x - (width / 2);
			this.y = y - (height / 2);
			this.width = width;
			this.height = height;
		} else {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	/** Predicate that returns true if the two rectangles have at least one intersection point. */
	public boolean intersects(DoubleRectangle other)
	{
		double tw = this.width; // 4
		double th = this.height; // 2
		double rw = other.width; // 1.6
		double rh = other.height;// 1.3
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) return false;
		double tx = this.x; // 28
		double ty = this.y; // 38
		double rx = other.x; // 28
		double ry = other.y; // 29.6
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		// overflow || intersect
		return ((rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry));
	}

	/**
	 * Predicate that returns true if and only if the position in parametters is
	 * inside the rectangle.
	 */
	public boolean isInside(Position p) {
		return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
	}

	/**
	 * Method that returns the 9 cardinal points of this hitbox. Those 9 points
	 * can be used to do light collision calculations.
	 * 
	 * @return A 9 length array containing the key positions of this hitbox.
	 */
	public Position[] getCardinals() {
		double w2 = width / 2, h2 = height / 2;
		Position[] cards = new Position[9];
		cards[0] = new Position(x, y);
		cards[1] = new Position(x + w2, y);
		cards[2] = new Position(x + width, y);
		cards[3] = new Position(x, y + h2);
		cards[4] = new Position(x + w2, y + h2);
		cards[5] = new Position(x + width, y + h2);
		cards[6] = new Position(x, y + height);
		cards[7] = new Position(x + w2, y + height);
		cards[8] = new Position(x + width, y + height);
		return cards;
	}

	@Override
	public boolean equals(Object test) {
		if (!(test instanceof DoubleRectangle))
			return false;
		DoubleRectangle t = (DoubleRectangle) test;
		return x == t.x && y == t.y && width == t.width && height == t.height;
	}
	
	@Override
	public String toString()
	{
		return "[" + this.x + "," + this.y + "]\n"+"[" + (this.x+this.width) + "," + (this.y+this.height) + "]";
	}
}
