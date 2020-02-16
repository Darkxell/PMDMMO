package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.CutsceneFunctions;
import com.darkxell.common.util.XMLUtils;

public class FunctionCutsceneEvent extends CutsceneEvent {

    public final String functionID;

    public FunctionCutsceneEvent(Element xml, CutsceneContext context) {
        super(xml, CutsceneEventType.function, context);
        this.functionID = XMLUtils.getAttribute(xml, "function", (String)null);
    }

    public FunctionCutsceneEvent(int id, String functionID) {
        super(id, CutsceneEventType.function);
        this.functionID = functionID;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.functionID != null)
            CutsceneFunctions.call(this.functionID, this.context.parent(), this);
    }

    @Override
    public String toString() {
        return this.displayID() + "Call function '" + this.functionID + "'";
    }

    @Override
    public Element toXML() {
        return super.toXML().setAttribute("function", this.functionID);
    }

}
