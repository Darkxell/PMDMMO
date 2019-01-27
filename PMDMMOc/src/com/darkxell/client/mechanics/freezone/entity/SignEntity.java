package com.darkxell.client.mechanics.freezone.entity;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;
import org.jdom2.Element;

/**
 * Describes a sign. Note that this entity doesn't have a graphical display and can be walked through. On interact,
 * it will open a simple dialog box.
 */
class SignEntity extends FreezoneEntity {
    private Message mess;

    @Override
    protected void onInitialize(Element el) {
        super.onInitialize(el);

        this.mess = new Message(XMLUtils.getAttribute(el, "id", "nothing"));
    }

    @Override
    public void onInteract() {
        DialogState messageBox = new DialogState(Persistence.stateManager.getCurrentState(),
                new DialogScreen(this.mess).setInstant().setCentered());
        Persistence.stateManager.setState(messageBox);
    }
}
