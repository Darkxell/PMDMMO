package com.darkxell.common.dungeon.floor;

import static com.darkxell.common.dungeon.floor.TileType.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.dungeon.TrapSteppedOnEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.MoneyCollectedEvent;
import com.darkxell.common.item.ItemAction;
import com.darkxell.common.item.ItemContainer;
import com.darkxell.common.item.ItemEffects;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DirectionSet;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

/** Represents a single tile in a Floor. */
public class Tile implements ItemContainer, Comparable<Tile> {

    /** Alternative tiles. */
    public byte alternate = 0;
    public final Floor floor;
    /** The Item on this Tile. null if no Item. */
    private ItemStack item;
    /** This Tile's neighbors connections. */
    private DirectionSet neighbors = new DirectionSet();
    /** The Pokemon standing on this Tile. null if no Pokemon. */
    private DungeonPokemon pokemon;
    /** This Tile's Trap. null if no trap. */
    public Trap trap;
    /** True if this Tile's trap is revealed. Always false if no trap. */
    public boolean trapRevealed;
    /** This Tile's type. */
    private TileType type;
    /** This Tile's coordinates. */
    public final int x, y;

    public Tile(Floor floor, int x, int y, TileType type) {
        this.floor = floor;
        this.x = x;
        this.y = y;
        this.setType(type);
    }

    @Override
    public void addItem(ItemStack item) {
        if (this.item != null && this.item.itemid() == item.itemid() && this.item.item().isStackable())
            this.item.setQuantity(this.item.quantity() + item.quantity());
        else
            this.setItem(item);
    }

    /**
     * @return The Tile adjacent to this Tile in the input direction. See {@link DirectionSet#NORTH}.
     */
    public Tile adjacentTile(Direction direction) {
        Point2D p = direction.move(this.x, this.y);
        return this.floor.tileAt((int) p.getX(), (int) p.getY());
    }

    public Tile adjacentTile(int xOffset, int yOffset) {
        return this.floor.tileAt(this.x + xOffset, this.y + yOffset);
    }

    /** @return True if there are walls blocking the path in the input direction. */
    public boolean blockingWalls(DungeonPokemon pokemon, Direction direction) {
        if (!direction.isDiagonal())
            return false;
        Pair<Direction, Direction> corners = direction.splitDiagonal();
        return !this.adjacentTile(corners.first).canCross(pokemon)
                || !this.adjacentTile(corners.second).canCross(pokemon);
    }

    @Override
    public int canAccept(ItemStack item) {
        return (!this.hasItem() || (item.item().isStackable() && this.getItem().item().getID() == item.item().getID()))
                ? 0
                : -1;
    }

    /**
     * @return True if the input Pokemon can walk diagonally with this Tile as a corner.
     */
    public boolean canCross(DungeonPokemon pokemon) {
        if (pokemon.species().isType(PokemonType.Ghost))
            return true;
        return this.type == TileType.GROUND || this.type == WATER || this.type == LAVA || this.type == AIR
                || this.type == STAIR || this.type == WARP_ZONE;
    }

    /**
     * @param  direction - The direction of the movement.
     * @return           True if the input Pokemon can walk on this Tile.
     */
    public boolean canMoveTo(DungeonPokemon pokemon, Direction direction, boolean allowSwitching) {
        if (!this.canWalkOn(pokemon, allowSwitching))
            return false;
        return !this.blockingWalls(pokemon, direction.opposite());
    }

    /**
     * @param  allowSwitching - True if switching leader and ally is allowed.
     * @return                True if the input Pokemon can walk on this Tile.
     */
    public boolean canWalkOn(DungeonPokemon pokemon, boolean allowSwitching) {
        if (this.getPokemon() != null)
            // If team leader and Pokemon here is ally, can exchange position
            if (!(allowSwitching && pokemon.isAlliedWith(this.getPokemon()) && pokemon.canMove(this.floor)))
                return false;
        return this.type.canWalkOn(pokemon);
    }

    @Override
    public int compareTo(Tile tile) {
        if (this.y == tile.y)
            return Integer.compare(this.x, tile.x);
        return Integer.compare(this.y, tile.y);
    }

