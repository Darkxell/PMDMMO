package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.renderers.pokemon.DungeonPokemonRenderer;
import com.darkxell.common.dungeon.floor.ComplexRoom;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.FloorData;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.SquareRoom;
import com.darkxell.common.dungeon.floor.Tile;

public class ShadowRenderer extends AbstractRenderer
{
	public static final Color SHADOW = new Color(0, 0, 0, 128), VISIBLE = new Color(0, 0, 0, 0);

	public final Floor floor;
	private Graphics2D gs;
	private BufferedImage shadowBuffer;

	public ShadowRenderer()
	{
		super(0, 0, MasterDungeonRenderer.LAYER_SHADOWS);
		this.floor = Persistance.floor;
		this.shadowBuffer = new BufferedImage(this.floor.getWidth() * TILE_SIZE, this.floor.getHeight() * TILE_SIZE, BufferedImage.TYPE_INT_ARGB_PRE);
		this.gs = this.shadowBuffer.createGraphics();
		this.gs.setBackground(VISIBLE);
		this.gs.setColor(SHADOW);
	}

	private Area area(Rectangle screen, ComplexRoom room)
	{
		Area global = new Area(screen);

		for (SquareRoom r : room.subRooms())
		{
			Area a = this.area(screen, r);
			Area sub = new Area(screen);
			sub.subtract(a);
			global.subtract(sub);
		}

		return global;
	}

	private Area area(Rectangle screen, SquareRoom room)
	{
		int x = room.x * TILE_SIZE - TILE_SIZE * 4 / 5, y = room.y * TILE_SIZE - TILE_SIZE;
		int x2 = x + room.width * TILE_SIZE + TILE_SIZE * 13 / 8, y2 = y + room.height * TILE_SIZE + TILE_SIZE * 13 / 8;

		Area area = new Area(screen);
		area.subtract(new Area(new Rectangle(x + TILE_SIZE, y, x2 - x - TILE_SIZE * 2, y2 - y)));
		area.subtract(new Area(new Rectangle(x, y + TILE_SIZE, x2 - x, y2 - y - TILE_SIZE * 2)));

		area.subtract(new Area(new Ellipse2D.Double(x, y, TILE_SIZE * 2, TILE_SIZE * 2)));
		area.subtract(new Area(new Ellipse2D.Double(x, y2 - TILE_SIZE * 2, TILE_SIZE * 2, TILE_SIZE * 2)));
		area.subtract(new Area(new Ellipse2D.Double(x2 - TILE_SIZE * 2, y, TILE_SIZE * 2, TILE_SIZE * 2)));
		area.subtract(new Area(new Ellipse2D.Double(x2 - TILE_SIZE * 2, y2 - TILE_SIZE * 2, TILE_SIZE * 2, TILE_SIZE * 2)));

		return area;
	}

	private Area getRoomArea(Rectangle screen, Room r)
	{
		if (r instanceof SquareRoom) return this.area(screen, (SquareRoom) r);
		else if (r instanceof ComplexRoom) return this.area(screen, (ComplexRoom) r);
		return new Area(screen);
	}

	public void render(Graphics2D g, int width, int height)
	{
		Rectangle screen = new Rectangle((int) this.x(), (int) this.y(), width, height);

		int shadows = Persistance.dungeon.dungeon().getData(Persistance.floor.id).shadows();
		if (shadows != FloorData.NO_SHADOW)
		{
			Tile t = Persistance.dungeonState.getCameraPokemon().tile();
			Room r = t == null ? null : this.floor.roomAt(t.x, t.y);

			this.gs.clearRect(screen.x, screen.y, screen.width, screen.height);
			if (r != null)
			{
				Area area = this.getRoomArea(screen, r);
				this.gs.fill(area);
			} else
			{
				DungeonPokemonRenderer p = Persistance.dungeonState.pokemonRenderer.getRenderer(Persistance.dungeonState.getCameraPokemon());
				Area a = new Area(screen);
				if (shadows == FloorData.NORMAL_SHADOW)
					a.subtract(new Area(new Ellipse2D.Double(p.x() - TILE_SIZE * 5 / 2, p.y() + -TILE_SIZE * 5 / 2, TILE_SIZE * 5, TILE_SIZE * 5)));
				if (shadows == FloorData.DENSE_SHADOW)
					a.subtract(new Area(new Ellipse2D.Double(p.x() - TILE_SIZE * 3 / 2, p.y() + -TILE_SIZE * 3 / 2, TILE_SIZE * 3, TILE_SIZE * 3)));
				this.gs.fill(a);
			}
			g.drawImage(this.shadowBuffer, 0, 0, null);

			Area outside = new Area(screen);
			outside.subtract(new Area(new Rectangle(0, 0, this.floor.getWidth() * TILE_SIZE, this.floor.getHeight() * TILE_SIZE)));
			g.setColor(SHADOW);
			g.fill(outside);
		}
	}
}
