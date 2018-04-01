/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch;

import entity.ConfigBarcode;
import gui.warehouse_dispatch.process_control_labels.ControlState;
import gui.warehouse_dispatch.process_control_labels.S001_PalletNumberScan;
import gui.warehouse_dispatch.state.WarehouseHelper;
import helper.HQLHelper;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTextField;
import org.hibernate.Query;
import helper.Helper;
import javax.swing.JFrame;
import ui.UILog;

/**
 *
 * @author Oussama EZZIOURI
 */
public class WAREHOUSE_DISPATCH_UI0009_LABELS_CONTROL extends javax.swing.JFrame {

    private String frameTitle = "Contrôle des étiquettes export";
    public ControlState state = null;//WarehouseHelper.warehouse_control_context.getState();

    /**
     * Creates new form WAREHOUSE_DISPATCH_UI0009_LABELS_CONTROL
     */
    public WAREHOUSE_DISPATCH_UI0009_LABELS_CONTROL() {
        initComponents();
        this.setTitle(frameTitle);
        scan_textbox.requestFocus();
        //Maximaze the jframe
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        loadDispatchPNPatterns();
        loadDispatchSerialNoPatterns();
    }

    /**
     *
     */
    private void loadDispatchPNPatterns() {
        System.out.println("Loading DISPATCH_PN pattern list ... ");
        Query query = Helper.sess.createQuery(HQLHelper.GET_PATTERN_BY_KEYWORD);
        query.setParameter("keyWord", "DISPATCH_PN");
        UILog.info(query.getQueryString());
        Helper.sess.beginTransaction();
        Helper.sess.getTransaction().commit();
        List resultList = query.list();
        WarehouseHelper.DISPATCH_PN_LIST = new String[query.list().size()];

        System.out.println("DISPATCH_PN_LIST pattern list...");
        int i = 0;
        for (Iterator it = resultList.iterator(); it.hasNext();) {
            ConfigBarcode object = (ConfigBarcode) it.next();
            WarehouseHelper.DISPATCH_PN_LIST[i] = object.getBarcodePattern();
            System.out.println(WarehouseHelper.DISPATCH_PN_LIST[i]);
            i++;
        }

        System.out.println(WarehouseHelper.DISPATCH_PN_LIST.length + " DISPATCH_PN_LIST pattern successfuly loaded 100% ! ");

    }

    /**
     *
     */
    private void loadDispatchSerialNoPatterns() {
        System.out.println("Loading DISPATCH_SERIAL_NO pattern list ... ");
        Query query = Helper.sess.createQuery(HQLHelper.GET_PATTERN_BY_KEYWORD);
        query.setParameter("keyWord", "DISPATCH_SERIAL_NO");
        UILog.info(query.getQueryString());
        Helper.sess.beginTransaction();
        Helper.sess.getTransaction().commit();
        List resultList = query.list();
        WarehouseHelper.DISPATCH_SERIAL_NO_LIST = new String[query.list().size()];

        System.out.println("DISPATCH_SERIAL_NO pattern list...");
        int i = 0;
        for (Iterator it = resultList.iterator(); it.hasNext();) {
            ConfigBarcode object = (ConfigBarcode) it.next();
            WarehouseHelper.DISPATCH_SERIAL_NO_LIST[i] = object.getBarcodePattern();
            System.out.println(WarehouseHelper.DISPATCH_SERIAL_NO_LIST[i]);
            i++;
        }

        System.out.println(WarehouseHelper.DISPATCH_SERIAL_NO_LIST.length + " DISPATCH_SERIAL_NO_LIST pattern successfuly loaded 100% ! ");

    }

    @Override
    public void setVisible(boolean b) {
        System.out.println("Current State = " + state.toString());
        super.setVisible(b);
    }

    public void setState(ControlState state) {
        this.state = state;
    }

    public JTextField getScanTxt() {
        return this.scan_textbox;
    }

    public void setScanTxt(JTextField setScanTxt) {
        this.scan_textbox = setScanTxt;
    }

    /**
     *
     * @param msg : String to be displayed
     * @param type : 2 for info, 1 for OK , -1 for error, 0 to clean the label
     */
    public void setMessageLabel(String msg, int type) {

        switch (type) {
            case -1:
                message_label.setBackground(Color.white);
                message_label.setForeground(Color.red);
                message_label.setText(msg);
                break;
            case 1:
                message_label.setBackground(Color.green);
                message_label.setForeground(Color.black);
                message_label.setText(msg);
                break;
            case 2:
                message_label.setBackground(Color.white);
                message_label.setForeground(Color.blue);
                message_label.setText(msg);
                break;
            case 0:
                message_label.setBackground(Color.WHITE);
                message_label.setText("");
                break;
        }

    }

