package com.darkxell.common.zones;

import java.util.HashMap;

import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

public enum FriendArea {

    AGED_CHAMBER_1(FreezoneInfo.FRIEND_AGEDCHAMBER1, 100, 5500),
    AGED_CHAMBER_2(FreezoneInfo.FRIEND_AGEDCHAMBER2, 100, 5500),
    ANCIENT_RELIC(FreezoneInfo.FRIEND_ANCIENTRELIC, 100, 8000),
    BEAU_PLAINS(FreezoneInfo.FRIEND_BEAUPLAINS, 0, 600),
    BOULDER_CAVE(FreezoneInfo.FRIEND_BOULDERCAVE),
    BOUNTIFUL_SEA(FreezoneInfo.FRIEND_BOUNTIFULSEA, 100, 5500),
    CRATER(FreezoneInfo.FRIEND_CRATER, 100, 7500),
    CRYPTIC_CAVE(FreezoneInfo.FRIEND_CRYPTICCAVE, 100, 6500),
    DARKNESS_RIDGE(FreezoneInfo.FRIEND_DARKNESSRIDGE),
    DECREPIT_LAB(FreezoneInfo.FRIEND_DECREPITLAB, 0, 1000),
    DEEP_SEA_CURRENT(FreezoneInfo.FRIEND_DEEPSEACURRENT),
    DEEP_SEA_FLOOR(FreezoneInfo.FRIEND_DEEPSEAFLOOR, 100, 5500),
    DRAGON_CAVE(FreezoneInfo.FRIEND_DRAGONCAVE),
    ECHO_CAVE(FreezoneInfo.FRIEND_ECHOCAVE, 100, 7500),
    ENCLOSED_ISLAND(FreezoneInfo.FRIEND_ENCLOSEDISLAND),
    ENERGETIC_FOREST(FreezoneInfo.FRIEND_ENERGETICFOREST, 100, 5000),
    FINAL_ISLAND(FreezoneInfo.FRIEND_FINALISLAND, 100, 8500),
    FLYAWAY_FOREST(FreezoneInfo.FRIEND_FLYAWAYFOREST, 0, 550),
    FRIGID_CAVERN(FreezoneInfo.FRIEND_FRIGIDCAVERN, 100, 9000),
    FURNACE_DESERT(FreezoneInfo.FRIEND_FURNACEDESERT, 100, 8500),
    HEALING_FOREST(FreezoneInfo.FRIEND_HEALINGFOREST),
    ICE_FLOE_BEACH(FreezoneInfo.FRIEND_ICEFLOEBEACH, 100, 9500),
    JUNGLE(FreezoneInfo.FRIEND_JUNGLE, 0, 800),
    LEGENDARY_ISLAND(FreezoneInfo.FRIEND_LEGENDARYISLAND),
    MAGNETIC_QUARRY(FreezoneInfo.FRIEND_MAGNETICQUARRY, 0, 1000),
    MIST_RISE_FOREST(FreezoneInfo.FRIEND_MISTRISEFOREST),
    MT_CLEFT(FreezoneInfo.FRIEND_MTCLEFT, 100, 5000),
    MT_DEEPGREEN(FreezoneInfo.FRIEND_MTDEEPGREEN, 0, 130),
    MT_DISCIPLINE(FreezoneInfo.FRIEND_MTDISCIPLINE, 0, 1200),
    MT_MOONVIEW(FreezoneInfo.FRIEND_MTMOONVIEW),
    MUSHROOM_FOREST(FreezoneInfo.FRIEND_MUSHROOMFOREST, 0, 500),
    MYSTIC_LAKE(FreezoneInfo.FRIEND_MYSTICLAKE, 100, 2500),
    OVERGROWN_FOREST(FreezoneInfo.FRIEND_OVERGROWNFOREST, 0, 600),
    PEANUT_SWAMP(FreezoneInfo.FRIEND_PEANUTSWAMP, 0, 2500),
    POISON_SWAMP(FreezoneInfo.FRIEND_POISONSWAMP, 100, 8500),
    POWER_PLANT(FreezoneInfo.FRIEND_POWERPLANT),
    RAINBOW_PEAK(FreezoneInfo.FRIEND_RAINBOWPEAK, 100, 6500),
    RAVAGED_FIELD(FreezoneInfo.FRIEND_RAVAGEDFIELD, 0, 1000),
    RUBADUB_RIVER(FreezoneInfo.FRIEND_RUBADUBRIVER, 0, 500),
    SACRED_FIELD(FreezoneInfo.FRIEND_SACREDFIELD, 100, 6500),
    SAFARI(FreezoneInfo.FRIEND_SAFARI, 0, 700),
    SCORCHED_PLAINS(FreezoneInfo.FRIEND_SCORCHEDPLAINS, 100, 5000),
    SEAFLOOR_CAVE(FreezoneInfo.FRIEND_SEAFLOORCAVE),
    SECRETIVE_FOREST(FreezoneInfo.FRIEND_SECRETIVEFOREST, 100, 6500),
    SERENE_SEA(FreezoneInfo.FRIEND_SERENESEA, 100, 8000),
    SHALLOW_BEACH(FreezoneInfo.FRIEND_SHALLOWBEACH, 100, 5000),
    SKY_BLUE_PLAINS(FreezoneInfo.FRIEND_SKYBLUEPLAINS),
    SOUTHERN_ISLAND(FreezoneInfo.FRIEND_SOUTHERNISLAND, 100, 9500),
    STRATOS_LOOKOUT(FreezoneInfo.FRIEND_STRATOSLOOKOUT),
    TADPOLE_POND(FreezoneInfo.FRIEND_TADPOLEPOND, 0, 500),
    THUNDER_MEADOW(FreezoneInfo.FRIEND_THUNDERMEADOW, 0, 1000),
    TRANSFORM_FOREST(FreezoneInfo.FRIEND_TRANSFORMFOREST, 0, 500),
    TREASURE_SEA(FreezoneInfo.FRIEND_TREASURESEA, 100, 4500),
    TURTLESHEEL_POND(FreezoneInfo.FRIEND_TURTLESHELLPOND, 100, 5500),
    VOLCANIC_PIT(FreezoneInfo.FRIEND_VOLCANICPIT),
    WATERFALL_LAKE(FreezoneInfo.FRIEND_WATERFALLLAKE, 100, 3500),
    WILD_PLAINS(FreezoneInfo.FRIEND_WILDPLAINS);

