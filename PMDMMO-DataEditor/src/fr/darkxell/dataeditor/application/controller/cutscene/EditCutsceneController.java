package fr.darkxell.dataeditor.application.controller.cutscene;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;

import fr.darkxell.dataeditor.application.controls.CustomListCell;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.data.Cutscenes;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EditCutsceneController implements Initializable, ListCellParent<CutsceneEvent>
{

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
		this.eventList.setCellFactory(param -> {
			return new CustomListCell<>(this, "Cutscene Event");
		});
	}

	@Override
	public void onCreate(CutsceneEvent nullItem)
	{
		// TODO Auto-generated method stub

	}

	public void onCreateEvent()
	{
		this.onCreate(null);
	}

	@Override
	public void onDelete(CutsceneEvent item)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit(CutsceneEvent item)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onRename(CutsceneEvent item, String name)
	{
		// TODO Auto-generated method stub

	}

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
