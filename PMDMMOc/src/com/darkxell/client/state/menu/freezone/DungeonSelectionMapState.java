package com.darkxell.client.state.menu.freezone;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.client.resources.images.MapResources;
import com.darkxell.client.state.AbstractState;
import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.dungeon.DungeonRegistry;

public class DungeonSelectionMapState extends AbstractState {

	private ArrayList<Dungeon> dungeonslist;

	public DungeonSelectionMapState() {
		super();
		ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();
		dungeons.addAll(DungeonRegistry.list());
		dungeons.sort(Comparator.naturalOrder());
		this.dungeonslist = dungeons;
	}

	@Override
	public void onKeyPressed(short key) {
	}

	@Override
	public void onKeyReleased(short key) {
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		g.drawImage(MapResources.GLOBALMAP, 0, 0, null);
	}

	@Override
	public void update() {
	}

}
