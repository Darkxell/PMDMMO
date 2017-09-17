package com.darkxell.client.renderers.layers;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.resources.images.GraphicalLayersAssets;

public class BackgroundSeaLayer extends AbstractGraphiclayer {

	private int counter_clouds = 0;
	private int cloudsposition = 0;
	private int counter_horizon = 0;
	private int horizonstate = 0;
	private int counter_waves = 0;
	private int wavesstate = 0;
	private int counter_upcamera = 0;
	private int upcamera = 0;

	@Override
	public void update() {
		// CLOUDS
		++counter_clouds;
		if (counter_clouds >= 6) {
			counter_clouds = 0;
			++cloudsposition;
		}
		if (cloudsposition >= GraphicalLayersAssets.WATER_clouds.getWidth())
			cloudsposition = 0;
		// HORIZON
		++counter_horizon;
		if (counter_horizon >= 12) {
			if (horizonstate >= GraphicalLayersAssets.WATER_horizon.length - 1)
				horizonstate = 0;
			else
				++horizonstate;
			counter_horizon = 0;
		}
		// WATER
		++counter_waves;
		if (counter_waves >= 19) {
			if (wavesstate >= GraphicalLayersAssets.WATER_waves.length - 1)
				wavesstate = 0;
			else
				++wavesstate;
			counter_waves = 0;
		}
		// UPCAMERA
		if (upcamera < 2000) {
			++counter_upcamera;
			if (counter_upcamera >= 3) {
				counter_upcamera = 0;
				++upcamera;
			}
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		g.setColor(new Color(32, 184, 248));
		g.fillRect(0, 0, width, height);
		int cumulatedheight = -(GraphicalLayersAssets.WATER_clouds.getHeight()
				+ GraphicalLayersAssets.WATER_horizon[horizonstate].getHeight()
				+ GraphicalLayersAssets.WATER_waves[wavesstate].getHeight()) + height + upcamera;
		if (cumulatedheight > 0)
			cumulatedheight = 0;
		g.drawImage(GraphicalLayersAssets.WATER_clouds, -cloudsposition, cumulatedheight, null);
		if (GraphicalLayersAssets.WATER_clouds.getWidth() - cloudsposition < width)
			g.drawImage(GraphicalLayersAssets.WATER_clouds,
					-cloudsposition + GraphicalLayersAssets.WATER_clouds.getWidth(), cumulatedheight, null);
		cumulatedheight += GraphicalLayersAssets.WATER_clouds.getHeight();
		for (int i = 0; i < width; i += 48)
			g.drawImage(GraphicalLayersAssets.WATER_horizon[horizonstate], i, cumulatedheight, null);
		cumulatedheight += GraphicalLayersAssets.WATER_horizon[horizonstate].getHeight();
		for (int i = 0; i < width; i += 48)
			g.drawImage(GraphicalLayersAssets.WATER_waves[wavesstate], i, cumulatedheight, null);
	}

}
