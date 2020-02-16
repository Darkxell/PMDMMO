package com.darkxell.common.dungeon.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffects;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.model.dungeon.DungeonModel;
import com.darkxell.common.model.dungeon.DungeonTrapGroupModel;
import com.darkxell.common.model.dungeon.DungeonWeatherModel;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registrable;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.Weather;

/** Describes a Dungeon: floors, Pokemon, items... */
public class Dungeon implements Registrable<Dungeon> {

    public enum DungeonDirection {
        DOWN,
        UP;

        public static DungeonDirection find(boolean value) {
            if (value)
                return DOWN;
            return UP;
        }

        public boolean value() {
            return this == DOWN;
        }
    }

    public static final String XML_ROOT = "dungeon";

    private final ArrayList<DungeonItemGroup> buriedItems = new ArrayList<>();
    private final ArrayList<DungeonEncounter> encounters = new ArrayList<>();
    private final ArrayList<FloorData> floorData = new ArrayList<>();
    private final ArrayList<DungeonItemGroup> items = new ArrayList<>();
    private final DungeonModel model;
    private final ArrayList<DungeonItemGroup> shopItems = new ArrayList<>();

    public Dungeon(DungeonModel model) {
        this.model = model;
        this.model.getEncounters().forEach(e -> this.encounters.add(new DungeonEncounter(e)));
        this.model.getItems().forEach(i -> this.items.add(new DungeonItemGroup(i)));
        this.model.getShopItems().forEach(i -> this.shopItems.add(new DungeonItemGroup(i)));
        this.model.getBuriedItems().forEach(i -> this.buriedItems.add(new DungeonItemGroup(i)));
        this.model.getFloorData().forEach(f -> this.floorData.add(new FloorData(f)));
    }

