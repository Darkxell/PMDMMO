package com.darkxell.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Logger
{
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	/** Booleans describing whether logging of each type should be effective. */
	public static final boolean error = true, info = true, debug = true, warning = true;
	public static final String ERROR = "ERROR", INFO = "INFO", DEBUG = "DEBUG", WARNING = "WARNING";

	private static Logger instance;

	public static Logger instance()
	{
		return instance;
	}

	public static void loadClient()
	{
		instance = new Logger("CLIENT");
	}

	public static void loadServer()
	{
		instance = new Logger("SERVER");
	}

	private ArrayList<String> log;
	public boolean saveOnExit = false;
	public final String source;
	private Date start;

	private Logger(String source)
	{
		this.source = source;
		this.log = new ArrayList<String>();
		this.start = new Date();
		this.info("New instance: " + this.date());
	}

	/** @return The current date with format: yyyy/MM/dd HH:mm:ss */
	public String date()
	{
		return dateFormat.format(new Date());
	}

	public String debug(Message message)
	{
		return this.log(message, DEBUG);
	}

	public String debug(String message)
	{
		return this.log(message, DEBUG);
	}

	public String error(Message message)
	{
		return this.log(message, ERROR);
	}

	public String error(String message)
	{
		return this.log(message, ERROR);
	}

	public String info(Message message)
	{
		return this.log(message, INFO);
	}

	public String info(String message)
	{
		return this.log(message, INFO);
	}

	public String log(Message message, String messageType)
	{
		return this.log(message.toString(), messageType);
	}

	public String log(String message, String messageType)
	{
		message = "[" + this.date() + " | " + this.source + "] " + messageType + ": " + message;

		if ((messageType.equals(DEBUG) && debug) || (messageType.equals(ERROR) && error) || (messageType.equals(INFO) && info)
				|| (messageType.equals(WARNING) && warning))
		{
			this.log.add(message);
			if (messageType == ERROR) System.err.println(message);
			else System.out.println(message);
		}

		return message;
	}

	public void saveClient()
	{
		if (!this.saveOnExit) return;
		File f = new File("resources/log" + new SimpleDateFormat("yyyy-MM-dd HH.mm").format(this.start) + ".txt");
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			for (String line : this.log)
				writer.write(line + "\n");
			writer.close();
		} catch (IOException e)
		{
			this.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public String warning(Message message)
	{
		return this.log(message, WARNING);
	}

	public String warning(String message)
	{
		return this.log(message, WARNING);
	}

}
