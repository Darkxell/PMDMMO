package fr.darkxell.dataeditor.application.controller.cutscene;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.AnimateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.CameraCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DelayCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DespawnCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.DialogCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.MoveCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.MusicCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.RotateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SetAnimatedCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SetStateCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SoundCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.SpawnCutsceneEvent;
import com.darkxell.client.mechanics.cutscene.event.WaitCutsceneEvent;

import fr.darkxell.dataeditor.application.DataEditor;
import fr.darkxell.dataeditor.application.controller.cutscene.SelectEventTypeController.CutsceneEventType;
import fr.darkxell.dataeditor.application.controller.cutscene.event.EventController;
import fr.darkxell.dataeditor.application.controls.CustomListCell;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.data.Cutscenes;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EditCutsceneController implements Initializable, ListCellParent<CutsceneEvent>
{

	public static Stage editEventPopup;
	public static CutsceneEvent editing;
	public static EditCutsceneController instance;
	public static Stage selectEventTypePopup;

	@FXML
	public CutsceneCreationController cutsceneCreationController;
	@FXML
	private CutsceneEndController cutsceneEndController;
	@FXML
	private ListView<CutsceneEvent> eventList;

	@Override
	public Node graphicFor(CutsceneEvent item)
	{
		if (item == null) return null;
		Image fxImage = SwingFXUtils.toFXImage(FXUtils.getIcon(item.getIconPath()), null);
		ImageView imageView = new ImageView(fxImage);
		return imageView;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		instance = this;
		this.eventList.setCellFactory(param -> {
			return new CustomListCell<>(this, "Cutscene Event").setCanRename(false);
		});
	}

	@Override
	public void onCreate(CutsceneEvent event)
	{
		if (event != null)
		{
			editing = event;
			if (event instanceof AnimateCutsceneEvent) this.onCreate(event, CutsceneEventType.animate);
			if (event instanceof CameraCutsceneEvent) this.onCreate(event, CutsceneEventType.camera);
			if (event instanceof DelayCutsceneEvent) this.onCreate(event, CutsceneEventType.delay);
			if (event instanceof DespawnCutsceneEvent) this.onCreate(event, CutsceneEventType.despawn);
			if (event instanceof DialogCutsceneEvent) this.onCreate(event, CutsceneEventType.dialog);
			if (event instanceof MoveCutsceneEvent) this.onCreate(event, CutsceneEventType.move);
			if (event instanceof MusicCutsceneEvent) this.onCreate(event, CutsceneEventType.music);
			if (event instanceof RotateCutsceneEvent) this.onCreate(event, CutsceneEventType.rotate);
			if (event instanceof SetAnimatedCutsceneEvent) this.onCreate(event, CutsceneEventType.setanimated);
			if (event instanceof SetStateCutsceneEvent) this.onCreate(event, CutsceneEventType.setstate);
			if (event instanceof SoundCutsceneEvent) this.onCreate(event, CutsceneEventType.sound);
			if (event instanceof SpawnCutsceneEvent) this.onCreate(event, CutsceneEventType.spawn);
			if (event instanceof WaitCutsceneEvent) this.onCreate(event, CutsceneEventType.wait);
		} else try
		{
			editing = null;
			FXMLLoader loader = new FXMLLoader(DataEditor.class.getResource("/layouts/cutscenes/select_event_type.fxml"));
			Parent root = loader.load();
			selectEventTypePopup = FXUtils.showPopup(root, "New Event");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void onCreate(CutsceneEvent event, CutsceneEventType type)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(DataEditor.class.getResource("/layouts/cutscenes/events/" + type.name() + ".fxml"));
			Parent root = loader.load();
			if (event != null)
			{
				EventController controller = loader.getController();
				controller.setup(event);
			}
			selectEventTypePopup = FXUtils.showPopup(root, (event == null ? "New" : "Edit") + " Event");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void onCreateEvent()
	{
		this.onCreate(null);
	}

	@Override
	public void onDelete(CutsceneEvent item)
	{
		this.eventList.getItems().remove(item);
	}

	@Override
	public void onEdit(CutsceneEvent item)
	{
		this.onCreate(item);
	}

	public void onEditConfirm(CutsceneEvent event)
	{
		ObservableList<CutsceneEvent> events = this.eventList.getItems();
		if (editing == null) events.add(event);
		else
		{
			int index = events.indexOf(editing);
			events.remove(index);
			events.add(index, event);
		}
	}

	@Override
	public void onRename(CutsceneEvent item, String name)
	{}

	public void saveChanges()
	{
		Cutscene c = new Cutscene(CutscenesTabController.instance.currentCutscene.name, this.cutsceneCreationController.getCreation(),
				this.cutsceneEndController.getEnd(), new ArrayList<>(this.eventList.getItems()));
		CutscenesTabController.instance.currentCutscene = c;
		Cutscenes.update(c);
	}

	public void setupFor(Cutscene cutscene)
	{
		this.cutsceneCreationController.setupFor(cutscene);
		this.cutsceneEndController.setupFor(cutscene);
		this.eventList.getItems().addAll(cutscene.events);
	}

}
