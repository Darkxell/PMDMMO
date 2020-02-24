package com.darkxell.pmdmmo.freezoneconverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import fr.darkxell.dataeditor.application.util.FileManager;

public class FreezoneConverterRunner {

	private static void convertAll(String[] args) throws IOException {
		String opath = args[1], dpath = args[2];

		File origin = new File(opath), destination = new File(dpath);

		if (!origin.exists()) {
			System.err.println("Origin directory " + opath + " doesn't exist.");
			return;
		}
		if (!destination.exists()) {
			System.out.println("Destination directory " + dpath + " doesn't exist, creating it.");
			if (!destination.mkdirs())
				System.err.println("Couldn't create destination directory " + dpath);
		}

		ArrayList<String> files = FileManager.findAllSubFiles(opath);
		files.removeIf(f -> !f.endsWith(".xml"));

		for (String file : files)
			System.out.println("  * " + file);
		System.out.println("This will convert all the previously listed XML files.");
		System.out.println("From the origin folder: " + opath);
		System.out.println("And export them to the destination folder: " + dpath);
		System.out.println("Continue? (y/n)");

		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		sc.close();

		if (!input.toLowerCase().startsWith("y")) {
			System.out.println("Aborting.");
			return;
		}

		for (String file : files)
			FreezoneConverter.convertFromBatch(file, opath, dpath);
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 3) {
			invalidSyntax();
		} else {
			System.out.println("Tiled to PMDMMO Converter -----------------------------------------------------");
			String mode = args[0];
			while (mode.startsWith("-"))
				mode = mode.substring(1);
			if (mode.startsWith("s")) {
				convertSingle(args);
			} else if (mode.startsWith("a")) {
				convertAll(args);
			} else {
				invalidSyntax();
			}
			System.out.println("Converter finished the job. --------------------------------------------------");
		}

	}

	private static void convertSingle(String[] args) throws IOException {
		String opath = args[1], dpath = args[2];

		File origin = new File(opath), destination = new File(dpath);

		if (!origin.exists()) {
			System.err.println("Origin file " + opath + " doesn't exist.");
			return;
		}
		if (destination.exists()) {
			System.out.println("Destination file " + dpath + " already exists. Continue? (y/n)");

			Scanner sc = new Scanner(System.in);
			String input = sc.nextLine();
			sc.close();

			if (!input.toLowerCase().startsWith("y")) {
				System.out.println("Aborting.");
				return;
			}
		}

		FreezoneConverter.convert(origin, destination);
	}

	private static void invalidSyntax() {
		System.err.println("Invalid arguments. Use one of the following syntaxes:");
		System.err.println("\trunner.jar --single <origin-path> <destination-path>");
		System.err.println("\trunner.jar --all <origin-directory> <destination-directory>");
		System.exit(0);
	}

}
