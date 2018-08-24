package fr.darkxell.dataeditor.application.util;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.status.StatusCondition;

public class AnimationListItem extends CustomTreeItem implements Comparable<AnimationListItem>
{

	public static AnimationListItem create(String anim)
	{
		String[] data = anim.split("/");
		return new AnimationListItem(data[0], Integer.parseInt(data[1]));
	}

	public final String folder;
	public final int id;

	public AnimationListItem(String folder, int id)
	{
		super();
		this.folder = folder;
		this.id = id;
	}

	@Override
	public int compareTo(AnimationListItem o)
	{
		if (this.folder.equals(o.folder)) return Integer.compare(this.id, o.id);
		return this.folder.compareTo(o.folder);
	}

	private String nameDetails()
	{
		String detail = null;
		switch (this.folder)
		{
			case "abilities":
				Ability a = Ability.find(this.id);
				if (a != null) detail = a.name().toString();
				break;

			case "items":
				Item i = ItemRegistry.find(this.id);
				if (i != null) detail = i.name().toString();
				break;

			case "moves":
				Move m = MoveRegistry.find(this.id);
				if (m != null) detail = m.name().toString();
				break;

			case "projectiles":
				detail = Integer.toString(this.id);
				if (this.id >= 1000)
				{
					Move mp = MoveRegistry.find(this.id - 1000);
					if (mp != null) detail = mp.name().toString() + " projectile";
				}
				break;

			case "statuses":
				StatusCondition s = StatusCondition.find(this.id);
				if (s != null) detail = s.name().toString();
				break;

			case "targets":
				Move m2 = MoveRegistry.find(this.id);
				if (m2 != null) detail = m2.name().toString() + " target";
				break;

			default:
				break;
		}
		if (detail == null) return "";
		detail = detail.replaceAll("<.*?>", "");
		while (detail.startsWith(" "))
			detail = detail.substring(1);
		return detail;
	}

	@Override
	public String toString()
	{
		return this.id + "- " + this.nameDetails();
	}

}
