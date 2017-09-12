package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;
import java.util.Random;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogElement;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.Message;

public class PokemonFreezoneEntity extends FreezoneEntity {

	/** The intelligent sprite of the pokemon. */
	private PokemonSprite pkmnsprite;

	public PokemonFreezoneEntity(int x, int y, PokemonSprite sprite) {
		super(true, true, x, y);
		this.pkmnsprite = sprite;
	}

	@Override
	public void onInteract() {
		Pokemon p = PokemonRegistry.find(1).generate(new Random(), 0);
		DialogElement e1 = new DialogElement(p, new Message("Hey there!\n This is a debug string to try to know if the dialog boxes are working! Item descriptions are coming next.\n", false));
		DialogElement e2 = new DialogElement(p, new Message("item.info.53"));
		DialogElement e3 = new DialogElement(PokemonRegistry.find(168).generate(new Random(), 0), new Message("item.info.69"));
		Launcher.stateManager.setState(new DialogState(Launcher.stateManager.getCurrentState(), e1, e2, e3),0);
	}

	@Override
	public void print(Graphics2D g) {
		g.drawImage(pkmnsprite.getCurrentSprite(), (int) (super.posX * 8 - pkmnsprite.pointer.gravityX),
				(int) (super.posY * 8 - pkmnsprite.pointer.gravityY), null);
	}

	@Override
	public void update() {
		pkmnsprite.update();
	}

}
