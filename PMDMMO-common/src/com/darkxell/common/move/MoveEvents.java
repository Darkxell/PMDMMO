package com.darkxell.common.move;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;

/** Handles Event creation from Move Effects. */
public class MoveEvents {

    public static final String ADDITIONAL = "additional_move_effect", MAIN = "main_move_effect";

    public final ArrayList<Event> events = new ArrayList<>();

    /**
     * Creates a Move Effect.
     *
     * @param effect       - The created Effect.
     * @param moveEvent    - The Move Use context.
     * @param missed       - <code>true</code> if the Move missed.
     * @param isAdditional - <code>true</code> if this Effect is an Additional Effect.
     */
    public void createEffect(Event effect, MoveUseEvent moveEvent, boolean missed, boolean isAdditional) {
        effect.addFlag(isAdditional ? ADDITIONAL : MAIN);
        if (effect != null)
            this.events.add(effect);
    }

}
