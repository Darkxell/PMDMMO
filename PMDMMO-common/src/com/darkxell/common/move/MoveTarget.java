package com.darkxell.common.move;

import com.darkxell.common.pokemon.DungeonPokemon;

public enum MoveTarget {
    /** Any Pokemon. */
    All,
    /** The user's allies, but not himself. */
    Allies,
    /** Only foes. */
    Foes,
    /** No targets (ambient moves.) */
    None,
    /** Any Pokemon except the user. */
    Others,
    /** The user and its allies. */
    Team,
    /** Only the user. */
    User;

    public boolean isValid(DungeonPokemon user, DungeonPokemon target) {
        switch (this) {
        case Allies:
            return target != user && target.isAlliedWith(user);

        case Foes:
            return !target.isAlliedWith(user);

        case Others:
            return target != user;

        case Team:
            return target == user || target.isAlliedWith(user);

        case User:
            return target == user;

        case None:
            return target == null;

        case All:
        default:
            return true;
        }
    }
}