package com.darkxell.common.dungeon;

/**Class that contains methods to determine the available dungeons to a player.*/
public class DungeonAccessibility{

  public static final byte LOCKED = 0;
  public static final byte ACCESSIBLE = 1;
  public static final byte NOMAP = 0;

  /**predicate that returns true if the dungeon is accessible by the given player.*/
  public static byte isAvailable(DBplayer player,int dungeonid){
    if(player==null)return false;
    switch(dungeonid){
    case 1://TINY WOODS
    return (player.storyposition>=1)?ACCESSIBLE:LOCKED;
    case 2://THUNDERWAVE CAVE
    return (player.storyposition>=5)?ACCESSIBLE:LOCKED;
    case 3://MOUNT STEEL
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 4://SINISTER WOODS
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 5://SILENT CHASM
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 6://MOUNT THUNDER
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 7://MOUNT THUNDER PEAK
    return (player.storyposition>=15)?NOMAP:LOCKED;
    case 8://GREAT CANYON
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 9://LAPIS CAVE
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 10://MOUNT BLAZE
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 11://MOUNT BLAZE PEAK
    return (player.storyposition>=15)?NOMAP:LOCKED;
    case 12://FROSTY FOREST
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 13://FROSTY GROTTO
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 14://MOUNT FREEZE
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 15://MOUNT FREEZE PEAK
    return (player.storyposition>=15)?NOMAP:LOCKED;
    case 16://MAGMA CAVERN
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 17://MAGMA CAVERN PIT
    return (player.storyposition>=15)?NOMAP:LOCKED;
    case 18://SKY TOWER
    return (player.storyposition>=15)?ACCESSIBLE:LOCKED;
    case 19://SKY TOWER SUMMIT
    return (player.storyposition>=15)?NOMAP:LOCKED;
    default:
    return LOCKED;
    }
  }
}
