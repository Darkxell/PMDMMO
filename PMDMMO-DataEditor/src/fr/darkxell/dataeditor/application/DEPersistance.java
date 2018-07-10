package fr.darkxell.dataeditor.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.common.util.XMLUtils;

import fr.darkxell.dataeditor.application.util.FileManager;

public class DEPersistance
{

	public static HashMap<String, Cutscene> cutscenes = new HashMap<>();

	public static void load()
	{
		loadCutscenes();
	}

	private static void loadCutscenes()
	{
		String base = FileManager.filePaths.get(FileManager.CUTSCENES);
		ArrayList<String> cutsceneFiles = FileManager.findAllSubFiles(base);
		for (String file : cutsceneFiles)
			try
			{
				Cutscene cutscene = new Cutscene(file.replace(".xml", ""), XMLUtils.read(new FileInputStream(new File(base + "/" + file))));
				cutscenes.put(cutscene.name, cutscene);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
	}

}
