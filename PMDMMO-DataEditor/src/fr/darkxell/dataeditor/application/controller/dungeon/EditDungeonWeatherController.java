package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.weather.Weather;
import fr.darkxell.dataeditor.application.data.WeatherTableItem;

public class EditDungeonWeatherController implements Initializable {

    public static EditDungeonWeatherController instance;

    @FXML
    public HBox container;
    @FXML
    public MenuItem createMenu;
    @FXML
    public MenuItem deleteMenu;
    @FXML
    public MenuItem editMenu;
    @FXML
    public EditWeatherController editWeatherController;
    @FXML
    public TitledPane editWeatherPane;
    @FXML
    public TableColumn<WeatherTableItem, FloorSet> floorsColumn;
    @FXML
    public TableColumn<WeatherTableItem, Weather> weatherColumn;
    @FXML
    public TableView<WeatherTableItem> weatherTable;

    public HashMap<Integer, FloorSet> generate() {
        HashMap<Integer, FloorSet> value = new HashMap<>();
        for (WeatherTableItem item : this.weatherTable.getItems())
            value.put(item.getWeather().id, item.getFloors());
        return value;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        this.editWeatherPane.setVisible(false);
        this.weatherColumn.setCellValueFactory(new PropertyValueFactory<>("weather"));
        this.floorsColumn.setCellValueFactory(new PropertyValueFactory<>("floors"));
    }

    public void onCreate() {
        WeatherTableItem i = new WeatherTableItem(Weather.CLEAR.id,
                new FloorSet(1, EditDungeonDataController.instance.currentFloorCount()));
        this.weatherTable.getItems().add(i);
        this.weatherTable.getSelectionModel().select(i);
        this.onEdit();
    }

    public void onDelete() {
        this.weatherTable.getItems().remove(this.weatherTable.getSelectionModel().getSelectedItem());
    }

    public void onEdit() {
        this.editWeatherPane.setVisible(this.weatherTable.getSelectionModel().getSelectedItem() != null);
        this.editWeatherController.setupFor(this.weatherTable.getSelectionModel().getSelectedItem());
    }

    public void onMenuRequested() {
        this.editMenu.setDisable(this.weatherTable.getSelectionModel().isEmpty());
        this.deleteMenu.setDisable(this.weatherTable.getSelectionModel().isEmpty());
    }

    public void onWeatherEdited(WeatherTableItem item) {
        if (this.editWeatherController.editing == null || item == null)
            return;
        this.weatherTable.getItems().remove(this.editWeatherController.editing);
        this.weatherTable.getItems().add(item);
        this.weatherTable.getSelectionModel().select(item);
        this.onEdit();
    }

    public void setupFor(Dungeon dungeon) {
        this.weatherTable.getItems().clear();
        HashMap<Integer, FloorSet> w = dungeon.weatherData();
        for (Integer id : w.keySet())
            this.weatherTable.getItems().add(new WeatherTableItem(id, w.get(id)));
        this.onEdit();
    }

}
