package com.darkxell.client.launchable;

import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.mechanics.freezone.Freezones;
import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.zones.FreezoneInfo;

/**
 * Class that sets the player in a default position depending on his
 * storyposition.
 */
public class StoryPositionSetup {

	private StoryPositionSetup() {
	}

	/**
	 * Sets the game in a predefined default state depending on the parsed
	 * storyposition.
	 * 
	 * @param storyposition    the player's storyposition, usually. This position
	 *                         will be used to place the player or play a cutscene
	 *                         accordingly.
	 * @param fromOpeningState true if this method is called from an openingState.
	 *                         This is usually mostly used on game startup, and will
	 *                         be false when this method is triggered at the end a a
	 *                         dungeon for example.
	 */
	public static void trigger(int storyposition, boolean fromOpeningState) {
		Logger.i("Triggered the Storyposition setup method with position : " + storyposition);
		boolean isAbnormal = false;
		switch (storyposition) {
		default:
			isAbnormal = true;
			break;
		case 1:
			// Starter selected, ready to play awakening cutscene
			if (fromOpeningState)
				CutsceneManager.playCutscene("startingwoods/introduction", true);
			else
				isAbnormal = true;
			break;
		case 2:
			// Tiny Woods failed, ready to play cutscene
			CutsceneManager.playCutscene("startingwoods/tinywoodsfailed", true);
			break;
		case 3:
			// Tiny Woods completed, ready to play cutscene
			CutsceneManager.playCutscene("startingwoods/found", true);
			break;
		case 4:
			// Formed a rescue team. Is waking up in base, and is having recalls
			// of yesterday.
			if (fromOpeningState)
				CutsceneManager.playCutscene("base/wakeup1", true);
			else
				isAbnormal = true;
			break;
		case 5:
			// Thunderwave Cave failed, restart
			CutsceneManager.playCutscene("magnetientrance", true);
			break;
		case 6:
			// magnemite saved, didn't have the dream yet.
			CutsceneManager.playCutscene("magnetifound", true);
			break;
		case 10:
			// dugtrio and dream with points condition
			if (Persistence.player.getData().points >= 10)
				CutsceneManager.playCutscene("base/predream", true);
			else
				isAbnormal = true;
			break;
		case 12:
			// Mt steel cutscenes
			CutsceneManager.playCutscene("skarmory/solve", true);
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

		if (isAbnormal) {
			Logger.i("Game could not determine special setup from "
					+ (fromOpeningState ? "Opening state" : "Dungeon end state") + ", storyposition is " + storyposition
					+ ".");
			Logger.i("Moved the player to base freezone.");
			StateManager.setExploreState(Freezones.loadMap(FreezoneInfo.BASEINSIDE), Direction.SOUTH, -1, -1, true);
		}

	}

}
