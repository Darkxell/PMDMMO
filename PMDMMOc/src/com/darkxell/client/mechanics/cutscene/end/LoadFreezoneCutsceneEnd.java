package com.darkxell.client.mechanics.cutscene.end;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.xml.XMLUtils;
import com.darkxell.common.zones.FreezoneInfo;

public class LoadFreezoneCutsceneEnd extends CutsceneEnd {

    public final Direction direction;
    public final FreezoneInfo freezone;
    public final int xPos, yPos;

    public LoadFreezoneCutsceneEnd(Cutscene cutscene, Element xml) {
        super(cutscene, xml);
        this.freezone = FreezoneInfo.find(xml.getAttributeValue("id"));
        this.xPos = XMLUtils.getAttribute(xml, "xpos", -1);
        this.yPos = XMLUtils.getAttribute(xml, "ypos", -1);
        this.direction = XMLUtils.getAttribute(xml, "facing") == null ? null
                : Direction.valueOf(XMLUtils.getAttribute(xml, "facing", Direction.SOUTH.name()).toUpperCase());
    }

    public LoadFreezoneCutsceneEnd(Cutscene cutscene, String function, boolean fadesOut) {
        super(cutscene, function, fadesOut);
        this.freezone = FreezoneInfo.BASE;
        this.xPos = -1;
        this.yPos = -1;
        this.direction = null;
    }

    public LoadFreezoneCutsceneEnd(FreezoneInfo freezone, int x, int y, Direction facing, String function,
            boolean fadesOut) {
        super(null, function, fadesOut);
        this.freezone = freezone;
        this.xPos = x;
        this.yPos = y;
        this.direction = facing;
    }

    @Override
    public void onCutsceneEnd() {
        super.onCutsceneEnd();
        int camX = (int) Persistence.freezoneCamera.getX(), camY = (int) Persistence.freezoneCamera.getY();
        int x = this.xPos, y = this.yPos;
        if (this.cutscene.creation.freezone != this.freezone) {
            camX = this.xPos - PrincipalMainState.displayWidth / 8 / 2;
            camY = this.yPos - PrincipalMainState.displayHeight / 8 / 2;
        } else {
            CutscenePokemon e = this.cutscene.player.getPlayerEntity();
            if (e != null) {
                if (x == -1)
                    x = (int) e.xPos;
                if (y == -1)
                    y = (int) e.yPos;
            }
        }
        StateManager.setExploreState(this.freezone, this.direction, x, y, this.fadesOut);
        Persistence.freezoneCamera.setX(camX);
        Persistence.freezoneCamera.setY(camY);
    }

    @Override
    public Element toXML() {
        Element root = super.toXML().setAttribute("id", this.freezone.id);
        XMLUtils.setAttribute(root, "xpos", this.xPos, -1);
        XMLUtils.setAttribute(root, "ypos", this.yPos, -1);
        if (this.direction != null)
            XMLUtils.setAttribute(root, "facing", this.direction.name(), "null");
        return root;
    }

    @Override
    protected String xmlName() {
        return "loadfreezone";
    }

}
