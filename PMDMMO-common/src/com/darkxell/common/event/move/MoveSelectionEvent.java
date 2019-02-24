package com.darkxell.common.event.move;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.WeatherSource;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class MoveSelectionEvent extends DungeonEvent implements Communicable {

    public static class MoveUse implements DamageSource, WeatherSource {
        public final Direction direction;
        /** The experience event resulting from this move's use. */
        private final ExperienceGeneratedEvent experienceEvent;
        public final LearnedMove move;
        public final DungeonPokemon user;

        public MoveUse(Floor floor, LearnedMove move, DungeonPokemon user, Direction direction, DungeonEventSource eventSource) {
            this.move = move;
            this.user = user;
            this.direction = direction;
            this.experienceEvent = this.user.type == DungeonPokemonType.TEAM_MEMBER
                    ? new ExperienceGeneratedEvent(floor, eventSource, this.user.player())
                    : null;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof MoveUse)) return false;
            MoveUse o = (MoveUse) obj;
            return this.direction == o.direction && this.move == o.move && this.user.id() == o.user.id();
        }

        @Override
        public ExperienceGeneratedEvent getExperienceEvent() {
            return this.experienceEvent;
        }

        @Override
        public boolean isOver() {
            return true;
        }

        /** @return True if the Move's type is the same as one of the user's types. */
        public boolean isStab() {
            return this.user.usedPokemon.species().type1 == this.move.move().type || this.user.usedPokemon.species().type2 == this.move.move().type;
        }
    }

    private boolean consumesPP = true;
    private MoveUse usedMove;

    public MoveSelectionEvent(Floor floor, DungeonEventSource eventSource) {
        super(floor, eventSource);
    }

    public MoveSelectionEvent(Floor floor, DungeonEventSource eventSource, LearnedMove move, DungeonPokemon user) {
        this(floor, eventSource, move, user, user.facing(), true);
    }

    public MoveSelectionEvent(Floor floor, DungeonEventSource eventSource, LearnedMove move, DungeonPokemon user, Direction direction) {
        this(floor, eventSource, move, user, direction, true);
    }

    public MoveSelectionEvent(Floor floor, DungeonEventSource eventSource, LearnedMove move, DungeonPokemon user, Direction direction,
            boolean consumesTurn) {
        super(floor, eventSource, consumesTurn ? user : null);
        this.usedMove = new MoveUse(floor, move, user, direction, this);

        if (this.usedMove.move.move().hasUseMessage()) this.messages.add(
                new Message("move.used").addReplacement("<pokemon>", user.getNickname()).addReplacement("<move>", this.usedMove.move.move().name()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MoveSelectionEvent)) return false;
        MoveSelectionEvent o = (MoveSelectionEvent) obj;
        if (!this.usedMove.equals(o.usedMove)) return false;
        return true;
    }

    @Override
    public String loggerMessage() {
        return this.usedMove.user + " used " + this.usedMove.move.move().name();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        // Rotate
        if (this.usedMove.direction != this.usedMove.user.facing()) this.usedMove.user.setFacing(this.usedMove.direction);

        // Use PP
        if (this.consumesPP) this.usedMove.move.setPP(this.usedMove.move.pp() - 1);

        // Use Move
        this.usedMove.move.move().prepareUse(this, this.resultingEvents);

        this.resultingEvents.add(this.usedMove.getExperienceEvent());

        // Use belly
        if (this.usedMove.user.isTeamLeader()) this.resultingEvents.add(new BellyChangedEvent(this.floor, this, this.usedMove.user,
                -(this.usedMove.move.isLinked() ? .9 : .1) * this.usedMove.user.energyMultiplier()));

        if (this.usedMove.move.isLinked()) this.resultingEvents
                .add(new MoveSelectionEvent(this.floor, this, this.usedMove.user.move(this.usedMove.move.getData().slot + 1), this.usedMove.user));

        return super.processServer();
    }

    @Override
    public void read(JsonObject value) throws JsonReadingException {
        if (value.get("pokemon") == null) throw new JsonReadingException("No value for Pokemon ID!");
        if (value.get("move") == null) throw new JsonReadingException("No value for move ID!");

        if (!value.get("pokemon").isNumber()) throw new JsonReadingException("Wrong value for Pokemon ID: " + value.get("pokemon"));
        if (!value.get("move").isNumber()) throw new JsonReadingException("Wrong value for move ID: " + value.get("move"));

        Pokemon pokemon = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("pokemon", 0));
        LearnedMove move = this.floor.dungeon.communication.moveIDs.get(value.getLong("move", 0));
        Direction d = null;
        if (pokemon == null) throw new JsonReadingException("No pokemon with ID " + value.getLong("pokemon", 0));
        if (move == null) throw new JsonReadingException("No move with ID " + value.getLong("move", 0));
        try {
            d = Direction.valueOf(value.getString("direction", Direction.NORTH.name()));
        } catch (IllegalArgumentException e) {
            throw new JsonReadingException("No direction with name " + value.getString("direction", "null"));
        }
        this.actor = pokemon.getDungeonPokemon();
        this.usedMove = new MoveUse(this.floor, move, pokemon.getDungeonPokemon(), d, this);

        if (this.usedMove.move.move() != MoveRegistry.ATTACK) this.messages.add(new Message("move.used")
                .addReplacement("<pokemon>", this.usedMove.user.getNickname()).addReplacement("<move>", this.usedMove.move.move().name()));

    }

    public void setConsumesNoPP() {
        this.consumesPP = false;
    }

    @Override
    public JsonObject toJson() {
        return Json.object().add("pokemon", this.usedMove.user.id()).add("move", this.usedMove.move.id()).add("direction",
                this.usedMove.direction.name());
    }

    public MoveUse usedMove() {
        return this.usedMove;
    }

}
