/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch.process_control_labels;

import __main__.GlobalVars;
import entity.BaseContainer;
import entity.LoadPlanLine;
import helper.HQLHelper;
import helper.Helper;
import java.util.List;
import javax.swing.JTextField;

import org.hibernate.Query;
import gui.warehouse_dispatch.state.WarehouseHelper;
import helper.SoundHelper;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class S002_FORS_PN implements ControlState {

    private BaseContainer bc;
    private String pnProduction;
    private String pnDispatch;

    /**
     *
     * @param bc Container/Pallet object to be checked
     */
    public S002_FORS_PN(BaseContainer bc) {
        this.bc = bc;
        this.pnProduction = (bc.getHarnessPart().startsWith(GlobalVars.HARN_PART_PREFIX)) ? bc.getHarnessPart().substring(1) : bc.getHarnessPart();
        WarehouseHelper.Label_Control_Gui.setMessageLabel("Scanner la référence produit de la fiche expédition (eg: " + GlobalVars.DISPATCH_PN_PREFIX + "XXXXXXXX).", 2);
        WarehouseHelper.Label_Control_Gui.setProgressValue(50);
    }

    @Override
    public void doAction(WarehouseControlContext context) {
        JTextField scan_txtbox = WarehouseHelper.Label_Control_Gui.getScanTxt();
        this.pnDispatch = (scan_txtbox.getText().trim().startsWith(GlobalVars.HARN_PART_PREFIX)) ? scan_txtbox.getText().trim().substring(1) : bc.getHarnessPart().trim();
        if (!WarehouseHelper.checkDispatchPNFormat(scan_txtbox.getText().trim())) {
            SoundHelper.PlayErrorSound(null);
            String msg = "Référence scannée inconnue. Vérifier le code scanné ou la configuration des formats.";
            WarehouseHelper.Label_Control_Gui.setMessageLabel(msg, -1);
            JOptionPane.showOptionDialog(null, msg, "Erreur de référence inconnue.", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
            clearScanBox(scan_txtbox);

        } else if (!bc.getHarnessPart().equals(scan_txtbox.getText().trim())
                && !bc.getHarnessPart().equals(scan_txtbox.getText().trim().substring(1))) { // Check the bc.PN and scanned part number
            SoundHelper.PlayErrorSound(null);
            
            String msg = "Référence production et expédition incompatible: " + this.pnProduction + " # " + this.pnDispatch;
            WarehouseHelper.Label_Control_Gui.setMessageLabel(msg, -1);
            JOptionPane.showOptionDialog(null, msg, "Erreur de référence incompatibles.", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
            clearScanBox(scan_txtbox);
        } else {
            //Passer au statut S003_DISPATCH_LABEL_NO
            SoundHelper.PlayOkSound(null);
            clearScanBox(scan_txtbox);
            
            WarehouseHelper.Label_Control_Gui.setTxt_dispatchPN(this.pnDispatch);
            
            S003_DISPATCH_LABEL_NO state = new S003_DISPATCH_LABEL_NO(this.bc);
            context.setState(state);
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
     * Check if this pallet Number is already read.
     *
     * @param palletNumber
     * @return
     */
    public boolean checkIfLineExist(String palletNumber) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_LINE_BY_PAL_NUM);
        query.setParameter("palletNumber", palletNumber);

        Helper.sess.getTransaction().commit();
        List result = query.list();

        if (!result.isEmpty()) {
            return true;
        }
        return false;
    }
}
