package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

/** An Item that teaches a move to a Pokémon when used, then turns into a Used TM. */
public class ItemTM extends ItemHM
{

	public ItemTM(Element xml)
	{
		super(xml);
	}

	public ItemTM(int id, int price, int sell, int sprite, boolean isStackable, int moveID)
	{
		super(id, price, sell, sprite, isStackable, moveID);
	}

	public ItemCategory category()
	{
		return ItemCategory.TMS;
	}

	@Override
	public ArrayList<ItemAction> getLegalActions(boolean inDungeon)
	{
		ArrayList<ItemAction> actions = super.getLegalActions(inDungeon);
		if (!actions.contains(ItemAction.USE)) actions.add(ItemAction.USE);
		return actions;
	}

	@Override
	public int getSpriteID()
	{
		return super.getSpriteID() + MoveRegistry.find(this.moveID).type.id;
	}

	@Override
	public Message name()
	{
		return new Message("move." + this.moveID).addPrefix("<tm" + this.move().type.id + ">");
	}

	@Override
	public DungeonEvent[] use(Floor floor, DungeonPokemon pokemon, DungeonPokemon target)
	{
		if (pokemon.player != null)
		{
			if (pokemon.player.inventory.isFull()) pokemon.tile.setItem(new ItemStack(Item.USED_TM));
			else pokemon.player.inventory.add(new ItemStack(Item.USED_TM));
		}
		return super.use(floor, pokemon, target);
	}
}
