package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

import com.darkxell.client.renderers.FloorRenderer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.PokemonD;
import com.darkxell.common.pokemon.PokemonRegistry;

public class DungeonState extends AbstractState
{

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
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		this.floorRenderer.drawFloor(g, this.xPos, this.yPos, width, height);
	}

	@Override
	public void update()
	{
		/* if (Keys.isPressed(Keys.KEY_UP)) yPos -= 5; if (Keys.isPressed(Keys.KEY_DOWN)) yPos += 5; if (Keys.isPressed(Keys.KEY_LEFT)) xPos -= 5; if (Keys.isPressed(Keys.KEY_RIGHT)) xPos += 5; */
	}

}
