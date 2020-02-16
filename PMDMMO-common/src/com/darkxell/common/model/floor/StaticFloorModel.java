package com.darkxell.common.model.floor;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "floor")
@XmlAccessorType(XmlAccessType.FIELD)
public class StaticFloorModel {

    @XmlElement
    private String cuscenein;

    @XmlElement
    private String cusceneout;

    @XmlElement
    private Integer cuscenestorypos;

    @XmlElementRef
    @XmlElementWrapper(name = "rooms")
    private ArrayList<IStaticFloorRoom> rooms = new ArrayList<>();

    @XmlElement(name = "row")
    @XmlElementWrapper(name = "tiles")
    private ArrayList<String> tileRows = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "items")
    private ArrayList<StaticFloorItemModel> items = new ArrayList<>();

    @XmlElement(name = "pokemon")
    @XmlElementWrapper(name = "pokemons")
    private ArrayList<StaticFloorPokemonModel> pokemon = new ArrayList<>();

    @XmlElement(name = "spawn")
    private StaticFloorSpawnModel spawn;

    @XmlElement(name = "trap")
    @XmlElementWrapper(name = "traps")
    private ArrayList<StaticFloorTrapModel> traps = new ArrayList<>();

    public String getCuscenein() {
        return cuscenein;
    }

    public String getCusceneout() {
        return cusceneout;
    }

    public Integer getCuscenestorypos() {
        return cuscenestorypos;
    }

    public ArrayList<IStaticFloorRoom> getRooms() {
        return new ArrayList<>(rooms);
    }

    public ArrayList<String> getTileRows() {
        return new ArrayList<>(tileRows);
    }

    public ArrayList<StaticFloorItemModel> getItems() {
        return new ArrayList<>(items);
    }

    public ArrayList<StaticFloorPokemonModel> getPokemon() {
        return new ArrayList<>(pokemon);
    }

    public StaticFloorSpawnModel getSpawn() {
        return spawn;
    }

    public ArrayList<StaticFloorTrapModel> getTraps() {
        return new ArrayList<>(traps);
    }

}
