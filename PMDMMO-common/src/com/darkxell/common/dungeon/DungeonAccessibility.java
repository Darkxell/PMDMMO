package com.darkxell.common.dungeon;

import com.darkxell.common.dbobject.DBPlayer;

/** Class that contains methods to determine the available dungeons to a player. */
public class DungeonAccessibility {

    public static final byte LOCKED = 0;
    public static final byte ACCESSIBLE = 1;
    public static final byte NOMAP = 2;

    /** predicate that returns true if the dungeon is accessible by the given player. */
    public static byte isAvailable(DBPlayer player, int dungeonid) {
        if (player == null)
            return LOCKED;
        switch (dungeonid) {
        case 1:// TINY WOODS
            return (player.storyposition >= 1) ? ACCESSIBLE : LOCKED;
        case 2:// THUNDERWAVE CAVE
            return (player.storyposition >= 5) ? ACCESSIBLE : LOCKED;
        case 3:// MOUNT STEEL
            return (player.storyposition >= 11) ? ACCESSIBLE : LOCKED;
        case 4:// SINISTER WOODS
            return (player.storyposition >= 17) ? ACCESSIBLE : LOCKED;
        case 5:// SILENT CHASM
            return (player.storyposition >= 21) ? ACCESSIBLE : LOCKED;
        case 6:// MOUNT THUNDER
            return (player.storyposition >= 25) ? ACCESSIBLE : LOCKED;
        case 7:// MOUNT THUNDER PEAK
            return (player.storyposition >= 26) ? NOMAP : LOCKED;
        case 8:// GREAT CANYON
            return (player.storyposition >= 30) ? ACCESSIBLE : LOCKED;
        case 9:// LAPIS CAVE
            return (player.storyposition >= 40) ? ACCESSIBLE : LOCKED;
        case 10:// MOUNT BLAZE
            return (player.storyposition >= 42) ? ACCESSIBLE : LOCKED;
        case 11:// MOUNT BLAZE PEAK
            return (player.storyposition >= 45) ? NOMAP : LOCKED;
        case 12:// FROSTY FOREST
            return (player.storyposition >= 50) ? ACCESSIBLE : LOCKED;
        case 13:// FROSTY GROTTO
            return (player.storyposition >= 51) ? ACCESSIBLE : LOCKED;
        case 14:// MOUNT FREEZE
            return (player.storyposition >= 52) ? ACCESSIBLE : LOCKED;
        case 15:// MOUNT FREEZE PEAK
            return (player.storyposition >= 53) ? NOMAP : LOCKED;
        case 16:// MAGMA CAVERN
            return (player.storyposition >= 95) ? ACCESSIBLE : LOCKED;
        case 17:// MAGMA CAVERN PIT
            return (player.storyposition >= 100) ? NOMAP : LOCKED;
        case 18:// SKY TOWER
            return (player.storyposition >= 111) ? ACCESSIBLE : LOCKED;
        case 19:// SKY TOWER SUMMIT
            return (player.storyposition >= 115) ? NOMAP : LOCKED;
        case 34:// UPROAR FOREST
            return (player.storyposition >= 67) ? ACCESSIBLE : LOCKED;
        case 39:// ROCK PATH
            return (player.storyposition >= 40) ? NOMAP : LOCKED;
        case 40:// SNOW PATH
            return (player.storyposition >= 40) ? NOMAP : LOCKED;
        default:
            return LOCKED;
        }
    }
}
