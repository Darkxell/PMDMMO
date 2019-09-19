package com.darkxell.common.mission;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MissionTest {

    private Mission mission;

    @Before
    public void beforeTests() {
        try {
            this.mission = new Mission("A", 1, 1, 1, 2, 1,
                    new MissionReward(1, new int[0], new int[0], 0, new String[0]), Mission.TYPE_DEFEAT);
        } catch (InvalidParammetersException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMissionStringify() {
        try {
            Assert.assertEquals(this.mission, new Mission(this.mission.toString()));
        } catch (InvalidParammetersException e) {
            e.printStackTrace();
        }
    }

}
