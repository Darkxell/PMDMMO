package fr.darkxell.dataeditor.application.controller.sprites;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.pokemon.PokemonSpriteFrame;
import com.darkxell.client.resources.images.pokemon.PokemonSpriteSequence;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesetData;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;

import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class EditSpriteController implements Initializable, ListCellParent<PokemonSpriteFrame>
{

	@FXML
	public CheckBox bigShadowCheckbox;
	private PokemonSpritesetData d;
	@FXML
	public TableColumn directionColumn;
	@FXML
	public ListView<PokemonSpriteFrame> framesList;
	@FXML
	public TextField heightTextfield;
	@FXML
	public TextField hitTextfield;
	@FXML
	public TextField returnTextfield;
	@FXML
	public TextField rushTextfield;
	@FXML
	public TableColumn sequenceColumn;
	@FXML
	public ComboBox<PokemonSpriteSequence> sequenceCombobox;
	@FXML
	public GridPane sequenceProperties;
	private RegularSpriteSet spriteset;
	private int spritesetid;
	@FXML
	public TableColumn stateColumn;
	@FXML
	public TableView table;
	@FXML
	public TextField widthTextfield;

	@Override
	public Node graphicFor(PokemonSpriteFrame item)
	{
		if (this.spriteset == null || item == null) return null;
		if (this.spriteset.get(item.frameID) == null) return null;
		return new ImageView(SwingFXUtils.toFXImage(this.spriteset.get(item.frameID).image(), null));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.sequenceProperties.setDisable(true);

		this.sequenceCombobox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.onSequenceChanged(oldValue, newValue));

		Pattern p = Pattern.compile("\\d*");
		this.heightTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return p.matcher(change.getControlNewText()).matches() ? change : null;
		}));
		this.widthTextfield.setTextFormatter(new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return p.matcher(change.getControlNewText()).matches() ? change : null;
		}));

		this.heightTextfield.textProperty().addListener((observable, oldValue, newValue) -> this.onDimensionsChanged());
		this.widthTextfield.textProperty().addListener((observable, oldValue, newValue) -> this.onDimensionsChanged());

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

	private void onDimensionsChanged()
	{
		int width = 16, height = 16;
		if (!this.widthTextfield.getText().equals("")) width = Integer.parseInt(this.widthTextfield.getText());
		if (!this.heightTextfield.getText().equals("")) height = Integer.parseInt(this.heightTextfield.getText());
		this.spriteset = PokemonSpritesets.loadTestSpriteset(this.spritesetid, width, height);
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
		if (this.spriteset == null || item == null) return 30;
		if (this.spriteset.get(item.frameID) == null) return 30;
		return this.spriteset.get(item.frameID).image().getHeight();
	}

	private void saveSequence(PokemonSpriteSequence old)
	{
		// TODO Auto-generated method stub

	}

	public void setupFor(PokemonSpritesetData item)
	{
		this.d = item;
		this.sequenceProperties.setDisable(item == null);

		this.spritesetid = item.id;
		this.widthTextfield.setText("");
		this.heightTextfield.setText("");
		this.widthTextfield.setText(String.valueOf(item.spriteWidth));
		this.heightTextfield.setText(String.valueOf(item.spriteHeight));
		this.bigShadowCheckbox.setSelected(item.hasBigShadow);

		this.sequenceCombobox.getItems().clear();
		this.sequenceCombobox.getItems().addAll(item.sequences());

		this.onDimensionsChanged();
	}

}
