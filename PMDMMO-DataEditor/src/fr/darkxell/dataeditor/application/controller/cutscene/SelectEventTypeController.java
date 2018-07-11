package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.ResourceBundle;

import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SelectEventTypeController implements Initializable
{

	public static enum CutsceneEventType
	{
		// Don't forget to also modify EditCutsceneController#onCreate().
		animate("Play animation"),
		camera("Move camera"),
		delay("Wait X ticks"),
		despawn("Despawn Entity"),
		dialog("Show Dialog"),
		move("Move Entity"),
		music("Change soundtrack"),
		rotate("Rotate Entity"),
		setanimated("Animate Pokémon"),
		setstate("Set Pokémon State"),
		sound("Play sound"),
		spawn("Spawn Entity"),
		wait("Wait for events to finish");

		public final String description;

		private CutsceneEventType(String name)
		{
			this.description = name;
		}
	}

	@FXML
	public ListView<CutsceneEventType> eventList;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.eventList.getItems().addAll(CutsceneEventType.values());
		this.eventList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			this.onSelect(newValue);
		});
		this.eventList.setCellFactory(param -> {
			return new ListCell<CutsceneEventType>() {
				@Override
				protected void updateItem(CutsceneEventType item, boolean empty)
				{
					super.updateItem(item, empty);
					this.setText(item == null ? null : item.description);
					if (item == null) return;
					Image fxImage = SwingFXUtils.toFXImage(FXUtils.getIcon("/icons/events/" + item.name() + ".png"), null);
					this.setGraphic(new ImageView(fxImage));
				}
			};
		});
	}

	public void onCancel()
	{
		EditCutsceneController.selectEventTypePopup.close();
	}

	private void onSelect(CutsceneEventType type)
	{
		EditCutsceneController.selectEventTypePopup.close();
		EditCutsceneController.instance.onCreate(null, type);
	}

}
