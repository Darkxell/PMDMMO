package com.darkxell.client.state.dungeon;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.mechanics.freezone.Freezones;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.freezone.CutsceneState;
import com.darkxell.client.state.map.DungeonFloorMap;
import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class NextFloorState extends TransitionState {
	private static Message[] createMessage(int floor) {
		Message[] m = new Message[2];
		m[0] = Persistence.dungeon.dungeon().name();
		m[1] = new Message(
				"stairs.floor." + (Persistence.dungeon.dungeon().direction == DungeonDirection.UP ? "up" : "down"))
						.addReplacement("<floor>", Integer.toString(floor));
		return m;
	}

	public static void resumeExploration() {
		Persistence.displaymap = new DungeonFloorMap();
		Persistence.dungeonState.floorVisibility.onCameraMoved();
		String ost = "dungeon-" + Persistence.floor.data.soundtrack() + ".mp3";
		if (Persistence.floor.data.isBossFloor()) ost = "boss.mp3";
		Persistence.soundmanager.setBackgroundMusic(SoundsHolder.getSong(ost));
	}

	public NextFloorState(AbstractState previous, int floor) {
		super(previous, null, createMessage(floor));
	}

	@Override
	public void onEnd() {
		super.onEnd();
		Persistence.eventProcessor().processPending();
	}

	@Override
	public void onTransitionHalf() {
		super.onTransitionHalf();
		Persistence.floor = Persistence.dungeon.currentFloor();
		/* if (Persistence.dungeonState != null) { for (DungeonPokemon p : Persistence.player.getDungeonTeam()) { DungeonPokemonRenderer r = Persistence.dungeonState.pokemonRenderer.getRenderer(p); if (r != null) r.sprite().setFacingDirection(Persistence.floor.teamSpawnDirection); } } */
		if (Persistence.floor.data.isBossFloor()
				&& Persistence.floor.data.bossFloor() != Persistence.player.storyPosition()) {

			for (DungeonPokemon p : Persistence.floor.listPokemon())
				if (!Persistence.player.isAlly(p.originalPokemon)) Persistence.floor.unsummonPokemon(p);

			Persistence.dungeon.eventProcessor
					.addToPending(new DungeonExitEvent(Persistence.floor, Persistence.player));
		}

		this.next = Persistence.dungeonState = new DungeonState();
		if (Persistence.floor.cutsceneIn != null && (Persistence.floor.cutsceneStorypos == -1
				|| Persistence.floor.cutsceneStorypos == Persistence.player.storyPosition())) {
			Cutscene c = CutsceneManager.loadCutscene(Persistence.floor.cutsceneIn);
			this.next = Persistence.cutsceneState = new CutsceneState(c);
			c.creation.create();
		} else if (Persistence.floor.data.isBossFloor()
				&& Persistence.floor.data.bossFloor() != Persistence.player.storyPosition()) {
			Cutscene c = CutsceneManager.loadCutscene("bosses/after_defeat");
			c.creation.freezone = Freezones.getFreezoneForBossFloor(Persistence.dungeon.dungeon(), Persistence.floor);
			this.next = Persistence.cutsceneState = new CutsceneState(c);
			c.creation.create();
		} else resumeExploration();
	}

}
