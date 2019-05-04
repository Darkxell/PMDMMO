package com.darkxell.common.util;

public class MathUtil {

    /** @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return The angle in degrees representing the direction of a line from x1,y1 to x2,y2 (straight south is 0°). */
    public static double angle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Make sure angle is in bounds [0;360[
        while (angle < 0)
            angle += 360;
        while (angle >= 360)
            angle -= 360;
        return angle;
    }

}
