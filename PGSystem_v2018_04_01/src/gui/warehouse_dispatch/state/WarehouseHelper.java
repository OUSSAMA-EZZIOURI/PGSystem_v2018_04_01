/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch.state;

import gui.warehouse_dispatch.process_reservation.WarehouseReservContext;
import entity.LoadPlan;
import entity.LoadPlanLine;
import gui.warehouse_dispatch.WAREHOUSE_DISPATCH_UI0002_DISPATCH_SCAN;
import gui.warehouse_dispatch.WAREHOUSE_DISPATCH_UI0009_LABELS_CONTROL;
import gui.warehouse_dispatch.process_control_labels.WarehouseControlContext;
import gui.warehouse_fg_reception.WAREHOUSE_FG_UI0001_SCAN;
import helper.HQLHelper;
import helper.Helper;
import helper.HibernateUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author user
 */
public class WarehouseHelper {

    //State Design pattern
    /**
     *
     */
    public static Session sess = HibernateUtil.getInstance().openSession();

    /**
     *
     */
    public static WarehouseReservContext warehouse_reserv_context = new WarehouseReservContext();

    /**
     *
     */
    public static WarehouseControlContext warehouse_control_context = new WarehouseControlContext();

    /**
     *
     */
    public static String[] DISPATCH_PN_LIST;

    /**
     *
     */
    public static String[] DISPATCH_SERIAL_NO_LIST;
    /**
     *
     */

    public static LoadPlan temp_load_plan = new LoadPlan();

    /**
     *
     */
    public static WAREHOUSE_DISPATCH_UI0002_DISPATCH_SCAN Dispatch_Gui;

    /**
     *
     */
    public static WAREHOUSE_DISPATCH_UI0009_LABELS_CONTROL Label_Control_Gui;

    public static WAREHOUSE_FG_UI0001_SCAN FinishGoodInput_Gui;

    /**
     * La liste des patterns dispatch
     */
    //public static Map<String, String> DISPATCH_PATTERN_LIST = new HashMap<String, String>();
    /**
     *
     */
    public static String ERR0001_ADVICE_NOTE_NUMBER_FORMAT = "Numéro BL invalide [%s].\nLe numéro BL ne doit commencé par N suivi des chiffres.";

    /**
     *
     */
    public static String ERR0002_PROD_HP_FORMAT = "Erreur Galia production. Référence faisceau invalide [%s]. !";

    /**
     *
     */
    public static String ERR0003_PROD_QTY_FORMAT = "Erreur Galia production. Quantité faisceau invalide [%s].\nLa quantité doit commencé par la lettre Q (ex: Q50) !";

    /**
     *
     */
    public static String ERR0004_FORS_HP_FORMAT = "Erreur Galia FORS. Référence faisceau invalide [%s]. !";

    /**
     *
     */
    public static String ERR0005_FORS_QTY_FORMAT = "Erreur Galia FORS. Quantité faisceau invalide [%s].\nLa quantité doit contenir que des chiffres !";

    /**
     *
     */
    public static String ERR0006_FORS_LABEL_ID = "Erreur Galia FORS. Numéro série invalide du Galia FORS [%s].\nLe N° de série doit contenir que des chiffres !";

    /**
     *
     */
    public static String ERR0007_DIFFERENT_ADVICE_NOTE_NUMBER = "Numéro BL différent à celui scanné au début [%s].";

    /**
     *
     */
    public static String ERR0008_DIFFERENT_HP_NUMBER = "Référence faisceau Galia production différente de celle du Galia FORS.";

    /**
     *
     */
    public static String ERR0009_DIFFERENT_QTY = "Quantité Galia production différente de celle du Galia FORS.";

    /**
     *
     */
    public static String ERR0010_FORS_LABEL_ALREADY_EXIST = "N° série Galia FORS déjà scanné. !";

    /**
     *
     */
    public static String ERR0011_ADVICE_NOTE_NUM_ALREADY_EXIST = "N° %s BL déjà scanné. !";

    /**
     *
     */
    public static String ERR0012_PROD_HP_NOT_EXIST = "Erreur Galia production. La référence [%s] est inéxistante dans la base de données !";

    public static String LOAD_PLAN_STATE_OPEN = "OPEN";
    public static String LOAD_PLAN_STATE_CLOSED = "CLOSED";

