/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch.process_control_labels;

import gui.warehouse_dispatch.process_reservation.WarehouseReservContext;
import __main__.GlobalVars;
import entity.BaseContainer;
import entity.LoadPlanLine;
import gui.warehouse_dispatch.WAREHOUSE_DISPATCH_UI0009_LABELS_CONTROL;
import helper.HQLHelper;
import helper.Helper;
import helper.SoundHelper;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.hibernate.Query;
import ui.UILog;
import gui.warehouse_dispatch.process_reservation.ReservationState;
import gui.warehouse_dispatch.state.WarehouseHelper;

/**
 *
 * @author user
 */
public class S001_PalletNumberScan implements ControlState {

    private BaseContainer bc;

    public S001_PalletNumberScan() {
        System.out.println("GlobalVars.CLOSING_PALLET_PREFIX" + GlobalVars.CLOSING_PALLET_PREFIX);
        WarehouseHelper.Label_Control_Gui.setMessageLabel("Scanner le N° série de la fiche production (eg: " + GlobalVars.CLOSING_PALLET_PREFIX + "XXXXXXXXX)", 2);
        WarehouseHelper.Label_Control_Gui.setProgressValue(10);
        
    }

    @Override
    public void doAction(WarehouseControlContext context) {
        JTextField scan_txtbox = WarehouseHelper.Label_Control_Gui.getScanTxt();
        String palletNum = scan_txtbox.getText().trim().substring(GlobalVars.CLOSING_PALLET_PREFIX.length());
        WarehouseHelper.Label_Control_Gui.setMessageLabel("", 0);
        if (scan_txtbox.getText().startsWith(GlobalVars.CLOSING_PALLET_PREFIX)) {
            Helper.startSession();
            Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_NUMBER);
            query.setParameter("palletNumber", palletNum);
            Helper.sess.getTransaction().commit();
            List result = query.list();

            if (result.isEmpty()) {
                SoundHelper.PlayErrorSound(null);
                String msg = "Numéro palette " + scan_txtbox.getText() + " introuvable !";
                WarehouseHelper.Label_Control_Gui.setMessageLabel(msg, -1);
                JOptionPane.showOptionDialog(null, msg, "Erreur du numéro palette.", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
                clearScanBox(scan_txtbox);
            } else {
                this.bc = (BaseContainer) result.get(0);
                if (!this.bc.getContainerState().equals(GlobalVars.PALLET_STORED)) {
                    SoundHelper.PlayErrorSound(null);
                    String msg = "Etat de la palette " + bc.getContainerState() + ". La palette " + this.bc.getPalletNumber() + " doit être à l'état " + GlobalVars.PALLET_STORED + ".";
                    WarehouseHelper.Label_Control_Gui.setMessageLabel(msg, -1);
                    JOptionPane.showOptionDialog(null, msg, "Erreur du statut palette.", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
                    clearScanBox(scan_txtbox);
                } else {
                    LoadPlanLine lp = isPalletAssociated(this.bc.getPalletNumber());
                    if (lp != null) {
                        SoundHelper.PlayErrorSound(null);
                        String msg = "Fiche palette " + this.bc.getPalletNumber() + " déjà associée à l'étiquette expédition N° " + lp.getDispatchLabelNo() + " - plan N° " + lp.getLoadPlanId()+" !";
                        WarehouseHelper.Label_Control_Gui.setMessageLabel(msg, -1);
                        JOptionPane.showOptionDialog(null, msg, "Erreur de statut palette.", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
                        clearScanBox(scan_txtbox);
                    } else {
                        //Prserver le pallet num dans une variable context
                        //Passer au statut S002_FORS_PN
                        WarehouseHelper.Label_Control_Gui.setTxt_productSerialNo(this.bc.getPalletNumber());
                        WarehouseHelper.Label_Control_Gui.setTxt_productPN(this.bc.getHarnessPart());
                        
                        SoundHelper.PlayOkSound(null);
                        clearScanBox(scan_txtbox);
                        
                        S002_FORS_PN state = new S002_FORS_PN(this.bc);
                        context.setState(state);
                    }
                }
            }
        } else {
            String msg = "Erreur du code palette." + scan_txtbox.getText() + ".";
            SoundHelper.PlayErrorSound(null);
            WarehouseHelper.Label_Control_Gui.setMessageLabel(msg, -1);
            JOptionPane.showOptionDialog(null, msg, "Erreur du code palette.", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
            clearScanBox(scan_txtbox);
        }
    }

    @Override
    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        WarehouseHelper.Label_Control_Gui.setScanTxt(scan_txtbox);
    }

    @Override
    public void clearContextSessionVals() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Check if this pallet Number is already associated with a dispatch label.
     *
     * @param palletNumber
     * @return Null if the pallet is not yet associated with dispatch label
     * String The dispatch label no
     */
    public LoadPlanLine isPalletAssociated(String palletNumber) {
        //Checker si la palette production déjà controlée from plan_labl_line DISPATCH_LABEL_NO != 0

        Helper.startSession();
        Helper.sess.getTransaction().begin();
        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_LINE_BY_PAL_NUM);
        query.setParameter("palletNumber", palletNumber);

        Helper.sess.getTransaction().commit();
        List result = query.list();
        LoadPlanLine l = (LoadPlanLine) result.get(0);
        System.out.println("l "+l.toString());
        System.out.println("l.getDispatchLabelNo() "+ l.getDispatchLabelNo());
        System.out.println("l.getDispatchLabelNo() "+l.getDispatchLabelNo().length());
        if (!"".equals(l.getDispatchLabelNo())) {
            return l;
        } else {
            return null;
        }

    }
}
