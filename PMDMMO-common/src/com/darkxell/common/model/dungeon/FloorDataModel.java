package com.darkxell.common.model.dungeon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.pokemon.PokemonType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FloorDataModel {

    /** The base Money of the Floor. Used to generate piles of Pokedollars. */
    @XmlAttribute(name = "money")
    private int baseMoney;

    /**
     * If not -1, the storypos index required to make this a bossfight. If not matched, this floor will only play a
     * cutscene.
     */
    @XmlAttribute(name = "bossfloor")
    private int bossFloor = -1;

    /** The density of Buried Items. */
    @XmlAttribute(name = "buried")
    private short buriedItemDensity = 1;

    /** The type given to a Pokemon using the move Camouflage. */
    @XmlAttribute(name = "camouflage")
    private PokemonType camouflageType = null;

    /** The Floor's difficulty. */

    @XmlAttribute
    private int difficulty = 1;

    /**
     * Describes which Floors this Data applies to. This should always be a continuous single set of floors [start -
     * end].
     */
    @XmlElement
    private FloorSet floors = null;

    /** The density of Items. */
    @XmlAttribute(name = "items")
    private short itemDensity = 1;

    /** The Layout to use to generate the Floor. */
    @XmlAttribute
    private int layout = 0;

    /** The chance of generating a Monster House in this Floor. */
    @XmlAttribute(name = "mhouse")
    private short monsterHouseChance = 0;

    /** The ID of the move chosen by Nature Power. */
    @XmlAttribute(name = "nature")
    private int naturePower = 709;

    /** The density of Pokemon. */
    @XmlAttribute(name = "pokemon")
    private short pokemonDensity = 1;

    /** The Effect of the Secret Power move. (Strings, will later be replaced with IDs when implementing the move.) */
    @XmlAttribute(name = "secret")
    private String secretPower = "Poison";

    /** The type of shadows for this Floor. See {@link FloorData#NO_SHADOW} */
    @XmlAttribute
    private byte shadows = 0;

    /** The chance of generating a Shop in this Floor. */
    @XmlAttribute(name = "shops")
    private short shopChance = 0;

    /** The soundtrack to play on this Floor. */
    @XmlAttribute
    private int soundtrack = 1;

    /** The Spriteset to use for the terrain. */
    @XmlAttribute(name = "terrain")
    private int terrainSpriteset = 0;

    /** The density of Traps. */
    @XmlAttribute(name = "traps")
    private short trapDensity = 1;

    public FloorDataModel() {
    }

    public FloorDataModel(FloorSet floors, int difficulty, int baseMoney, int layout, int terrainSpriteset,
            byte shadows, PokemonType camouflageType, int naturePower, String secretPower, int soundtrack,
            short shopChance, short monsterHouseChance, short itemDensity, short pokemonDensity, short trapDensity,
            short buriedItemDensity, int bossFloor) {
        this.floors = floors;
        this.difficulty = difficulty;
        this.baseMoney = baseMoney;
        this.layout = layout;
        this.terrainSpriteset = terrainSpriteset;
        this.shadows = shadows;
        this.camouflageType = camouflageType;
        this.naturePower = naturePower;
        this.secretPower = secretPower;
        this.soundtrack = soundtrack;
        this.shopChance = shopChance;
        this.monsterHouseChance = monsterHouseChance;
        this.itemDensity = itemDensity;
        this.pokemonDensity = pokemonDensity;
        this.trapDensity = trapDensity;
        this.buriedItemDensity = buriedItemDensity;
        this.bossFloor = bossFloor;
    }

    public FloorDataModel copy() {
        return new FloorDataModel(floors, difficulty, baseMoney, layout, terrainSpriteset, shadows, camouflageType,
                naturePower, secretPower, soundtrack, shopChance, monsterHouseChance, itemDensity, pokemonDensity,
                trapDensity, buriedItemDensity, bossFloor);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FloorDataModel))
            return false;
        FloorDataModel o = (FloorDataModel) obj;
        return this.floors.equals(o.floors) && this.difficulty == o.difficulty && this.baseMoney == o.baseMoney
                && this.layout == o.layout && this.terrainSpriteset == o.terrainSpriteset && this.shadows == o.shadows
                && this.camouflageType == o.camouflageType && this.naturePower == o.naturePower
                && ((this.secretPower == null && o.secretPower == null)
                        || (this.secretPower != null && this.secretPower.equals(o.secretPower)))
                && this.soundtrack == o.soundtrack && this.shopChance == o.shopChance
                && this.monsterHouseChance == o.monsterHouseChance && this.itemDensity == o.itemDensity
                && this.pokemonDensity == o.pokemonDensity && this.trapDensity == o.trapDensity
                && this.buriedItemDensity == o.buriedItemDensity && this.bossFloor == o.bossFloor;
    }

    public int getBaseMoney() {
        return baseMoney;
    }

    public int getBossFloor() {
        return bossFloor;
    }

    public short getBuriedItemDensity() {
        return buriedItemDensity;
    }

    public PokemonType getCamouflageType() {
        return camouflageType;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public FloorSet getFloors() {
        return floors;
    }

    public short getItemDensity() {
        return itemDensity;
    }

    public int getLayout() {
        return layout;
    }

    public short getMonsterHouseChance() {
        return monsterHouseChance;
    }

    public int getNaturePower() {
        return naturePower;
    }

    public short getPokemonDensity() {
        return pokemonDensity;
    }

    public String getSecretPower() {
        return secretPower;
    }

    public byte getShadows() {
        return shadows;
    }

    public short getShopChance() {
        return shopChance;
    }

    public int getSoundtrack() {
        return soundtrack;
    }

    public int getTerrainSpriteset() {
        return terrainSpriteset;
    }

    public short getTrapDensity() {
        return trapDensity;
    }

    public void setBaseMoney(int baseMoney) {
        this.baseMoney = baseMoney;
    }

    public void setBossFloor(int bossFloor) {
        this.bossFloor = bossFloor;
    }

    public void setBuriedItemDensity(short buriedItemDensity) {
        this.buriedItemDensity = buriedItemDensity;
    }

    public void setCamouflageType(PokemonType camouflageType) {
        this.camouflageType = camouflageType;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setFloors(FloorSet floors) {
        this.floors = floors;
    }

    public void setItemDensity(short itemDensity) {
        this.itemDensity = itemDensity;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public void setMonsterHouseChance(short monsterHouseChance) {
        this.monsterHouseChance = monsterHouseChance;
    }

    public void setNaturePower(int naturePower) {
        this.naturePower = naturePower;
    }

    public void setPokemonDensity(short pokemonDensity) {
        this.pokemonDensity = pokemonDensity;
    }

    public void setSecretPower(String secretPower) {
        this.secretPower = secretPower;
    }

    public void setShadows(byte shadows) {
        this.shadows = shadows;
    }

    public void setShopChance(short shopChance) {
        this.shopChance = shopChance;
    }

    public void setSoundtrack(int soundtrack) {
        this.soundtrack = soundtrack;
    }

    public void setTerrainSpriteset(int terrainSpriteset) {
        this.terrainSpriteset = terrainSpriteset;
    }

    public void setTrapDensity(short trapDensity) {
        this.trapDensity = trapDensity;
    }

}
