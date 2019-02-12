package com.darkxell.client.graphics.floor;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.common.ai.visibility.Visibility;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.pokemon.DungeonPokemon;

import java.util.function.*;

/**
 * Acts on the current camera visibility (i.e. that of the player), and provides extended behavior for certain checks.
 *
 * @see Visibility
 */
public class CameraVisibility {
    private static Visibility camera() {
        DungeonPokemon c = Persistence.dungeonState.getCameraPokemon();
        return c == null ? null : Persistence.floor.aiManager.getAI(c).visibility;
    }

    /**
     * Test a predicate on the current camera visibility.
     * <p>
     * For example, to test if the current camera has super vision:
     * </p>
     * 
     * <pre>
     * CameraVisibility::check(Visibility::hasSuperVision).test(obj);
     * </pre>
     * 
     * @return Is there a camera active, and if so, is the predicate true on it?
     */
    public static <T> Predicate<T> check(BiPredicate<Visibility, T> callback) {
        Visibility v = camera();
        return t -> v != null && callback.test(v, t);
    }

    public static boolean isMapVisible(Visibility v, DungeonPokemon pokemon) {
        return pokemon.player() == Persistence.player || v.isVisible(pokemon);
    }

    public static boolean isVisible(Visibility v, DungeonPokemon pokemon) {
        return Persistence.floor.data.shadows() == FloorData.NO_SHADOW || v.isVisible(pokemon);
    }
}
