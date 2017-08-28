package com.darkxell.common.status;

public class StatusConditionInstance
{

	/** This Status Condition's ID. */
	public final StatusCondition condition;
	/** The number of turns this Condition has been in effect. */
	private int turns;

	public StatusConditionInstance(StatusCondition condition)
	{
		this.condition = condition;
		this.turns = 0;
	}

	public int getTurns()
	{
		return this.turns;
	}

}
