package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DefaultDamageSource;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DealDamageFoodItemEffect extends FoodItemEffect {

    public final int damage, thrownDamage;

    public DealDamageFoodItemEffect(int id, int food, int belly, int bellyIfFull, int damage, int thrownDamage) {
        super(id, food, belly, bellyIfFull);
        this.damage = damage;
        this.thrownDamage = thrownDamage;
    }

    @Override
    public void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target,
            ArrayList<DungeonEvent> events) {
        super.use(floor, item, pokemon, target, events);

        DungeonPokemon damageTarget = target.tile().adjacentTile(target.facing()).getPokemon();
        if (damageTarget != null) {
            DefaultDamageSource s = new DefaultDamageSource(floor, target.player());
            events.add(new DamageDealtEvent(floor, damageTarget, s, DamageType.ITEM, this.damage));
            if (s.experienceEvent != null)
                events.add(s.experienceEvent);
        }
    }

    @Override
    public void useThrown(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target,
            ArrayList<DungeonEvent> events) {
        DungeonPokemon damageTarget = target;
        if (damageTarget != null) {
            DefaultDamageSource s = new DefaultDamageSource(floor, pokemon.player());
            events.add(new DamageDealtEvent(floor, damageTarget, s, DamageType.ITEM, this.thrownDamage));
            if (s.experienceEvent != null)
                events.add(s.experienceEvent);
        }
    }

}
