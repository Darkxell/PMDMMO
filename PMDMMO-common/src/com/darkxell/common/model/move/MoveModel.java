package com.darkxell.common.model.move;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveCategory;
import com.darkxell.common.move.MoveRange;
import com.darkxell.common.move.MoveTarget;
import com.darkxell.common.pokemon.PokemonType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MoveModel implements Comparable<MoveModel> {

    /** This move's ID. */
    @XmlAttribute
    private int id;

    /** This move's type. */
    @XmlAttribute
    private PokemonType type;

    /** This move's category. See {@link Move#PHYSICAL}. */
    @XmlAttribute
    private MoveCategory category;

    /** This move's effect. */
    @XmlAttribute
    private int effectID;

    /** This move's default Power Points. */
    @XmlAttribute
    private int pp;

    /** This move's power. */
    @XmlAttribute
    private int power;

    /** This move's accuracy. */
    @XmlAttribute
    private Integer accuracy;

    /** The change of landing critical hits. */
    @XmlAttribute
    private Integer critical;

    /** This move's range. */
    @XmlAttribute
    private MoveRange range;

    /** This move's targets. */
    @XmlAttribute
    private MoveTarget targets;

    /** True if this move deals damage. */
    @XmlAttribute
    private Boolean dealsDamage;

    /** True if this move can be boosted by Ginseng. */
    @XmlAttribute
    private Boolean ginsengable;

    /** True if this move pierces frozen Pokemon. */
    @XmlAttribute
    private Boolean piercesFreeze;

    /** True if this move can be reflected by Magic Coat. */
    @XmlAttribute
    private Boolean reflectable;

    /** True if this move can be snatched. */
    @XmlAttribute
    private Boolean snatchable;

    /** True if this move is a sound-based move. */
    @XmlAttribute
    private Boolean sound;

    public MoveModel() {
    }

    public MoveModel(int id, PokemonType type, MoveCategory category, int pp, int power, Integer accuracy,
            MoveRange range, MoveTarget targets, Integer critical, Boolean reflectable, Boolean snatchable,
            Boolean sound, Boolean piercesFreeze, Boolean dealsDamage, Boolean ginsengable, int effectID) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.pp = pp;
        this.power = power;
        this.accuracy = accuracy;
        this.range = range;
        this.targets = targets;
        this.critical = critical;
        this.reflectable = reflectable;
        this.snatchable = snatchable;
        this.sound = sound;
        this.piercesFreeze = piercesFreeze;
        this.dealsDamage = dealsDamage;
        this.ginsengable = ginsengable;
        this.effectID = effectID;
    }

    public MoveModel copy() {
        return new MoveModel(id, type, category, pp, power, accuracy, range, targets, critical, reflectable, snatchable,
                sound, piercesFreeze, dealsDamage, ginsengable, effectID);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MoveModel))
            return false;
        MoveModel o = (MoveModel) obj;
        System.out.println(this.pp + ": " + o.pp);
        System.out.println(this.power + ": " + o.power);
        return this.accuracy.equals(o.accuracy) && this.category == o.category && this.critical.equals(o.critical)
                && this.dealsDamage.equals(o.dealsDamage) && this.effectID == o.effectID
                && this.ginsengable.equals(o.ginsengable) && this.id == o.id
                && this.piercesFreeze.equals(o.piercesFreeze) && this.power == o.power && this.pp == o.pp
                && this.range == o.range && this.reflectable.equals(o.reflectable)
                && this.snatchable.equals(o.snatchable) && this.sound.equals(o.sound) && this.targets == o.targets
                && this.type == o.type;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public MoveCategory getCategory() {
        return category;
    }

    public Integer getCritical() {
        return critical;
    }

    public int getEffectID() {
        return effectID;
    }

    public int getID() {
        return id;
    }

    public int getPower() {
        return power;
    }

    public int getPP() {
        return pp;
    }

    public MoveRange getRange() {
        return range;
    }

    public MoveTarget getTargets() {
        return targets;
    }

    public PokemonType getType() {
        return type;
    }

    public Boolean isDealsDamage() {
        return dealsDamage;
    }

    public Boolean isGinsengable() {
        return ginsengable;
    }

    public Boolean isPiercesFreeze() {
        return piercesFreeze;
    }

    public Boolean isReflectable() {
        return reflectable;
    }

    public Boolean isSnatchable() {
        return snatchable;
    }

    public Boolean isSound() {
        return sound;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public void setCategory(MoveCategory category) {
        this.category = category;
    }

    public void setCritical(Integer critical) {
        this.critical = critical;
    }

    public void setDealsDamage(Boolean dealsDamage) {
        this.dealsDamage = dealsDamage;
    }

    public void setEffectID(int effectID) {
        this.effectID = effectID;
    }

    public void setGinsengable(Boolean ginsengable) {
        this.ginsengable = ginsengable;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setPiercesFreeze(Boolean piercesFreeze) {
        this.piercesFreeze = piercesFreeze;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setPP(int pp) {
        this.pp = pp;
    }

    public void setRange(MoveRange range) {
        this.range = range;
    }

    public void setReflectable(Boolean reflectable) {
        this.reflectable = reflectable;
    }

    public void setSnatchable(Boolean snatchable) {
        this.snatchable = snatchable;
    }

    public void setSound(Boolean sound) {
        this.sound = sound;
    }

    public void setTargets(MoveTarget targets) {
        this.targets = targets;
    }

    public void setType(PokemonType type) {
        this.type = type;
    }

    @Override
    public int compareTo(MoveModel o) {
        return Integer.compare(this.id, o.id);
    }

}
