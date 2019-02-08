package com.darkxell.client.mechanics.cutscene.end;

import java.util.Random;

import org.jdom2.Element;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.XMLUtils;

public class EnterDungeonCutsceneEnd extends CutsceneEnd {

    public final int dungeonID;

    public EnterDungeonCutsceneEnd(Cutscene cutscene, Element xml) {
        super(cutscene, xml);
        this.dungeonID = XMLUtils.getAttribute(xml, "id", 1);
    }

    public EnterDungeonCutsceneEnd(int dungeonID, String function, boolean fadesOut) {
        super(null, function, fadesOut);
        this.dungeonID = dungeonID;
    }

    @Override
    public void onCutsceneEnd() {
        super.onCutsceneEnd();
        AbstractState fadesOut = null;
        if (this.fadesOut)
            fadesOut = Persistence.cutsceneState;
        if (Persistence.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED)
            StateManager.setDungeonState(fadesOut, this.dungeonID);
        else
            StateManager.setDungeonState(fadesOut, this.dungeonID, new Random().nextLong());
    }

    @Override
    public Element toXML() {
        return super.toXML().setAttribute("id", String.valueOf(this.dungeonID));
    }

    @Override
    protected String xmlName() {
        return "enterdungeon";
    }

}
