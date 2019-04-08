package com.darkxell.common.zones;

import static com.darkxell.common.util.Util.POST_GAME_STORYPOS;

import java.util.HashMap;

import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Util;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FriendAreaAcquisition.AutoStoryFriendArea;
import com.darkxell.common.zones.FriendAreaAcquisition.BaseFriendAreaAcquisition;
import com.darkxell.common.zones.FriendAreaAcquisition.BuyableFriendArea;

public enum FriendArea {

    AGED_CHAMBER_1(FreezoneInfo.FRIEND_AGEDCHAMBER1, new BuyableFriendArea(POST_GAME_STORYPOS, 5500)),
    AGED_CHAMBER_2(FreezoneInfo.FRIEND_AGEDCHAMBER2, new BuyableFriendArea(POST_GAME_STORYPOS, 5500)),
    ANCIENT_RELIC(FreezoneInfo.FRIEND_ANCIENTRELIC, new BuyableFriendArea(POST_GAME_STORYPOS, 8000)),
    BEAU_PLAINS(FreezoneInfo.FRIEND_BEAUPLAINS, new BuyableFriendArea(600)),
    BOULDER_CAVE(FreezoneInfo.FRIEND_BOULDERCAVE),
    BOUNTIFUL_SEA(FreezoneInfo.FRIEND_BOUNTIFULSEA, new BuyableFriendArea(POST_GAME_STORYPOS, 5500)),
    CRATER(FreezoneInfo.FRIEND_CRATER, new BuyableFriendArea(POST_GAME_STORYPOS, 7500)),
    CRYPTIC_CAVE(FreezoneInfo.FRIEND_CRYPTICCAVE, new BuyableFriendArea(POST_GAME_STORYPOS, 6500)),
    DARKNESS_RIDGE(FreezoneInfo.FRIEND_DARKNESSRIDGE),
    DECREPIT_LAB(FreezoneInfo.FRIEND_DECREPITLAB, new BuyableFriendArea(1000)),
    DEEP_SEA_CURRENT(FreezoneInfo.FRIEND_DEEPSEACURRENT, BaseFriendAreaAcquisition.ON_RECRUIT),
    DEEP_SEA_FLOOR(FreezoneInfo.FRIEND_DEEPSEAFLOOR, new BuyableFriendArea(POST_GAME_STORYPOS, 5500)),
    DRAGON_CAVE(FreezoneInfo.FRIEND_DRAGONCAVE),
    ECHO_CAVE(FreezoneInfo.FRIEND_ECHOCAVE, new BuyableFriendArea(POST_GAME_STORYPOS, 7500)),
    ENCLOSED_ISLAND(FreezoneInfo.FRIEND_ENCLOSEDISLAND, BaseFriendAreaAcquisition.ON_RECRUIT),
    ENERGETIC_FOREST(FreezoneInfo.FRIEND_ENERGETICFOREST, new BuyableFriendArea(POST_GAME_STORYPOS, 5000)),
    FINAL_ISLAND(FreezoneInfo.FRIEND_FINALISLAND, new BuyableFriendArea(POST_GAME_STORYPOS, 8500)),
    FLYAWAY_FOREST(FreezoneInfo.FRIEND_FLYAWAYFOREST, new BuyableFriendArea(550)),
    FRIGID_CAVERN(FreezoneInfo.FRIEND_FRIGIDCAVERN, new BuyableFriendArea(POST_GAME_STORYPOS, 9000)),
    FURNACE_DESERT(FreezoneInfo.FRIEND_FURNACEDESERT, new BuyableFriendArea(POST_GAME_STORYPOS, 8500)),
    HEALING_FOREST(FreezoneInfo.FRIEND_HEALINGFOREST, BaseFriendAreaAcquisition.ON_RECRUIT),
    ICE_FLOE_BEACH(FreezoneInfo.FRIEND_ICEFLOEBEACH, new BuyableFriendArea(POST_GAME_STORYPOS, 9500)),
    JUNGLE(FreezoneInfo.FRIEND_JUNGLE, new BuyableFriendArea(800)),
    LEGENDARY_ISLAND(FreezoneInfo.FRIEND_LEGENDARYISLAND, BaseFriendAreaAcquisition.ON_RECRUIT),
    MAGNETIC_QUARRY(FreezoneInfo.FRIEND_MAGNETICQUARRY, new BuyableFriendArea(1000)),
    MIST_RISE_FOREST(FreezoneInfo.FRIEND_MISTRISEFOREST, new AutoStoryFriendArea(Util.POST_WIGGLYTUFF_STORYPOS)),
    MT_CLEFT(FreezoneInfo.FRIEND_MTCLEFT, new BuyableFriendArea(POST_GAME_STORYPOS, 5000)),
    MT_DEEPGREEN(FreezoneInfo.FRIEND_MTDEEPGREEN, new BuyableFriendArea(130)),
    MT_DISCIPLINE(FreezoneInfo.FRIEND_MTDISCIPLINE, new BuyableFriendArea(1200)),
    MT_MOONVIEW(FreezoneInfo.FRIEND_MTMOONVIEW),
    MUSHROOM_FOREST(FreezoneInfo.FRIEND_MUSHROOMFOREST, new BuyableFriendArea(500)),
    MYSTIC_LAKE(FreezoneInfo.FRIEND_MYSTICLAKE, new BuyableFriendArea(POST_GAME_STORYPOS, 2500)),
    OVERGROWN_FOREST(FreezoneInfo.FRIEND_OVERGROWNFOREST, new BuyableFriendArea(600)),
    PEANUT_SWAMP(FreezoneInfo.FRIEND_PEANUTSWAMP, new BuyableFriendArea(2500)),
    POISON_SWAMP(FreezoneInfo.FRIEND_POISONSWAMP, new BuyableFriendArea(POST_GAME_STORYPOS, 8500)),
    POWER_PLANT(FreezoneInfo.FRIEND_POWERPLANT, new AutoStoryFriendArea(Util.POST_WIGGLYTUFF_STORYPOS)),
    RAINBOW_PEAK(FreezoneInfo.FRIEND_RAINBOWPEAK, new BuyableFriendArea(POST_GAME_STORYPOS, 6500)),
    RAVAGED_FIELD(FreezoneInfo.FRIEND_RAVAGEDFIELD, new BuyableFriendArea(1000)),
    RUBADUB_RIVER(FreezoneInfo.FRIEND_RUBADUBRIVER, new BuyableFriendArea(500)),
    SACRED_FIELD(FreezoneInfo.FRIEND_SACREDFIELD, new BuyableFriendArea(POST_GAME_STORYPOS, 6500)),
    SAFARI(FreezoneInfo.FRIEND_SAFARI, new BuyableFriendArea(700)),
    SCORCHED_PLAINS(FreezoneInfo.FRIEND_SCORCHEDPLAINS, new BuyableFriendArea(POST_GAME_STORYPOS, 5000)),
    SEAFLOOR_CAVE(FreezoneInfo.FRIEND_SEAFLOORCAVE, BaseFriendAreaAcquisition.ON_RECRUIT),
    SECRETIVE_FOREST(FreezoneInfo.FRIEND_SECRETIVEFOREST, new BuyableFriendArea(POST_GAME_STORYPOS, 6500)),
    SERENE_SEA(FreezoneInfo.FRIEND_SERENESEA, new BuyableFriendArea(POST_GAME_STORYPOS, 8000)),
    SHALLOW_BEACH(FreezoneInfo.FRIEND_SHALLOWBEACH, new BuyableFriendArea(POST_GAME_STORYPOS, 5000)),
    SKY_BLUE_PLAINS(FreezoneInfo.FRIEND_SKYBLUEPLAINS),
    SOUTHERN_ISLAND(FreezoneInfo.FRIEND_SOUTHERNISLAND, new BuyableFriendArea(POST_GAME_STORYPOS, 9500)),
    STRATOS_LOOKOUT(FreezoneInfo.FRIEND_STRATOSLOOKOUT, BaseFriendAreaAcquisition.ON_RECRUIT),
    TADPOLE_POND(FreezoneInfo.FRIEND_TADPOLEPOND, new BuyableFriendArea(500)),
    THUNDER_MEADOW(FreezoneInfo.FRIEND_THUNDERMEADOW, new BuyableFriendArea(1000)),
    TRANSFORM_FOREST(FreezoneInfo.FRIEND_TRANSFORMFOREST, new BuyableFriendArea(500)),
    TREASURE_SEA(FreezoneInfo.FRIEND_TREASURESEA, new BuyableFriendArea(POST_GAME_STORYPOS, 4500)),
    TURTLESHEEL_POND(FreezoneInfo.FRIEND_TURTLESHELLPOND, new BuyableFriendArea(POST_GAME_STORYPOS, 5500)),
    VOLCANIC_PIT(FreezoneInfo.FRIEND_VOLCANICPIT, BaseFriendAreaAcquisition.ON_RECRUIT),
    WATERFALL_LAKE(FreezoneInfo.FRIEND_WATERFALLLAKE, new BuyableFriendArea(POST_GAME_STORYPOS, 3500)),
    WILD_PLAINS(FreezoneInfo.FRIEND_WILDPLAINS, new AutoStoryFriendArea(Util.POST_WIGGLYTUFF_STORYPOS));

