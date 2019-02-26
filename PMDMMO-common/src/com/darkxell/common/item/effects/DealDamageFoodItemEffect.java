package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
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
    public void use(ItemUseEvent itemEvent, ArrayList<Event> events) {
        super.use(itemEvent, events);

        DungeonPokemon damageTarget = itemEvent.target.tile().adjacentTile(itemEvent.target.facing()).getPokemon();
        if (damageTarget != null) {
            DefaultDamageSource s = new DefaultDamageSource(itemEvent.floor, itemEvent.target.player(), itemEvent);
            events.add(new DamageDealtEvent(itemEvent.floor, itemEvent, damageTarget, s, DamageType.ITEM, this.damage));
            if (s.experienceEvent != null) events.add(s.experienceEvent);
        }
    }

    @Override
    public void useThrown(ItemUseEvent itemEvent, ArrayList<Event> events) {
        DungeonPokemon damageTarget = itemEvent.target;
        if (damageTarget != null) {
            DefaultDamageSource s = new DefaultDamageSource(itemEvent.floor, itemEvent.user.player(), itemEvent);
            events.add(new DamageDealtEvent(itemEvent.floor, itemEvent, damageTarget, s, DamageType.ITEM,
                    this.thrownDamage));
            if (s.experienceEvent != null) events.add(s.experienceEvent);
        }
    }

}
