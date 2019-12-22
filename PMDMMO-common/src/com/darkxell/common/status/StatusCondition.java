package com.darkxell.common.status;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventListener;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class StatusCondition extends Status implements AffectsPokemon, DamageSource, EventListener {

    /**
     * <code>true</code> if this is an ailment, i.e. if this is an adverse effect on the Pokemon on which it's applied.
     */
    public final boolean isAilment;

    public StatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, durationMin, durationMax);
        this.isAilment = isAilment;
        StatusConditions._registry.put(this.id, this);
    }

    /**
     * @return - True if this Status Condition affects the input Pokemon.<br>
     *         - A Message to display if this Condition doesn't affect the Pokemon. May be <code>null</code> if there is
     *         no necessary message.
     */
    public Pair<Boolean, Message> affects(Floor floor, AppliedStatusCondition condition, DungeonPokemon pokemon) {
        if (pokemon.hasStatusCondition(this))
            return new Pair<>(false, new Message("status.already").addReplacement("<pokemon>", pokemon.getNickname())
                    .addReplacement("<condition>", this.name()));
        return new Pair<>(true, null);
    }

    public AppliedStatusCondition create(Floor floor, DungeonPokemon target, Object source) {
        return new AppliedStatusCondition(this, target, source,
                RandomUtil.nextIntInBounds(this.durationMin, this.durationMax, floor.random));
    }

    @Override
    public ExperienceGeneratedEvent getExperienceEvent() {
        return null;
    }

    protected Message immune(DungeonPokemon pokemon) {
        return new Message("status.immune").addReplacement("<pokemon>", pokemon.getNickname())
                .addReplacement("<condition>", this.name());
    }

    public Message name() {
        return new Message("status." + this.id);
    }

    public void onEnd(StatusConditionEndedEvent event, ArrayList<Event> events) {
    }

    public void onStart(Floor floor, AppliedStatusCondition instance, ArrayList<Event> events) {
    }

    public void tick(Floor floor, AppliedStatusCondition instance, ArrayList<Event> events) {
    }

}
