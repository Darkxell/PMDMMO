package com.darkxell.client.renderers.layers;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.resources.image.Sprites.GraphicalLayerSprites;

public class WetDreamLayer extends AbstractGraphiclayer {

    private static final Color BG = new Color(247, 247, 181);

    private float counter_small = 0;
    private float counter_big = 0;

    @Override
    public void update() {
        counter_small -= 0.3;
        if (counter_small <= -GraphicalLayerSprites.Dream.getSmallLeft().getHeight())
            counter_small = 0;
        counter_big += 0.3;
        if (counter_big >= GraphicalLayerSprites.Dream.getBigLeft().getHeight())
            counter_big = 0;
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        g.setColor(BG);
        g.fillRect(0, 0, width, height);

        int ctemp = (int) (counter_big);
        for (int i = -1; i < 2; i++) {
            g.drawImage(GraphicalLayerSprites.Dream.getBigLeft(), 20,
                    ctemp + (i * GraphicalLayerSprites.Dream.getBigLeft().getHeight()), null);
            g.drawImage(GraphicalLayerSprites.Dream.getBigRight(),
                    width - 20 - GraphicalLayerSprites.Dream.getBigRight().getWidth(),
                    ctemp + (i * GraphicalLayerSprites.Dream.getBigRight().getHeight()), null);
        }

        ctemp = (int) counter_small;
        for (int i = 0; i < 3; i++) {
            g.drawImage(GraphicalLayerSprites.Dream.getSmallLeft(), 0,
                    ctemp + (i * GraphicalLayerSprites.Dream.getSmallLeft().getHeight()), null);
            g.drawImage(GraphicalLayerSprites.Dream.getSmallRight(),
                    width - GraphicalLayerSprites.Dream.getSmallRight().getWidth(),
                    ctemp + (i * GraphicalLayerSprites.Dream.getSmallRight().getHeight()), null);
        }
    }

}
