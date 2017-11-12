package com.darkxell.client.renderers.floor;

import java.util.HashSet;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.FloorData;
import com.darkxell.common.dungeon.floor.Room;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Directions;

public class FloorVisibility
{

	private HashSet<Tile> currentlyVisible = new HashSet<Tile>();
	public final Floor floor;
	private HashSet<Tile> itemTiles = new HashSet<Tile>();
	private HashSet<Tile> seenTiles = new HashSet<Tile>();

	public FloorVisibility()
	{
		this.floor = Persistance.floor;
		this.onCameraMoved();
	}

	public boolean hasVisibleItem(Tile tile)
	{
		return this.itemTiles.contains(tile);
	}

	public boolean isVisible(DungeonPokemon pokemon)
	{
		return pokemon.pokemon.player == Persistance.player || this.currentlyVisible.contains(pokemon.tile);
	}

	public boolean isVisible(Tile tile)
	{
		return this.seenTiles.contains(tile);
	}

	public void onCameraMoved()
	{
		if (Persistance.dungeonState == null) return;
		DungeonPokemon camera = Persistance.dungeonState.getCameraPokemon();
		if (camera == null) return;
		this.currentlyVisible.clear();

		Tile t = camera.tile;
		Room r = this.floor.roomAt(t.x, t.y);
		if (r == null)
		{
			this.visit(t);
			for (short direction : Directions.directions())
				this.visit(t.adjacentTile(direction));

			if (this.floor.dungeon.dungeon().getData(this.floor.id).shadows() != FloorData.DENSE_SHADOW) for (Tile corner : new Tile[]
			{ t.adjacentTile(Directions.NORTHWEST), t.adjacentTile(Directions.SOUTHWEST), t.adjacentTile(Directions.SOUTHEAST),
					t.adjacentTile(Directions.NORTHEAST) })
				for (short direction : Directions.directions())
					this.visit(corner.adjacentTile(direction));
		} else for (int x = r.x - 1; x <= r.maxX() + 1; ++x)
			for (int y = r.y - 1; y <= r.maxY() + 1; ++y)
				this.visit(this.floor.tileAt(x, y));
	}

	public void onItemremoved(Tile tile)
	{
		this.itemTiles.remove(tile);
	}

	@SuppressWarnings("unchecked")
	public HashSet<Tile> visibleTiles()
	{
		return (HashSet<Tile>) this.currentlyVisible.clone();
	}

	private void visit(Tile tile)
	{
		if (tile != null)
		{
			this.currentlyVisible.add(tile);
			this.seenTiles.add(tile);
			if (tile.getItem() != null) this.itemTiles.add(tile);
			else this.itemTiles.remove(tile);
		}
	}

}
