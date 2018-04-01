/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.config;

import __main__.GlobalMethods;
import __main__.GlobalVars;
import entity.ManufactureUsers;
import gui.packaging.PackagingVars;
import gui.packaging.mode2.state.Mode2_S010_UserCodeScan;
import helper.ComboItem;
import helper.HQLHelper;
import helper.Helper;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import ui.UILog;
import ui.info.InfoMsg;

/**
 *
 * @author Oussama EZZIOURI
 */
public class CONFIG_UI0000_AUTH extends javax.swing.JDialog {

    Frame parent;

    /**
     * Creates new form NewJDialog
     */
    public CONFIG_UI0000_AUTH(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public CONFIG_UI0000_AUTH() {
        menus_list = new JComboBox();

        initComponents();

        initMenusList();
        Helper.centerJDialog(this);
        this.setResizable(false);
    }

    public final void initMenusList() {
        for (int i = 0; i < GlobalVars.CONFIG_MENUS.size(); i++) {
            menus_list.addItem(new ComboItem(GlobalVars.CONFIG_MENUS.get(i), GlobalVars.CONFIG_MENUS.get(i)));
        }
    }

    public void clearPasswordBox() {
        //Vider le champs de text scan
        admin_password_txtbox.setText("");
    }

    private boolean checkLoginAndPass() {
        Helper.sess.beginTransaction();

        Query query = Helper.sess.createQuery(HQLHelper.CHECK_LOGIN_PASS);
        query.setParameter("password", String.valueOf(admin_password_txtbox.getPassword()));
        query.setParameter("login", admin_login_txtbox.getText());

        Helper.sess.getTransaction().commit();
        List result = query.list();
        if (!result.isEmpty()) {
            Helper.startSession();
            ManufactureUsers user = (ManufactureUsers) result.get(0);
            user.setLoginTime(new Date());
            PackagingVars.context.setUser(user);
            PackagingVars.context.getUser().update(PackagingVars.context.getUser());

            try {
                GlobalVars.APP_HOSTNAME = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Mode2_S010_UserCodeScan.class.getName()).log(Level.SEVERE, null, ex);
            }
//            String str = String.format(Helper.INFO0001_LOGIN_SUCCESS,
//                    user.getFirstName() + " " + user.getLastName()
//                    + " / " + user.getLogin(), GlobalVars.APP_HOSTNAME,
//                    GlobalMethods.getStrTimeStamp() + " Module : Planner");
//            Helper.log.log(Level.INFO, str);
            
            String str = String.format(InfoMsg.APP_INFO0003[1],
                user.getFirstName() + " " + user.getLastName()
                + " / " + user.getLogin(), GlobalVars.APP_HOSTNAME,
                GlobalMethods.getStrTimeStamp() + " Module : Configuration : ");
        
            UILog.info(str); 
            
            //Save authentication line in HisLogin table
//            HisLogin his_login = new HisLogin(
//                    user.getId(), user.getId(),
//                    String.format(Helper.INFO0001_LOGIN_SUCCESS,
//                            user.getFirstName() + " " + user.getLastName() + " / " + user.getLogin(),
//                            GlobalVars.APP_HOSTNAME, GlobalMethods.getStrTimeStamp()));
//            his_login.setCreateId(user.getId());
//            his_login.setWriteId(user.getId());
//            his_login.setMessage(str);
//            his_login.create(his_login);
            return true;
        }
        return false;
    }

