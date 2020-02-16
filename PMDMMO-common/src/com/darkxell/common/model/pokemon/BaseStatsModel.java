package com.darkxell.common.model.pokemon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseStatsModel implements Communicable {

    /** Level for these stats. */
    @XmlAttribute(name = "lvl")
    private int level;

    /** Attack. */
    @XmlAttribute(name = "atk")
    private Integer attack;

    /** Defense. */
    @XmlAttribute(name = "def")
    private Integer defense;

    /** Health Points. */
    @XmlAttribute(name = "hp")
    private Integer health;

    /** Special Attack. */
    @XmlAttribute(name = "spatk")
    private Integer specialAttack;

    /** Special Defense. */
    @XmlAttribute(name = "spdef")
    private Integer specialDefense;

    /** Movement Speed. */
    @XmlAttribute(name = "speed")
    private Integer moveSpeed;

    public BaseStatsModel() {
    }

    public BaseStatsModel(int level, Integer attack, Integer defense, Integer health, Integer specialAttack,
            Integer specialDefense, Integer moveSpeed) {
        this.level = level;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.moveSpeed = moveSpeed;
    }

    public BaseStatsModel(int level, int[] stat) {
        this.level = level;
        this.attack = stat[Stat.Attack.id];
        this.defense = stat[Stat.Defense.id];
        this.health = stat[Stat.Health.id];
        this.specialAttack = stat[Stat.SpecialAttack.id];
        this.specialDefense = stat[Stat.SpecialDefense.id];
        this.moveSpeed = stat.length == 5 ? 0 : stat[5];
    }

    public BaseStatsModel copy() {
        return new BaseStatsModel(this.level, this.attack, this.defense, this.health, this.specialAttack,
                this.specialDefense, this.moveSpeed);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseStatsModel))
            return false;
        BaseStatsModel s = (BaseStatsModel) obj;
        return this.level == s.level && this.attack.equals(s.attack) && this.defense.equals(s.defense)
                && this.health.equals(s.health) && this.moveSpeed.equals(s.moveSpeed)
                && this.specialAttack.equals(s.specialAttack) && this.specialDefense.equals(s.specialDefense);
    }

    public Integer getAttack() {
        return this.attack;
    }

    public Integer getDefense() {
        return this.defense;
    }

    public Integer getHealth() {
        return this.health;
    }

    public int getLevel() {
        return level;
    }

    public Integer getMoveSpeed() {
        return this.moveSpeed;
    }

    public Integer getSpecialAttack() {
        return this.specialAttack;
    }

    public Integer getSpecialDefense() {
        return this.specialDefense;
    }

    @Override
    public void read(JsonObject value) {
        this.attack = value.getInt("atk", 0);
        this.defense = value.getInt("def", 0);
        this.health = value.getInt("hea", 0);
        this.specialAttack = value.getInt("spa", 0);
        this.specialDefense = value.getInt("spd", 0);
        this.moveSpeed = value.getInt("msp", 1);
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMoveSpeed(Integer moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setSpecialAttack(Integer specialAttack) {
        this.specialAttack = specialAttack;
    }

    public void setSpecialDefense(Integer specialDefense) {
        this.specialDefense = specialDefense;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = Json.object();
        root.add("atk", this.attack);
        root.add("def", this.defense);
        root.add("hea", this.health);
        root.add("spa", this.specialAttack);
        root.add("spd", this.specialDefense);
        if (this.moveSpeed != 1)
            root.add("msp", this.moveSpeed);
        return root;
    }

}
