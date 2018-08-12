package com.darkxell.client.state.menu.freezone;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.mission.MissionReward;

public class MissionBoardState extends AbstractState {

	private AbstractState exploresource;
	private ArrayList<Mission> missions = new ArrayList<>();

	private int selectedmissionpos = 0;

	public MissionBoardState(AbstractState exploresource) {
		this.exploresource = exploresource;

		try {
			this.missions.add(new Mission("E", 2, 3, 3, 6, 1,
					new MissionReward(55, new int[] { 1 }, new int[] { 1 }, 5, null), Mission.TYPE_RESCUEME));
			this.missions.add(new Mission("A", 12, 14, 15, 71, 2,
					new MissionReward(70, new int[] { 1 }, new int[] { 1 }, 5, null), Mission.TYPE_DEFEAT));
		} catch (InvalidParammetersException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onKeyPressed(Key key) {
		switch (key) {
		case RUN:
			Persistance.stateManager.setState(new MissionBoardSelectionState(this.exploresource));
			break;
		case UP:
			if (selectedmissionpos > 0)
				selectedmissionpos--;
			break;
		case DOWN:
			if (selectedmissionpos < missions.size() - 1)
				selectedmissionpos++;
			break;
		default:
			break;
		}
	}

	@Override
	public void onKeyReleased(Key key) {

	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		this.exploresource.render(g, width, height);
		g.setColor(new Color(100, 100, 100, 100));
		g.fillRect(0, 0, width, height);

		int offsetx = 40, offsety = 50;
		int baseheight = 30, basewidth = width - (2 * offsetx);
		for (int i = 0; i < this.missions.size(); i++) {
			g.translate(offsetx, offsety + (i * baseheight + 15));
			drawMission(g, basewidth, baseheight, missions.get(i));
			if (i == selectedmissionpos) {
				g.setColor(Color.CYAN);
				g.drawRect(0, 0, basewidth, baseheight);
			}
			g.translate(-offsetx, -offsety);

		}

	}

	@Override
	public void update() {
		this.exploresource.update();
		if (selectedmissionpos >= missions.size())
			selectedmissionpos = missions.size();
	}

	private void drawMission(Graphics2D g, int width, int height, Mission mission) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, width, height);
		// 1st line
		TextRenderer.render(g, "<mission> Flavor text n" + mission.getMissiontype(), 5, 5);
		TextRenderer.render(g, mission.getDifficulty(), width - 10, 5);
		// 2nd line
		int dungeontextlength = TextRenderer.width(DungeonRegistry.find(mission.getDungeonid()).name());
		TextRenderer.setColor(Color.YELLOW);
		TextRenderer.render(g, DungeonRegistry.find(mission.getDungeonid()).name(), 5, 15);
		TextRenderer.render(g, "Floor " + mission.getFloor(), 15 + dungeontextlength, 15);
	}

}
