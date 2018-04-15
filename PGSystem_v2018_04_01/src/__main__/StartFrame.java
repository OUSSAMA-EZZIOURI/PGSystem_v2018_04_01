package __main__;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import gui.config.CONFIG_UI0000_AUTH;
import gui.packaging.PackagingVars;
import gui.packaging.mode1.gui.PACKAGING_UI0001_Main_Mode1;
import gui.packaging.mode2.gui.PACKAGING_UI0001_Main_Mode2;
import gui.packaging_warehouse.PACKAGING_WAREHOUSE_UI0001_PasswordRequest;
import gui.warehouse_fg_reception.WAREHOUSE_FG_UI0001_SCAN;
import gui.warehouse_dispatch.WAREHOUSE_DISPATCH_UI0003_PasswordRequest;
import gui.warehouse_dispatch.state.S010_DispatchUserCodeScan;
import gui.warehouse_dispatch.state.WarehouseHelper;
import helper.Helper;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import gui.packaging.mode2.state.Mode2_S010_UserCodeScan;
import gui.warehouse_fg_reception.WAREHOUSE_FG_UI0003_PasswordRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.bytecode.stackmap.TypeData.ClassName;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author Administrator
 */
public class StartFrame extends javax.swing.JFrame implements ActionListener,
        PropertyChangeListener {

    /**
     * To be used because the custom UILog object has not been initialized yet !
     */
    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());

    //private static final int PORT = 12345;        // random large port number
    private static ServerSocket s;

    private UpdateTask task;

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class UpdateTask extends SwingWorker<Void, Void> {

        /*
         * StartFrame task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            int a = 15;
            for (int i = 0; i < a; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }
                System.out.println("i " + i);
                System.out.println("tot" + (i * 100) / a);
                progress += (int) (i * 100) / a;
                System.out.println("progress " + progress);
                setProgress(progress);
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            updateButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            JOptionPane.showMessageDialog(null, "Terminé!\n");

        }
    }

    // static initializer
    {
        /* Create and display the form */
        String feedback = PropertiesLoader.loadConfigProperties();
        LOGGER.log(Level.INFO, feedback);
        try {
            s = new ServerSocket(Integer.valueOf(GlobalVars.APP_PROP.getProperty("RUNNING_PORT").toString()), 10, InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            //* Application encountered a problem !
            //JOptionPane.showMessageDialog(null, UILog.(ErrorMsg.APP_ERR0002[0]), ErrorMsg.APP_ERR0002[0], JOptionPane.ERROR_MESSAGE);            
            UILog.severeDialog(this, ErrorMsg.APP_ERR0002);
            System.out.println(UILog.i18n(ErrorMsg.APP_ERR0002[0]));
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            //Application already running !            
            UILog.severeDialog(this, ErrorMsg.APP_ERR0003);
            //JOptionPane.showMessageDialog(null, ErrorMsg.i18n(ErrorMsg.APP_ERR0003[0]), ErrorMsg.APP_ERR0003[0], JOptionPane.ERROR_MESSAGE);
            System.out.println(UILog.i18n(ErrorMsg.APP_ERR0003[0]));
            System.out.println(e.getMessage());
            System.exit(1);

        }
    }

    /**
     * Creates new form UI0000_ProjectChoice
     */
    public StartFrame() {
        initComponents();
        versionLabel.setText(GlobalVars.APP_NAME + " " + GlobalVars.APP_VERSION);
        authorLabel.setText(GlobalVars.APP_AUTHOR);
        this.setTitle(GlobalVars.APP_NAME + " " + GlobalVars.APP_VERSION);
        Helper.centerJFrame(this);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        PACKAGING_MODULE = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        WAREHOUSE_INPUT_MODULE = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        WAREHOUSE_DISPATCH_MODULE = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        SETTING_MODULE = new javax.swing.JButton();
        authorLabel = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        PACKAGING_MGMT_MODULE = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        updateButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 51));
        setForeground(new java.awt.Color(0, 0, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setMinimumSize(new java.awt.Dimension(128, 128));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Packaging");

        PACKAGING_MODULE.setBackground(new java.awt.Color(204, 204, 255));
        PACKAGING_MODULE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/img/packaging-icon.png"))); // NOI18N
        PACKAGING_MODULE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PACKAGING_MODULEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(PACKAGING_MODULE, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PACKAGING_MODULE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setMinimumSize(new java.awt.Dimension(128, 128));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Entrées magasin");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        WAREHOUSE_INPUT_MODULE.setBackground(new java.awt.Color(204, 204, 255));
        WAREHOUSE_INPUT_MODULE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/img/warehouse-icon.png"))); // NOI18N
        WAREHOUSE_INPUT_MODULE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WAREHOUSE_INPUT_MODULEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(WAREHOUSE_INPUT_MODULE, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WAREHOUSE_INPUT_MODULE, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setMinimumSize(new java.awt.Dimension(128, 128));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Dispatch");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        WAREHOUSE_DISPATCH_MODULE.setBackground(new java.awt.Color(204, 204, 255));
        WAREHOUSE_DISPATCH_MODULE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/img/warehouse-output.png"))); // NOI18N
        WAREHOUSE_DISPATCH_MODULE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WAREHOUSE_DISPATCH_MODULEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(WAREHOUSE_DISPATCH_MODULE, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WAREHOUSE_DISPATCH_MODULE, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setMinimumSize(new java.awt.Dimension(128, 128));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Paramètres");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        SETTING_MODULE.setBackground(new java.awt.Color(204, 204, 255));
        SETTING_MODULE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/img/setting-icon.png"))); // NOI18N
        SETTING_MODULE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SETTING_MODULEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SETTING_MODULE, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SETTING_MODULE, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        authorLabel.setText("EZZIOURI Oussama");

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setMinimumSize(new java.awt.Dimension(128, 128));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Gestion stock");

        PACKAGING_MGMT_MODULE.setBackground(new java.awt.Color(204, 204, 255));
        PACKAGING_MGMT_MODULE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/img/packaging-warehouse-icon.png"))); // NOI18N
        PACKAGING_MGMT_MODULE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PACKAGING_MGMT_MODULEActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("packaging");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(PACKAGING_MGMT_MODULE, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PACKAGING_MGMT_MODULE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        versionLabel.setText("PG System - v1.17.6 - EZZIOURI Oussama");

        updateButton.setText("Mise à jour");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(versionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(authorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 382, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(versionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(authorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to exit the program?", "Exit Program Message Box",
                JOptionPane.YES_NO_OPTION);
        if (confirmed == 0) {
            dispose();
        } else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//no
        }
    }//GEN-LAST:event_formWindowClosing

    private void PACKAGING_MODULEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PACKAGING_MODULEActionPerformed
        //Bind to localhost adapter with a zero connection queue
        PackagingVars.mode2_context.setState(new Mode2_S010_UserCodeScan());

        if (GlobalVars.APP_PROP.getProperty("PACKAGING_SCAN_MODE").equals("1")) {
            PackagingVars.Packaging_Gui_Mode1 = new PACKAGING_UI0001_Main_Mode1(null, this);
            PackagingVars.Packaging_Gui_Mode1.reloadDataTable();
            PackagingVars.Packaging_Gui_Mode1.disableAdminMenus();
        } else if (GlobalVars.APP_PROP.getProperty("PACKAGING_SCAN_MODE").equals("2")) {
            PackagingVars.Packaging_Gui_Mode2 = new PACKAGING_UI0001_Main_Mode2(null, this);
            PackagingVars.Packaging_Gui_Mode2.reloadDataTable();
            PackagingVars.Packaging_Gui_Mode2.disableAdminMenus();
        }
    }//GEN-LAST:event_PACKAGING_MODULEActionPerformed

    private void WAREHOUSE_INPUT_MODULEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WAREHOUSE_INPUT_MODULEActionPerformed
        //new WAREHOUSE_FG_UI0001_SCAN(null, this).setVisible(true);
        //this.setExtendedState(this.getExtendedState() | JFrame.ICONIFIED);
        new WAREHOUSE_FG_UI0003_PasswordRequest(null, true).setVisible(true);
        this.setExtendedState(this.getExtendedState() | JFrame.ICONIFIED);
    }//GEN-LAST:event_WAREHOUSE_INPUT_MODULEActionPerformed

    private void WAREHOUSE_DISPATCH_MODULEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WAREHOUSE_DISPATCH_MODULEActionPerformed
        WarehouseHelper.warehouse_reserv_context.setState(new S010_DispatchUserCodeScan());
        new WAREHOUSE_DISPATCH_UI0003_PasswordRequest(null, true).setVisible(true);
        this.setExtendedState(this.getExtendedState() | JFrame.ICONIFIED);
    }//GEN-LAST:event_WAREHOUSE_DISPATCH_MODULEActionPerformed

    private void SETTING_MODULEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SETTING_MODULEActionPerformed
        new CONFIG_UI0000_AUTH().setVisible(true);
    }//GEN-LAST:event_SETTING_MODULEActionPerformed

    private void PACKAGING_MGMT_MODULEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PACKAGING_MGMT_MODULEActionPerformed
        new PACKAGING_WAREHOUSE_UI0001_PasswordRequest(null, true).setVisible(true);
        this.setExtendedState(this.getExtendedState() | JFrame.ICONIFIED);
    }//GEN-LAST:event_PACKAGING_MGMT_MODULEActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed

        updateButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new UpdateTask();
        task.addPropertyChangeListener(this);
        task.execute();
    }//GEN-LAST:event_updateButtonActionPerformed

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);