    public void setProgressValue(int n) {
        this.progressBar.setValue(n);
    }

    public void setTxt_dispatchPN(String txt_dispatchPN) {
        this.txt_dispatchPN.setText(txt_dispatchPN);
    }

    public void setTxt_dispatchSerialNo(String txt_dispatchSerialNo) {
        this.txt_dispatchSerialNo.setText(txt_dispatchSerialNo);
    }

    public void setTxt_productPN(String txt_productPN) {
        this.txt_productPN.setText(txt_productPN);
    }

    public void setTxt_productSerialNo(String txt_productSerialNo) {
        this.txt_productSerialNo.setText(txt_productSerialNo);
    }
    
    public void clearTextFields(){
        this.txt_dispatchPN.setText("");
        this.txt_dispatchSerialNo.setText("");
        this.txt_productSerialNo.setText("");
        this.txt_productPN.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scan_textbox = new javax.swing.JTextField();
        progressBar = new javax.swing.JProgressBar();
        message_label = new javax.swing.JTextField();
        button_initialize = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_productSerialNo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_productPN = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txt_dispatchPN = new javax.swing.JTextField();
        txt_dispatchSerialNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        scan_textbox.setBackground(new java.awt.Color(204, 255, 255));
        scan_textbox.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        scan_textbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scan_textboxActionPerformed(evt);
            }
        });
        scan_textbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scan_textboxKeyPressed(evt);
            }
        });

        progressBar.setBackground(new java.awt.Color(51, 255, 51));

        message_label.setEditable(false);
        message_label.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N

        button_initialize.setText("Initialiser");
        button_initialize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_initializeActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Production sérial number :");

        txt_productSerialNo.setEditable(false);
        txt_productSerialNo.setBackground(new java.awt.Color(255, 255, 255));
        txt_productSerialNo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_productSerialNo.setForeground(new java.awt.Color(0, 0, 153));
        txt_productSerialNo.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Production part number :");

        txt_productPN.setEditable(false);
        txt_productPN.setBackground(new java.awt.Color(255, 255, 255));
        txt_productPN.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_productPN.setForeground(new java.awt.Color(0, 0, 153));
        txt_productPN.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(26, 26, 26)
                        .addComponent(txt_productSerialNo, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(26, 26, 26)
                        .addComponent(txt_productPN, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_productSerialNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_productPN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Dispatch part number :");

        txt_dispatchPN.setEditable(false);
        txt_dispatchPN.setBackground(new java.awt.Color(255, 255, 255));
        txt_dispatchPN.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_dispatchPN.setForeground(new java.awt.Color(0, 0, 153));
        txt_dispatchPN.setToolTipText("");

        txt_dispatchSerialNo.setEditable(false);
        txt_dispatchSerialNo.setBackground(new java.awt.Color(255, 255, 255));
        txt_dispatchSerialNo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_dispatchSerialNo.setForeground(new java.awt.Color(0, 0, 153));
        txt_dispatchSerialNo.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Dispatch sérial number :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_dispatchPN, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dispatchSerialNo, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txt_dispatchPN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_dispatchSerialNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 1192, Short.MAX_VALUE)
            .addComponent(scan_textbox)
            .addComponent(message_label)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button_initialize)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(message_label, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scan_textbox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button_initialize)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(174, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void scan_textboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scan_textboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scan_textboxActionPerformed

    private void scan_textboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scan_textboxKeyPressed
        // User has pressed Carriage return button
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            state.doAction(WarehouseHelper.warehouse_control_context);
            state = WarehouseHelper.warehouse_control_context.getState();
        }
    }//GEN-LAST:event_scan_textboxKeyPressed

    private void button_initializeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_initializeActionPerformed
        S001_PalletNumberScan state = new S001_PalletNumberScan();
        WarehouseHelper.warehouse_control_context.setState(state);
        scan_textbox.setText("");
        scan_textbox.requestFocus();
        clearTextFields();
    }//GEN-LAST:event_button_initializeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_initialize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField message_label;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextField scan_textbox;
    private javax.swing.JTextField txt_dispatchPN;
    private javax.swing.JTextField txt_dispatchSerialNo;
    private javax.swing.JTextField txt_productPN;
    private javax.swing.JTextField txt_productSerialNo;
    // End of variables declaration//GEN-END:variables
}
