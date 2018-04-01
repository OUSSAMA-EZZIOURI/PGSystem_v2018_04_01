/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package __main__;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Oussama EZZIOURI
 */
public class GlobalVars {

    /**
     *
     */
    public static String APP_NAME = "PGSystem";

    /**
     *
     */
    public static String APP_VERSION = "v18.04.01";

    /**
     *
     */
    public static String APP_AUTHOR = "Created By EZZIOURI Oussama";

    /**
     *
     */
    public static String APP_HOSTNAME;

    /**
     * GlobalVars propeties of the application
     */
    public final static Properties APP_PROP = new Properties();

    //........................ PREFIXS .........................
    /**
     *
     */
    public static String HARN_PART_PREFIX = GlobalVars.APP_PROP.getProperty("HARN_PART_PREFIX");

    /**
     *
     */
    public static String DISPATCH_SERIAL_NO_PREFIX = GlobalVars.APP_PROP.getProperty("DISPATCH_SERIAL_NO_PREFIX");

    /**
     *
     */
    public static String DISPATCH_PN_PREFIX = GlobalVars.APP_PROP.getProperty("DISPATCH_PN_PREFIX");

    /**
     *
     */
    public static String SUPPLIER_PART_PREFIX = GlobalVars.APP_PROP.getProperty("SUPPLIER_PART_PREFIX");

    /**
     *
     */
    public static String HARN_COUNTER_PREFIX = GlobalVars.APP_PROP.getProperty("HARN_COUNTER_PREFIX");

    /**
     *
     */
    public static String QUANTITY_PREFIX = GlobalVars.APP_PROP.getProperty("QUANTITY_PREFIX");

    /**
     *
     */
    public static String CLOSING_PALLET_PREFIX = GlobalVars.APP_PROP.getProperty("CLOSING_PALLET_PREFIX");

    /**
     *
     */
    public static String OPEN_PALLET_KEYWORD = GlobalVars.APP_PROP.getProperty("OPEN_PALLET_KEYWORD");

    public static void mapProperties() {
        OPEN_PALLET_KEYWORD = GlobalVars.APP_PROP.getProperty("OPEN_PALLET_KEYWORD");

        HARN_PART_PREFIX = GlobalVars.APP_PROP.getProperty("HARN_PART_PREFIX");

        SUPPLIER_PART_PREFIX = GlobalVars.APP_PROP.getProperty("SUPPLIER_PART_PREFIX");

        HARN_COUNTER_PREFIX = GlobalVars.APP_PROP.getProperty("HARN_COUNTER_PREFIX");

        QUANTITY_PREFIX = GlobalVars.APP_PROP.getProperty("QUANTITY_PREFIX");

        CLOSING_PALLET_PREFIX = GlobalVars.APP_PROP.getProperty("CLOSING_PALLET_PREFIX");
        
        DISPATCH_SERIAL_NO_PREFIX = GlobalVars.APP_PROP.getProperty("DISPATCH_SERIAL_NO_PREFIX");
        
        DISPATCH_PN_PREFIX = GlobalVars.APP_PROP.getProperty("DISPATCH_PN_PREFIX");
    }

    /**
     *
     */
    public static String UCS_SPLITER = "|";

    //##########################################################################
    //##########################################################################
    //##########################################################################
    //.............................. PATTERNS LIST .............................
    //Example ; P2220512705.02.2016;11:44:00
    /**
     *
     */
    public static String[] DATAMATRIX_PATTERN_LIST;

    /**
     *
     */
    public static String[] PARTNUMBER_PATTERN_LIST;

    /**
     *
     */
    public static String[][] PLASTICBAG_BARCODE_PATTERN_LIST;

    //##########################################################################
    //##########################################################################
    //##########################################################################    
    //......................PALLET PRINTING STATES .............................
    /**
     *
     */
    public static final String PALLET_PRINT_NEW = "NEW";

    /**
     *
     */
    public static final String PALLET_PRINT_INPROCESS = "IN_PROCESS";

    /**
     *
     */
    public static final String PALLET_PRINT_PRINTED = "PRINTED";

    /**
     *
     */
    public static final String PALLET_PRINT_ERROR = "ERROR";

    /**
     *
     */
    public static final String PALLET_PRINT_REPRINT = "REPRINT";

    //##########################################################################
    //##########################################################################
    //##########################################################################    
    //........................... PALLET STATES ................................
    /**
     *
     */
    public static final String PALLET_OPEN = "OPEN";

    /**
     *
     */
    public static final String PALLET_OPEN_CODE = "1000";

    /**
     *
     */
    public static final String PALLET_WAITING = "WAITING";

    /**
     *
     */
    public static final String PALLET_WAITING_CODE = "1500";

    /**
     *
     */
    public static final String PALLET_QUARANTAINE = "QUARANTAINE";

    /**
     *
     */
    public static final String PALLET_QUARANTAINE_CODE = "1400";

    /**
     *
     */
    public static final String PALLET_CLOSED = "CLOSED";

    /**
     *
     */
    public static final String PALLET_CLOSED_CODE = "1900";

    /**
     *
     */
    public static final String PALLET_STORED = "STORED";

    /**
     *
     */
    public static final String PALLET_STORED_CODE = "2000";

    /**
     *
     */
    public static final String PALLET_SHIPPED = "SHIPPED";

    /**
     *
     */
    public static final String PALLET_SHIPPED_CODE = "3000";

    /**
     *
     */
    public static final String PALLET_DROPPED = "DROPPED";

    /**
     *
     */
    public static final String PALLET_DROPPED_CODE = "-1000";

    /**
     *
     */
    public static final String[][] PALLET_STATES = {
        {"ALL", "selected"},
        {PALLET_OPEN, ""},
        {PALLET_WAITING, ""},
        {PALLET_CLOSED, ""},
        {PALLET_STORED, ""},
        {PALLET_SHIPPED, ""}, //{PALLET_QUARANTAINE, ""}
    };

    //##########################################################################
    //##########################################################################
    //##########################################################################    
    //........................... CONFIG STATES ................................
    /**
     *
     */
    public static List<String> CONFIG_MENUS = Arrays.asList(
            "---",
            "Unités de conditionnement standard (UCS)",
            "Masque code à barre",
            "Utilisateurs",
            //"Planner",
            "Configuration packaging"
    );

    //##########################################################################
    //##########################################################################
    //##########################################################################    
    //........................... PROFIL LEVELS ................................
    /**
     *
     */
    public static final Integer PROFIL_OPERATOR = 1000;
    /**
     *
     */
    public static final Integer PROFIL_ADMIN = 9000;

    /**
     * true: Only one pack type can be opened in the actual workstation (KLTV,
     * HV, RV)
     *
     */
    public static final boolean UNIQUE_PALLET_PER_PACK_TYPE = false;

    //PALLET STATE COLLUMN INDEX IN UI0000_MAIN CONTAINER TABLE
    /**
     *
     */
    public static int PALLET_STATE_COL_INDEX = 8;

    /**
     *
     *
     */
    public static String SCHEDULE_STATE_NEW = "NEW";

    /**
     *
     *
     */
    public static String SCHEDULE_STATE_NEW_CODE = "1000";

    //HARNESS PART COUNTER LENGTH
    /**
     *
     */
    public static Integer HARN_PART_LEN = 9;
}
