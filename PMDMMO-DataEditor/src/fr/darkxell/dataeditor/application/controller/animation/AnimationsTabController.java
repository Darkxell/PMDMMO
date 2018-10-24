package fr.darkxell.dataeditor.application.controller.animation;

import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.client.mechanics.animation.AnimationData;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.Animations.AnimationGroup;
import com.darkxell.common.util.Pair;

import fr.darkxell.dataeditor.application.util.AnimationListItem;
import fr.darkxell.dataeditor.application.util.CustomTreeItem;
import fr.darkxell.dataeditor.application.util.TreeCategory;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

public class AnimationsTabController implements Initializable
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
	public EditAnimationController editAnimationController;
	@FXML
	private TitledPane editAnimationPane;
	AnimationListItem editing;
	@FXML
	public TestAnimationController testAnimationController;

	private TreeItem<CustomTreeItem> category(AnimationGroup group)
	{
		switch (group)
		{
			case Abilities:
				return abilities;

			case Custom:
				return custom;

			case Items:
				return items;

			case Moves:
				return moves;

			case Projectiles:
				return projectiles;

			case Statuses:
				return statuses;

			case MoveTargets:
				return targets;

			default:
				break;
		}
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

		this.editAnimationPane.setVisible(false);

		this.reloadList();
	}

	public void onCreate()
	{
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("New Animation");
		dialog.setHeaderText(null);
		dialog.setContentText("Type in the ID of the new Animation (can be preceded by items/, moves/, etc. for its catagory).");
		Optional<String> name = dialog.showAndWait();
		if (name.isPresent())
		{
			Pair<Integer, AnimationGroup> id = Animations.splitID(name.get());
			if (id != null)
			{
				if (Animations.existsAnimation(id))
					new Alert(AlertType.ERROR, "There is already an Animation with ID " + name.get(), ButtonType.OK).showAndWait();
				else
				{
					Animations.register(new AnimationData(id.first), id.second);
					this.reloadList();
				}
			} else new Alert(AlertType.ERROR, "Wrong ID: " + name.get(), ButtonType.OK).showAndWait();
		}
	}

	public void onDelete()
	{
		TreeItem<CustomTreeItem> selected = this.animationsTreeView.getSelectionModel().getSelectedItem();
		if (selected == null) return;
		if (selected.getValue() instanceof AnimationListItem)
		{
			AnimationListItem anim = (AnimationListItem) selected.getValue();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Delete Animation");
			alert.setContentText("Are you sure you want to delete animation '" + anim.group + "/" + anim.id + "'?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK)
			{
				if (anim == this.editing)
				{
					this.editing = null;
					this.editAnimationPane.setVisible(false);
				}
				Animations.unregister(anim.id, anim.group);
				this.reloadList();
			}
		}
	}

	public void onEdit(AnimationListItem item)
	{
		this.editing = item;
		this.testAnimationController.setAnimation(item);
		this.editAnimationController.setupFor(item);
		this.editAnimationPane.setText("Animation: " + item);
		this.editAnimationPane.setVisible(true);
	}

	public void onSaveAll()
	{
		Animations.save(new File("../PMDMMOc/resources/data/animations.xml"));
	}

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
			if (item != null) this.category(item.group).getChildren().add(new TreeItem<CustomTreeItem>(item));
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
