package com.darkxell.common.model.dungeon;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.ai.AI.CustomAI;
import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.util.Pair;

public class DungeonTest {

    private DungeonModel dungeon;
    private DungeonListModel list;

    @Before
    public void before() {
        this.dungeon = new DungeonModel(1, 42, DungeonDirection.DOWN, false, 12, 63, 11, new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), 77, 88);
        this.dungeon.getEncounters()
                .add(new DungeonEncounterModel(11, 111, 1111, CustomAI.NONE, new FloorSet(22, 222)));

        ArrayList<Pair<Integer, Integer>> parts = new ArrayList<>();
        parts.add(new Pair<>(33, 333));
        parts.add(new Pair<>(44, 444));
        this.dungeon.getEncounters().add(new DungeonEncounterModel(11, 111, 1111, CustomAI.NONE,
                new FloorSet(parts, new ArrayList<>(Arrays.asList(2, 3)))));

        this.dungeon.getWeather().add(new DungeonWeatherModel(1, new FloorSet(0, 1)));
        this.dungeon.getWeather().add(new DungeonWeatherModel(72, new FloorSet(73, 74)));

        this.dungeon.getTraps()
                .add(new DungeonTrapGroupModel(new Integer[] { 0, 1 }, new Integer[] { 1, 0 }, new FloorSet(44, 55)));

        this.dungeon.getItems().add(new DungeonItemGroupModel(new Integer[] { 1111 }, new Integer[] { 2222, 3333 }, 420,
                new FloorSet(69, 96)));

        this.dungeon.getFloorData().add(new FloorDataModel(new FloorSet(423, 324), 1, 2, 3, 4, (byte) 5,
                PokemonType.Bug, 6, "7", 8, (short) 9, (short) 10, (short) 11, (short) 12, (short) 13, (short) 14, 15));
        
        this.list = new DungeonListModel();
        this.list.dungeons.add(dungeon);
        this.list.dungeons.add(dungeon);
    }

    @Test
    public void toXmlTest() throws JAXBException {
        Marshaller marshaller = JAXBContext.newInstance(DungeonModel.class).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(this.dungeon, System.out);
    }

    @Test
    public void listToXmlTest() throws JAXBException {
        Marshaller marshaller = JAXBContext.newInstance(DungeonListModel.class).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(this.list, System.out);
    }

}
