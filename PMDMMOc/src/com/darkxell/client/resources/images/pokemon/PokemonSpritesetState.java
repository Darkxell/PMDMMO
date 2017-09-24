package com.darkxell.client.resources.images.pokemon;

import org.jdom2.Element;

public class PokemonSpritesetState
{

	final int duration;
	final int[] durations;
	final int[] indexes;

	public PokemonSpritesetState(Element xml)
	{
		String[] data = xml.getAttributeValue("duration").split(",");
		this.durations = new int[data.length];
		for (int i = 0; i < data.length; i++)
			this.durations[i] = Integer.parseInt(data[i]);

		if (xml.getAttribute("index") != null)
		{
			data = xml.getAttributeValue("index").split(",");
			this.indexes = new int[data.length];
			for (int i = 0; i < data.length; i++)
				this.indexes[i] = Integer.parseInt(data[i]);
		} else
		{
			this.indexes = new int[this.durations.length];
			for (int i = 0; i < this.indexes.length; ++i)
				this.indexes[i] = i;
		}

		this.duration = this.totalDuration();
	}

	public PokemonSpritesetState(int variantCount)
	{
		this.indexes = new int[variantCount];
		this.durations = new int[variantCount];

		for (int i = 0; i < variantCount; ++i)
		{
			this.indexes[i] = i;
			this.durations[i] = PokemonSprite.FRAMELENGTH;
		}

		this.duration = this.totalDuration();
	}

	public PokemonSpritesetState(int[] indexes, int[] durations)
	{
		this.indexes = indexes;
		this.durations = durations;
		this.duration = this.totalDuration();

	}

	public int duration(int counter)
	{
		return this.durations[counter];
	}

	private int totalDuration()
	{
		int d = 0;
		for (int i : this.durations)
			d += i;
		return d;
	}

}
