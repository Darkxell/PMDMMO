package com.darkxell.common.dbobject;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DBObjectTest {

    private ArrayList<DatabaseIdentifier> idArray, idArray2;
    private ArrayList<String> missionIDs;

    @Before
    public void before() {
        this.idArray = new ArrayList<>();
        this.idArray.add(new DatabaseIdentifier(2));
        this.idArray.add(new DatabaseIdentifier(3));
        this.idArray.add(new DatabaseIdentifier(5));
        this.idArray.add(new DatabaseIdentifier(7));
        this.idArray.add(new DatabaseIdentifier(11));

        this.idArray2 = new ArrayList<>();
        this.idArray2.add(new DatabaseIdentifier(0));
        this.idArray2.add(new DatabaseIdentifier(1));
        this.idArray2.add(new DatabaseIdentifier(4));
        this.idArray2.add(new DatabaseIdentifier(6));
        this.idArray2.add(new DatabaseIdentifier(8));

        this.missionIDs = new ArrayList<>();
        this.missionIDs.add("mission::1::lol");
        this.missionIDs.add("mission::2::poggers");
    }

    @Test
    public void testDeployKey() {
        DBDeployKey key = new DBDeployKey("fkhlk", new Random().nextLong(), false, 42);
        DBDeployKey key2 = new DBDeployKey();
        key2.read(key.toJson());
        Assert.assertEquals(key, key2);
    }

    @Test
    public void testInventory() {
        DBInventory inventory = new DBInventory(42, 2, this.idArray);
        DBInventory inventory2 = new DBInventory();
        inventory2.read(inventory.toJson());
        Assert.assertEquals(inventory, inventory2);
    }

    @Test
    public void testPokemon() {
        DBPokemon pokemon = new DBPokemon(1, 2, 3, 4, 5, "nickname", 6, 7, 8, false, 9, 10, 11, 12, 13,
                new DatabaseIdentifier(14), this.idArray);
        DBPokemon pokemon2 = new DBPokemon();
        pokemon2.read(pokemon.toJson());
        Assert.assertEquals(pokemon, pokemon2);
    }

    @Test
    public void testItemStack() {
        DBItemstack item = new DBItemstack(42, 2, 5);
        DBItemstack item2 = new DBItemstack();
        item2.read(item.toJson());
        Assert.assertEquals(item, item2);
    }

    @Test
    public void testLearnedMove() {
        DBLearnedmove move = new DBLearnedmove(2, 3, 4, 5, true, false, 6);
        DBLearnedmove move2 = new DBLearnedmove();
        move2.read(move.toJson());
        Assert.assertEquals(move, move2);
    }

    @Test
    public void testPlayer() {
        DBPlayer player = new DBPlayer(1, "PLAYERNAME", "PASSHASH", 2, 3, 4, this.idArray, this.idArray2,
                new DatabaseIdentifier(75), new DatabaseIdentifier(76), new DatabaseIdentifier(77), this.missionIDs, 10,
                false, true);
        DBPlayer player2 = new DBPlayer();
        player2.read(player.toJson());
        Assert.assertEquals(player, player2);
    }

}
