package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import com.darkxell.client.mechanics.cutscene.end.ArbitraryCutsceneEnds;
import com.darkxell.client.mechanics.cutscene.end.EnterDungeonCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.LoadFreezoneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.PlayCutsceneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.ResumeExplorationCutsceneEnd;
import com.darkxell.common.util.xml.XMLUtils;

public class Cutscene implements Comparable<Cutscene>, CutsceneContext {

    public static abstract class CutsceneEnd {
        public static CutsceneEnd create(Cutscene cutscene, Element xml) {
            if (xml.getChild("enterdungeon", xml.getNamespace()) != null)
                return new EnterDungeonCutsceneEnd(cutscene, xml.getChild("enterdungeon", xml.getNamespace()));
            if (xml.getChild("playcutscene", xml.getNamespace()) != null)
                return new PlayCutsceneCutsceneEnd(cutscene, xml.getChild("playcutscene", xml.getNamespace()));
            if (xml.getChild("loadfreezone", xml.getNamespace()) != null)
                return new LoadFreezoneCutsceneEnd(cutscene, xml.getChild("loadfreezone", xml.getNamespace()));
            if (xml.getChild("resumeexploration", xml.getNamespace()) != null)
                return new ResumeExplorationCutsceneEnd(cutscene,
                        xml.getChild("resumeexploration", xml.getNamespace()));
            return null;
        }

        String arbitraryFunction;
        protected Cutscene cutscene;
        protected boolean fadesOut;

        public CutsceneEnd(Cutscene cutscene, Element xml) {
            this(cutscene, XMLUtils.getAttribute(xml, "function", null), XMLUtils.getAttribute(xml, "fadesout", true));
        }

        public CutsceneEnd(Cutscene cutscene, String function, boolean fadesOut) {
            this.cutscene = cutscene;
            this.arbitraryFunction = function;
            this.fadesOut = fadesOut;
        }

        public boolean fadesOut() {
            return this.fadesOut;
        }

        public String function() {
            return this.arbitraryFunction;
        }

        public void onCutsceneEnd() {
        }

        public Element toXML() {
            Element xml = new Element(this.xmlName());
            if (this.arbitraryFunction != null)
                xml.setAttribute("function", this.arbitraryFunction);
            XMLUtils.setAttribute(xml, "fadesout", this.fadesOut, true);
            return xml;
        }

        protected abstract String xmlName();
    }

    public final CutsceneCreation creation;
    public final ArrayList<CutsceneEvent> events;
    public String name;
    public CutsceneEnd onFinish;
    public final CutscenePlayer player;

    public Cutscene(String name) {
        this(name, null, null, new ArrayList<>());
    }

    public Cutscene(String name, CutsceneCreation creation, CutsceneEnd end, ArrayList<CutsceneEvent> events) {
        this.name = name;
        this.creation = creation != null ? creation : new CutsceneCreation(this);
        this.onFinish = end != null ? end : new LoadFreezoneCutsceneEnd(this, (String) null, false);
        this.events = events;
        this.player = new CutscenePlayer(this);

        this.creation.cutscene = this;
        this.onFinish.cutscene = this;
        for (CutsceneEvent e : this.events)
            e.context = this;
    }

    public Cutscene(String name, Element xml) {
        this.name = name;
        this.creation = new CutsceneCreation(this, xml.getChild("creation", xml.getNamespace()));
        this.onFinish = CutsceneEnd.create(this, xml.getChild("onfinish", xml.getNamespace()));
        this.onFinish.cutscene = this;

        this.events = new ArrayList<>();
        for (Element event : xml.getChild("events", xml.getNamespace()).getChildren()) {
            CutsceneEvent e = CutsceneEvent.create(event, this);
            if (e != null)
                this.events.add(e);
        }

        this.player = new CutscenePlayer(this);
    }

    @Override
    public List<CutsceneEvent> availableEvents() {
        return new ArrayList<>(this.events);
    }

    @Override
    public int compareTo(Cutscene o) {
        return this.name.compareTo(o.name);
    }

    public void onCutsceneEnd() {
        this.onFinish.onCutsceneEnd();
        if (this.onFinish.arbitraryFunction != null)
            ArbitraryCutsceneEnds.execute(this.onFinish.arbitraryFunction, this);
    }

    @Override
    public Cutscene parent() {
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Element toXML() {
        Element root = new Element("cutscene").setAttribute("name", this.name);
        root.addContent(this.creation.toXML());
        Element events = new Element("events");
        for (CutsceneEvent event : this.events)
            events.addContent(event.toXML());
        root.addContent(events);
        root.addContent(new Element("onfinish").addContent(this.onFinish.toXML()));
        return root;
    }

}