    public void select_menu() {
        System.out.println("Selected menu [" + menus_list.getSelectedItem() + "]");
        //"Unités de conditionnement standard (UCS)",
        if (menus_list.getSelectedItem().toString().equals(GlobalVars.CONFIG_MENUS.get(1))) {//
            new CONFIG_UI0001_UCS_CONFIG(parent, true).setVisible(true);
            this.dispose();
        } //"Masque code à barre",
        else if (menus_list.getSelectedItem().toString().equals(GlobalVars.CONFIG_MENUS.get(2))) {
            new CONFIG_UI0001_BARCODE_CONFIG().setVisible(true);
            this.dispose();
        } //"Utilisateurs"
        else if (menus_list.getSelectedItem().toString().equals(GlobalVars.CONFIG_MENUS.get(3))) {
            new CONFIG_UI0003_USERS(parent, true).setVisible(true);
            this.dispose();

        } //"Planner"
        //        else if (menus_list.getSelectedItem().toString().equals(Helper.CONFIG_MENUS.get(4))) {
        //            this.parent.setState(JFrame.ICONIFIED);
        //            PLANNER_UI0001_Main planner = new PLANNER_UI0001_Main(null, null);
        //            planner.setVisible(true);
        //            planner.toFront();
        //            planner.repaint();
        //            this.dispose();
        //        }
        else if (menus_list.getSelectedItem().toString().equals(GlobalVars.CONFIG_MENUS.get(4))) { //packaging config
//            this.parent.setState(JFrame.ICONIFIED);
            CONFIG_UI0002_PACKAGING_CONFIG packaging_config = new CONFIG_UI0002_PACKAGING_CONFIG(null, false);
            packaging_config.setVisible(true);
            packaging_config.toFront();
            packaging_config.repaint();
            this.dispose();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        menus_list = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        admin_login_txtbox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        admin_password_txtbox = new javax.swing.JPasswordField();
        ok_btn1 = new javax.swing.JButton();
        ok_btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel3.setText("Menu");

        menus_list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menus_listActionPerformed(evt);
            }
        });

        jLabel2.setText("Login");

        admin_login_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                admin_login_txtboxActionPerformed(evt);
            }
        });
        admin_login_txtbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                admin_login_txtboxKeyPressed(evt);
            }
        });

        jLabel1.setText("Mot de passe");

        admin_password_txtbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                admin_password_txtboxKeyPressed(evt);
            }
        });

        ok_btn1.setText("Annuler");
        ok_btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_btn1ActionPerformed(evt);
            }
        });

        ok_btn.setText("OK");
        ok_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(113, 113, 113)
                        .addComponent(jLabel1))
                    .addComponent(jLabel3)
                    .addComponent(menus_list, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(ok_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(ok_btn))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(admin_login_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(admin_password_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menus_list, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(admin_login_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(admin_password_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ok_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menus_listActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menus_listActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menus_listActionPerformed

    private void admin_login_txtboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_admin_login_txtboxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            admin_password_txtbox.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_admin_login_txtboxKeyPressed

    private void admin_password_txtboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_admin_password_txtboxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (checkLoginAndPass()) {
                this.select_menu();
            } else {
                JOptionPane.showMessageDialog(null, Helper.ERR0001_LOGIN_FAILED, "Login Error", JOptionPane.ERROR_MESSAGE);
                admin_password_txtbox.setText("");
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_admin_password_txtboxKeyPressed

    private void ok_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_btn1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_ok_btn1ActionPerformed

    private void ok_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_btnActionPerformed
        //Authentification valide
        if (checkLoginAndPass()) {
            this.select_menu();
        } else {
            JOptionPane.showMessageDialog(null, Helper.ERR0001_LOGIN_FAILED, "Login Error", JOptionPane.ERROR_MESSAGE);
            admin_password_txtbox.setText("");
        }
    }//GEN-LAST:event_ok_btnActionPerformed

    private void admin_login_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_admin_login_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_admin_login_txtboxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CONFIG_UI0000_AUTH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CONFIG_UI0000_AUTH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CONFIG_UI0000_AUTH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CONFIG_UI0000_AUTH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CONFIG_UI0000_AUTH dialog = new CONFIG_UI0000_AUTH(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField admin_login_txtbox;
    private javax.swing.JPasswordField admin_password_txtbox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox menus_list;
    private javax.swing.JButton ok_btn;
    private javax.swing.JButton ok_btn1;
    // End of variables declaration//GEN-END:variables
}
