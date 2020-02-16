package com.darkxell.common.model.pokemon;

import com.darkxell.common.util.language.Message;

public enum Stat {
    Accuracy(6),
    Attack(0),
    Defense(1),
    Evasiveness(5),
    Health(2),
    SpecialAttack(3),
    SpecialDefense(4),
    Speed(7);

    public final static int DEFAULT_STAGE = 10;
    public final static int MAX_SPEED = 4;

    public final int id;

    Stat(int id) {
        this.id = id;
    }

    public Message getName() {
        return new Message("stat." + this.name());
    }

}