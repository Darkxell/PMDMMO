package com.darkxell.client.state.freezone.cutscenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.CutsceneCreation;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent.CutsceneDialogScreen;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait.PortraitEmotion;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.client.state.dungeon.DungeonEndState;
import com.darkxell.client.state.freezone.CutsceneState;
import com.darkxell.common.Registries;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.mission.MissionReward;
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

		public NextMissionCutsceneEnd(Cutscene cutscene, MissionResultsState state, boolean fadesOut)
		{
			super(cutscene, (String) null, fadesOut);
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
		protected String xmlName()
		{
			return "nextmissionresults";
		}

	}

	private static Cutscene createCutscene(Mission mission)
	{
		boolean has2Pks = mission.has2Pokemon();

		PokemonRegistry species = Registries.species();
		ItemRegistry items = Registries.items();

		Pokemon client = species.find(mission.getClientPokemon()).generate(new Random(), 1);
		Pokemon target = has2Pks ? species.find(mission.getTargetPokemon()).generate(new Random(), 1) : null;
		CutscenePokemon clientEntity = new CutscenePokemon(2, has2Pks ? 35 : 37.5, 30, client, PokemonSpriteState.IDLE, Direction.SOUTH, false);
		CutscenePokemon targetEntity = has2Pks ? new CutscenePokemon(3, 40, 30, target, PokemonSpriteState.IDLE, Direction.SOUTH, false) : null;

		ArrayList<CutsceneEntity> entities = new ArrayList<>();
		entities.add(new CutscenePokemon(1, 37.5, 35, Persistance.player.getTeamLeader(), PokemonSpriteState.IDLE, Direction.NORTH, false));
		entities.add(clientEntity);
		if (has2Pks) entities.add(targetEntity);
		CutsceneCreation creation = new CutsceneCreation(FreezoneInfo.OFFICE, true, true, 37.5, 32.5, entities);

		Message thank = new Message("mission.thank." + mission.getMissiontype()).addReplacement("<item>", items.find(mission.getItemid()).name());
		if (has2Pks) thank.addReplacement("<pokemon>", target.getNickname());
		CutsceneDialogScreen screen1 = new CutsceneDialogScreen(thank, PortraitEmotion.Normal, clientEntity, DialogPortraitLocation.BOTTOM_RIGHT);

		ArrayList<CutsceneDialogScreen> screens = new ArrayList<>();
		screens.add(screen1);

		MissionReward rewards = mission.getRewards();
		if (rewards.getMoney() > 0)
			screens.add(new CutsceneDialogScreen(new Message("mission.reward.money").addReplacement("<money>", String.valueOf(rewards.getMoney())),
					PortraitEmotion.Normal, null, null));
		for (int i = 0; i < rewards.getItems().length; ++i)
			screens.add(new CutsceneDialogScreen(new Message("mission.reward.item").addReplacement("<item>", items.find(rewards.getItems()[i]).name())
					.addReplacement("<quantity>", String.valueOf(rewards.getQuantities()[i])), PortraitEmotion.Normal, null, null));
		if (rewards.getPoints() > 0)
			screens.add(new CutsceneDialogScreen(new Message("mission.reward.points").addReplacement("<points>", String.valueOf(rewards.getPoints())),
					PortraitEmotion.Normal, null, null));

		ArrayList<CutsceneEvent> events = new ArrayList<>();
		events.add(new DialogCutsceneEvent(1, false, screens));

		return new Cutscene("Mission Results", creation, null, events);
	}

	private List<Mission> missions;

	public MissionResultsState(List<Mission> missions)
	{
		super(createCutscene(missions.get(0)));
		this.missions = missions;
		this.cutscene.onFinish = new NextMissionCutsceneEnd(this.cutscene, this, true);
	}

}
