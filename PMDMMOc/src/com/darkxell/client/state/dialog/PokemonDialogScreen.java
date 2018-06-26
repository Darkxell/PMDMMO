package com.darkxell.client.state.dialog;

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
		this(pokemon.species(), message, pokemon.isShiny(), pokemon.getNickname());
	}

	public PokemonDialogScreen(PokemonSpecies pokemon, Message message)
	{
		this(pokemon, null, message);
	}

	public PokemonDialogScreen(PokemonSpecies pokemon, Message message, boolean shiny, Message speakerName)
	{
		super(message);
		this.pokemon = pokemon;
		this.speakerName = speakerName;
		this.shiny = shiny;

		if (this.speakerName != null) this.message.addPrefix(new Message(": ", false)).addPrefix(this.speakerName);
	}

	public PokemonDialogScreen(PokemonSpecies pokemon, Message speakerName, Message message)
	{
		this(pokemon, speakerName, false, message);
	}

}