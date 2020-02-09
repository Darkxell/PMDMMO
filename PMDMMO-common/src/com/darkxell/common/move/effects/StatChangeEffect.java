package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.util.language.Message;

public class StatChangeEffect extends RandomStatChangeEffect {

    public final Stat stat;

    public StatChangeEffect(Stat stat, int stage, int probability) {
        super(stage, probability);
        this.stat = stat;
    }

    @Override
    public Message description() {
        return super.description().addReplacement("<stat>", this.stat.getName());
    }

    protected String descriptionID() {
        return super.descriptionID().replaceAll("_random", "");
    }

    @Override
    protected Stat stat(Floor floor) {
        return this.stat;
    }

}
