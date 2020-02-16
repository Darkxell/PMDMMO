package com.darkxell.common.model.io;

import com.darkxell.common.model.item.ItemListModel;
import com.darkxell.common.model.item.ItemModel;

public class ItemModelIOHandler extends ModelIOHandler<ItemListModel> {

    public ItemModelIOHandler() {
        super(ItemListModel.class);
    }

    @Override
    protected ItemListModel handleAfterImport(ItemListModel object) {

        for (ItemModel item : object.items) {
            if (item.isRare() == null)
                item.setRare(false);

            if (item.isStackable() == null)
                item.setStackable(false);
        }

        return super.handleAfterImport(object);
    }

    @Override
    protected ItemListModel handleBeforeExport(ItemListModel object) {

        object = object.copy();

        for (ItemModel item : object.items) {
            if (item.isRare().equals(false))
                item.setRare(null);

            if (item.isStackable().equals(false))
                item.setStackable(null);
        }

        return super.handleAfterImport(object);
    }

}
