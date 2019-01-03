package com.darkxell.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.darkxell.common.util.language.Message;

public class Logger {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public enum LogLevel {
        NOTSET(0),
        EVENT(5),
        DEBUG(10),
        INFO(20),
        WARNING(30),
        ERROR(40);

        public final int severity;

        /**
         * Shortcuts to log level. Severity levels and a couple names taken from the
         * <a href="https://docs.python.org/3/library/logging.html#levels">Python logging module</a>.
         */
        LogLevel(int severity) {
            this.severity = severity;
        }
    }
    /**
     * At which severity should the logger start outputting to {@code stdout}.
     */
    public static final int STDOUT_SEVERITY = 10;

    /**
     * At which severity should the logger start outputting to {@code stderr}.
     */
    public static final int STDERR_SEVERITY = 40;

    private static Logger instance;

    public static Logger instance() {
        return instance;
    }

    /**
     * Gets the logger instance and prints a debug message.
     */
    public static void d(String m) {
        instance.debug(m);
    }

    /**
     * Gets the logger instance and prints an info message.
     */
    public static void i(String m) {
        instance.info(m);
    }

    /**
     * Gets the logger instance and prints a warning message.
     */
    public static void w(String m) {
        instance.warning(m);
    }

    /**
     * Gets the logger instance and prints an error message.
     */
    public static void e(String m) {
        instance.error(m);
    }

    /**
     * Gets the logger instance and prints an event message.
     */
    public static void event(String m) {
        if (m != null) {
            instance.log(m.replaceAll("<\\/color|<red>|<green>|<blue>|<yellow>", ""), LogLevel.EVENT);
        }
    }

    public static void load(String source) {
        instance = new Logger(source);
    }

    private ArrayList<String> log;

    public boolean saveOnExit = true;

    public final String source;

    private Date start;

    private Logger(String source) {
        this.source = source;
        this.log = new ArrayList<String>();
        this.start = new Date();
        this.info("New instance: " + this.date());
    }

    /**
     * @return The current date with format: yyyy/MM/dd HH:mm:ss
     */
    public String date() {
        return dateFormat.format(new Date());
    }

    public String debug(Message message) {
        return this.log(message, LogLevel.DEBUG);
    }

    public String debug(String message) {
        return this.log(message, LogLevel.DEBUG);
    }

    public String info(Message message) {
        return this.log(message, LogLevel.INFO);
    }

    public String info(String message) {
        return this.log(message, LogLevel.INFO);
    }

    public String warning(Message message) {
        return this.log(message, LogLevel.WARNING);
    }

    public String warning(String message) {
        return this.log(message, LogLevel.WARNING);
    }

    public String error(Message message) {
        return this.log(message, LogLevel.ERROR);
    }

    public String error(String message) {
        return this.log(message, LogLevel.ERROR);
    }

    public String log(Message message, LogLevel messageType) {
        return this.log(message.toString(), messageType);
    }

    public String log(String message, LogLevel level) {
        message = "[" + this.date() + " | " + this.source + "] " + level.name() + ": " + message;

        if (level.severity >= Math.min(STDOUT_SEVERITY, STDERR_SEVERITY)) {
            this.log.add(message);
        }

        if (level.severity >= STDERR_SEVERITY) {
            System.err.println(message);
        } else if (level.severity >= STDOUT_SEVERITY) {
            System.out.println(message);
        }

        return message;
    }

    public void saveClient() {
        if (!this.saveOnExit) {
            return;
        }
        new File("resources").mkdirs();
        File f = new File("resources/log" + new SimpleDateFormat("yyyy-MM-dd HH.mm").format(this.start) + ".txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            for (String line : this.log) {
                writer.write(line + "\n");
            }
            writer.close();
        } catch (IOException e) {
            this.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