//            JOptionPane.showMessageDialog(null, String.format(
//                    "Completed %d%% of task.\n", task.getProgress()));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
 /* try {
         UIManager.setLookAndFeel("SyntheticaSilverMoonLookAndFeel");
         } catch (Exception e) {
         e.printStackTrace();
         }*/

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                System.out.println("" + info.getName());
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());

                    // break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartFrame ui = new StartFrame();
                ui.setVisible(true);
                UILog.createDailyLogFile(GlobalVars.APP_PROP.getProperty("LOG_PATH"));
                PropertiesLoader.createDailyOutPrintDir(GlobalVars.APP_PROP.getProperty("PRINT_DIR"),
                        GlobalVars.APP_PROP.getProperty("PRINT_PALLET_DIR"),
                        GlobalVars.APP_PROP.getProperty("PRINT_CLOSING_PALLET_DIR"),
                        GlobalVars.APP_PROP.getProperty("PRINT_PICKING_SHEET_DIR"),
                        GlobalVars.APP_PROP.getProperty("PRINT_DISPATCH_SHEET_DIR"));
            }
        });

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PACKAGING_MGMT_MODULE;
    private javax.swing.JButton PACKAGING_MODULE;
    private javax.swing.JButton SETTING_MODULE;
    private javax.swing.JButton WAREHOUSE_DISPATCH_MODULE;
    private javax.swing.JButton WAREHOUSE_INPUT_MODULE;
    private javax.swing.JLabel authorLabel;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton updateButton;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
