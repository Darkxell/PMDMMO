package com.darkxell.common.testutils.move;

import static com.darkxell.common.testutils.TestUtils.getFloor;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Direction;

public class MoveTestBuilder {

    private Direction direction;
    private boolean missed;
    private Move move = Registries.moves().find(0);
    private LearnedMove moveToUse;
    private DungeonPokemon user, target;

    public MoveTestBuilder(DungeonPokemon user, DungeonPokemon target) {
        this.user = user;
        this.target = target;
    }

    public ArrayList<Event> build() {
        if (this.moveToUse == null)
            this.buildMoveToUse();
        MoveUse moveUse = new MoveUse(getFloor(), this.moveToUse == null ? this.user.move(0) : this.moveToUse,
                this.user, this.direction == null ? this.user.facing() : this.direction, null);
        MoveUseEvent event = new MoveUseEvent(getFloor(), null, moveUse, this.target);
        ArrayList<Event> events = new ArrayList<>();
        this.missed = this.move.useOn(event, events);
        return events;
    }

    private void buildMoveToUse() {
        this.moveToUse = new LearnedMove(this.move.id);
        this.user.setMove(0, this.moveToUse);
    }

    public boolean missed() {
        return this.missed;
    }

    public LearnedMove moveToUse() {
        return this.moveToUse;
    }

    public MoveTestBuilder withDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    public MoveTestBuilder withLearnedMove(LearnedMove moveToUse) {
        this.moveToUse = moveToUse;
        return this;
    }

    public MoveTestBuilder withMove(Move move) {
        this.move = move;
        Registries.moves().register(move);
        return this;
    }

}
