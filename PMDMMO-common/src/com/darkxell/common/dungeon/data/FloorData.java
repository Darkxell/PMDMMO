package com.darkxell.common.dungeon.data;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.model.dungeon.FloorDataModel;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.XMLUtils;

public class FloorData implements Comparable<FloorData> {

    public static final byte NO_SHADOW = 0, NORMAL_SHADOW = 1, DENSE_SHADOW = 2;
    public static final String XML_ROOT = "d";

    private final FloorDataModel model;

    public FloorData(Element xml) {
        this.model = new FloorDataModel();
        this.load(xml);
    }

    public FloorData(FloorDataModel model) {
        this.model = model;
    }

    public FloorData(FloorSet floors, int difficulty, int baseMoney, int layout, int terrainSpriteset, byte shadows,
            PokemonType camouflageType, int naturePower, String secretPower, int soundtrack, short shopChance,
            short monsterHouseChance, short itemDensity, short pokemonDensity, short trapDensity,
            short buriedItemDensity, int bossFloor) {
        this.model = new FloorDataModel(floors, difficulty, baseMoney, layout, terrainSpriteset, shadows,
                camouflageType, naturePower, secretPower, soundtrack, shopChance, monsterHouseChance, itemDensity,
                pokemonDensity, trapDensity, buriedItemDensity, bossFloor);
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

    public void load(Element xml) {
        model.setFloors(new FloorSet(xml.getChild(FloorSet.XML_ROOT, xml.getNamespace())));
        model.setDifficulty(XMLUtils.getAttribute(xml, "difficulty", model.getDifficulty()));
        model.setBaseMoney(XMLUtils.getAttribute(xml, "money", model.getBaseMoney()));
        model.setLayout(XMLUtils.getAttribute(xml, "layout", model.getLayout()));
        model.setTerrainSpriteset(XMLUtils.getAttribute(xml, "terrain", model.getTerrainSpriteset()));
        model.setShadows(XMLUtils.getAttribute(xml, "shadows", model.getShadows()));
        model.setCamouflageType(PokemonType.find(XMLUtils.getAttribute(xml, "camouflage",
                model.getCamouflageType() == null ? 0 : model.getCamouflageType().id)));
        model.setNaturePower(XMLUtils.getAttribute(xml, "nature", model.getNaturePower()));
        model.setSecretPower(xml.getAttributeValue("secret"));
        model.setSoundtrack(XMLUtils.getAttribute(xml, "soundtrack", model.getSoundtrack()));
        model.setShopChance(XMLUtils.getAttribute(xml, "shop", model.getShopChance()));
        model.setMonsterHouseChance(XMLUtils.getAttribute(xml, "mhouse", model.getMonsterHouseChance()));
        model.setItemDensity(XMLUtils.getAttribute(xml, "items", model.getItemDensity()));
        model.setPokemonDensity(XMLUtils.getAttribute(xml, "pokemon", model.getPokemonDensity()));
        model.setTrapDensity(XMLUtils.getAttribute(xml, "traps", model.getTrapDensity()));
        model.setBuriedItemDensity(XMLUtils.getAttribute(xml, "buried", model.getBuriedItemDensity()));
        model.setBossFloor(XMLUtils.getAttribute(xml, "bossfloor", model.getBossFloor()));
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

    public Element toXML(FloorData previous) {
        Element xml = new Element(XML_ROOT);
        xml.addContent(this.floors().toXML());
        if (previous != null || this.difficulty() != 0)
            XMLUtils.setAttribute(xml, "difficulty", this.difficulty(), previous != null ? previous.difficulty() : -1);
        if (previous != null || this.baseMoney() != 0)
            XMLUtils.setAttribute(xml, "money", this.baseMoney(), previous != null ? previous.baseMoney() : -1);
        if (previous != null || this.layoutID() != 0)
            XMLUtils.setAttribute(xml, "layout", this.layoutID(), previous != null ? previous.layoutID() : -1);
        if (previous != null || this.terrainSpriteset() != 0)
            XMLUtils.setAttribute(xml, "terrain", this.terrainSpriteset(),
                    previous != null ? previous.terrainSpriteset() : -1);
        if (previous != null || this.shadows() != 0)
            XMLUtils.setAttribute(xml, "shadows", this.shadows(), previous != null ? previous.shadows() : -1);
        if (previous != null || this.camouflageType().id != 0)
            XMLUtils.setAttribute(xml, "camouflage", this.camouflageType().id,
                    previous != null ? previous.camouflageType().id : -1);
        if (previous != null || this.naturePowerID() != 0)
            XMLUtils.setAttribute(xml, "nature", this.naturePowerID(),
                    previous != null ? previous.naturePowerID() : -1);
        if (previous != null || this.secretPower() != null)
            XMLUtils.setAttribute(xml, "secret", this.secretPower(), previous != null ? previous.secretPower() : null);
        if (previous != null || this.soundtrack() != 0)
            XMLUtils.setAttribute(xml, "soundtrack", this.soundtrack(), previous != null ? previous.soundtrack() : 0);
        if (previous != null || this.shopChance() != 0)
            XMLUtils.setAttribute(xml, "shop", this.shopChance(), previous != null ? previous.shopChance() : -1);
        if (previous != null || this.monsterHouseChance() != 0)
            XMLUtils.setAttribute(xml, "mhouse", this.monsterHouseChance(),
                    previous != null ? previous.monsterHouseChance() : -1);
        if (previous != null || this.itemDensity() != 0)
            XMLUtils.setAttribute(xml, "items", this.itemDensity(), previous != null ? previous.itemDensity() : -1);
        if (previous != null || this.pokemonDensity() != 0)
            XMLUtils.setAttribute(xml, "pokemon", this.pokemonDensity(),
                    previous != null ? previous.pokemonDensity() : -1);
        if (previous != null || this.trapDensity() != 0)
            XMLUtils.setAttribute(xml, "traps", this.trapDensity(), previous != null ? previous.trapDensity() : -1);
        if (previous != null || this.buriedItemDensity() != 0)
            XMLUtils.setAttribute(xml, "buried", this.buriedItemDensity(),
                    previous != null ? previous.buriedItemDensity() : -1);
        if (previous != null || this.isBossFloor())
            XMLUtils.setAttribute(xml, "bossfloor", "-1", null);
        return xml;
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
