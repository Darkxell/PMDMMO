package com.darkxell.common.test.tests;

import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.mission.MissionReward;
import com.darkxell.common.test.UTest;

public class MissionCompressionTest extends UTest {

    @Override
    protected int test() {
        try {
            Mission mission1 = new Mission("B", 2, 4, 53, 47, 14,
                    new MissionReward(42, new int[] { 2, 4 }, new int[] { 3, 1 }, 50, null), Mission.TYPE_DEFEAT);
            Mission mission2 = new Mission("C", 5, 5, 57, 43, 8,
                    new MissionReward(69, null, null, 50, new String[] { "test" }), Mission.TYPE_ESCORT);

            String s1 = mission1.toString();
            String s2 = mission2.toString();
            System.out.println(s1 + "\n" + s2);

            Mission c1 = new Mission(s1), c2 = new Mission(s2);

            return c1.equals(mission1) && c2.equals(mission2) ? 0 : 1;
        } catch (InvalidParammetersException e) {
            e.printStackTrace();
            return 2;
        }
    }

}
