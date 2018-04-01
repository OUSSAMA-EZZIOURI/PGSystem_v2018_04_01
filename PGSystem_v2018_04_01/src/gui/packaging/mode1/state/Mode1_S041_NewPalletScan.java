/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode1.state;

import __main__.GlobalVars;
import com.itextpdf.text.DocumentException;
import gui.packaging.Mode1_Context;
import helper.Helper;
import helper.PrinterHelper;
import entity.BaseContainer;
import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecode;
import entity.BaseHarnessAdditionalBarecodeTmp;
import entity.BaseHarness;
import entity.HisBaseHarness;
import gui.packaging.PackagingVars;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import ui.UILog;

/**
 *
 * @author ezou1001
 */
public class Mode1_S041_NewPalletScan implements Mode1_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S041_NewPaletScan.jpg");

    public Mode1_S041_NewPalletScan() {

        PackagingVars.Packaging_Gui_Mode1.setIconLabel(this.imgIcon);

        //Reload Data Table to display new pallet
        PackagingVars.Packaging_Gui_Mode1.reloadDataTable();

        PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea("Scanner la fiche Ouverture Palette N° "
                + PackagingVars.mode1_context.getBaseContainerTmp().getPalletNumber());
    }

    public void doAction(Mode1_Context context) {
        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode1.getScanTxt();
        String barcode = scan_txtbox.getText().trim();

        System.out.println("Scanned Pallet Number " + barcode);
        System.out.println("Tempon Pallet Number " + PackagingVars.mode1_context.getBaseContainerTmp().getPalletNumber());

        if (barcode.equals(PackagingVars.mode1_context.getBaseContainerTmp().getPalletNumber())) {
            //Vide le scan box
            this.clearScanBox(scan_txtbox);

            //##############################################################
            //Inserer les données de la nouvelle palette dans les 2 tables BaseContainer et BaseHarness            
            BaseContainer bc = new BaseContainer().setDefautlVals();
            bc.setPalletNumber(context.getBaseContainerTmp().getPalletNumber());
            bc.setHarnessPart(context.getBaseContainerTmp().getHarnessPart().substring(1));
            bc.setHarnessIndex(context.getBaseContainerTmp().getHarnessIndex());
            bc.setSupplierPartNumber(context.getBaseContainerTmp().getSupplierPartNumber());
            bc.setQtyExpected(context.getBaseContainerTmp().getQtyExpected());
            bc.setPackType(context.getBaseContainerTmp().getPackType());
            bc.setQtyRead(1);
            bc.setHarnessType(context.getBaseContainerTmp().getHarnessType());
            bc.setStdTime(context.getBaseContainerTmp().getStdTime());
            bc.setContainerState(GlobalVars.PALLET_OPEN);
            bc.setContainerStateCode(GlobalVars.PALLET_OPEN_CODE);
            bc.setPackWorkstation(GlobalVars.APP_HOSTNAME);
            //bc.setAssyWorkstation(mode1_context.getBaseContainerTmp().getAssyWorkstation());
            bc.setSegment(context.getBaseContainerTmp().getSegment());
            bc.setWorkplace(context.getBaseContainerTmp().getWorkplace());
            bc.setUcsId(context.getBaseContainerTmp().getUcsId());
            bc.setUcsLifes(context.getBaseContainerTmp().getUcsLifes());
            bc.setComment(context.getBaseContainerTmp().getComment());
            bc.setOrder_no(context.getBaseContainerTmp().getOrder_no());
            bc.setSpecial_order(context.getBaseContainerTmp().getSpecial_order());
            bc.create(bc);
            UILog.info(String.format("BaseContainer created %s ", bc.toString()));
            //##############################################################            

            //##############################################################
            //Inserer les données du BaseHarness
            BaseHarness bh = new BaseHarness().setDefautlVals();
            bh.setHarnessPart(context.getBaseContainerTmp().getHarnessPart());
            bh.setCounter(context.getBaseContainerTmp().getHernessCounter());
            bh.setPalletNumber(context.getBaseContainerTmp().getPalletNumber());
            bh.setHarnessType(context.getBaseContainerTmp().getHarnessType());
            bh.setStdTime(context.getBaseContainerTmp().getStdTime());
            bh.setPackWorkstation(GlobalVars.APP_HOSTNAME);
            bh.setAssyWorkstation(context.getBaseContainerTmp().getAssyWorkstation());
            bh.setSegment(context.getBaseContainerTmp().getSegment());
            bh.setWorkplace(context.getBaseContainerTmp().getWorkplace());
            bh.setContainer(bc);

            //Insert the harness into the container
            bc.getHarnessList().add(bh);
            //##############################################################

            //##############################################################
            //Save harness History Line
            HisBaseHarness hbh = new HisBaseHarness().parseHarnessData(bh, "New harness added to pallet.");
            hbh.create(hbh);
            //##############################################################

            //############### SET & SAVE ENGINE LABEL DATA #################       
            //if (PackagingVars.context.getUser().getHarnessType().equals(Helper.ENGINE)) {
            if (PackagingVars.mode1_context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode().length != 0) {
                for (String labelCode : PackagingVars.mode1_context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode()) {
                    BaseHarnessAdditionalBarecode bel = new BaseHarnessAdditionalBarecode();
                    bel.setDefautlVals();
                    bel.setLabelCode(labelCode);
                    bel.setHarness(bh);
                    bel.create(bel);
                }
            }
            //}
            //##############################################################       
            //}
            //##############################################################

            //Close connection 
            //Clear session vals in mode1_context
            clearContextSessionVals();

            //Clear requested pallet txt box
            clearRequestedPallet_txt();

            //############## Check if pallet should be closed ##############
            //############## UCS Contains just 1 harness ###################
            if (bc.getQtyExpected() == 1) {
                try {
                    PrinterHelper.saveAndPrintClosingSheet(PackagingVars.mode1_context, bc, false);
                } catch (IOException ex) {
                    UILog.severe(ex.toString());
                } catch (DocumentException ex) {
                    UILog.severe(ex.toString());
                }
                Helper.startSession();
                bc.setContainerState(GlobalVars.PALLET_WAITING);
                bc.setContainerStateCode(GlobalVars.PALLET_WAITING_CODE);
                bc.update(bc);

                context.getBaseContainerTmp().setPalletNumber(bc.getPalletNumber());
                PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea("N° "
                        + GlobalVars.CLOSING_PALLET_PREFIX + PackagingVars.mode1_context.getBaseContainerTmp().getPalletNumber());

                context.setState(new Mode1_S050_ClosingPallet());
            } else {
                // Change go back to state HarnessPartScan            
                context.setState(new Mode1_S020_PalletChoice());
            }
        } else {
            Helper.log.warning(String.format("Line 146 " + Helper.ERR0008_INCORRECT_PALLET_NUMBER, barcode));
            JOptionPane.showMessageDialog(null, String.format("Line 146 " + Helper.ERR0008_INCORRECT_PALLET_NUMBER, barcode), "ERR0008 Invalid Barcode", JOptionPane.ERROR_MESSAGE);
            clearScanBox(scan_txtbox);
            context.setState(this);
        }
    }

    public void clearContextSessionVals() {
        //Pas besoin de réinitialiser le uid
        PackagingVars.mode1_context.setBaseContainerTmp(new BaseContainerTmp());
        PackagingVars.mode1_context.setBaseHarnessAdditionalBarecodeTmp(new BaseHarnessAdditionalBarecodeTmp());
    }

    public String toString() {
        return "State S041 : S041_NewPaletScan";
    }

    public ImageIcon getImg() {
        return this.imgIcon;
    }

    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        PackagingVars.Packaging_Gui_Mode1.setScanTxt(scan_txtbox);
    }

    public void clearRequestedPallet_txt() {
        PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea("");
    }

}
