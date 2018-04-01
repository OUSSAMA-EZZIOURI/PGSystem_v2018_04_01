/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch;

import entity.LoadPlan;
import entity.LoadPlanDestination;
import entity.LoadPlanDestinationRel;
import helper.HQLHelper;
import helper.Helper;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JDialog;
//import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author user
 */
public class WAREHOUSE_DISPATCH_UI0005_EDIT_PLAN extends javax.swing.JDialog {

    private LoadPlan lp;
    private final WAREHOUSE_DISPATCH_UI0002_DISPATCH_SCAN parent;
    public String[] planDests;
    public LoadPlanDestinationRel[] loadPlanDestRel;

    /**
     * Creates new form WAREHOUSE_OUT_UI0004_NEW_PLAN
     *
     * @param parent
     * @param modal
     * @param lp
     */
    public WAREHOUSE_DISPATCH_UI0005_EDIT_PLAN(WAREHOUSE_DISPATCH_UI0002_DISPATCH_SCAN parent, boolean modal, LoadPlan lp) {
        super(parent, modal);
        this.parent = parent;
        this.lp = lp;

        initComponents();
        initGui();

    }

    private void initGui() {
        plan_num_label.setText(this.lp.getId().toString());
        create_user_label.setText(this.lp.getUser());
        create_time_label.setText(this.lp.getCreateTime().toString());
        deliv_date_label.setText(this.lp.getDeliveryTime().toString());
        state_label.setText(this.lp.getPlanState());
        newDeliveryDatePicker.setDate(this.lp.getDeliveryTime());
        //Load plan destination
        this.loadPlanDestinationsList();
        this.initDestinationsJtable();

        Helper.centerJDialog((JDialog) this);
        this.setResizable(false);

    }

    public boolean ifDestIsSelected(String dest, String[] destsList) {
        for (String item : destsList) {
            System.out.println("item" + item);
            if (item != null && item.equals(dest)) {
                return true;
            }
        }
        return false;
    }

    public void disableEditingTable() {
        for (int c = 1; c < destinations_table.getColumnCount(); c++) {
            Class<?> col_class = destinations_table.getColumnClass(c);
            if (col_class != Boolean.class) {
                destinations_table.setDefaultEditor(col_class, null);        // remove editor            
            }
        }
    }

    private String[] loadPlanDestinationsList() {
        // Load destinations of this plan
        Helper.startSession();

        Query query = Helper.sess.createQuery(HQLHelper.GET_FINAL_DESTINATIONS_OF_PLAN);
        query.setParameter("loadPlanId", this.lp.getId());
        Helper.sess.getTransaction().commit();
        List result = query.list();
        if (result.isEmpty()) {

            UILog.severe(ErrorMsg.APP_ERR0025[0]);
            UILog.severeDialog(null, ErrorMsg.APP_ERR0025);

            return null;
        } else {
            this.planDests = new String[result.size()];
            this.loadPlanDestRel = new LoadPlanDestinationRel[result.size()];
            int i = 0;
            //Map project data in the list
            for (Object o : result) {
                LoadPlanDestinationRel obj = (LoadPlanDestinationRel) o;
                this.loadPlanDestRel[i] = obj;
                this.planDests[i] = obj.getDestination();
                i++;
            }

            return this.planDests;
        }
    }

