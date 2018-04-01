/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode1.state;

import __main__.GlobalMethods;
import __main__.GlobalVars;
import com.itextpdf.text.DocumentException;
import gui.packaging.Mode1_Context;
import helper.Helper;
import helper.PrinterHelper;
import entity.BaseContainer;
import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecode;
import entity.BaseHarness;
import gui.packaging.PackagingVars;
import gui.packaging.mode1.gui.PACKAGING_UI9000_ChoosePackType_Mode1;
import helper.HQLHelper;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.hibernate.Query;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author ezou1001
 */
public class Mode1_S020_PalletChoice implements Mode1_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S040_PaletChoice.jpg");

    public Mode1_S020_PalletChoice() {
        PackagingVars.Packaging_Gui_Mode1.setIconLabel(this.imgIcon);
        //Reload container table content
        PackagingVars.Packaging_Gui_Mode1.reloadDataTable();
    }

    public void doAction(Mode1_Context context) {
        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode1.getScanTxt();
        String barcode = scan_txtbox.getText().trim();
        //Textbox is not empty
        if (!barcode.isEmpty()) {

            Helper.log.info("Is it a new pallet ?");
            if (barcode.equals(GlobalVars.OPEN_PALLET_KEYWORD)) {//NEWP barcode
                Helper.log.info(" [Yes]");

                //Vide le scan box
                this.clearScanBox(scan_txtbox);

                //Clear session vals in mode1_context
                //clearContextSessionVals();
                // Change go to HarnessPartScan                    
                PackagingVars.mode1_context.setState(new Mode1_S021_HarnessPartScan(true, null));
            } else {//Palette existante !
                //Charger les données de la palette
                BaseContainer bc = new BaseContainer().getBaseContainer(barcode);
                //Palette existe
                if (bc != null) {
                    if (!bc.getPackWorkstation().equals(GlobalVars.APP_HOSTNAME)) {
                        UILog.severe(ErrorMsg.APP_ERR0014[0], bc.getPackWorkstation(), GlobalVars.APP_HOSTNAME, bc.getPackWorkstation());
                        UILog.severeDialog(null, ErrorMsg.APP_ERR0014, bc.getPackWorkstation(), GlobalVars.APP_HOSTNAME, bc.getPackWorkstation());
                    } //Palette n'est pas ouverte
                    else if (!bc.getContainerState().equals(GlobalVars.PALLET_OPEN)) {
                        UILog.severe(ErrorMsg.APP_ERR0015[0]);
                        UILog.severeDialog(null, ErrorMsg.APP_ERR0015);
                        //Vide le scan box
                        this.clearScanBox(scan_txtbox);
                        //Retourner l'état actuel
                        context.setState(this);
                    } //Palette ouverte
                    else {
                        PackagingVars.mode1_context.setTempBC(bc);
                        //Palette existe et ouverte, Scanner la référence fx
                        //context.setState(new Mode1_S030_MatrixIdScan());
                        PackagingVars.mode1_context.setState(new Mode1_S021_HarnessPartScan(false, bc));
                        this.clearScanBox(scan_txtbox);
                    }
                } else { // Code palette introuvable !
                    Helper.log.warning(String.format(Helper.ERR0023_PALLET_NOT_FOUND, barcode));
                    JOptionPane.showMessageDialog(null, String.format(Helper.ERR0023_PALLET_NOT_FOUND, barcode), "Invalid Barcode", JOptionPane.ERROR_MESSAGE);
                    UILog.severe(ErrorMsg.APP_ERR0014[0], bc.getPackWorkstation(), GlobalVars.APP_HOSTNAME, bc.getPackWorkstation());
                    UILog.severeDialog(null, ErrorMsg.APP_ERR0014, bc.getPackWorkstation(), GlobalVars.APP_HOSTNAME, bc.getPackWorkstation());
                    //Vide le scan box
                    this.clearScanBox(scan_txtbox);
                    //Retourner l'état actuel
                    context.setState(this);
                }
            }
        } //############################### INVALID PALLET CODE #############
        else {
            Helper.log.warning(String.format(Helper.ERR0006_INVALID_OPEN_PALLET_BARCODE, barcode));
            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0006_INVALID_OPEN_PALLET_BARCODE, barcode), "Invalid Barcode", JOptionPane.ERROR_MESSAGE);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
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
        PackagingVars.Packaging_Gui_Mode1.setScanTxt(scan_txtbox);
    }

    public void clearContextSessionVals() {
        PackagingVars.mode1_context.setBaseContainerTmp(new BaseContainerTmp());
        PackagingVars.mode1_context.setLabelCount(0);
        GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST = new String[0][];
    }

}
