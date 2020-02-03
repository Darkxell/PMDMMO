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
        this.createEffect(effect, isAdditional, false);
    }

    /**
     * Creates a Move Effect.
     *
     * @param effect            - The created Effect.
     * @param isAdditional      - <code>true</code> if this Effect is an Additional Effect.
     * @param beforeOtherEvents - <code>true</code> if this Effect should be added before other existing effects.
     */
    public void createEffect(Event effect, boolean isAdditional, boolean beforeOtherEvents) {
        effect.addFlag(isAdditional ? ADDITIONAL : MAIN);
        if (effect != null) {
            if (beforeOtherEvents)
                this.events.add(0, effect);
            else
                this.events.add(effect);
        }
    }

}
