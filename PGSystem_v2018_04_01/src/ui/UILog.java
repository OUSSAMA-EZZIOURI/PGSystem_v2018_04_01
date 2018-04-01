/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import __main__.GlobalVars;
import static __main__.PropertiesLoader.logFileHandler;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;

/**
 *
 * @author Oussama EZZIOURI
 */
public class UILog {

    /**
     *
     */
    public static Logger LOGGER = null;

    /**
     *
     * @return
     */
    public static Logger getLOGGER() {
        return LOGGER;
    }

    /**
     *
     * @param LOGGER
     */
    public static void setLOGGER(Logger LOGGER) {
        UILog.LOGGER = LOGGER;
    }

    /**
     * Log a message, with an array of object arguments.
     * <p>
     * Translate the message from MessageBundle file and print it into the log
     * file
     * <p>
     * @param msg
     * @param args Arguments to be replaced in msg string
     * @return The logged message
     */
    public static String log(String msg, Object... args) {
        return log(Level.INFO, String.format(i18n(msg), args));
    }

    /**
     * Log a simple message.
     * <p>
     * Translate the message from MessageBundle file and print it into the log
     * file
     * <p>
     * @param msg
     * @return The logged message
     */
    public static String log(String msg) {
        return log(Level.INFO, i18n(msg));
    }

    /**
     * Used to print a info message into the log file.
     *
     * @param msg : Message to be logged MessageBundle.properties file. false
     * print directly the given message.
     * @param args
     * @return The logged message
     */
    public static String info(String msg, Object... args) {
        return log(Level.INFO, String.format(i18n(msg), args));
    }

    /**
     * Used to print a info message into the log file.
     *
     * @param msg : Message to be logged MessageBundle.properties file. false
     * print directly the given message.
     * @return The logged message
     */
    public static String info(String msg) {
        return log(Level.INFO, i18n(msg));
    }

    /**
     * Used to print a warning message into the log file.
     *
     * @param msg : Message to be logged Use 'i18n' function to load translation
     * from MessageBundle.properties file. false print directly the given
     * message.
     * @param args
     * @return The logged message
     */
    public static String warn(String msg, Object... args) {
        return log(Level.WARNING, String.format(i18n(msg), args));
    }

    /**
     * Used to print a warning message into the log file.
     *
     * @param msg : Message to be logged Use 'i18n' function to load translation
     * from MessageBundle.properties file. false print directly the given
     * message.
     * @return The logged message
     */
    public static String warn(String msg) {
        return log(Level.WARNING, i18n(msg));
    }

    /**
     * Used to print a severe message into the log file. It use the 'i18n'
     * function to load translation from MessageBundle.properties file.
     *
     * @param msg : Message to be logged
     * @param args
     * @return The logged message
     */
    public static String severe(String msg, Object... args) {
        return log(Level.SEVERE, String.format(i18n(msg), args));
    }

    /**
     * Used to print a severe message into the log file. It use the 'i18n'
     * function to load translation from MessageBundle.properties file.
     *
     * @param msg : Message to be logged
     * @return The logged message
     */
    public static String severe(String msg) {
       return log(Level.SEVERE, i18n(msg));
    }

    /**
     *
     * It use 'i18n' function to load translation from MessageBundle.properties
     * file. false print directly the given message.
     *
     * @param level
     * @param msg
     * @return The logged message
     *
     */
    public static String log(Level level, String msg) {

        if (level == Level.INFO) {
            LOGGER.info(msg);
        } else if (level == Level.FINE) {
            LOGGER.fine(msg);
        } else if (level == Level.FINER) {
            LOGGER.finer(msg);
        } else if (level == Level.FINEST) {
            LOGGER.finest(msg);
        } else if (level == Level.SEVERE) {
            LOGGER.severe(msg);
        } else if (level == Level.WARNING) {
            LOGGER.warning(msg);
        } else if (level == Level.CONFIG) {
            LOGGER.config(msg);
        }
        return msg;
    }

