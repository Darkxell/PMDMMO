package com.darkxell.client.mechanics.freezones.xmlstorage;

import java.util.HashMap;

import com.darkxell.client.model.freezone.FreezoneModel;
import com.darkxell.client.model.io.ClientModelIOHandlers;
import com.darkxell.common.util.Logger;

/** Utility class that stores the Freezone models instead of looking in files each time. */
public abstract class FreezoneModelRegistry {

    /** Stores Freezone models. */
    private static final HashMap<String, FreezoneModel> cache = new HashMap<>(10);

    public static FreezoneModel getFreezone(String modelPath) {
        if (!cache.containsKey(modelPath)) {
            readFreezone(modelPath);
        }
        return cache.get(modelPath);
    }

    private static void readFreezone(String modelPath) {
        Logger.i("Read freezone XML for the first time:" + modelPath);
        FreezoneModel model = ClientModelIOHandlers.freezone.read(FreezoneModel.class.getResource(modelPath));
        cache.put(modelPath, model);
    }

}