    @Override
    public long containerID() {
        return this.y * this.floor.getWidth() + this.x;
    }

    @Override
    public Message containerName() {
        return new Message("menu.ground");
    }

    @Override
    public ItemContainerType containerType() {
        return ItemContainerType.TILE;
    }

    @Override
    public void deleteItem(int index) {
        this.setItem(null);
    }

    /** @return The distance between this Tile and another input Tile. */
    public double distance(Tile tile) {
        return Math.sqrt(Math.pow(this.x - tile.x, 2) + Math.pow(this.y - tile.y, 2));
    }

    public Point2D distanceCoordinates(Tile destination) {
        return new Point2D.Double(destination.x - this.x, destination.y - this.y);
    }

    /**
     * @return The sum of NORTH, EAST, SOUTH, WEST if the tile in that direction is the same type as this tile.
     */
    public DirectionSet getCardinalDifferences() {
        DirectionSet s = new DirectionSet();
        if (this.adjacentTile(Direction.NORTH).type() != this.type())
            s.add(Direction.NORTH);
        if (this.adjacentTile(Direction.EAST).type() != this.type())
            s.add(Direction.EAST);
        if (this.adjacentTile(Direction.SOUTH).type() != this.type())
            s.add(Direction.SOUTH);
        if (this.adjacentTile(Direction.WEST).type() != this.type())
            s.add(Direction.WEST);
        return s;
    }

