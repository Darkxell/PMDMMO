package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.language.Message;

/** An Item that has different effects when used. */
public class OrbItemEffect extends ItemEffect {
    /** ID of the Move used when using this Orb. */
    public final int moveID;

    public OrbItemEffect(int id, int moveID) {
        super(id);
        this.moveID = moveID;
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public boolean isUsableOnCatch() {
        return false;
    }

    @Override
    public Message name(Item item) {
        return super.name(item).addPrefix("<orb>");
    }

    @Override
    public final void use(ItemUseEvent itemEvent, ArrayList<Event> events) {
        if (itemEvent.floor.data.isBossFloor())
            events.add(new MessageEvent(itemEvent.floor, itemEvent, new Message("item.orb.boss")));
        else events.add(new MoveSelectionEvent(itemEvent.floor, itemEvent, new LearnedMove(this.moveID), itemEvent.user,
                itemEvent.user.facing(), false));
    }

}
