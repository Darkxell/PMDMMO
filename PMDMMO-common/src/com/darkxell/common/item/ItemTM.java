package com.darkxell.common.item;

public class ItemTM extends Item
{

	/** The move this TM teaches. */
	public final int moveID;
	/** The TM ID. */
	public final int tmID;

	public ItemTM(int id, String name, int moveID, int tmID)
	{
		super(id, name);
		this.moveID = moveID;
		this.tmID = tmID;
	}

}