    /**
     *
     * @param adviceNotNum
     * @return
     */
//    public static Boolean checkAdviceNoteNumFormat(String adviceNotNum) {
//        //Tester le format du harness part        
//        if (!adviceNotNum.equals("") && adviceNotNum.matches(DISPATCH_PATTERN_LIST.get("DISPATCH_ADVICE_NOTE_NUM"))) {
//            return true;
//        } else {
//            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0003_HP_FORMAT), "Error", JOptionPane.ERROR_MESSAGE);
//            Helper.log.info(String.format(ERR0001_ADVICE_NOTE_NUMBER_FORMAT, adviceNotNum));
//            return false;
//        }
//    }
    /**
     *
     * @param pn
     * @return
     */
    public static Boolean checkDispatchPNFormat(String pn) {
        //Tester le format du harness part        

        for (String pattern : WarehouseHelper.DISPATCH_PN_LIST) {
            System.out.println("Comparing part number format " + pn + " and pattern " + pattern + " => " + pn.matches(pattern.trim()));
            if (pn.matches(pattern.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param dispatchNum
     * @return
     */
    public static Boolean checkDispatchLabelSerialNumFormat(String dispatchNum) {
        //Tester le format du numéro série expédition    
        for (String pattern : WarehouseHelper.DISPATCH_SERIAL_NO_LIST) {
            System.out.println("Comparing dispatch serial num format " + dispatchNum + " and pattern " + pattern + " => " + dispatchNum.matches(pattern.trim()));
            if (dispatchNum.matches(pattern.trim())) {
                return true;
            }
        }
        return false;
    }
    //    /**
    //     *
    //     * @param qty
    //     * @return
    //     */
    //    public static Boolean checkForsQtyFormat(String qty) {
    //        //Tester le format du harness part        
    //        if (!qty.equals("") && qty.matches(DISPATCH_PATTERN_LIST.get("DISPATCH_FORS_LABEL_QTY"))) {
    //            return true;
    //        } else {
    //            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0003_HP_FORMAT), "Error", JOptionPane.ERROR_MESSAGE);
    //            Helper.log.info(String.format(ERR0005_FORS_QTY_FORMAT, qty));
    //            return false;
    //        }
    //    }
    //    /**
    //     *
    //     * @param hp
    //     * @return
    //     */
    //    public static Boolean checkProdcutionHpFormat(String hp) {
    //        //Tester le format du harness part        
    //        if (!hp.equals("") && hp.matches(DISPATCH_PATTERN_LIST.get("DISPATCH_PROD_LABEL_HP"))) {
    //            return true;
    //        } else {
    //            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0003_HP_FORMAT), "Error", JOptionPane.ERROR_MESSAGE);
    //            Helper.log.info(String.format(ERR0002_PROD_HP_FORMAT, hp));
    //            return false;
    //        }
    //    }
    //    /**
    //     *
    //     * @param qty
    //     * @return
    //     */
    //    public static Boolean checkProdcutionQtyFormat(String qty) {
    //        //Tester le format du harness part        
    //        if (!qty.equals("") && qty.matches(DISPATCH_PATTERN_LIST.get("DISPATCH_PROD_LABEL_QTY"))) {
    //            return true;
    //        } else {
    //            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0003_HP_FORMAT), "Error", JOptionPane.ERROR_MESSAGE);
    //            Helper.log.info(String.format(ERR0003_PROD_QTY_FORMAT, qty));
    //            return false;
    //        }
    //    }

    /**
     *
     * @param adviceNote
     * @return
     */
    public static Boolean isAdviceNoteNumberExist(String adviceNoteNum) {
        //Tester si le harness counter exist dans la base BaseHarness        
        Helper.log.info("Searching advice note number [" + adviceNoteNum + "].");
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_DISPATCH_NOTE_BY_ADVICE_NOTE_NUM);
        query.setParameter("advice_note_num", adviceNoteNum);
        Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();

        if (query.list().size() == 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     *
     * @param dispatch_label_no
     * @return true if exist, false otherwise
     */
    public static LoadPlanLine isDispatchSerialExist(String dispatch_label_no) {
        //Tester si le harness counter exist dans la base BaseHarness                
        System.out.println("Searching dispatch label number [" + dispatch_label_no + "].");
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_LINE_BY_DISPATCH_LABEL_NO);
        query.setParameter("dispatch_label_no", dispatch_label_no);
        //Helper.log.info(query.getQueryString());
        System.out.println(query.getQueryString());
        Helper.sess.getTransaction().commit();

        if (!query.list().isEmpty()) {
            LoadPlanLine l = (LoadPlanLine) query.list().get(0);
            return l;
        } else {
            return null;
        }

    }

    //----------------------------- Date And Time ------------------------------
    /**
     *
     * @return
     */
    public static String getStrTimeStamp() {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    /**
     *
     * @param format : Patter of datetime example : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getStrTimeStamp(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    /**
     *
     * @param format
     * @return
     */
    public static Date getTimeStamp(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(reportDate);
        } catch (ParseException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     *
     * @param end
     * @param start
     * @return
     */
    public static long DiffInMinutes(Date end, Date start) {
        int duration = (int) (end.getTime() - start.getTime());
        long diffInMin = TimeUnit.MILLISECONDS.toMinutes(duration);
        return diffInMin;
    }
    //-------------------------- End Date And Time -----------------------------
}
