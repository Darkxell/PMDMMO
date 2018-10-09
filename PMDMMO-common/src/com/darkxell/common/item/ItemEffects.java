package com.darkxell.common.item;

import static com.darkxell.common.item.effects.ThrowableItemEffect.ThrowableTrajectory.Arc;
import static com.darkxell.common.item.effects.ThrowableItemEffect.ThrowableTrajectory.Straight;

import java.util.Collection;
import java.util.HashMap;

import com.darkxell.common.item.effects.CureStatusFoodItemEffect;
import com.darkxell.common.item.effects.DealDamageFoodItemEffect;
import com.darkxell.common.item.effects.DrinkItemEffect;
import com.darkxell.common.item.effects.ElixirItemEffect;
import com.darkxell.common.item.effects.FoodItemEffect;
import com.darkxell.common.item.effects.GummiItemEffect;
import com.darkxell.common.item.effects.HealFoodItemEffect;
import com.darkxell.common.item.effects.InflictStatusFoodItemEffect;
import com.darkxell.common.item.effects.OrbItemEffect;
import com.darkxell.common.item.effects.StatBoostDrinkItemEffect;
import com.darkxell.common.item.effects.ThrowableItemEffect;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.StatusConditions;

/** Holds all Move Effects. */
public final class ItemEffects
{
	static final HashMap<Integer, ItemEffect> effects = new HashMap<Integer, ItemEffect>();

	public static final ItemEffect Default = new ItemEffect(-1);
	public static final ItemEffect Pokedollars = new ItemEffect(0);

	public static final ItemEffect Food_50_0_5 = new FoodItemEffect(1, 50, 0, 5);
	public static final ItemEffect Food_100_0_10 = new FoodItemEffect(2, 100, 0, 10);
	public static final ItemEffect Food_max_10_0 = new FoodItemEffect(3, 1000, 10, 0);
	public static final ItemEffect Grimy_Food = new FoodItemEffect(6, 30, 0, 0);

	public static final ItemEffect Heal_100_0_Food_5_0_0 = new HealFoodItemEffect(7, 5, 0, 0, 100, 0);
	public static final ItemEffect Heal_max_2_Food_5_0_0 = new HealFoodItemEffect(8, 5, 0, 0, 1000, 2);
	public static final ItemEffect Cure_paralysis_Food_5_0_0 = new CureStatusFoodItemEffect(9, 5, 0, 0, StatusConditions.Paralyzed);
	public static final ItemEffect Cure_sleep_Food_5_0_0 = new FoodItemEffect(10, 5, 0, 0);
	public static final ItemEffect Cure_poison_Food_5_0_0 = new CureStatusFoodItemEffect(11, 5, 0, 0, StatusConditions.Poisoned,
			StatusConditions.Badly_poisoned);
	public static final ItemEffect Cure_burn_Food_5_0_0 = new FoodItemEffect(12, 5, 0, 0);

	public static final ItemEffect Thrown_arc_15 = new ThrowableItemEffect(21, 15, Arc);
	public static final ItemEffect Thrown_arc_20 = new ThrowableItemEffect(22, 20, Arc);
	public static final ItemEffect Thrown_straight_6 = new ThrowableItemEffect(23, 60, Straight);
	public static final ItemEffect Thrown_straight_4 = new ThrowableItemEffect(24, 40, Straight);
	public static final ItemEffect Thrown_straight_1 = new ThrowableItemEffect(25, 10, Straight);
	public static final ItemEffect Thrown_straight_2 = new ThrowableItemEffect(26, 20, Straight);

	public static final ItemEffect Damage_45_Food_5_0_0 = new DealDamageFoodItemEffect(31, 5, 0, 0, 45);
	public static final ItemEffect Food_5_0_0 = new FoodItemEffect(39, 5, 0, 0);
	public static final ItemEffect Inflict_Asleep_Food_5_0_0 = new InflictStatusFoodItemEffect(42, 5, 0, 0, StatusConditions.Asleep);

