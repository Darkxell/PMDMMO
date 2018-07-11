package fr.darkxell.dataeditor.application.controller.cutscene;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneCreation;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;

import fr.darkxell.dataeditor.application.DataEditor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CutsceneCreationController implements Initializable
{
	
	public static Stage editEntityPopup;

	@FXML
	private TextField cameraXTextfield;
	@FXML
	private TextField cameraYTextfield;
	@FXML
	public ListView<CutsceneEntity> entitiesList;
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
		try
		{
			Parent root = FXMLLoader.load(DataEditor.class.getResource("/layouts/cutscenes/edit_entity.fxml"));
			editEntityPopup = new Stage();
			editEntityPopup.setScene(new Scene(root));
			editEntityPopup.setTitle("Create Entity");
			editEntityPopup.getIcons().addAll(DataEditor.primaryStage.getIcons());
			editEntityPopup.setWidth(300);
			editEntityPopup.setHeight(400);
			editEntityPopup.initModality(Modality.APPLICATION_MODAL);
			editEntityPopup.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
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
