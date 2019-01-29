package com.darkxell.client.graphics.layers;

import com.darkxell.client.graphics.AbstractGraphicsLayer;
import com.darkxell.common.util.xml.XMLObject;
import org.jdom2.Element;

/**
 * This object describes an animated background or foreground. This asset is
 * based on update/render mechanics. The render method must render on the whole
 * screen.
 */
public abstract class BackgroundLayer extends XMLObject implements AbstractGraphicsLayer {
    public static final String TAG_NAME = "background";

    private Element original;

    @Override
    public Element serialize() {
        return this.original;
    }

    @Override
    protected void deserialize(Element el) {
        this.original = el;
    }
}
