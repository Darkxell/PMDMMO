package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;
import java.util.List;

import com.darkxell.client.mechanics.cutscene.end.ArbitraryCutsceneEnds;
import com.darkxell.client.model.cutscene.CutsceneModel;
import com.darkxell.client.model.cutscene.end.CutsceneEndModel;

public class Cutscene implements Comparable<Cutscene>, CutsceneContext {

    public static abstract class CutsceneEnd {

        protected Cutscene cutscene;
        private final CutsceneEndModel model;

        public CutsceneEnd(Cutscene cutscene, CutsceneEndModel model) {
            this.cutscene = cutscene;
            this.model = model;
        }

        public boolean fadesOut() {
            return this.model.isFadesOut();
        }

        public String getFunction() {
            return this.model.getFunctionID();
        }

        public void onCutsceneEnd() {
        }
    }

    public final CutsceneCreation creation;
    public final ArrayList<CutsceneEvent> events;
    private final CutsceneModel model;
    public String name;
    public final CutsceneEnd onFinish;
    public final CutscenePlayer player;

    public Cutscene(String name, CutsceneModel model) {
        this.name = name;
        this.model = model;
        this.creation = new CutsceneCreation(this, this.model.getCreation());
        this.onFinish = this.model.getEnd().build(this);
        this.events = new ArrayList<>();
        this.model.getEvents().forEach(e -> this.events.add(CutsceneEvent.create(e, this)));
        this.player = new CutscenePlayer(this);

        this.creation.cutscene = this;
        this.onFinish.cutscene = this;
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
        if (this.onFinish.getFunction() != null)
            ArbitraryCutsceneEnds.execute(this.onFinish.getFunction(), this);
    }

    @Override
    public Cutscene parent() {
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public CutsceneModel getModel() {
        return this.model;
    }

}
