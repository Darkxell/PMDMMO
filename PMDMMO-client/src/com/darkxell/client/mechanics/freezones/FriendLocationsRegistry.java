package com.darkxell.client.mechanics.freezones;

import java.util.HashMap;

import com.darkxell.client.model.friendlocations.FriendLocationRegistryModel;
import com.darkxell.client.model.friendlocations.FriendLocationsModel;
import com.darkxell.client.model.io.ClientModelIOHandlers;
import com.darkxell.common.zones.FreezoneInfo;

public final class FriendLocationsRegistry {

    private static final HashMap<FreezoneInfo, FriendLocationsModel> cache = new HashMap<>();

    public static void load() {
        FriendLocationRegistryModel model = ClientModelIOHandlers.friendLocations
                .read(FriendLocationsRegistry.class.getResource("/data/freezone-locations.xml"));
        for (FriendLocationsModel l : model.getLocations())
            cache.put(l.getTargetFreezone(), l);
    }

    public static FriendLocationsModel get(FreezoneInfo freezone) {
        return cache.get(freezone);
    }

}
