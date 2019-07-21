package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.messagehandlers.InventoryRequestHandler;
import com.darkxell.client.launchable.messagehandlers.MonsterRequestHandler;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.eclipsesource.json.JsonObject;

public class PlayerLoadingState extends AbstractState {
	public static interface PlayerLoadingEndListener {
		public default void onPlayerLoadingEnd(PlayerLoadingState state) {
			Persistence.stateManager.setState(new OpenningState());
		}
	}

	public static final int TIMEOUT = 600;

	private boolean base = false, inventory = false, leader = false;
	private boolean hasSent = false;
	public final PlayerLoadingEndListener listener;
	public final long playerID;
	private boolean[] team;
	private Pokemon[] teamtmp;
	private int tick;

	public PlayerLoadingState(long playerID, PlayerLoadingEndListener listener) {
		this.playerID = playerID;
		this.listener = listener;
	}

	private void askForMore() {
		this.hasSent = false;
		this.tick = 0;
	}

	private void checkFinished() {
		if (this.base && this.inventory && this.leader) {
			for (boolean b : this.team)
				if (!b) return;
			this.finish();
		}
	}

	private void exit() {
		Persistence.isCommunicating = false;
		if (this.listener == null) Persistence.stateManager.setState(new OpenningState());
		else this.listener.onPlayerLoadingEnd(this);
	}

	private void finish() {
		Persistence.player.clearAllies();
		for (Pokemon p : this.teamtmp)
			Persistence.player.addAlly(p);
		this.exit();
	}

	public void onInventoryReceived(JsonObject message) {
		Persistence.player.setInventory(InventoryRequestHandler.readInventory(message, Persistence.player));
		this.inventory = true;
		this.checkFinished();
	}

	@Override
	public void onKeyPressed(Key key) {}

	@Override
	public void onKeyReleased(Key key) {}

	public void onMonsterReceived(JsonObject message) {
		Pokemon p = MonsterRequestHandler.readMonster(message);
		if (p.id() == Persistence.player.getData().mainpokemon.id) {
			Persistence.player.setLeaderPokemon(p);
			this.leader = true;
		} else {
			ArrayList<DatabaseIdentifier> team = Persistence.player.getData().pokemonsinparty;
			for (int i = 0; i < team.size(); ++i)
				if (p.id() == team.get(i).id) {
					this.teamtmp[i] = p;
					this.team[i] = true;
					break;
				}
		}
		this.checkFinished();
	}

	public void onPlayerReceived(JsonObject player) {
		DBPlayer playerdata = new DBPlayer();
		playerdata.read(player);
		Persistence.player = new Player(playerdata);
		this.base = true;

		this.team = new boolean[Persistence.player.getData().pokemonsinparty.size()];
		this.teamtmp = new Pokemon[Persistence.player.getData().pokemonsinparty.size()];
		if (Persistence.player.getData().toolboxinventory == null) this.inventory = true;
		if (Persistence.player.getData().mainpokemon == null) this.leader = true;

		this.askForMore();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (Persistence.socketendpoint.connectionStatus() != GameSocketEndpoint.CONNECTED) this.exit();
		else Persistence.isCommunicating = true;
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		/* Message m = new Message("general.loading"); TextRenderer.render(g, m, width / 2 - TextRenderer.width(m), height / 2 - TextRenderer.height()); */
	}

	@Override
	public void update() {
		if (this.hasSent) {
			++this.tick;
			if (this.tick >= TIMEOUT) this.askForMore();
		} else {
			if (!this.base) Persistence.socketendpoint.requestObject("DBPlayer", this.playerID);
			else {
				if (!this.inventory)
					Persistence.socketendpoint.requestInventory(Persistence.player.getData().toolboxinventory.id);
				if (!this.leader)
					Persistence.socketendpoint.requestMonster(Persistence.player.getData().mainpokemon.id);
				for (int i = 0; i < this.team.length; ++i)
					if (!this.team[i]) Persistence.socketendpoint
							.requestMonster(Persistence.player.getData().pokemonsinparty.get(i).id);
			}
			this.hasSent = true;
		}
	}

}
