package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.stats.PPChangedEvent;

/** An Item that restores PP when eaten. */
public class RestorePPItemEffect extends DrinkItemEffect {

    public final int pp;

    public RestorePPItemEffect(int id, int food, int bellyIfFull, int belly, int pp) {
        super(id, food, bellyIfFull, belly);
        this.pp = pp;
    }

    @Override
    public boolean isUsedOnTeamMember() {
        return true;
    }

    @Override
    public void use(ItemUseEvent itemEvent, ArrayList<Event> events) {
        super.use(itemEvent, events);
        events.add(new PPChangedEvent(itemEvent.floor, itemEvent, itemEvent.target, this.pp, PPChangedEvent.CHANGE_ALL_MOVES));
    }

}
