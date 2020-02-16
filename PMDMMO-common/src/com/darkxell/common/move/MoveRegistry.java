package com.darkxell.common.move;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import com.darkxell.common.model.io.ModelIOHandlers;
import com.darkxell.common.model.move.MoveListModel;
import com.darkxell.common.model.move.MoveModel;
import com.darkxell.common.registry.Registry;

/**
 * Holds all Moves.
 */
public final class MoveRegistry extends Registry<Move, MoveListModel> {
    public static Move ATTACK, STRUGGLE;

    public MoveRegistry(URL registryURL) throws IOException {
        super(registryURL, ModelIOHandlers.move, "Moves");

        ATTACK = this.find(0);
        STRUGGLE = this.find(2002);
    }

    @Override
    protected HashMap<Integer, Move> deserializeDom(MoveListModel model) {
        HashMap<Integer, Move> moves = new HashMap<>();
        for (MoveModel m : model.moves) {
            Move move = new Move(m);
            moves.put(move.getID(), move);
        }
        return moves;
    }

    @Override
    protected MoveListModel serializeDom(HashMap<Integer, Move> moves) {
        MoveListModel model = new MoveListModel();
        moves.values().forEach(m -> model.moves.add(m.getModel()));
        return model;
    }
}
