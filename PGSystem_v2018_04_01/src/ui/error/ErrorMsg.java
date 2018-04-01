/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.error;

/**
 *
 * @author Oussama EZZIOURI
 */
public class ErrorMsg {

    /**
     * Failed to connect to database !
     */
    public static String[] APP_ERR0001 = {"APP_ERR0001", "APP_ERR0001 : Failed to connect to the database !"};

    /**
     * Application encountered a problem !
     */
    public static String[] APP_ERR0002 = {"APP_ERR0002", "APP_ERR0002 : Application encountered a problem !"};

    /**
     * Application already running !
     */
    public static String[] APP_ERR0003 = {"APP_ERR0003", "APP_ERR0003 : Application already running !"};
    /**
     * Invalid matricule number or account locked !
     */
    public static String[] APP_ERR0004 = {"APP_ERR0004", "APP_ERR0004 : Invalid matricule number or account locked !"};
    /**
     * Part number [%s] not found.
     */
    public static String[] APP_ERR0005 = {"APP_ERR0005", "APP_ERR0005 : Part number [%s] not found."};

    /**
     * Invalid part number scanned [%s] !.
     */
    public static String[] APP_ERR0006 = {"APP_ERR0006", "APP_ERR0006 : Invalid part number scanned [%s] !."};
    /**
     * Part number [%s] not configured for the project [%s].
     */
    public static String[] APP_ERR0007 = {"APP_ERR0007", "APP_ERR0007 : Part number [%s] not configured for the project [%s]."};
    /**
     * Many users founds with the same login [%s]
     */
    public static String[] APP_ERR0008 = {"APP_ERR0008", "APP_ERR0008 : Many users founds with the same login [%s]."};

    /**
     * APP_ERR0010 : QR code [%s] already scanned in pallet N° [%s] ! Invalid QR
     * code format [%s]!
     */
    public static String[] APP_ERR0009 = {"APP_ERR0009", "APP_ERR0009 : Invalid QR code format [%s]!"};
    /**
     * QR code [%s] already scanned in pallet N° [%s] !
     */
    public static String[] APP_ERR0010 = {"APP_ERR0010", "APP_ERR0010 : QR code [%s] already scanned in pallet N° [%s] !"};
    /**
     * QR code [%s] doesn't match the part number [%s]
     */
    public static String[] APP_ERR0011 = {"APP_ERR0011", "APP_ERR0011 : QR code [%s] doesn't match the part number [%s] !"};
    /**
     * Invalid barecode scanned format [%s]
     */
    public static String[] APP_ERR0012 = {"APP_ERR0012", "APP_ERR0012 : Invalid barecode scanned format [%s] !"};

    /**
     * Barecode %s already scanned !
     */
    public static String[] APP_ERR0013 = {"APP_ERR0013", "APP_ERR0013 : Barecode %s already scanned !"};
    /**
     * Pallet opened in workstation %s and you use another workstation %s. You
     * should complete the scan in the same workstation %s!
     */
    public static String[] APP_ERR0014 = {"APP_ERR0014", "APP_ERR0014 : Pallet opened in workstation %s and you use another workstation %s. You should complete the scan in the same workstation %s!"};

    /**
     * Pallet number / code [%s] not correct to open the pallete!
     */
    public static String[] APP_ERR0015 = {"APP_ERR0015", "APP_ERR0015 : Pallet number / code [%s] not correct to open the pallete! \n-Scan the code %s to open a new palette.\n- Scanne an open pallet barecode to continue."};

    /**
     * Pallet/Box of type %s already open in this workstation, for part number
     * %s.
     */
    public static String[] APP_ERR0016 = {"APP_ERR0016", "APP_ERR0016 : Pallet/Box of type %s already open in this workstation, for part number %s. \n\n"
        + "Solution N° 1 : - Finish the packaging process of the pallet number %s before you can open a new one of type %s.\n"
        + "Solution N° 2 : - Open this pack type %s in another workstation."};

    /**
     * Pallet already open for part number %s..
     */
    public static String[] APP_ERR0017 = {"APP_ERR0017", "APP_ERR0017 : Pallet already open for part number %s."};

    /**
     * Invalid pallet number %s.
     */
    public static String[] APP_ERR0018 = {"APP_ERR0018", "APP_ERR0018 : Invalid pallet number %s."};

    /**
     * An error has occured during pallet creation for part number %s.\n
     * Creation returned code %s
     */
    public static String[] APP_ERR0019 = {"APP_ERR0019", "APP_ERR0019 : An error has occured during pallet creation for part number %s.\n Creation returned code %s"};
    /**
     * Invalid closing palette barcode [%s]!
     */
    public static String[] APP_ERR0020 = {"APP_ERR0020", "APP_ERR0020 : Invalid closing palette barcode [%s]!"};
    /**
     * No standard pack configuration found for part number [%s]!.
     */
    public static String[] APP_ERR0021 = {"APP_ERR0021", "APP_ERR0021 : No standard pack configuration found for part number [%s]!"};
    /**
     * Failed to found UCS matching criteria %s AND %s
     */
    public static String[] APP_ERR0022 = {"APP_ERR0022", "Failed to found standard pack config matching this criteria : \n- Internal part number [%s]\n- Index [%s} !"};
    /**
     * APP_ERR0023 DISPATCH : Load plan not empty.\nPlease delete all items
     * before deleting the plan.
     */
    public static String[] APP_ERR0023 = {"APP_ERR0023", "APP_ERR0023 DISPATCH : Load plan not empty.\nPlease delete all items before deleting the plan."};
    /**
     * APP_ERR0024 DISPATCH : Destination %s not empty.\nRemove associated items before delete the destination.
     */
    public static String[] APP_ERR0024 = {"APP_ERR0024", "APP_ERR0024 DISPATCH : Destination %s not empty.\nRemove associated items before delete the destination."};
    /**
     * APP_ERR0025 DISPATCH : No destination found for the dispatch module. Create destinations in the table 'load_plan_destination'.
     */
    public static String[] APP_ERR0025 = {"APP_ERR0025", "APP_ERR0025 DISPATCH : No destination found for the dispatch module. Create destinations in the table 'load_plan_destination'."};
    /**
     * APP_ERR0026 DISPATCH : No final destination selected.
     */
    public static String[] APP_ERR0026 = {"APP_ERR0026", "APP_ERR0026 DISPATCH : No final destination selected."};
    /**
     * APP_ERR0027 DISPATCH : No delivery date selected.
     */
    public static String[] APP_ERR0027 = {"APP_ERR0027", "APP_ERR0027 DISPATCH : No delivery date selected."};
    /**
     * APP_ERR0028 DISPATCH : Invalid date format.
     */
    public static String[] APP_ERR0028 = {"APP_ERR0028", "APP_ERR0028 DISPATCH : Invalid date format."};
    /**
     * APP_ERR0029 DISPATCH : No pallets/positions found for this destination %s.
     */
    public static String[] APP_ERR0029 = {"APP_ERR0029", "APP_ERR0029 DISPATCH : No pallets/positions found for this destination %s."};
    /**
     * APP_ERR0030 DISPATCH : No items associated with this load plan %s.
     */
    public static String[] APP_ERR0030 = {"APP_ERR0030", "APP_ERR0030 DISPATCH : No items associated with this load plan %s."};
    
    /**
     * APP_ERR0031 DISPATCH : No loading plan selected.
     */
    public static String[] APP_ERR0031 = {"APP_ERR0031", "APP_ERR0031 DISPATCH : No loading plan selected."};

}
