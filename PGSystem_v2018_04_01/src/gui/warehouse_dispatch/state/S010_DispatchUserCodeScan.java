/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch.state;

import gui.warehouse_dispatch.process_reservation.WarehouseReservContext;
import __main__.GlobalVars;
import helper.Helper;
import helper.HQLHelper;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.hibernate.Query;
import gui.warehouse_dispatch.process_reservation.ReservationState;

/**
 *
 * @author ezou1001
 */
public class S010_DispatchUserCodeScan implements ReservationState {

    private ImageIcon imgIcon = new ImageIcon(
            GlobalVars.APP_PROP.getProperty("IMG_PATH")
            + File.separator
            + "WAREHOUSE_OUT"
            + File.separator
            + "S010_DispatchUserCodeScan.jpg");

    public S010_DispatchUserCodeScan() {
        //Charger l'image de l'état
        //WarehouseOutHelper.Dispatch_Gui.setIconLabel(this.imgIcon);
    }

    public String toString() {
        return "State S01 : S01_DispatchUserCodeScan";
    }

    public ImageIcon getImg() {
        return this.imgIcon;
    }

    public void doAction(WarehouseReservContext context) {

        JTextField scan_txtbox = WarehouseHelper.Dispatch_Gui.getScanTxt();

        System.out.println(scan_txtbox.getText());

        if (!scan_txtbox.getText().trim().equals("")) {
            //Start transaction
            Helper.startSession();

            Query query = Helper.sess.createQuery(HQLHelper.CHECK_LOGIN_ONLY);
            query.setParameter("login", scan_txtbox.getText().trim());
            Helper.sess.getTransaction().commit();
            List result = query.list();

            if (!result.isEmpty()) {
                System.out.println(result.size() + " user(s) found.");
                //new WAREHOUSE_DISPATCH_UI0003_PasswordRequest(null, true, (ManufactureUsers) result.get(0)).setVisible(true);

                //Clear login from barcode
                clearScanBox(scan_txtbox);
            } else {
                System.out.println("No users found.");
                JOptionPane.showMessageDialog(null, Helper.ERR0001_LOGIN_FAILED, "Login Error", JOptionPane.ERROR_MESSAGE);
                Helper.log.log(Level.INFO, Helper.ERR0001_LOGIN_FAILED);
                context.setState(this);
                clearScanBox(scan_txtbox);
            }
        } else {
            JOptionPane.showMessageDialog(null, Helper.ERR0001_LOGIN_FAILED, "Login Error", JOptionPane.ERROR_MESSAGE);
            Helper.log.log(Level.INFO, Helper.ERR0001_LOGIN_FAILED);
            context.setState(this);
            clearScanBox(scan_txtbox);
        }

    }

    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        WarehouseHelper.Dispatch_Gui.setScanTxt(scan_txtbox);
    }

    public void clearContextSessionVals() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
