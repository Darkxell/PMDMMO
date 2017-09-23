package com.darkxell.client.mechanics.event;

import java.util.ArrayList;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.animation.StatChangeAnimation;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.renderers.MoveRenderer;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogScreen;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.PokemonStats;
import com.darkxell.common.util.Message;

public final class MoveEventProcessor
{

	static void processDamageEvent(DamageDealtEvent event)
	{
		DungeonPokemonRenderer.instance.getSprite(event.target).setState(PokemonSprite.STATE_HURT);
		DungeonPokemonRenderer.instance.getSprite(event.target).setHealthChange(-event.damage);
		DungeonPersistance.dungeonState.setSubstate(new DelayState(DungeonPersistance.dungeonState, PokemonSprite.FRAMELENGTH));
		DungeonEventProcessor.processPending = false;
	}

	public static void processExperienceEvent(ExperienceGainedEvent event)
	{
		int levels = event.levelsup();
		if (levels != 0 && DungeonPersistance.player.isAlly(event.pokemon))
		{
			DungeonEventProcessor.processPending = false;

			ArrayList<DialogScreen> screens = new ArrayList<DialogScreen>();
			for (int level = event.pokemon.getLevel() - levels + 1; level <= event.pokemon.getLevel(); ++level)
			{
				screens.add(new DialogScreen(new Message("xp.levelup").addReplacement("<pokemon>", event.pokemon.getNickname()).addReplacement("<level>",
						Integer.toString(level))));
				PokemonStats stats = event.pokemon.species.baseStatsIncrease(level - 1);
				screens.add(new DialogScreen(new Message("xp.stats").addReplacement("<atk>", TextRenderer.instance.alignNumber(stats.getAttack(), 2))
						.addReplacement("<def>", TextRenderer.instance.alignNumber(stats.getDefense(), 2))
						.addReplacement("<hea>", TextRenderer.instance.alignNumber(stats.getHealth(), 2))
						.addReplacement("<spa>", TextRenderer.instance.alignNumber(stats.getSpecialAttack(), 2))
						.addReplacement("<spd>", TextRenderer.instance.alignNumber(stats.getSpecialDefense(), 2))));
			}

			Launcher.stateManager.setState(new DialogState(DungeonPersistance.dungeonState, DungeonEventProcessor.processEventsOnDialogEnd, false, screens));
		}
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
