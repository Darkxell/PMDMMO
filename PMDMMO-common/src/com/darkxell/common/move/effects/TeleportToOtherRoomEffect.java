package com.darkxell.common.move.effects;

import java.util.ArrayList;
import java.util.Collections;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.PokemonTeleportedEvent;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.util.RandomUtil;

public class TeleportToOtherRoomEffect extends MoveEffect {

    public TeleportToOtherRoomEffect(int id) {
        super(id);
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        Room current = moveEvent.floor.roomAt(moveEvent.usedMove.user.tile());
        ArrayList<Room> rooms = new ArrayList<>();
        Collections.addAll(rooms, moveEvent.floor.rooms);
        if (current != null && rooms.size() >= 1) rooms.remove(current);

        Tile destination = null;

        if (rooms.size() >= 1) destination = RandomUtil.random(rooms, moveEvent.floor.random).randomTile(moveEvent.floor.random, TileType.GROUND, true);
        else destination = moveEvent.floor.randomEmptyTile(true, false, TileType.GROUND, moveEvent.floor.random);

        effects.createEffect(new PokemonTeleportedEvent(moveEvent.floor, moveEvent, moveEvent.target, destination), moveEvent, missed, false, moveEvent.target);
    }

}
