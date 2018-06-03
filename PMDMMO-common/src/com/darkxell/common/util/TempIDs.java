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
		private HashMap<Integer, T> registry = new HashMap<>();

		public void clear()
		{
			this.registry.clear();
		}

		public T get(int id)
		{
			return this.registry.get(id);
		}

		private int newID()
		{
			int next = -1;
			while (registry.containsKey(next))
				--next;
			return next;
		}

		private void register(int id, T object)
		{
			this.registry.put(id, object);
		}

		public void register(T object)
		{
			this.register(this.newID(), object);
		}

		public void unregister(int id)
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

}
