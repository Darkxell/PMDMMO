package com.darkxell.client.state.dialog;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.resources.images.pokemon.PokemonPortrait;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class PokemonDialogScreen extends DialogScreen
{
	/** The emotion of the Pokemon. Unused for now. */
	public short emotion;
	/** The Pokemon species talking. null if not a Pokemon. */
	public final PokemonSpecies pokemon;
	/** True if the talking Pokemon is shiny. Has no effect if pokemon is null. */
	public final boolean shiny;
	/** If not null, the name of the speaker to appear in front of the text. */
	public final Message speakerName;

	/** Shortcut constructor if using an instanciated Pokemon. */
	public PokemonDialogScreen(Pokemon pokemon, Message message)
	{
		this(pokemon == null ? null : pokemon.species(), message, pokemon == null ? null : pokemon.isShiny(), pokemon == null ? null : pokemon.getNickname());
	}

	public PokemonDialogScreen(PokemonSpecies pokemon, Message message)
	{
		this(pokemon, message, pokemon == null ? null : pokemon.speciesName());
	}

	public PokemonDialogScreen(PokemonSpecies pokemon, Message message, boolean shiny, Message speakerName)
	{
		super(message);
		this.pokemon = pokemon;
		this.speakerName = speakerName;
		this.shiny = shiny;

		if (this.speakerName != null) this.message.addPrefix(new Message(": ", false)).addPrefix(this.speakerName);
	}

	public PokemonDialogScreen(PokemonSpecies pokemon, Message message, Message speakerName)
	{
		this(pokemon, message, false, speakerName);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);

		if (this.pokemon != null)
		{
			Rectangle dialogBox = this.parentState.dialogBox();
			PokemonPortrait.drawPortrait(g, this.pokemon, this.shiny, dialogBox.x + 5, dialogBox.y - Sprites.Hud.portrait.image().getHeight() - 5);
		}
	}

	@Override
	protected boolean switchAnimation()
	{
		if (!super.switchAnimation()) return false;
		if (this.parentState.nextScreen() instanceof PokemonDialogScreen)
		{
			PokemonDialogScreen next = (PokemonDialogScreen) this.parentState.nextScreen();
			if (this.pokemon == null) return next.pokemon == null;
			return this.pokemon.equals(next.pokemon) && this.emotion == next.emotion;
		}
		return true;
	}

}