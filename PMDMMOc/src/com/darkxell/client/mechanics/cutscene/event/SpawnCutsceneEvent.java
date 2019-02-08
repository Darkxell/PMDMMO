package com.darkxell.client.mechanics.cutscene.event;

import java.util.ArrayList;

import org.jdom2.Attribute;
import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;

public class SpawnCutsceneEvent extends CutsceneEvent {

    public final CutsceneEntity entity;

    public SpawnCutsceneEvent(Element xml, CutsceneContext context) {
        super(xml, CutsceneEventType.spawn, context);
        if (xml.getName().equals("spawnpokemon"))
            this.entity = new CutscenePokemon(xml);
        else
            this.entity = new CutsceneEntity(xml);
    }

    public SpawnCutsceneEvent(int id, CutsceneEntity entity) {
        super(id, CutsceneEventType.spawn);
        this.entity = entity;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.context.parent().player.createEntity(this.entity);
    }

    @Override
    public String toString() {
        return this.displayID() + "Spawn " + this.entity;
    }

    @Override
    public Element toXML() {
        Element sup = super.toXML();
        Element ent = this.entity.toXML();

        sup.setName(this.entity instanceof CutscenePokemon ? "spawnpokemon" : "spawn");
        for (Attribute a : new ArrayList<>(ent.getAttributes())) {
            ent.removeAttribute(a);
            sup.setAttribute(a);
        }
        for (Element e : new ArrayList<>(ent.getChildren())) {
            ent.removeContent(e);
            sup.addContent(e);
        }

        return sup;
    }

}
