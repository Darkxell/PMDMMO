package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.item.effects.ThrowableItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.util.Direction;

public class ProjectileThrownEvent extends DungeonEvent implements DamageSource
{

	public final Tile destination;
	public final Direction direction;
	public final ExperienceGeneratedEvent experienceEvent;
	public final Item item;
	private ItemStack placedItem;
	public final DungeonPokemon thrower;

	public ProjectileThrownEvent(Floor floor, Item item, DungeonPokemon thrower, Tile destination)
	{
		super(floor);
		this.item = item;
		this.thrower = thrower;
		this.destination = destination;
		this.experienceEvent = this.thrower.type == DungeonPokemonType.TEAM_MEMBER ? new ExperienceGeneratedEvent(this.floor, this.thrower.player()) : null;
		this.direction = AIUtils.direction(this.thrower, this.destination);
	}

	@Override
	public ExperienceGeneratedEvent getExperienceEvent()
	{
		return this.experienceEvent;
	}

	@Override
	public String loggerMessage()
	{
		return this.thrower + " threw " + this.item + " (destination= " + this.destination + ").";
	}

	public ItemStack placedItem()
	{
		return this.placedItem;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		if (this.item.effect() instanceof ThrowableItemEffect && this.destination.getPokemon() != null)
		{
			this.resultingEvents.add(new DamageDealtEvent(this.floor, this.destination.getPokemon(), this, ((ThrowableItemEffect) this.item.effect()).damage));
			this.resultingEvents.add(this.experienceEvent);
		} else
		{
			this.placedItem = new ItemStack(this.item.id);
			this.destination.setItem(this.placedItem);
			this.floor.dungeon.communication.itemIDs.register(this.placedItem, null);
		}
		return super.processServer();
	}

}
