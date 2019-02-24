package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.language.Message;

/** An Item that teaches a move to a Pokemon when used, then turns into a Used TM. */
public class TeachesMoveItemEffect extends TeachesMoveRenewableItemEffect {

    public TeachesMoveItemEffect(int id, int moveID) {
        super(id, moveID);
    }

    @Override
    public Message description(Item item) {
        return new Message("item.info.tm").addReplacement("<move>", this.move().name());
    }

    @Override
    public boolean isConsummable() {
        return true;
    }

    @Override
    public void use(ItemUseEvent itemEvent, ArrayList<DungeonEvent> events) {
        if (itemEvent.user.player() != null)
            if (itemEvent.user.player().inventory().isFull()) itemEvent.user.tile().setItem(new ItemStack(-1 * itemEvent.item.id));
            else itemEvent.user.player().inventory().addItem(new ItemStack(-1 * itemEvent.item.id));
    }
}
