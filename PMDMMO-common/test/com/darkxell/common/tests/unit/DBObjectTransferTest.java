package com.darkxell.common.tests.unit;

import static com.darkxell.common.util.Communicable.JsonReadingException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.darkxell.common.dbobject.*;
import com.darkxell.common.tests.CommonSetup;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.WriterConfig;

public class DBObjectTransferTest {
    @BeforeAll
    public static void setUp() {
        CommonSetup.setUp();
    }

    private ArrayList<DatabaseIdentifier> dummyList() {
        ArrayList<DatabaseIdentifier> idArray = new ArrayList<>();
        idArray.add(new DatabaseIdentifier(-1));
        idArray.add(new DatabaseIdentifier(-2));
        return idArray;
    }

    private String prettyPrint(Communicable c) {
        return c.toJson().toString(WriterConfig.PRETTY_PRINT);
    }

    private void assertCopy(Communicable built, Communicable copy, String name) throws JsonReadingException {
        copy.read(built.toJson());
        Logger.d(name + ":\nbuilt: " + this.prettyPrint(built) + "\ncopy: " + this.prettyPrint(copy));
        assertEquals(copy, built, name + " json conversion has mistakes.");
    }

    @Test
    public void inventory() throws JsonReadingException {
        DBInventory built = new DBInventory(0, 1, this.dummyList());
        DBInventory copy = new DBInventory();
        this.assertCopy(built, copy, "Inventory");
    }

    @Test
    public void itemStack() throws JsonReadingException {
        DBItemstack built = new DBItemstack(0, 1, 2);
        DBItemstack copy = new DBItemstack();
        this.assertCopy(built, copy, "Item stack");
    }

    @Test
    public void learnedMove() throws JsonReadingException {
        DBLearnedmove built = new DBLearnedmove(0, 1, 2, 3, true, false, 4);
        DBLearnedmove copy = new DBLearnedmove();
        this.assertCopy(built, copy, "Learned move");
    }

    @Test
    public void player() throws JsonReadingException {
        DBPlayer built = new DBPlayer(0, "My Name", "P455w0Rd----H45H", 1, 2, 3, new ArrayList<>(), this.dummyList(),
                new DatabaseIdentifier(4), new DatabaseIdentifier(5), new DatabaseIdentifier(6), null, 0, false, false);
        DBPlayer copy = new DBPlayer();
        this.assertCopy(built, copy, "Player");
    }

    @Test
    public void species() throws JsonReadingException {
        DBPokemon built = new DBPokemon(0, 1, 2, 3, 4, "My Pokemon", 5, 6, 7, true, 8, 9, 10, 11, 12,
                new DatabaseIdentifier(13), this.dummyList());
        DBPokemon copy = new DBPokemon();
        this.assertCopy(built, copy, "Species");
    }
}
