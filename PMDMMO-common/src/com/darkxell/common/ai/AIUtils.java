package com.darkxell.common.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;
import javafx.util.Pair;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.RandomUtil;

/** Contains various static methods used in AI computations. */
public final class AIUtils {

    /** @return The direction to face to look at an adjacent enemy. May be null if there are no adjacent enemies. */
    public static Direction adjacentEnemyDirection(Floor floor, DungeonPokemon pokemon) {
        ArrayList<Tile> tiles = adjacentTiles(floor, pokemon);
        tiles.removeIf((Tile t) -> t.getPokemon() == null || pokemon.isAlliedWith(t.getPokemon()));
        if (tiles.isEmpty())
            return null;
        return generalDirection(pokemon.tile(), RandomUtil.random(tiles, floor.random));
    }

    /** @return The list of adjacent Tiles the input Pokemon can move to. */
    public static ArrayList<Tile> adjacentReachableTiles(Floor floor, DungeonPokemon pokemon) {
        ArrayList<Tile> tiles = adjacentTiles(floor, pokemon);
        tiles.removeIf((Tile tile) -> !tile.canMoveTo(pokemon, generalDirection(pokemon.tile(), tile), false));
        return tiles;
    }

    /** @return The list of adjacent Tiles for the input Pokemon. */
    public static ArrayList<Tile> adjacentTiles(Floor floor, DungeonPokemon pokemon) {
        ArrayList<Tile> tiles = new ArrayList<>();
        Tile t = pokemon.tile();
        for (Direction d : Direction.DIRECTIONS)
            tiles.add(t.adjacentTile(d));
        return tiles;
    }

    private static boolean canUse(DungeonPokemon pokemon, LearnedMove move, Floor floor) {
        if (!pokemon.canUse(move, floor))
            return false;
        return move.pp() != 0;
    }

    public static LearnedMove chooseMove(AI ai) {
        ArrayList<LearnedMove> candidates = new ArrayList<>();
        for (int i = 0; i < ai.pokemon.moveCount(); ++i)
            candidates.add(ai.pokemon.move(i));
        candidates.removeIf(m -> !canUse(ai.pokemon, m, ai.floor));
        if (candidates.isEmpty())
            candidates.add(new LearnedMove(MoveRegistry.STRUGGLE.id));
        candidates.add(new LearnedMove(MoveRegistry.ATTACK.id));
        return RandomUtil.random(candidates, ai.floor.random);
    }

    /**
     * @param  angle - An angle in degrees.
     * @return       The direction closest to the input angle (0 = North).
     */
    public static Direction closestDirection(double angle) {
        angle = 360 - angle; // Trigonometric to clockwise
        angle += 180; // So that 0=south => 0=north
        angle += 22.5; // So that 23 goes to 45, meaning 23� will be Northeast.
        while (angle >= 360) // Go back to range [0;360[
            angle -= 360;
        return Direction.DIRECTIONS.get(((int) (angle / 45)) % Direction.DIRECTIONS.size());
    }

    /**
     * @return The direction to go to for the input Pokemon to reach the target. May return null if there is no path.
     */
    public static Direction direction(DungeonPokemon pokemon, DungeonPokemon target) {
        return direction(pokemon, target.tile());
    }

