package com.darkxell.common.pokemon;

import java.util.ArrayList;

import com.darkxell.common.ai.visibility.Visibility.VisibleObjectType;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.item.Item;
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
public interface AffectsPokemon {

    /** Called when a Pokemon uses a damaging move. Modifies the critical hit rate.
     *
     * @param critical - The critical hit rate before this object applies its modifications.
     * @param move - The Move use context.
     * @param target - The Pokemon the move was used on.
     * @param isUser - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item it holds).
     * @param events - The current Events being generated.
     *
     * @return          The new critical hit rate with modifications applied by this object.
     */
    public default int applyCriticalRateModifications(int critical, MoveUse move, DungeonPokemon target, boolean isUser,
            Floor floor, ArrayList<Event> events) {
        return critical;
    }

    /** Called when a Pokemon uses a damaging move. Modifies the damage output.
     *
     * @param  damage    - The damage to be dealt.
     * @param  isUser    - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item
     *                   it holds).
     * @param  moveEvent TODO
     * @param  events    - The current Events being generated.
     * @return           The new damage with modifications applied by this object.
     */
    public default double applyDamageModifications(double damage, boolean isUser, MoveUseEvent moveEvent,
            ArrayList<Event> events) {
        return damage;
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
    public default double applyEffectivenessModifications(double effectiveness, MoveUse move, DungeonPokemon target, boolean isUser, Floor floor) {
        return effectiveness;
    }

    /** Called when a Pokemon uses a damaging move. Modifies the value of the input Stat. (If attack or accuracy, it's for the user; if defense or evasion, it's for the target.) Can also be called to compute a Pokemon's speed.
     *
     * @param  stat      - The Stat to modify.
     * @param  value     - The value of the Stat before this object applies its modifications.
     * @param  move      - The Move use context. May be <code>null</code> if this calculation is not part of a move use.
     * @param  target    - The Pokemon the move was used on.
     * @param  isUser    - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item
     *                   it holds).
     * @param  moveEvent - The move that was used and requires that stat modification. May be {@code null} if this
     *                   method computes Speed.
     * @param  events    - The current Events being generated.
     * @return           The new value of the Stat with modifications applied by this object.
     */
    public default double applyStatModifications(Stat stat, double value, MoveUse move, DungeonPokemon target,
            boolean isUser, Floor floor, MoveUseEvent moveEvent, ArrayList<Event> events) {
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
     * @return        The new stage of the Stat with modifications applied by this object.
     */
    public default int applyStatStageModifications(Stat stat, int stage, MoveUse move, DungeonPokemon target,
            boolean isUser, Floor floor, ArrayList<Event> events) {
        return stage;
    }

    /** Called when a Pokemon uses a damaging move. Returns a damage multiplier to add to the final damage value.
     *
     * @param  isUser    - <code>true</code> if this Object belongs to the Move's user (if it's its ability or an item
     *                   it holds).
     * @param  moveEvent TODO
     * @param  events    - The current Events being generated.
     * @return           The multiplier to add to the final damage value (damage *= returned_multiplier).
     */
    public default double damageMultiplier(boolean isUser, MoveUseEvent moveEvent, ArrayList<Event> events) {
        return 1;
    }

    /** Modifies the visibility of an object of the input type.
     * 
     * @param floor - The Floor context.
     * @param pokemon - The subject Pokemon.
     * @param object - The type of object to check.
     * @return <code>true</code> if the Pokemon is able to see the input object type from afar. */
    public default boolean hasSuperVision(Floor floor, DungeonPokemon pokemon, VisibleObjectType object) {
        return false;
    }

    /** Called when a Pokemon tries to move. Returns true if this Pokemon can't move.
     *
     * @param pokemon - The Pokemon trying to use a Move.
     * @param floor - The Floor context.
     *
     * @return <code>true</code> if the Pokemon can't move, <code>false</code> else. */
    public default boolean preventsMoving(DungeonPokemon pokemon, Floor floor) {
        return false;
    }

    /** Called when a Pokemon tries to use an Item. Returns true if this object prevents the use of that Item.
     *
     * @param item - The Item being tested. May be <code>null</code> if this is in a more general sense (i.e. if the pokemon can use <i>any</i> item).
     * @param pokemon - The Pokemon trying to use the Move.
     * @param floor - The Floor context.
     *
     * @return <code>true</code> if a Move can't be used, <code>false</code> else. */
    public default boolean preventsUsingItem(Item item, DungeonPokemon pokemon, Floor floor) {
        return false;
    }

    /** Called when a Pokemon tries to use a Move. Returns true if this object prevents the use of that Move.
     *
     * @param move - The Move being tested. May be <code>null</code> if this is in a more general sense (i.e. if the pokemon can use <i>any</i> move).
     * @param pokemon - The Pokemon trying to use the Move.
     * @param floor - The Floor context.
     *
     * @return <code>true</code> if a Move can't be used, <code>false</code> else. */
    public default boolean preventsUsingMove(LearnedMove move, DungeonPokemon pokemon, Floor floor) {
        return false;
    }

}
