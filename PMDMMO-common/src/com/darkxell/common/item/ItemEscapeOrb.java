package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

/** An Item that has different effects when used. */
public class ItemEscapeOrb extends ItemOrb
{

	public ItemEscapeOrb(Element xml)
	{
		super(xml);
	}

	public ItemEscapeOrb(int id, int price, int sell, int sprite, boolean isStackable)
	{
		super(id, price, sell, sprite, isStackable);
	}

	@Override
	public void use(Floor floor, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		// if (pokemon.player() == null) events.addAll(new PokemonDespawnedEvent(floor, pokemon));else TODO PokemonDespawn on EscapeOrb with no Player
		events.add(new DungeonExitEvent(floor, pokemon.player()));
	}
}
