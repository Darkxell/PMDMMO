package com.darkxell.client.resources.image.pokemon.body;

public enum PokemonSpriteState {
    APPEAL(22),
    ATTACK(2, true),
    BITE(15, true),
    CHARGE(6),
    CHOP(9, true),
    DANCE(23),
    DOUBLE(37),
    EMIT(32),
    FLAPAROUND(29),
    GAS(30, true),
    HIGHJUMP(41),
    HURT(5),
    IDLE(0),
    JAB(17, true),
    JUMP(40),
    KICK(18, true),
    LICK(19, true),
    MOVE(1),
    MULTISCRATCH(11, true),
    MULTISTRIKE(12, true),
    OTHER(42),
    REARUP(34),
    RICOCHET(14, true),
    ROTATE(38),
    RUMBLE(28),
    SHAKE(16),
    SHOCK(31),
    SHOOT(7),
    SING(26),
    SLAM(20, true),
    SLEEP(4),
    SLICE(10, true),
    SOUND(27),
    SPECIAL(3),
    SPIN(39),
    STOMP(21, true),
    STRIKE(8, true),
    SWELL(35),
    SWING(36),
    TAILWHIP(25),
    TWIRL(24),
    UPPERCUT(13, true),
    WITHDRAW(33);
    /* REST(6), WAKING(7), VICTORYPOSE(8), EATING(9); */

    /** True if the State's animation should dash forward. */
    public final boolean hasDash;
    public final int id;

    PokemonSpriteState(int id) {
        this(id, false);
    }

    PokemonSpriteState(int id, boolean hasDash) {
        this.id = id;
        this.hasDash = hasDash;
    }
}
