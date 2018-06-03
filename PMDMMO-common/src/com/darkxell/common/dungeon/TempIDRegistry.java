package com.darkxell.common.dungeon;

import java.util.HashMap;

import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.dungeon.TempIDRegistry.HasID;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;

public abstract class TempIDRegistry<T extends HasID>
{
	public static interface HasID
	{
		public long id();

		public void setId(long id);
	}

	public static class ItemsTempIDRegistry extends TempIDRegistry<ItemStack>
	{
		public long register(ItemStack item, Pokemon pokemon)
		{
			long id = this.register(item);
			if (pokemon != null) pokemon.getData().holdeditem = new DatabaseIdentifier(id);

			return id;
		}
	}

	public static class MovesTempIDRegistry extends TempIDRegistry<LearnedMove>
	{
		public long register(LearnedMove move, Pokemon pokemon)
		{
			long id = this.register(move);
			if (pokemon != null)
			{
				for (int i = 0; i < pokemon.moveCount(); ++i)
					if (pokemon.move(i) == move)
					{
						pokemon.getData().learnedmoves.set(i, new DatabaseIdentifier(id));
						break;
					}
			}

			return id;
		}
	}

	public static class PokemonTempIDRegistry extends TempIDRegistry<Pokemon>
	{
		public long register(Pokemon pokemon, ItemsTempIDRegistry items, TempIDRegistry<LearnedMove> moves)
		{
			long id = this.register(pokemon);
			if (pokemon.getItem() != null) items.register(pokemon.getItem(), pokemon);
			for (int i = 0; i < pokemon.moveCount(); ++i)
				moves.register(pokemon.move(i));

			return id;
		}

		/** Unregisters the input Pokemon and the item and moves it has. */
		public void unregister(Pokemon pokemon, ItemsTempIDRegistry items, TempIDRegistry<LearnedMove> moves)
		{
			this.unregister(pokemon.getData().id);
			if (pokemon.getItem() != null) items.unregister(pokemon.getItem().getData().id);
			for (int i = 0; i < pokemon.moveCount(); ++i)
				moves.unregister(pokemon.move(i).getData().id);
		}
	}

	private HashMap<Long, T> registry = new HashMap<>();

	public void clear()
	{
		this.registry.clear();
	}

	public T get(long id)
	{
		return this.registry.get(id);
	}

	private long newID()
	{
		long next = -2;
		while (registry.containsKey(next))
			--next;
		return next;
	}

	protected long register(long id, T object)
	{
		if (id != 0 && this.registry.containsKey(id)) return id;
		if (id == 0) id = this.newID();
		this.registry.put(id, object);
		object.setId(id);
		return id;
	}

	protected long register(T object)
	{
		if (this.registry.containsValue(object)) return object.id();
		return this.register(object.id(), object);
	}

	protected void unregister(long id)
	{
		this.registry.remove(id);
	}
}
