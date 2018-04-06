package com.darkxell.client.renderers.layers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.Random;

import com.darkxell.client.resources.images.others.GraphicalLayersAssets;

public class BackgroundLsdLayer extends AbstractGraphiclayer {

	private float counter_1 = 0;
	private float counter_2 = 0;
	private float counter_3 = 0;
	private float counter_4 = 0;
	private float counter_5 = 0;
	private float counter_6 = 0;
	private float counter_7 = 0;

	private int maincolorcounter = 0;
	private int offcolorcounter = 0;
	private int gradientcounter = 150;

	@Override
	public void update() {
		// Update the different layers to move them according to their speed
		counter_1 += 0.3;
		if (counter_1 >= GraphicalLayersAssets.LSD_top[0].getWidth())
			counter_1 = 0;
		counter_2 += 0.2;
		if (counter_2 >= GraphicalLayersAssets.LSD_top[0].getWidth())
			counter_2 = 0;
		counter_3 += 0.25;
		if (counter_3 >= GraphicalLayersAssets.LSD_top[0].getWidth())
			counter_3 = 0;
		counter_4 += 0.35;
		if (counter_4 >= GraphicalLayersAssets.LSD_top[0].getWidth())
			counter_4 = 0;
		counter_5 += 0.40;
		if (counter_5 >= GraphicalLayersAssets.LSD_top[0].getWidth())
			counter_5 = 0;
		counter_6 += 0.1;
		if (counter_6 >= GraphicalLayersAssets.LSD_top[0].getWidth())
			counter_6 = 0;
		counter_7 += 0.07;
		if (counter_7 >= GraphicalLayersAssets.LSD_top[0].getWidth())
			counter_7 = 0;
		// Update the color changing variables
		if (++gradientcounter >= 200) {
			gradientcounter = 0;
			offcolorcounter = maincolorcounter;
			maincolorcounter = new Random().nextInt(7);
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// Creates alpha gradient images
		float[] scales = { 1f, 1f, 1f, 1f - ((float) gradientcounter) / 200 };
		float[] offsets = new float[4];
		RescaleOp rop = new RescaleOp(scales, offsets, null);
		BufferedImage img_bot = new BufferedImage(GraphicalLayersAssets.LSD_bot[0].getWidth(),
				GraphicalLayersAssets.LSD_bot[0].getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		img_bot.createGraphics().drawImage(GraphicalLayersAssets.LSD_bot[offcolorcounter], rop, 0, 0);
		BufferedImage img_top = new BufferedImage(GraphicalLayersAssets.LSD_top[0].getWidth(),
				GraphicalLayersAssets.LSD_top[0].getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		img_top.createGraphics().drawImage(GraphicalLayersAssets.LSD_top[offcolorcounter], rop, 0, 0);
		scales = new float[] { 1f, 1f, 1f, ((float) gradientcounter) / 200 };
		rop = new RescaleOp(scales, offsets, null);
		img_bot.createGraphics().drawImage(GraphicalLayersAssets.LSD_bot[maincolorcounter], rop, 0, 0);
		img_top.createGraphics().drawImage(GraphicalLayersAssets.LSD_top[maincolorcounter], rop, 0, 0);

		// Draws the different layers
		lsdRow(g, width, height, -15, counter_1, true, img_bot, img_top);
		lsdRow(g, width, height, -20, counter_2, false, img_bot, img_top);
		lsdRow(g, width, height, -30, counter_3, true, img_bot, img_top);
		lsdRow(g, width, height, -50, counter_4, false, img_bot, img_top);
		lsdRow(g, width, height, -62, counter_5, true, img_bot, img_top);
		lsdRow(g, width, height, -70, counter_6, true, img_bot, img_top);
		lsdRow(g, width, height, -76, counter_7, true, img_bot, img_top);
	}

	private void lsdRow(Graphics2D g, int width, int height, int offset, float counter, boolean direction,
			BufferedImage bot, BufferedImage top) {
		for (int i = (int) (direction ? -counter : counter)
				- GraphicalLayersAssets.LSD_top[0].getWidth(); i <= width; i += GraphicalLayersAssets.LSD_top[0]
						.getWidth())
			g.drawImage(top, i, offset, null);
		
		for (int i = (int) (direction ? -counter : counter)
				- GraphicalLayersAssets.LSD_bot[0].getWidth(); i <= width; i += GraphicalLayersAssets.LSD_bot[0]
						.getWidth())
			g.drawImage(bot, i, height - GraphicalLayersAssets.LSD_bot[0].getHeight() - offset, null);
	}

}
