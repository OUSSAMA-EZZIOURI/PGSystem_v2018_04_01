/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode1.state;

import __main__.GlobalVars;
import gui.packaging.Mode1_Context;
import entity.BaseHarnessAdditionalBarecode;
import gui.packaging.PackagingVars;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author ezou1001
 */
public class Mode1_S031_PlasticBagScan implements Mode1_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S031_EngineLabelScan.jpg");

    //
    private int numberOfPatterns = 0;
    //
    private int patternIndex = 0;

    public Mode1_S031_PlasticBagScan(int numberOfPatterns, String[][] patternList) {
        this.numberOfPatterns = numberOfPatterns;
        PackagingVars.Packaging_Gui_Mode1.setIconLabel(this.imgIcon);
        //Reload container table content
        PackagingVars.Packaging_Gui_Mode1.reloadDataTable();

        //this.loadPlasticBagPattern();
        if (this.numberOfPatterns != 0 && this.patternIndex == 0) {
            PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea(String.format("Scanner code à barre N° %d / %d. %s ", this.patternIndex + 1, this.numberOfPatterns, patternList[this.patternIndex][1]));
        }

    }

    public void doAction(Mode1_Context context) {
        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode1.getScanTxt();
        String labelCode = scan_txtbox.getText().trim();
        BaseHarnessAdditionalBarecode bel = new BaseHarnessAdditionalBarecode();

        //Tester le format et l'existance et si le engineLabel concerne ce harness part
        if (!bel.checkLabelFormat(labelCode)) {//Problème de format
            UILog.severeDialog(null, ErrorMsg.APP_ERR0012, "" + labelCode);
            UILog.severe(ErrorMsg.APP_ERR0012[0], "" + labelCode);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else if (bel.isLabelCodeExist(labelCode)) {//Problème de doublant            
            UILog.severeDialog(null, ErrorMsg.APP_ERR0013, "" + labelCode);
            UILog.severe(ErrorMsg.APP_ERR0013[0], "" + labelCode);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else {//BINGO !! Label Code OK            
            //Boucler sur le nombre des codes à barre pour cette référence.
            if (this.patternIndex + 1 < this.numberOfPatterns) {
                this.clearScanBox(scan_txtbox);
                PackagingVars.mode1_context.getBaseHarnessAdditionalBarecodeTmp().setLabelCode(this.patternIndex, labelCode);
                UILog.info("First Valid Engine Label scanned [" + labelCode + "] OK.");
                this.patternIndex++;
                PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea(String.format("Scanner le code à barre sachet N° %d / %d. %s ", this.patternIndex + 1, this.numberOfPatterns, GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST[this.patternIndex][1]));
            } else { // Touts les patternes se sont scannés
                PackagingVars.mode1_context.getBaseHarnessAdditionalBarecodeTmp().setLabelCode(this.patternIndex, labelCode);
                PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea("");
                this.clearScanBox(scan_txtbox);
                Mode1_S020_PalletChoice state = new Mode1_S020_PalletChoice();
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
        PackagingVars.Packaging_Gui_Mode1.setScanTxt(scan_txtbox);
    }

    public void clearContextSessionVals() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
