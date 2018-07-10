package fr.darkxell.dataeditor.application.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.common.util.XMLUtils;

import fr.darkxell.dataeditor.application.util.FileManager;

public class Cutscenes
{

	private static HashMap<String, Cutscene> cutscenes = new HashMap<>();

	public static void add(Cutscene cutscene)
	{
		if (!cutscenes.containsKey(cutscene.name)) put(cutscene.name, cutscene);
	}

	public static boolean containsKey(String name)
	{
		return cutscenes.containsKey(name);
	}

	public static void load()
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

	private static void put(String name, Cutscene cutscene)
	{
		cutscenes.put(name, cutscene);
		File f = FileManager.create(FileManager.filePaths.get(FileManager.CUTSCENES) + "/" + name + ".xml");
		XMLUtils.saveFile(f, cutscene.toXML());
	}

	public static void remove(String name)
	{
		cutscenes.remove(name);
		FileManager.delete(FileManager.filePaths.get(FileManager.CUTSCENES) + "/" + name + ".xml");
	}

	public static void update(Cutscene cutscene)
	{
		put(cutscene.name, cutscene);
	}

	public static Collection<Cutscene> values()
	{
		return cutscenes.values();
	}

}
