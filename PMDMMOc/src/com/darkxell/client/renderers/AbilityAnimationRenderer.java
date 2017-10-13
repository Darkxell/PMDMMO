package com.darkxell.client.renderers;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.StatChangeAnimation;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.PokemonStats;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.pokemon.ability.AbilityTypeBoost;

public class AbilityAnimationRenderer
{

	public static AbstractAnimation createAnimation(TriggeredAbilityEvent event)
	{
		Ability ability = event.ability;

		if (ability instanceof AbilityTypeBoost)
		{
			StatChangeAnimation a = new StatChangeAnimation(ClientEventProcessor.processEventsOnAnimationEnd, event.pokemon, PokemonStats.ATTACK, 1);
			a.needsPause = false;
			return a;
		}

		return null;
	}

}