    private static HashMap<String, FriendArea> lookup = new HashMap<>();

    public static final int POST_GAME = 100;

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

    /** How to acquire this Friend Area. */
    public final FriendAreaAcquisition acquisition;
    /** The Freezone for this Friend Area. */
    public final FreezoneInfo freezone;
    /** The maximum number of Pokemon this Friend Area can hold. Automatically computed to be 2*number of different species this accepts. */
    private int maxFriends = 0;

    private FriendArea(FreezoneInfo freezone) {
        this(freezone, BaseFriendAreaAcquisition.OTHER);
    }

    private FriendArea(FreezoneInfo freezone, FriendAreaAcquisition acquisition) {
        this.freezone = freezone;
        this.acquisition = acquisition;
    }

    public int buyPrice() {
        if (!(this.acquisition instanceof BuyableFriendArea)) return -1;
        return ((BuyableFriendArea) this.acquisition).price;
    }

    public boolean canBuy(int storypos) {
        if (!(this.acquisition instanceof BuyableFriendArea)) return false;
        return storypos >= ((BuyableFriendArea) this.acquisition).minStorypos;
    }

    public Message getName() {
        return this.freezone.getName();
    }

    public String id() {
        return this.freezone.id;
    }

    public LocalMapLocation mapLoaction() {
        return this.freezone.maplocation;
    }

    public int maxFriends() {
        return this.maxFriends;
    }

}
