package com.darkxell.common.tests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.mission.MissionReward;
import com.darkxell.common.tests.CommonSetup;
import com.darkxell.common.util.Logger;

public class MissionCompressionTest {
    @BeforeAll
    public static void setUp() {
        CommonSetup.setUp();
    }

    private void copyTest(Mission mission) throws InvalidParammetersException {
        String compressed = mission.toString();
        Logger.d(compressed);
        Mission copy = new Mission(compressed);
        assertEquals(copy, mission, "toString serialized copy should yield the same object as original.");
    }

    @Test
    public void mission() throws InvalidParammetersException {
        this.copyTest(new Mission("B", 2, 4, 53, 47, 14,
                new MissionReward(42, new int[] { 2, 4 }, new int[] { 3, 1 }, 50, null), Mission.TYPE_DEFEAT));
        this.copyTest(new Mission("C", 5, 5, 57, 43, 8, new MissionReward(69, null, null, 50, new String[] { "test" }),
                Mission.TYPE_ESCORT));
    }
}
