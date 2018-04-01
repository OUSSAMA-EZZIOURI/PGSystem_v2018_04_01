/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode1.state;

import __main__.GlobalVars;
import gui.packaging.Mode1_Context;
import entity.BaseContainer;
import helper.Helper;
import entity.BaseHarness;
import entity.ConfigUcs;
import gui.packaging.PackagingVars;
import gui.packaging.mode1.gui.PACKAGING_UI9000_ChoosePackType_Mode1;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author ezou1001
 */
public class Mode1_S021_HarnessPartScan implements Mode1_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S02_HarnessPartScan.jpg");

    private BaseContainer bc;

    public Mode1_S021_HarnessPartScan(boolean newPallet, BaseContainer bc) {

        PackagingVars.mode1_context.setNewPalette(newPallet);

        //Charger l'image de l'état
        PackagingVars.Packaging_Gui_Mode1.setIconLabel(this.imgIcon);

        //Reload container table content
        PackagingVars.Packaging_Gui_Mode1.reloadDataTable();

        if (bc != null) {

            this.bc = bc;

            System.out.println(bc.toString());
            if (!newPallet) {
                //Reload Session Harness Type

                //Changer le texte de l'interface principale
                PackagingVars.Packaging_Gui_Mode1.getRequestedPallet_label().setText(String.format("Scanner des pièces %s pour la palette %s ",
                        PackagingVars.mode1_context.getTempBC().getHarnessPart(),
                        PackagingVars.mode1_context.getTempBC().getPalletNumber()));

            }
            PackagingVars.mode1_context.getTempBC().setHarnessType(String.valueOf(PackagingVars.Packaging_Gui_Mode1.getHarnessTypeBox().getSelectedItem()));
        }
    }

    

    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        PackagingVars.Packaging_Gui_Mode1.setScanTxt(scan_txtbox);
    }

    @Override
    public void doAction(Mode1_Context context) {
        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode1.getScanTxt();
        String harnessPart = scan_txtbox.getText().trim();

        //################## Check harness Part Format #####################
        if (PackagingVars.mode1_context.getTempBC() == null) {
            //L'utilisateur n'a pas scanner aucune palette
            Helper.log.warning(Helper.ERR0008_NO_PALLET_SELECTED);
            JOptionPane.showMessageDialog(null, Helper.ERR0008_NO_PALLET_SELECTED, "Invalid Harness Part format !", JOptionPane.ERROR_MESSAGE);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else if (!BaseHarness.checkPartNumberFormat(harnessPart)) {
            Helper.log.warning(String.format(Helper.ERR0003_HP_FORMAT, harnessPart));
            JOptionPane.showMessageDialog(null, String.format(Helper.ERR0003_HP_FORMAT, harnessPart), "Invalid Harness Part format !", JOptionPane.ERROR_MESSAGE);
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);

        } //################## Check harness Part in UCS #####################
        else if (!ConfigUcs.isHarnessPartExist(harnessPart, PackagingVars.Packaging_Gui_Mode1.getHarnessTypeBox().getSelectedItem().toString())) {
            Helper.log.warning(String.format(Helper.ERR0016_HP_NOT_FOUND_IN_UCS_HARNESS_TYPE, harnessPart, PackagingVars.Packaging_Gui_Mode1.getHarnessTypeBox().getSelectedItem().toString()));
            JOptionPane.showMessageDialog(null,
                    String.format(Helper.ERR0016_HP_NOT_FOUND_IN_UCS_HARNESS_TYPE, harnessPart, PackagingVars.Packaging_Gui_Mode1.getHarnessTypeBox().getSelectedItem().toString()), "Invalid Harness Part format !", JOptionPane.ERROR_MESSAGE);

            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } //################## Harness part exist and format OK ##################
        else {//BINGO !!!!!
            Helper.log.log(Level.INFO, "Harness Part scanned [{0}] OK.", harnessPart);

            //Vide le scan box
            this.clearScanBox(scan_txtbox);

            //S'il s'agit d'une nouvelle palette, il faut afficher la liste des UCS
            if (PackagingVars.mode1_context.isNewPalette()) {
                PackagingVars.mode1_context.getTempBC().
                        setHarnessPart((harnessPart.startsWith(GlobalVars.HARN_PART_PREFIX) ? harnessPart.substring(1) : harnessPart));
                //Choisir le type d'UCS pour cette nouvelle palette
                new PACKAGING_UI9000_ChoosePackType_Mode1(null, true, PackagingVars.mode1_context.getTempBC().getHarnessPart());

            } //C'est une palette déjà créer mais la pièce scannée va être ajouter à la palette
            else if (PackagingVars.mode1_context.getTempBC().getHarnessPart().equals(harnessPart)) {
                //Passer au scanne de code QR
                Mode1_S030_MatrixIdScan state = new Mode1_S030_MatrixIdScan();
                PackagingVars.mode1_context.setState(state);
            }

        }
        //context.setState(state);
    }

    @Override
    public String toString() {
        return "S020_HarnessPartScan{" + "imgIcon=" + imgIcon + '}';
    }

    public ImageIcon getImg() {
        return this.imgIcon;
    }

    public void clearContextSessionVals() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
