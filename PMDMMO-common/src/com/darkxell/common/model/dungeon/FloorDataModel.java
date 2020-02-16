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
    private Integer baseMoney;

    /**
     * If not -1, the storypos index required to make this a bossfight. If not matched, this floor will only play a
     * cutscene.
     */
    @XmlAttribute(name = "bossfloor")
    private Integer bossFloor;

    /** The density of Buried Items. */
    @XmlAttribute(name = "buried")
    private Short buriedItemDensity;

    /** The type given to a Pokemon using the move Camouflage. */
    @XmlAttribute(name = "camouflage")
    private PokemonType camouflageType = null;

    /** The Floor's difficulty. */

    @XmlAttribute
    private Integer difficulty;

    /**
     * Describes which Floors this Data applies to. This should always be a continuous single set of floors [start -
     * end].
     */
    @XmlElement
    private FloorSet floors = null;

    /** The density of Items. */
    @XmlAttribute(name = "items")
    private Short itemDensity;

    /** The Layout to use to generate the Floor. */
    @XmlAttribute
    private Integer layout;

    /** The chance of generating a Monster House in this Floor. */
    @XmlAttribute(name = "mhouse")
    private Short monsterHouseChance;

    /** The ID of the move chosen by Nature Power. */
    @XmlAttribute(name = "nature")
    private Integer naturePower;

    /** The density of Pokemon. */
    @XmlAttribute(name = "pokemon")
    private Short pokemonDensity;

    /** The Effect of the Secret Power move. (Strings, will later be replaced with IDs when implementing the move.) */
    @XmlAttribute(name = "secret")
    private String secretPower;

    /** The type of shadows for this Floor. See {@link FloorData#NO_SHADOW} */
    @XmlAttribute
    private Byte shadows;

    /** The chance of generating a Shop in this Floor. */
    @XmlAttribute(name = "shops")
    private Short shopChance;

    /** The soundtrack to play on this Floor. */
    @XmlAttribute
    private Integer soundtrack;

    /** The Spriteset to use for the terrain. */
    @XmlAttribute(name = "terrain")
    private Integer terrainSpriteset;

    /** The density of Traps. */
    @XmlAttribute(name = "traps")
    private Short trapDensity;

    public FloorDataModel() {
    }

    public FloorDataModel(FloorSet floors, Integer difficulty, Integer baseMoney, Integer layout,
            Integer terrainSpriteset, Byte shadows, PokemonType camouflageType, Integer naturePower, String secretPower,
            Integer soundtrack, Short shopChance, Short monsterHouseChance, Short itemDensity, Short pokemonDensity,
            Short trapDensity, Short buriedItemDensity, Integer bossFloor) {
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
        return this.floors.equals(o.floors) && this.difficulty.equals(o.difficulty)
                && this.baseMoney.equals(o.baseMoney) && this.layout.equals(o.layout)
                && this.terrainSpriteset.equals(o.terrainSpriteset) && this.shadows.equals(o.shadows)
                && this.camouflageType.equals(o.camouflageType) && this.naturePower.equals(o.naturePower)
                && ((this.secretPower == null && o.secretPower == null)
                        || (this.secretPower != null && this.secretPower.equals(o.secretPower)))
                && this.soundtrack.equals(o.soundtrack) && this.shopChance.equals(o.shopChance)
                && this.monsterHouseChance.equals(o.monsterHouseChance) && this.itemDensity.equals(o.itemDensity)
                && this.pokemonDensity.equals(o.pokemonDensity) && this.trapDensity.equals(o.trapDensity)
                && this.buriedItemDensity.equals(o.buriedItemDensity) && this.bossFloor.equals(o.bossFloor);
    }

    public Integer getBaseMoney() {
        return baseMoney;
    }

    public Integer getBossFloor() {
        return bossFloor;
    }

    public Short getBuriedItemDensity() {
        return buriedItemDensity;
    }

    public PokemonType getCamouflageType() {
        return camouflageType;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public FloorSet getFloors() {
        return floors;
    }

    public Short getItemDensity() {
        return itemDensity;
    }

    public Integer getLayout() {
        return layout;
    }

    public Short getMonsterHouseChance() {
        return monsterHouseChance;
    }

    public Integer getNaturePower() {
        return naturePower;
    }

    public Short getPokemonDensity() {
        return pokemonDensity;
    }

    public String getSecretPower() {
        return secretPower;
    }

    public Byte getShadows() {
        return shadows;
    }

    public Short getShopChance() {
        return shopChance;
    }

    public Integer getSoundtrack() {
        return soundtrack;
    }

    public Integer getTerrainSpriteset() {
        return terrainSpriteset;
    }

    public Short getTrapDensity() {
        return trapDensity;
    }

    public void setBaseMoney(Integer baseMoney) {
        this.baseMoney = baseMoney;
    }

    public void setBossFloor(Integer bossFloor) {
        this.bossFloor = bossFloor;
    }

    public void setBuriedItemDensity(Short buriedItemDensity) {
        this.buriedItemDensity = buriedItemDensity;
    }

    public void setCamouflageType(PokemonType camouflageType) {
        this.camouflageType = camouflageType;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public void setFloors(FloorSet floors) {
        this.floors = floors;
    }

    public void setItemDensity(Short itemDensity) {
        this.itemDensity = itemDensity;
    }

    public void setLayout(Integer layout) {
        this.layout = layout;
    }

    public void setMonsterHouseChance(Short monsterHouseChance) {
        this.monsterHouseChance = monsterHouseChance;
    }

    public void setNaturePower(Integer naturePower) {
        this.naturePower = naturePower;
    }

    public void setPokemonDensity(Short pokemonDensity) {
        this.pokemonDensity = pokemonDensity;
    }

    public void setSecretPower(String secretPower) {
        this.secretPower = secretPower;
    }

    public void setShadows(Byte shadows) {
        this.shadows = shadows;
    }

    public void setShopChance(Short shopChance) {
        this.shopChance = shopChance;
    }

    public void setSoundtrack(Integer soundtrack) {
        this.soundtrack = soundtrack;
    }

    public void setTerrainSpriteset(Integer terrainSpriteset) {
        this.terrainSpriteset = terrainSpriteset;
    }

    public void setTrapDensity(Short trapDensity) {
        this.trapDensity = trapDensity;
    }

}
