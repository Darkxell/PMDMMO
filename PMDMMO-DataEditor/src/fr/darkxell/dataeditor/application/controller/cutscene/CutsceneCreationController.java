package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneCreation;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class CutsceneCreationController implements Initializable
{

	@FXML
	private TextField cameraXTextfield;
	@FXML
	private TextField cameraYTextfield;
	@FXML
	private ListView<CutsceneEntity> entitiesList;
	@FXML
	private ComboBox<String> fadingCombobox;
	@FXML
	private TextField freezoneTextfield;

	public CutsceneCreation getCreation()
	{
		return new CutsceneCreation(this.freezoneTextfield.getText(), this.fadingCombobox.getSelectionModel().getSelectedIndex() == 0,
				Integer.parseInt(this.cameraXTextfield.getText()), Integer.parseInt(this.cameraYTextfield.getText()),
				new ArrayList<>(this.entitiesList.getItems()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.fadingCombobox.getItems().addAll("No fading", "Fading in");
		this.fadingCombobox.getSelectionModel().select(0);

		Pattern pattern = Pattern.compile("-?\\d*");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.cameraXTextfield.setTextFormatter(formatter);
		formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.cameraYTextfield.setTextFormatter(formatter);
	}

	public void onCreateEntity()
	{

	}

	public void setupFor(Cutscene cutscene)
	{
		CutsceneCreation creation = cutscene.creation;
		this.freezoneTextfield.setText(creation.freezoneID);
		this.cameraXTextfield.setText(String.valueOf(creation.camerax));
		this.cameraYTextfield.setText(String.valueOf(creation.cameray));
		this.fadingCombobox.getSelectionModel().select(creation.fading ? 1 : 0);
		this.entitiesList.getItems().addAll(creation.entities());
	}

}
