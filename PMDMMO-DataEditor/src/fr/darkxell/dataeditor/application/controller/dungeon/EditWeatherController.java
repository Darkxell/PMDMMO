package fr.darkxell.dataeditor.application.controller.dungeon;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.common.weather.Weather;

import fr.darkxell.dataeditor.application.data.WeatherTableItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class EditWeatherController implements Initializable
{

	public WeatherTableItem editing;
	@FXML
	public EditFloorsetController floorsetController;
	@FXML
	public ComboBox<Weather> weatherCombobox;

	public void cancelChances()
	{
		this.setupFor(this.editing);
	}

	private WeatherTableItem generate()
	{
		return new WeatherTableItem(this.weatherCombobox.getValue().id, this.floorsetController.generate());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.weatherCombobox.getItems().addAll(Weather.list());
		this.weatherCombobox.getSelectionModel().select(0);
	}

	public void onSave()
	{
		EditDungeonWeatherController.instance.onWeatherEdited(this.generate());
	}

	public void setupFor(WeatherTableItem item)
	{
		this.editing = item;
		if (item != null) this.weatherCombobox.setValue(item.getWeather());
		this.floorsetController.setupFor(item == null ? null : item.getFloors());
	}

}
