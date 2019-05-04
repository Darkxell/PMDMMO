package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.pokemon.RecruitAttemptEvent;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.util.language.Message;

public class BadgeItemEffect extends ItemEffect {

	public BadgeItemEffect(int id) {
		super(id);
	}

	@Override
	protected String getUseEffectID() {
		return "item.used.badge";
	}

	@Override
	public boolean isConsummable() {
		return false;
	}

	@Override
	public boolean isThrowable() {
		return false;
	}

	@Override
	public boolean isUsable() {
		return true;
	}

	@Override
	public boolean isUsableOnCatch() {
		return false;
	}

	@Override
	public void use(ItemUseEvent itemEvent, ArrayList<Event> events) {
		super.use(itemEvent, events);

		DungeonPokemon target = itemEvent.user.tile().adjacentTile(itemEvent.user.facing()).getPokemon();
		if (!itemEvent.user.isTeamLeader())
			events.add(new MessageEvent(itemEvent.floor, itemEvent, new Message("recruit.not_leader")));
		else if (target == null || target.type != DungeonPokemonType.WILD && target.type != DungeonPokemonType.MINIBOSS
				&& target.type != DungeonPokemonType.BOSS)
			events.add(new MessageEvent(itemEvent.floor, itemEvent, new Message("move.no_target")));
		else if (!RecruitAttemptEvent.checkFriendArea(itemEvent.user, target))
			events.add(new MessageEvent(itemEvent.floor, itemEvent, new Message("recruit.no_area")));
		else
			events.add(new RecruitAttemptEvent(itemEvent.floor, itemEvent, itemEvent.user, target));
	}

}
