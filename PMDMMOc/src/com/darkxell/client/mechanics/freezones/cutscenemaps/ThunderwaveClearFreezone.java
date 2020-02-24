package com.darkxell.client.mechanics.freezones.cutscenemaps;

import com.darkxell.client.mechanics.freezones.CutsceneFreezoneMap;
import com.darkxell.common.zones.FreezoneInfo;

public class ThunderwaveClearFreezone extends CutsceneFreezoneMap {

    public ThunderwaveClearFreezone() {
        super(buildModel("cutscenes/thunderwave-clear", 416, 320), FreezoneInfo.THUNDERWAVE_CLEAR);
    }

}
