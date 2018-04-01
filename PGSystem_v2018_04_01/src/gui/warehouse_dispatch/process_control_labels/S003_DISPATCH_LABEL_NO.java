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
import static gui.warehouse_dispatch.state.WarehouseHelper.isDispatchSerialExist;
import helper.SoundHelper;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class S003_DISPATCH_LABEL_NO implements ControlState {

    private final BaseContainer bc;
    private String dispatchLabelNo;

    /**
     *
     * @param bc Container/Pallet object to be checked
     */
    public S003_DISPATCH_LABEL_NO(BaseContainer bc) {
        this.bc = bc;
        WarehouseHelper.Label_Control_Gui.setMessageLabel("Scanner le N° série de la fiche dispatch " + GlobalVars.DISPATCH_SERIAL_NO_PREFIX + "XXXXXXXXX.", 2);
        WarehouseHelper.Label_Control_Gui.setProgressValue(90);
    }

    @Override
    public void doAction(WarehouseControlContext context) {
        JTextField scan_txtbox = WarehouseHelper.Label_Control_Gui.getScanTxt();
        this.dispatchLabelNo = scan_txtbox.getText().trim();
        WarehouseHelper.Label_Control_Gui.setMessageLabel("", 0);
        if (!WarehouseHelper.checkDispatchLabelSerialNumFormat(this.dispatchLabelNo)) {
            SoundHelper.PlayErrorSound(null);
            String msg = "Format du numéro de série incorrecte. "+this.dispatchLabelNo;
            WarehouseHelper.Label_Control_Gui.setMessageLabel(msg, -1);
            JOptionPane.showOptionDialog(null, msg, "Erreur de format.", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
            clearScanBox(scan_txtbox);
        } else {
            //Checker si le numéro série scannée n'existe pas dans la table LoadPlanLine
            LoadPlanLine objResult = isDispatchSerialExist(this.dispatchLabelNo);
            if (objResult != null) {
                SoundHelper.PlayErrorSound(null);
                String msg = "Num. série expédition " + this.dispatchLabelNo + " déjà associé à la fiche production " + objResult.getPalletNumber() + ".";
                WarehouseHelper.Label_Control_Gui.setMessageLabel(msg, -1);
                JOptionPane.showOptionDialog(null, msg, "Erreur Num. série expédition déjà scanné.", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
                clearScanBox(scan_txtbox);
            } else {// BINGO !!!!
                //Asscocier la fiche production à la fiche expédition     
                Helper.startSession();
                Query query = Helper.sess.createQuery(HQLHelper.SET_LOAD_PLAN_LINE_PRODLABEL_TO_DISPATCHLABEL);
                query.setParameter("dispatchLabelNo", this.dispatchLabelNo);
                query.setParameter("palletNumber", bc.getPalletNumber());
                int result = query.executeUpdate();
                System.out.println(String.format("%s Line updated.", result));
                Helper.sess.getTransaction().commit();
                Helper.sess.clear();
                
                SoundHelper.PlayOkSound(null);
                clearScanBox(scan_txtbox);
                WarehouseHelper.Label_Control_Gui.setProgressValue(100);
                
                WarehouseHelper.Label_Control_Gui.setTxt_dispatchPN(this.dispatchLabelNo);
                WarehouseHelper.Label_Control_Gui.clearTextFields();
                S001_PalletNumberScan state = new S001_PalletNumberScan();
                context.setState(state);
            }
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
