package fr.darkxell.dataeditor.application.controller.sprites;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesetData;
import com.darkxell.common.util.Direction;

import fr.darkxell.dataeditor.application.data.SpriteTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Pair;

public class EditTableController implements Initializable
{

	@FXML
	public TableColumn<SpriteTableItem, String> eastColumn;
	@FXML
	public TableColumn<SpriteTableItem, String> northColumn;
	@FXML
	public TableColumn<SpriteTableItem, String> northeastColumn;
	@FXML
	public TableColumn<SpriteTableItem, String> northwestColumn;
	public SpritesTabController parent;
	@FXML
	public TableColumn<SpriteTableItem, String> southColumn;
	@FXML
	public TableColumn<SpriteTableItem, String> southeastColumn;
	@FXML
	public TableColumn<SpriteTableItem, String> southwestColumn;
	@FXML
	public TableColumn<SpriteTableItem, PokemonSpriteState> stateColumn;
	@FXML
	public TableView<SpriteTableItem> table;
	@FXML
	public TableColumn<SpriteTableItem, String> westColumn;

	public void fillRowWithNew()
	{
		SpriteTableItem i = this.table.getSelectionModel().getSelectedItem();
		HashSet<Integer> existing = new HashSet<>();
		for (SpriteTableItem it : this.table.getItems())
		{
			existing.add(it.north);
			existing.add(it.northeast);
			existing.add(it.east);
			existing.add(it.southeast);
			existing.add(it.south);
			existing.add(it.southwest);
			existing.add(it.west);
			existing.add(it.northwest);
		}
		i.fillWithNew(existing);
		this.table.refresh();
		this.onExistingSequencesChanged(existing);
	}

	private SpriteTableItem getSpriteItem(PokemonSpriteState state)
	{
		for (SpriteTableItem i : this.table.getItems())
			if (i.state == state) return i;
		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
		this.northColumn.setCellValueFactory(new PropertyValueFactory<>("north"));
		this.northeastColumn.setCellValueFactory(new PropertyValueFactory<>("northeast"));
		this.eastColumn.setCellValueFactory(new PropertyValueFactory<>("east"));
		this.southeastColumn.setCellValueFactory(new PropertyValueFactory<>("southeast"));
		this.southColumn.setCellValueFactory(new PropertyValueFactory<>("south"));
		this.southwestColumn.setCellValueFactory(new PropertyValueFactory<>("southwest"));
		this.westColumn.setCellValueFactory(new PropertyValueFactory<>("west"));
		this.northwestColumn.setCellValueFactory(new PropertyValueFactory<>("northwest"));

		this.northColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.northeastColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.eastColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.southeastColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.southColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.southwestColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.westColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.northwestColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		for (PokemonSpriteState s : PokemonSpriteState.values())
			this.table.getItems().add(new SpriteTableItem(s));
	}

	public void onEastEdited(CellEditEvent<SpriteTableItem, String> cell)
	{
		String value = cell.getNewValue();
		if (value.matches("\\d+")) cell.getRowValue().east = Integer.parseInt(value);
		else
		{
			cell.getRowValue().east = -1;
			this.table.refresh();
		}
	}

	private void onExistingSequencesChanged(HashSet<Integer> existing)
	{
		this.parent.onExistingSequencesChanged(existing);
	}

	public void onNortheastEdited(CellEditEvent<SpriteTableItem, String> cell)
	{
		String value = cell.getNewValue();
		if (value.matches("\\d+")) cell.getRowValue().northeast = Integer.parseInt(value);
		else
		{
			cell.getRowValue().northeast = -1;
			this.table.refresh();
		}
	}

	public void onNorthEdited(CellEditEvent<SpriteTableItem, String> cell)
	{
		String value = cell.getNewValue();
		if (value.matches("\\d+")) cell.getRowValue().north = Integer.parseInt(value);
		else
		{
			cell.getRowValue().north = -1;
			this.table.refresh();
		}
	}

	public void onNorthwestEdited(CellEditEvent<SpriteTableItem, String> cell)
	{
		String value = cell.getNewValue();
		if (value.matches("\\d+")) cell.getRowValue().northwest = Integer.parseInt(value);
		else
		{
			cell.getRowValue().northwest = -1;
			this.table.refresh();
		}
	}

	public void onSoutheastEdited(CellEditEvent<SpriteTableItem, String> cell)
	{
		String value = cell.getNewValue();
		if (value.matches("\\d+")) cell.getRowValue().southeast = Integer.parseInt(value);
		else
		{
			cell.getRowValue().southeast = -1;
			this.table.refresh();
		}
	}

	public void onSouthEdited(CellEditEvent<SpriteTableItem, String> cell)
	{
		String value = cell.getNewValue();
		if (value.matches("\\d+")) cell.getRowValue().south = Integer.parseInt(value);
		else
		{
			cell.getRowValue().south = -1;
			this.table.refresh();
		}
	}

	public void onSouthwestEdited(CellEditEvent<SpriteTableItem, String> cell)
	{
		String value = cell.getNewValue();
		if (value.matches("\\d+")) cell.getRowValue().southwest = Integer.parseInt(value);
		else
		{
			cell.getRowValue().southwest = -1;
			this.table.refresh();
		}
	}

	public void onWestEdited(CellEditEvent<SpriteTableItem, String> cell)
	{
		String value = cell.getNewValue();
		if (value.matches("\\d+")) cell.getRowValue().west = Integer.parseInt(value);
		else
		{
			cell.getRowValue().west = -1;
			this.table.refresh();
		}
	}

	public void setupFor(PokemonSpritesetData item)
	{
		for (SpriteTableItem i : this.table.getItems())
			i.reset();

		HashMap<Pair<PokemonSpriteState, Direction>, Integer> map = item.sequenceMapper();
		for (PokemonSpriteState s : PokemonSpriteState.values())
		{
			SpriteTableItem i = this.getSpriteItem(s);
			Pair<PokemonSpriteState, Direction> key = new Pair<>(s, Direction.NORTH);
			if (map.containsKey(key)) i.north = map.get(key);
			key = new Pair<>(s, Direction.NORTHEAST);
			if (map.containsKey(key)) i.northeast = map.get(key);
			key = new Pair<>(s, Direction.EAST);
			if (map.containsKey(key)) i.east = map.get(key);
			key = new Pair<>(s, Direction.SOUTHEAST);
			if (map.containsKey(key)) i.southeast = map.get(key);
			key = new Pair<>(s, Direction.SOUTH);
			if (map.containsKey(key)) i.south = map.get(key);
			key = new Pair<>(s, Direction.SOUTHWEST);
			if (map.containsKey(key)) i.southwest = map.get(key);
			key = new Pair<>(s, Direction.WEST);
			if (map.containsKey(key)) i.west = map.get(key);
			key = new Pair<>(s, Direction.NORTHWEST);
			if (map.containsKey(key)) i.northwest = map.get(key);
		}

		this.table.refresh();
	}

}
