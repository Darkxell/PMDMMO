package com.darkxell.client.graphics.layers;

import org.jdom2.Element;

public class BackgroundLayerFactory {
    public static BackgroundLayer getLayer(Element el) {
        BackgroundLayer bg = createLayer(el.getAttributeValue("type"));
        if (bg != null)
            bg.initialize(el);
        return bg;
    }

    private static BackgroundLayer createLayer(String type) {
        switch (type == null ? "none" : type) {
        case "lsd":
            return new BackgroundLsdLayer();
        case "sea":
            return new BackgroundSeaLayer();
        case "wet":
            return new WetDreamLayer();
        case "none":
        default:
            return null;
        }
    }
}
