package com.darkxell.common.dungeon.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.jdom2.Element;

import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffects;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.model.dungeon.DungeonModel;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registrable;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.XMLUtils;
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

    private final ArrayList<DungeonEncounter> encounters = new ArrayList<>();

    private final DungeonModel model;

    public Dungeon(Element xml) {
        this.model = new DungeonModel();
        this.model.setId(Integer.parseInt(xml.getAttributeValue("id")));
        this.model.setFloorCount(Integer.parseInt(xml.getAttributeValue("floors")));
        this.model.setDirection(DungeonDirection.find(XMLUtils.getAttribute(xml, "down", DungeonDirection.UP.value())));
        this.model.setRecruitsAllowed(XMLUtils.getAttribute(xml, "recruits", true));
        this.model.setTimeLimit(XMLUtils.getAttribute(xml, "limit", 2000));
        this.model.setLinkedTo(XMLUtils.getAttribute(xml, "linked", -1));
        this.model.setStickyChance(XMLUtils.getAttribute(xml, "sticky", 0));
        this.model.setMapx(XMLUtils.getAttribute(xml, "mapx", -260) + 260);
        this.model.setMapy(XMLUtils.getAttribute(xml, "mapy", -140) + 140);

        for (Element pokemon : xml.getChild("encounters", xml.getNamespace()).getChildren(DungeonEncounter.XML_ROOT,
                xml.getNamespace()))
        {
            DungeonEncounter encounter = new DungeonEncounter(pokemon);
            this.encounters.add(encounter);
            this.model.getEncounters().add(encounter.getModel());
        }

        for (Element item : xml.getChild("items", xml.getNamespace()).getChildren(DungeonItemGroup.XML_ROOT,
                xml.getNamespace()))
            this.model.getItems().add(new DungeonItemGroup(item));

        if (xml.getChild("shops", xml.getNamespace()) != null)
            for (Element item : xml.getChild("shops", xml.getNamespace()).getChildren(DungeonItemGroup.XML_ROOT,
                    xml.getNamespace()))
                this.model.getShopItems().add(new DungeonItemGroup(item));

        if (xml.getChild("buried", xml.getNamespace()) != null)
            for (Element item : xml.getChild("buried", xml.getNamespace()).getChildren(DungeonItemGroup.XML_ROOT,
                    xml.getNamespace()))
                this.model.getBuriedItems().add(new DungeonItemGroup(item));

        if (xml.getChild("traps", xml.getNamespace()) == null)
            this.model.getTraps().add(new DungeonTrapGroup(new int[] { TrapRegistry.WONDER_TILE.id }, new int[] { 100 },
                    new FloorSet(1, this.model.getFloorCount())));
        else
            for (Element trap : xml.getChild("traps", xml.getNamespace()).getChildren(DungeonTrapGroup.XML_ROOT,
                    xml.getNamespace()))
                this.model.getTraps().add(new DungeonTrapGroup(trap));

        for (Element data : xml.getChild("data", xml.getNamespace()).getChildren(FloorData.XML_ROOT,
                xml.getNamespace())) {
            FloorData d = null;
            if (this.model.getFloorData().isEmpty())
                d = new FloorData(data);
            else {
                d = this.model.getFloorData().get(this.model.getFloorData().size() - 1).copy();
                d.load(data);
            }
            this.model.getFloorData().add(d);
        }

        if (xml.getChild("weather", xml.getNamespace()) != null)
            for (Element data : xml.getChild("weather", xml.getNamespace()).getChildren("w", xml.getNamespace()))
                this.model.getWeather().put(Integer.parseInt(data.getAttributeValue("id")),
                        new FloorSet(data.getChild(FloorSet.XML_ROOT, xml.getNamespace())));
    }

    public Dungeon(int id, int floorCount, DungeonDirection direction, boolean recruits, int timeLimit,
            int stickyChance, int linkedTo, ArrayList<DungeonEncounter> encounters, ArrayList<DungeonItemGroup> items,
            ArrayList<DungeonItemGroup> shopItems, ArrayList<DungeonItemGroup> buriedItems,
            ArrayList<DungeonTrapGroup> traps, ArrayList<FloorData> floorData, HashMap<Integer, FloorSet> weather,
            int mapx, int mapy) {
        this.encounters.addAll(encounters);
        this.model = new DungeonModel(id, floorCount, direction, recruits, timeLimit, stickyChance, linkedTo,
                new ArrayList<>(), items, shopItems, buriedItems, traps, floorData, weather, mapx, mapy);
        this.encounters.forEach(e -> this.model.getEncounters().add(e.getModel()));
    }

    public ArrayList<DungeonItemGroup> buriedItems(int floor) {
        ArrayList<DungeonItemGroup> items = new ArrayList<>();
        for (DungeonItemGroup itemgroup : this.buriedItemsData())
            if (itemgroup.floors.contains(floor))
                items.add(itemgroup);
        return items;
    }

    public ArrayList<DungeonItemGroup> buriedItemsData() {
        return new ArrayList<>(this.model.getBuriedItems());
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
        return this.model.getFloorData();
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
            if (item.floors.contains(floor))
                items.add(item);
        return items;
    }

    public ArrayList<DungeonItemGroup> itemsData() {
        return new ArrayList<>(this.model.getItems());
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
            if (!allowMoney && i.items.length == 1 && i.items[0] == Item.POKEDOLLARS)
                return true;
            return !i.floors.contains(floor);
        });
        if (candidates.size() == 0)
            return null;
        ItemStack stack = candidates.get(random.nextInt(candidates.size())).generate(random, allowMoney);

        int quantity = 0;
        if (stack.item().effect() == ItemEffects.Pokedollars)
            quantity = this.getMoneyQuantity(random, floor);
        else if (stack.item().isStackable)
            quantity = RandomUtil.nextGaussian(10, 7, random);
        if (quantity <= 0)
            quantity = 1;
        stack.setQuantity(quantity);

        return stack;
    }

    public ArrayList<DungeonItemGroup> shopItemsData() {
        return new ArrayList<>(this.shopItemsData());
    }

    @Override
    public String toString() {
        return this.getID() + "- " + this.name().toString();
    }

    public Element toXML() {
        Element root = new Element(XML_ROOT);
        root.setAttribute("id", Integer.toString(this.getID()));
        root.setAttribute("floors", Integer.toString(this.getFloorCount()));
        XMLUtils.setAttribute(root, "mapx", this.getMapX() - 260, -260);
        XMLUtils.setAttribute(root, "mapy", this.getMapY() - 140, -140);
        XMLUtils.setAttribute(root, "down", this.getDirection().value(), DungeonDirection.UP.value());
        XMLUtils.setAttribute(root, "recruits", this.isRecruitsAllowed(), true);
        XMLUtils.setAttribute(root, "limit", this.getTimeLimit(), 2000);
        XMLUtils.setAttribute(root, "sticky", this.getStickyChance(), 0);
        XMLUtils.setAttribute(root, "linked", this.getLinkedTo(), -1);

        Element pokemon = new Element("encounters");
        for (DungeonEncounter poke : this.encountersData())
            pokemon.addContent(poke.toXML());
        root.addContent(pokemon);

        Element items = new Element("items");
        for (DungeonItemGroup item : this.itemsData())
            items.addContent(item.toXML());
        root.addContent(items);

        if (!this.shopItemsData().isEmpty()) {
            Element shops = new Element("shops");
            for (DungeonItemGroup item : this.shopItemsData())
                shops.addContent(item.toXML());
            root.addContent(shops);
        }

        if (!this.buriedItemsData().isEmpty()) {
            Element buried = new Element("buried");
            for (DungeonItemGroup item : this.buriedItemsData())
                buried.addContent(item.toXML());
            root.addContent(buried);
        }

        if (!this.trapsData().isEmpty() && !(this.trapsData().size() == 1 && this.trapsData().get(0).ids.length == 1
                && this.trapsData().get(0).ids[0] == TrapRegistry.WONDER_TILE.id)) {
            Element traps = new Element("traps");
            for (DungeonTrapGroup trap : this.trapsData())
                traps.addContent(trap.toXML());
            root.addContent(traps);
        }

        Element data = new Element("data");
        for (FloorData d : this.getFloorData())
            data.addContent(d.toXML(this.getFloorData().indexOf(d) == 0 ? null
                    : this.getFloorData().get(this.getFloorData().indexOf(d) - 1)));
        root.addContent(data);

        if (!this.weatherData().isEmpty()) {
            Element weather = new Element("weather");
            for (Integer id : this.weatherData().keySet())
                weather.addContent(new Element("w").setAttribute("id", Integer.toString(id))
                        .addContent(this.weatherData().get(id).toXML()));
            root.addContent(weather);
        }

        return root;
    }

    /** @return The traps found on the input floor. First are Trap IDs, second are Trap weights. */
    public Pair<ArrayList<Integer>, ArrayList<Integer>> traps(int floor) {
        ArrayList<Integer> traps = new ArrayList<>(), weights = new ArrayList<>();
        for (DungeonTrapGroup t : this.trapsData())
            if (t.floors.contains(floor))
                for (int i = 0; i < t.ids.length; ++i) {
                    traps.add(t.ids[i]);
                    weights.add(t.chances[i]);
                }
        return new Pair<>(traps, weights);
    }

    public ArrayList<DungeonTrapGroup> trapsData() {
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

        for (Integer id : this.weatherData().keySet())
            if (id == Weather.random)
                candidates.add(Weather.random(random));
            else if (this.weatherData().get(id).contains(floor))
                candidates.add(Weather.find(id));

        if (candidates.isEmpty())
            return w;
        return RandomUtil.random(candidates, random);
    }

    public HashMap<Integer, FloorSet> weatherData() {
        return new HashMap<>(this.model.getWeather());
    }
}
