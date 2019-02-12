package com.darkxell.client.graphics.floor;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.common.ai.visibility.Visibility;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.pokemon.DungeonPokemon;

import java.util.function.*;

/**
 * Wrapper class around the current camera's Visibility function. This is to easily handle null cases but may also have
 * different behaviors, because the camera may show things the Pokemon may not be able to see.
 */
public class CameraVisibility {
    private static Visibility camera() {
        DungeonPokemon c = Persistence.dungeonState.getCameraPokemon();
        return c == null ? null : Persistence.floor.aiManager.getAI(c).visibility;
    }

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
