package com.darkxell.common.move;

import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.Move.MoveRange;
import com.darkxell.common.move.Move.MoveTarget;
import com.darkxell.common.pokemon.PokemonType;

public class MoveBuilder {

    private int accuracy = 100, critical = 12, effectID = 1, id = 0, power = 10, pp = 10;
    private MoveCategory category = MoveCategory.Physical;
    private boolean dealsDamage = true, ginsengable = true, piercesFreeze = false, reflectable = true,
            snatchable = true, sound = false;
    private MoveRange range = MoveRange.Front;
    private MoveTarget targets = MoveTarget.Foes;
    private PokemonType type = PokemonType.Normal;

    public int accuracy() {
        return this.accuracy;
    }

    public Move build() {
        return new Move(this.id(), this.type(), this.category(), this.pp(), this.power(), this.accuracy(), this.range(),
                this.targets(), this.critical(), this.reflectable(), this.snatchable(), this.sound(),
                this.piercesFreeze(), this.dealsDamage(), this.ginsengable(), this.effectID());
    }

    public MoveCategory category() {
        return this.category;
    }

    public int critical() {
        return this.critical;
    }

    public boolean dealsDamage() {
        return this.dealsDamage;
    }

    public int effectID() {
        return this.effectID;
    }

    public boolean ginsengable() {
        return this.ginsengable;
    }

    public int id() {
        return this.id;
    }

    public boolean piercesFreeze() {
        return this.piercesFreeze;
    }

    public int power() {
        return this.power;
    }

    public int pp() {
        return this.pp;
    }

    public MoveRange range() {
        return this.range;
    }

    public boolean reflectable() {
        return this.reflectable;
    }

    public boolean snatchable() {
        return this.snatchable;
    }

    public boolean sound() {
        return this.sound;
    }

    public MoveTarget targets() {
        return this.targets;
    }

    public PokemonType type() {
        return this.type;
    }

    public MoveBuilder withAccuracy(int accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public MoveBuilder withCategory(MoveCategory category) {
        this.category = category;
        return this;
    }

    public MoveBuilder withCritical(int critical) {
        this.critical = critical;
        return this;
    }

    public MoveBuilder withEffectID(int effectID) {
        this.effectID = effectID;
        return this;
    }

    public MoveBuilder withFreezePiercing() {
        this.piercesFreeze = true;
        return this;
    }

    public MoveBuilder withID(int id) {
        this.id = id;
        return this;
    }

    public MoveBuilder withoutDamage() {
        this.dealsDamage = false;
        return this;
    }

    public MoveBuilder withoutGinsengable() {
        this.ginsengable = false;
        return this;
    }

    public MoveBuilder withoutReflectable() {
        this.reflectable = false;
        return this;
    }

    public MoveBuilder withoutSnatchable() {
        this.snatchable = false;
        return this;
    }

    public MoveBuilder withPower(int power) {
        this.power = power;
        return this;
    }

    public MoveBuilder withPP(int pp) {
        this.pp = pp;
        return this;
    }

    public MoveBuilder withRange(MoveRange range) {
        this.range = range;
        return this;
    }

    public MoveBuilder withSound() {
        this.sound = true;
        return this;
    }

    public MoveBuilder withTargets(MoveTarget targets) {
        this.targets = targets;
        return this;
    }

    public MoveBuilder withType(PokemonType type) {
        this.type = type;
        return this;
    }

}
