package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.common.util.XMLUtils;

public class PlayCutsceneCutsceneEnd extends CutsceneEnd {

    public final String cutsceneID;

    public PlayCutsceneCutsceneEnd(Cutscene cutscene, Element xml) {
        super(cutscene, xml);
        this.cutsceneID = XMLUtils.getAttribute(xml, "id", null);
    }

    public PlayCutsceneCutsceneEnd(String cutsceneID, String function, boolean fadesOut) {
        super(null, function, fadesOut);
        this.cutsceneID = cutsceneID;
    }

    @Override
    public void onCutsceneEnd() {
        super.onCutsceneEnd();
        if (this.cutsceneID != null)
            CutsceneManager.playCutscene(this.cutsceneID, this.fadesOut);
    }

    @Override
    public Element toXML() {
        return super.toXML().setAttribute("id", this.cutsceneID);
    }

    @Override
    protected String xmlName() {
        return "playcutscene";
    }

}
