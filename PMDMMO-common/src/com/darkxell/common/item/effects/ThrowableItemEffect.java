package com.darkxell.common.item.effects;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ProjectileThrownEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemCategory;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

public class ThrowableItemEffect extends ItemEffect {
    public static enum ThrowableTrajectory {
        Arc,
        Straight
    }

    /** The damage this throwable Item deals. */
    public final int damage;
    /** The type of the trajectory. */
    public final ThrowableTrajectory trajectory;

    public ThrowableItemEffect(int id, int damage, ThrowableTrajectory trajectory) {
        super(id);
        this.damage = damage;
        this.trajectory = trajectory;
    }

    private ArrayList<Tile> arcReachableTiles(Floor floor, Item item, DungeonPokemon pokemon) {
        final int maxDist = 8;
        Direction d = pokemon.facing(), left = d.rotateCounterClockwise(), right = d.rotateClockwise();
        ArrayList<Tile> toreturn = new ArrayList<>();
        Stack<Tile> toprocess = new Stack<>();
        toprocess.add(pokemon.tile().adjacentTile(d));
        toprocess.add(pokemon.tile().adjacentTile(left));
        toprocess.add(pokemon.tile().adjacentTile(right));
        toreturn.addAll(toprocess);

        while (!toprocess.isEmpty()) {
            Tile current = toprocess.pop();
            Tile test = current.adjacentTile(d);
            if (test != null && !toreturn.contains(test) && pokemon.tile().distance(test) <= maxDist) {
                toreturn.add(test);
                toprocess.add(test);
            }
            test = current.adjacentTile(left);
            if (test != null && !toreturn.contains(test) && pokemon.tile().distance(test) <= maxDist) {
                toreturn.add(test);
                toprocess.add(test);
            }
            test = current.adjacentTile(right);
            if (test != null && !toreturn.contains(test) && pokemon.tile().distance(test) <= maxDist) {
                toreturn.add(test);
                toprocess.add(test);
            }
        }

        return toreturn;
    }

    public ItemCategory category() {
        return ItemCategory.THROWABLE;
    }

    public Tile findDestination(Floor floor, Item item, DungeonPokemon pokemon) {
        if (this.trajectory == ThrowableTrajectory.Straight)
            return this.findDestinationStraight(floor, item, pokemon, false);
        return this.findDestinationArc(floor, item, pokemon);
    }

    private Tile findDestinationArc(Floor floor, Item item, DungeonPokemon pokemon) {
        ArrayList<Tile> arcReachable = arcReachableTiles(floor, item, pokemon);
        ArrayList<DungeonPokemon> candidates = new ArrayList<>();
        for (Tile t : arcReachable)
            if (t.getPokemon() != null && !pokemon.isAlliedWith(t.getPokemon()))
                candidates.add(t.getPokemon());
        candidates.sort((o1, o2) -> {
            double d1 = pokemon.tile().distance(o1.tile()), d2 = pokemon.tile().distance(o2.tile());
            return Double.compare(d1, d2);
        });
        return candidates.size() == 0 ? pokemon.tile().adjacentTile(pokemon.facing()) : candidates.get(0).tile();
    }

    @Override
    protected String getUseEffectID() {
        return "item.thrown";
    }

    @Override
    public Message getUseEffectMessage(ItemSelectionEvent event) {
        return super.getUseEffectMessage(event);
    }

    @Override
    public boolean isThrowable() {
        return false;
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public boolean isUsableOnCatch() {
        return false;
    }

    @Override
    public void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target,
            ArrayList<DungeonEvent> events) {
        super.use(floor, item, pokemon, target, events);

        Tile destination = this.findDestination(floor, item, pokemon);
        events.add(new ProjectileThrownEvent(floor, item, pokemon, destination));
    }

}
