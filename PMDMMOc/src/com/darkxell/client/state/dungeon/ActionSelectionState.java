package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.GameUtil;

public class ActionSelectionState extends DungeonSubState
{

	private static final BufferedImage arrows = Res.getBase("resources/tilesets/diagonal-arrow.png");

	public ActionSelectionState(DungeonState parent)
	{
		super(parent);
	}

	@Override
	public void onKeyPressed(short key)
	{
		// TODO move this to update() when MovementState is done
		if (this.parent.diagonal)
		{
			if (key == Keys.KEY_UP && Keys.isPressed(Keys.KEY_RIGHT)) this.parent.player.tryMoveTo(GameUtil.NORTHEAST);
			if (Keys.isPressed(Keys.KEY_UP) && key == Keys.KEY_RIGHT) this.parent.player.tryMoveTo(GameUtil.NORTHEAST);

			if (key == Keys.KEY_DOWN && Keys.isPressed(Keys.KEY_RIGHT)) this.parent.player.tryMoveTo(GameUtil.SOUTHEAST);
			if (Keys.isPressed(Keys.KEY_DOWN) && key == Keys.KEY_RIGHT) this.parent.player.tryMoveTo(GameUtil.SOUTHEAST);

			if (key == Keys.KEY_DOWN && Keys.isPressed(Keys.KEY_LEFT)) this.parent.player.tryMoveTo(GameUtil.SOUTHWEST);
			if (Keys.isPressed(Keys.KEY_DOWN) && key == Keys.KEY_LEFT) this.parent.player.tryMoveTo(GameUtil.SOUTHWEST);

			if (key == Keys.KEY_UP && Keys.isPressed(Keys.KEY_LEFT)) this.parent.player.tryMoveTo(GameUtil.NORTHWEST);
			if (Keys.isPressed(Keys.KEY_UP) && key == Keys.KEY_LEFT) this.parent.player.tryMoveTo(GameUtil.NORTHWEST);
		} else
		{
			if (key == Keys.KEY_UP) this.parent.player.tryMoveTo(GameUtil.NORTH);
			if (key == Keys.KEY_DOWN) this.parent.player.tryMoveTo(GameUtil.SOUTH);
			if (key == Keys.KEY_LEFT) this.parent.player.tryMoveTo(GameUtil.WEST);
			if (key == Keys.KEY_RIGHT) this.parent.player.tryMoveTo(GameUtil.EAST);
		}
	}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{

		if (this.parent.diagonal) g.drawImage(arrows, this.parent.player.tile.x * AbstractDungeonTileset.TILE_SIZE - arrows.getWidth() / 2
				+ AbstractDungeonTileset.TILE_SIZE / 2, this.parent.player.tile.y * AbstractDungeonTileset.TILE_SIZE - arrows.getHeight() / 2
				+ AbstractDungeonTileset.TILE_SIZE / 2, null);
	}

	@Override
	public void update()
	{}

}
