package com.darkxell.common.pokemon;

import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.model.pokemon.BaseStatsModel;

public class PokemonBaseStats {

    private int attack, defense, health, specialAttack, specialDefense, moveSpeed;

    public Pokemon pokemon;

    public PokemonBaseStats(int attack, int defense, int health, int specialAttack, int specialDefense, int moveSpeed) {
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.moveSpeed = moveSpeed;
    }

    public void add(BaseStatsModel stats) {
        this.attack += stats.getAttack();
        this.defense += stats.getDefense();
        this.health += stats.getHealth();
        this.specialAttack += stats.getSpecialAttack();
        this.specialDefense += stats.getSpecialDefense();
        this.moveSpeed += stats.getMoveSpeed();

        this.updateDBStats();
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getHealth() {
        return health;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    private void updateDBStats() {
        if (this.pokemon == null)
            return;

        DBPokemon db = this.pokemon.getData();
        db.stat_atk = this.attack;
        db.stat_def = this.defense;
        db.stat_hp = this.health;
        db.stat_speatk = this.specialAttack;
        db.stat_spedef = this.specialDefense;
    }

}
