package com.darkxell.common.pokemon;

public abstract class Evolution
{
	/** Evolution methods.<br />
	 * <ul>
	 * <li>LEVEL = 0</li>
	 * <li>ITEM = 1</li>
	 * <li>IQ = 2</li>
	 * </ul> */
	public static final byte LEVEL = 0, ITEM = 1, IQ = 2;

	/** How this Evolution occurs. See {@link Evolution#LEVEL}. */
	public final byte method;
	/** The ID of the species this Evolution leads to. */
	public final int species;
	/** The ID of the species form this Evolution leads to. */
	public final int speciesForm;
	/** If method is LEVEL, this is the minimum level of the Pokémon.<br />
	 * If method is ITEM, this is the ID of the Item to be used on.<br />
	 * If method is IQ, this is the minimum IQ of the Pokémon. */
	public final int value;

	public Evolution(int species, int speciesForm, byte method, int value)
	{
		this.species = species;
		this.speciesForm = speciesForm;
		this.method = method;
		this.value = value;
	}

}
