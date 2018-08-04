package com.darkxell.client.renderers.floor;

import java.util.HashSet;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.item.ItemID;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;

public class FloorVisibility
{

	private HashSet<Tile> currentlyVisible = new HashSet<Tile>();
	public final Floor floor;
	private HashSet<Tile> itemTiles = new HashSet<Tile>();
	private HashSet<Tile> seenTiles = new HashSet<Tile>();

	public FloorVisibility()
	{
		this.floor = Persistance.floor;
	}

	public boolean hasVisibleItem(Tile tile)
	{
		if (this.isXrayOn()) return true;
		return this.itemTiles.contains(tile);
	}

	public boolean isMapVisible(DungeonPokemon pokemon)
	{
		if (this.isXrayOn()) return true;
		return pokemon.player() == Persistance.player || this.currentlyVisible.contains(pokemon.tile());
	}

	public boolean isVisible(DungeonPokemon pokemon)
	{
		if (Persistance.floor.data.shadows() == FloorData.NO_SHADOW) return true;
		return this.isMapVisible(pokemon);
	}

	public boolean isVisible(Tile tile)
	{
		return this.seenTiles.contains(tile);
	}

	public boolean isXrayOn()
	{
		ItemStack item = Persistance.dungeonState.getCameraPokemon().getItem();
		return item != null && item.item().id == ItemID.XRaySpecs;
	}

	public void onCameraMoved()
	{
		if (Persistance.dungeonState == null) return;
		DungeonPokemon camera = Persistance.dungeonState.getCameraPokemon();
		if (camera == null) return;
		this.currentlyVisible.clear();

		Tile t = camera.tile();
		Room r = this.floor.roomAt(t.x, t.y);
		if (r == null)
		{
			this.visit(t);
			for (Direction direction : Direction.directions)
				this.visit(t.adjacentTile(direction));

			if (this.floor.dungeon.dungeon().getData(this.floor.id).shadows() != FloorData.DENSE_SHADOW)
				for (Tile corner : new Tile[] { t.adjacentTile(Direction.NORTHWEST), t.adjacentTile(Direction.SOUTHWEST), t.adjacentTile(Direction.SOUTHEAST),
						t.adjacentTile(Direction.NORTHEAST) })
				for (Direction direction : Direction.directions)
				this.visit(corner.adjacentTile(direction));
		} else
		{
			for (Tile tile : r.listTiles())
				this.visit(tile);
			for (Tile tile : r.outline())
				this.visit(tile);
		}
	}

	public void onItemremoved(Tile tile)
	{
		if (!this.isXrayOn()) this.itemTiles.remove(tile);
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
