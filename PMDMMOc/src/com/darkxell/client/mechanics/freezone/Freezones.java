package com.darkxell.client.mechanics.freezone;

import com.darkxell.client.mechanics.freezone.customzones.FriendAreaFreezone;
import com.darkxell.client.mechanics.freezone.cutscene.MtSteelTopFreezone;
import com.darkxell.client.mechanics.freezone.cutscene.MtsteelEntranceFreezone;
import com.darkxell.client.mechanics.freezone.cutscene.ThunderwaveClearFreezone;
import com.darkxell.client.mechanics.freezone.cutscene.ThunderwaveEntranceFreezone;
import com.darkxell.client.mechanics.freezone.cutscene.TinywoodsClearFreezone;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.util.Logger;
import com.darkxell.common.zones.FreezoneInfo;

public class Freezones {
    public static FreezoneInfo getFreezoneForBossFloor(Dungeon dungeon, Floor floor) {
        if (dungeon.id == 3 && floor.id == 9)
            return FreezoneInfo.DUNGEON_MT_STEEL_TOP;

        // DEFAULT
        Logger.w("Couldn't find a freezone for the Boss Floor after boss defeated. Defaults to Mt Steel Top.");
        return FreezoneInfo.DUNGEON_MT_STEEL_TOP;
    }

    /**
     * Creates a new instance of the desired freezone map.
     */
    public static FreezoneMap loadMap(FreezoneInfo freezone) {
        switch (freezone) {
        case MTSTEEL_ENTRANCE:
            return new MtsteelEntranceFreezone();
        case THUNDERWAVE_ENTRANCE:
            return new ThunderwaveEntranceFreezone();
        case THUNDERWAVE_CLEAR:
            return new ThunderwaveClearFreezone();
        case TINYWOODS_CLEAR:
            return new TinywoodsClearFreezone();
        case DUNGEON_MT_STEEL_TOP:
            return new MtSteelTopFreezone();
        case FRIEND_AGEDCHAMBER1:
        case FRIEND_AGEDCHAMBER2:
        case FRIEND_ANCIENTRELIC:
        case FRIEND_BEAUPLAINS:
        case FRIEND_BOULDERCAVE:
        case FRIEND_BOUNTIFULSEA:
        case FRIEND_CRATER:
        case FRIEND_CRYPTICCAVE:
        case FRIEND_DARKNESSRIDGE:
        case FRIEND_DECREPITLAB:
        case FRIEND_DEEPSEACURRENT:
        case FRIEND_DEEPSEAFLOOR:
        case FRIEND_DRAGONCAVE:
        case FRIEND_ECHOCAVE:
        case FRIEND_ENCLOSEDISLAND:
        case FRIEND_ENERGETICFOREST:
        case FRIEND_FINALISLAND:
        case FRIEND_FLYAWAYFOREST:
        case FRIEND_FRIGIDCAVERN:
        case FRIEND_FURNACEDESERT:
        case FRIEND_HEALINGFOREST:
        case FRIEND_ICEFLOEBEACH:
        case FRIEND_JUNGLE:
        case FRIEND_LEGENDARYISLAND:
        case FRIEND_MAGNETICQUARRY:
        case FRIEND_MISTRISEFOREST:
        case FRIEND_MTCLEFT:
        case FRIEND_MTDEEPGREEN:
        case FRIEND_MTDISCIPLINE:
        case FRIEND_MTMOONVIEW:
        case FRIEND_MUSHROOMFOREST:
        case FRIEND_MYSTICLAKE:
        case FRIEND_OVERGROWNFOREST:
        case FRIEND_PEANUTSWAMP:
        case FRIEND_POISONSWAMP:
        case FRIEND_POWERPLANT:
        case FRIEND_RAINBOWPEAK:
        case FRIEND_RAVAGEDFIELD:
        case FRIEND_RUBADUBRIVER:
        case FRIEND_SACREDFIELD:
        case FRIEND_SAFARI:
        case FRIEND_SCORCHEDPLAINS:
        case FRIEND_SEAFLOORCAVE:
        case FRIEND_SECRETIVEFOREST:
        case FRIEND_SERENESEA:
        case FRIEND_SHALLOWBEACH:
        case FRIEND_SKYBLUEPLAINS:
        case FRIEND_SOUTHERNISLAND:
        case FRIEND_STRATOSLOOKOUT:
        case FRIEND_TADPOLEPOND:
        case FRIEND_THUNDERMEADOW:
        case FRIEND_TRANSFORMFOREST:
        case FRIEND_TREASURESEA:
        case FRIEND_TURTLESHELLPOND:
        case FRIEND_VOLCANICPIT:
        case FRIEND_WATERFALLLAKE:
        case FRIEND_WILDPLAINS:
            return new FriendAreaFreezone("/freezones/friend/" + freezone.id + ".xml", freezone);
        case BASEINSIDE:
            return new FreezoneMap("/freezones/base_normal.xml", freezone);
        default:
            return new FreezoneMap("/freezones/" + freezone.id + ".xml", freezone);
        }
    }
}
