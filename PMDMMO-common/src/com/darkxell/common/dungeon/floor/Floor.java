package com.darkxell.common.dungeon.floor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.darkxell.common.Registries;
import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AIManager;
import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.dungeon.data.DungeonEncounter;
import com.darkxell.common.dungeon.data.DungeonItemGroup;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.PokemonSpawnedEvent;
import com.darkxell.common.event.dungeon.weather.PersistantWeatherChangedEvent;
import com.darkxell.common.event.dungeon.weather.WeatherChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.mission.DungeonMission;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.status.ActiveFloorStatus;
import com.darkxell.common.status.FloorStatus;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.weather.ActiveWeather;
import com.darkxell.common.weather.Weather;

/** Represents a generated Floor in a Dungeon. */
public class Floor {

    private final ArrayList<ActiveFloorStatus> activeFloorStatuses;
    /** Stores all AI objects for Pokemon on this Floor. */
    public final AIManager aiManager;
    /** The current Weather condition applied to this Floor. */
    private ActiveWeather currentWeather;
    /** IDs of the Cutscenes to play when, respectively, entering this Floor and defeating the boss. */
    public String cutsceneIn, cutsceneOut;
    /** Storypos required to play cutsceneIn and cutsceneOut. */
    public int cutsceneStorypos;
    /** This Floor's data. */
    public final FloorData data;
    /** This Floor's Dungeon. */
    public final DungeonExploration dungeon;
    /** This Floor's ID. */
    public final int id;
    private boolean isGenerating = true;
    /** True if this Floor is a static floor. No random Pokemon will spawn. */
    public final boolean isStatic;
    /** This Floor's layout. */
    public final Layout layout;
    /** The number of turns until a Pokemon spawns. */
    private int nextSpawn;
    /** the default weather condition for this Floor. */
    private ActiveWeather persistantWeather;
    /** RNG for game logic: moves, mob spawning, etc. */
    public final Random random;
    /** This Floor's rooms. null before generating. */
    public Room[] rooms;
    /** The position at which the team will spawn. */
    public Point teamSpawn;
    /** The direction which the team will spawn facing. */
    public Direction teamSpawnDirection = Direction.SOUTH;
    /**
     * This Floor's tiles. null before generating. Note that this array must NOT be modified. It is only public because
     * the generation algorithm uses this array to generate the floor.
     */
    public Tile[][] tiles;

    public Floor(int id, Layout layout, DungeonExploration dungeon, Random random, boolean isStatic) {
        this.id = id;
        this.dungeon = dungeon;
        this.data = this.dungeon.dungeon().getData(this.id);
        this.layout = layout;
        this.random = random;
        this.isStatic = isStatic;
        this.activeFloorStatuses = new ArrayList<>();
        this.aiManager = new AIManager(this);
        this.persistantWeather = Weather.CLEAR.create(this, null, -1);
    }

    public ActiveFloorStatus[] activeStatuses() {
        return this.activeFloorStatuses.toArray(new ActiveFloorStatus[this.activeFloorStatuses.size()]);
    }

    public void addFloorStatus(ActiveFloorStatus status) {
        this.activeFloorStatuses.add(status);
    }

    /**
     * @return True if the input Tile connects to a path outside a Room (considering that Tile is in a Room, which is
     *         not tested in this method).
     */
    public boolean connectsToPath(Tile tile) {
        for (Direction direction : Direction.CARDINAL)
            if (!tile.adjacentTile(direction).isInRoom() && tile.adjacentTile(direction).type() == TileType.GROUND)
                return true;
        return false;
    }

    private int countWildPokemon() {
        int count = 0;
        for (DungeonPokemon p : this.listPokemon())
            if (p.isEnemy())
                ++count;
        return count;
    }

    public ActiveWeather currentWeather() {
        if (this.currentWeather != null)
            return this.currentWeather;
        return this.persistantWeather;
    }

    /** Clears unnecessary data of this Floor. */
    public void dispose() {
        for (Tile[] row : this.tiles)
            for (Tile tile : row)
                if (tile.getPokemon() != null && tile.getPokemon().type != DungeonPokemonType.TEAM_MEMBER)
                    tile.getPokemon().dispose();
    }