    /** @return The Item on this Tile. null if no Item. */
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.getItem();
    }

    public DirectionSet getNeighbors() {
        return this.neighbors;
    }

    /** @return The Pokemon standing on this Tile. null if no Pokemon. */
    public DungeonPokemon getPokemon() {
        return this.pokemon;
    }

    /**
     * @return The sum of each direction if the tile in that direction is the same type as this tile.
     */
    public DirectionSet getSurroundingDifferences() {
        DirectionSet s = this.getCardinalDifferences();
        if (this.adjacentTile(Direction.NORTHEAST).type() != this.type())
            s.add(Direction.NORTHEAST);
        if (this.adjacentTile(Direction.NORTHWEST).type() != this.type())
            s.add(Direction.NORTHWEST);
        if (this.adjacentTile(Direction.SOUTHEAST).type() != this.type())
            s.add(Direction.SOUTHEAST);
        if (this.adjacentTile(Direction.SOUTHWEST).type() != this.type())
            s.add(Direction.SOUTHWEST);
        return s;
    }

    public boolean hasItem() {
        return this.getItem() != null;
    }

    public boolean hasTrap() {
        return this.trap != null;
    }

    public boolean isAdjacentTo(Tile tile) {
        Point2D d = this.distanceCoordinates(tile);
        return d.getX() <= 1 && d.getY() <= 1 && this != tile;
    }

    public boolean isAdjacentWalkable(Direction direction) {
        Tile t = this.adjacentTile(direction);
        return t != null
                && (t.type() == TileType.GROUND || t.type() == TileType.STAIR || t.type() == TileType.WARP_ZONE);
    }

    /** @return True if this Tile has no trap or item. */
    public boolean isEmpty() {
        return this.item == null && !this.hasTrap();
    }

    public boolean isInRoom() {
        return this.floor.roomAt(this.x, this.y) != null;
    }

    public boolean isWall() {
        return this.type() == TileType.WALL || this.type() == TileType.WALL_END;
    }

    @Override
    public ArrayList<ItemAction> legalItemActions(boolean inDungeon) {
        ArrayList<ItemAction> actions = new ArrayList<>();
        actions.add(ItemAction.GET);
        actions.add(ItemAction.SWAP);
        return actions;
    }

    @Override
    public Tile locationOnFloor() {
        return this;
    }

    public int maxOrthogonalDistance(Tile tile) {
        return Math.max(Math.abs(this.x - tile.x), Math.abs(this.y - tile.y));
    }

    /**
     * Called when an adjacent tile has its type changed.
     *
     * @param direction - The direction of the Tile. See {@link DirectionSet#NORTH}.
     */
    private void onNeighborTypeChange(Direction direction) {
        if (this.neighbors.contains(direction))
            this.neighbors.remove(direction);
        if (this.type.connectsTo(this.adjacentTile(direction).type))
            this.neighbors.add(direction);
        this.neighbors.removeFreeCorners();
    }

    /** Called when a Pokemon steps on this Tile. */
    public void onPokemonStep(PokemonTravelEvent travelEvent, ArrayList<Event> events) {
        if (this.hasItem()) {
            ItemStack i = this.getItem();
            int index = pokemon.player() == null ? -1 : pokemon.player().inventory().canAccept(i);
            if (!travelEvent.running() && i.item().effect() == ItemEffects.Pokedollars && pokemon.player() != null)
                events.add(new MoneyCollectedEvent(travelEvent.floor, travelEvent, pokemon));
            else if (!travelEvent.running() && pokemon.player() != null && index != -1)
                events.add(new ItemMovedEvent(travelEvent.floor, travelEvent, ItemAction.GET, pokemon, this, 0,
                        pokemon.player().inventory(), -1));
            else if (!travelEvent.running() && !pokemon.hasItem())
                events.add(new ItemMovedEvent(travelEvent.floor, travelEvent, ItemAction.GET, pokemon, this, 0, pokemon,
                        -1));
            else
                events.add(new MessageEvent(travelEvent.floor, travelEvent,
                        new Message("ground.step").addReplacement("<pokemon>", pokemon.getNickname())
                                .addReplacement("<item>", this.getItem().name())));
        }

        if (this.hasTrap())
            events.add(new TrapSteppedOnEvent(travelEvent.floor, travelEvent, pokemon, this));
    }

    /**
     * Called when this Tile's type is changed. Reloads the connections of itself and its neighbors.
     */
    private void onTypeChanged() {
        if (this.floor.isGenerated()) {
            this.updateNeighbors();
            for (Direction direction : Direction.DIRECTIONS) {
                Tile t = this.adjacentTile(direction);
                if (t != null)
                    t.onNeighborTypeChange(direction.opposite());
            }
            this.neighbors.removeFreeCorners();
        }
    }

    /** Sets this Tile's Pokemon to null, only if it is the input Pokemon. */
    public void removePokemon(DungeonPokemon pokemon) {
        if (this.getPokemon() == pokemon)
            this.setPokemon(null);
    }

    /** @return The Room this Tile is in. May return null. */
    public Room room() {
        return this.floor.roomAt(this);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        this.setItem(item);
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Sets the Pokemon on this tile. Also changes this Pokemon's previous tile's Pokemon to null.
     */
    public void setPokemon(DungeonPokemon pokemon) {
        // if (this.pokemon != null && this.pokemon.tile() == this)
        // this.pokemon.setTile(null);
        if (pokemon == null)
            this.pokemon = null;
        else {
            if (pokemon.tile() != null)
                pokemon.tile().removePokemon(pokemon);
            this.pokemon = pokemon;
            this.pokemon.setTile(this);
            if (this.floor.aiManager.getAI(this.pokemon) != null)
                this.floor.aiManager.getAI(this.pokemon).visibility.onPokemonMoved();
        }
    }

    /** Sets this Tile's type. */
    public Tile setType(TileType type) {
        this.type = type;
        if (this.isWall()) {
            this.alternate = (byte) (Math.random() * 10);
            if (this.alternate > 2)
                this.alternate = 0;
        } else if (this.type == TileType.GROUND) {
            this.alternate = (byte) (Math.random() * 10);
            if (this.alternate > 1)
                this.alternate = 0;
        } else
            this.alternate = 0;
        this.onTypeChanged();
        return this;
    }

    @Override
    public int size() {
        return this.hasItem() ? 1 : 0;
    }

    @Override
    public String toString() {
        return this.x + "," + this.y + ": " + this.type.name();
    }

    /** @return This Tile's type. */
    public TileType type() {
        return this.type;
    }

    /** Checks each neighbor. */
    public void updateNeighbors() {
        this.neighbors.clear();
        for (Direction direction : Direction.DIRECTIONS) {
            Tile t = this.adjacentTile(direction);
            if (t == null || t.type.connectsTo(this.type))
                this.neighbors.add(direction);
        }
        this.neighbors.removeFreeCorners();
    }
}
