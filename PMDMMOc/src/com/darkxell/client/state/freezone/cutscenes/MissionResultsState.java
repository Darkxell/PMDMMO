package com.darkxell.client.state.freezone.cutscenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.CutsceneCreation;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent.CutsceneDialogScreen;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.state.dungeon.DungeonEndState;
import com.darkxell.client.state.freezone.CutsceneState;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FreezoneInfo;

public class MissionResultsState extends CutsceneState
{

	private static class NextMissionCutsceneEnd extends CutsceneEnd
	{

		private MissionResultsState state;

		public NextMissionCutsceneEnd(Cutscene cutscene, MissionResultsState state)
		{
			super(cutscene);
			this.state = state;
		}

		@Override
		public void onCutsceneEnd()
		{
			super.onCutsceneEnd();
			if (this.state.missions.size() <= 1) DungeonEndState.finish();
			else
			{
				Persistance.cutsceneState = new MissionResultsState(this.state.missions.subList(1, this.state.missions.size()));
				Persistance.cutsceneState.cutscene.creation.create();
				Persistance.stateManager.setState(Persistance.cutsceneState);
			}
		}

		@Override
		public Element toXML()
		{
			return new Element("nextmissionresults");
		}

	}

	private static Cutscene createCutscene(Mission mission)
	{
		boolean has2Pks = mission.getMissiontype() == Mission.TYPE_DEFEAT || mission.getMissiontype() == Mission.TYPE_ESCORT
				|| mission.getMissiontype() == Mission.TYPE_RESCUEHIM;

		Pokemon pokemon1 = PokemonRegistry.find(mission.getPokemonid1()).generate(new Random(), 1);
		Pokemon pokemon2 = has2Pks ? PokemonRegistry.find(mission.getPokemonid2()).generate(new Random(), 1) : null;
		CutscenePokemon pk1Entity = new CutscenePokemon(3, has2Pks ? 35 : 37.5, 30, pokemon1, PokemonSpriteState.IDLE, Direction.SOUTH, false);
		CutscenePokemon pk2Entity = has2Pks ? new CutscenePokemon(3, 40, 30, pokemon2, PokemonSpriteState.IDLE, Direction.SOUTH, false) : null;

		ArrayList<CutsceneEntity> entities = new ArrayList<>();
		entities.add(new CutscenePokemon(1, 37.5, 35, Persistance.player.getTeamLeader(), PokemonSpriteState.IDLE, Direction.NORTH, false));
		entities.add(pk1Entity);
		if (has2Pks) entities.add(pk2Entity);
		CutsceneCreation creation = new CutsceneCreation(FreezoneInfo.OFFICE, true, 37.5, 32.5, entities);

		CutsceneDialogScreen screen1 = new CutsceneDialogScreen(new Message("mission.thank." + mission.getMissiontype())
				.addReplacement("<pokemon>", pokemon1.getNickname()).addReplacement("<item>", ItemRegistry.find(mission.getItemid()).name()), 0,
				has2Pks ? pk2Entity : pk1Entity);
		ArrayList<CutsceneEvent> events = new ArrayList<>();
		events.add(new DialogCutsceneEvent(1, false, Arrays.asList(screen1)));

		return new Cutscene("Mission Results", creation, null, events);
	}

	private List<Mission> missions;

	public MissionResultsState(List<Mission> missions)
	{
		super(createCutscene(missions.get(0)));
		this.missions = missions;
		this.cutscene.onFinish = new NextMissionCutsceneEnd(this.cutscene, this);
	}

}
