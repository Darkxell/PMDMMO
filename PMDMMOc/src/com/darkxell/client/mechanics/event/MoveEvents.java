package com.darkxell.client.mechanics.event;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.mechanics.animation.StatChangeAnimation;
import com.darkxell.client.renderers.AbilityAnimationRenderer;
import com.darkxell.client.renderers.MoveRenderer;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.state.DialogState;
import com.darkxell.client.state.DialogState.DialogScreen;
import com.darkxell.client.state.dungeon.AnimationState;
import com.darkxell.client.state.dungeon.DelayState;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.*;
import com.darkxell.common.event.stats.ExperienceGainedEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.PokemonStats;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.language.Message;

public final class MoveEvents
{

	public static void processAbilityEvent(TriggeredAbilityEvent event)
	{
		AbstractAnimation animation = AbilityAnimationRenderer.createAnimation(event);
		if (animation != null && animation.needsPause)
		{
			AnimationState s = new AnimationState(Persistance.dungeonState);
			s.animation = animation;
			Persistance.dungeonState.setSubstate(s);
			Persistance.eventProcessor.processPending = false;
		} else animation.start();
	}

	static void processDamageEvent(DamageDealtEvent event)
	{
		if (!(event.source instanceof BellyChangedEvent))
		{
			Persistance.dungeonState.pokemonRenderer.getRenderer(event.target).sprite.setState(PokemonSprite.STATE_HURT);
			Persistance.dungeonState.pokemonRenderer.getRenderer(event.target).sprite.setHealthChange(-event.damage);
			Persistance.dungeonState.setSubstate(new DelayState(Persistance.dungeonState, PokemonSprite.FRAMELENGTH));
			Persistance.eventProcessor.processPending = false;
		}
	}

	public static void processExperienceEvent(ExperienceGainedEvent event)
	{
		int levels = event.levelsup();
		if (levels != 0 && Persistance.player.isAlly(event.pokemon))
		{
			Persistance.eventProcessor.processPending = false;

			ArrayList<DialogScreen> screens = new ArrayList<DialogScreen>();
			for (int level = event.pokemon.getLevel() - levels + 1; level <= event.pokemon.getLevel(); ++level)
			{
				screens.add(new DialogScreen(new Message("xp.levelup").addReplacement("<pokemon>", event.pokemon.getNickname()).addReplacement("<level>",
						Integer.toString(level))));
				PokemonStats stats = event.pokemon.species.baseStatsIncrease(level - 1);
				screens.add(new DialogScreen(new Message("xp.stats").addReplacement("<atk>", TextRenderer.alignNumber(stats.getAttack(), 2))
						.addReplacement("<def>", TextRenderer.alignNumber(stats.getDefense(), 2))
						.addReplacement("<hea>", TextRenderer.alignNumber(stats.getHealth(), 2))
						.addReplacement("<spa>", TextRenderer.alignNumber(stats.getSpecialAttack(), 2))
						.addReplacement("<spd>", TextRenderer.alignNumber(stats.getSpecialDefense(), 2))));
			}

			Persistance.stateManager.setState(new DialogState(Persistance.dungeonState, ClientEventProcessor.processEventsOnDialogEnd, false, screens));
		}
	}

	static void processFaintedEvent(FaintedPokemonEvent event)
	{
		Persistance.dungeonState.pokemonRenderer.unregister(event.pokemon);
	}

	static void processMoveEvent(MoveSelectionEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = MoveRenderer.createAnimation(s, event.usedMove.user, event.usedMove.move.move());
		Persistance.dungeonState.setSubstate(s);
		Persistance.eventProcessor.processPending = false;
	}

	static void processMoveUseEvent(MoveUseEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = MoveRenderer.createTargetAnimation(s, event.usedMove.user, event.usedMove.move.move());
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			Persistance.eventProcessor.processPending = false;
		}
	}

	public static void processStatEvent(StatChangedEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		s.animation = new StatChangeAnimation(s, event.target, event.stat, event.stage);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			Persistance.eventProcessor.processPending = false;
		}
	}

	public static void processStatusEvent(StatusConditionCreatedEvent event)
	{
		AnimationState s = new AnimationState(Persistance.dungeonState);
		if (event.condition.condition == StatusCondition.POISONED || event.condition.condition == StatusCondition.BADLY_POISONED) s.animation = SpritesetAnimation
				.getCustomAnimation(event.condition.pokemon, 200, s);
		if (s.animation != null)
		{
			Persistance.dungeonState.setSubstate(s);
			Persistance.eventProcessor.processPending = false;
		}
	}

	private MoveEvents()
	{}

}
