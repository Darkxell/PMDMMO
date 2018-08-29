package com.darkxell.common.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventListener;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class StatusCondition implements AffectsPokemon, DamageSource, DungeonEventListener
{
	private static final HashMap<Integer, StatusCondition> _registry = new HashMap<Integer, StatusCondition>();

	public static final StatusCondition Poisoned = new PeriodicDamageStatusCondition(0, -1, -1, 10, 4);
	public static final StatusCondition Badly_poisoned = new PeriodicDamageStatusCondition(1, -1, -1, 2, 6);
	public static final StatusCondition Burn = new PeriodicDamageStatusCondition(2, -1, -1, 20, 5);
	public static final StatusCondition Asleep = new PreventsActionStatusCondition(3, 3, 6);
	public static final StatusCondition Confused = new ConfusedStatusCondition(4, 7, 12);

	public static final StatusCondition Leech_seed = new StealsHpStatusCondition(10, 11, 12, 2, 10);

	public static final StatusCondition Constricted_fire = new ConstrictedStatusCondition(20, 4, 6, 2, 5);
	public static final StatusCondition Cringed = new CringedStatusCondition(25, 1, 1);

	public static final StatusCondition Protect = new ImmuneStatusCondition(40, 2, 3);
	public static final StatusCondition Light_screen = new ChangeAttackerStatStatusCondition(42, 11, 12, Stat.SpecialAttack, 0, .5);

	public static final StatusCondition Skull_bash = new ChargedMoveStatusCondition(60, 1, 1, -50);

	public static final StatusCondition Sleepless = new PreventsOtherStatusCondition(100, 11, 12, Asleep);

	/** @return The Status Condition with the input ID. */
	public static StatusCondition find(int id)
	{
		return _registry.get(id);
	}

	/** This Status condition's duration. -1 for indefinite. */
	public final int durationMin, durationMax;
	/** This Status Condition's ID. */
	public final int id;

	public StatusCondition(int id, int durationMin, int durationMax)
	{
		this.id = id;
		this.durationMin = durationMin;
		this.durationMax = durationMax;
		_registry.put(this.id, this);
	}

	/** @return - True if this Status Condition affects the input Pokemon.<br>
	 *         - A Message to display if this Condition doesn't affect the Pokemon. May be <code>null</code> if there is no necessary message. */
	public Pair<Boolean, Message> affects(DungeonPokemon pokemon)
	{
		if (pokemon.hasStatusCondition(this)) return new Pair<>(false,
				new Message("status.already").addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<condition>", this.name()));
		for (AppliedStatusCondition c : pokemon.activeStatusConditions())
			if (c.condition instanceof PreventsOtherStatusCondition && ((PreventsOtherStatusCondition) c.condition).prevents(this))
				new Message("status.prevented.condition").addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<prevented>", this.name())
						.addReplacement("<preventer>", c.condition.name());
		return new Pair<>(true, null);
	}

	public AppliedStatusCondition create(DungeonPokemon target, Object source, Random random)
	{
		return new AppliedStatusCondition(this, target, source, RandomUtil.nextIntInBounds(this.durationMin, this.durationMax, random));
	}

	@Override
	public ExperienceGeneratedEvent getExperienceEvent()
	{
		return null;
	}

	Message immune(DungeonPokemon pokemon)
	{
		return new Message("status.immune").addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<condition>", this.name());
	}

	public Message name()
	{
		return new Message("status." + this.id);
	}

	public void onEnd(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{}

	public void onStart(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{}

	public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<DungeonEvent> events)
	{}

}
