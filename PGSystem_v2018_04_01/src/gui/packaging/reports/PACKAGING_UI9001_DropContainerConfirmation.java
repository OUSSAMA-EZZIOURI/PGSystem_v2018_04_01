/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.reports;

import __main__.GlobalVars;
import entity.BaseContainer;
import entity.BaseHarness;
import entity.DropBaseContainer;
import entity.DropBaseHarness;
import entity.PackagingStockMovement;
import gui.packaging.PackagingVars;
import helper.HQLHelper;
import helper.Helper;
import java.util.Date;
import java.util.Set;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import ui.UILog;

/**
 *
 * @author Administrator
 */
public final class PACKAGING_UI9001_DropContainerConfirmation extends javax.swing.JDialog {

    private BaseContainer bc;
    private int scanMode;    
    private final String MSG_DROP_SUCCESS = "Pallet [%s] successfully dropped !";
    private PACKAGING_UI0010_PalletDetails parent;

    /**
     * Creates new form UI9001_DropPalletConfirmation
     *
     * @param parent
     * @param modal
     * @param bc
     * @param scanMode
     * 
     */
    public PACKAGING_UI9001_DropContainerConfirmation(javax.swing.JFrame parent, boolean modal, BaseContainer bc, int scanMode ) {
        super(parent, modal);
        initComponents();
        initGui(parent);
        this.setBaseContainer(bc);
        palletNumber_lbl.setText(bc.getPalletNumber());
        this.scanMode = scanMode;
    }

    private void initGui(javax.swing.JFrame parent) {
        this.parent = (PACKAGING_UI0010_PalletDetails) parent;

        //Center the this dialog in the screen
        Helper.centerJDialog(this);

        //Disable resizing
        this.setResizable(false);

    }

    public BaseContainer getBaseContainer() {
        return bc;
    }

