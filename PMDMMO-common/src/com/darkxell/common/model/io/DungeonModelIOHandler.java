package com.darkxell.common.model.io;

import java.util.ArrayList;

import com.darkxell.common.ai.AI.CustomAI;
import com.darkxell.common.model.dungeon.DungeonEncounterModel;
import com.darkxell.common.model.dungeon.DungeonItemGroupModel;
import com.darkxell.common.model.dungeon.DungeonListModel;
import com.darkxell.common.model.dungeon.DungeonModel;
import com.darkxell.common.model.dungeon.FloorDataModel;

public class DungeonModelIOHandler extends ModelIOHandler<DungeonListModel> {

    public DungeonModelIOHandler() {
        super(DungeonListModel.class);
    }

    @Override
    protected DungeonListModel handleAfterImport(DungeonListModel object) {

        for (DungeonModel dungeon : object.dungeons) {
            ArrayList<FloorDataModel> floorData = dungeon.getFloorData();
            this.replicateMissingValuesFromPreviousFloor(this.defaultFloorData(), floorData.get(0));
            for (int i = 1; i < floorData.size(); ++i) {
                this.replicateMissingValuesFromPreviousFloor(floorData.get(i - 1), floorData.get(i));
            }

            for (DungeonEncounterModel encounter : dungeon.getEncounters()) {
                if (encounter.getAiType() == null) {
                    encounter.setAiType(CustomAI.NONE);
                }
                if (encounter.getWeight() == null) {
                    encounter.setWeight(1);
                }
            }

            for (DungeonItemGroupModel itemGroup : dungeon.getItems()) {
                if (itemGroup.getWeight() == null) {
                    itemGroup.setWeight(1);
                }
            }
        }

        return super.handleAfterImport(object);
    }

    @Override
    protected DungeonListModel handleBeforeExport(DungeonListModel object) {

        object = object.copy();
        for (DungeonModel dungeon : object.dungeons) {
            ArrayList<FloorDataModel> floorData = dungeon.getFloorData();
            FloorDataModel currentData = this.defaultFloorData(), nextData;
            for (int i = 0; i < floorData.size(); ++i) {
                nextData = floorData.get(i).copy();
                this.ignoreRedundantValuesInFloorData(currentData, floorData.get(i));
                currentData = nextData;
            }

            for (DungeonEncounterModel encounter : dungeon.getEncounters()) {
                if (encounter.getAiType() == CustomAI.NONE) {
                    encounter.setAiType(null);
                }
                if (encounter.getWeight().equals(1)) {
                    encounter.setWeight(null);
                }
            }

            for (DungeonItemGroupModel itemGroup : dungeon.getItems()) {
                if (itemGroup.getWeight().equals(1)) {
                    itemGroup.setWeight(null);
                }
            }
        }

        return object;
    }

    private FloorDataModel defaultFloorData() {
        return new FloorDataModel(null, 1, 0, 0, 0, (byte) 0, null, 709, "Poison", 1, (short) 0, (short) 0, (short) 1,
                (short) 1, (short) 1, (short) 0, -1);
    }

    private void ignoreRedundantValuesInFloorData(FloorDataModel currentData, FloorDataModel targetFloorData) {
        if (targetFloorData.getBaseMoney().equals(currentData.getBaseMoney()))
            targetFloorData.setBaseMoney(null);

        if (targetFloorData.getBossFloor().equals(currentData.getBossFloor()))
            targetFloorData.setBossFloor(null);

        if (targetFloorData.getBuriedItemDensity().equals(currentData.getBuriedItemDensity()))
            targetFloorData.setBuriedItemDensity(null);

        if (targetFloorData.getCamouflageType().equals(currentData.getCamouflageType()))
            targetFloorData.setCamouflageType(null);

        if (targetFloorData.getDifficulty().equals(currentData.getDifficulty()))
            targetFloorData.setDifficulty(null);

        if (targetFloorData.getItemDensity().equals(currentData.getItemDensity()))
            targetFloorData.setItemDensity(null);

        if (targetFloorData.getLayout().equals(currentData.getLayout()))
            targetFloorData.setLayout(null);

        if (targetFloorData.getMonsterHouseChance().equals(currentData.getMonsterHouseChance()))
            targetFloorData.setMonsterHouseChance(null);

        if (targetFloorData.getNaturePower().equals(currentData.getNaturePower()))
            targetFloorData.setNaturePower(null);

        if (targetFloorData.getPokemonDensity().equals(currentData.getPokemonDensity()))
            targetFloorData.setPokemonDensity(null);

        if (targetFloorData.getSecretPower().equals(currentData.getSecretPower()))
            targetFloorData.setSecretPower(null);

        if (targetFloorData.getShadows().equals(currentData.getShadows()))
            targetFloorData.setShadows(null);

        if (targetFloorData.getShopChance().equals(currentData.getShopChance()))
            targetFloorData.setShopChance(null);

        if (targetFloorData.getSoundtrack().equals(currentData.getSoundtrack()))
            targetFloorData.setSoundtrack(null);

        if (targetFloorData.getTerrainSpriteset().equals(currentData.getTerrainSpriteset()))
            targetFloorData.setTerrainSpriteset(null);

        if (targetFloorData.getTrapDensity().equals(currentData.getTrapDensity()))
            targetFloorData.setTrapDensity(null);
    }

    private void replicateMissingValuesFromPreviousFloor(FloorDataModel previous, FloorDataModel current) {
        if (current.getBaseMoney() == null)
            current.setBaseMoney(previous.getBaseMoney());

        if (current.getBossFloor() == null)
            current.setBossFloor(previous.getBossFloor());

        if (current.getBuriedItemDensity() == null)
            current.setBuriedItemDensity(previous.getBuriedItemDensity());

        if (current.getCamouflageType() == null)
            current.setCamouflageType(previous.getCamouflageType());

        if (current.getDifficulty() == null)
            current.setDifficulty(previous.getDifficulty());

        if (current.getItemDensity() == null)
            current.setItemDensity(previous.getItemDensity());

        if (current.getLayout() == null)
            current.setLayout(previous.getLayout());

        if (current.getMonsterHouseChance() == null)
            current.setMonsterHouseChance(previous.getMonsterHouseChance());

        if (current.getNaturePower() == null)
            current.setNaturePower(previous.getNaturePower());

        if (current.getPokemonDensity() == null)
            current.setPokemonDensity(previous.getPokemonDensity());

        if (current.getSecretPower() == null)
            current.setSecretPower(previous.getSecretPower());

        if (current.getShadows() == null)
            current.setShadows(previous.getShadows());

        if (current.getShopChance() == null)
            current.setShopChance(previous.getShopChance());

        if (current.getSoundtrack() == null)
            current.setSoundtrack(previous.getSoundtrack());

        if (current.getTerrainSpriteset() == null)
            current.setTerrainSpriteset(previous.getTerrainSpriteset());

        if (current.getTrapDensity() == null)
            current.setTrapDensity(previous.getTrapDensity());
    }

}
