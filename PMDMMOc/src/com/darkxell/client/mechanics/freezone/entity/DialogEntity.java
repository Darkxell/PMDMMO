package com.darkxell.client.mechanics.freezone.entity;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogScreenFactory;
import com.darkxell.client.state.dialog.DialogState;

class DialogEntity extends FreezoneEntity {
    private DialogScreen[] dialogs;

    /**
     * Note: the supplied interactivity attribute is ignored, and depends on if dialogs are present.
     */
    @Override
    protected void deserialize(Element el) {
        super.deserialize(el);

        List<DialogScreen> dialogs = new ArrayList<>();
        for (Element dialogEl : el.getChildren("dialog"))
            dialogs.add(DialogScreenFactory.getScreen(dialogEl));
        this.dialogs = dialogs.toArray(new DialogScreen[0]);
        this.interactive = this.dialogs.length > 0;
    }

    @Override
    public void onInteract() {
        if (this.dialogs != null)
            Persistence.stateManager.setState(
                    new DialogState(Persistence.stateManager.getCurrentState(), null, this.dialogs).setOpaque(true));
    }
}
