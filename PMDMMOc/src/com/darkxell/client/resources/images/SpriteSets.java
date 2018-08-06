package com.darkxell.client.resources.images;

import com.darkxell.client.resources.images.others.FontSpriteSet;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.client.resources.images.tilesets.DungeonMapTileset;
import com.darkxell.client.resources.images.tilesets.ItemsSpriteset;

/** Class that holds SpriteSets used in the whole project. */
public final class SpriteSets
{

	public static final CommonDungeonTileset dungeonCommon = new CommonDungeonTileset();
	public static final DungeonMapTileset dungeonMap = new DungeonMapTileset();
	public static final FontSpriteSet font = new FontSpriteSet();
	public static final ItemsSpriteset items = new ItemsSpriteset();

	/** Loads SpriteSets used in the whole project. */
	public static void loadCommon()
	{

	}

	private SpriteSets()
	{}

}
