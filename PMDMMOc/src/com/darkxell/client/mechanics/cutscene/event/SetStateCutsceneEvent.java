package com.darkxell.client.mechanics.cutscene.event;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.common.util.XMLUtils;

public class SetStateCutsceneEvent extends CutsceneEvent {

    public final PokemonSpriteState state;
    public final int target;

    public SetStateCutsceneEvent(Element xml, CutsceneContext context) {
        super(xml, CutsceneEventType.setstate, context);
        this.target = XMLUtils.getAttribute(xml, "target", -1);
        PokemonSpriteState s = null;
        try {
            s = PokemonSpriteState.valueOf(XMLUtils.getAttribute(xml, "state", (String)null));
        } catch (Exception ignored) {
        }
        this.state = s;
    }

    public SetStateCutsceneEvent(int id, CutsceneEntity entity, PokemonSpriteState state) {
        super(id, CutsceneEventType.setstate);
        this.target = entity.id;
        this.state = state;
    }

    @Override
    public void onStart() {
        super.onStart();
        CutsceneEntity entity = this.context.parent().player.getEntity(this.target);
        if (entity instanceof CutscenePokemon)
            ((CutscenePokemon) entity).currentState = this.state;
    }

    @Override
    public String toString() {
        return this.displayID() + "(" + this.target + ") gains state " + this.state;
    }

    @Override
    public Element toXML() {
        return super.toXML().setAttribute("target", String.valueOf(this.target)).setAttribute("state",
                this.state.name());
    }

}
