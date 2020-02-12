package com.darkxell.common.testutils;

import java.util.ArrayList;

import com.darkxell.common.ai.AI.CustomAI;
import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.dungeon.data.DungeonEncounter;
import com.darkxell.common.dungeon.data.DungeonItemGroup;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.model.dungeon.DungeonTrapGroupModel;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Direction;

public class TestUtils {

    private static boolean customDungeon = false, customPlayer = false, customLeader = false, customEnemy;
    private static Dungeon dungeon;
    private static DungeonExploration exploration;
    private static Floor floor;
    private static Pokemon leader, enemy;
    private static DungeonPokemon leftPokemon, rightPokemon;
    private static Player player;

    /**
     * generateDefaultObjects() + generateTestFloor()
     */
    public static void generateALL() {
        generateDefaultObjects();
        generateTestFloor();
    }

    public static Dungeon generateDefaultDungeon() {
        ArrayList<DungeonEncounter> pokemon = new ArrayList<>();
        pokemon.add(new DungeonEncounter(1, 2, 1, new FloorSet(1, 2), CustomAI.NONE));
        pokemon.add(new DungeonEncounter(4, 4, 1, new FloorSet(2, 2), CustomAI.NONE));

        ArrayList<DungeonItemGroup> items = new ArrayList<>();
        items.add(new DungeonItemGroup(new FloorSet(1, 2), 1, new int[] { 1 }, new int[] { 1 }));

        ArrayList<DungeonTrapGroupModel> traps = new ArrayList<>();
        traps.add(new DungeonTrapGroupModel(new Integer[] { TrapRegistry.WONDER_TILE.id }, new Integer[] { 1 }, new FloorSet(1, 2)));

        ArrayList<FloorData> floorData = new ArrayList<>();
        floorData.add(new FloorData(new FloorSet(1, 2), 1, 1, Layout.LAYOUT_SINGLEROOM, 1, FloorData.NO_SHADOW,
                PokemonType.Normal, 0, "", 1, (short) 0, (short) 0, (short) 1, (short) 2, (short) 1, (short) 0, -1));

        Dungeon d = new Dungeon(-99, 2, DungeonDirection.DOWN, true, 2000, 0, -1, pokemon, items, new ArrayList<>(),
                new ArrayList<>(), traps, floorData, new ArrayList<>(), 0, 0);
        return d;
    }

    public static Pokemon generateDefaultEnemyPokemon() {
        return Registries.species().find(360).generate(5);
    }

    public static Pokemon generateDefaultLeaderPokemon() {
        return Registries.species().find(150).generate(5);
    }

    /**
     * Generates Dungeon, Player and Pokemon.
     */
    public static void generateDefaultObjects() {
        CommonTestSetup.setUp();
        if (!customDungeon) {
            dungeon = generateDefaultDungeon();
            Registries.dungeons().register(dungeon);
        }

        if (!customPlayer)
            player = generateDefaultPlayer();
    }

    public static Player generateDefaultPlayer() {
        Player p = new Player(new DBPlayer(0, "Player", "passhash", 5000, 500, 300, new ArrayList<>(),
                new ArrayList<>(), new DatabaseIdentifier(0), new DatabaseIdentifier(0), new DatabaseIdentifier(0),
                new ArrayList<>(), 1000, false, false));

        if (!customLeader)
            leader = generateDefaultLeaderPokemon();
        p.setLeaderPokemon(leader);
        p.setInventory(new Inventory(new DBInventory(0, 50, new ArrayList<>()), p));

        return p;
    }

    public static void generateTestFloor() {
        exploration = new DungeonExploration(getDungeon().getID(), 0);
        exploration.addPlayer(player);

        exploration.eventProcessor = new CommonEventProcessor(exploration);
        floor = exploration.initiateExploration(1);

        leftPokemon = player.getDungeonLeader();
        Tile leaderTile = floor.tileAt(20, 20);
        if (customDungeon)
            leaderTile = leftPokemon.tile();
        leaderTile.setPokemon(leftPokemon);
        Tile rightTile = leaderTile.adjacentTile(Direction.EAST).adjacentTile(Direction.EAST);
        if (!customEnemy)
            enemy = generateDefaultEnemyPokemon();
        rightPokemon = enemy.createDungeonPokemon();
        floor.summonPokemon(rightPokemon, rightTile.x, rightTile.y, new ArrayList<>());
    }

    public static Dungeon getDungeon() {
        return dungeon;
    }

    public static Pokemon getEnemy() {
        return enemy;
    }

    public static CommonEventProcessor getEventProcessor() {
        return exploration.eventProcessor;
    }

    public static DungeonExploration getExploration() {
        return exploration;
    }

    public static Floor getFloor() {
        return floor;
    }

    public static Pokemon getLeader() {
        return leader;
    }

    /** @return The Pokemon on the left of the floor. IS a team leader. */
    public static DungeonPokemon getLeftPokemon() {
        return leftPokemon;
    }

    public static Player getPlayer() {
        return player;
    }

    /** @return The Pokemon on the right of the floor. Is NOT a team leader. */
    public static DungeonPokemon getRightPokemon() {
        return rightPokemon;
    }

    public static void resetDungeonToDefault() {
        customDungeon = false;
        dungeon = null;
    }

    public static void resetEnemyPokemonToDefault() {
        customEnemy = false;
        enemy = null;
    }

    public static void resetLeaderPokemonToDefault() {
        customLeader = false;
        leader = null;
    }

    public static void resetPlayerToDefault() {
        customPlayer = false;
        player = null;
    }

    public static void setDungeon(Dungeon d) {
        customDungeon = true;
        dungeon = d;
        Registries.dungeons().register(dungeon);
    }

    public static void setEnemyPokemon(Pokemon p) {
        customEnemy = true;
        enemy = p;
    }

    public static void setLeaderPokemon(Pokemon p) {
        customLeader = true;
        leader = p;
    }

    public static void setPlayer(Player p) {
        customPlayer = true;
        player = p;
    }

}