    /**
     * @return The direction to go to for the input Pokemon to reach the destination. May return null if there is no
     *         path.
     */
    public static Direction direction(DungeonPokemon pokemon, Tile destination) {
        Direction direction = generalDirection(pokemon.tile(), destination);
        // If is adjacent and can move to, return.
        if (pokemon.tile().adjacentTile(direction).canMoveTo(pokemon, direction, false))
            return direction;

        // Else, if diagonal, try closest cardinal directions.
        if (direction.isDiagonal()) {
            Direction cardinal = generalCardinalDirection(pokemon, destination);
            if (pokemon.tile().adjacentTile(cardinal).canMoveTo(pokemon, cardinal, false))
                return cardinal;

            Pair<Direction, Direction> split = direction.splitDiagonal();
            Direction other;
            if (cardinal == split.getKey())
                other = split.getValue();
            else
                other = split.getKey();
            if (pokemon.tile().adjacentTile(other).canMoveTo(pokemon, other, false))
                return other;
        } else { // Else try to minimize distance by choosing increasingly further distances.
            int distance = pokemon.tile().maxHorizontalDistance(destination);
            Direction testing = direction.rotateClockwise();
            if (pokemon.tile().adjacentTile(testing).maxHorizontalDistance(destination) < distance
                    && pokemon.tile().adjacentTile(testing).canMoveTo(pokemon, testing, false))
                return testing;

            testing = direction.rotateCounterClockwise();
            if (pokemon.tile().adjacentTile(testing).maxHorizontalDistance(destination) < distance
                    && pokemon.tile().adjacentTile(testing).canMoveTo(pokemon, testing, false))
                return direction.rotateCounterClockwise();

            testing = direction.rotateClockwise().rotateClockwise();
            if (pokemon.tile().adjacentTile(testing).maxHorizontalDistance(destination) < distance
                    && pokemon.tile().adjacentTile(testing).canMoveTo(pokemon, testing, false))
                return direction.rotateClockwise().rotateClockwise();

            testing = direction.rotateCounterClockwise().rotateCounterClockwise();
            if (pokemon.tile().adjacentTile(testing).maxHorizontalDistance(destination) < distance
                    && pokemon.tile().adjacentTile(testing).canMoveTo(pokemon, testing, false))
                return direction.rotateCounterClockwise().rotateCounterClockwise();
        }

        // else there is no improvement.
        return null;
    }

    /**
     * @return The farthest Tiles that can be seen and walked on by the input Pokemon, sorted by closeness from the
     *         Pokemon's facing direction. This method considers the Pokemon is not in a Room.
     */
    public static ArrayList<Tile> furthestWalkableTiles(Floor floor, DungeonPokemon pokemon) {
        double maxDistance = 0, distance;
        ArrayList<Tile> tiles = new ArrayList<>(floor.aiManager.getAI(pokemon).visibility.currentlyVisibleTiles());
        tiles.removeIf(tile -> !tile.canWalkOn(pokemon, false) || !isReachable(floor, pokemon, tile));

        for (Tile t : tiles) {
            distance = t.distance(pokemon.tile());
            if (distance >= maxDistance)
                maxDistance = distance;
        }

        final double maxD = maxDistance;
        tiles.removeIf((Tile t) -> {
            return t.distance(pokemon.tile()) < maxD;
        });

        tiles.sort(Comparator.comparingInt((Tile t) -> pokemon.facing().distance(generalDirection(pokemon.tile(), t))));

        return tiles;
    }

    /** @return The general cardinal direction the input Pokemon has to face to look at the target. */
    public static Direction generalCardinalDirection(DungeonPokemon pokemon, DungeonPokemon target) {
        return generalCardinalDirection(pokemon, target.tile());
    }

    /** @return The general cardinal direction the input Pokemon has to face to look at the destination. */
    public static Direction generalCardinalDirection(DungeonPokemon pokemon, Tile destination) {
        double angle = Math.toDegrees(Math.atan2(destination.x - pokemon.tile().x, destination.y - pokemon.tile().y));
        // Make sure angle is in bounds [0;360[
        while (angle < 0)
            angle += 360;
        while (angle >= 360)
            angle -= 360;
        if (angle < 45 || angle > 315)
            return Direction.SOUTH;
        if (angle < 135)
            return Direction.EAST;
        if (angle < 225)
            return Direction.NORTH;
        return Direction.WEST;
    }

    /** @return The general direction the input Pokemon has to face to look at the target. */
    public static Direction generalDirection(DungeonPokemon pokemon, DungeonPokemon target) {
        return generalDirection(pokemon.tile(), target.tile());
    }

    /** @return The general direction the input Pokemon has to face to look at the destination. */
    public static Direction generalDirection(Tile origin, Tile destination) {
        /*
         * if (pokemon == null) System.out.println("pokemon"); if (target == null) System.out.println("target"); if
         * (pokemon.tile() == null) System.out.println(pokemon); if (target.tile == null) System.out.println(target);
         */
        double angle = Math.toDegrees(Math.atan2(destination.x - origin.x, destination.y - origin.y));
        // Make sure angle is in bounds [0;360[
        while (angle < 0)
            angle += 360;
        while (angle >= 360)
            angle -= 360;
        return closestDirection(angle);
    }

