package fr.darkxell.dataeditor.application.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.darkxell.client.resources.images.Res;

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
		InputStream is = Res.class.getResourceAsStream(path);
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(is);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if (!icons.containsKey(path)) icons.put(path, img);
		return icons.get(path);
	}

	public static Stage showPopup(Parent root, String title)
	{
		Stage s = new Stage();
		s.setScene(new Scene(root));
		s.setTitle(title);
		s.getIcons().addAll(DataEditor.primaryStage.getIcons());
		s.setWidth(700);
		s.setHeight(600);
		s.initModality(Modality.APPLICATION_MODAL);
		s.show();
		return s;
	}

}