    private static HashMap<String, FriendArea> lookup = new HashMap<>();

    static {
        for (FriendArea a : values())
            lookup.put(a.freezone.id, a);
    }

    public static void computeMaxFriends(PokemonRegistry registry) {
        for (PokemonSpecies species : registry.toList())
            if (species.friendArea() != null) species.friendArea().maxFriends += 2;
            else Logger.e(species + " has an unknown friend area: " + species.friendAreaID);

        ENCLOSED_ISLAND.maxFriends -= 6; // Deoxys Attack, Defense and Speed can't be obtained as friends
        THUNDER_MEADOW.maxFriends -= 6; // Castform Rain, Hail and Sun can't be obtained as friends
    }

    public static FriendArea find(String id) {
        return lookup.get(id);
    }

    /** The amount of Poke to pay to buy this Friend Area. */
    public final int buyprice;
    /** The Freezone for this Friend Area. */
    public final FreezoneInfo freezone;
    /** The maximum number of Pokemon this Friend Area can hold. Automatically computed to be 2*number of different species this accepts. */
    private int maxFriends = 0;
    /** The storypos form which this Friend Area may be bought. If -1, can't be bought at all. */
    private int storyposBuyable = -1;

    private FriendArea(FreezoneInfo freezone) {
        this(freezone, -1, -1);
    }

    private FriendArea(FreezoneInfo freezone, int storyposBuyable, int buyprice) {
        this.freezone = freezone;
        this.storyposBuyable = storyposBuyable;
        this.buyprice = buyprice;
    }

    public boolean canBuy(int storypos) {
        if (this.storyposBuyable == -1) return false;
        return storypos >= this.storyposBuyable;
    }

    public Message getName() {
        return this.freezone.getName();
    }

    public LocalMapLocation mapLoaction() {
        return this.freezone.maplocation;
    }

    public int maxFriends() {
        return this.maxFriends;
    }

}
