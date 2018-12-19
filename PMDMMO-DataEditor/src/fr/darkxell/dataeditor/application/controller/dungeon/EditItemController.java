package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.darkxell.common.Registries;
import com.darkxell.common.dungeon.data.DungeonItemGroup;
import com.darkxell.common.item.Item;

import fr.darkxell.dataeditor.application.data.DungeonItemTableItem;
import fr.darkxell.dataeditor.application.data.SingleItemTableItem;
import fr.darkxell.dataeditor.application.util.DungeonCreationException;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class EditItemController implements Initializable
{

	public DungeonItemTableItem editing;

	@FXML
	public EditFloorsetController floorsetController;
	@FXML
	public TableColumn<SingleItemTableItem, Item> itemColumn;
	@FXML
	public TableView<SingleItemTableItem> itemsTable;
	@FXML
	public MenuItem removeMenuItem;
	@FXML
	public TableColumn<SingleItemTableItem, String> weightColumn;
	@FXML
	public TextField weightTextfield;

	private DungeonItemGroup generate() throws DungeonCreationException
	{
		ObservableList<SingleItemTableItem> list = this.itemsTable.getItems();
		int[] ids = new int[list.size()], chances = new int[list.size()];
		for (int i = 0; i < ids.length; ++i)
		{
			ids[i] = list.get(i).item.id;
			chances[i] = list.get(i).weight;
		}

		int weight;
		try
		{
			weight = Integer.parseInt(this.weightTextfield.getText());
		} catch (NumberFormatException e)
		{
			throw new DungeonCreationException("Invalid Item Group weight: " + this.weightTextfield.getText());
		}

		return new DungeonItemGroup(this.floorsetController.generate(), weight, ids, chances);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
		this.weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

		ArrayList<Item> items = Registries.items().toList();
		Item[] t = new Item[items.size()];
		for (int i = 0; i < t.length; ++i)
			t[i] = items.get(i);
		this.itemColumn.setCellFactory(ComboBoxTableCell.<SingleItemTableItem, Item> forTableColumn(t));
		this.weightColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	public void onAdd()
	{
		this.itemsTable.getItems().add(new SingleItemTableItem(Registries.items().find(Item.POKEDOLLARS), 1));
	}

	public void onItemEdited(CellEditEvent<SingleItemTableItem, Item> cell)
	{
		cell.getRowValue().item = cell.getNewValue();
	}

	public void onMenuShown()
	{
		this.removeMenuItem.setDisable(this.itemsTable.getSelectionModel().getSelectedItem() == null);
	}

	public void onRemove()
	{
		if (this.itemsTable.getSelectionModel().getSelectedItem() != null)
			this.itemsTable.getItems().remove(this.itemsTable.getSelectionModel().getSelectedItem());
	}

	public void onSave()
	{
		try
		{
			this.editing.itemGroup = this.generate();
			EditDungeonItemsController.instance.onItemEdited(this.editing);
		} catch (DungeonCreationException e)
		{
			FXUtils.showAlert("There was an error while saving the Dungeon: " + e.getMessage() + ".");
		}
	}

	public void onWeightEdited(CellEditEvent<SingleItemTableItem, String> cell)
	{
		String input = cell.getNewValue();
		if (input.matches("\\d+")) cell.getRowValue().weight = Integer.parseInt(input);
		else
		{
			FXUtils.showAlert("Wrong Weight value: " + input + ". Must be an integer.");
			this.itemsTable.refresh();
		}
	}

	public void setupFor(DungeonItemTableItem item)
	{
		this.editing = item;
		this.itemsTable.getItems().clear();
		if (item != null)
		{
			this.weightTextfield.setText(String.valueOf(item.getWeight()));
			for (int i = 0; i < item.itemGroup.items.length; ++i)
				this.itemsTable.getItems().add(new SingleItemTableItem(Registries.items().find(item.itemGroup.items[i]),
						item.itemGroup.chances[i]));
			this.floorsetController.setupFor(item.getFloors());
		}
	}

}
