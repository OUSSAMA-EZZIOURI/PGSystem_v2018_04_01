/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import __main__.GlobalMethods;
import __main__.GlobalVars;
import com.itextpdf.text.DocumentException;
import gui.packaging.Mode2_Context;
import helper.Helper;
import helper.PrinterHelper;
import entity.BaseContainer;
import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecode;
import entity.BaseHarness;
import gui.packaging.mode2.gui.PACKAGING_UI9000_ChoosePackType_Mode2;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import gui.packaging.PackagingVars;
import java.io.IOException;
import org.hibernate.Query;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author ezou1001
 */
public class Mode2_S031_PalletChoice implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S040_PaletChoice.jpg");

    public Mode2_S031_PalletChoice() {
        PackagingVars.Packaging_Gui_Mode2.setIconLabel(this.imgIcon);
        //Reload container table content
        PackagingVars.Packaging_Gui_Mode2.reloadDataTable();
    }

    public void doAction(Mode2_Context context) {
        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode2.getScanTxt();
        String barcode = scan_txtbox.getText().trim();
        System.out.println("GlobalVars.APP_HOSTNAME " + GlobalVars.APP_HOSTNAME);

        //Textbox is not empty
        if (!barcode.isEmpty()) {
            BaseContainer bc = new BaseContainer().getBaseContainer(barcode);
            //######################### OPEN NEW PALLET ########################
            //Is it a new pallet ?
            if (barcode.equals(GlobalVars.OPEN_PALLET_KEYWORD)) {//NEWP barcode
                choosePack(scan_txtbox, context);
            } //####################################################
            else if (bc != null && !bc.getPackWorkstation().equals(GlobalVars.APP_HOSTNAME)) {

                UILog.severe(ErrorMsg.APP_ERR0014[0], bc.getPackWorkstation(), GlobalVars.APP_HOSTNAME, bc.getPackWorkstation());
                UILog.severeDialog(null, ErrorMsg.APP_ERR0014, bc.getPackWorkstation(), GlobalVars.APP_HOSTNAME, bc.getPackWorkstation());
                PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0014[0], bc.getPackWorkstation(), GlobalVars.APP_HOSTNAME, bc.getPackWorkstation()));

            } //# 1- If container exist 
            //# 2- Mode2_State is Open
            //# 3- Max Quantity not reached
            //# 4- Container Harness Part = Mode2_Context Harness Part  
            //# 5- Container Harness Type = Mode2_Context Harness Type
            else if (bc != null
                    && bc.getContainerState().equals(GlobalVars.PALLET_OPEN)
                    && bc.getQtyRead() < bc.getQtyExpected()
                    && (bc.getHarnessPart().equals(PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart().substring(1))
                    || bc.getHarnessPart().equals(PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart()))
                    && bc.getHarnessType().equals(PackagingVars.mode2_context.getBaseContainerTmp().getHarnessType())) {

                Helper.sess.beginTransaction();
                Helper.sess.persist(bc);
                bc.setWriteId(PackagingVars.context.getUser().getId());
                bc.setFifoTime(GlobalMethods.getTimeStamp(null));
                bc.setHarnessType(context.getBaseContainerTmp().getHarnessType());

                //#################### SET HARNESS DATA  #######################                                
                //- Set harness data from current mode2_context.                
                BaseHarness bh = new BaseHarness().setDefautlVals();
                bh = saveBaseHarness(bc, bh, barcode, context);
                //##############################################################

                //############### SET & SAVE ALL ENGINE LABELS DATA #################     
                //Si ce part number contient des code à barre pour sachet
                if (PackagingVars.mode2_context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode().length != 0) {
                    savePlaticBagCodes(bh, PackagingVars.mode2_context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode());
                }
                //##############################################################

                //############## ADD THE HARNESS TO THE CONTAINER ##############    
                //Insert the harness into the container
                bc.getHarnessList().add(bh);

                int newQty = bc.getQtyRead() + 1;
                //Incrémenter la taille du contenaire                
                bc.setQtyRead(bc.getQtyRead() + 1);
                bc.update(bc);
                clearScanBox(scan_txtbox);

                UILog.info("Deletion  counters result %d ", deletePieceFromDropTable(bh));

                bh.create(bh);
                //##############################################################

                //- Set harness data from drop table 
                //- Remouve it from drop table (use a flag var to drop it in the end 
                // of this condition)
                //- Create a history mouvement line in base_harness history
                //##############################################################
                //############## Check if pallet should be closed ##############
                //############## UCS Contains just 1 harness ###################
                if (bc.getQtyExpected() == newQty || bc.getQtyExpected() == 1) {
                    UILog.info("Quantité terminée %s", bc.toString());
                    try {
                        PrinterHelper.saveAndPrintClosingSheet(PackagingVars.mode2_context, bc, false);
                    } catch (IOException ex) {
                        UILog.severe(ex.toString());
                    } catch (DocumentException ex) {
                        UILog.severe(ex.toString());
                    }
                    //Helper.startSession();
                    bc.setContainerState(GlobalVars.PALLET_WAITING);
                    bc.setContainerStateCode(GlobalVars.PALLET_WAITING_CODE);
                    bc.update(bc);
                    PackagingVars.mode2_context.getBaseContainerTmp().setPalletNumber(bc.getPalletNumber());
                    //Set requested closing pallet number in the main gui
                    PackagingVars.Packaging_Gui_Mode2.setFeedbackTextarea("Scanner le code de fermeture palette N°\n "
                            + GlobalVars.CLOSING_PALLET_PREFIX + bc.getPalletNumber());
                    context.setState(new Mode2_S040_ClosingPallet());

                } else { //QtyExpected not reached yet ! Pallet will still open.

                    //Clear session vals in mode2_context
                    clearContextSessionVals();
                    // Change go back to state HarnessPartScan                    
                    context.setState(new Mode2_S020_HarnessPartScan());
                }
            } //############################### INVALID PALLET CODE #############
            else {
                UILog.severe(ErrorMsg.APP_ERR0015[0], barcode, GlobalVars.OPEN_PALLET_KEYWORD);
                UILog.severeDialog(null, ErrorMsg.APP_ERR0015, barcode, GlobalVars.OPEN_PALLET_KEYWORD);
                PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0015[0], barcode, GlobalVars.OPEN_PALLET_KEYWORD));
                //Vide le scan box
                clearScanBox(scan_txtbox);
                //Retourner l'état actuel
                context.setState(this);
            }
        } //############################### INVALID PALLET CODE #############
        else {
            UILog.severe(ErrorMsg.APP_ERR0015[0], barcode, GlobalVars.OPEN_PALLET_KEYWORD);
            UILog.severeDialog(null, ErrorMsg.APP_ERR0015, barcode, GlobalVars.OPEN_PALLET_KEYWORD);
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0015[0], barcode, GlobalVars.OPEN_PALLET_KEYWORD));
            //Vide le scan box
            clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        }
    }

    @Override
    public String toString() {
        return "State S04 : S040_PaletChoice";
    }

    public ImageIcon getImg() {
        return this.imgIcon;
    }

    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        PackagingVars.Packaging_Gui_Mode2.setScanTxt(scan_txtbox);
    }

    public void clearContextSessionVals() {
        PackagingVars.mode2_context.setBaseContainerTmp(new BaseContainerTmp());
        PackagingVars.mode2_context.setLabelCount(0);
        GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST = new String[0][];
    }

    /**
     * Load choosing pack pop up
     *
     * @param scan_txtbox
     * @param context
     */
    private void choosePack(JTextField scan_txtbox, Mode2_Context context) {
        //Vide le scan box
        clearScanBox(scan_txtbox);
        String hp;
        hp
                = (PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart()
                        .startsWith(GlobalVars.HARN_PART_PREFIX))
                ? context.getBaseContainerTmp().getHarnessPart().substring(1)
                : context.getBaseContainerTmp().getHarnessPart();

        Object o = new PACKAGING_UI9000_ChoosePackType_Mode2(null, true, hp);

    }

    /**
     * Delete the given piece from drop table
     *
     * @param bh
     * @return
     */
    private int deletePieceFromDropTable(BaseHarness bh) {
        //####### CHECK IF THE HARNESS EXISTS IN THE DROP TABLE ########
        //Yes                
        Query query = Helper.sess.createQuery("DELETE DropBaseHarness WHERE counter = :COUNTER");
        query.setParameter("COUNTER", bh.getCounter());

        return query.executeUpdate();

    }

    private void savePlaticBagCodes(BaseHarness bh, String[] labels) {
        for (String labelCode : PackagingVars.mode2_context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode()) {
            BaseHarnessAdditionalBarecode bel = new BaseHarnessAdditionalBarecode();
            bel.setDefautlVals();
            bel.setLabelCode(labelCode);
            bel.setHarness(bh);
            bel.create(bel);
        }
    }
    /**
     * Save a piece in container and return BaseHarness object
     * @param bc
     * @param bh
     * @param barcode
     * @param context
     * @return 
     */
    
    private BaseHarness saveBaseHarness(BaseContainer bc, BaseHarness bh, String barcode, Mode2_Context context) {
        bh.setHarnessPart(context.getBaseContainerTmp().getHarnessPart());
        bh.setCounter(context.getBaseContainerTmp().getHernessCounter());
        bh.setPalletNumber(barcode);
        bh.setHarnessType(context.getBaseContainerTmp().getHarnessType());
        bh.setStdTime(bc.getStdTime());
        bh.setPackWorkstation(GlobalVars.APP_HOSTNAME);
        bh.setSegment(bc.getSegment());
        bh.setWorkplace(bc.getWorkplace());
        bh.setContainer(bc);
        return bh;
    }

}
