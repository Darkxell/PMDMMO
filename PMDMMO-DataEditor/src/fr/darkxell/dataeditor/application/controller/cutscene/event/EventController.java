package fr.darkxell.dataeditor.application.controller.cutscene.event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.cutscene.CutsceneEvent;

import fr.darkxell.dataeditor.application.controller.cutscene.EditCutsceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public abstract class EventController implements Initializable
{

	@FXML
	protected TextField idTextfield;

	public abstract CutsceneEvent generateEvent();

	public int id()
	{
		return Integer.parseInt(this.idTextfield.getText());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		Pattern pattern = Pattern.compile("-?\\d*");
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern.matcher(change.getControlNewText()).matches() ? change : null;
		});
		this.idTextfield.setTextFormatter(formatter);
		this.idTextfield.setText("-1");
	}

	public void onCancel()
	{
		EditCutsceneController.editEventPopup.close();
	}

	public void onOk()
	{
		this.onCancel();
		CutsceneEvent e = this.generateEvent();
		EditCutsceneController.instance.onEditConfirm(e);
	}

	public void setup(CutsceneEvent event)
	{
		this.idTextfield.setText(String.valueOf(event.id));
	}

}
