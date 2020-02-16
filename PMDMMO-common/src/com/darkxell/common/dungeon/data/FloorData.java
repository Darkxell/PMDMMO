package com.darkxell.common.dungeon.data;

import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.model.dungeon.FloorDataModel;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registries;

public class FloorData implements Comparable<FloorData> {

    public static final byte NO_SHADOW = 0, NORMAL_SHADOW = 1, DENSE_SHADOW = 2;

    private final FloorDataModel model;

    public FloorData(FloorDataModel model) {
        this.model = model;
    }

    public int baseMoney() {
        return this.model.getBaseMoney();
    }

    public int bossFloor() {
        return this.model.getBossFloor();
    }

    public short buriedItemDensity() {
        return this.model.getBuriedItemDensity();
    }

    public PokemonType camouflageType() {
        return this.model.getCamouflageType();
    }

    @Override
    public int compareTo(FloorData o) {
        return this.model.getFloors().compareTo(o.model.getFloors());
    }

    /** @return A copy of this Data. */
    public FloorData copy() {
        return new FloorData(this.model);
    }

    public int difficulty() {
        return this.model.getDifficulty();
    }

    public FloorSet floors() {
        return this.model.getFloors();
    }

    public FloorDataModel getModel() {
        return this.model;
    }

    public boolean hasCustomTileset() {
        return this.terrainSpriteset() == -1;
    }

    public boolean isBossFloor() {
        return this.model.getBossFloor() != -1;
    }

    public short itemDensity() {
        return this.model.getItemDensity();
    }

    public Layout layout() {
        return Layout.find(this.layoutID());
    }

    public int layoutID() {
        return this.model.getLayout();
    }

    public short monsterHouseChance() {
        return this.model.getMonsterHouseChance();
    }

    public Move naturePower() {
        return Registries.moves().find(this.naturePowerID());
    }

    public int naturePowerID() {
        return this.model.getNaturePower();
    }

    public short pokemonDensity() {
        return this.model.getPokemonDensity();
    }

    public String secretPower() {
        return this.model.getSecretPower();
    }

    public byte shadows() {
        return this.model.getShadows();
    }

    public short shopChance() {
        return this.model.getShopChance();
    }

    public int soundtrack() {
        return this.model.getSoundtrack();
    }

    public int terrainSpriteset() {
        return this.model.getTerrainSpriteset();
    }

    public short trapDensity() {
        return this.model.getTrapDensity();
    }

    public int visionDistance() {
        if (this.shadows() == DENSE_SHADOW)
            return 1;
        if (this.shadows() == NO_SHADOW)
            return 2;
        return 5;
    }

}
