package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.darkxell.client.renderers.FloorRenderer;
import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.PokemonD;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.GameUtil;

public class DungeonState extends AbstractState
{
	private static final BufferedImage arrows = Res.getBase("resources/tilesets/diagonal-arrow.png");

	private boolean diagonal;
	public final Floor floor;
	private final FloorRenderer floorRenderer;
	private PokemonD player;
	private int xPos = 185, yPos = 225;

	public DungeonState(Floor floor)
	{
		this.floor = floor;
		this.floorRenderer = new FloorRenderer(this.floor);
		this.player = new PokemonD(PokemonRegistry.find(1).generate(new Random(), 10));
		Point p = this.floor.getTeamSpawn();
		this.floor.tileAt(p.x, p.y).setPokemon(this.player);
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (key == Keys.KEY_DIAGONAL) this.diagonal = true;

		if (this.diagonal)
		{
			if (key == Keys.KEY_UP && Keys.isPressed(Keys.KEY_RIGHT)) this.player.tryMoveTo(GameUtil.NORTHEAST);
			if (Keys.isPressed(Keys.KEY_UP) && key == Keys.KEY_RIGHT) this.player.tryMoveTo(GameUtil.NORTHEAST);
			
			if (key == Keys.KEY_DOWN && Keys.isPressed(Keys.KEY_RIGHT)) this.player.tryMoveTo(GameUtil.SOUTHEAST);
			if (Keys.isPressed(Keys.KEY_DOWN) && key == Keys.KEY_RIGHT) this.player.tryMoveTo(GameUtil.SOUTHEAST);
			
			if (key == Keys.KEY_DOWN && Keys.isPressed(Keys.KEY_LEFT)) this.player.tryMoveTo(GameUtil.SOUTHWEST);
			if (Keys.isPressed(Keys.KEY_DOWN) && key == Keys.KEY_LEFT) this.player.tryMoveTo(GameUtil.SOUTHWEST);
			
			if (key == Keys.KEY_UP && Keys.isPressed(Keys.KEY_LEFT)) this.player.tryMoveTo(GameUtil.NORTHWEST);
			if (Keys.isPressed(Keys.KEY_UP) && key == Keys.KEY_LEFT) this.player.tryMoveTo(GameUtil.NORTHWEST);
		} else
		{
			if (key == Keys.KEY_UP) this.player.tryMoveTo(GameUtil.NORTH);
			if (key == Keys.KEY_DOWN) this.player.tryMoveTo(GameUtil.SOUTH);
			if (key == Keys.KEY_LEFT) this.player.tryMoveTo(GameUtil.WEST);
			if (key == Keys.KEY_RIGHT) this.player.tryMoveTo(GameUtil.EAST);
		}
	}

	@Override
	public void onKeyReleased(short key)
	{
		if (key == Keys.KEY_DIAGONAL) this.diagonal = false;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		g.translate(-this.xPos, -this.yPos);

		this.floorRenderer.drawFloor(g, this.xPos, this.yPos, width, height);

		if (this.diagonal) g.drawImage(arrows, this.player.tile.x * AbstractDungeonTileset.TILE_SIZE - arrows.getWidth() / 2 + AbstractDungeonTileset.TILE_SIZE
				/ 2, this.player.tile.y * AbstractDungeonTileset.TILE_SIZE - arrows.getHeight() / 2 + AbstractDungeonTileset.TILE_SIZE / 2, null);

		g.translate(this.xPos, this.yPos);
	}

	@Override
	public void update()
	{
		this.floorRenderer.update();
		/* if (Keys.isPressed(Keys.KEY_UP)) yPos -= 5; if (Keys.isPressed(Keys.KEY_DOWN)) yPos += 5; if (Keys.isPressed(Keys.KEY_LEFT)) xPos -= 5; if (Keys.isPressed(Keys.KEY_RIGHT)) xPos += 5; */
	}

}
