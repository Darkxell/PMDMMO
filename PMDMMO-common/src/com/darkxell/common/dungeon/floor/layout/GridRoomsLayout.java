package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.dungeon.floor.room.SquareRoom;

/** A Layout with random rooms in a grid-like pattern. */
public class GridRoomsLayout extends Layout {

    private int offsetx = 5;
    private int offsety = 5;
    private int gridwidth;
    private int gridheight;
    /** Maximum dimensions of rooms. Width and Height can be switched. */
    public final int maxRoomWidth, maxRoomHeight;
    /** Minimum dimensions of rooms. Width and Height can be switched. */
    public final int minRoomWidth, minRoomHeight;
    private Point[][] roomcenters;
    private int removedrooms;
    public final boolean hasliquids;

    /**
     *
     * @param id                         the id of this layout.
     * @param gridwidth                  the witdh of the rooms grid
     * @param gridheight                 the height of the rooms grid
     * @param minRoomWidth               the minimum width for a room in this layout
     * @param minRoomHeight              the minimum height for a room in this layout
     * @param maxRoomWidth               the maximum width for a room in this layout
     * @param maxRoomHeight              the maximum height for a room in this layout
     * @param maximumremovedroomsammount the maximum ammount of rooms randomely removed from the grid. If this ammount
     *                                   is equal or geater than <code>gridwidth*gridheight</code>, it will be set to 0.
     * @param offsetx                    the default offset size between two rooms on the x axis
     * @param offsety                    the default offset size between two rooms on the y axis
     */
    public GridRoomsLayout(int id, int gridwidth, int gridheight, int minRoomWidth, int minRoomHeight, int maxRoomWidth,
            int maxRoomHeight, int maximumremovedroomsammount, int offsetx, int offsety, boolean hasliquids) {
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
        this.minRoomWidth = minRoomWidth;
        this.minRoomHeight = minRoomHeight;
        this.maxRoomWidth = maxRoomWidth;
        this.maxRoomHeight = maxRoomHeight;
        this.roomcenters = new Point[gridwidth][gridheight];
        this.removedrooms = (maximumremovedroomsammount >= gridwidth * gridheight) ? 0 : maximumremovedroomsammount;
        this.offsetx = offsetx;
        this.offsety = offsety;
        this.hasliquids = hasliquids;
    }

    /**
     *
     * @param id                         the id of this layout.
     * @param gridwidth                  the witdh of the rooms grid
     * @param gridheight                 the height of the rooms grid
     * @param minRoomWidth               the minimum width for a room in this layout
     * @param minRoomHeight              the minimum height for a room in this layout
     * @param maxRoomWidth               the maximum width for a room in this layout
     * @param maxRoomHeight              the maximum height for a room in this layout
     * @param maximumremovedroomsammount the maximum ammount of rooms randomely removed from the grid. If this ammount
     *                                   is equal or geater than <code>gridwidth*gridheight</code>, it will be set to 0.
     * @param offsetx                    the default offset size between two rooms on the x axis
     * @param offsety                    the default offset size between two rooms on the y axis
     */
    public GridRoomsLayout(int id, int gridwidth, int gridheight, int minRoomWidth, int minRoomHeight, int maxRoomWidth,
            int maxRoomHeight, int maximumremovedroomsammount, int offsetx, int offsety) {
        this(id, gridwidth, gridheight, minRoomWidth, minRoomHeight, maxRoomWidth, maxRoomHeight,
                maximumremovedroomsammount, offsetx, offsety, false);
    }

    @Override
    protected void generateLiquids() {
        if (this.hasliquids) {
            int puddleamount = 7;
            // Place water on room edges
            for (int i = 0; i < puddleamount; i++) {
                Tile t = this.floor.randomEmptyTile(true, false, this.random);
                generateTileCircle(t.x, t.y, 4, TileType.WATER, false);
            }
            // Random water
            for (int i = 0; i < puddleamount * 2; i++) {
                int tx = this.random.nextInt(this.floor.tiles.length),
                        ty = this.random.nextInt(this.floor.tiles[0].length);
                generateTileCircle(tx, ty, 5, TileType.WATER, false);
            }
        }

    }

    private void generateTileCircle(int x, int y, int radius, TileType type, boolean override) {
        double r2 = Math.pow(radius, 2);
        for (int i = Math.max(x - radius, 0); i <= x + radius; i++)
            for (int j = Math.max(y - radius, 0); j <= y + radius; j++)
                if (this.floor.tiles.length > i && Math.pow(i - x, 2) + Math.pow(j - y, 2) <= r2
                        && this.floor.tiles[i].length > j
                        && (override || this.floor.tiles[i][j].type() == TileType.WALL))
                    this.floor.tiles[i][j].setType(type);
    }

