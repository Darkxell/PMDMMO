package com.darkxell.common.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
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
	 * @param events - The current Events being generated.
	 * 
	 * @return The new critical hit rate with modifications applied by this object. */
	public default int applyCriticalRateModifications(int critical, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		return critical;
	}

	/** Called when a Pokemon uses a damaging move. Modifies the value of the input effectiveness.
	 * 
	 * @param effectiveness - The effectiveness to modify.
	 * @param value - The value of the Stat before this object applies its modifications.
	 * @param move - The Move use context.
	 * @param target - The Pokemon the move was used on.
	 * @param isUser - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item it holds).
	 * 
	 * @return The new value of the effectiveness with modifications applied by this object. */
	public default double applyEffectivenessModifications(double effectiveness, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor)
	{
		return effectiveness;
	}

	/** Called when a Pokemon uses a damaging move. Modifies the value of the input Stat. (If attack or accuracy, it's for the user; if defense or evasion, it's for the target.)
	 * 
	 * @param stat - The Stat to modify.
	 * @param value - The value of the Stat before this object applies its modifications.
	 * @param move - The Move use context. May be <code>null</code> if this calculation is not part of a move use.
	 * @param target - The Pokemon the move was used on.
	 * @param isUser - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item it holds).
	 * @param events - The current Events being generated.
	 * 
	 * @return The new value of the Stat with modifications applied by this object. */
	public default double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		return value;
	}

	/** Called when a Pokemon uses a damaging move. Modifies the stage of the input Stat. (If attack or accuracy, it's for the user; if defense or evasion, it's for the target.)
	 * 
	 * @param stat - The Stat to modify.
	 * @param value - The value of the Stat before this object applies its modifications.
	 * @param move - The Move use context. May be <code>null</code> if this calculation is not part of a move use.
	 * @param target - The Pokemon the move was used on.
	 * @param isUser - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item it holds).
	 * @param events - The current Events being generated.
	 * 
	 * @return The new stage of the Stat with modifications applied by this object. */
	public default int applyStatStageModifications(Stat stat, int stage, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor,
			ArrayList<DungeonEvent> events)
	{
		return stage;
	}

	/** Called when a Pokemon uses a damaging move. Returns a damage multiplier to add to the final damage value.
	 * 
	 * @param move - The Move use context.
	 * @param target - The Pokemon the move was used on.
	 * @param isUser - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item it holds).
	 * @param floor - The Floor context.
	 * @param events - The current Events being generated.
	 * 
	 * @return The multiplier to add to the final damage value (damage *= returned_multiplier). */
	public default double damageMultiplier(MoveUse move, DungeonPokemon target, boolean isUser, Floor floor, ArrayList<DungeonEvent> events)
	{
		return 1;
	}

	/** Called when an Event targets a Pokemon.
	 * 
	 * @param event - The Event targeting the Pokemon.
	 * @param target - The Pokemon the event is targeting.
	 * @param events - The current Events being generated.
	 * 
	 * @return <code>false</code> if the Event shouldn't affect the Pokemon. */
	public default boolean isPokemonAffected(DungeonEvent event, DungeonPokemon target, ArrayList<DungeonEvent> events)
	{
		return true;
	}

	/** Called when a Pokemon tries to move. Returns true if this Pokemon can't move.
	 * 
	 * @param pokemon - The Pokemon trying to use a Move.
	 * @param floor - The Floor context.
	 * 
	 * @return <code>true</code> if the Pokemon can't move, <code>false</code> else. */
	public default boolean preventsMoving(DungeonPokemon pokemon, Floor floor)
	{
		return false;
	}

	/** Called when a Pokemon tries to use a Move. Returns true if this object prevents the use of a Move.
	 * 
	 * @param pokemon - The Pokemon trying to use a Move.
	 * @param floor - The Floor context.
	 * 
	 * @return <code>true</code> if a Move can't be used, <code>false</code> else. */
	public default boolean preventsUsingMoves(DungeonPokemon pokemon, Floor floor)
	{
		return false;
	}

}
