package com.darkxell.common.item;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.darkxell.common.Registry;
import org.jdom2.Element;

import com.darkxell.common.item.Item.ItemCategory;
import com.darkxell.common.item.effects.TeachedMoveItemEffect;
import com.darkxell.common.item.effects.TeachesMoveItemEffect;
import com.darkxell.common.item.effects.TeachesMoveRenewableItemEffect;
import com.darkxell.common.util.XMLUtils;

/**
 * Holds all Items.
 */
public final class ItemRegistry extends Registry<Item> {
    protected Element serializeDom(HashMap<Integer, Item> items) {
        Element xml = new Element("items");
        for (Item item : items.values()) {
            if (!(item.effect() instanceof TeachedMoveItemEffect)) {
                Element i = item.toXML();
                if (item.effect() instanceof TeachesMoveRenewableItemEffect) {
                    int move = ((TeachesMoveRenewableItemEffect) item.effect()).moveID;
                    if (item.effect() instanceof TeachesMoveItemEffect) {
                        i.setAttribute("extra", "tm:" + move);
                    } else {
                        i.setAttribute("extra", "hm:" + move);
                    }
                }
                xml.addContent(i);
            }
        }
        return xml;
    }

    protected HashMap<Integer, Item> deserializeDom(Element root) {
        List<Element> itemElements = root.getChildren();
        HashMap<Integer, Item> items = new HashMap<>(itemElements.size());
        for (Element e : itemElements) {
            Item item = new Item(e);
            items.put(item.id, item);

		Element root = XMLUtils.read(ItemRegistry.class.getResourceAsStream("/data/items.xml"));
		for (Element e : root.getChildren())
		{
			Item item = new Item(e);
			register(item);

			String extra = XMLUtils.getAttribute(e, "extra", null);

                    Item used = new Item(-1 * item.id, ItemCategory.OTHERS, 0, 1, -effect, 95, false, false);
                    register(used);
                } else if (data[0].equals("hm")) {
                    new TeachesMoveRenewableItemEffect(effect, Integer.parseInt(data[1]));
                }
            }
        }
        return items;
    }

    public ItemRegistry(URL registryURL, String name) throws IOException {
        super(registryURL, name);
    }
}
