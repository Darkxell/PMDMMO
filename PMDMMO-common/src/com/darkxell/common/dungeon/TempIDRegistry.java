package com.darkxell.common.dungeon;

import java.util.HashMap;

public class TempIDRegistry<T>
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
