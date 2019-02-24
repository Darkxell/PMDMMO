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

        Room current = floor.roomAt(moveEvent.user.tile());
        ArrayList<Room> rooms = new ArrayList<>();
        Collections.addAll(rooms, floor.rooms);
        if (current != null && rooms.size() >= 1)
            rooms.remove(current);

        Tile destination = null;

        if (rooms.size() >= 1)
            destination = RandomUtil.random(rooms, floor.random).randomTile(floor.random, TileType.GROUND, true);
        else
            destination = floor.randomEmptyTile(true, false, TileType.GROUND, floor.random);

        effects.createEffect(new PokemonTeleportedEvent(floor, eventSource, target, destination), moveEvent, missed, false, target);
    }

}
