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
import fr.darkxell.dataeditor.application.controls.CutsceneEntityTreeCell;
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
	public static CutsceneCreationController instance;

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
		instance = this;

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

		this.entitiesList.setCellFactory(param -> {
			return new CutsceneEntityTreeCell();
		});
	}

	public void onCreateEntity()
	{
		this.onEdit(null);
	}

	public void onDelete(CutsceneEntity item)
	{
		this.entitiesList.getItems().remove(item);
	}

	public void onEdit(CutsceneEntity entity)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(DataEditor.class.getResource("/layouts/cutscenes/edit_entity.fxml"));
			Parent root = loader.load();
			if (entity != null)
			{
				EditEntityController controller = loader.getController();
				controller.setupFor(entity);
			}
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
