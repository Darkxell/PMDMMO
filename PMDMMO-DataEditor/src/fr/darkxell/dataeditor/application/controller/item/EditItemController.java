package fr.darkxell.dataeditor.application.controller.item;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemCategory;

import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EditItemController implements Initializable
{

	@FXML
	public ComboBox<ItemCategory> categoryCombobox;
	@FXML
	public TextField effectTextfield;
	@FXML
	public TextField idTextfield;
	@FXML
	public TextField priceTextfield;
	@FXML
	public CheckBox rareCheckbox;
	@FXML
	public TextField sellTextfield;
	@FXML
	public TextField spriteTextfield;
	@FXML
	public CheckBox stackableCheckbox;

	private Item generate()
	{
		int id, price, sell, effectID, spriteID;

		if (this.idTextfield.getText().matches("\\d+")) id = Integer.parseInt(this.idTextfield.getText());
		else
		{
			FXUtils.showAlert("Wrong ID: " + this.idTextfield.getText());
			return null;
		}

		if (this.priceTextfield.getText().matches("\\d+")) price = Integer.parseInt(this.priceTextfield.getText());
		else
		{
			FXUtils.showAlert("Wrong Buy price: " + this.priceTextfield.getText());
			return null;
		}

		if (this.sellTextfield.getText().matches("\\d+")) sell = Integer.parseInt(this.sellTextfield.getText());
		else
		{
			FXUtils.showAlert("Wrong Sell price: " + this.sellTextfield.getText());
			return null;
		}

		if (this.effectTextfield.getText().matches("\\d+")) effectID = Integer.parseInt(this.effectTextfield.getText());
		else
		{
			FXUtils.showAlert("Wrong effect ID: " + this.effectTextfield.getText());
			return null;
		}

		if (this.spriteTextfield.getText().matches("\\d+")) spriteID = Integer.parseInt(this.spriteTextfield.getText());
		else
		{
			FXUtils.showAlert("Wrong sprite ID: " + this.spriteTextfield.getText());
			return null;
		}

		return new Item(id, this.categoryCombobox.getValue(), price, sell, effectID, spriteID, this.stackableCheckbox.isSelected(),
				this.rareCheckbox.isSelected());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.categoryCombobox.getItems().addAll(ItemCategory.values());
		this.categoryCombobox.getSelectionModel().select(0);
	}

	public void onCancelChanges()
	{
		ItemsTabController.instance.onEdit(ItemsTabController.instance.currentItem);
	}

	public void onSaveItem()
	{
		Item i = this.generate();
		if (i != null) ItemsTabController.instance.onEdited(i);
	}

	public void setupFor(Item item)
	{
		this.idTextfield.setText(String.valueOf(item.id));
		this.categoryCombobox.getSelectionModel().select(item.category);
		this.priceTextfield.setText(String.valueOf(item.price));
		this.sellTextfield.setText(String.valueOf(item.sell));
		this.effectTextfield.setText(String.valueOf(item.effectID));
		this.spriteTextfield.setText(String.valueOf(item.spriteID));
		this.stackableCheckbox.setSelected(item.isStackable);
		this.rareCheckbox.setSelected(item.isRare);
	}

}
