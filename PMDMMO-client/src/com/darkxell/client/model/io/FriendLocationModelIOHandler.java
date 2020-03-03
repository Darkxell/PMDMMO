package com.darkxell.client.model.io;

import com.darkxell.client.model.friendlocations.FriendLocationModel;
import com.darkxell.client.model.friendlocations.FriendLocationRegistryModel;
import com.darkxell.client.model.friendlocations.FriendLocationsModel;
import com.darkxell.common.model.io.ModelIOHandler;

public class FriendLocationModelIOHandler extends ModelIOHandler<FriendLocationRegistryModel> {

    public FriendLocationModelIOHandler() {
        super(FriendLocationRegistryModel.class);
    }

    @Override
    protected FriendLocationRegistryModel handleAfterImport(FriendLocationRegistryModel object) {

        for (FriendLocationsModel locations : object.getLocations())
            for (FriendLocationModel location : locations.getLocations())
                this.handleLocationAfterImport(location);

        return super.handleAfterImport(object);
    }

    @Override
    protected FriendLocationRegistryModel handleBeforeExport(FriendLocationRegistryModel object) {

        object = object.copy();
        for (FriendLocationsModel locations : object.getLocations())
            for (FriendLocationModel location : locations.getLocations())
                this.handleLocationBeforeExport(location);

        return super.handleBeforeExport(object);
    }

    private void handleLocationAfterImport(FriendLocationModel location) {
        if (location.getShiny() == null)
            location.setShiny(false);
    }

    private void handleLocationBeforeExport(FriendLocationModel location) {
        if (location.getShiny().equals(false))
            location.setShiny(null);
    }

}
