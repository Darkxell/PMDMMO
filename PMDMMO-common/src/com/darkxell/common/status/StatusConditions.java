package com.darkxell.common.status;

import java.util.HashMap;

import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.conditions.BoostCritStatusCondition;
import com.darkxell.common.status.conditions.BoostMoveTypeStatusCondition;
import com.darkxell.common.status.conditions.ChangeAttackerStatStatusCondition;
import com.darkxell.common.status.conditions.ChargedMoveStatusCondition;
import com.darkxell.common.status.conditions.ConfusedStatusCondition;
import com.darkxell.common.status.conditions.ConstrictedStatusCondition;
import com.darkxell.common.status.conditions.CringedStatusCondition;
import com.darkxell.common.status.conditions.ImmuneStatusCondition;
import com.darkxell.common.status.conditions.ParalyzedStatusCondition;
import com.darkxell.common.status.conditions.PeriodicDamageStatusCondition;
import com.darkxell.common.status.conditions.PreventActionStatusCondition;
import com.darkxell.common.status.conditions.PreventOtherStatusCondition;
import com.darkxell.common.status.conditions.RemoveTypeImmunitiesStatusCondition;
import com.darkxell.common.status.conditions.StealsHpStatusCondition;
import com.darkxell.common.status.conditions.TauntedStatusCondition;
import com.darkxell.common.status.conditions.TerrifiedStatusCondition;

public final class StatusConditions
{
	static final HashMap<Integer, StatusCondition> _registry = new HashMap<Integer, StatusCondition>();

	public static final StatusCondition Poisoned = new PeriodicDamageStatusCondition(0, -1, -1, 10, 4);
	public static final StatusCondition Badly_poisoned = new PeriodicDamageStatusCondition(1, -1, -1, 2, 6);
	public static final StatusCondition Burn = new PeriodicDamageStatusCondition(2, -1, -1, 20, 5);
	public static final StatusCondition Asleep = new PreventActionStatusCondition(3, 3, 6);
	public static final StatusCondition Confused = new ConfusedStatusCondition(4, 7, 12);
	public static final StatusCondition Paralyzed = new ParalyzedStatusCondition(5, 3, 3);

	public static final StatusCondition Leech_seed = new StealsHpStatusCondition(10, 11, 12, 2, 10);

	public static final StatusCondition Constricted_fire = new ConstrictedStatusCondition(20, 4, 6, 2, 5);
	public static final StatusCondition Cringed = new CringedStatusCondition(25, 1, 1);
	public static final StatusCondition Terrified = new TerrifiedStatusCondition(26, 9, 11);
	public static final StatusCondition Taunted = new TauntedStatusCondition(27, 10, 11);

	public static final StatusCondition Protect = new ImmuneStatusCondition(40, 2, 3);
	public static final StatusCondition Light_screen = new ChangeAttackerStatStatusCondition(42, 11, 12, Stat.SpecialAttack, 0, .5);

	public static final StatusCondition Skull_bash = new ChargedMoveStatusCondition(60, 1, 1, -50);

	public static final StatusCondition Charging = new BoostMoveTypeStatusCondition(70, 1, 1, PokemonType.Electric);
	public static final StatusCondition Focus_energy = new BoostCritStatusCondition(71, 3, 3, 80);

	public static final StatusCondition Identified = new RemoveTypeImmunitiesStatusCondition(90, -1, -1, PokemonType.Ghost);

	public static final StatusCondition Sleepless = new PreventOtherStatusCondition(100, 11, 12, Asleep);

	/** @return The Status Condition with the input ID. */
	public static StatusCondition find(int id)
	{
		return _registry.get(id);
	}

	private StatusConditions()
	{}

}
