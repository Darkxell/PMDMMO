package fr.darkxell.dataeditor.application.util;

public class AnimationListItem implements Comparable<AnimationListItem>
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

	@Override
	public String toString()
	{
		return this.folder.substring(0, 1).toUpperCase() + this.folder.substring(1) + ": " + this.id;
	}

}
