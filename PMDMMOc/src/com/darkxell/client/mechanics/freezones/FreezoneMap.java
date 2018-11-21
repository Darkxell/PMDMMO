package com.darkxell.client.mechanics.freezones;

import static com.darkxell.client.resources.images.tilesets.AbstractFreezoneTileset.TILE_SIZE;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.input.SAXBuilder;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.freezones.entities.OtherPlayerEntity;
import com.darkxell.client.renderers.EntityRendererHolder;
import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.tilesets.AbstractFreezoneTileset;
import com.darkxell.common.util.Logger;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.JsonValue;

/** A tiled map of a freezone. Freezones are the areas where you can move freely and don't have to fight. */
public abstract class FreezoneMap
{

	public FreezoneTile[] tiles;
	/** The width of the map, in tiles. */
	public int mapWidth;
	/** The height of the map, in tiles. */
	public int mapHeight;

	public String freezonebgm = "";

	public final FreezoneInfo info;
	public final int defaultX, defaultY;

	/** List the entities in this map. Note that the player isn't actually an entity. */
	private ArrayList<FreezoneEntity> entities = new ArrayList<>();

	public ArrayList<TriggerZone> triggerzones = new ArrayList<>();

	public final EntityRendererHolder<FreezoneEntity> entityRenderers = new EntityRendererHolder<>();
	public final EntityRendererHolder<CutsceneEntity> cutsceneEntityRenderers = new EntityRendererHolder<>();

	public FreezoneMap(String xmlfilepath, int defaultX, int defaultY, FreezoneInfo info)
	{

		this.loadFreezoneData(xmlfilepath);

		this.defaultX = defaultX;
		this.defaultY = defaultY;
		this.info = info;
	}

	protected void loadFreezoneData(String xmlfilepath)
	{
		InputStream is = Res.get(xmlfilepath);
		SAXBuilder builder = new SAXBuilder();
		org.jdom2.Element rootelement;
		try
		{
			rootelement = builder.build(is).getRootElement();
			this.mapWidth = Integer.parseInt(rootelement.getChild("width").getText()) / TILE_SIZE;
			this.mapHeight = Integer.parseInt(rootelement.getChild("height").getText()) / TILE_SIZE;
			List<org.jdom2.Element> tiles = rootelement.getChild("tiles").getChildren();
			this.tiles = new FreezoneTile[mapWidth * mapHeight];
			for (int i = 0; i < this.tiles.length; i++)
				this.tiles[i] = new FreezoneTile(FreezoneTile.TYPE_WALKABLE, null);

			HashMap<String, AbstractFreezoneTileset> tilesets = new HashMap<>();
			AbstractFreezoneTileset defaulttileset = null;
			for (org.jdom2.Element element : tiles)
			{
				int refferingTileID = (mapWidth * (Integer.parseInt(element.getAttributeValue("y")) / TILE_SIZE))
						+ (Integer.parseInt(element.getAttributeValue("x")) / TILE_SIZE);
				if (element.getAttributeValue("bgName").equals("terrain"))
				{
					this.tiles[refferingTileID].type = element.getAttributeValue("xo").equals("0") ? FreezoneTile.TYPE_SOLID : FreezoneTile.TYPE_WALKABLE;
				} else
				{
					String tileset = element.getAttributeValue("bgName");
					if (!tilesets.containsKey(tileset))
					{
						tilesets.put(tileset, AbstractFreezoneTileset.getTileSet(tileset, this.mapWidth * TILE_SIZE, this.mapHeight * TILE_SIZE));
						if (defaulttileset == null) defaulttileset = tilesets.get(tileset);
					}
					this.tiles[refferingTileID].sprite = tilesets.get(tileset).get(Integer.parseInt(element.getAttributeValue("xo")) / TILE_SIZE,
							Integer.parseInt(element.getAttributeValue("yo")) / TILE_SIZE);
				}
			}
			for (FreezoneTile t : this.tiles)
				if (t.sprite == null) t.sprite = defaulttileset.getDefault();
		} catch (Exception e)
		{
			Logger.e("Could not build freezonemap from XML file : " + e);
			e.printStackTrace();
		}
	}

	public void addEntity(FreezoneEntity entity)
	{
		this.entities.add(entity);
		this.entityRenderers.register(entity, entity.createRenderer());
	}

	@SuppressWarnings("unchecked")
	public ArrayList<FreezoneEntity> entities()
	{
		return (ArrayList<FreezoneEntity>) this.entities.clone();
	}

	public void removeEntity(FreezoneEntity entity)
	{
		this.entityRenderers.unregister(entity);
		entities.remove(entity);
	}

	private int flushcounter = 0;
	private static final long FLUSHTIMEOUT = 1000000000;

	public void update()
	{
		Persistance.currentplayer.update();
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
		if (flushcounter >= 120)
		{
			flushcounter = 0;
			long ct = System.nanoTime();
			for (int i = 0; i < entities.size(); ++i)
				if (entities.get(i) instanceof OtherPlayerEntity && ((OtherPlayerEntity) entities.get(i)).lastupdate < ct - FLUSHTIMEOUT)
				{
					this.removeEntity(entities.get(i));
					--i;
				}
		} else++flushcounter;
	}

	public byte getTileTypeAt(double x, double y)
	{
		int calc = mapWidth * (int) y + (int) x;
		if (calc >= this.tiles.length || calc < 0) return FreezoneTile.TYPE_WALKABLE;
		return this.tiles[calc].type;
	}

	/** Update the OtherPlayer entity destinations and last update timestamp according to the parsed json values for the specified entity. */
	public void updateOtherPlayers(JsonValue data)
	{
		String dataname = data.asObject().getString("name", "");
		if (Persistance.player.name().equals(dataname)) return;
		double pfx = data.asObject().getDouble("posfx", 0d);
		double pfy = data.asObject().getDouble("posfy", 0d);
		int spriteID = Integer.parseInt(data.asObject().getString("currentpokemon", "0"));
		boolean found = false;
		if (!dataname.equals(""))
		{
			for (int i = 0; i < entities.size(); i++)
				if (entities.get(i) instanceof OtherPlayerEntity && ((OtherPlayerEntity) entities.get(i)).name.equals(dataname))
				{
					OtherPlayerEntity etty = (OtherPlayerEntity) entities.get(i);
					etty.applyServerUpdate(pfx, pfy, spriteID);
					found = true;
					break;
				}
			if (!found) this.addEntity(new OtherPlayerEntity(pfx, pfy, spriteID, dataname, System.nanoTime()));
		}
	}

	/** Returns the additionnal informations not related to this instance about this freezone. */
	public FreezoneInfo getInfo()
	{
		return this.info;
	}

	/** @return Default X position for a Player in this Map. */
	public int defaultX()
	{
		return this.defaultX;
	}

	/** @return Default Y position for a Player in this Map. */
	public int defaultY()
	{
		return this.defaultY;
	}

}
