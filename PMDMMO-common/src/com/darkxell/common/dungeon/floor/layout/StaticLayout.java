package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.SkipTurnsAI;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.ComplexRoom;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.dungeon.floor.room.SquareRoom;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.model.floor.IStaticFloorRoom;
import com.darkxell.common.model.floor.StaticFloorComplexRoomModel;
import com.darkxell.common.model.floor.StaticFloorItemModel;
import com.darkxell.common.model.floor.StaticFloorModel;
import com.darkxell.common.model.floor.StaticFloorPokemonModel;
import com.darkxell.common.model.floor.StaticFloorRoomModel;
import com.darkxell.common.model.floor.StaticFloorSpawnModel;
import com.darkxell.common.model.floor.StaticFloorTrapModel;
import com.darkxell.common.model.io.ModelIOHandlers;
import com.darkxell.common.model.pokemon.BaseStatsModel;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Logger;

public class StaticLayout extends Layout {

    private static Pokemon readPokemon(StaticFloorPokemonModel pokemon, Random r) {
        PokemonSpecies species = Registries.species().find(pokemon.getId());
        return species.generate(pokemon.getLevel());
    }

    /** Temporary variable to store the model of the current Floor to load. */
    private StaticFloorModel model;

    @Override
    public void generate(Floor floor) {
        this.model = ModelIOHandlers.staticFloor
                .read(StaticLayout.class.getResource("/data/floors/" + floor.dungeon.id + "-" + floor.id + ".xml"));
        super.generate(floor);
        this.floor.cutsceneIn = this.model.getCuscenein();
        this.floor.cutsceneOut = this.model.getCusceneout();
        this.floor.cutsceneStorypos = Optional.ofNullable(this.model.getCuscenestorypos()).orElse(-1);
    }

    @Override
    protected void generateLiquids() {
    }

    @Override
    protected void generatePaths() {
    }

    @Override
    protected void generateRooms() {
        // ROOMS
        ArrayList<IStaticFloorRoom> rooms = this.model.getRooms();
        this.floor.rooms = new Room[rooms.size()];
        for (int i = 0; i < this.floor.rooms.length; ++i)
            if (rooms.get(i) instanceof StaticFloorComplexRoomModel)
                this.floor.rooms[i] = new ComplexRoom(floor, (StaticFloorComplexRoomModel) rooms.get(i));
            else
                this.floor.rooms[i] = new SquareRoom(this.floor, (StaticFloorRoomModel) rooms.get(i));

        // TILES
        List<String> rows = this.model.getTileRows();
        for (int y = 0; y < rows.size(); ++y) {
            String data = rows.get(y);
            if (this.floor.tiles == null)
                this.floor.tiles = new Tile[data.length()][rows.size()];
            for (int x = 0; x < data.length(); x++) {
                Tile t = new Tile(this.floor, x, y, TileType.find(data.charAt(x)));
                if (t.type() == null) {
                    Logger.e("Invalid tile type: " + data.charAt(x));
                    t.setType(TileType.GROUND);
                }
                this.floor.tiles[x][y] = t;
            }
        }
    }

    @Override
    protected void placeItems() {
        if (this.model.getItems() != null)
            for (StaticFloorItemModel item : this.model.getItems())
                this.floor.tiles[item.getX()][item.getY()].setItem(new ItemStack(item.getId(), item.getQuantity()));
    }

    @Override
    protected void placeStairs() {
    }

    @Override
    protected void placeTeam() {
        StaticFloorSpawnModel spawn = this.model.getSpawn();
        this.floor.teamSpawn = new Point(spawn.getX(), spawn.getY());
        this.floor.teamSpawnDirection = spawn.getFacing();
    }

    @Override
    protected void placeTraps() {
        if (this.model.getTraps() != null)
            for (StaticFloorTrapModel trap : this.model.getTraps()) {
                Tile t = this.floor.tiles[trap.getX()][trap.getY()];
                t.trap = Registries.traps().find(trap.getId());
                if (t.trap == TrapRegistry.WONDER_TILE)
                    t.trapRevealed = true;
            }
    }

    @Override
    protected void summonPokemon() {
        if (this.model.getPokemon() != null)
            for (StaticFloorPokemonModel pokemon : this.model.getPokemon()) {
                boolean isBoss = pokemon.isBoss();
                Pokemon created = readPokemon(pokemon, this.floor.random);
                if (isBoss) {
                    created.getBaseStats()
                            .add(new BaseStatsModel(0, 0, 0, created.getBaseStats().getHealth() * 3, 0, 0, 0));
                }
                DungeonPokemon p = new DungeonPokemon(created);
                if (isBoss)
                    p.type = DungeonPokemonType.BOSS;
                AI ai = null;
                String ainame = pokemon.getAi();
                if (ainame != null)
                    switch (ainame) {
                    case "skipper":
                        ai = new SkipTurnsAI(this.floor, p);
                        break;

                    default:
                        break;
                    }
                this.floor.summonPokemon(p, pokemon.getX(), pokemon.getY(), new ArrayList<>(), ai);
            }
    }

}
