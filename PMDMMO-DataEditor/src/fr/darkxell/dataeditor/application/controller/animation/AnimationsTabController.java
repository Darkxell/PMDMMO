package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.animation.Animations;

import fr.darkxell.dataeditor.application.controls.CustomListCell;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.util.AnimationListItem;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

public class AnimationsTabController implements Initializable, ListCellParent<AnimationListItem>
{

	public static AnimationsTabController instance;

	@FXML
	public ListView<AnimationListItem> animationsListView;
	@FXML
	public EditAnimationController editAnimationController;
	@FXML
	private TitledPane editAnimationPane;

	@Override
	public Node graphicFor(AnimationListItem item)
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
		this.animationsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click)
			{
				if (click.getClickCount() == 2) onEdit(animationsListView.getSelectionModel().getSelectedItem());
			}
		});

		this.reloadList();
	}

	@Override
	public void onCreate(AnimationListItem nullItem)
	{}

	@Override
	public void onDelete(AnimationListItem item)
	{}

	@Override
	public void onEdit(AnimationListItem item)
	{
		AnimationListItem selected = this.animationsListView.getSelectionModel().getSelectedItem();
		this.editAnimationController.setAnimation(selected);

		this.editAnimationPane.setText("Animation: " + selected);
	}

	@Override
	public void onMove(AnimationListItem item, int newIndex)
	{}

	@Override
	public void onRename(AnimationListItem item, String name)
	{}

	public void reloadList()
	{
		animationsListView.getItems().clear();

		String[] anims = Animations.listAnimations();
		for (String anim : anims)
			animationsListView.getItems().add(AnimationListItem.create(anim));
		animationsListView.getItems().sort(Comparator.naturalOrder());
	}

}
