package fr.darkxell.dataeditor.application.data;

import com.darkxell.common.item.Item;

import fr.darkxell.dataeditor.application.util.CustomTreeItem;

public class ItemListItem extends CustomTreeItem
{

	public final Item item;

	public ItemListItem(Item item)
	{
		this.item = item;
	}

	@Override
	public String toString()
	{
		return this.item.name().toString().replaceAll("<.*?>", "");
	}

}
