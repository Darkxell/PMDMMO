package com.darkxell.common.item.effects;

import java.util.ArrayList;
import java.util.Stack;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.item.ProjectileThrownEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemCategory;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

public class ThrowableItemEffect extends ItemEffect {
    public enum ThrowableTrajectory {
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
        Stack<Tile> toprocess = new Stack<>();
        toprocess.add(pokemon.tile().adjacentTile(d));
        toprocess.add(pokemon.tile().adjacentTile(left));
        toprocess.add(pokemon.tile().adjacentTile(right));
        ArrayList<Tile> toreturn = new ArrayList<>(toprocess);

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

    public Tile findDestination(ItemUseEvent itemEvent) {
        if (this.trajectory == ThrowableTrajectory.Straight)
            return this.findDestinationStraight(itemEvent.floor, itemEvent.user, itemEvent.item, false);
        return this.findDestinationArc(itemEvent);
    }

    private Tile findDestinationArc(ItemUseEvent itemEvent) {
        ArrayList<Tile> arcReachable = arcReachableTiles(itemEvent.floor, itemEvent.item, itemEvent.user);
        ArrayList<DungeonPokemon> candidates = new ArrayList<>();
        for (Tile t : arcReachable)
            if (t.getPokemon() != null && !itemEvent.user.isAlliedWith(t.getPokemon()))
                candidates.add(t.getPokemon());
        candidates.sort((o1, o2) -> {
            double d1 = itemEvent.user.tile().distance(o1.tile()), d2 = itemEvent.user.tile().distance(o2.tile());
            return Double.compare(d1, d2);
        });
        return candidates.size() == 0 ? itemEvent.user.tile().adjacentTile(itemEvent.user.facing())
                : candidates.get(0).tile();
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
    public void use(ItemUseEvent itemEvent, ArrayList<DungeonEvent> events) {
        super.use(itemEvent, events);

        Tile destination = this.findDestination(itemEvent);
        events.add(new ProjectileThrownEvent(itemEvent.floor, itemEvent, itemEvent.item, itemEvent.user, destination));
    }

}
