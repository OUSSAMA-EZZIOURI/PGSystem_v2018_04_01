/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import __main__.GlobalVars;
import gui.packaging.Mode2_Context;
import helper.Helper;
import helper.HQLHelper;
import entity.ManufactureUsers;
import gui.packaging.PackagingVars;
import gui.packaging.mode2.gui.PACKAGING_UI0002_PasswordRequest_Mode2;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import org.hibernate.Query;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author ezou1001
 */
public class Mode2_S010_UserCodeScan implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S01_UserCodeScan.jpg");

    public Mode2_S010_UserCodeScan() {

    }

    @Override
    public String toString() {
        return "State S01 : S01_UserCodeScan";
    }

    @Override
    public ImageIcon getImg() {
        return this.imgIcon;
    }

    @Override
    public void doAction(Mode2_Context context) {

        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode2.getScanTxt();
        //String harnessType = String.valueOf(PackagingVars.Packaging_Gui_Mode2.getHarnessTypeBox().getSelectedItem());        

        if (!scan_txtbox.getText().trim().equals("")) {
            //Start transaction
            Helper.startSession();

            Query query = Helper.sess.createQuery(HQLHelper.CHECK_LOGIN_ONLY);
            query.setParameter("login", scan_txtbox.getText());
            //query.setParameter("active", 1); //active user only
            //query.setParameter("harnessType", harnessType);
            Helper.sess.getTransaction().commit();
            List result = query.list();

            //Helper.log.log(Level.INFO, Helper.ERR0001_LOGIN_FAILED);
            //.log(Level.INFO, Helper.ERR0001_LOGIN_FAILED);
            switch (result.size()) {
                case 0: //No user founds
                    //APP_ERR0004 : Invalid matricule number or account locked !
                    UILog.severeDialog(null, ErrorMsg.APP_ERR0004);
                    UILog.severe(ErrorMsg.APP_ERR0004[0]);
                    //Helper.log.log(Level.INFO, Helper.ERR0001_LOGIN_FAILED);
                    context.setState(this);
                    clearScanBox(scan_txtbox);
                    PackagingVars.Packaging_Gui_Mode2.HarnessTypeBoxSelectIndex(0);
                    break;
                case 1: //Good
                    new PACKAGING_UI0002_PasswordRequest_Mode2(null, true, (ManufactureUsers) result.get(0)).setVisible(true);

                    //Clear login from barcode
                    clearScanBox(scan_txtbox);

                    //Disable Project Box
                    PackagingVars.Packaging_Gui_Mode2.setHarnessTypeBoxState(false);
                    break;

                default:
                    UILog.severeDialog(null, ErrorMsg.APP_ERR0008, scan_txtbox.getText());
                    UILog.severe(ErrorMsg.APP_ERR0008[0], scan_txtbox.getText());
                    context.setState(this);
                    clearScanBox(scan_txtbox);
                    PackagingVars.Packaging_Gui_Mode2.HarnessTypeBoxSelectIndex(0);
                    break;
            }
        } else {
            
            UILog.severe(ErrorMsg.APP_ERR0004[0]);
            UILog.severeDialog(null, ErrorMsg.APP_ERR0004);
            context.setState(this);
            clearScanBox(scan_txtbox);
        }
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
