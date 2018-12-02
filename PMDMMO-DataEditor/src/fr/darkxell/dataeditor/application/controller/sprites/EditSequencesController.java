package fr.darkxell.dataeditor.application.controller.sprites;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.pokemon.PokemonSpriteFrame;
import com.darkxell.client.resources.images.pokemon.PokemonSpriteSequence;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesetData;

import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class EditSequencesController implements Initializable, ListCellParent<PokemonSpriteFrame>
{

	@FXML
	public ListView<PokemonSpriteFrame> framesList;
	@FXML
	public TextField hitTextfield;
	public SpritesTabController parent;
	@FXML
	public TextField returnTextfield;
	@FXML
	public TextField rushTextfield;
	@FXML
	public ComboBox<PokemonSpriteSequence> sequenceCombobox;
	@FXML
	public GridPane sequenceProperties;

	@Override
	public Node graphicFor(PokemonSpriteFrame item)
	{
		if (this.parent == null || item == null) return null;
		RegularSpriteSet spriteset = this.parent.generalDataController.spriteset;
		if (spriteset == null) return null;
		if (spriteset.get(item.frameID) == null) return null;
		return new ImageView(SwingFXUtils.toFXImage(spriteset.get(item.frameID).image(), null));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.sequenceProperties.setDisable(true);

		this.sequenceCombobox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.onSequenceChanged(oldValue, newValue));

		CustomList.setup(this, this.framesList, "Frame", true, false, true, true, true);
	}

	@Override
	public void onCreate(PokemonSpriteFrame nullItem)
	{
		PokemonSpriteFrame frame = new PokemonSpriteFrame();
		this.framesList.getItems().add(frame);
		this.onEdit(frame);
	}

	@Override
	public void onDelete(PokemonSpriteFrame item)
	{
		this.framesList.getItems().remove(item);
	}

	@Override
	public void onEdit(PokemonSpriteFrame item)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMove(PokemonSpriteFrame item, int newIndex)
	{}

	@Override
	public void onRename(PokemonSpriteFrame item, String name)
	{}

	private void onSequenceChanged(PokemonSpriteSequence oldValue, PokemonSpriteSequence newValue)
	{
		if (oldValue != null) this.saveSequence(oldValue);
		this.framesList.getItems().clear();
		if (newValue != null)
		{
			this.rushTextfield.setText(String.valueOf(newValue.rushPoint));
			this.hitTextfield.setText(String.valueOf(newValue.hitPoint));
			this.returnTextfield.setText(String.valueOf(newValue.returnPoint));
			this.framesList.getItems().addAll(newValue.frames());
		}
	}

	@Override
	public double prefWidth(PokemonSpriteFrame item)
	{
		if (this.parent == null || item == null) return 30;
		RegularSpriteSet spriteset = this.parent.generalDataController.spriteset;
		if (spriteset == null) return 30;
		if (spriteset.get(item.frameID) == null) return 30;
		return spriteset.get(item.frameID).image().getHeight();
	}

	private void saveSequence(PokemonSpriteSequence old)
	{
		// TODO Auto-generated method stub

	}

	public void setupFor(PokemonSpritesetData item)
	{
		this.sequenceProperties.setDisable(item == null);

		this.sequenceCombobox.getItems().clear();
		this.sequenceCombobox.getItems().addAll(item.sequences());
	}

}
