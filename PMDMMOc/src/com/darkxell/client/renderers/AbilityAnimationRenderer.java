package com.darkxell.client.renderers;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.pokemon.ability.AbilityTypeBoost;

public class AbilityAnimationRenderer
{

	public static AbstractAnimation createAnimation(TriggeredAbilityEvent event)
	{
		Ability ability = event.ability;

		if (ability instanceof AbilityTypeBoost)
		{
			PokemonAnimation a = Animations.getCustomAnimation(event.pokemon, Animations.ATTACK_UP, ClientEventProcessor.processEventsOnAnimationEnd);
			a.needsPause = false;
			return a;
		}

		return null;
	}

}
