package com.darkxell.common.status;

public abstract class Status
{

	/** This Status condition's duration. -1 for indefinite. */
	public final int durationMin, durationMax;
	/** This Status Condition's ID. */
	public final int id;

	public Status(int id, int durationMin, int durationMax)
	{
		this.id = id;
		this.durationMin = durationMin;
		this.durationMax = durationMax;
	}

}
