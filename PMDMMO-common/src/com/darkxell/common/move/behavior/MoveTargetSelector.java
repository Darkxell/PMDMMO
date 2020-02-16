package com.darkxell.common.move.behavior;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRange;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.RandomUtil;

public final class MoveTargetSelector {

    private Floor floor;
    private Move move;
    private DungeonPokemon user;

    public MoveTargetSelector(Floor floor, Move move, DungeonPokemon user) {
        this.move = move;
        this.user = user;
        this.floor = floor;
    }

    /** @return The list of Pokemon affected by the move. */
    public DungeonPokemon[] select() {
        ArrayList<DungeonPokemon> targets = new ArrayList<>();
        Tile t = user.tile(), front = t.adjacentTile(user.facing());

        switch (move.getRange()) {
        case Ambient:
            targets.add(null);
            break;

        case Around2:
            for (int x = -2; x < 3; ++x)
                for (int y = -2; y < 3; ++y)
                    if (x == -2 || x == 2 || y == -2 || y == 2) {
                        Tile t2 = floor.tileAt(t.x + x, t.y + y);
                        if (t2.getPokemon() != null)
                            targets.add(t2.getPokemon());
                    }

        case Around:
            for (Direction d : Direction.DIRECTIONS)
                if (t.adjacentTile(d).getPokemon() != null)
                    targets.add(t.adjacentTile(d).getPokemon());
            break;

        case Floor:
            targets.addAll(floor.listPokemon());
            break;

        case Front_row:
            for (Direction d : new Direction[] { user.facing().rotateCounterClockwise(), user.facing(),
                    user.facing().rotateClockwise() })
                if (t.adjacentTile(d).getPokemon() != null)
                    targets.add(t.adjacentTile(d).getPokemon());
            break;

        case Line:
            int distance = 0;
            boolean done;
            Tile current = t;
            do {
                current = current.adjacentTile(user.facing());
                if (current.getPokemon() != null && move.getTargets().isValid(user, current.getPokemon()))
                    targets.add(current.getPokemon());
                ++distance;
                done = !targets.isEmpty() || distance > 10 || current.isWall();
            } while (!done);
            break;

        case Room:
            Room r = user.tile().room();
            if (r == null) {
                for (Tile tile : floor.aiManager.getAI(user).visibility.currentlyVisibleTiles())
                    if (tile.getPokemon() != null)
                        targets.add(tile.getPokemon());
            } else
                for (Tile t2 : r.listTiles())
                    if (t2.getPokemon() != null)
                        targets.add(t2.getPokemon());
            break;

        case Self:
            targets.add(user);
            break;

        case Two_tiles:
            if (front.getPokemon() != null && move.getTargets().isValid(user, front.getPokemon()))
                targets.add(front.getPokemon());
            else if (front.type() != TileType.WALL && front.type() != TileType.WALL_END) {
                Tile behind = front.adjacentTile(user.facing());
                if (behind.getPokemon() != null)
                    targets.add(behind.getPokemon());
            }
            break;

        case Random_ally:
            ArrayList<DungeonPokemon> candidates = new ArrayList<>(floor.listPokemon());
            candidates.removeIf(p -> p == user || !user.isAlliedWith(p) || p.type == DungeonPokemonType.RESCUEABLE);
            if (!candidates.isEmpty())
                targets.add(RandomUtil.random(candidates, floor.random));
            break;

        case Front:
        case Front_corners:
        default:
            DungeonPokemon f = user.tile().adjacentTile(user.facing()).getPokemon();
            if (f != null) {
                boolean valid = true;
                if (user.facing().isDiagonal() && move.getRange() != MoveRange.Front_corners) {
                    Tile t1 = user.tile().adjacentTile(user.facing().rotateClockwise());
                    if (t1.isWall())
                        valid = false;
                    t1 = user.tile().adjacentTile(user.facing().rotateCounterClockwise());
                    if (t1.isWall())
                        valid = false;
                }
                if (valid)
                    targets.add(f);
            }
        }

        if (!user.hasStatusCondition(StatusConditions.Confused))
            targets.removeIf(p -> !move.getTargets().isValid(user, p));
        if (move.getRange() == MoveRange.Room || move.getRange() == MoveRange.Floor)
            targets.sort(floor.dungeon::compare);
        if (targets.isEmpty() && move.behavior().allowsNoTarget(move, user))
            targets.add(null);

        return targets.toArray(new DungeonPokemon[0]);
    }

}
