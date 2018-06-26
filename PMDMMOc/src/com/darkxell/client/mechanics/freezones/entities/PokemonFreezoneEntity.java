package com.darkxell.client.mechanics.freezones.entities;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.pokemon.FreezonePokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.client.state.dialog.DialogState;
import com.darkxell.common.util.Direction;

public class PokemonFreezoneEntity extends FreezoneEntity
{

	/** The intelligent sprite of the pokemon. */
	private PokemonSprite pkmnsprite;

	private ArrayList<DialogScreen> dialogs;

	public PokemonFreezoneEntity(double x, double y, PokemonSprite sprite)
	{
		super(true, true, x, y);
		this.pkmnsprite = sprite;
	}

	public PokemonFreezoneEntity(double x, double y, int spriteid)
	{
		this(x, y, new PokemonSprite(PokemonSpritesets.getSpriteset(spriteid)));
	}

	public PokemonFreezoneEntity(double x, double y, int spriteid, Direction facing)
	{
		this(x, y, new PokemonSprite(PokemonSpritesets.getSpriteset(spriteid)));
		this.pkmnsprite.setFacingDirection(facing);
	}

	public PokemonFreezoneEntity(double x, double y, int spriteid, Direction facing, ArrayList<DialogScreen> dialogs)
	{
		this(x, y, spriteid, facing);
		this.dialogs = dialogs;
	}

	@Override
	public AbstractRenderer createRenderer()
	{
		return new FreezonePokemonRenderer(this, this.pkmnsprite);
	}

	@Override
	public void onInteract()
	{
		Persistance.stateManager.setState(new DialogState(Persistance.stateManager.getCurrentState(), null, this.dialogs));
	}

	@Override
	public void update()
	{}

}
