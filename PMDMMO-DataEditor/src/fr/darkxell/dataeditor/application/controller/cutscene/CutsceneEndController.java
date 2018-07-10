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
import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.dungeon.DungeonRegistry;

import fr.darkxell.dataeditor.application.data.Cutscenes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class CutsceneEndController implements Initializable
{

	public static enum CutsceneEndMode
	{
		CUTSCENE("Play Cutscene"),
		DUNGEON("Enter Dungeon"),
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
	private TextField freezoneTextfield;
	@FXML
	private TextField freezoneXTextfield;
	@FXML
	private TextField freezoneYTextfield;
	@FXML
	private ComboBox<CutsceneEndMode> modeCombobox;

	public CutsceneEnd getEnd()
	{
		switch (this.modeCombobox.getSelectionModel().getSelectedItem())
		{
			case CUTSCENE:
				return new PlayCutsceneCutsceneEnd(this.cutsceneCombobox.getSelectionModel().getSelectedItem().name);

			case DUNGEON:
				return new EnterDungeonCutsceneEnd(this.dungeonCombobox.getSelectionModel().getSelectedItem().id);

			case FREEZONE:
				return new LoadFreezoneCutsceneEnd(this.freezoneTextfield.getText(), Integer.parseInt(this.freezoneXTextfield.getText()),
						Integer.parseInt(this.freezoneYTextfield.getText()));
		}
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		Pattern pattern = Pattern.compile("\\d*");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.freezoneXTextfield.setTextFormatter(formatter);
		this.freezoneYTextfield.setTextFormatter(formatter);

		this.cutsceneCombobox.getItems().addAll(Cutscenes.values());
		this.dungeonCombobox.getItems().addAll(DungeonRegistry.list());
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
			this.freezoneTextfield.setText(e.freezoneID);
			this.freezoneXTextfield.setText(String.valueOf(e.xPos));
			this.freezoneYTextfield.setText(String.valueOf(e.yPos));
			this.modeCombobox.getSelectionModel().select(CutsceneEndMode.FREEZONE);
		}

		this.cutsceneCombobox.setVisible(end instanceof PlayCutsceneCutsceneEnd);
		this.dungeonCombobox.setVisible(end instanceof EnterDungeonCutsceneEnd);
		this.freezoneTextfield.setVisible(end instanceof LoadFreezoneCutsceneEnd);
		this.freezoneXTextfield.setVisible(end instanceof LoadFreezoneCutsceneEnd);
		this.freezoneYTextfield.setVisible(end instanceof LoadFreezoneCutsceneEnd);
	}

}
