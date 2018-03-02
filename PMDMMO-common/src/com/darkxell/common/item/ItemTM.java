package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

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
		if (this.move() == null) return 64;
		return 64 + this.move().type.id;
	}

	@Override
	public Message name()
	{
		if (this.move() == null) return new Message("move." + this.moveID).addPrefix("<tm" + 0 + ">");
		return new Message("move." + this.moveID).addPrefix("<tm" + this.move().type.id + ">");
	}

	@Override
	public ArrayList<DungeonEvent> use(Floor floor, DungeonPokemon pokemon, DungeonPokemon target)
	{
		if (pokemon.player() != null)
		{
			if (pokemon.player().inventory.isFull()) pokemon.tile().setItem(new ItemStack(Item.USED_TM));
			else pokemon.player().inventory.addItem(new ItemStack(Item.USED_TM));
		}
		return super.use(floor, pokemon, target);
	}
}
