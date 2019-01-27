package com.darkxell.client.graphics.layers;

import com.darkxell.common.util.XMLObject;
import org.jdom2.Element;

import java.awt.*;

/**
 * This object describes an animated background or foreground. This asset is
 * based on update/render mechanics. The render method must render on the whole
 * screen.
 */
public abstract class AbstractGraphicLayer extends XMLObject {
    @Override
    protected void onInitialize(Element el) {
    }

    public abstract void update();
    public abstract void render(Graphics2D g, int width, int height);
}
