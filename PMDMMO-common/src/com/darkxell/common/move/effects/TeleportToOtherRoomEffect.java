package com.darkxell.common.move.effects;

import java.util.ArrayList;
import java.util.Collections;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.PokemonTeleportedEvent;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.RandomUtil;

public class TeleportToOtherRoomEffect extends MoveEffect {

    @Override
    public void effects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        if (!missed && !createAdditionals) {
            Room current = moveEvent.usedMove.user.tile().room();
            ArrayList<Room> rooms = new ArrayList<>();
            Collections.addAll(rooms, moveEvent.floor.rooms);
            if (current != null && rooms.size() >= 1)
                rooms.remove(current);

            Tile destination = null;

            if (rooms.size() >= 1)
                destination = RandomUtil.random(rooms, moveEvent.floor.random).randomTile(moveEvent.floor.random,
                        TileType.GROUND, true);
            else
                destination = moveEvent.floor.randomEmptyTile(true, false, TileType.GROUND, moveEvent.floor.random);

            effects.add(new PokemonTeleportedEvent(moveEvent.floor, moveEvent, moveEvent.target, destination));
        }
    }

}
