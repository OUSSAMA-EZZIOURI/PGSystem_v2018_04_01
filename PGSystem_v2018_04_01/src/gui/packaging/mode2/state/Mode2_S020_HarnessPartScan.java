/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import __main__.GlobalVars;
import gui.packaging.Mode2_Context;
import entity.BaseHarness;
import entity.ConfigUcs;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import gui.packaging.PackagingVars;
import ui.UILog;
import ui.error.ErrorMsg;
import ui.info.InfoMsg;

/**
 *
 * @author ezou1001
 */
public class Mode2_S020_HarnessPartScan implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S02_HarnessPartScan.jpg");
    private String msg = "";

    /**
     *
     */
    public Mode2_S020_HarnessPartScan() {
        //Charger l'image de l'état
        PackagingVars.Packaging_Gui_Mode2.setIconLabel(this.imgIcon);
        //Reload container table content
        PackagingVars.Packaging_Gui_Mode2.reloadDataTable();
        //Reload Session Harness Type
        PackagingVars.mode2_context.getBaseContainerTmp().setHarnessType(String.valueOf(PackagingVars.Packaging_Gui_Mode2.getHarnessTypeBox().getSelectedItem()));
    }

    /**
     *
     * @param context
     */
    @Override
    public void doAction(Mode2_Context context) {
        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode2.getScanTxt();
        String harnessPart = scan_txtbox.getText().trim();
        //################## Check harness Part Format #####################
        if (!BaseHarness.checkPartNumberFormat(harnessPart)) {
            //Invalid part number scanned [%s] !.
            UILog.severe(ErrorMsg.APP_ERR0006[0], harnessPart);
            UILog.severeDialog(null, ErrorMsg.APP_ERR0006, harnessPart);
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0006[0], harnessPart, PackagingVars.context.getUser().getHarnessType()));
            //Vide le scan box
            clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } //################## Check harness Part in UCS #####################
        else if (!ConfigUcs.isHarnessPartExist(harnessPart, PackagingVars.context.getUser().getHarnessType())) {
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0007[0], harnessPart, PackagingVars.context.getUser().getHarnessType()));
            UILog.severe(ErrorMsg.APP_ERR0007[0], harnessPart, PackagingVars.context.getUser().getHarnessType());
            UILog.severeDialog(null, ErrorMsg.APP_ERR0007, harnessPart, PackagingVars.context.getUser().getHarnessType());
            //Vide le scan box
            clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } //################## Harness part exist and format OK ##################
        else {//BINGO !!!!!            
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.info(InfoMsg.APP_INFO0005[0], harnessPart));
            PackagingVars.mode2_context.getBaseContainerTmp().setHarnessPart(harnessPart);
            //Vide le scan box
            clearScanBox(scan_txtbox);
            //Passer à l état 3. Scan du compteur, code QR
            Mode2_S021_MatrixIdScan state = new Mode2_S021_MatrixIdScan();
            context.setState(state);
        }
    }

    @Override
    public String toString() {
        return "S020_HarnessPartScan{" + "imgIcon=" + imgIcon + '}';
    }

    /**
     *
     * @return
     */
    public ImageIcon getImg() {
        return this.imgIcon;
    }

    /**
     *
     * @param scan_txtbox
     */
    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        PackagingVars.Packaging_Gui_Mode2.setScanTxt(scan_txtbox);
    }

    /**
     *
     */
    public void clearContextSessionVals() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
