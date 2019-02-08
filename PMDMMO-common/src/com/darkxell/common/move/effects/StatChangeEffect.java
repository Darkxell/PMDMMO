package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.util.language.Message;

public class StatChangeEffect extends RandomStatChangeEffect {

    public final Stat stat;

    public StatChangeEffect(int id, Stat stat, int stage, int probability) {
        super(id, stage, probability);
        this.stat = stat;
    }

    @Override
    public Message descriptionBase(Move move) {
        return super.descriptionBase(move).addReplacement("<stat>", this.stat.getName());
    }

    protected String descriptionID() {
        return super.descriptionID().replaceAll("_random", "");
    }

    @Override
    protected Stat stat(Floor floor) {
        return this.stat;
    }

}
