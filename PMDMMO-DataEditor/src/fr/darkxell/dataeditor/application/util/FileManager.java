package fr.darkxell.dataeditor.application.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager
{

	public static final HashMap<String, String> filePaths = new HashMap<>();

	public static final String LANG = "lang", CUTSCENES = "cutscenes", POKEMON_SPRITES = "sprites";

	static
	{
		filePaths.put(LANG, "../PMDMMOc/resources/lang");
		filePaths.put(CUTSCENES, "../PMDMMOc/resources/cutscenes");
		filePaths.put(POKEMON_SPRITES, "../PMDMMOc/resources/pokemons/data");
	}

	public static File create(String path)
	{
		File file = new File(path);
		if (!file.exists()) file.mkdirs();
		file.delete();
		try
		{
			file.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return file;
	}

	public static void delete(String path)
	{
		File f = new File(path);
		if (f.exists()) f.delete();
	}

	public static ArrayList<String> findAllSubFiles(String startPath)
	{
		return findAllSubFiles(startPath, "");
	}

	private static ArrayList<String> findAllSubFiles(String path, String parents)
	{
		ArrayList<String> files = new ArrayList<>();

		File f = new File(path);
		if (f.exists() && f.isDirectory()) for (String sub : f.list())
		{
			String p = path + "/" + sub;
			File subf = new File(p);
			if (subf.isDirectory()) files.addAll(findAllSubFiles(p, parents + sub + "/"));
			else files.add(parents + sub);
		}

		return files;
	}

}
