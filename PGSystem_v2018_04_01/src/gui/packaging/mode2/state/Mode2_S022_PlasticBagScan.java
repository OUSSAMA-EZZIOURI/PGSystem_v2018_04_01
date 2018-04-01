/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import __main__.GlobalVars;
import gui.packaging.Mode2_Context;
import entity.BaseHarnessAdditionalBarecode;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import gui.packaging.PackagingVars;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author ezou1001
 */
public class Mode2_S022_PlasticBagScan implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S031_EngineLabelScan.jpg");

    //
    private int numberOfPatterns = 0;
    //
    private int patternIndex = 0;

    public Mode2_S022_PlasticBagScan(int numberOfPatterns, String[][] patternList) {
        this.numberOfPatterns = numberOfPatterns;
        PackagingVars.Packaging_Gui_Mode2.setIconLabel(this.imgIcon);
        //Reload container table content
        PackagingVars.Packaging_Gui_Mode2.reloadDataTable();

        //this.loadPlasticBagPattern();
        if (this.numberOfPatterns != 0 && this.patternIndex == 0) {
            String msg = String.format("Scanner code à barre N° %d / %d. %s ", this.patternIndex + 1, this.numberOfPatterns, patternList[this.patternIndex][1]);
            PackagingVars.Packaging_Gui_Mode2.setFeedbackTextarea(msg);
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(msg);
        }

    }

    public void doAction(Mode2_Context context) {
        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode2.getScanTxt();
        String engineLabel = scan_txtbox.getText().trim();
        BaseHarnessAdditionalBarecode bel = new BaseHarnessAdditionalBarecode();

        //Tester le format et l'existance et si le engineLabel concerne ce harness part
        if (!bel.checkLabelFormat(engineLabel)) {//Problème de format
            UILog.severe(ErrorMsg.APP_ERR0012[0], engineLabel);
            UILog.severeDialog(null, ErrorMsg.APP_ERR0012, engineLabel);
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0012[0], engineLabel));
            //Vide le scan box
            clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else if (bel.isLabelCodeExist(engineLabel)) {//Problème de doublant            
            UILog.severe(ErrorMsg.APP_ERR0013[0], engineLabel);
            UILog.severeDialog(null, ErrorMsg.APP_ERR0013, engineLabel);
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0013[0], engineLabel));
            //Vide le scan box
            clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else {//BINGO !! Label Code OK            
            //Boucler sur le nombre des codes à barre pour cette référence.
            if (this.patternIndex + 1 < this.numberOfPatterns) {
                clearScanBox(scan_txtbox);
                PackagingVars.mode2_context.getBaseHarnessAdditionalBarecodeTmp().setLabelCode(this.patternIndex, engineLabel);
                this.patternIndex++;
                String msg = String.format("Scanner code à barre N° %d / %d. %s ", this.patternIndex + 1, this.numberOfPatterns, GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST[this.patternIndex][1]);
                PackagingVars.Packaging_Gui_Mode2.setFeedbackTextarea(msg);
            } else { // Touts les patternes se sont scannés
                PackagingVars.mode2_context.getBaseHarnessAdditionalBarecodeTmp().setLabelCode(this.patternIndex, engineLabel);
                PackagingVars.Packaging_Gui_Mode2.setFeedbackTextarea("");
                clearScanBox(scan_txtbox);
                Mode2_S031_PalletChoice state = new Mode2_S031_PalletChoice();
                context.setState(state);
            }
        }

    }

    @Override
    public String toString() {
        return "S031_EngineLabelScan{" + "imgIcon=" + imgIcon + '}';
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
