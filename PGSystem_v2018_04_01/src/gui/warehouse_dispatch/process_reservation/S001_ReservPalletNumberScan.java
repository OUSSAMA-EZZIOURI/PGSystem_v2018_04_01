/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch.process_reservation;

import __main__.GlobalVars;
import entity.BaseContainer;
import entity.LoadPlanLine;
import gui.warehouse_dispatch.state.WarehouseHelper;
import helper.HQLHelper;
import helper.Helper;
import helper.SoundHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.hibernate.Query;

/**
 *
 * @author user
 */
public class S001_ReservPalletNumberScan implements ReservationState {

    public S001_ReservPalletNumberScan() {
    }

    @Override
    public void doAction(WarehouseReservContext context) {
        JTextField scan_txtbox = WarehouseHelper.Dispatch_Gui.getScanTxt();
        String palletNum = "";
        System.out.println("Global.CLOSING_PALLET_PREFIX "+this.getClass().getSimpleName()+" "+GlobalVars.CLOSING_PALLET_PREFIX);
        WarehouseHelper.Dispatch_Gui.setMessageLabel("", 0);
        if (WarehouseHelper.temp_load_plan == null || !"OPEN".equals(WarehouseHelper.temp_load_plan.getPlanState())) {
            SoundHelper.PlayErrorSound(null);
            String msg = "Merci de selectionné un plan de chargement avec l'état OPEN !";
            WarehouseHelper.Dispatch_Gui.setMessageLabel(msg, -1);
            JOptionPane.showOptionDialog(null, msg, "Erreur plan de chargement", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
            clearScanBox(scan_txtbox);
        } else if (scan_txtbox.getText().startsWith(GlobalVars.CLOSING_PALLET_PREFIX)) {
            palletNum = scan_txtbox.getText().trim().substring(GlobalVars.CLOSING_PALLET_PREFIX.length());
            Helper.startSession();
            Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_NUMBER);
            query.setParameter("palletNumber", palletNum);
            Helper.sess.getTransaction().commit();
            List result = query.list();

            if (result.isEmpty()) {
                SoundHelper.PlayErrorSound(null);
                String msg = "Numéro palette "+scan_txtbox.getText()+" introuvable !\nErreur au niveau pile "+WarehouseHelper.Dispatch_Gui.getSelectedPileNum()+".";
                WarehouseHelper.Dispatch_Gui.setMessageLabel(msg, -1);
                JOptionPane.showOptionDialog(null, msg, "Pallet number not found.", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);                
                clearScanBox(scan_txtbox);
            } else {
                BaseContainer bc = (BaseContainer) result.get(0);
                if (!bc.getContainerState().equals(GlobalVars.PALLET_STORED)) {
                    SoundHelper.PlayErrorSound(null);
                    String msg = "Etat de la palette " + bc.getContainerState() + ". La palette doit être à l'état " + GlobalVars.PALLET_STORED + ".\nErreur au niveau pile "+WarehouseHelper.Dispatch_Gui.getSelectedPileNum()+".";
                    WarehouseHelper.Dispatch_Gui.setMessageLabel(msg, -1);
                    JOptionPane.showOptionDialog(null, msg, "Erreur du code palette.", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);                    
                    clearScanBox(scan_txtbox);
                } else if (checkIfLineExist(bc.getPalletNumber())) {
                    SoundHelper.PlayErrorSound(null);
                    String msg = "Fiche palette " + bc.getPalletNumber() + " déjà scannée !\nErreur au niveau pile "+WarehouseHelper.Dispatch_Gui.getSelectedPileNum()+".";
                    WarehouseHelper.Dispatch_Gui.setMessageLabel(msg, -1);
                    JOptionPane.showOptionDialog(null, msg, "Erreur de scanne palette.", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);                    
                    clearScanBox(scan_txtbox);
                } else if (WarehouseHelper.Dispatch_Gui.getSelectedPileNum().equals("*")) {
                    SoundHelper.PlayErrorSound(null);
                    String msg = "Numéro de pile invalide ! \nErreur au niveau pile "+WarehouseHelper.Dispatch_Gui.getSelectedPileNum()+".";
                    WarehouseHelper.Dispatch_Gui.setMessageLabel(msg, -1);
                    JOptionPane.showOptionDialog(null, msg, "Erreur de scanne palette.", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
                    clearScanBox(scan_txtbox);
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String hp = "";
                        if (bc.getHarnessPart().startsWith(GlobalVars.HARN_PART_PREFIX)) {
                            hp = bc.getHarnessPart().substring(1);
                        } else {
                            hp = bc.getHarnessPart();
                        }
                        LoadPlanLine line = new LoadPlanLine(
                                new Date(),
                                sdf.parse(WarehouseHelper.Dispatch_Gui.getPlanDispatchTime()),
                                WarehouseHelper.warehouse_reserv_context.getUser().getId(),
                                WarehouseHelper.warehouse_reserv_context.getUser().getFirstName() + " " + WarehouseHelper.warehouse_reserv_context.getUser().getLastName(),
                                Integer.valueOf(WarehouseHelper.Dispatch_Gui.getSelectedPileNum()),
                                bc.getHarnessType(),
                                bc.getPalletNumber(),
                                hp,
                                bc.getHarnessIndex(),
                                bc.getSupplierPartNumber(),
                                bc.getQtyRead(),
                                bc.getOrder_no(),
                                bc.getPackType(),
                                WarehouseHelper.Dispatch_Gui.getSelectedDestination(),
                                bc.getStdTime(),
                                bc.getPrice(),
                                ""
                        );

                        line.setLoadPlanId(Integer.valueOf(WarehouseHelper.Dispatch_Gui.getPlanNum()));

                        Helper.sess.beginTransaction();
                        line.create(line);

                        clearScanBox(scan_txtbox);
//                        WarehouseHelper.Dispatch_Gui.reloadPlanLinesData(
//                                Integer.valueOf(WarehouseHelper.Dispatch_Gui.getPlanNum()),
//                                WarehouseHelper.Dispatch_Gui.getSelectedDestination());
//                        
                        WarehouseHelper.Dispatch_Gui.filterPlanLines(false);
                        SoundHelper.PlayOkSound(null);
                        WarehouseHelper.Dispatch_Gui.setMessageLabel("OK - Element ajouté", 1);
                    } catch (ParseException ex) {
                        Logger.getLogger(S001_ReservPalletNumberScan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            String msg = "Erreur du code palette." + scan_txtbox.getText()+"\nErreur au niveau pile "+WarehouseHelper.Dispatch_Gui.getSelectedPileNum()+".";
            SoundHelper.PlayErrorSound(null);
            WarehouseHelper.Dispatch_Gui.setMessageLabel(msg, -1);
            JOptionPane.showOptionDialog(null, msg, "Erreur du code palette.", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);            
            clearScanBox(scan_txtbox);
        }
    }

    @Override
    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        WarehouseHelper.Dispatch_Gui.setScanTxt(scan_txtbox);
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