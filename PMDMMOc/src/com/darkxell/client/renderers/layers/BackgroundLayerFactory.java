package com.darkxell.client.renderers.layers;

import org.jdom2.Element;

public class BackgroundLayerFactory {
    public static AbstractGraphicLayer getLayer(Element el) {
        String type = el.getAttributeValue("type");
        switch (type == null ? "none" : type) {
            case "lsd":
                return new BackgroundLsdLayer();
            case "sea":
                boolean rising = el.getAttribute("rising") != null;
                return new BackgroundSeaLayer(rising);
            case "wet":
                return new WetDreamLayer();
            case "none":
            default:
                return null;
        }
    }
}
