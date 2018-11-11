package com.darkxell.client.state;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;
import com.darkxell.client.mechanics.freezones.zones.BaseInsideFreezone;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.BackgroundSeaLayer;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;

public class OpenningState extends AbstractState {

	private BackgroundSeaLayer background = new BackgroundSeaLayer(true);
	private boolean ismusicset = false;
	private int textblink = 0;

	@Override
	public void onKeyPressed(Key key) {
		if (key == Key.ATTACK)
			startGame(Persistance.player.getData().storyposition);
	}

	@Override
	public void onKeyReleased(Key key) {
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		background.render(g, width, height);
		g.drawImage(Sprites.Res_Hud.gametitle.image(), width / 2 - Sprites.Res_Hud.gametitle.image().getWidth() / 2,
				height / 2 - Sprites.Res_Hud.gametitle.image().getHeight() / 2, null);
		if (textblink >= 50) {
			String press = "Press attack (default D) to continue.";
			TextRenderer.render(g, press, width / 2 - TextRenderer.width(press) / 2, height / 4 * 3);
		}
	}

	@Override
	public void update() {
		if (!ismusicset) {
			ismusicset = true;
			Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong("intro.mp3"));
		}

		background.update();
		++textblink;
		if (textblink >= 100)
			textblink = 0;
	}

	/**
	 * A switch case to launch the game in the right conditions depending on the
	 * parsed storyposition.
	 */
	private void startGame(int storyposition) {
		Logger.i("Triggered the OpeningState launch method with storyposition : " + storyposition);
		switch (storyposition) {
		default:
			StateManager.setExploreState(new BaseInsideFreezone(), Direction.SOUTH, -1, -1, true);
			break;
		case 0:
			Logger.e("Openning state could not determine what to do, storyposition is 0.");
			Logger.w("Moved the player to base freezone.");
			StateManager.setExploreState(new BaseFreezone(), Direction.SOUTH, -1, -1, true);
			break;
		case 1:
			// Starter selected, ready to play awakening cutscene
			CutsceneManager.playCutscene("startingwoods/introduction", true);
			break;
		case 2:
			// Tiny Woods failed, ready to play cutscene
			CutsceneManager.playCutscene("startingwoods/tinywoodsfailed", true);
			break;
		case 3:
			// Tiny Woods completed, ready to play cutscene
			CutsceneManager.playCutscene("startingwoods/solve", true);
			break;
		case 4:
			// TODO:Formed a rescue team. Is waking up in base, and is having recalls of yesterday.
			CutsceneManager.playCutscene("base/wakeup1", true);
			break;
		case 5:
			// TODO:Rescue team created after saving caterpie, recieved mail
			// from magneti and went to thunderwave cave
			break;
		case 38:
			// TODO:Had the fugitive cutscene. Spawns in front of your house
			// early in the morning before fuging to prepare.
			break;
		case 40:
			// TODO:Started the fugue, is at the entrance of lapis cave.
			break;
		case 42:
			// TODO:Exited lapis cave, is in front of mount blaze and rock path.
			break;
		case 45:
			// TODO:Finished mount blaze, is in the safe room before the peak
			break;
		case 50:
			// TODO:Defeated sulfura. Is in front of the frosty forest and snow
			// path.
			break;
		case 51:
			// TODO:Finished frosty forest, is at the frosty grotto checkpoint.
			break;
		case 52:
			// TODO:Defeated articuno, is in front of mt freeze entrance
			break;
		case 53:
			// TODO:Finished the first part of mount freeze,is at the checkpoint
			break;
		case 100:
			// TODO:You cleared magma cavern up to the checkpoint. Loading this
			// storyposition will put you in the magma cavern checkpoint.
			break;
		case 115:
			// TODO:Finished the first part of skytower. Is at the sky tower
			// checkpoint.
			break;

		}
	}

}
