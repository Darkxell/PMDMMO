package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.OptionDialogCutsceneEvent;

import fr.darkxell.dataeditor.application.data.DialogOptionTableItem;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class OptionEventController extends EventController
{

	@FXML
	public EditDialogController editDialogController;
	@FXML
	public TableColumn<DialogOptionTableItem, String> optionColumn;
	@FXML
	public TableView<DialogOptionTableItem> optionsTable;
	@FXML
	public TableColumn<DialogOptionTableItem, String> translateColumn;

	@Override
	public CutsceneEvent generateEvent()
	{
		return new OptionDialogCutsceneEvent(this.id(), null);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		super.initialize(location, resources);

		this.editDialogController.okButton.setDisable(true);
		this.editDialogController.okButton.setVisible(false);
		this.editDialogController.cancelButton.setDisable(true);
		this.editDialogController.cancelButton.setVisible(false);

		this.optionColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		this.translateColumn.setCellValueFactory(new PropertyValueFactory<>("translate"));

		this.optionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.translateColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		this.optionColumn.setOnEditCommit(event -> this.onOptionEdited(event));
		this.translateColumn.setOnEditCommit(event -> this.onTranslateEdited(event));

		ContextMenu menu = new ContextMenu();
		final MenuItem add, remove, up, down;
		menu.getItems().add(add = new MenuItem("Add"));
		menu.getItems().add(remove = new MenuItem("Delete"));
		menu.getItems().add(up = new MenuItem("Up"));
		menu.getItems().add(down = new MenuItem("Down"));

		add.setOnAction(e -> {
			this.optionsTable.getItems().add(new DialogOptionTableItem("", true));
		});

		remove.setOnAction(e -> {
			this.optionsTable.getItems().remove(this.optionsTable.getSelectionModel().getSelectedIndex());
		});

		up.setOnAction(e -> {
			int i = this.optionsTable.getSelectionModel().getSelectedIndex();
			DialogOptionTableItem item = this.optionsTable.getItems().remove(i);
			this.optionsTable.getItems().add(i - 1, item);
		});

		down.setOnAction(e -> {
			int i = this.optionsTable.getSelectionModel().getSelectedIndex();
			DialogOptionTableItem item = this.optionsTable.getItems().remove(i);
			if (i == this.optionsTable.getItems().size() - 2) this.optionsTable.getItems().add(item);
			else this.optionsTable.getItems().add(i + 1, item);
		});

		menu.setOnShowing(e -> {
			remove.setDisable(this.optionsTable.getSelectionModel().isEmpty());
			up.setDisable(this.optionsTable.getSelectionModel().isEmpty() || this.optionsTable.getSelectionModel().getSelectedIndex() == 0);
			down.setDisable(this.optionsTable.getSelectionModel().isEmpty()
					|| this.optionsTable.getSelectionModel().getSelectedIndex() == this.optionsTable.getItems().size() - 1);
		});

		this.optionsTable.setContextMenu(menu);
	}

	private void onOptionEdited(CellEditEvent<DialogOptionTableItem, String> event)
	{
		if (event.getNewValue().equals("")) FXUtils.showAlert("Bruh please put it something");
		else event.getRowValue().message = event.getNewValue();
		this.optionsTable.refresh();
	}

	private void onTranslateEdited(CellEditEvent<DialogOptionTableItem, String> event)
	{
		boolean isTrue = false, isFalse = false;
		String newValue = event.getNewValue();
		boolean value = false;

		if (newValue.equals("0")) isFalse = true;
		if (newValue.toLowerCase().startsWith("f")) isFalse = true;
		if (newValue.equals("1")) isTrue = true;
		if (newValue.toLowerCase().startsWith("t")) isTrue = true;

		if (isTrue) value = true;
		else if (isFalse) value = false;
		else FXUtils.showAlert("Invalid boolean: " + newValue);

		if (isTrue || isFalse) event.getRowValue().translate = value;

		this.optionsTable.refresh();
	}

	@Override
	public void setup(CutsceneEvent event)
	{
		super.setup(event);
		this.optionsTable.getItems().clear();
	}

}