    @Override
    protected void generateRooms() {
        int gridCellWidth = this.maxRoomWidth + offsetx;
        int gridCellHeight = this.maxRoomHeight + offsety;
        this.createDefaultTiles(2 + this.gridwidth * (this.maxRoomWidth + offsetx),
                2 + this.gridheight * (this.maxRoomHeight + offsety));

        // Sets the centers.
        for (int x = 0; x < this.gridwidth; x++)
            for (int y = 0; y < this.gridheight; y++)
                this.roomcenters[x][y] = new Point((gridCellWidth / 2) + (gridCellWidth * x),
                        (gridCellHeight / 2) + (gridCellHeight * y));

        // Create new rooms
        this.floor.rooms = new Room[this.gridheight * this.gridwidth];
        for (int x = 0; x < this.gridwidth; x++)
            for (int y = 0; y < this.gridheight; y++) {
                int roomX = this.roomcenters[x][y].x
                        - ((this.random.nextInt(maxRoomWidth - minRoomWidth) + minRoomWidth) / 2);
                int roomY = this.roomcenters[x][y].y
                        - ((this.random.nextInt(maxRoomHeight - minRoomHeight) + minRoomHeight) / 2);
                int roomWidth = this.roomcenters[x][y].x
                        + ((this.random.nextInt(maxRoomWidth - minRoomWidth) + minRoomWidth) / 2) - roomX;
                int roomHeight = this.roomcenters[x][y].y
                        + ((this.random.nextInt(maxRoomHeight - minRoomHeight) + minRoomHeight) / 2) - roomY;
                this.floor.rooms[x + (y * this.gridwidth)] = new SquareRoom(this.floor, 1 + roomX, 1 + roomY, roomWidth,
                        roomHeight, false);
            }

        // remove rooms randomely
        int removeammount = this.random.nextInt(this.removedrooms + 1);
        for (int i = 0; i < removeammount; ++i) {
            int remid = this.random.nextInt(this.floor.rooms.length);
            System.arraycopy(this.floor.rooms, remid + 1, this.floor.rooms, remid, this.floor.rooms.length - 1 - remid);
        }
        this.fillRoomsWithGround();
    }

    @Override
    protected void generatePaths() {
        for (int x = 0; x < roomcenters.length; ++x)
            for (int y = 0; y < roomcenters[0].length; ++y) {
                SquareRoom r1 = (SquareRoom) this.floor.roomAt(roomcenters[x][y].x, roomcenters[x][y].y);
                if (x != roomcenters.length - 1) {
                    // Generate the key points
                    int startx1 = (r1 == null) ? roomcenters[x][y].x : r1.x + r1.width;
                    int starty1 = (r1 == null) ? roomcenters[x][y].y : this.random.nextInt(r1.height) + r1.y;
                    SquareRoom r2 = (SquareRoom) this.floor.roomAt(roomcenters[x + 1][y].x, roomcenters[x + 1][y].y);
                    int endx1 = (r2 == null) ? roomcenters[x + 1][y].x : r2.x;
                    int endy1 = (r2 == null) ? roomcenters[x + 1][y].y : this.random.nextInt(r2.height) + r2.y;
                    // Sets the floor in the corridor
                    for (int i = startx1; i < endx1; ++i)
                        this.floor.tiles[i][i - startx1 > (endx1 - startx1) / 2 ? endy1 : starty1]
                                .setType(TileType.GROUND);
                    if (starty1 > endy1)
                        for (int i = endy1; i <= starty1; ++i)
                            this.floor.tiles[startx1 + (endx1 - startx1) / 2][i].setType(TileType.GROUND);
                    else if (starty1 < endy1)
                        for (int i = starty1; i <= endy1; ++i)
                            this.floor.tiles[startx1 + (endx1 - startx1) / 2][i].setType(TileType.GROUND);
                }
                if (y != roomcenters[0].length - 1) {
                    // Generate the key points
                    int startx1 = (r1 == null) ? roomcenters[x][y].x : this.random.nextInt(r1.width) + r1.x;
                    int starty1 = (r1 == null) ? roomcenters[x][y].y : r1.y + r1.height;
                    SquareRoom r2 = (SquareRoom) this.floor.roomAt(roomcenters[x][y + 1].x, roomcenters[x][y + 1].y);
                    int endx1 = (r2 == null) ? roomcenters[x][y + 1].x : this.random.nextInt(r2.width) + r2.x;
                    int endy1 = (r2 == null) ? roomcenters[x][y + 1].y : r2.y;
                    // Sets the floor in the corridor
                    for (int i = starty1; i < endy1; ++i)
                        this.floor.tiles[i - starty1 > (endy1 - starty1) / 2 ? endx1 : startx1][i]
                                .setType(TileType.GROUND);
                    if (startx1 > endx1)
                        for (int i = endx1; i <= startx1; ++i)
                            this.floor.tiles[i][starty1 + (endy1 - starty1) / 2].setType(TileType.GROUND);
                    else if (startx1 < endx1)
                        for (int i = startx1; i <= endx1; ++i)
                            this.floor.tiles[i][starty1 + (endy1 - starty1) / 2].setType(TileType.GROUND);
                }
            }
    }

}