    public void setBaseContainer(BaseContainer bc) {
        this.bc = bc;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dropFeedback_txtbox = new javax.swing.JTextArea();
        ok_btn = new javax.swing.JButton();
        cancel_btn = new javax.swing.JButton();
        palletNumber_lbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Drop Comment");
        setModal(true);
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        jLabel1.setText("Commentaire d'annulation");

        dropFeedback_txtbox.setColumns(20);
        dropFeedback_txtbox.setRows(5);
        jScrollPane1.setViewportView(dropFeedback_txtbox);

        ok_btn.setText("OK");
        ok_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_btnActionPerformed(evt);
            }
        });

        cancel_btn.setText("Cancel");
        cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_btnActionPerformed(evt);
            }
        });

        palletNumber_lbl.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        palletNumber_lbl.setForeground(new java.awt.Color(0, 51, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ok_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel_btn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(palletNumber_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addComponent(palletNumber_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok_btn)
                    .addComponent(cancel_btn))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_btnActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancel_btnActionPerformed

    private void ok_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_btnActionPerformed

        if (!dropFeedback_txtbox.getText().isEmpty()) {
            int confirmed = JOptionPane.showConfirmDialog(null,
                    String.format("Are you sure you want to drop the pallet [%s] ?",
                            this.bc.getPalletNumber()),
                    "Drop Pallet Confirmation",
                    JOptionPane.WARNING_MESSAGE);

            if (confirmed == 0) {
                DropBaseContainer dp = new DropBaseContainer();
                dp.setClosedTime(this.bc.getClosedTime());
                dp.setContainerState(this.bc.getContainerState());
                dp.setContainerStateCode(this.bc.getContainerStateCode());
                dp.setCreateId(PackagingVars.context.getUser().getId());
                dp.setCreateTime(this.bc.getCreateTime());
                dp.setDropTime(new Date());
                dp.setDropFeedback(this.dropFeedback_txtbox.getText());
                dp.setHarnessIndex(this.bc.getHarnessIndex());
                dp.setHarnessPart(this.bc.getHarnessPart());
                dp.setHarnessType(this.bc.getHarnessType());
                dp.setPackType(this.bc.getPackType());
                dp.setPalletNumber(this.bc.getPalletNumber());
                dp.setQtyExpected(this.bc.getQtyExpected());
                dp.setQtyRead(this.bc.getQtyRead());
                dp.setStartTime(this.bc.getStartTime());
                dp.setSupplierPartNumber(this.bc.getSupplierPartNumber());
                dp.setWorkTime(this.bc.getWorkTime());
                dp.setWriteId(PackagingVars.context.getUser().getId());
                dp.setWriteTime(this.bc.getFifoTime());
                dp.setUser(this.bc.getUser());

                //Copy the container into the DropBaseContainer table
                dp.create(dp);
                UILog.info("Droping harness list...{0}", this.bc.getHarnessList().size());
                //Selectionner l
                Set<BaseHarness> set = this.bc.getHarnessList();
                for (BaseHarness bh : set) {
                    System.out.println("Bh ID " + bh.getId());
                    DropBaseHarness dh = new DropBaseHarness();
                    dh.setContainer(dp);
                    dh.setCounter(bh.getCounter());
                    dh.setCreateId(PackagingVars.context.getUser().getId());
                    dh.setCreateTime(bh.getCreateTime());
                    dh.setDropTime(new Date());
                    dh.setDropFeedback(dropFeedback_txtbox.getText());
                    dh.setHarnessPart(bh.getHarnessPart());
                    dh.setHarnessType(bh.getHarnessType());
                    dh.setPalletNumber(bh.getPalletNumber());
                    dh.setUser(PackagingVars.context.getUser().getLogin());
                    dh.setWriteId(PackagingVars.context.getUser().getId());
                    dh.setWriteTime(bh.getWriteTime());
                    dh.create(dh);

                    //Drop pallet from table
                    Helper.sess.beginTransaction();
                    //############ Remove Additional Barecode ######################
                    Query query = Helper.sess.createQuery(HQLHelper.DEL_LABEL_BY_HP_ID);
                    query.setParameter("harnessId", bh.getId());
                    int result = query.executeUpdate();
                    System.out.println(String.format("Labels removed for harness %s ", result, bh.getId()));

                    //############ REMOVE THE HARNESS FROM CONTAINER ###############
                    query = Helper.sess.createQuery(HQLHelper.DEL_HP_BY_COUNTER);
                    query.setParameter("counter", bh.getCounter());
                    int id = query.executeUpdate();
                    System.out.println(String.format("Harness [%s] removed from pallet %s ", id, bh.getCounter(), bc.getPalletNumber()));
                    //Helper.sess.delete(bh);
                    Helper.sess.clear();
                    Helper.sess.getTransaction().commit();
                }

                //Delete the container from the BaseContainer
                
//                Helper.sess.beginTransaction();
//                Helper.sess.flush();
//                Helper.sess.delete(this.bc);
//                Helper.sess.getTransaction().commit();

                //Book back the packaging items only for stored pallets
                if (bc.getContainerState().equals(GlobalVars.PALLET_STORED)) {
                    if ("1".equals(GlobalVars.APP_PROP.getProperty("BOOK_PACKAGING").toString())) {
                        PackagingStockMovement pm = new PackagingStockMovement();
                        pm.bookMasterPack(PackagingVars.context.getUser().getFirstName() + " " + PackagingVars.context.getUser().getLastName(),
                                this.bc.getPackType(), 1, "IN",
                                GlobalVars.APP_PROP.getProperty("WH_FINISH_GOODS"),
                                GlobalVars.APP_PROP.getProperty("WH_PACKAGING").toString(),
                                "Pallet dropped : " + this.dropFeedback_txtbox.getText(),
                                bc.getPalletNumber());
                    }
                }
                
                bc.delete(this.bc);

                //Clear parent search box
                this.parent.clearSearchBox();

                //Print delete OK message
                this.parent.setOkText(String.format(MSG_DROP_SUCCESS, dp.getPalletNumber()));

                //Reset the table
                this.parent.reset_container_table_content();

                //Clear form fields
                this.parent.clearContainerFieldsValues();
                
                //Reset the title
                this.parent.setTitle("Détails palette");
                
                if(this.scanMode == 1){                
                    PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea("Scanner une référence pour ouvrir \nune palette\nOu selectionner une palette du tableau çi-dessous");
                    PackagingVars.Packaging_Gui_Mode1.setRequestedPallet_txt("");
                    //Refresh the main table
                    PackagingVars.Packaging_Gui_Mode1.reloadDataTable();
                }else{
                    PackagingVars.Packaging_Gui_Mode2.setFeedbackTextarea("Scanner le code à barre d'une référence.");
                    PackagingVars.Packaging_Gui_Mode2.setRequestedPallet_txt("");
                    //Refresh the main table
                    PackagingVars.Packaging_Gui_Mode2.reloadDataTable();
                }

                this.dispose();

            }
        }//end if(!dropFeedback_txtbox.getText().isEmpty()) 
        else {
            JOptionPane.showMessageDialog(null, "Empty drop comment !", "Please specify a comment for this operation ! ", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ok_btnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel_btn;
    private javax.swing.JTextArea dropFeedback_txtbox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton ok_btn;
    private javax.swing.JLabel palletNumber_lbl;
    // End of variables declaration//GEN-END:variables
}