package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.util.Pair;

import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

public class EditFloorsetController implements Initializable, ListCellParent<Integer>
{

	@FXML
	public ListView<Integer> exceptList;
	@FXML
	public VBox partsBox;
	public ArrayList<Pair<Integer, Integer>> partsList = new ArrayList<>();

	public FloorSet generate()
	{
		ArrayList<Integer> except = new ArrayList<>(this.exceptList.getItems());
		except.sort(Comparator.naturalOrder());
		HashMap<Integer, Integer> parts = new HashMap<>();
		for (Pair<Integer, Integer> part : this.partsList)
			parts.put(part.first, part.second);
		return new FloorSet(parts, except);
	}

	@Override
	public Node graphicFor(Integer item)
	{
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		CustomList.setup(this, this.exceptList, "Excepted Floor", true, false, true, true, false);
	}

	@Override
	public void onCreate(Integer nullItem)
	{
		this.onEdit(null);
	}

	@Override
	public void onDelete(Integer item)
	{
		this.exceptList.getItems().remove(item);
	}

	@Override
	public void onEdit(Integer item)
	{
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Excepted Floor");
		dialog.setHeaderText(null);
		dialog.setContentText("Type in the excepted floor.");
		Optional<String> name = dialog.showAndWait();
		if (name.isPresent())
		{
			if (name.get().matches("\\d+"))
			{
				int floor = Integer.parseInt(name.get());
				if (this.exceptList.getItems().contains(floor))
					new Alert(AlertType.ERROR, "Floor " + floor + " is already excepted.", ButtonType.OK).showAndWait();
				else
				{
					if (item != null) this.exceptList.getItems().remove(item);
					this.exceptList.getItems().add(floor);
				}
			} else new Alert(AlertType.ERROR, "Wrong number: " + name.get(), ButtonType.OK);
		}
	}

	@Override
	public void onMove(Integer item, int newIndex)
	{}

	public void onNewPart()
	{
		this.partsList.add(new Pair<>(1, EditDungeonDataController.instance.currentFloorCount()));
		this.reloadParts();
	}

	public void onPartChanged(Pair<Integer, Integer> part, Pair<Integer, Integer> newPart)
	{
		int index = this.partsList.indexOf(part);
		if (index == -1) return;
		this.partsList.remove(index);
		this.partsList.add(newPart);
		this.reloadParts();
	}

	public void onRemovePart(Pair<Integer, Integer> part)
	{
		if (this.partsList.contains(part))
		{
			this.partsList.remove(this.partsList.indexOf(part));// Not sure if remove(Object) removes all instances
			this.reloadParts();
		}
	}

	@Override
	public void onRename(Integer item, String name)
	{}

	private void reloadParts()
	{
		this.partsList.sort((p1, p2) -> {
			int first = Integer.compare(p1.first, p2.first);
			if (first == 0) return Integer.compare(p1.second, p2.second);
			return first;
		});
		this.partsBox.getChildren().clear();

		for (Pair<Integer, Integer> part : this.partsList)
			this.partsBox.getChildren().add(new FloorsPart(this, part));
	}

	public void setupFor(FloorSet floors)
	{
		this.exceptList.getItems().clear();
		this.partsList.clear();

		if (floors != null)
		{
			this.exceptList.getItems().addAll(floors.except());
			HashMap<Integer, Integer> parts = floors.parts();
			for (Integer start : parts.keySet())
				this.partsList.add(new Pair<>(start, parts.get(start)));
		}
		this.reloadParts();
	}

}
