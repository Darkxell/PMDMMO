package com.darkxell.client.state.dungeon;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.dialog.NarratorDialogState;
import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.util.language.Message;

public class NextFloorState extends AbstractState
{
	private static final int fade = NarratorDialogState.FADETIME, pause = 30, text = 60;
	private static final int total = 2 * fade + 2 * pause + text;

	private int counter = 0;
	public final int floor;
	public final Message floorIndex, dungeon = Persistance.dungeon.dungeon().name();
	public final AbstractState previous;

	public NextFloorState(AbstractState previous, int floor)
	{
		this.previous = previous;
		this.floor = floor;
		this.floorIndex = new Message("stairs.floor." + (Persistance.dungeon.dungeon().direction == Dungeon.UP ? "up" : "down")).addReplacement("<floor>",
				Integer.toString(this.floor));
	}

	public int fading()
	{
		if (this.counter <= fade || this.counter >= total - fade) return (this.counter <= fade ? this.counter : total - this.counter) * 255 / fade;
		return 255;
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
		int alpha = -1;
		if (this.counter <= fade || this.counter >= total - fade)
		{
			if (this.counter <= fade && this.previous != null) this.previous.render(g, width, height);
			else if (this.counter > fade) Persistance.dungeonState.render(g, width, height);
			alpha = this.fading();
		}
		if (this.counter >= fade + pause && this.counter <= total - fade)
		{
			TextRenderer.render(g, this.floorIndex, width / 2 - TextRenderer.width(this.floorIndex) / 2, height / 2 + TextRenderer.lineSpacing() / 2);
			TextRenderer.render(g, this.dungeon, width / 2 - TextRenderer.width(this.dungeon) / 2,
					height / 2 - TextRenderer.height() - TextRenderer.lineSpacing() / 2);
			if (this.counter >= fade + pause + text) alpha = (this.counter - fade - pause - text) * 255 / pause;
		}
		if (alpha != -1)
		{
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, width, height);
		}
	}

	@Override
	public void update()
	{
		if (this.counter <= fade || this.counter >= total - fade) if (this.previous != null) this.previous.update();

		++this.counter;
		if (this.counter >= total)
		{
			Persistance.stateManager.setState(Persistance.dungeonState);
			Persistance.eventProcessor.processPending();
		}
		if (this.counter == fade + pause)
		{
			Persistance.floor = Persistance.dungeon.currentFloor();
			Persistance.dungeonState = new DungeonState();
			Persistance.dungeonState.floorVisibility.onCameraMoved();
			Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong("dungeon-" + Persistance.floor.data.soundtrack() + ".mp3"));
		}

	}

}
