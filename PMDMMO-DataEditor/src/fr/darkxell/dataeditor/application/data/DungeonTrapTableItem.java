package fr.darkxell.dataeditor.application.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.darkxell.common.dungeon.data.DungeonTrapGroup;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.trap.Trap;

public class DungeonTrapTableItem implements Comparable<DungeonTrapTableItem>
{

	public DungeonTrapGroup trapGroup;

	public DungeonTrapTableItem(DungeonTrapGroup trapGroup)
	{
		this.trapGroup = trapGroup;
	}

	@Override
	public int compareTo(DungeonTrapTableItem o)
	{
		return this.getFloors().compareTo(o.getFloors());
	}

	public FloorSet getFloors()
	{
		return this.trapGroup.floors;
	}

	public List<Trap> getTraps()
	{
		return Arrays.asList(this.trapGroup.traps());
	}

	public List<Integer> getWeights()
	{
		ArrayList<Integer> weights = new ArrayList<>();
		for (int w : this.trapGroup.chances)
			weights.add(w);
		return weights;
	}

}
