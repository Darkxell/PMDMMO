package com.darkxell.client.state.menu.freezone;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.Sprite;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait.PortraitEmotion;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.JsonObject;

public class MissionDetailsState extends AbstractState {

	private AbstractState previousstate;
	private AbstractState exploresource;
	private Mission mission;
	Sprite billboard = new Sprite("/hud/billboard_details.png");
	private byte acceptstatus = STATUS_CANACCEPT;
	private static final byte STATUS_CANACCEPT = 0;
	private static final byte STATUS_WAITING = 1;
	private static final byte STATUS_ACCEPTED = 2;
	private static final byte STATUS_REFUSED = 3;
	private static final byte STATUS_IMPOSSIBLE = 4;

	public MissionDetailsState(AbstractState exploresource, Mission content, AbstractState previousstate) {
		this.exploresource = exploresource;
		this.previousstate = previousstate;
		this.mission = content;
		for (int i = 0; i < Persistance.player.getMissions().size(); i++) 
            if (Persistance.player.getMissions().get(i).toString().equals(content.toString())) {
                acceptstatus = STATUS_ACCEPTED;
                break;
            }
	}

	@Override
	public void onKeyPressed(Key key) {
		switch (key) {
		case RUN:
			Persistance.stateManager.setState(previousstate);
			break;
		case ATTACK:
			if (acceptstatus == STATUS_CANACCEPT) {
				acceptstatus = STATUS_WAITING;
				JsonObject value = new JsonObject();
				value.add("action", "acceptmission");
				value.add("mission", this.mission.toString());
				Persistance.socketendpoint.sendMessage(value.toString());
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onKeyReleased(Key key) {
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		this.exploresource.render(g, width, height);
		g.drawImage(billboard.image(), 0, 0, null);

		int offsetx = 55, offsety = 50;
		int basewidth = width - (2 * offsetx);
		g.translate(offsetx, offsety);
		// Draw the infos
		Message m = new Message(mission.getMissionFlavor().line1id);
		m.addReplacement("<pokemon>", PokemonRegistry.find(mission.getPokemonid2()).speciesName());
		m.addReplacement("<item>", ItemRegistry.find(mission.getItemid()).name());
		TextRenderer.render(g, m, 5, 5);
		m = new Message(mission.getMissionFlavor().line2id);
		m.addReplacement("<pokemon>", PokemonRegistry.find(mission.getPokemonid2()).speciesName());
		m.addReplacement("<item>", ItemRegistry.find(mission.getItemid()).name());
		TextRenderer.render(g, m, 5, 20);

		g.drawImage(PokemonPortrait.portrait(PokemonRegistry.find(mission.getClientPokemon()), PortraitEmotion.Normal, false),
				basewidth - PokemonPortrait.PORTRAIT_SIZE - 20, 35, null);
		switch (acceptstatus) {
		case STATUS_CANACCEPT:
			TextRenderer.render(g, new Message("mission.info.acceptinfo"), basewidth - 90, 80);
			break;
		case STATUS_ACCEPTED:
			TextRenderer.render(g, new Message("mission.info.accepted"), basewidth - 90, 80);
			break;
		case STATUS_WAITING:
			TextRenderer.render(g, new Message("mission.info.waiting"), basewidth - 90, 80);
			break;
		case STATUS_REFUSED:
			TextRenderer.render(g, new Message("mission.info.refused"), basewidth - 90, 80);
			break;
		case STATUS_IMPOSSIBLE:
			TextRenderer.render(g, new Message("mission.info.impossible"), basewidth - 90, 80);
			break;
		}
		TextRenderer.render(g, new Message("mission.info.client"), 5, 60);
		TextRenderer.render(g, new Message("mission.info.objective"), 5, 75);
		TextRenderer.render(g, new Message("mission.info.place"), 5, 90);
		TextRenderer.render(g, new Message("mission.info.difficulty"), 5, 105);
		TextRenderer.render(g, new Message("mission.info.reward"), 5, 120);
		TextRenderer.setColor(Color.YELLOW);
		TextRenderer.render(g, PokemonRegistry.find(mission.getPokemonid1()).speciesName(), 70, 60);
		TextRenderer.render(g, mission.getMissionFlavor().getObjectiveText(), 70, 75);
		TextRenderer.render(g, "<yellow>" + DungeonRegistry.find(mission.getDungeonid()).name() + "</color>  "
				+ new Message("mission.floor") + " <blue>" + mission.getFloor() + "</color>", 70, 90);
		TextRenderer.render(g, mission.getDifficulty(), 70, 105);
		// Draws the rewards
		TextRenderer.render(g,
				new Message("mission.info.reward.points")
						.addReplacement("<money>", mission.getRewards().getMoney() + "")
						.addReplacement("<points>", mission.getRewards().getPoints() + ""),
				40, 140);
		TextRenderer.render(g,
				new Message("mission.info.reward.points")
						.addReplacement("<money>", mission.getRewards().getMoney() + "")
						.addReplacement("<points>", mission.getRewards().getPoints() + ""),
				40, 140);
		int rewardsoffsety = 155;
		if (mission.getRewards().getTriggers().length > 0) {
			TextRenderer.render(g, new Message("mission.info.reward.trigger"), 40, rewardsoffsety);
			rewardsoffsety += 15;
		}
		for (int i = 0; i < mission.getRewards().getItems().length && i < 3; i++) {
			TextRenderer.render(g,
					mission.getRewards().getQuantities()[i] + "x <blue>"
							+ ItemRegistry.find(mission.getRewards().getItems()[i]).name() + "</color>",
					40, rewardsoffsety);
			rewardsoffsety += 15;
		}
		// Resets the graphics
		g.translate(-offsetx, -offsety);
	}

	/**
	 * Notifies this state that the mission has been accepted (by the server
	 * typically) or not.
	 */
	public void notifyMissionAcceptResponse(boolean accepted) {
		if (accepted)
			acceptstatus = STATUS_ACCEPTED;
		else
			acceptstatus = STATUS_REFUSED;
	}

	@Override
	public void update() {
		this.exploresource.update();
	}

}
