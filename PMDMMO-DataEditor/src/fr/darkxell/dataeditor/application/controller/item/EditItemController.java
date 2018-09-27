package fr.darkxell.dataeditor.application.controller.item;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.resources.images.Sprites;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemCategory;

import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

public class EditItemController implements Initializable
{

	@FXML
	public ComboBox<ItemCategory> categoryCombobox;
	@FXML
	public TextField effectTextfield;
	@FXML
	public TextField idTextfield;
	@FXML
	public ImageView imageView;
	@FXML
	public TextField priceTextfield;
	@FXML
	public CheckBox rareCheckbox;
	@FXML
	public TextField sellTextfield;
	@FXML
	public Spinner<Integer> spriteSpinner;
	@FXML
	public CheckBox stackableCheckbox;

	private Item generate()
	{
		int id, price, sell, effectID;

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

		return new Item(id, this.categoryCombobox.getValue(), price, sell, effectID, this.spriteSpinner.getValue(), this.stackableCheckbox.isSelected(),
				this.rareCheckbox.isSelected());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.categoryCombobox.getItems().addAll(ItemCategory.values());
		this.categoryCombobox.getSelectionModel().select(0);

		this.spriteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0) {
			@Override
			public void decrement(int steps)
			{
				super.decrement(steps);
				updateImage();
			}

			@Override
			public void increment(int steps)
			{
				super.increment(steps);
				updateImage();
			}
		});
		this.spriteSpinner.getValueFactory().setWrapAround(true);
		this.spriteSpinner.setEditable(true);
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
		this.spriteSpinner.getValueFactory().setValue(item.spriteID);
		this.stackableCheckbox.setSelected(item.isStackable);
		this.rareCheckbox.setSelected(item.isRare);

		this.updateImage();
	}

	private void updateImage()
	{
		this.imageView.setImage(SwingFXUtils.toFXImage(Sprites.Res_Dungeon.items.getImg(this.spriteSpinner.getValue()), new WritableImage(16, 16)));
	}

}
