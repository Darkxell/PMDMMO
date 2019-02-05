package com.darkxell.common.test.tests;

import java.util.ArrayList;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.test.UTest;

public class DBObjecttransferTest extends UTest {

    public DBObjecttransferTest() {
        super(ACTION_WARN);
    }

    @Override
    protected int test() {
        ArrayList<DatabaseIdentifier> idArray = new ArrayList<>();
        idArray.add(new DatabaseIdentifier(-1));
        idArray.add(new DatabaseIdentifier(-2));

        // Inventory
        DBInventory i = new DBInventory(0, 1, idArray);
        DBInventory i2 = new DBInventory();
        i2.read(i.toJson());
        if (!i.equals(i2)) {
            log("Inventory Json conversion has mistakes.");
            return 1;
        }

        // Itemstack
        DBItemstack s = new DBItemstack(0, 1, 2);
        DBItemstack s2 = new DBItemstack();
        s2.read(s.toJson());
        if (!s.equals(s2)) {
            log("Itemstack Json conversion has mistakes.");
            return 2;
        }

        // Learnedmove
        DBLearnedmove m = new DBLearnedmove(0, 1, 2, 3, true, false, 4);
        DBLearnedmove m2 = new DBLearnedmove();
        m2.read(m.toJson());
        if (!m.equals(m2)) {
            log("Learnedmove Json conversion has mistakes.");
            return 3;
        }

        // Player
        DBPlayer j = new DBPlayer(0, "My Name", "P455w0Rd----H45H", 1, 2, 3, new ArrayList<>(), idArray,
                new DatabaseIdentifier(4), new DatabaseIdentifier(5), new DatabaseIdentifier(6), null, 0, false, false);
        DBPlayer j2 = new DBPlayer();
        j2.read(j.toJson());
        if (!j.equals(j2)) {
            log("Player Json conversion has mistakes.");
            return 4;
        }

        // Pokemon
        DBPokemon p = new DBPokemon(0, 1, 2, 3, 4, "My Pokemon", 5, 6, 7, true, 8, 9, 10, 11, 12,
                new DatabaseIdentifier(13), idArray);
        DBPokemon p2 = new DBPokemon();
        p2.read(p.toJson());
        if (!p.equals(p2)) {
            log("Pokemon Json conversion has mistakes.");
            return 5;
        }

        return TEST_SUCCESSFUL;
    }

}