    public DungeonPokemon findPokemon(long id) {
        for (DungeonPokemon p : this.listPokemon())
            if (p.id() == id)
                return p;
        return null;
    }

    /** Generates this Floor. */
    public void generate() {
        this.layout.generate(this);
        isGenerating = false;
        for (Tile[] row : this.tiles)
            for (Tile t : row)
                t.updateNeighbors();
    }

    public int getHeight() {
        return this.tiles[0].length;
    }

    /** @return A random quantity of Pokedollars for a single stack. */
    public int getMoneyQuantity() {
        return this.dungeon.dungeon().getMoneyQuantity(this.random, this.id);
    }

    public int getWidth() {
        return this.tiles.length;
    }

    public boolean hasStatus(FloorStatus status) {
        for (ActiveFloorStatus s : this.activeFloorStatuses)
            if (s.status == status)
                return true;
        return false;
    }

    /** @return True if this Floor is done generating. */
    public boolean isGenerated() {
        return !isGenerating;
    }

    /** @return A list of all Items laying on the ground of this Floor. */
    public ArrayList<ItemStack> listItemsOnFloor() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (Tile[] row : this.tiles)
            for (Tile t : row)
                if (t.getItem() != null)
                    items.add(t.getItem());
        return items;
    }

    /** @return A list of all Pokemon on this Floor. */
    public ArrayList<DungeonPokemon> listPokemon() {
        ArrayList<DungeonPokemon> pokemon = new ArrayList<DungeonPokemon>();
        if (this.tiles != null)
            for (Tile[] row : this.tiles)
                for (Tile t : row)
                    if (t.getPokemon() != null && !pokemon.contains(t.getPokemon()))
                        pokemon.add(t.getPokemon());
        return pokemon;
    }

    /** Called when a this Floor starts. */
    public void onFloorStart(ArrayList<DungeonEvent> events) {
        this.dungeon.eventProcessor.onFloorStart(this);
        Weather w = this.dungeon.dungeon().weather(this.id, this.random);
        events.add(new PersistantWeatherChangedEvent(this, new ActiveWeather(w, null, this, -1)));
        for (DungeonPokemon pokemon : this.listPokemon())
            pokemon.onFloorStart(this, events);
        for (DungeonMission mission : this.dungeon.activeMissions)
            if (!mission.isCleared())
                mission.onFloorStart(this, events);
    }

    /** Called when a new turn starts. */
    public void onTurnStart(ArrayList<DungeonEvent> events) {
        // events.add(new MessageEvent(this, new Message("New turn!", false)));

        // For each existing Pokemon: has been moved to DungeonInstance

        // Weather
        if (this.currentWeather != null)
            this.currentWeather.update(events);
        this.persistantWeather.update(events);

        // Statuses
        for (int s = 0; s < this.activeFloorStatuses.size(); ++s)
            this.activeFloorStatuses.get(s).tick(this, events);

        // Pokemon spawning
        if (!this.isStatic && this.data.pokemonDensity() > this.countWildPokemon())
            if (this.nextSpawn <= 0) {
                DungeonEncounter s = this.dungeon.dungeon().randomEncounter(this.random, this.id);
                if (s != null) {
                    DungeonPokemon wild = new DungeonPokemon(s.pokemon().generate(this.random, s.level));
                    Tile tile = this.randomEmptyTile(true, true, this.random);
                    if (tile != null) {
                        events.add(new PokemonSpawnedEvent(this, wild, tile));
                        this.nextSpawn = RandomUtil.nextIntInBounds(50, 100, this.random) / this.data.pokemonDensity();
                    }
                }
            } else
                --this.nextSpawn;
    }

    private void placePlayer(Player player) {
        Point spawn = this.teamSpawn;
        this.tileAt(spawn.x, spawn.y).setPokemon(player.getDungeonLeader());
        player.getDungeonLeader().setFacing(this.teamSpawnDirection);
        this.aiManager.register(player.getDungeonLeader());

        ArrayList<Tile> candidates = new ArrayList<Tile>();
        Tile initial = player.getDungeonLeader().tile();
        candidates.add(initial.adjacentTile(Direction.WEST));
        candidates.add(initial.adjacentTile(Direction.EAST));
        candidates.add(initial.adjacentTile(Direction.SOUTH));
        candidates.add(initial.adjacentTile(Direction.NORTH));
        candidates.add(initial.adjacentTile(Direction.NORTHWEST));
        candidates.add(initial.adjacentTile(Direction.NORTHEAST));
        candidates.add(initial.adjacentTile(Direction.SOUTHWEST));
        candidates.add(initial.adjacentTile(Direction.SOUTHEAST));
        candidates.removeIf(t -> t.getPokemon() != null || t.isWall() || t.type() == TileType.WATER
                || t.type() == TileType.LAVA || t.type() == TileType.AIR);

        DungeonPokemon[] team = player.getDungeonTeam();

        for (int i = team.length - 1; i > 0; --i) {
            if (team[i].isFainted())
                continue;
            if (candidates.size() == 0) {
                Logger.e("Could not find a spawn location for ally " + team[i].getNickname() + "!");
                continue;
            }
            this.tileAt(candidates.get(0).x, candidates.get(0).y).setPokemon(team[i]);
            team[i].setFacing(this.teamSpawnDirection);
            this.aiManager.register(team[i]);
            candidates.remove(0);
        }
    }

    public void placePlayers(ArrayList<Player> players) {
        for (Player player : players)
            this.placePlayer(player);
    }

    public Item randomBuriedItem(Random random) {
        ArrayList<DungeonItemGroup> items = this.dungeon.dungeon().buriedItems(this.id);
        return randomItem(items, random);
    }

    /**
     * @param  inRoom          - True if the Tile should be in a Room (will also avoid tiles adjacent to corridors in
     *                         rooms).
     * @param  awayFromPlayers - True if the Tile should be far away from players (to avoid spawning a Pokemon close to
     *                         a player for example).
     * @return                 A Random Tile in this floor.
     */
    public Tile randomEmptyTile(boolean inRoom, boolean awayFromPlayers, Random random) {
        return this.randomEmptyTile(inRoom, awayFromPlayers, null, random);
    }

    /**
     * @param  inRoom          - True if the Tile should be in a Room (will also avoid tiles adjacent to corridors in
     *                         rooms).
     * @param  awayFromPlayers - True if the Tile should be far away from players (to avoid spawning a Pokemon close to
     *                         a player for example).
     * @param  type            - A Type that the Tile has to match. null for any type.
     * @return                 A Random Tile in this floor.
     */
    public Tile randomEmptyTile(boolean inRoom, boolean awayFromPlayers, TileType type, Random random) {
        ArrayList<Tile> candidates = new ArrayList<Tile>();
        if (inRoom)
            for (Room room : this.rooms)
                candidates.addAll(room.listTiles());
        else
            for (Tile[] row : this.tiles)
                for (Tile tile : row)
                    candidates.add(tile);

        if (type != null)
            candidates.removeIf(t -> t.type() != type);

        candidates.removeIf(t -> !t.isEmpty());

        if (inRoom)
            candidates.removeIf(t -> connectsToPath(t));

        if (awayFromPlayers) {
            ArrayList<DungeonPokemon> players = new ArrayList<DungeonPokemon>(this.listPokemon());
            players.removeIf((DungeonPokemon p) -> {
                return !p.isTeamLeader();
            });
            candidates.removeIf((Tile t) -> {
                for (DungeonPokemon player : players)
                    if (Math.abs(t.x - player.tile().x) < 12 && Math.abs(t.y - player.tile().y) < 10)
                        return true;
                return false;
            });
        }

        return RandomUtil.random(candidates, random);
    }

    public Item randomItem(ArrayList<DungeonItemGroup> items, Random random) {
        DungeonItemGroup itemGroup = RandomUtil.weightedRandom(items, DungeonItemGroup.weights(items), random);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<Integer> chances = new ArrayList<Integer>();
        for (int i = 0; i < itemGroup.items.length; ++i) {
            ids.add(itemGroup.items[i]);
            chances.add(itemGroup.chances[i]);
        }
        return Registries.items().find(RandomUtil.weightedRandom(ids, chances, random));
    }

    public Item randomItem(Random random) {
        ArrayList<DungeonItemGroup> items = this.dungeon.dungeon().items(this.id);
        return randomItem(items, random);
    }

    /** @return A random Room in this Floor. */
    public Room randomRoom(Random random) {
        return this.rooms[random.nextInt(this.rooms.length)];
    }

    /** @return A Random Trap in this Floor. */
    public Trap randomTrap(Random random) {
        Pair<ArrayList<Integer>, ArrayList<Integer>> traps = this.dungeon.dungeon().traps(this.id);
        return Registries.traps().find(RandomUtil.weightedRandom(traps.first, traps.second, random));
    }

    public void removeFloorStatus(ActiveFloorStatus status) {
        this.activeFloorStatuses.remove(status);
    }

    /**
     * @param  weather - The weather to clean.
     * @return         The Weather Changed Event if the Weather changes.
     */
    public void removeWeather(ActiveWeather weather, ArrayList<DungeonEvent> events) {
        ActiveWeather previous = this.currentWeather();
        if (this.currentWeather == weather)
            this.currentWeather = null;
        ActiveWeather next = this.currentWeather();
        if (previous != next)
            events.add(new WeatherChangedEvent(this, previous, next));
    }

    /** @return The room at the input X, Y coordinates. null if not in a Room. */
    public Room roomAt(int x, int y) {
        for (Room room : this.rooms)
            if (room.contains(x, y))
                return room;
        return null;
    }

    public Room roomAt(Tile tile) {
        return this.roomAt(tile.x, tile.y);
    }

    public void setPersistantWeather(ActiveWeather weather, ArrayList<DungeonEvent> events) {
        ActiveWeather previous = this.currentWeather();
        this.persistantWeather = weather;
        ActiveWeather next = this.currentWeather();
        if (previous.weather != next.weather)
            events.add(new WeatherChangedEvent(this, previous, next));
    }

    /** Overrides all of the floor's tiles. */
    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    /**
     * @param  weather - The weather to add.
     * @return         The Weather Changed Event if the Weather changes.
     */
    public void setWeather(ActiveWeather weather, ArrayList<DungeonEvent> events) {
        ActiveWeather previous = this.currentWeather();
        this.currentWeather = weather;
        ActiveWeather next = this.currentWeather();
        if (previous.weather != next.weather)
            events.add(new WeatherChangedEvent(this, previous, next));
    }

    public void summonPokemon(DungeonPokemon pokemon, int x, int y, ArrayList<DungeonEvent> events) {
        this.summonPokemon(pokemon, x, y, events, null);
    }

    public void summonPokemon(DungeonPokemon pokemon, int x, int y, ArrayList<DungeonEvent> events, AI ai) {
        if (!(this.tiles == null || x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[x].length))
            this.tileAt(x, y).setPokemon(pokemon);
        if (!this.dungeon.isGeneratingFloor()) {
            this.dungeon.registerActor(pokemon);
            this.dungeon.communication.pokemonIDs.register(pokemon.originalPokemon, this.dungeon.communication.itemIDs,
                    this.dungeon.communication.moveIDs);
        }
        if (!pokemon.isTeamLeader())
            this.aiManager.register(pokemon, ai);

        pokemon.onFloorStart(this, events);
    }

    /** @return The tile at the input X, Y coordinates. */
    public Tile tileAt(int x, int y) {
        if (this.tiles == null || x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[x].length)
            return null;
        return this.tiles[x][y];
    }

    @Override
    public String toString() {
        String s = "";
        for (int y = 0; y < this.getHeight(); ++y) {
            for (int x = 0; x < this.getWidth(); ++x) {
                Tile t = this.tileAt(x, y);
                s += t != null ? t.type().c : "?";
            }
            s += "\n";
        }
        return s;
    }

    public int turnCount() {
        if (this.dungeon.currentTurn() == null)
            return 0;
        return this.dungeon.currentTurn().id;
    }

    public void unsummonPokemon(DungeonPokemon pokemon) {
        this.dungeon.unregisterActor(pokemon);
        if (!pokemon.isTeamLeader())
            this.aiManager.unregister(pokemon);
        pokemon.tile().removePokemon(pokemon);
    }

}