	public static final ItemEffect Gummi_Normal = new GummiItemEffect(61, 20, 0, 0, PokemonType.Normal);
	public static final ItemEffect Gummi_Fighting = new GummiItemEffect(62, 20, 0, 0, PokemonType.Fighting);
	public static final ItemEffect Gummi_Flying = new GummiItemEffect(63, 20, 0, 0, PokemonType.Flying);
	public static final ItemEffect Gummi_Poison = new GummiItemEffect(64, 20, 0, 0, PokemonType.Poison);
	public static final ItemEffect Gummi_Ground = new GummiItemEffect(65, 20, 0, 0, PokemonType.Ground);
	public static final ItemEffect Gummi_Rock = new GummiItemEffect(66, 20, 0, 0, PokemonType.Rock);
	public static final ItemEffect Gummi_Bug = new GummiItemEffect(67, 20, 0, 0, PokemonType.Bug);
	public static final ItemEffect Gummi_Ghost = new GummiItemEffect(68, 20, 0, 0, PokemonType.Ghost);
	public static final ItemEffect Gummi_Steel = new GummiItemEffect(69, 20, 0, 0, PokemonType.Steel);
	public static final ItemEffect Gummi_Fire = new GummiItemEffect(70, 20, 0, 0, PokemonType.Fire);
	public static final ItemEffect Gummi_Water = new GummiItemEffect(71, 20, 0, 0, PokemonType.Water);
	public static final ItemEffect Gummi_Grass = new GummiItemEffect(72, 20, 0, 0, PokemonType.Grass);
	public static final ItemEffect Gummi_Electric = new GummiItemEffect(73, 20, 0, 0, PokemonType.Electric);
	public static final ItemEffect Gummi_Psychic = new GummiItemEffect(74, 20, 0, 0, PokemonType.Psychic);
	public static final ItemEffect Gummi_Ice = new GummiItemEffect(75, 20, 0, 0, PokemonType.Ice);
	public static final ItemEffect Gummi_Dragon = new GummiItemEffect(76, 20, 0, 0, PokemonType.Dragon);
	public static final ItemEffect Gummi_Dark = new GummiItemEffect(77, 20, 0, 0, PokemonType.Dark);
	public static final ItemEffect Gummi_Fairy = new GummiItemEffect(78, 20, 0, 0, PokemonType.Fairy);

	public static final ItemEffect Orb_Default = new OrbItemEffect(81, 2500);
	public static final ItemEffect Orb_Blowback = new OrbItemEffect(82, 2501);
	public static final ItemEffect Orb_Escape = new OrbItemEffect(86, 2505);
	public static final ItemEffect Orb_Hurl = new OrbItemEffect(90, 2509);
	public static final ItemEffect Orb_Petrify = new OrbItemEffect(102, 2521);
	public static final ItemEffect Orb_Switcher = new OrbItemEffect(125, 2544);
	public static final ItemEffect Orb_Warp = new OrbItemEffect(133, 2552);

	public static final ItemEffect PP_10_Food_5_0_0 = new ElixirItemEffect(151, 5, 0, 0, 10);
	public static final ItemEffect PP_max_Food_5_0_0 = new ElixirItemEffect(152, 5, 0, 0, 1000);

	public static final ItemEffect Boost_Attack_Drink_5_0_0 = new StatBoostDrinkItemEffect(153, 5, 0, 0, Stat.Attack);
	public static final ItemEffect Boost_Defense_Drink_5_0_0 = new StatBoostDrinkItemEffect(154, 5, 0, 0, Stat.Defense);
	public static final ItemEffect Boost_Sp_Attack_Drink_5_0_0 = new StatBoostDrinkItemEffect(155, 5, 0, 0, Stat.SpecialAttack);
	public static final ItemEffect Boost_Sp_Defense_Drink_5_0_0 = new StatBoostDrinkItemEffect(156, 5, 0, 0, Stat.SpecialDefense);
	public static final ItemEffect Drink_5_0_0 = new DrinkItemEffect(157, 5, 0, 0);

	public static final ItemEffect XRaySpecs = new ItemEffect(208);

	/** @return The Effect with the input ID. */
	public static ItemEffect find(int id)
	{
		if (!effects.containsKey(id)) return Default;
		return effects.get(id);
	}

	/** @return All Effects. */
	public static Collection<ItemEffect> list()
	{
		return effects.values();
	}

}
