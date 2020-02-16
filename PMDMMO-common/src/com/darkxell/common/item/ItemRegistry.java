package com.darkxell.common.item;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.common.item.effects.TeachedMoveItemEffect;
import com.darkxell.common.item.effects.TeachesMoveItemEffect;
import com.darkxell.common.item.effects.TeachesMoveRenewableItemEffect;
import com.darkxell.common.model.io.ModelIOHandlers;
import com.darkxell.common.model.item.ItemListModel;
import com.darkxell.common.model.item.ItemModel;
import com.darkxell.common.registry.Registry;

/**
 * Holds all Items.
 */
public final class ItemRegistry extends Registry<Item, ItemListModel> {

    public ItemRegistry(URL registryURL) throws IOException {
        super(registryURL, ModelIOHandlers.item, "Items");
    }

    protected HashMap<Integer, Item> deserializeDom(ItemListModel model) {
        HashMap<Integer, Item> items = new HashMap<>();
        for (ItemModel i : model.items) {
            Item item = new Item(i);
            items.put(item.getID(), item);

            String extra = i.getExtra();

            if (extra != null) {
                int effect = item.getEffectID();
                String[] data = extra.split(":");
                if (data[0].equals("tm")) {
                    new TeachesMoveItemEffect(effect, Integer.parseInt(data[1]));
                    new TeachedMoveItemEffect(-effect, Integer.parseInt(data[1]));
                } else if (data[0].equals("hm"))
                    new TeachesMoveRenewableItemEffect(effect, Integer.parseInt(data[1]));
            }
        }
        return items;
    }

    protected ItemListModel serializeDom(HashMap<Integer, Item> items) {
        ItemListModel model = new ItemListModel();
        items.values().forEach(i -> model.items.add(i.getModel()));
        model.items.sort(Comparator.naturalOrder());
        return model;
    }
}