    /**
     * Brings up a dialog that displays a message using a default icon
     * determined by the <code>messageType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     * dialog is displayed; if <code>null</code>, or if the
     * <code>parentComponent</code> has no <code>Frame</code>, a default
     * <code>Frame</code> is used
     * @param msg the <code>Object</code> to display {title, message, reason,
     * what to do to solve the error } <code>INFORMATION_MESSAGE</code>
     */
    public static void infoDialog(Component parentComponent, String[] msg) {
        JOptionPane.showMessageDialog(parentComponent, i18n(msg[0]), msg[0], JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Brings up a dialog that displays a message using a default icon
     * determined by the <code>messageType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     * dialog is displayed; if <code>null</code>, or if the
     * <code>parentComponent</code> has no <code>Frame</code>, a default
     * <code>Frame</code> is used
     * @param msg the <code>Object</code> to display {title, message, reason,
     * what to do to solve the error } <code>INFORMATION_MESSAGE</code>
     * @param args Arguments to be replaced in the message msg[1]
     */
    public static void infoDialog(Component parentComponent, String[] msg, Object... args) {
        JOptionPane.showMessageDialog(parentComponent, i18n(msg[0]), msg[0], JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Brings up a dialog that displays a message using a default icon
     * determined by the <code>messageType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     * dialog is displayed; if <code>null</code>, or if the
     * <code>parentComponent</code> has no <code>Frame</code>, a default
     * <code>Frame</code> is used
     * @param msg the <code>Object</code> to display {title, message, reason,
     * what to do to solve the error } <code>WARNING_MESSAGE</code>
     */
    public static void warnDialog(Component parentComponent, String[] msg) {
        JOptionPane.showMessageDialog(parentComponent, i18n(msg[0]), msg[0], JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Brings up a dialog that displays a message using a default icon
     * determined by the <code>messageType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     * dialog is displayed; if <code>null</code>, or if the
     * <code>parentComponent</code> has no <code>Frame</code>, a default
     * <code>Frame</code> is used
     * @param msg the <code>Object</code> to display {title, message, reason,
     * what to do to solve the error } <code>ERROR_MESSAGE</code>
     */
    public static void severeDialog(Component parentComponent, String[] msg) {
        JOptionPane.showMessageDialog(parentComponent, i18n(msg[0]), msg[0], JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Brings up a dialog that displays a message using a default icon
     * determined by the <code>messageType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code> in which the
     * dialog is displayed; if <code>null</code>, or if the
     * <code>parentComponent</code> has no <code>Frame</code>, a default
     * <code>Frame</code> is used
     * @param msg the <code>Object</code> to display {title, message, reason,
     * what to do to solve the error } <code>ERROR_MESSAGE</code>
     * @param args Arguments to be replaced in the message msg[1]
     */
    public static void severeDialog(Component parentComponent, String[] msg, Object... args) {
        JOptionPane.showMessageDialog(parentComponent, String.format(i18n(msg[0]), args), msg[0], JOptionPane.ERROR_MESSAGE);
    }

    //-------------------------- Log folder Path ------------------------------
    /**
     * Create daily logging file. If the log output directory doesn't exist, it
     * creates one
     *
     * @param path
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void createDailyLogFile(String path) {

        String msg = "";

        // if the main log directory does not exist, create it        
        File log_dir = new File(path);
        if (!log_dir.exists()) {
            log_dir.mkdir();
        }

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String today = dateFormat.format(now);
        File today_log_dir = new File(path + today);

        // if the today log directory does not exist, create it
        if (!today_log_dir.exists()) {
            today_log_dir.mkdir();
        }
        try {
            String log_path = today_log_dir + File.separator + today + ".log";
            File file = new File(log_path);

            if (file.createNewFile()) {
                msg += "File " + file.getName() + " is created!\n";
            } else {
                msg += "File " + file.getName() + " already exists.\n";
            }
            //Intilize the logFileHandler, formating etc...

            logFileHandler = new FileHandler(log_path, true);
            logFileHandler.setFormatter(new SimpleFormatter());
            //Add the file handler to log object.            
            LOGGER = Logger.getLogger(log_path);
            LOGGER.addHandler(logFileHandler);

            UILog.log(Level.INFO, msg);
        } catch (IOException e) {
        }

    }

    /**
     * Class to load translation for each UILogger level from
     * src/MessageBundle_xx_XX.properties
     *
     * @param msgcode
     * @return The translated message according to country and language code in
     * config.properties file
     */
    public static String i18n(String msgcode) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("MessageBundle");
            return bundle.getString(msgcode);
        } catch (Exception e) {
            System.out.println("Message [" + msgcode + "] to be defined in 'MessageBundle_" + GlobalVars.APP_PROP.getProperty("LANG") + "_" + GlobalVars.APP_PROP.getProperty("LOCAL") + ".properties' file");
            return msgcode;
        }

    }
}
