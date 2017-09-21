package com.darkxell.client.mechanics;

import com.darkxell.client.mechanics.animation.StatChangeAnimation;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.renderers.MoveRenderer;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.stats.StatChangedEvent;

public final class MoveEventProcessor
{

	static void processDamageEvent(DamageDealtEvent event)
	{
		DungeonPokemonRenderer.instance.getSprite(event.target).setState(PokemonSprite.STATE_HURT);
		DungeonPokemonRenderer.instance.getSprite(event.target).setHealthChange(-event.damage);
		DungeonPersistance.dungeonState.setSubstate(new DelayState(DungeonPersistance.dungeonState, PokemonSprite.FRAMELENGTH));
		DungeonEventProcessor.processPending = false;
	}

	static void processFaintedEvent(FaintedPokemonEvent event)
	{
		DungeonPokemonRenderer.instance.unregister(event.pokemon);
	}

	static void processMoveEvent(MoveSelectionEvent event)
	{
		AnimationState s = new AnimationState(DungeonPersistance.dungeonState);
		s.animation = MoveRenderer.createAnimation(s, event.user, event.move.move());
		DungeonPersistance.dungeonState.setSubstate(s);
		DungeonEventProcessor.processPending = false;
	}

	static void processMoveUseEvent(MoveUseEvent event)
	{
		AnimationState s = new AnimationState(DungeonPersistance.dungeonState);
		s.animation = MoveRenderer.createTargetAnimation(s, event.user, event.move.move());
		if (s.animation != null)
		{
			DungeonPersistance.dungeonState.setSubstate(s);
			DungeonEventProcessor.processPending = false;
		}
	}

	public static void processStatEvent(StatChangedEvent event)
	{
		AnimationState s = new AnimationState(DungeonPersistance.dungeonState);
		s.animation = new StatChangeAnimation(s, event.target, event.stat, event.stage);
		if (s.animation != null)
		{
			DungeonPersistance.dungeonState.setSubstate(s);
			DungeonEventProcessor.processPending = false;
		}
	}

	private MoveEventProcessor()
	{}

}
