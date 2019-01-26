package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogScreenFactory;
import com.darkxell.client.state.dialog.DialogState;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class DialogEntity extends FreezoneEntity {
    private DialogScreen[] dialogs;

    /**
     * Note: the supplied interactivity attribute is ignored, and depends on if dialogs are present.
     */
    @Override
    protected void onInitialize(Element el) {
        super.onInitialize(el);

        List<DialogScreen> dialogs = new ArrayList<>();
        for (Element dialogEl : el.getChildren("dialog")) {
            dialogs.add(DialogScreenFactory.getScreen(dialogEl));
        }
        this.dialogs = dialogs.toArray(new DialogScreen[0]);
        this.canInteract = this.dialogs.length > 0;
    }

    @Override
    public void onInteract() {
        if (this.dialogs != null) {
            Persistence.stateManager.setState(
                    new DialogState(Persistence.stateManager.getCurrentState(), null, this.dialogs).setOpaque(true));
        }
    }
}