    /**
     * @return True if the input Pokemon is just one tile away from the target. If they're diagonally disposed, also
     *         checks if there is a wall blocking their interaction.
     */
    public static boolean isAdjacentTo(DungeonPokemon pokemon, DungeonPokemon target) {
        return isAdjacentTo(pokemon, target, true);
    }

    /**
     * @param  checkBlockingWalls - If true and the Pokemon are diagonally disposed, also checks if there is a wall
     *                            blocking their interaction.
     * @return                    <code>true</code> if the input Pokemon is one tile away from the target.
     */
    public static boolean isAdjacentTo(DungeonPokemon pokemon, DungeonPokemon target, boolean checkBlockingWalls) {
        Direction direction = generalDirection(pokemon, target);
        if (pokemon.tile().adjacentTile(direction).getPokemon() != target)
            return false; // Adjacent test
        else if (!checkBlockingWalls)
            return true;
        return !pokemon.tile().blockingWalls(pokemon, direction); // Blocking walls test
    }

    /**
     * @param  pokemon - The moving Pokemon.
     * @param  tile    - The tile to reach.
     * @return         <code>true</code> if the input Pokemon is able to reach the input Tile.
     */
    public static boolean isReachable(Floor floor, DungeonPokemon pokemon, Tile tile) {
        if (pokemon.tile() == tile)
            return true;

        ArrayList<Tile> visible = new ArrayList<>(floor.aiManager.getAI(pokemon).visibility.currentlyVisibleTiles());
        Stack<Tile> accessible = new Stack<>();
        HashSet<Tile> treated = new HashSet<>();
        accessible.add(pokemon.tile());

        while (!accessible.isEmpty()) {
            Tile t = accessible.pop();
            for (Direction direction : Direction.DIRECTIONS) {
                Tile a = t.adjacentTile(direction);
                if (!treated.contains(a) && visible.contains(a) && a.canWalkOn(pokemon, false)) {
                    boolean ok = true;
                    if (direction.isDiagonal()) {
                        Pair<Direction, Direction> others = direction.splitDiagonal();
                        if (!t.adjacentTile(others.getKey()).canCross(pokemon)
                                || !t.adjacentTile(others.getValue()).canCross(pokemon))
                            ok = false;
                    }
                    if (ok) {
                        if (a == tile)
                            return true;
                        accessible.push(a);
                        treated.add(a);
                    }
                }
            }
        }

        return false;
    }

    /** @return <code>true</code> if the input Pokemon should stop running. */
    public static boolean shouldStopRunning(DungeonPokemon pokemon) {
        Tile tile = pokemon.tile();
        Tile previous = tile.adjacentTile(pokemon.facing().opposite());

        if (!pokemon.tryMoveTo(pokemon.facing(), false))
            return true; // Can't move
        if (tile.isInRoom() && !previous.isInRoom())
            return true; // Entered room
        if (tile.isInRoom() && !tile.adjacentTile(pokemon.facing()).isInRoom())
            return true; // In front of room
        if (tile.type() == TileType.STAIR || tile.trapRevealed || tile.hasItem())
            return true; // Is on special tile
        int origin = 0, destination = 0;
        Direction facing = pokemon.facing();

        for (Direction dir : facing.isDiagonal()
                ? new Direction[] { facing, facing.rotateClockwise(), facing.rotateCounterClockwise() }
                : new Direction[] { facing, facing.rotateClockwise(), facing.rotateCounterClockwise(),
                        facing.rotateClockwise().rotateClockwise(), facing.rotateCounterClockwise(),
                        facing.rotateCounterClockwise().rotateCounterClockwise() }) {
            Tile o = previous.adjacentTile(dir);
            Tile d = tile.adjacentTile(dir);
            if (!(dir.isDiagonal() && !o.isInRoom()) && o.type().canWalkOn(pokemon))
                ++origin;
            if (!(dir.isDiagonal() && !d.isInRoom()) && d.type().canWalkOn(pokemon))
                ++destination;
            if (d.type() == TileType.STAIR || d.trapRevealed || d.getItem() != null)
                return true;
        }

        return destination > origin;
    }

    private AIUtils() {
    }

}
