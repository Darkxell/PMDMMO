package com.darkxell.client.state.map;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.graphics.renderer.DungeonPokemonRenderer;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.resources.images.Sprites.Res_Dungeon;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.client.resources.images.tilesets.DungeonMapTileset;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;

public class DungeonFloorMap extends AbstractDisplayMap {

    public static final int PLAYER_TICK = 30;
    public static final int TILE_SIZE = 4;
    public static final Color walls = Color.WHITE;

    /** True if the default location needs has been set. Reset to false for each new floor. */
    private boolean defaultLocationSet = false;
    private Floor floor;
    /** True if the map should follow the leader. Set to false whenever the player moves the map themselves. */
    private boolean followLeader = true;
    private int tick = 0;
    public final DungeonMapTileset tileset = Res_Dungeon.dungeonMap;
    /** Map offsets. */
    private int x = 0, y = 0;

    @Override
    public void render(Graphics2D g, int width, int height) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        if (Persistence.dungeonState == null)
            return;

        if ((this.followLeader || !this.defaultLocationSet) && this.floor != null
                && Persistence.player.getDungeonLeader() != null
                && Persistence.player.getDungeonLeader().tile() != null) {
            DungeonPokemonRenderer renderer = Persistence.dungeonState.pokemonRenderer
                    .getRenderer(Persistence.player.getDungeonLeader());
            if (renderer != null) {
                this.x = (int) (renderer.x() * TILE_SIZE / AbstractDungeonTileset.TILE_SIZE - width / 2);
                this.y = (int) (renderer.y() * TILE_SIZE / AbstractDungeonTileset.TILE_SIZE - height / 2);
                this.defaultLocationSet = true;
            }
        }

        g.translate(-this.x, -this.y);

        if (this.floor != null) {
            int xStart = this.x / TILE_SIZE, yStart = this.y / TILE_SIZE;

            for (int x = xStart; x < this.floor.getWidth() && x <= xStart + width / TILE_SIZE; ++x)
                for (int y = yStart; y < this.floor.getHeight() && y <= yStart + height / TILE_SIZE; ++y) {
                    if (x < 0 || y < 0)
                        continue;
                    Tile tile = this.floor.tileAt(x, y);
                    if (tile == null)
                        Logger.e("null tile at " + x + ", " + y);
                    else {
                        int tx = tile.x * TILE_SIZE, ty = tile.y * TILE_SIZE;
                        if (Persistence.dungeonState.floorVisibility.isVisible(tile)) {
                            boolean isMain = tile.getPokemon() == Persistence.player.getDungeonLeader();
                            if ((this.tick >= PLAYER_TICK || !isMain) && tile.getPokemon() != null)
                                g.drawImage(this.tileset.ground(), tx, ty, null);
                            else if (tile.getItem() != null
                                    && Persistence.dungeonState.floorVisibility.isItemVisible(tile))
                                g.drawImage(this.tileset.item(), tx, ty, null);
                            else if (tile.trap == TrapRegistry.WONDER_TILE)
                                g.drawImage(this.tileset.wonder(), tx, ty, null);
                            else if (tile.trapRevealed)
                                g.drawImage(this.tileset.trap(), tx, ty, null);
                            else if (tile.type() == TileType.STAIR)
                                g.drawImage(this.tileset.stairs(), tx, ty, null);
                            else if (tile.type() == TileType.WARP_ZONE)
                                g.drawImage(this.tileset.warpzone(), tx, ty, null);
                            else if (tile.type() == TileType.GROUND)
                                g.drawImage(this.tileset.ground(), tx, ty, null);
                            else {
                                g.setColor(walls);
                                if (tile.isAdjacentWalkable(Direction.NORTH))
                                    g.drawLine(tx, ty, tx + 3, ty);
                                if (tile.isAdjacentWalkable(Direction.EAST))
                                    g.drawLine(tx + 3, ty, tx + 3, ty + 3);
                                if (tile.isAdjacentWalkable(Direction.SOUTH))
                                    g.drawLine(tx, ty + 3, tx + 3, ty + 3);
                                if (tile.isAdjacentWalkable(Direction.WEST))
                                    g.drawLine(tx, ty, tx, ty + 3);
                                if (tile.isAdjacentWalkable(Direction.NORTHEAST))
                                    g.drawLine(tx + 3, ty, tx + 3, ty);
                                if (tile.isAdjacentWalkable(Direction.SOUTHEAST))
                                    g.drawLine(tx + 3, ty + 3, tx + 3, ty + 3);
                                if (tile.isAdjacentWalkable(Direction.SOUTHWEST))
                                    g.drawLine(tx, ty + 3, tx, ty + 3);
                                if (tile.isAdjacentWalkable(Direction.NORTHWEST))
                                    g.drawLine(tx, ty, tx, ty);
                            }
                        } else if (tile.getItem() != null
                                && Persistence.dungeonState.floorVisibility.isItemVisible(tile))
                            g.drawImage(this.tileset.item(), tx, ty, null);
                    }
                }

            for (DungeonPokemon pokemon : Persistence.floor.listPokemon())
                if (Persistence.dungeonState.floorVisibility.isVisible(pokemon)) {
                    boolean isMain = pokemon == Persistence.player.getDungeonLeader();
                    DungeonPokemonRenderer renderer = Persistence.dungeonState.pokemonRenderer.getRenderer(pokemon);
                    int x = (int) (renderer.x() * TILE_SIZE / AbstractDungeonTileset.TILE_SIZE),
                            y = (int) (renderer.y() * TILE_SIZE / AbstractDungeonTileset.TILE_SIZE);
                    if (isMain && this.tick >= PLAYER_TICK)
                        g.drawImage(this.tileset.player(), x, y, null);
                    else if (!isMain && Persistence.dungeonState.floorVisibility.isMapVisible(pokemon))
                        if (Persistence.player.isAlly(pokemon) || pokemon.type == DungeonPokemonType.RESCUEABLE)
                            g.drawImage(this.tileset.ally(), x, y, null);
                        else
                            g.drawImage(this.tileset.enemy(), x, y, null);
                }
        }

        g.translate(this.x, this.y);
    }

    @Override
    public void update() {
        if (Persistence.floor != this.floor) {
            this.defaultLocationSet = false;
            this.floor = Persistence.floor;
        }

        final int mapSpeed = 3;

        if (Key.MAP_UP.isPressed())
            this.y -= mapSpeed;
        if (Key.MAP_DOWN.isPressed())
            this.y += mapSpeed;
        if (Key.MAP_LEFT.isPressed())
            this.x -= mapSpeed;
        if (Key.MAP_RIGHT.isPressed())
            this.x += mapSpeed;
        if (Key.MAP_RESET.isPressed()) {
            this.followLeader = true;
            this.defaultLocationSet = false;
        }
        if (Key.MAP_UP.isPressed() || Key.MAP_DOWN.isPressed() || Key.MAP_LEFT.isPressed() || Key.MAP_RIGHT.isPressed())
            this.followLeader = false;

        ++this.tick;
        if (this.tick >= PLAYER_TICK * 2)
            this.tick = 0;
    }

}
