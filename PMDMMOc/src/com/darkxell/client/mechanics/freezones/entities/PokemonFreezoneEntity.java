package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogScreen;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.language.Message;

public class PokemonFreezoneEntity extends FreezoneEntity
{

	/** The intelligent sprite of the pokemon. */
	private PokemonSprite pkmnsprite;

	public PokemonFreezoneEntity(int x, int y, PokemonSprite sprite)
	{
		super(true, true, x, y);
		this.pkmnsprite = sprite;
	}

	@Override
	public void onInteract()
	{
		Pokemon p = new Pokemon(0, 69, null, null, null, 0, 0, 1, null, null, null, null, (byte) 0, 0, true);
		ArrayList<DialogScreen> screens = new ArrayList<DialogState.DialogScreen>();
		screens.add(new DialogScreen(p,
				new Message(
						"Hey there! I'm Shiny :D <br>This is a debug string to try to know if the dialog boxes are working! Item descriptions are coming next.",
						false)));
		screens.add(new DialogScreen(p, new Message("item.info.302")));
		screens.add(new DialogScreen(PokemonRegistry.find(168).generate(new Random(), 0), new Message("item.info.69")));
		Persistance.stateManager.setState(new DialogState(Persistance.stateManager.getCurrentState(), screens));
	}

	@Override
	public void print(Graphics2D g)
	{
		g.drawImage(pkmnsprite.getCurrentSprite(), (int) (super.posX * 8 - pkmnsprite.getCurrentSprite().getWidth() / 2 + pkmnsprite.getCurrentFrame().spriteX),
				(int) (super.posY * 8 - pkmnsprite.getCurrentSprite().getHeight() / 2 + pkmnsprite.getCurrentFrame().spriteY), null);
	}

	@Override
	public void update()
	{
		pkmnsprite.update();
	}

}
