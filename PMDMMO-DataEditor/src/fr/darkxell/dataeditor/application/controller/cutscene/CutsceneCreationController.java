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
import com.darkxell.common.zones.FreezoneInfo;

import fr.darkxell.dataeditor.application.DataEditor;
import fr.darkxell.dataeditor.application.controls.CustomListCell;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class CutsceneCreationController implements Initializable, ListCellParent<CutsceneEntity>
{

	public static Stage editEntityPopup;

	@FXML
	private TextField cameraXTextfield;
	@FXML
	private TextField cameraYTextfield;
	private int entityEditing;
	@FXML
	public ListView<CutsceneEntity> entitiesList;
	@FXML
	private ComboBox<String> fadingCombobox;
	@FXML
	private ComboBox<FreezoneInfo> freezoneCombobox;

	public CutsceneCreation getCreation()
	{
		return new CutsceneCreation(this.freezoneCombobox.getValue(), this.fadingCombobox.getSelectionModel().getSelectedIndex() == 0,
				Double.parseDouble(this.cameraXTextfield.getText()), Double.parseDouble(this.cameraYTextfield.getText()),
				new ArrayList<>(this.entitiesList.getItems()));
	}

	@Override
	public Node graphicFor(CutsceneEntity item)
	{
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.fadingCombobox.getItems().addAll("No fading", "Fading in");
		this.fadingCombobox.getSelectionModel().select(0);

		this.freezoneCombobox.getItems().addAll(FreezoneInfo.values());
		this.freezoneCombobox.getSelectionModel().select(0);

		Pattern pattern = Pattern.compile("-?\\d*(\\.\\d*)?");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.cameraXTextfield.setTextFormatter(formatter);
		formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.cameraYTextfield.setTextFormatter(formatter);

		this.entitiesList.setCellFactory(param -> {
			return new CustomListCell<>(this, "Cutscene Entity").setCanOrder(false);
		});
	}

	@Override
	public void onCreate(CutsceneEntity entity)
	{
		this.onEdit(null);
	}

	public void onCreateEntity()
	{
		this.onCreate(null);
	}

	@Override
	public void onDelete(CutsceneEntity item)
	{
		this.entitiesList.getItems().remove(item);
	}

	@Override
	public void onEdit(CutsceneEntity entity)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(DataEditor.class.getResource("/layouts/cutscenes/edit_entity.fxml"));
			Parent root = loader.load();
			EditEntityController controller = loader.getController();
			if (entity != null)
			{
				controller.setupFor(entity);
				this.entityEditing = this.entitiesList.getItems().indexOf(entity);
			} else this.entityEditing = -1;
			editEntityPopup = FXUtils.showPopup(root, (entity == null ? "New" : "Edit") + " Cutscene Entity");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void onEntityEdited(CutsceneEntity entity)
	{
		if (this.entityEditing == -1) this.entitiesList.getItems().add(entity);
		else this.entitiesList.getItems().set(this.entityEditing, entity);
	}

	@Override
	public void onMove(CutsceneEntity item, int newIndex)
	{}

	@Override
	public void onRename(CutsceneEntity item, String name)
	{}

	public void setupFor(Cutscene cutscene)
	{
		CutsceneCreation creation = cutscene.creation;
		this.freezoneCombobox.setValue(creation.freezone);
		this.cameraXTextfield.setText(String.valueOf(creation.camerax));
		this.cameraYTextfield.setText(String.valueOf(creation.cameray));
		this.fadingCombobox.getSelectionModel().select(creation.fading ? 1 : 0);
		this.entitiesList.getItems().clear();
		this.entitiesList.getItems().addAll(creation.entities());
	}

}
