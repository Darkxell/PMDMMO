package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.EnterDungeonCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.LoadFreezoneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.PlayCutsceneCutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.ResumeExplorationCutsceneEnd;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.zones.FreezoneInfo;

import fr.darkxell.dataeditor.application.controller.cutscene.CutsceneEndController.CutsceneEndMode;
import fr.darkxell.dataeditor.application.data.Cutscenes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class CutsceneEndController implements Initializable, ChangeListener<CutsceneEndMode>
{

	public static enum CutsceneEndMode
	{
		CUTSCENE("Play Cutscene"),
		DUNGEON("Enter Dungeon"),
		EXPLORE("Resume Dungeon Exploration"),
		FREEZONE("Load Freezone");

		public final String name;

		private CutsceneEndMode(String name)
		{
			this.name = name;
		}

		@Override
		public String toString()
		{
			return this.name;
		}
	}

	@FXML
	private ComboBox<Cutscene> cutsceneCombobox;
	@FXML
	private ComboBox<Dungeon> dungeonCombobox;
	@FXML
	private ComboBox<FreezoneInfo> freezoneCombobox;
	@FXML
	private TextField freezoneXTextfield;
	@FXML
	private TextField freezoneYTextfield;
	@FXML
	private ComboBox<CutsceneEndMode> modeCombobox;

	@Override
	public void changed(ObservableValue<? extends CutsceneEndMode> observable, CutsceneEndMode oldValue, CutsceneEndMode newValue)
	{
		this.cutsceneCombobox.setVisible(newValue == CutsceneEndMode.CUTSCENE);
		this.dungeonCombobox.setVisible(newValue == CutsceneEndMode.DUNGEON);
		this.freezoneCombobox.setVisible(newValue == CutsceneEndMode.FREEZONE);
		this.freezoneXTextfield.setVisible(newValue == CutsceneEndMode.FREEZONE);
		this.freezoneYTextfield.setVisible(newValue == CutsceneEndMode.FREEZONE);
	}

	public CutsceneEnd getEnd()
	{
		switch (this.modeCombobox.getSelectionModel().getSelectedItem())
		{
			case CUTSCENE:
				return new PlayCutsceneCutsceneEnd(this.cutsceneCombobox.getSelectionModel().getSelectedItem().name);

			case DUNGEON:
				return new EnterDungeonCutsceneEnd(this.dungeonCombobox.getSelectionModel().getSelectedItem().id);

			case FREEZONE:
				return new LoadFreezoneCutsceneEnd(this.freezoneCombobox.getValue(), Integer.parseInt(this.freezoneXTextfield.getText()),
						Integer.parseInt(this.freezoneYTextfield.getText()));

			case EXPLORE:
				return new ResumeExplorationCutsceneEnd();
		}
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		Pattern pattern = Pattern.compile("-?\\d*");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.freezoneXTextfield.setTextFormatter(formatter);
		formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.freezoneYTextfield.setTextFormatter(formatter);

		this.cutsceneCombobox.getItems().addAll(Cutscenes.values());
		this.dungeonCombobox.getItems().addAll(DungeonRegistry.list());
		this.modeCombobox.getItems().addAll(CutsceneEndMode.values());
		this.freezoneCombobox.getItems().addAll(FreezoneInfo.values());

		this.modeCombobox.getSelectionModel().selectedItemProperty().addListener(this);
		this.modeCombobox.getSelectionModel().select(0);
		this.dungeonCombobox.getSelectionModel().select(0);
		this.cutsceneCombobox.getSelectionModel().select(0);
		this.freezoneCombobox.getSelectionModel().select(0);
	}

	public void setupFor(Cutscene cutscene)
	{
		CutsceneEnd end = cutscene.onFinish;
		if (end instanceof PlayCutsceneCutsceneEnd)
		{
			this.cutsceneCombobox.getSelectionModel().select(Cutscenes.get(((PlayCutsceneCutsceneEnd) end).cutsceneID));
			this.modeCombobox.getSelectionModel().select(CutsceneEndMode.CUTSCENE);
		}
		if (end instanceof EnterDungeonCutsceneEnd)
		{
			this.dungeonCombobox.getSelectionModel().select(DungeonRegistry.find(((EnterDungeonCutsceneEnd) end).dungeonID));
			this.modeCombobox.getSelectionModel().select(CutsceneEndMode.DUNGEON);
		}
		if (end instanceof LoadFreezoneCutsceneEnd)
		{
			LoadFreezoneCutsceneEnd e = (LoadFreezoneCutsceneEnd) end;
			this.freezoneCombobox.setValue(e.freezone);
			this.freezoneXTextfield.setText(String.valueOf(e.xPos));
			this.freezoneYTextfield.setText(String.valueOf(e.yPos));
			this.modeCombobox.getSelectionModel().select(CutsceneEndMode.FREEZONE);
		}
		if (end instanceof ResumeExplorationCutsceneEnd) this.modeCombobox.getSelectionModel().select(CutsceneEndMode.EXPLORE);
	}

}
