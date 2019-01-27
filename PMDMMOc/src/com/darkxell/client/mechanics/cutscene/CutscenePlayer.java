package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;
import java.util.LinkedList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.mechanics.cutscene.event.DelayCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.WaitCutsceneEvent;
import com.darkxell.client.graphics.AbstractRenderer;

public class CutscenePlayer {

	public final Cutscene cutscene;
	private final ArrayList<CutsceneEntity> entities;
	public double mapAlpha = 1;
	private final LinkedList<CutsceneEvent> pendingEvents;
	private final ArrayList<CutsceneEvent> playingEvents;

	public CutscenePlayer(Cutscene cutscene) {
		this.cutscene = cutscene;

		this.entities = new ArrayList<>();
		this.playingEvents = new ArrayList<>();
		this.pendingEvents = new LinkedList<>(cutscene.events);
	}

	public void addEvents(CutsceneEvent[] results) {
		for (int i = results.length - 1; i >= 0; --i)
			this.pendingEvents.addFirst(results[i]);
	}

	public void createEntity(CutsceneEntity entity) {
		this.entities.add(entity);
		if (Persistence.cutsceneState != null) {
			AbstractRenderer renderer = entity.createRenderer();
			if (renderer != null) Persistence.currentmap.cutsceneEntityRenderers.register(entity, renderer);
		}
	}

	public CutsceneEntity getEntity(int id) {
		if (id == -1) return null;
		for (CutsceneEntity e : this.entities)
			if (e.id == id) return e;
		return null;
	}

	public CutscenePokemon getPlayerEntity() {
		for (CutsceneEntity e : this.entities)
			if (e instanceof CutscenePokemon && ((CutscenePokemon) e).instanciated == Persistence.player.leaderPokemon)
				return (CutscenePokemon) e;
		return null;
	}

	/** @return True if a playing event is causing later events to wait before being played. */
	private boolean hasPausingEvent() {
		for (CutsceneEvent e : this.playingEvents)
			if ((e instanceof DelayCutsceneEvent || e instanceof WaitCutsceneEvent) && !e.isOver()) return true;
		return false;
	}

	public void removeEntity(CutsceneEntity entity) {
		if (this.entities.contains(entity)) {
			this.entities.remove(entity);
			if (Persistence.cutsceneState != null) Persistence.currentmap.cutsceneEntityRenderers.unregister(entity);
		}
	}

	private void startEvent(CutsceneEvent event) {
		event.onStart();
		if (event.isOver()) event.onFinish();
		else this.playingEvents.add(event);
	}

	public void update() {
		while (!this.hasPausingEvent() && !this.pendingEvents.isEmpty())
			this.startEvent(this.pendingEvents.removeFirst());

		for (int i = 0; i < this.playingEvents.size(); ++i) {
			CutsceneEvent event = this.playingEvents.get(i);
			if (event.isOver()) {
				this.playingEvents.remove(i--);
				event.onFinish();
			} else event.update();
		}

		if (this.playingEvents.isEmpty() && this.pendingEvents.isEmpty()) this.cutscene.onCutsceneEnd();
	}

}
