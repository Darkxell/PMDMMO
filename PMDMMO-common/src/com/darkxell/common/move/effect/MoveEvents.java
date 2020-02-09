package com.darkxell.common.move.effect;

import java.util.ArrayList;

import com.darkxell.common.event.Event;

/** Handles Event creation from Move Effects. */
public class MoveEvents {

    public static final String ADDITIONAL = "additional_move_effect", MAIN = "main_move_effect";

    public final ArrayList<Event> events = new ArrayList<>();

    /**
     * Creates a Move Effect.
     *
     * @param effect       - The created Effect.
     * @param isAdditional - <code>true</code> if this Effect is an Additional Effect.
     */
    public void createEffect(Event effect, boolean isAdditional) {
        if (effect != null) {
            effect.addFlag(isAdditional ? ADDITIONAL : MAIN);
            this.events.add(effect);
        }
    }

}
