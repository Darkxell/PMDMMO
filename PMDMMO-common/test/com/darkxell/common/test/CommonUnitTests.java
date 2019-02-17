package com.darkxell.common.test;

import com.darkxell.common.Registries;
import com.darkxell.common.test.tests.DBObjecttransferTest;
import com.darkxell.common.test.tests.MissionCompressionTest;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Localization;

public class CommonUnitTests {
    public static void executeTests() {
        Logger.load("SERVER");
        Localization.load(false);
        Registries.load();

        new DBObjecttransferTest().execute();
        // new DungeonEventTransferTest().execute();
        // new AutoDungeonTest().execute();
        new MissionCompressionTest().execute();
    }

    public static void main(String[] args) {
        executeTests();
    }
}
