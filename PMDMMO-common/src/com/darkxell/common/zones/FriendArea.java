package com.darkxell.common.zones;

import java.util.HashMap;

import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public enum FriendArea {

    AGED_CHAMBER_1(FreezoneInfo.FRIEND_AGEDCHAMBER1),
    AGED_CHAMBER_2(FreezoneInfo.FRIEND_AGEDCHAMBER2),
    ANCIENT_RELIC(FreezoneInfo.FRIEND_ANCIENTRELIC),
    BEAU_PLAINS(FreezoneInfo.FRIEND_BEAUPLAINS),
    BOULDER_CAVE(FreezoneInfo.FRIEND_BOULDERCAVE),
    BOUNTIFUL_SEA(FreezoneInfo.FRIEND_BOUNTIFULSEA),
    CRATER(FreezoneInfo.FRIEND_CRATER),
    CRYPTIC_CAVE(FreezoneInfo.FRIEND_CRYPTICCAVE),
    DARKNESS_RIDGE(FreezoneInfo.FRIEND_DARKNESSRIDGE),
    DECREPIT_LAB(FreezoneInfo.FRIEND_DECREPITLAB),
    DEEP_SEA_CURRENT(FreezoneInfo.FRIEND_DEEPSEACURRENT),
    DRAGON_CAVE(FreezoneInfo.FRIEND_DRAGONCAVE),
    ECHO_CAVE(FreezoneInfo.FRIEND_ECHOCAVE),
    ENCLOSED_ISLAND(FreezoneInfo.FRIEND_ENCLOSEDISLAND),
    ENERGETIC_FOREST(FreezoneInfo.FRIEND_ENERGETICFOREST),
    FINAL_ISLAND(FreezoneInfo.FRIEND_FINALISLAND),
    FLYAWAY_FOREST(FreezoneInfo.FRIEND_FLYAWAYFOREST),
    FRIGID_CAVERN(FreezoneInfo.FRIEND_FRIGIDCAVERN),
    FURNACE_DESERT(FreezoneInfo.FRIEND_FURNACEDESERT),
    HEALING_FOREST(FreezoneInfo.FRIEND_HEALINGFOREST),
    ICE_FLOE_BEACH(FreezoneInfo.FRIEND_ICEFLOEBEACH),
    JUNGLE(FreezoneInfo.FRIEND_JUNGLE),
    LEGENDARY_ISLAND(FreezoneInfo.FRIEND_LEGENDARYISLAND),
    MAGNETIC_QUARRY(FreezoneInfo.FRIEND_MAGNETICQUARRY),
    MIST_RISE_FOREST(FreezoneInfo.FRIEND_MISTRISEFOREST),
    MT_CLEFT(FreezoneInfo.FRIEND_MTCLEFT),
    MT_DEEPGREEN(FreezoneInfo.FRIEND_MTDEEPGREEN),
    MT_MOONVIEW(FreezoneInfo.FRIEND_MTMOONVIEW),
    MUSHROOM_FOREST(FreezoneInfo.FRIEND_MUSHROOMFOREST),
    MYSTIC_LAKE(FreezoneInfo.FRIEND_MYSTICLAKE),
    OVERGROWN_FOREST(FreezoneInfo.FRIEND_OVERGROWNFOREST),
    PEANUT_SWAMP(FreezoneInfo.FRIEND_PEANUTSWAMP),
    POISON_SWAMP(FreezoneInfo.FRIEND_POISONSWAMP),
    POWER_PLANT(FreezoneInfo.FRIEND_POWERPLANT),
    RAINBOW_PEAK(FreezoneInfo.FRIEND_RAINBOWPEAK),
    RAVAGED_FIELD(FreezoneInfo.FRIEND_RAVAGEDFIELD),
    RUBADUB_RIVER(FreezoneInfo.FRIEND_RUBADUBRIVER),
    SACRED_FIELD(FreezoneInfo.FRIEND_SACREDFIELD),
    SAFARI(FreezoneInfo.FRIEND_SAFARI),
    SCORCHED_PLAINS(FreezoneInfo.FRIEND_SCORCHEDPLAINS),
    SEAFLOOR_CAVE(FreezoneInfo.FRIEND_SEAFLOORCAVE),
    SECRETIVE_FOREST(FreezoneInfo.FRIEND_SECRETIVEFOREST),
    SERENE_SEA(FreezoneInfo.FRIEND_SERENESEA),
    SHALLOW_BEACH(FreezoneInfo.FRIEND_SHALLOWBEACH),
    SKY_BLUE_PLAINS(FreezoneInfo.FRIEND_SKYBLUEPLAINS),
    SOUTHERN_ISLAND(FreezoneInfo.FRIEND_SOUTHERNISLAND),
    STRATOS_LOOKOUT(FreezoneInfo.FRIEND_STRATOSLOOKOUT),
    TADPOLE_POND(FreezoneInfo.FRIEND_TADPOLEPOND),
    THUNDER_MEADOW(FreezoneInfo.FRIEND_THUNDERMEADOW),
    TRANSFORM_FOREST(FreezoneInfo.FRIEND_TRANSFORMFOREST),
    TREASURE_SEA(FreezoneInfo.FRIEND_TREASURESEA),
    TURTLESHEEL_POND(FreezoneInfo.FRIEND_TURTLESHELLPOND),
    VOLCANIC_PIT(FreezoneInfo.FRIEND_VOLCANICPIT),
    WATERFALL_LAKE(FreezoneInfo.FRIEND_WATERFALLLAKE),
    WILD_PLAINS(FreezoneInfo.FRIEND_WILDPLAINS);

    private static HashMap<String, FriendArea> lookup = new HashMap<>();

    static {
        for (FriendArea a : values())
            lookup.put(a.freezone.id, a);
    }

    public static FriendArea find(String id) {
        return lookup.get(id);
    }

    /** The Freezone for this Friend Area. */
    public final FreezoneInfo freezone;
    /** The maximum number of Pokemon this Friend Area can hold. Automatically computed to be 2*number of different species this accepts. */
    private int maxFriends = 0;

    private FriendArea(FreezoneInfo freezone) {
        this.freezone = freezone;
    }

    public Message getName() {
        return this.freezone.getName();
    }

    public LocalMapLocation mapLoaction() {
        return this.freezone.maplocation;
    }

}
