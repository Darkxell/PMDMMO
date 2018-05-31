package com.darkxell.client.mechanics.freezones.entities;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.pokemon.FreezonePokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.state.dialog.AbstractDialogState.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.common.util.Direction;

public class PokemonFreezoneEntity extends FreezoneEntity {

	/** The intelligent sprite of the pokemon. */
	private PokemonSprite pkmnsprite;

	private ArrayList<DialogScreen> dialogs;
	
	public PokemonFreezoneEntity(int x, int y, PokemonSprite sprite) {
		super(true, true, x, y);
		this.pkmnsprite = sprite;
	}

	public PokemonFreezoneEntity(int x, int y, int spriteid) {
		this(x, y, new PokemonSprite(PokemonSpritesets.getSpriteset(spriteid)));
	}
	
	public PokemonFreezoneEntity(int x, int y, int spriteid, Direction facing) {
		this(x, y, new PokemonSprite(PokemonSpritesets.getSpriteset(spriteid)));
		this.pkmnsprite.setFacingDirection(facing);
	}
	
	public PokemonFreezoneEntity(int x, int y,int spriteid, Direction facing, ArrayList<DialogScreen> dialog) {
		this(x,y,spriteid,facing);
		this.dialogs = dialog;
	}

	@Override
	public AbstractRenderer createRenderer() {
		return new FreezonePokemonRenderer(this, this.pkmnsprite);
	}

	@Override
	public void onInteract() {
		Persistance.stateManager.setState(new DialogState(Persistance.stateManager.getCurrentState(), this.dialogs));
	}

	@Override
	public void update() {
	}

}
