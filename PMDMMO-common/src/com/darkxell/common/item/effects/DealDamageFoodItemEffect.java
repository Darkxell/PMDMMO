package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DefaultDamageSource;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DealDamageFoodItemEffect extends FoodItemEffect {

    public final int damage, thrownDamage;

    public DealDamageFoodItemEffect(int id, int food, int belly, int bellyIfFull, int damage, int thrownDamage) {
        super(id, food, belly, bellyIfFull);
        this.damage = damage;
        this.thrownDamage = thrownDamage;
    }

    @Override
    public void use(ItemUseEvent itemEvent, ArrayList<DungeonEvent> events) {
        super.use(itemEvent, events);

        DungeonPokemon damageTarget = target.tile().adjacentTile(target.facing()).getPokemon();
        if (damageTarget != null) {
            DefaultDamageSource s = new DefaultDamageSource(itemEvent, target.player());
            events.add(new DamageDealtEvent(itemEvent, eventSource, damageTarget, s, DamageType.ITEM, this.damage));
            if (s.experienceEvent != null)
                events.add(s.experienceEvent);
        }
    }

    @Override
    public void useThrown(ItemUseEvent itemEvent, ArrayList<DungeonEvent> events) {
        DungeonPokemon damageTarget = target;
        if (damageTarget != null) {
            DefaultDamageSource s = new DefaultDamageSource(floor, pokemon.player());
            events.add(new DamageDealtEvent(floor, eventSource, damageTarget, s, DamageType.ITEM, this.thrownDamage));
            if (s.experienceEvent != null)
                events.add(s.experienceEvent);
        }
    }

}
