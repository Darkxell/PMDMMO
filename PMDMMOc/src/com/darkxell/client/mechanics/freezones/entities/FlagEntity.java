package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.image.Sprites.FreezoneEntitySprites;

public class FlagEntity extends FreezoneEntity
{

	private byte state = 0;
	private byte counter = 0;

	public FlagEntity(double x, double y)
	{
		super(false, false, x, y);
	}

	@Override
	public void onInteract()
	{}

	@Override
	public void print(Graphics2D g)
	{
		g.drawImage(FreezoneEntitySprites.flag.getSprite(this.state), (int) (super.posX * 8), (int) (super.posY * 8), null);
	}

	@Override
	public void update()
	{
		++counter;
		if (counter >= 10)
		{
			counter = 0;
			if (state >= 5) state = 0;
			else++state;
		}
	}

}
