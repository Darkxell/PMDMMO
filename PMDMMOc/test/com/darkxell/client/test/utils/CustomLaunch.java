package com.darkxell.client.test.utils;

import java.util.Random;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.renderers.pokemon.OnFirstPokemonDraw;
import com.darkxell.client.resources.image.SpriteLoader;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.mainstates.LoginMainState;
import com.darkxell.client.state.quiz.PersonalityQuizDialog;
import com.darkxell.common.registry.Registries;

public class CustomLaunch {

    public static void main(String[] args) {
        Launcher.main(args);
        ((LoginMainState) Persistence.stateManager).launchOffline();

        if (args.length > 0) {
            if (args[0].startsWith("--dungeon")) {
                int dungeon = 0, floor = 0;
                if (args.length > 1)
                    dungeon = Integer.parseInt(args[1]);
                if (args.length > 2)
                    floor = Integer.parseInt(args[2]);
                OnFirstPokemonDraw.newDungeon();
                Persistence.dungeon = Registries.dungeons().find(dungeon).newInstance(new Random().nextLong());
                Persistence.dungeon.eventProcessor = new ClientEventProcessor(Persistence.dungeon);
                Persistence.dungeon.addPlayer(Persistence.player);
                SpriteLoader.loadDungeon(Persistence.dungeon);
                Persistence.floor = Persistence.dungeon.initiateExploration(floor);
                Persistence.stateManager.setState(new NextFloorState(null, floor));
            } else if (args[0].startsWith("--personality-test")) {
                Persistence.stateManager.setState(new TransitionState(null, new PersonalityQuizDialog().getLoadingState()));
            }
        }
    }

}
