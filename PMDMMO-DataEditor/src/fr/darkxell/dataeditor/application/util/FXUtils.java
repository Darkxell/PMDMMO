package fr.darkxell.dataeditor.application.util;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.darkxell.client.resources.Res;

import fr.darkxell.dataeditor.application.DataEditor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXUtils
{

	private static final HashMap<String, BufferedImage> icons = new HashMap<>();

	public static BufferedImage getIcon(String path)
	{
		if (!icons.containsKey(path)) icons.put(path, Res.getBase(path));
		return icons.get(path);
	}

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
