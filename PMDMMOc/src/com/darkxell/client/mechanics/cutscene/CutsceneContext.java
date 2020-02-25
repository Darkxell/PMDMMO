package com.darkxell.client.mechanics.cutscene;

import java.util.List;

public interface CutsceneContext {

    public List<CutsceneEvent> availableEvents();

    public default CutsceneEvent getEvent(int id) {
        for (CutsceneEvent e : this.availableEvents())
            if (e.getID() == id)
                return e;
        return null;
    }

    public Cutscene parent();

}