    public Dungeon(int id, int floorCount, DungeonDirection direction, boolean recruits, int timeLimit,
            int stickyChance, int linkedTo, ArrayList<DungeonEncounter> encounters, ArrayList<DungeonItemGroup> items,
            ArrayList<DungeonItemGroup> shopItems, ArrayList<DungeonItemGroup> buriedItems,
            ArrayList<DungeonTrapGroupModel> traps, ArrayList<FloorData> floorData,
            ArrayList<DungeonWeatherModel> weather, int mapx, int mapy) {
        this.encounters.addAll(encounters);
        this.buriedItems.addAll(buriedItems);
        this.items.addAll(items);
        this.shopItems.addAll(shopItems);
        this.floorData.addAll(floorData);
        this.model = new DungeonModel(id, floorCount, direction, recruits, timeLimit, stickyChance, linkedTo,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), traps, new ArrayList<>(),
                weather, mapx, mapy);
        this.encounters.forEach(e -> this.model.getEncounters().add(e.getModel()));
        this.buriedItems.forEach(i -> this.model.getBuriedItems().add(i.getModel()));
        this.items.forEach(i -> this.model.getItems().add(i.getModel()));
        this.shopItems.forEach(i -> this.model.getShopItems().add(i.getModel()));
        this.floorData.forEach(d -> this.model.getFloorData().add(d.getModel()));
    }

    public ArrayList<DungeonItemGroup> buriedItems(int floor) {
        ArrayList<DungeonItemGroup> items = new ArrayList<>();
        for (DungeonItemGroup itemgroup : this.buriedItemsData())
            if (itemgroup.getFloors().contains(floor))
                items.add(itemgroup);
        return items;
    }

    public ArrayList<DungeonItemGroup> buriedItemsData() {
        return new ArrayList<>(this.buriedItems);
    }

    @Override
    public int compareTo(Dungeon o) {
        return Integer.compare(this.getID(), o.getID());
    }

    public ArrayList<DungeonEncounter> encountersData() {
        return new ArrayList<>(this.encounters);
    }

    /** @return The Data of the input floor. */
    public FloorData getData(int floor) {
        for (FloorData data : this.getFloorData())
            if (data.floors().contains(floor))
                return data;
        return this.getFloorData().get(0);
    }

    public DungeonDirection getDirection() {
        return this.model.getDirection();
    }

    public int getFloorCount() {
        return this.model.getFloorCount();
    }

    public ArrayList<FloorData> getFloorData() {
        return new ArrayList<>(this.floorData);
    }

    public int getID() {
        return this.model.getId();
    }

    public int getLinkedTo() {
        return this.model.getLinkedTo();
    }

    public int getMapX() {
        return this.model.getMapX();
    }

    public int getMapY() {
        return this.model.getMapY();
    }

    public DungeonModel getModel() {
        return this.model;
    }

    /** @return A random quantity of Pokedollars for a single stack. */
    public int getMoneyQuantity(Random random, int floor) {
        final int[] moneyTable = new int[] { 4, 6, 10, 14, 22, 26, 37, 38, 46, 58, 62, 74, 82, 86, 94, 106, 118, 122,
                134, 142, 146, 158, 166, 178, 194, 202, 206, 214, 218, 226, 254, 262, 274, 278, 298, 302, 314, 326, 334,
                346, 358, 362, 382, 386, 394, 398, 422, 454, 458, 466, 478, 482, 502, 514, 526, 538, 542, 554, 562, 566,
                586, 614, 622, 626, 634, 662, 674, 694, 698, 706, 718, 734, 746, 758, 768, 778, 794, 802, 818, 838, 842,
                862, 866, 878, 886, 898, 914, 922, 926, 934, 958, 974, 982, 998, 1000, 1100, 1300, 1500, 20000 };

        int r = random.nextInt(99) + 1;
        int value = 0;
        int max = this.getData(floor).baseMoney() * 40;

        for (int i = 0; i < 200; ++i) {
            value = moneyTable[r - 1];
            if (value <= max)
                return value;
            else
                r /= 2;
        }
        return moneyTable[0];
    }

    public HashSet<PokemonSpecies> getRecruitablePokemon() {
        HashSet<PokemonSpecies> recruitable = new HashSet<>();
        if (this.isRecruitsAllowed())
            for (DungeonEncounter encounter : this.encountersData())
                recruitable.add(encounter.pokemon());
        return recruitable;
    }

    public int getStickyChance() {
        return this.model.getStickyChance();
    }

    public int getTimeLimit() {
        return this.model.getTimeLimit();
    }

    public ArrayList<DungeonWeatherModel> getWeather() {
        return new ArrayList<>(this.model.getWeather());
    }

    /**
     * @return true if the input species is recruitable in this dungeon. Faster method than getRecruitablePokemon.
     */
    public boolean isRecruitable(PokemonSpecies species) {
        if (this.isRecruitsAllowed())
            for (DungeonEncounter encounter : this.encountersData())
                if (encounter.pokemon() == species)
                    return true;
        return false;
    }

    public boolean isRecruitsAllowed() {
        return this.model.isRecruitsAllowed();
    }

    public ArrayList<DungeonItemGroup> items(int floor) {
        ArrayList<DungeonItemGroup> items = new ArrayList<>();
        for (DungeonItemGroup item : this.itemsData())
            if (item.getFloors().contains(floor))
                items.add(item);
        return items;
    }

    public ArrayList<DungeonItemGroup> itemsData() {
        return new ArrayList<>(this.items);
    }

    /** @return This Dungeon's name. */
    public Message name() {
        return new Message("dungeon." + this.getID());
    }

    /** @return A new instance of this Dungeon. */
    public DungeonExploration newInstance(long seed) {
        return new DungeonExploration(this.getID(), seed);
    }

    public ArrayList<DungeonEncounter> pokemon(int floor) {
        ArrayList<DungeonEncounter> encounters = new ArrayList<>(this.encountersData());
        encounters.removeIf((DungeonEncounter candidate) -> {
            return !candidate.getFloors().contains(floor);
        });
        return encounters;
    }

    public DungeonEncounter randomEncounter(Random random, int floor) {
        ArrayList<DungeonEncounter> candidates = this.pokemon(floor);
        return RandomUtil.random(candidates, random);
    }

    /**
     * @return A random Item for the input floor. May return <code>null</code> if there are no items on the floor, or if
     *         they don't meet requirements.
     */
    public ItemStack randomItem(Random random, int floor, boolean allowMoney) {
        ArrayList<DungeonItemGroup> candidates = new ArrayList<>(this.itemsData());
        candidates.removeIf(i -> {
            if (!allowMoney && i.items().length == 1 && i.getItemIDs()[0] == Item.POKEDOLLARS)
                return true;
            return !i.getFloors().contains(floor);
        });
        if (candidates.size() == 0)
            return null;
        ItemStack stack = candidates.get(random.nextInt(candidates.size())).generate(random, allowMoney);

        int quantity = 0;
        if (stack.item().effect() == ItemEffects.Pokedollars)
            quantity = this.getMoneyQuantity(random, floor);
        else if (stack.item().isStackable())
            quantity = RandomUtil.nextGaussian(10, 7, random);
        if (quantity <= 0)
            quantity = 1;
        stack.setQuantity(quantity);

        return stack;
    }

    public ArrayList<DungeonItemGroup> shopItemsData() {
        return new ArrayList<>(this.shopItems);
    }

    @Override
    public String toString() {
        return this.getID() + "- " + this.name().toString();
    }

    /** @return The traps found on the input floor. First are Trap IDs, second are Trap weights. */
    public Pair<ArrayList<Integer>, ArrayList<Integer>> traps(int floor) {
        ArrayList<Integer> traps = new ArrayList<>(), weights = new ArrayList<>();
        for (DungeonTrapGroupModel t : this.trapsData())
            if (t.getFloors().contains(floor))
                for (int i = 0; i < t.getIds().length; ++i) {
                    traps.add(t.getIds()[i]);
                    weights.add(t.getChances()[i]);
                }
        return new Pair<>(traps, weights);
    }

    public ArrayList<DungeonTrapGroupModel> trapsData() {
        return new ArrayList<>(this.model.getTraps());
    }

    /**
     * @param  floor  - A Floor ID.
     * @param  random - A Random Number Generator.
     * @return        The prevailing Weather for the input Floor.
     */
    public Weather weather(int floor, Random random) {
        Weather w = Weather.CLEAR;

        ArrayList<Weather> candidates = new ArrayList<>();

        for (DungeonWeatherModel weather : this.getWeather())
            if (weather.getID() == Weather.random)
                candidates.add(Weather.random(random));
            else if (weather.getFloors().contains(floor))
                candidates.add(Weather.find(weather.getID()));

        if (candidates.isEmpty())
            return w;
        return RandomUtil.random(candidates, random);
    }
}
