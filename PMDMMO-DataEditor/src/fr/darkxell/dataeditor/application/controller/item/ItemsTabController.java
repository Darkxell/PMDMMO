package fr.darkxell.dataeditor.application.controller.item;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemCategory;
import com.darkxell.common.item.ItemRegistry;

import fr.darkxell.dataeditor.application.controller.dungeon.EditDungeonController;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.data.ItemListItem;
import fr.darkxell.dataeditor.application.util.CustomTreeItem;
import fr.darkxell.dataeditor.application.util.TreeCategory;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("unused")
public class ItemsTabController implements Initializable, ListCellParent<ItemListItem>
{

	public static ItemsTabController instance;

	private TreeItem<CustomTreeItem>[] categories;
	/** Currently edited Item. */
	public Item currentItem;
	@FXML
	public EditDungeonController editItemController;
	@FXML
	private TitledPane editItemPane;
	private TreeItem<CustomTreeItem> equipable, throwable, food, berries, drinks, gummis, seeds, other_usables, orbs, tms, hms, evolutionary, others,
			newcategory;
	@FXML
	private TreeView<CustomTreeItem> itemsTreeView;

	Item defaultItem(int id)
	{
		return new Item(id, ItemCategory.OTHERS, 0, 0, 0, 0, false, false);
	}

	@Override
	public Node graphicFor(ItemListItem item)
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		instance = this;
		TreeItem<CustomTreeItem> root = new TreeItem<CustomTreeItem>(new TreeCategory("Animations"));
		root.setExpanded(true);
		this.itemsTreeView.setRoot(root);
		List<TreeItem<CustomTreeItem>> categories = this.itemsTreeView.getRoot().getChildren();
		categories.add(this.equipable = new TreeItem<CustomTreeItem>(new TreeCategory("Equipable")));
		categories.add(this.throwable = new TreeItem<CustomTreeItem>(new TreeCategory("Throwable")));
		categories.add(this.food = new TreeItem<CustomTreeItem>(new TreeCategory("Move Food")));
		categories.add(this.berries = new TreeItem<CustomTreeItem>(new TreeCategory("Berries")));
		categories.add(this.drinks = new TreeItem<CustomTreeItem>(new TreeCategory("Drinks")));
		categories.add(this.gummis = new TreeItem<CustomTreeItem>(new TreeCategory("Gummis")));
		categories.add(this.seeds = new TreeItem<CustomTreeItem>(new TreeCategory("Seeds")));
		categories.add(this.other_usables = new TreeItem<CustomTreeItem>(new TreeCategory("Other Usables")));
		categories.add(this.orbs = new TreeItem<CustomTreeItem>(new TreeCategory("Orbs")));
		categories.add(this.tms = new TreeItem<CustomTreeItem>(new TreeCategory("TMs")));
		categories.add(this.hms = new TreeItem<CustomTreeItem>(new TreeCategory("HMs")));
		categories.add(this.evolutionary = new TreeItem<CustomTreeItem>(new TreeCategory("Evolutionary")));
		categories.add(this.others = new TreeItem<CustomTreeItem>(new TreeCategory("Others")));
		this.categories = categories.toArray(new TreeItem[categories.size()]);

		/* this.itemsList.setCellFactory(param -> { return new CustomListCell<>(AnimationsTabController.instance, "Animation").setCanOrder(false).setCanCreate(false).setCanDelete(false) .setCanRename(false); }); */
		this.itemsTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click)
			{
				if (click.getClickCount() == 2)
				{
					TreeItem<CustomTreeItem> item = itemsTreeView.getSelectionModel().getSelectedItem();
					if (item.getValue() instanceof TreeCategory) item.setExpanded(!item.isExpanded());
					else onEdit((ItemListItem) item.getValue());
				}
			}
		});

		this.reloadList();/* CustomList.setup(this, this.itemsList, "Item", true, false, true, true, false); this.itemsList.getItems().addAll(ItemRegistry.list()); this.itemsList.getItems().sort(Comparator.naturalOrder()); */

		this.editItemPane.setVisible(false);
	}

	@Override
	public void onCreate(ItemListItem nullItem)
	{
		this.onCreateItem();
	}

	public void onCreateItem()
	{
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("New Item");
		dialog.setHeaderText(null);
		dialog.setContentText("Type in the ID of the new Item.");
		Optional<String> name = dialog.showAndWait();
		if (name.isPresent())
		{
			if (name.get().matches("\\d+"))
			{
				Item i = this.defaultItem(Integer.parseInt(name.get()));
				if (ItemRegistry.find(i.id) != null) new Alert(AlertType.ERROR, "There is already an Item with ID " + i.id, ButtonType.OK).showAndWait();
				else
				{
					ItemRegistry.register(i);
					this.reloadList();
				}
			} else new Alert(AlertType.ERROR, "Wrong ID: " + name.get(), ButtonType.OK);
		}
	}

	@Override
	public void onDelete(ItemListItem item)
	{
		if (item.item == this.currentItem)
		{
			this.currentItem = null;
			this.editItemPane.setVisible(false);
		}
		ItemRegistry.unregister(item.item.id);
		this.reloadList();
	}

	public void onDeleteItem()
	{
		TreeItem<CustomTreeItem> selected = this.itemsTreeView.getSelectionModel().getSelectedItem();
		if (selected == null) return;
		if (selected.getValue() instanceof ItemListItem) this.onDelete((ItemListItem) selected.getValue());
	}

	@Override
	public void onEdit(ItemListItem item)
	{
		this.currentItem = item.item;
		this.editItemPane.setVisible(true);
		this.editItemPane.setText(this.currentItem.name().toString());
		// this.editItemController.setupFor(this.currentItem);
	}

	public void onEdited(Item item)
	{
		boolean idChanged = this.currentItem.id != item.id;
		if (idChanged && DungeonRegistry.find(item.id) != null)
			new Alert(AlertType.ERROR, "Cannot save: There is already another Dungeon with ID " + item.id, ButtonType.OK).showAndWait();
		else
		{
			ItemRegistry.unregister(this.currentItem.id);
			ItemRegistry.register(item);
			this.reloadList();
			/* this.itemsList.getItems().remove(this.currentItem); this.itemsList.getItems().add(item); this.itemsList.getItems().sort(Comparator.naturalOrder()); this.itemsList.getSelectionModel().select(item); this.onEdit(item); */
		}
	}

	@Override
	public void onMove(ItemListItem item, int newIndex)
	{}

	@Override
	public void onRename(ItemListItem item, String name)
	{}

	public void onSaveAllItems()
	{
		ItemRegistry.save(new File("../PMDMMO-common/resources/data/items.xml"));
	}

	private void reloadList()
	{
		for (TreeItem<CustomTreeItem> item : this.categories)
			item.getChildren().clear();
		for (Item i : ItemRegistry.list())
			this.categories[i.category.order].getChildren().add(new TreeItem<>(new ItemListItem(i)));
	}

}
