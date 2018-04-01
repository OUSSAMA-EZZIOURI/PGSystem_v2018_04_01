package __main__;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import ui.UILog;

/**
 *
 * @author OUSSAMA EZZIOURI
 */
public class PropertiesLoader {

    /**
     *
     */
    private static MessageDigest digester;

    /**
     *
     */
    //public static String HOSTNAME;

    /**
     *
     */
    //public final static Properties APP_PROP = new Properties();
    /**
     *
     */
    public static FileHandler logFileHandler;

    /**
     *
     */
    public static Logger log = null;

    //----------------------------- MD5 BLOC ---------------------------------
    static {
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }

        try {
            GlobalVars.APP_HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }

    }

    /**
     *
     * @param str
     * @return
     */
    public static String crypt(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        digester.update(str.getBytes());
        byte[] hash = digester.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }

    /**
     * Create daily print directory. If it doesn't exist, it creates one a new
     * one
     *
     * @param print_dir_path
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void createDailyOutPrintDir(String print_dir_path) {
        // if the printing output printing
        File print_dir = new File(print_dir_path);
        if (!print_dir.exists()) {
            print_dir.mkdir();
        }

        // if the today printing directory does not exist, create it
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String today = dateFormat.format(now);

        File today_print_dir = new File(print_dir_path + today);
        if (!today_print_dir.exists()) {
            today_print_dir.mkdir();
            log.log(Level.INFO, "Print directory [{0}] is created!\n", today_print_dir.getPath());
        } else {
            log.log(Level.INFO, "Print directory [{0}] already exist!\n", today_print_dir.getPath());
        }
    }

    /**
     *
     * @param print_dir_path
     * @param palet_dir
     * @param galia_dir
     * @param picking_dir
     * @param dispatch_dir
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void createDailyOutPrintDir(String print_dir_path, String palet_dir, String galia_dir, String picking_dir, String dispatch_dir) {
        String msg = "";
        // if the printing output printing
        File print_dir = new File(print_dir_path);
        if (!print_dir.exists()) {
            print_dir.mkdir();
        }

        // if the today printing directory does not exist, create it
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String today = dateFormat.format(now);

        File today_print_dir = new File(print_dir_path + today);
        if (!today_print_dir.exists()) {
            today_print_dir.mkdir();
            msg += "Print directory [" + today_print_dir.getPath() + "] created !\n";
        } else {
            msg += "Print directory [" + today_print_dir.getPath() + "] already exist!\n";
        }

        //Create palet sub directory        
        String palet_dir_path = today_print_dir.getPath() + palet_dir + File.separator;
        File file1 = new File(palet_dir_path);
        if (!file1.exists()) {
            file1.mkdir();
            msg += "Print sub directory [" + file1.getPath() + "] created !\n";
        } else {
            msg += "Print sub directory [" + file1.getPath() + "] already exist!\n";
        }

        //Create galia sub directory
        String galia_dir_path = today_print_dir.getPath() + galia_dir + File.separator;
        File file2 = new File(galia_dir_path);
        if (!file2.exists()) {
            file2.mkdir();
            msg += "Print sub directory [" + file2.getPath() + "] created !\n";
        } else {
            msg += "Print sub directory [" + file2.getPath() + "] already exist!\n";
        }

        //Create galia sub directory
        String picking_dir_path = today_print_dir.getPath() + picking_dir + File.separator;
        File file3 = new File(picking_dir_path);
        if (!file3.exists()) {
            file3.mkdir();
            msg += "Print sub directory [" + file3.getPath() + "] created !\n";
        } else {
            msg += "Print sub directory [" + file3.getPath() + "] already exist!\n";
        }

        //Create galia sub directory
        String dispatch_dir_path = today_print_dir.getPath() + dispatch_dir + File.separator;
        File file4 = new File(dispatch_dir_path);
        if (!file4.exists()) {
            file4.mkdir();
            msg += "Print sub directory [" + file4.getPath() + "] created !\n";
        } else {
            msg += "Print sub directory [" + file4.getPath() + "] already exist!\n";
        }
        UILog.info(msg);
    }

    /**
     * Populate DBInstance.APP_PROP attribut values from config.properties
     */
    public static String loadConfigProperties() {
        String msg="";
        /// read from file
        InputStream input = null;
        try {
            input = new FileInputStream(".\\src\\config.properties");
            // load a properties file
            GlobalVars.APP_PROP.load(input);

            //Map loaded value to global variables
            GlobalVars.mapProperties();

            // get the property value and print it out
            msg+="Load properties file :\n " + GlobalVars.APP_PROP.toString()+"\n";
            return msg;
        } catch (IOException ex) {
            msg = "Properties file must be in the same folder as the .jar file. \n";
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Config properties error !", ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, msg, "Config properties error !", ERROR_MESSAGE);
            return msg;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }

    }

}
