package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.TileTypeChangedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.util.Direction;

public class DestroySurroundingTilesEffect extends MoveEffect {

    public final int radius;

    public DestroySurroundingTilesEffect(int id, int radius) {
        super(id);
        this.radius = radius;
    }

    @Override
    public void additionalEffectsOnUse(MoveSelectionEvent moveEvent, Move move, ArrayList<Event> events) {
        super.additionalEffectsOnUse(moveEvent, move, events);

        Tile t = moveEvent.usedMove().user.tile();
        for (int i = 1; i <= this.radius; ++i) {
            t = t.adjacentTile(Direction.SOUTHWEST);
            for (Direction d : Direction.CARDINAL) { // Go around user North, East, South, West
                for (int times = 0; times < i * 2; ++times) {
                    t = t.adjacentTile(d);
                    if (t.type() == TileType.WALL) {
                        events.add(new TileTypeChangedEvent(moveEvent.floor, moveEvent, t, TileType.GROUND));
                    }
                }
            }
        }

    }

}
