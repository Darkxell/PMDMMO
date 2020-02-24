package com.darkxell.pmdmmo.freezoneconverter;

import java.io.File;
import java.io.IOException;

import com.darkxell.client.model.freezone.FreezoneModel;
import com.darkxell.client.model.io.ClientModelIOHandlers;
import com.darkxell.pmdmmo.freezoneconverter.mapper.TiledToPmdmmoMapper;
import com.darkxell.pmdmmo.freezoneconverter.tiledmodel.TiledFreezoneModel;
import com.darkxell.pmdmmo.freezoneconverter.tiledmodel.TiledFreezoneModelIOHandler;

public class FreezoneConverter {

	private static final TiledFreezoneModelIOHandler ioHandler = new TiledFreezoneModelIOHandler();

	public static void convert(File origin, File destination) throws IOException {

		System.out.println();
		System.out.println(" ----- Converting " + origin.getAbsolutePath());
		System.out.println(" ------------- To " + destination.getAbsolutePath());

		if (!destination.exists()) {
			destination.mkdirs();
			destination.delete();
			if (!destination.createNewFile()) {
				System.err.println("Couldn't create file " + destination.getAbsolutePath());
				return;
			}
		}

		@SuppressWarnings("deprecation")
		TiledFreezoneModel tiledmodel = ioHandler.read(origin.toURL());
		FreezoneModel model = TiledToPmdmmoMapper.map(tiledmodel);
		ClientModelIOHandlers.freezone.export(model, destination);

		System.out.println(" ----- DONE -----");
	}

	public static void convertFromBatch(String file, String originDirectory, String destinationDirectory)
			throws IOException {
		if (file.startsWith(originDirectory))
			file = file.substring(originDirectory.length());
		if (file.startsWith("/"))
			file = file.substring(0);
		if (originDirectory.endsWith("/") || originDirectory.endsWith("\\"))
			originDirectory = originDirectory.substring(0, originDirectory.length() - 1);
		if (destinationDirectory.endsWith("/") || destinationDirectory.endsWith("\\"))
			destinationDirectory = destinationDirectory.substring(0, destinationDirectory.length() - 1);

		File destination = new File(destinationDirectory + "/" + file);
		convert(new File(originDirectory + "/" + file), destination);
	}

}
