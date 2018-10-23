package fr.darkxell.dataeditor.application.controller.animation;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.animation.Animations;

import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.util.AnimationListItem;
import fr.darkxell.dataeditor.application.util.CustomTreeItem;
import fr.darkxell.dataeditor.application.util.TreeCategory;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

public class AnimationsTabController implements Initializable, ListCellParent<AnimationListItem>
{

	public static AnimationsTabController instance;
	public static Comparator<TreeItem<CustomTreeItem>> sorter = new Comparator<TreeItem<CustomTreeItem>>() {
		@Override
		public int compare(TreeItem<CustomTreeItem> o1, TreeItem<CustomTreeItem> o2)
		{
			if (o1.getValue() instanceof AnimationListItem && o2.getValue() instanceof AnimationListItem)
				return ((AnimationListItem) o1.getValue()).compareTo((AnimationListItem) o2.getValue());
			return o1.getValue().toString().compareTo(o2.getValue().toString());
		}
	};

	private TreeItem<CustomTreeItem> abilities, custom, items, moves, projectiles, statuses, targets;
	@FXML
	public TreeView<CustomTreeItem> animationsTreeView;
	@FXML
	public TestAnimationController testAnimationController;
	@FXML
	public TestAnimationController testAnimation2Controller;

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
		TreeItem<CustomTreeItem> root = new TreeItem<CustomTreeItem>(new TreeCategory("Animations"));
		root.setExpanded(true);
		this.animationsTreeView.setRoot(root);
		List<TreeItem<CustomTreeItem>> categories = this.animationsTreeView.getRoot().getChildren();
		categories.add(this.abilities = new TreeItem<CustomTreeItem>(new TreeCategory("Abilities")));
		categories.add(this.moves = new TreeItem<CustomTreeItem>(new TreeCategory("Moves")));
		categories.add(this.targets = new TreeItem<CustomTreeItem>(new TreeCategory("Move Targets")));
		categories.add(this.projectiles = new TreeItem<CustomTreeItem>(new TreeCategory("Projectiles")));
		categories.add(this.items = new TreeItem<CustomTreeItem>(new TreeCategory("Items")));
		categories.add(this.statuses = new TreeItem<CustomTreeItem>(new TreeCategory("Status Conditions")));
		categories.add(this.custom = new TreeItem<CustomTreeItem>(new TreeCategory("Other")));

		/* this.animationsTreeView.setCellFactory(param -> { return new CustomListCell<>(AnimationsTabController.instance, "Animation").setCanOrder(false).setCanCreate(false).setCanDelete(false) .setCanRename(false); }); */
		this.animationsTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click)
			{
				if (click.getClickCount() == 2)
				{
					TreeItem<CustomTreeItem> item = animationsTreeView.getSelectionModel().getSelectedItem();
					if (item.getValue() instanceof TreeCategory) item.setExpanded(!item.isExpanded());
					else onEdit((AnimationListItem) item.getValue());
				}
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
		AnimationListItem selected = item;
		this.testAnimationController.setAnimation(selected);

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
		this.abilities.getChildren().clear();
		this.custom.getChildren().clear();
		this.items.getChildren().clear();
		this.moves.getChildren().clear();
		this.projectiles.getChildren().clear();
		this.statuses.getChildren().clear();
		this.targets.getChildren().clear();

		String[] anims = Animations.listAnimations();
		for (String anim : anims)
		{
			AnimationListItem item = AnimationListItem.create(anim);
			switch (item.folder)
			{
				case "abilities":
					this.abilities.getChildren().add(new TreeItem<CustomTreeItem>(item));
					break;

				case "custom":
					this.custom.getChildren().add(new TreeItem<CustomTreeItem>(item));
					break;

				case "items":
					this.items.getChildren().add(new TreeItem<CustomTreeItem>(item));
					break;

				case "moves":
					this.moves.getChildren().add(new TreeItem<CustomTreeItem>(item));
					break;

				case "projectiles":
					this.projectiles.getChildren().add(new TreeItem<CustomTreeItem>(item));
					break;

				case "statuses":
					this.statuses.getChildren().add(new TreeItem<CustomTreeItem>(item));
					break;

				case "targets":
					this.targets.getChildren().add(new TreeItem<CustomTreeItem>(item));
					break;

				default:
					break;
			}
		}

		this.abilities.getChildren().sort(sorter);
		this.custom.getChildren().sort(sorter);
		this.moves.getChildren().sort(sorter);
		this.items.getChildren().sort(sorter);
		this.statuses.getChildren().sort(sorter);
		this.projectiles.getChildren().sort(sorter);
		this.targets.getChildren().sort(sorter);
	}

}
