package com.darkxell.client.renderers.layers;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.resources.images.Sprites.GraphicalLayersAssets;

public class BackgroundSeaLayer extends AbstractGraphiclayer
{

	private int cloudsposition = 0;
	private int counter_clouds = 0;
	private int counter_horizon = 0;
	private int counter_upcamera = 0;
	private int counter_waves = 0;
	private int horizonstate = 0;
	private int upcamera = 0;
	private int wavesstate = 0;

	public BackgroundSeaLayer(boolean rising)
	{
		if (!rising) upcamera = 2000;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		g.setColor(new Color(32, 184, 248));
		g.fillRect(0, 0, width, height);
		int cumulatedheight = -(GraphicalLayersAssets.Sea.clouds().getHeight() + GraphicalLayersAssets.Sea.horizon(horizonstate).getHeight()
				+ GraphicalLayersAssets.Sea.waves(wavesstate).getHeight()) + height + upcamera;
		if (cumulatedheight > 0) cumulatedheight = 0;
		g.drawImage(GraphicalLayersAssets.Sea.clouds(), -cloudsposition, cumulatedheight, null);
		if (GraphicalLayersAssets.Sea.clouds().getWidth() - cloudsposition < width)
			g.drawImage(GraphicalLayersAssets.Sea.clouds(), -cloudsposition + GraphicalLayersAssets.Sea.clouds().getWidth(), cumulatedheight, null);
		cumulatedheight += GraphicalLayersAssets.Sea.clouds().getHeight();
		for (int i = 0; i < width; i += 48)
			g.drawImage(GraphicalLayersAssets.Sea.horizon(horizonstate), i, cumulatedheight, null);
		cumulatedheight += GraphicalLayersAssets.Sea.horizon(horizonstate).getHeight();
		for (int i = 0; i < width; i += 48)
			g.drawImage(GraphicalLayersAssets.Sea.waves(wavesstate), i, cumulatedheight, null);
	}

	@Override
	public void update()
	{
		// CLOUDS
		++counter_clouds;
		if (counter_clouds >= 6)
		{
			counter_clouds = 0;
			++cloudsposition;
		}
		if (cloudsposition >= GraphicalLayersAssets.Sea.clouds().getWidth()) cloudsposition = 0;
		// HORIZON
		++counter_horizon;
		if (counter_horizon >= 12)
		{
			if (horizonstate >= GraphicalLayersAssets.Sea.horizonCount() - 1) horizonstate = 0;
			else++horizonstate;
			counter_horizon = 0;
		}
		// WATER
		++counter_waves;
		if (counter_waves >= 19)
		{
			if (wavesstate >= GraphicalLayersAssets.Sea.wavesCount() - 1) wavesstate = 0;
			else++wavesstate;
			counter_waves = 0;
		}
		// UPCAMERA
		if (upcamera < 2000)
		{
			++counter_upcamera;
			if (counter_upcamera >= 3)
			{
				counter_upcamera = 0;
				++upcamera;
			}
		}
	}

}
