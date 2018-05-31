package com.darkxell.client.state.dialog;

import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.util.language.Message;

public class DialogScreen
{
	/** The emotion of the Pokemon. Unused for now. */
	public short emotion;
	/** True if this DialogScreen prints text centered horizontally. */
	public boolean isCentered;
	/** True if this DialogScreen prints instantaneously. */
	public boolean isInstant;
	/** The Message to show in this Screen. */
	public final Message message;
	/** The Pokemon species talking. null if not a Pokemon. */
	public final PokemonSpecies pokemon;
	/** True if the talking Pokemon is shiny. Has no effect if pokemon is null. */
	public final boolean shiny;
	/** If not null, the name of the speaker to appear in front of the text. */
	public final Message speakerName;

	public DialogScreen(Message message)
	{
		this((PokemonSpecies) null, message);
	}

	/** Shortcut constructor if using an instanciated Pokemon. */
	public DialogScreen(Pokemon pokemon, Message message)
	{
		this(pokemon.species(), pokemon.getNickname(), pokemon.isShiny(), message);
	}

	public DialogScreen(PokemonSpecies pokemon, Message message)
	{
		this(pokemon, null, message);
	}

	public DialogScreen(PokemonSpecies pokemon, Message speakerName, Message message)
	{
		this(pokemon, speakerName, false, message);
	}

	public DialogScreen(PokemonSpecies pokemon, Message speakerName, boolean shiny, Message message)
	{
		this.pokemon = pokemon;
		this.speakerName = speakerName;
		this.shiny = shiny;
		this.message = message;
		this.isInstant = false;

		if (this.speakerName != null) this.message.addPrefix(new Message(": ", false)).addPrefix(this.speakerName);
	}

	public DialogScreen setCentered()
	{
		this.isCentered = true;
		return this;
	}

	public DialogScreen setInstant()
	{
		this.isInstant = true;
		return this;
	}

}