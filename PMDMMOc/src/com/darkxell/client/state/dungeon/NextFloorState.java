package com.darkxell.client.state.dungeon;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.state.AbstractState;
import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.util.language.Message;

public class NextFloorState extends AbstractState {
	private static final int fade = 30, pause = 30, text = 60;
	private static final int total = 2 * fade + 2 * pause + text;

	private int counter = 0;
	public final int floor;
	public final Message t, dungeon = Persistance.dungeon.dungeon().name();

	public NextFloorState(int floor) {
		this.floor = floor;
		this.t = new Message(
				"stairs.floor." + (Persistance.dungeon.dungeon().direction == Dungeon.UP ? "up" : "down"))
						.addReplacement("<floor>", Integer.toString(this.floor));
	}

	@Override
	public void onKeyPressed(short key) {
	}

	@Override
	public void onKeyReleased(short key) {
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		int alpha = -1;
		if (this.counter <= fade || this.counter >= total - fade) {
			Persistance.dungeonState.render(g, width, height);
			alpha = (this.counter <= fade ? this.counter : total - this.counter) * 255 / fade;
		}
		if (this.counter >= fade + pause && this.counter <= total - fade) {
			TextRenderer.instance.render(g, this.t, width / 2 - TextRenderer.instance.width(this.t) / 2,
					height / 2 + TextRenderer.LINE_SPACING / 2);
			TextRenderer.instance.render(g, this.dungeon, width / 2 - TextRenderer.instance.width(this.dungeon) / 2,
					height / 2 - TextRenderer.CHAR_HEIGHT - TextRenderer.LINE_SPACING / 2);
			if (this.counter >= fade + pause + text)
				alpha = (this.counter - fade - pause - text) * 255 / pause;
		}
		if (alpha != -1) {
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, width, height);
		}
	}

	@Override
	public void update() {
		if (this.counter <= fade || this.counter >= total - fade)
			Persistance.dungeonState.update();
		++this.counter;
		if (this.counter >= total)
			Persistance.stateManager.setState(Persistance.dungeonState);
		if (this.counter == fade + pause) {
			Persistance.floor = Persistance.dungeon.currentFloor();
			Persistance.dungeonState = new DungeonState();
		}

	}

}
