package com.darkxell.client.graphics;

import java.awt.*;

/**
 * This object describes an animated background or foreground. This asset is
 * based on update/render mechanics. The render method must render on the whole
 * screen.
 */
public interface AbstractGraphicsLayer {
    void update();
    void render(Graphics2D g, int width, int height);
}