    /**
     * Load destinations in jTable
     */
    public void initDestinationsJtable() {
        //First we load the plan destinations list
        this.planDests = this.loadPlanDestinationsList();
        if (this.planDests != null) {
            //Second we load all destinations, if the destination
            //is in the plan, we mark it with true, else we mark it with false
            Helper.startSession();
            Query query = Helper.sess.createQuery(HQLHelper.GET_ALL_FINAL_DESTINATIONS);

            Helper.sess.getTransaction().commit();
            List result = query.list();

            if (result.isEmpty()) {
                UILog.severe(ErrorMsg.APP_ERR0025[0]);
                UILog.severeDialog(null, ErrorMsg.APP_ERR0025);
                this.dispose();
            } else {

                //Create the destinations table component
                Object[][] data = new Object[result.size()][2];
                for (int i = 0; i < result.size(); i++) {
                    LoadPlanDestination dest = (LoadPlanDestination) result.get(i);
                    for (String str : this.planDests) {
                        System.out.println("Str " + str + " = " + dest.getDestination());
                        if (dest.getDestination().equals(str)) {
                            data[i][0] = true;
                            break;
                        } else {
                            data[i][0] = false;
                        }
                        //data[i][0] = (dest.getDestination() == null ? str == null : dest.getDestination().equals(str));                        
                    }
                    data[i][1] = dest.getDestination();
                }

                String[] cols = {"", "Destination finale"};
                DefaultTableModel model = new DefaultTableModel(data, cols) {
                    @Override
                    public Class<?> getColumnClass(int column) {
                        if (column == 1) {
                            return String.class;
                        } else {
                            return Boolean.class;
                        }
                    }

                };
                destinations_table.setModel(model);

                //Set the Jtable column
                TableColumnModel columnModel = destinations_table.getColumnModel();
                columnModel.getColumn(0).setMaxWidth((int) (10 * 10000));
                columnModel.getColumn(1).setMaxWidth((int) (90 * 10000));

                destinations_table.setColumnModel(columnModel);

                this.setVisible(true);
            }
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

        ok_btn = new javax.swing.JButton();
        cancel_btn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        newDeliveryDatePicker = new org.jdesktop.swingx.JXDatePicker();
        time_label2 = new javax.swing.JLabel();
        plan_num_label = new javax.swing.JLabel();
        time_label3 = new javax.swing.JLabel();
        create_user_label = new javax.swing.JLabel();
        time_label1 = new javax.swing.JLabel();
        create_time_label = new javax.swing.JLabel();
        time_label4 = new javax.swing.JLabel();
        deliv_date_label = new javax.swing.JLabel();
        time_label6 = new javax.swing.JLabel();
        state_label = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        destinations_table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modifier plan de chargment");

        ok_btn.setText("Sauvegarder");
        ok_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_btnActionPerformed(evt);
            }
        });

        cancel_btn.setText("Annuler");
        cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_btnActionPerformed(evt);
            }
        });

        jLabel1.setText("Nouvelle date d'expédition");

        time_label2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label2.setText("Plan de chargement N° :");

        plan_num_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        plan_num_label.setText("-----");

        time_label3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label3.setText("Create user :");

        create_user_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        create_user_label.setText("-----");

        time_label1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label1.setText("Date création :");

        create_time_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        create_time_label.setText("--/--/---- --:--");

        time_label4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label4.setText("Date Expédition :");

        deliv_date_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        deliv_date_label.setText("--/--/----");

        time_label6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        time_label6.setText("Status :");

        state_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        state_label.setText("-----");

        destinations_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(destinations_table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(newDeliveryDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(time_label4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(time_label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(time_label3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(time_label6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(create_user_label)
                                .addComponent(create_time_label, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(deliv_date_label, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(state_label, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(time_label2)
                        .addGap(18, 18, 18)
                        .addComponent(plan_num_label)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ok_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancel_btn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(time_label2)
                    .addComponent(plan_num_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(time_label3)
                    .addComponent(create_user_label))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(time_label1)
                    .addComponent(create_time_label))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(time_label4)
                    .addComponent(deliv_date_label))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(state_label)
                    .addComponent(time_label6))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newDeliveryDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok_btn)
                    .addComponent(cancel_btn))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ok_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_btnActionPerformed
        //Get selected destinations 
        String[] selectedDestinations = new String[destinations_table.getRowCount()];
        int i = 0;
        boolean flag = false;
        boolean destinationsValidation = true;
        for (int r = 0; r < destinations_table.getRowCount(); r++) {
            if ((boolean) destinations_table.getValueAt(r, 0) == true) {
                selectedDestinations[i++] = destinations_table.getValueAt(r, 1).toString();
                flag = true;
            }
        }
        if (!flag) {
            UILog.severe(ErrorMsg.APP_ERR0026[0]);
            UILog.severeDialog(null, ErrorMsg.APP_ERR0026);

        } else {
            try {
                //Remove old destinations from the plan
                for (LoadPlanDestinationRel obj : this.loadPlanDestRel) {
                    System.out.println("selected " + selectedDestinations.length);
                    if (ifDestIsSelected(obj.getDestination(), planDests) && !ifDestIsSelected(obj.getDestination(), selectedDestinations)) {
                        System.out.println(obj.getDestination() + "not selected");
                        //before remove the destination
                        // Check if contains any pallets
                        Helper.startSession();

                        Query query = Helper.sess.createQuery(HQLHelper.GET_NBRE_LOAD_PLAN_LINES);
                        query.setParameter("loadPlanId", this.lp.getId());
                        query.setParameter("destinationWh", obj.getDestination());
                        Helper.sess.getTransaction().commit();
                        List result = query.list();
                        //Empty and Not selected
                        if (result.isEmpty()) {
                            obj.delete(obj);
                        } else {
                            UILog.infoDialog(null, ErrorMsg.APP_ERR0024, obj.getDestination());
                            UILog.info(ErrorMsg.APP_ERR0024[0], obj.getDestination());
                            destinationsValidation = false;
                            break;
                        }
                    }
                }

                if (destinationsValidation) {
                    //Save the destinations of the plan
                    for (String finalDest : selectedDestinations) {
                        if (finalDest != null && !"".equals(finalDest)) {
                            if (!ifDestIsSelected(finalDest, this.planDests)) {
                                LoadPlanDestinationRel planRel = new LoadPlanDestinationRel(finalDest, this.lp.getId());
                                planRel.create(planRel);
                            }
                        }
                    }
                }

                if (newDeliveryDatePicker.getDate() == null) {
                    UILog.severe(ErrorMsg.APP_ERR0027[0]);
                    UILog.severeDialog(null, ErrorMsg.APP_ERR0027);
                } else {
                    this.lp.setDeliveryTime(newDeliveryDatePicker.getDate());
                }
                this.lp.update(this.lp);
                this.parent.reloadPlansData();
                this.parent.loadPlanDataInGui();
                this.dispose();
            } catch (HibernateException | HeadlessException e) {
                UILog.severe(ErrorMsg.APP_ERR0028[0]);
                UILog.severeDialog(null, ErrorMsg.APP_ERR0028);
            }
        }
    }//GEN-LAST:event_ok_btnActionPerformed

    private void cancel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_btnActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancel_btnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel_btn;
    private javax.swing.JLabel create_time_label;
    private javax.swing.JLabel create_user_label;
    private javax.swing.JLabel deliv_date_label;
    private javax.swing.JTable destinations_table;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private org.jdesktop.swingx.JXDatePicker newDeliveryDatePicker;
    private javax.swing.JButton ok_btn;
    private javax.swing.JLabel plan_num_label;
    private javax.swing.JLabel state_label;
    private javax.swing.JLabel time_label1;
    private javax.swing.JLabel time_label2;
    private javax.swing.JLabel time_label3;
    private javax.swing.JLabel time_label4;
    private javax.swing.JLabel time_label6;
    // End of variables declaration//GEN-END:variables
}
