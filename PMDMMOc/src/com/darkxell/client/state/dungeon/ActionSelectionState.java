package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.state.dungeon.PokemonTravelState.Travel;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.GameUtil;

public class ActionSelectionState extends DungeonSubState
{

	private static final BufferedImage arrows = Res.getBase("resources/tilesets/diagonal-arrow.png");

	public ActionSelectionState(DungeonState parent)
	{
		super(parent);
	}

	public boolean checkMovement()
	{
		short direction = -1;

		if (Keys.isPressed(Keys.KEY_UP) && Keys.isPressed(Keys.KEY_RIGHT) && !Keys.isPressed(Keys.KEY_DOWN) && !Keys.isPressed(Keys.KEY_LEFT)) direction = GameUtil.NORTHEAST;
		else if (Keys.isPressed(Keys.KEY_DOWN) && Keys.isPressed(Keys.KEY_RIGHT) && !Keys.isPressed(Keys.KEY_UP) && !Keys.isPressed(Keys.KEY_LEFT)) direction = GameUtil.SOUTHEAST;
		else if (Keys.isPressed(Keys.KEY_DOWN) && Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_UP) && !Keys.isPressed(Keys.KEY_RIGHT)) direction = GameUtil.SOUTHWEST;
		else if (Keys.isPressed(Keys.KEY_UP) && Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_DOWN) && !Keys.isPressed(Keys.KEY_RIGHT)) direction = GameUtil.NORTHWEST;

		else if (!this.parent.diagonal)
		{
			if (Keys.isPressed(Keys.KEY_UP) && !Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_DOWN) && !Keys.isPressed(Keys.KEY_RIGHT)) direction = GameUtil.NORTH;
			else if (Keys.isPressed(Keys.KEY_DOWN) && !Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_UP) && !Keys.isPressed(Keys.KEY_RIGHT)) direction = GameUtil.SOUTH;
			else if (Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_UP) && !Keys.isPressed(Keys.KEY_DOWN) && !Keys.isPressed(Keys.KEY_RIGHT)) direction = GameUtil.WEST;
			else if (Keys.isPressed(Keys.KEY_RIGHT) && !Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_DOWN) && !Keys.isPressed(Keys.KEY_UP)) direction = GameUtil.EAST;
		}

		if (direction != -1 && this.parent.player.getDungeonPokemon().tryMoveTo(direction))
		{
			this.parent.setSubstate(new PokemonTravelState(this.parent, new Travel(this.parent.player.getDungeonPokemon(), direction)));
			return true;
		}
		return false;
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.parent.diagonal) g.drawImage(arrows, this.parent.player.getDungeonPokemon().tile.x * AbstractDungeonTileset.TILE_SIZE - arrows.getWidth() / 2
				+ AbstractDungeonTileset.TILE_SIZE / 2, this.parent.player.getDungeonPokemon().tile.y * AbstractDungeonTileset.TILE_SIZE - arrows.getHeight()
				/ 2 + AbstractDungeonTileset.TILE_SIZE / 2, null);
	}

	@Override
	public void update()
	{
		this.checkMovement();
	}
}
