/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import __main__.GlobalMethods;
import __main__.GlobalVars;
import __main__.PropertiesLoader;
import com.itextpdf.text.DocumentException;
import entity.BaseContainer;
import entity.HisGaliaPrint;
import entity.HisPalletPrint;
import gui.packaging.Context;
import gui.packaging.PackagingVars;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import org.hibernate.Query;
import print.PrintClosingPallet_A5;
import print.PrintDispatchNote_A4;
import print.PrintOpenPallet_A5;
import ui.UILog;

/**
 *
 * @author Administrator
 */
public class PrinterHelper {

    /**
     *
     * Init output print/log files and dirs Whene app is still open for J+1
     */
    public static void initDailyDestPrintDir() {
        PropertiesLoader.createDailyOutPrintDir(GlobalVars.APP_PROP.getProperty("PRINT_DIR"),
                GlobalVars.APP_PROP.getProperty("PRINT_PALLET_DIR"),
                GlobalVars.APP_PROP.getProperty("PRINT_CLOSING_PALLET_DIR"),
                GlobalVars.APP_PROP.getProperty("PRINT_PICKING_SHEET_DIR"),
                GlobalVars.APP_PROP.getProperty("PRINT_DISPATCH_SHEET_DIR")
        );
    }

    /**
     * @param hisPallet
     *
     * @return
     */
    public static int saveAndReprintOpenSheet(HisPalletPrint hisPallet) {

        try {
            initDailyDestPrintDir();
            //Creation of PDF A5 open pallet number
            PrintOpenPallet_A5 openPallet = new PrintOpenPallet_A5(
                    PackagingVars.context.getUser().getLogin(),
                    hisPallet.getPackType(),
                    GlobalVars.PALLET_PRINT_REPRINT,
                    hisPallet.getHarnessPart(),
                    hisPallet.getHarnessIndex(),
                    hisPallet.getSupplier_part_number(),
                    String.valueOf(hisPallet.getPackSize()),
                    String.valueOf(hisPallet.getId()),
                    String.valueOf(hisPallet.getWriteTimeString("yyyy-MM-dd")),
                    String.valueOf(hisPallet.getWriteTimeString("HH:mm:ss")));

            String filePath = openPallet.createPdf(0);
            System.out.println(String.format("Set Pallet [%d] state to [%s]", hisPallet.getId(), GlobalVars.PALLET_PRINT_INPROCESS));

            if (String.valueOf(GlobalVars.APP_PROP.get("PRINT_MODE")).equals("DesktopPrinter")) {
                //If printing has failed !
                if (!openPallet.sentToDefaultDesktopPrinter(filePath)) {
                    hisPallet.setPalletState(GlobalVars.PALLET_PRINT_ERROR, hisPallet.getId());
                    System.out.println(String.format("Set Pallet [%d] state to [%s]", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR));
                    JOptionPane.showMessageDialog(null, String.format(Helper.ERR0007_PRINTING_FAILED, filePath), "ERR0007 : Printing error !", ERROR_MESSAGE);
                    return -1;
                } else {
                    System.out.println(String.format("Set Pallet [%d] state to [%s]", hisPallet.getId(), GlobalVars.PALLET_PRINT_PRINTED));
                    hisPallet.setPalletReprint(GlobalVars.PALLET_PRINT_PRINTED, hisPallet.getId(), hisPallet.getWriteId());
                    return hisPallet.getId();
                }
            } else {
                hisPallet.setPalletState(GlobalVars.PALLET_PRINT_ERROR, hisPallet.getId());
                System.out.println(String.format("Set Pallet [%d] state to [%s]", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR));
                //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0013_UNKNOWN_PRINT_MODE, String.valueOf(GlobalVars.APP_PROP.get("PRINT_MODE"))), "ERR0013 : Printing error !", ERROR_MESSAGE);
                UILog.severe(String.format("Line 90 UNKNOWN_PRINT_MODE exception. Set Pallet [%d] state to [%s]. %s", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR));
                return -1;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            hisPallet.setPalletState(GlobalVars.PALLET_PRINT_ERROR, hisPallet.getId());            
            //Logger.getLogger(PrinterHelper.class.getName()).log(Level.SEVERE, null, ex);
            UILog.severe(String.format("Line 96 IOException exception. Set Pallet [%d] state to [%s]. %s", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR, ex.getMessage()));

        } catch (DocumentException ex) {
            System.out.println(ex.getMessage());
            hisPallet.setPalletState(GlobalVars.PALLET_PRINT_ERROR, hisPallet.getId());
            System.out.println(String.format("Set Pallet [%d] state to [%s]. %s", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR, ex.getMessage()));
            //Logger.getLogger(PrinterHelper.class.getName()).log(Level.SEVERE, null, ex);
            UILog.severe(String.format("Line 103 DocumentException exception. Set Pallet [%d] state to [%s]. %s", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR, ex.getMessage()));

        }
        return -1;
    }

    /**
     * Instert new object in hisPallet table
     *
     * @param context
     * @param harnessPart
     * @param harnessIndex
     * @param supplier_part_number
     * @param packType
     * @param packSize
     * @param user
     * @return
     */
    public static HisPalletPrint saveOpenSheet(
            Context context, String harnessPart, String harnessIndex, String supplier_part_number, String packType, int packSize, String user) {
        System.out.println("saveOpenSheet " + harnessPart + " " + harnessIndex + " " + supplier_part_number + " " + packType + " " + packSize + " " + user);
        //Save the new open pallet in DB with new state
        HisPalletPrint hisPallet = new HisPalletPrint(
                PackagingVars.context.getUser(),
                harnessPart,
                harnessIndex,
                supplier_part_number,
                packSize,
                packType,
                user,
                "-",
                GlobalVars.PALLET_PRINT_NEW);
        Helper.startSession();
        hisPallet.create(hisPallet);
        //Helper.sess.save(hisPallet);
        //Helper.sess.getTransaction().commit();
        System.out.println("Saved ! : hisPallet"+hisPallet.toString());
        return hisPallet;
    }

    /**
     * @param context
     * @param harnessPart
     * @param harnessIndex
     * @param supplier_part_number
     * @param packType
     * @param packSize
     * @param user
     * @return
     */
    public static int saveAndPrintOpenSheet(
            Context context, String harnessPart, String harnessIndex, 
            String supplier_part_number, String packType, int packSize, 
            String user) {

        System.out.println("saveAndPrintOpenSheet " + harnessPart + " " + harnessIndex + " " + supplier_part_number + " " + packType + " " + packSize + " " + user);
        initDailyDestPrintDir();

        HisPalletPrint hisPallet = saveOpenSheet(context, harnessPart, harnessIndex, supplier_part_number, packType, packSize, user);
        System.out.println("hisPallet "+hisPallet.toString());
        if (GlobalVars.APP_PROP.getProperty("PRINT_OPENING_SHEET").equals("1")) {
            //hisPallet.
            //Set pallet number var in global mode2_context            
            //GVars.mode2_context.getBaseContainerTmp().setPalletNumber(String.valueOf(hisPallet.getId()));
            try {
                hisPallet.setPalletState(GlobalVars.PALLET_PRINT_INPROCESS, hisPallet.getId());
                //Creation of PDF A5 open pallet number
                PrintOpenPallet_A5 openPallet = new PrintOpenPallet_A5(
                        context.getUser().getLogin(),
                        packType,
                        "-",
                        harnessPart,
                        harnessIndex,
                        supplier_part_number,
                        String.valueOf(packSize),
                        String.valueOf(hisPallet.getId()),
                        String.valueOf(hisPallet.getCreateTimeString("yyyy-MM-dd")),
                        String.valueOf(hisPallet.getCreateTimeString("HH:mm:ss")));
                String filePath = openPallet.createPdf(0);
                System.out.println("openPallet object "+openPallet.toString());
                System.out.println(String.format("Set Pallet [%d] state to [%s]", hisPallet.getId(), GlobalVars.PALLET_PRINT_INPROCESS));

                if (String.valueOf(GlobalVars.APP_PROP.get("PRINT_MODE")).equals("DesktopPrinter")) {
                    //If printing has failed !
                    if (!openPallet.sentToDefaultDesktopPrinter(filePath)) {
                        hisPallet.setPalletState(GlobalVars.PALLET_PRINT_ERROR, hisPallet.getId());
                        System.out.println(String.format("Set Pallet [%d] state to [%s]", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR));
                        JOptionPane.showMessageDialog(null, String.format(Helper.ERR0007_PRINTING_FAILED, filePath), "ERR0007 : Printing error !", ERROR_MESSAGE);
                        return -1;
                    } else {
                        hisPallet.setPalletState(GlobalVars.PALLET_PRINT_PRINTED, hisPallet.getId());
                        System.out.println(String.format("Set Pallet [%d] state to [%s]", hisPallet.getId(), GlobalVars.PALLET_PRINT_PRINTED));

                        return hisPallet.getId();
                    }
                } else {
                    hisPallet.setPalletState(GlobalVars.PALLET_PRINT_ERROR, hisPallet.getId());
                    System.out.println(String.format("Set Pallet [%d] state to [%s]", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR));
                    JOptionPane.showMessageDialog(null, String.format(Helper.ERR0013_UNKNOWN_PRINT_MODE, String.valueOf(GlobalVars.APP_PROP.get("PRINT_MODE"))), "ERR0013 : Printing error !", ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                hisPallet.setPalletState(GlobalVars.PALLET_PRINT_ERROR, hisPallet.getId());
                System.out.println(String.format("Set Pallet [%d] state to [%s]. %s", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR, ex.getMessage()));
                Logger.getLogger(PrinterHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                System.out.println(ex.getMessage());
                hisPallet.setPalletState(GlobalVars.PALLET_PRINT_ERROR, hisPallet.getId());
                System.out.println(String.format("Set Pallet [%d] state to [%s]. %s", hisPallet.getId(), GlobalVars.PALLET_PRINT_ERROR, ex.getMessage()));
                Logger.getLogger(PrinterHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return hisPallet.getId();
    }

    public static void saveAndPrintClosingSheet(Context ctx, BaseContainer bc, boolean reprint) throws IOException, DocumentException {
    
        System.out.println("saveAndPrintClosingSheet");
        System.out.println("ctx"+ ctx.toString());
        System.out.println("bc"+bc.toString());
        System.out.println("reprint"+reprint);
        //############ Impression de la fiche close pallet #################
        initDailyDestPrintDir();
        HisGaliaPrint hisGalia;
        //reprint=false. Create new closing sheet 
        if (!reprint) {
            //Save the new close pallet in DB with new state
            hisGalia = new HisGaliaPrint(bc.getHarnessPart(),
                    GlobalVars.APP_PROP.getProperty("SUPPLIER_NAME"),
                    bc.getSupplierPartNumber(),
                    bc.getHarnessIndex(),
                    bc.getQtyExpected(),
                    bc.getPalletNumber(),
                    GlobalVars.PALLET_PRINT_NEW,
                    "-");

            Helper.startSession();
            Helper.sess.save(hisGalia);
            Helper.sess.getTransaction().commit();

            //Set pallet number var in global mode2_context        
            ctx.getTempBC().setPalletNumber(String.valueOf(hisGalia.getId()));
        }//reprint = true, Update the old closing sheet
        else {
            //Get the hisGalia to be reprinted
            Query query = Helper.sess.createQuery(HQLHelper.GET_CLOSING_SHEET);
            query.setParameter("closingPallet", GlobalVars.CLOSING_PALLET_PREFIX + bc.getPalletNumber());
            Helper.sess.beginTransaction();
            Helper.sess.getTransaction().commit();
            List result = query.list();

            hisGalia = (HisGaliaPrint) result.get(0);
            hisGalia.setWriteTime(new Date());
            hisGalia.setWriteId(PackagingVars.context.getUser().getId());
            hisGalia.setReprint(GlobalVars.PALLET_PRINT_REPRINT);

            Helper.startSession();
            Helper.sess.update(hisGalia);
            Helper.sess.getTransaction().commit();
        }

        try {
            hisGalia.setGaliaState(GlobalVars.PALLET_PRINT_INPROCESS, hisGalia.getId());
            //Creation of PDF A5 open pallet number
            PrintClosingPallet_A5 closePallet = new PrintClosingPallet_A5(
                    bc.getHarnessPart(),
                    bc.getHarnessIndex(),
                    GlobalVars.QUANTITY_PREFIX + String.valueOf(bc.getQtyExpected()),
                    GlobalVars.CLOSING_PALLET_PREFIX + bc.getPalletNumber(),
                    GlobalVars.SUPPLIER_PART_PREFIX + bc.getSupplierPartNumber(),
                    GlobalVars.APP_PROP.getProperty("SUPPLIER_NAME"),
                    GlobalMethods.getStrTimeStamp(),
                    GlobalVars.APP_PROP.getProperty("WAREHOUSE_CODE"));

        String filePath = closePallet.createPdf(bc.getSpecial_order());
            System.out.println(String.format("Set Closing Pallet item [%d] state to [%s]", hisGalia.getId(), GlobalVars.PALLET_PRINT_INPROCESS));

            if (String.valueOf(GlobalVars.APP_PROP.get("PRINT_MODE")).equals("DesktopPrinter")) {
                //If printing has failed !
                System.out.println("filePath"+filePath);
                if (!closePallet.sentToDefaultDesktopPrinter(filePath)) {
                    hisGalia.setGaliaState(GlobalVars.PALLET_PRINT_ERROR, hisGalia.getId());
                    //System.out.println();
                    UILog.severe(String.format("Line 286 Set Closing Pallet item [%d] state to [%s]", hisGalia.getId(), GlobalVars.PALLET_PRINT_ERROR));
                    JOptionPane.showMessageDialog(null, String.format(Helper.ERR0007_PRINTING_FAILED, filePath), "ERR0007 : Printing error !", ERROR_MESSAGE);
                } else {
                    hisGalia.setGaliaState(GlobalVars.PALLET_PRINT_PRINTED, hisGalia.getId());
                    System.out.println(String.format("Set Closing Pallet [%d] state to [%s]", hisGalia.getId(), GlobalVars.PALLET_PRINT_PRINTED));
                }
            } else {//Unknown PRINT_MODE
                hisGalia.setGaliaState(GlobalVars.PALLET_PRINT_ERROR, hisGalia.getId());
                //System.out.println(String.format("Set Pallet [%d] state to [%s]", hisGalia.getId(), GlobalVars.PALLET_PRINT_ERROR));
                //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0013_UNKNOWN_PRINT_MODE, String.valueOf(GlobalVars.APP_PROP.get("PRINT_MODE"))), "ERR0013 : Printing error !", ERROR_MESSAGE);
                UILog.severe(String.format("Line 295 Set Pallet [%d] state to [%s].", hisGalia.getId(), GlobalVars.PALLET_PRINT_ERROR));
            }

        } catch (DocumentException | HeadlessException | IOException ex) {
            hisGalia.setGaliaState(GlobalVars.PALLET_PRINT_ERROR, hisGalia.getId());
            System.out.println(ex.getMessage());
            UILog.severe(String.format("Line 300 Set Pallet [%d] state to [%s].", hisGalia.getId(), GlobalVars.PALLET_PRINT_ERROR));
            //Logger.getLogger(PrinterHelper.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
    }

    /**
     *
     * @param user
     * @param loadPlanNum
     * @param createTime
     * @param noteLines
     * @return
     */
    public static int saveAndPrintDispatchNote(
            String loadPlanNum, String user, String createTime, Object[][] noteLines) {

        initDailyDestPrintDir();

        try {

            PrintDispatchNote_A4 dispatchNote = new PrintDispatchNote_A4(
                    loadPlanNum,
                    user,
                    createTime,
                    GlobalMethods.getStrTimeStamp(), //End Time                                        
                    noteLines);
            String filePath = dispatchNote.createPdf(0);

            if (String.valueOf(GlobalVars.APP_PROP.get("PRINT_MODE")).equals("DesktopPrinter")) {
                //If printing has failed !
                if (!dispatchNote.sentToDefaultDesktopPrinter(filePath)) {
                    System.out.println(String.format("Printing error !", GlobalVars.PALLET_PRINT_ERROR));
                    JOptionPane.showMessageDialog(null, String.format(Helper.ERR0007_PRINTING_FAILED, filePath), "ERR0007 : Printing error !", ERROR_MESSAGE);
                    return -1;
                } else {
                    System.out.println(String.format("Document printed."));
                    return 1;
                }
            } else {
                System.out.println(String.format("Printing error !", GlobalVars.PALLET_PRINT_ERROR));
                JOptionPane.showMessageDialog(null, String.format(Helper.ERR0007_PRINTING_FAILED, filePath), "ERR0007 : Printing error !", ERROR_MESSAGE);
                return -1;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(PrinterHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(PrinterHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

}
