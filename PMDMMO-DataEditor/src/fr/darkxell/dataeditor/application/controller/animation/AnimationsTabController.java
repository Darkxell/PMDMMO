package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.ResourceBundle;

import fr.darkxell.dataeditor.application.controls.CustomListCell;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class AnimationsTabController implements Initializable, ListCellParent<String>
{

	public static AnimationsTabController instance;

	@FXML
	public ListView<String> animationsListView;
	@FXML
	public EditAnimationController editAnimationController;
	@FXML
	private TitledPane editAnimationPane;

	@Override
	public Node graphicFor(String item)
	{
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		instance = this;
		this.animationsListView.setCellFactory(param -> {
			return new CustomListCell<>(AnimationsTabController.instance, "Animation").setCanOrder(false).setCanCreate(false).setCanDelete(false)
					.setCanRename(false);
		});
	}

	@Override
	public void onCreate(String nullItem)
	{}

	@Override
	public void onDelete(String item)
	{}

	@Override
	public void onEdit(String item)
	{}

	@Override
	public void onMove(String item, int newIndex)
	{}

	@Override
	public void onRename(String item, String name)
	{}

}
