package com.darkxell.client.mechanics.freezones.cutscenemaps;

import com.darkxell.client.mechanics.freezones.CutsceneFreezoneMap;
import com.darkxell.common.zones.FreezoneInfo;

public class MtsteelEntranceFreezone extends CutsceneFreezoneMap {

    public MtsteelEntranceFreezone() {
        super(buildModel("cutscenes/mtsteel-entrances", 416, 320), FreezoneInfo.MTSTEEL_ENTRANCE);
    }

}
