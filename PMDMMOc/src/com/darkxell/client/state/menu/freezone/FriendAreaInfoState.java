package com.darkxell.client.state.menu.freezone;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.menu.InfoState;
import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.zones.FriendArea;

public class FriendAreaInfoState extends InfoState {

	private static Message generateContent(FriendArea area) {
		int recruited = 0;

		for (Pokemon pokemon : Persistence.player.pokemonInZones.values())
			if (pokemon.species().friendArea() == area)
				++recruited;

		for (Pokemon pokemon : Persistence.player.getTeam())
			if (pokemon.species().friendArea() == area)
				++recruited;

		return new Message("friendarea.recruited").addReplacement("<recruited>", String.valueOf(recruited))
				.addReplacement("<total>", String.valueOf(area.maxFriends()));
	}

	private ArrayList<Message> speciesList = new ArrayList<>();
	/**
	 * Pokemon listing rendering data
	 */
	private int xStart, yStart, xDistance, cols;

	public FriendAreaInfoState(FreezoneMenuState parent, AbstractGraphiclayer background, FriendArea area) {
		super(background, parent, new Message[] { area.getName() }, new Message[] { generateContent(area) });

		ArrayList<PokemonSpecies> species = Registries.species().toList();
		species.removeIf(s -> s.friendArea() != area);

		for (PokemonSpecies s : species) {
			Message m = s.formName();
			boolean isRecruited = false;
			for (Pokemon pokemon : Persistence.player.getTeam())
				if (pokemon.species() == s) {
					isRecruited = true;
					break;
				}
			if (!isRecruited)
				for (Pokemon pokemon : Persistence.player.pokemonInZones.values())
					if (pokemon.species() == s) {
						isRecruited = true;
						break;
					}

			if (isRecruited)
				m.addPrefix("<blue>").addSuffix("</color>");
			this.speciesList.add(m);
		}

		Rectangle r = this.window.inside();
		this.xStart = r.width / 2 + r.x;
		this.yStart = (r.height - TextRenderer.height() - 10) / 2 + r.y + TextRenderer.height() + 10;

		int availableHeight = r.height - TextRenderer.height() - 10;
		int availableLines = availableHeight / (TextRenderer.height() + TextRenderer.lineSpacing());
		this.cols = this.speciesList.size() / availableLines;
		if (this.speciesList.size() % availableLines != 0 && this.speciesList.size() > availableLines)
			++this.cols;
		this.xDistance = r.width / (1 + this.cols);
		this.xStart = r.x + this.xDistance;
		this.yStart -= (TextRenderer.height() + TextRenderer.lineSpacing()) * 1d * this.speciesList.size() / this.cols
				/ 2;
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		super.render(g, width, height);

		int x = this.xStart, y = this.yStart - TextRenderer.height() / 2, colIndex = 0;
		for (Message species : this.speciesList) {
			int mwidth = TextRenderer.width(species);
			TextRenderer.render(g, species, x - mwidth / 2, y);

			x += this.xDistance;
			++colIndex;
			if (colIndex == this.cols) {
				x = this.xStart;
				colIndex = 0;
				y += TextRenderer.height() + TextRenderer.lineSpacing();
			}
		}
	}

}
