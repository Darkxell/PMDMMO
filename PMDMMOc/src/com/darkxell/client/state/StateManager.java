package com.darkxell.client.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.Freezones;
import com.darkxell.client.renderers.pokemon.OnFirstPokemonDraw;
import com.darkxell.client.resources.image.SpriteLoader;
import com.darkxell.client.state.dungeon.AskServerForDungeonSeedState;
import com.darkxell.client.state.dungeon.DungeonEndState;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.state.freezone.FreezoneExploreState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.Registries;
import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.PrettyPrint;

/**
 * Describes how a statemanager is supposed to work. A statemanager is expected to display A very big portion of the
 * application, like for example the game / the login facilities...<br/>
 * Note that changing the statemanager removes the previous one completely, and should only be done when the user does
 * significants acts that changes the way he is going to interact with the application after, like for exemple logging
 * in.
 */
public abstract class StateManager {

    public abstract void onKeyPressed(KeyEvent e, Key key);

    /** @param e - MAY BE NULL (when window loses focus) */
    public abstract void onKeyReleased(KeyEvent e, Key key);

    public abstract void onKeyTyped(KeyEvent e);

    public abstract void onMouseClick(int x, int y);

    public abstract void onMouseMove(int x, int y);

    public abstract void onMouseRightClick(int x, int y);

    public abstract void render(Graphics2D g, int width, int height);

    public abstract void update();

    public void setState(AbstractState state) {
        Logger.e("Tried to call setState() on the wrong state manager!");
    }

    public AbstractState getCurrentState() {
        Logger.e("Tried to call getCurrentState() on the wrong state manager!");
        return null;
    }

    // State switching methods

    /**
     * @param freezone  - ID of a Map. If null or doesn't match a valid ID, this method will not do anything.
     * @param           xPos, yPos - Coordinates of the Player in the map. If any is -1, uses the default coordinates
     *                  for that map.
     * @param direction - The direction to face when entering the Freezone. null to keep current direction.
     */
    public static void setExploreState(FreezoneInfo freezone, Direction direction, int xPos, int yPos, boolean fading) {
        FreezoneMap map = Freezones.loadMap(freezone);
        if (map == null)
            return;
        setExploreState(map, direction, xPos, yPos, fading);
    }

    /**
     * @param map - Map to explore. If null, this method will not do anything.
     * @param     xPos, yPos - Coordinates of the Player in the map. If any is -1, uses the default coordinates for that
     *            map.
     */
    public static void setExploreState(FreezoneMap map, Direction direction, int xPos, int yPos, boolean fading) {
        AbstractState next;
        if (Persistence.stateManager.getCurrentState() instanceof FreezoneExploreState)
            next = Persistence.stateManager.getCurrentState();
        else
            next = new FreezoneExploreState();

        if (fading)
            Persistence.stateManager.setState(new TransitionState(Persistence.stateManager.getCurrentState(), next) {
                @Override
                public void onTransitionHalf() {
                    super.onTransitionHalf();
                    ((FreezoneExploreState) next).initiate(map, direction, xPos, yPos);
                }
            });
        else {
            ((FreezoneExploreState) next).initiate(map, direction, xPos, yPos);
            Persistence.stateManager.setState(next);
        }
    }

    /**
     * @param fadeOutState - State to fade out of.
     * @param dungeonID    - ID of a Dungeon. If doesn't match a valid ID, this method will not do anything.
     * @param seed         - Seed to use for RNG in the Dungeon.
     */
    public static void setDungeonState(AbstractState fadeOutState, int dungeonID, long seed) {
        OnFirstPokemonDraw.newDungeon();
        Persistence.dungeon = Registries.dungeons().find(dungeonID).newInstance(seed);
        Persistence.dungeon.eventProcessor = new ClientEventProcessor(Persistence.dungeon);
        Persistence.dungeon.addPlayer(Persistence.player);
        SpriteLoader.loadDungeon(Persistence.dungeon);
        Persistence.floor = Persistence.dungeon.initiateExploration();
        Persistence.stateManager.setState(new NextFloorState(fadeOutState, 1));
    }

    /** Will set to a transition state asking the server for a seed. */
    public static void setDungeonState(AbstractState fadeOutState, int dungeonID) {
        TransitionState s = new TransitionState(fadeOutState, new AskServerForDungeonSeedState(dungeonID));
        s.fadeOut = 0;
        Persistence.stateManager.setState(s);
    }

    public static void onDungeonEnd(DungeonOutcome outcome) {
        if (Persistence.isUnitTesting)
            Launcher.stopGame();
        if (Persistence.saveDungeonExplorations) {
            JsonObject o = Persistence.dungeon.communication.explorationSummary(true);
            try {
                BufferedWriter fw = new BufferedWriter(new FileWriter(
                        new File("dungeon-" + Persistence.dungeon.id + "-" + Persistence.dungeon.seed + ".json")));
                fw.write(o.toString(PrettyPrint.indentWithTabs()));
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DungeonEndState state = new DungeonEndState(outcome);

        Persistence.stateManager.setState(new TransitionState(Persistence.dungeonState, state) {
            @Override
            public void onTransitionHalf() {
                super.onTransitionHalf();
                Persistence.displaymap = null;
            }
        });
    }

}
