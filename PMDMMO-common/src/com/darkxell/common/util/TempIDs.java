package com.darkxell.common.util;

import java.util.HashMap;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;

public class TempIDs
{

	@SuppressWarnings("unused")
	private static class TempIDRegistry<T>
	{
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

		private void register(long id, T object)
		{
			this.registry.put(id, object);
		}

		public void register(T object)
		{
			this.register(this.newID(), object);
		}

		public void unregister(long id)
		{
			this.registry.remove(id);
		}
	}

	public static final TempIDRegistry<ItemStack> items = new TempIDRegistry<>();
	public static final TempIDRegistry<LearnedMove> moves = new TempIDRegistry<>();
	public static final TempIDRegistry<Pokemon> pokemon = new TempIDRegistry<>();

	public static void clearAll()
	{
		items.clear();
		moves.clear();
		pokemon.clear();
	}

	/** Unregisters the input Pokemon and the item and moves it has. */
	public static void unregisterDeep(Pokemon p)
	{
		pokemon.unregister(p.getData().id);
		if (p.getItem() != null) items.unregister(p.getItem().getData().id);
		for (int i = 0; i < p.moveCount(); ++i)
			moves.unregister(p.move(i).getData().id);
	}

}
