package com.darkxell.common.move;

import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

/**
 * Move range.<br />
 * <ul>
 * <li>FRONT = 0 ; The Pokemon on the Tile in front of the user.</li>
 * <li>FRONT_ROW = 1 ; The Pokemon on the Tiles in front and diagonals of the user.</li>
 * <li>AROUND = 2 ; All Pokemon within a 1-Tile range of the user.</li>
 * <li>ROOM = 3 ; All Pokemon in a room (or visible, if not in a room.)</li>
 * <li>TWO_TILES = 4 ; The Pokemon on the Tile in front of the user, or if no Pokemon, the one on the second Tile in
 * front.</li>
 * <li>LINE = 5 ; The first Pokemon in the user's direction (up to ten tiles), cuts corners.</li>
 * <li>FLOOR = 6 ; All Pokemon on the Floor.</li>
 * <li>USER = 7 ; Only the user.</li>
 * <li>FRONT_CORNERS = 8 ; The Pokemon on the Tile in front of the user, cuts corners.</li>
 * <li>AMBIENT = 9 ; Does not target any Pokemon.</li>
 * </ul>
 */
public enum MoveRange {
    /** Does not target any Pokemon. */
    Ambient,
    /** All Pokemon adjacent to the user. */
    Around,
    /** All Pokemon up to two Tiles around the user in all DIRECTIONS. */
    Around2,
    /** All Pokemon on the Floor. */
    Floor,
    /** The Pokemon on the Tile in front of the user. */
    Front,
    /** The Pokemon on the Tile in front of the user, cuts corners. */
    Front_corners,
    /** The Pokemon on the Tiles in front and diagonals of the user. */
    Front_row,
    /** The first Pokemon in the user's direction (up to ten tiles), cuts corners. */
    Line,
    /** A random ally. */
    Random_ally,
    /** All Pokemon in a room (or visible, if not in a room.) */
    Room,
    /** Only the user. */
    Self,
    /** The Pokemon on the Tile in front of the user, or if no Pokemon, the one on the second Tile in front. */
    Two_tiles;

    public Message getName(MoveTarget target) {

        String rangeID = "move.info.range." + this.name();
        if (Localization.containsKey(rangeID + "." + target.name()))
            rangeID += "." + target.name();
        return new Message(rangeID);
    }
}
