package com.darkxell.common.pokemon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.BaseStats.Stat;

/** Any game object that may modify stats or behavior of a Pokemon should implement this interface, for consistency.<br>
 * This interface has default methods and only methods that are necessary can be implemented.<br>
 * This allows for all methods that may affect a Pokemon's stats or behavior to be part of this interface. <br>
 * Some objects that implement this interface do not <i>belong</i> to a Pokemon (such as Weather), or the object it belongs to is self-evident (such as Move); in that case, <code>isUser</code> booleans in method arguments should be ignored.<br>
 * <br>
 * 
 * This Interface allows the alteration of:<br>
 * <ul>
 * <li>Critical hit rates</li>
 * <li>Stat stages</li>
 * <li>Stat values</li>
 * <li>Damage multiplier</li>
 * </ul>
 */
public interface AffectsPokemon
{

	/** Called when a Pokemon uses a damaging move. Modifies the critical hit rate.
	 * 
	 * @param critical - The critical hit rate before this object applies its modifications.
	 * @param move - The Move use context.
	 * @param target - The Pokemon the move was used on.
	 * @param isUser - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item it holds).
	 * 
	 * @return The new critical hit rate with modifications applied by this object. */
	public default int applyCriticalRateModifications(int critical, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return critical;
	}

	/** Called when a Pokemon uses a damaging move. Modifies the value of the input Stat. (If attack or accuracy, it's for the user; if defense or evasion, it's for the target.)
	 * 
	 * @param stat - The Stat to modify.
	 * @param value - The value of the Stat before this object applies its modifications.
	 * @param move - The Move use context.
	 * @param target - The Pokemon the move was used on.
	 * @param isUser - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item it holds).
	 * 
	 * @return The new value of the Stat with modifications applied by this object. */
	public default double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return value;
	}

	/** Called when a Pokemon uses a damaging move. Modifies the stage of the input Stat. (If attack or accuracy, it's for the user; if defense or evasion, it's for the target.)
	 * 
	 * @param stat - The Stat to modify.
	 * @param value - The value of the Stat before this object applies its modifications.
	 * @param move - The Move use context.
	 * @param target - The Pokemon the move was used on.
	 * @param isUser - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item it holds).
	 * 
	 * @return The new stage of the Stat with modifications applied by this object. */
	public default int applyStatStageModifications(Stat stat, int stage, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return stage;
	}

	/** Called when a Pokemon uses a damaging move. Returns a damage multiplier to add to the final damage value.
	 * 
	 * @param move - The Move use context.
	 * @param target - The Pokemon the move was used on.
	 * @param isUser - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item it holds).
	 * @param floor - The Floor context.
	 * 
	 * @return The new stage of the Stat with modifications applied by this object. */
	public default double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return 1;
	}

}