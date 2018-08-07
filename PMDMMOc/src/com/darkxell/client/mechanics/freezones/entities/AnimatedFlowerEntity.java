package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.images.Sprites.FreezoneEntities;
import com.darkxell.client.resources.images.tilesets.AbstractFreezoneTileset;

public class AnimatedFlowerEntity extends FreezoneEntity
{

	private byte state = 0;
	private byte counter = 0;
	private boolean color;

	public AnimatedFlowerEntity(double x, double y, boolean isyellow)
	{
		super(false, false, x, y);
		color = isyellow;
		if (color) state = 4;
	}

	@Override
	public void onInteract()
	{}

	@Override
	public void print(Graphics2D g)
	{
		g.drawImage(color ? FreezoneEntities.YellowFlower.getImg(this.state) : FreezoneEntities.RedFlower.getImg(this.state),
				(int) (super.posX * AbstractFreezoneTileset.TILE_SIZE - 16), (int) (super.posY * AbstractFreezoneTileset.TILE_SIZE - 24), null);
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
