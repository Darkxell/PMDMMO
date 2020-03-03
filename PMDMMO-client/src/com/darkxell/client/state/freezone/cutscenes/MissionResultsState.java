package com.darkxell.client.state.freezone.cutscenes;

import java.util.ArrayList;
import java.util.List;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.model.cutscene.CutsceneCreationModel;
import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.CutsceneModel;
import com.darkxell.client.model.cutscene.CutscenePokemonModel;
import com.darkxell.client.model.cutscene.common.CutsceneDialogScreenModel;
import com.darkxell.client.model.cutscene.end.CutsceneEndModel;
import com.darkxell.client.model.cutscene.end.CutsceneEndType;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;
import com.darkxell.client.model.cutscene.event.DialogCutsceneEventModel;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.client.resources.image.pokemon.portrait.PortraitEmotion;
import com.darkxell.client.state.dialog.PokemonDialogScreen.DialogPortraitLocation;
import com.darkxell.client.state.dungeon.DungeonEndState;
import com.darkxell.client.state.freezone.CutsceneState;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.mission.MissionReward;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FreezoneInfo;

public class MissionResultsState extends CutsceneState {

    private static class NextMissionCutsceneEndModel extends CutsceneEndModel {

        public NextMissionCutsceneEndModel() {
            super(CutsceneEndType.NEXT_MISSION);
        }

        @Override
        protected CutsceneEndModel copyChild() {
            return new NextMissionCutsceneEndModel();
        }

        @Override
        public CutsceneEnd build(Cutscene cutscene) {
            return new NextMissionCutsceneEnd(cutscene, this);
        }
    }

    private static class NextMissionCutsceneEnd extends CutsceneEnd {

        public MissionResultsState state;

        public NextMissionCutsceneEnd(Cutscene cutscene, CutsceneEndModel model) {
            super(cutscene, model);
        }

        @Override
        public void onCutsceneEnd() {
            super.onCutsceneEnd();
            if (this.state.missions.size() <= 1)
                DungeonEndState.finish();
            else {
                Persistence.cutsceneState = new MissionResultsState(
                        this.state.missions.subList(1, this.state.missions.size()));
                Persistence.cutsceneState.cutscene.creation.create();
                Persistence.stateManager.setState(Persistence.cutsceneState);
            }
        }

    }

    private static Cutscene createCutscene(Mission mission) {
        boolean has2Pks = mission.has2Pokemon();

        ItemRegistry items = Registries.items();

        CutscenePokemonModel clientEntity = new CutscenePokemonModel(2, has2Pks ? 35. : 37.5, 30., null,
                mission.getClientPokemon(), PokemonSpriteState.IDLE, Direction.SOUTH, false);
        CutscenePokemonModel targetEntity = has2Pks
                ? new CutscenePokemonModel(3, 40., 30., null, mission.getTargetPokemon(), PokemonSpriteState.IDLE,
                        Direction.SOUTH, false)
                : null;

        ArrayList<CutsceneEntityModel> entities = new ArrayList<>();
        entities.add(new CutscenePokemonModel(1, 37.5, 35., 0, null, PokemonSpriteState.IDLE, Direction.NORTH, false));
        entities.add(clientEntity);
        if (has2Pks)
            entities.add(targetEntity);
        CutsceneCreationModel creation = new CutsceneCreationModel(FreezoneInfo.OFFICE, 37.5, 32.5, true, true,
                entities);

        Message thank = new Message("mission.thank." + mission.getMissiontype()).addReplacement("<item>",
                items.find(mission.getItemid()).name());
        if (has2Pks)
            thank.addReplacement("<pokemon>", Registries.species().find(mission.getTargetPokemon()).speciesName());
        CutsceneDialogScreenModel screen1 = new CutsceneDialogScreenModel("mission.thank." + mission.getMissiontype(),
                true, 2, PortraitEmotion.Normal, DialogPortraitLocation.BOTTOM_RIGHT);

        ArrayList<CutsceneDialogScreenModel> screens = new ArrayList<>();
        screens.add(screen1);

        MissionReward rewards = mission.getRewards();
        if (rewards.getMoney() > 0)
            screens.add(
                    new CutsceneDialogScreenModel(
                            new Message("mission.reward.money")
                                    .addReplacement("<money>", String.valueOf(rewards.getMoney())).toString(),
                            false, null, null, null));
        for (int i = 0; i < rewards.getItems().length; ++i)
            screens.add(new CutsceneDialogScreenModel(
                    new Message("mission.reward.item")
                            .addReplacement("<item>", items.find(rewards.getItems()[i]).name())
                            .addReplacement("<quantity>", String.valueOf(rewards.getQuantities()[i])).toString(),
                    false, null, null, null));
        if (rewards.getPoints() > 0)
            screens.add(
                    new CutsceneDialogScreenModel(
                            new Message("mission.reward.points")
                                    .addReplacement("<points>", String.valueOf(rewards.getPoints())).toString(),
                            false, null, null, null));

        ArrayList<CutsceneEventModel> events = new ArrayList<>();
        events.add(new DialogCutsceneEventModel(1, false, screens));

        return new Cutscene(new CutsceneModel("Mission Results", creation, events, new NextMissionCutsceneEndModel()));
    }

    private List<Mission> missions;

    public MissionResultsState(List<Mission> missions) {
        super(createCutscene(missions.get(0)));
        this.missions = missions;
        ((NextMissionCutsceneEnd) this.cutscene.onFinish).state = this;
    }

}
