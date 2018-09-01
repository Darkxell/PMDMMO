package com.darkxell.common.status.conditions;

import com.darkxell.common.status.StatusCondition;

public class PreventsOtherStatusCondition extends StatusCondition
{

	private final StatusCondition[] prevented;

	public PreventsOtherStatusCondition(int id, int durationMin, int durationMax, StatusCondition... prevented)
	{
		super(id, durationMin, durationMax);
		this.prevented = prevented;
	}

	public boolean prevents(StatusCondition condition)
	{
		for (StatusCondition s : this.prevented)
			if (s == condition) return true;
		return false;
	}

}
