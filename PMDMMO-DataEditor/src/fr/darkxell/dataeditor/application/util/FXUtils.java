package fr.darkxell.dataeditor.application.util;

import fr.darkxell.dataeditor.application.DataEditor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXUtils
{

	public static Stage showPopup(Parent root, String title)
	{
		Stage s = new Stage();
		s.setScene(new Scene(root));
		s.setTitle(title);
		s.getIcons().addAll(DataEditor.primaryStage.getIcons());
		s.setWidth(300);
		s.setHeight(400);
		s.initModality(Modality.APPLICATION_MODAL);
		s.show();
		return s;
	}

}
