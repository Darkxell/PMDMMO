package com.darkxell.common.move.behavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveCategory;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

public class MoveBehavior {

    /** Default move effect for all moves. */
    private class DefaultEffect extends MoveEffect {
        public DefaultEffect() {
            super(0);
        }

        @Override
        public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
                ArrayList<Event> effects, boolean createAdditionals) {
            if (context.target != null && !createAdditionals)
                if (missed)
                    effects.add(new MessageEvent(context.floor, context.event,
                            new Message(context.target == null ? "move.miss.no_target" : "move.miss")
                                    .addReplacement("<pokemon>", context.target == null ? new Message("no one", false)
                                            : context.target.getNickname())));
                else if (context.move.dealsDamage)
                    effects.add(new DamageDealtEvent(context.floor, context.event, context.target,
                            context.event.usedMove, DamageType.MOVE, calculator.compute(effects)));
        }
    }

    public static final String DEFAULT_DESCRIPTION_ID = "move.info.default";
    private final ArrayList<MoveEffect> effects = new ArrayList<>();

    public final int id;

    public MoveBehavior(int id, MoveEffect... effects) {
        this.id = id;
        this.effects.addAll(Arrays.asList(effects));
        this.effects.add(new DefaultEffect());
        this.effects.sort(Comparator.naturalOrder());
        if (this.id != -1)
            MoveBehaviors.register(this);
    }

    public boolean allowsNoTarget(Move move, DungeonPokemon user) {
        for (MoveEffect effect : this.effects) {
            if (effect.hasEffectWithoutTarget(move, user))
                return true;
        }
        return false;
    }

    private MoveEffectCalculator buildCalculator(MoveContext moveContext) {
        MoveEffectCalculator calculator = null; // TODO build calculator
        if (calculator == null)
            calculator = new MoveEffectCalculator(moveContext);
        return null;
    }

    /**
     * Creates the MoveUseEvent and adds it to the events list.
     *
     * @param moveEvent - The Move use context.
     * @param target    - The target Pokemon.
     * @param events    - The event list to add the MoveUseEvent to.
     */
    protected void createMoveForTarget(MoveSelectionEvent moveEvent, DungeonPokemon target, ArrayList<Event> events) {
        events.add(new MoveUseEvent(moveEvent.floor, moveEvent, moveEvent.usedMove(), target));
    }

    protected void createMoves(MoveSelectionEvent moveEvent, ArrayList<Event> events) {
        Move m = moveEvent.usedMove().move.move();

        DungeonPokemon[] pokemon = new MoveTargetSelector(moveEvent.floor, m, moveEvent.usedMove().user).select();
        for (DungeonPokemon element : pokemon)
            this.createMoveForTarget(moveEvent, element, events);
    }

    public Message description(Move move) {
        if (Localization.containsKey("move.info." + this.id))
            return new Message("move.info." + move.getID());

        Message m;
        if (Localization.containsKey("move.info." + this.id))
            m = new Message("move.info." + this.id);
        else
            m = this.descriptionBase(move);
        m.addReplacement("<move>", move.name());
        if (move.dealsDamage)
            m.addPrefix(new Message("move.info.deals_damage").addSuffix(" <br>"));

        // Add all effect descriptions
        for (MoveEffect e : this.effects) {
            Message em = e.description();

            if (!em.id.equals(DEFAULT_DESCRIPTION_ID)) { // If effect has default description, don't show it
                em.addReplacement("<move>", move.name());

                if (m.id.equals(DEFAULT_DESCRIPTION_ID)) // If compound has default description, don't show it
                    m = em;
                else { // Else add effect description to total description
                    m.addSuffix(" <br>");
                    m.addSuffix(em);
                }
            }
        }
        return m;
    }

    public Message descriptionBase(Move move) {
        return new Message(DEFAULT_DESCRIPTION_ID);
    }

    public ArrayList<MoveEffect> effects() {
        return new ArrayList<>(this.effects);
    }

    public PokemonType getMoveType(Move move, Pokemon user) {
        PokemonType finalType = move.getType();
        for (MoveEffect effect : this.effects) {
            PokemonType type = effect.alterMoveType(move, user);
            if (type != null)
                finalType = type;
        }
        return finalType;
    }

    public boolean hasEffect(MoveEffect effect) {
        return this.effects.contains(effect);
    }

    /**
     * This method creates the main effects of this Move. This method shouldn't be overridden unless you want to change
     * the basics of how Moves work. Effects should be added to the input MoveEvents list.
     *
     * @param context    - The Move Use context.
     * @param calculator - Object that helps with Damage computation.
     * @param missed     - <code>true</code> if the Move missed.
     * @param effects    - Resulting Events list.
     */
    protected void mainEffects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {

        ArrayList<Event> createdEffects = new ArrayList<>();
        for (MoveEffect effect : this.effects) {
            effect.effects(context, calculator, missed, createdEffects, false);
        }

        ArrayList<Event> originalEffects = new ArrayList<>(createdEffects);
        for (MoveEffect effect : this.effects) {
            effect.effects(context, calculator, missed, createdEffects, true);
        }

        ArrayList<Event> additionalEffects = new ArrayList<>(createdEffects);
        additionalEffects.removeAll(originalEffects); // Get only additional

        for (Event effect : originalEffects) {
            effects.createEffect(effect, false);
        }
        for (Event effect : additionalEffects) {
            effects.createEffect(effect, true);
        }
    }

    public void onMoveSelected(MoveSelectionEvent moveEvent, ArrayList<Event> events) {
        this.createMoves(moveEvent, events);

        for (MoveEffect effect : this.effects) {
            effect.additionalEffectsOnUse(moveEvent, moveEvent.usedMove().move.move(), events);
        }

        if (events.size() == 0 && this != MoveBehaviors.Basic_attack)
            events.add(new MessageEvent(moveEvent.floor, moveEvent, new Message("move.no_target")));
    }

    /**
     * Main method called when a Pokemon uses a Move on a target.
     *
     * @return <code>true</code> if the Move missed.
     */
    public boolean onMoveUsed(MoveContext context, ArrayList<Event> events) {
        MoveEffectCalculator calculator = this.buildCalculator(context);
        boolean missed = calculator.misses(events);
        double effectiveness = calculator.effectiveness();
        if (effectiveness == PokemonType.NO_EFFECT && context.move.category != MoveCategory.Status)
            events.add(new MessageEvent(context.floor, context.event, context.move.unaffectedMessage(context.target)));
        else {
            if (!missed && this != MoveBehaviors.Basic_attack && context.target != null)
                context.target.receiveMove(
                        context.learnedMove.isLinked() ? DungeonPokemon.LINKED_MOVES : DungeonPokemon.MOVES);
            if (!missed && context.move.dealsDamage)
                if (effectiveness >= PokemonType.SUPER_EFFECTIVE)
                    events.add(new MessageEvent(context.floor, context.event, new Message("move.effectiveness.super")
                            .addReplacement("<pokemon>", context.target.getNickname())));
                else if (effectiveness <= PokemonType.NOT_VERY_EFFECTIVE)
                    events.add(new MessageEvent(context.floor, context.event, new Message("move.effectiveness.not_very")
                            .addReplacement("<pokemon>", context.target.getNickname())));

            MoveEvents effects = new MoveEvents();
            this.mainEffects(context, calculator, missed, effects);
            events.addAll(effects.events);
        }
        return missed;
    }

}
