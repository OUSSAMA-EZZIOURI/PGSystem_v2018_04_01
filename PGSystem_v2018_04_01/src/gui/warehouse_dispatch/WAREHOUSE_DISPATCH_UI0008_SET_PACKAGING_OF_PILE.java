/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch;

import entity.LoadPlan;
import entity.LoadPlanLinePackaging;
import entity.PackagingItems;
import gui.warehouse_dispatch.state.WarehouseHelper;
import helper.ComboItem;
import helper.HQLHelper;
import helper.Helper;
import helper.UIHelper;
import static java.awt.Event.DELETE;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author user
 */
public class WAREHOUSE_DISPATCH_UI0008_SET_PACKAGING_OF_PILE extends javax.swing.JDialog {

    private final LoadPlan lp;
    private final String destinationWh;
    private final WAREHOUSE_DISPATCH_UI0002_DISPATCH_SCAN parent;
    Vector<String> pack_items_table_header = new Vector<String>();
    List<String> header_columns = Arrays.asList(
            "Num Ligne",
            "Plan ID",
            "Pile",
            "Destination",
            "Pack Item",
            "Quantité"
    );
    Vector pack_items_table_data = new Vector();
    PackagingItems itemsAux;
    public LoadPlanLinePackaging packLineAux;

    /**
     * Creates new form WAREHOUSE_DISPATCH_UI0008_SET_PACKAGING_OF_PILE
     *
     * @param parent
     * @param modal
     * @param destinationWh
     * @param lp
     */
    public WAREHOUSE_DISPATCH_UI0008_SET_PACKAGING_OF_PILE(WAREHOUSE_DISPATCH_UI0002_DISPATCH_SCAN parent, boolean modal, LoadPlan lp, String destinationWh) {
        super(parent, modal);
        this.parent = parent;
        this.lp = lp;
        this.destinationWh = destinationWh;
        initComponents();
        if (initGui()) {
            Helper.centerJDialog(this);
            this.setResizable(false);
            this.setVisible(true);
        } else {
            this.dispose();
        }

    }

    private boolean initPilesBox(int planId) {

        Helper.startSession();
        String query_str = String.format(HQLHelper.GET_PILES_OF_PLAN_BY_DEST, Integer.valueOf(planId), this.destinationWh);
        SQLQuery query = Helper.sess.createSQLQuery(query_str);

        query.addScalar("pile_num", StandardBasicTypes.INTEGER);
        List<Object[]> resultList = query.list();
        Helper.sess.getTransaction().commit();
        Integer[] arg = (Integer[]) resultList.toArray(new Integer[resultList.size()]);
        if (!resultList.isEmpty()) {
            piles_box.removeAll();
            for (int pile : arg) {
                piles_box.addItem(new ComboItem(pile + "", pile + ""));
            }
            return true;
        } else {
            return false;

        }
    }

    private void initPackItemsBox() {
        pack_items_box.removeAllItems();
        List result = new PackagingItems().selectAllPackItems();
        //Map project data in the list
        for (Object o : result) {
            PackagingItems pc = (PackagingItems) o;
            pack_items_box.addItem(new ComboItem(pc.getPackItem(), pc.getPackItem()));
        }

    }

    private boolean initGui() {
        if (initPilesBox(this.lp.getId())) {
            load_plan_id_txt.setText(lp.getId() + "");
            destination_txt.setText(this.destinationWh);
            if (WarehouseHelper.LOAD_PLAN_STATE_CLOSED.equals(this.lp.getPlanState())) {
                UIHelper.setComponentsState(false, ok_btn, ok_and_close_btn, pack_items_box, piles_box, qty_txt, comment_txt);
            }
            initPackItemsBox();
            initItemsTabUIjtableHeader();
            disableEditingJtable();
            initjTableDoubleClick();
            initJTableKeyListener();
            refresh();
            return true;
        } else {
            UILog.info(ErrorMsg.APP_ERR0029[0], this.destinationWh);
            UILog.infoDialog(null, ErrorMsg.APP_ERR0025, this.destinationWh);
            this.dispose();
            return false;
        }

    }

    public void initJTableKeyListener() {
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = this.pack_items_jtable.getInputMap(condition);
        ActionMap actionMap = this.pack_items_jtable.getActionMap();

        // DELETE is a String constant that for me was defined as "Delete"
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE);

        actionMap.put(DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Voulez-vous supprimer la ligne ?", "Suppression !",
                        JOptionPane.YES_NO_OPTION);
                if (confirmed == 0) {

                    Integer id = (Integer) pack_items_jtable.getValueAt(pack_items_jtable.getSelectedRow(), 0);

                    //Delete line from the database
                    Helper.startSession();
                    Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_PACKAGING_BY_ID);
                    query.setParameter("id", id);

                    Helper.sess.getTransaction().commit();
                    List result = query.list();
                    LoadPlanLinePackaging line = (LoadPlanLinePackaging) result.get(0);
                    line.delete(line);
                    msg_lbl.setText("Element supprimé !");
                    clearPackItemsTabFields();
                    refresh();
                }

            }

        });

    }

    private void initjTableDoubleClick() {
        this.pack_items_jtable.addMouseListener(
                new MouseAdapter() {
            private LoadPlanLinePackaging packLineAux = this.packLineAux;

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadPackItemsValues(this.packLineAux);
                }
            }

            public void loadPackItemsValues(LoadPlanLinePackaging packLineAux) {
                String id = String.valueOf(pack_items_jtable.getValueAt(pack_items_jtable.getSelectedRow(), 0));
                Helper.startSession();
                Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_PACKAGING_BY_ID);
                query.setParameter("id", Integer.valueOf(id));

                Helper.sess.getTransaction().commit();
                List result = query.list();
                this.packLineAux = (LoadPlanLinePackaging) result.get(0);
                loadDataToLabels(this.packLineAux);
            }

            private void loadDataToLabels(LoadPlanLinePackaging packLineAux) {
                load_plan_id_txt.setText(packLineAux.getLoadPlanId() + "");
                destination_txt.setText(packLineAux.getDestination());
                for (int i = 0; i < pack_items_box.getItemCount(); i++) {
                    if (pack_items_box.getItemAt(i).toString() == null ? packLineAux.getPackItem().toString() == null : pack_items_box.getItemAt(i).toString().equals(packLineAux.getPackItem().toString())) {
                        pack_items_box.setSelectedIndex(i);
                    }
                }
                id_txt.setText(packLineAux.getId() + "");
                qty_txt.setText(packLineAux.getQty() + "");
            }
        }
        );
    }

    private void postForm() {
        if ("".equals(qty_txt.getText().toString())
                || "0".equals(qty_txt.getText().toString())
                || Integer.valueOf(qty_txt.getText().toString()) == 0
                || Integer.valueOf(qty_txt.getText().toString()) == null) {
            JOptionPane.showMessageDialog(null, "Erreur quantité.", "Erreur utilisateur !", ERROR_MESSAGE);

        } else {
            if (id_txt.getText().equals("")) //Is a new entry
            {
                LoadPlanLinePackaging p = new LoadPlanLinePackaging(
                        Integer.valueOf(load_plan_id_txt.getText()),
                        Integer.valueOf(piles_box.getSelectedItem().toString()),
                        pack_items_box.getSelectedItem().toString(),
                        Integer.valueOf(qty_txt.getText()),
                        destination_txt.getText(),
                        comment_txt.getText());
                p.create(p);
                
                clearPackItemsTabFields();
                msg_lbl.setText("Nouveau élément enregistré !");
            } else { // Is an update
                String id = id_txt.getText();
                Helper.startSession();
                Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_PACKAGING_BY_ID);
                query.setParameter("id", Integer.valueOf(id));

                Helper.sess.getTransaction().commit();
                List result = query.list();
                this.packLineAux = (LoadPlanLinePackaging) result.get(0);
                this.packLineAux.setComment(comment_txt.getText());
                this.packLineAux.setQty(Integer.valueOf(qty_txt.getText()));
                this.packLineAux.setPackItem(pack_items_box.getSelectedItem().toString());
                this.packLineAux.update(this.packLineAux);
                msg_lbl.setText("Données mises à jour !");
                clearPackItemsTabFields();

            }
        }
    }

    /**
     * Desactive l'édition du jTable load plan lines
     */
    public void disableEditingJtable() {
        for (int c = 0; c < pack_items_jtable.getColumnCount(); c++) {
            Class<?> col_class = pack_items_jtable.getColumnClass(c);
            pack_items_jtable.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    /**
     * Reset the content of Packagin Items jtable header
     */
    private void initItemsTabUIjtableHeader() {
        for (Iterator<String> it = header_columns.iterator(); it.hasNext();) {
            pack_items_table_header.add(it.next());
        }

        pack_items_jtable.setModel(new DefaultTableModel(pack_items_table_data, pack_items_table_header));
    }

    public void resetJtableContent() {
        //Reset the jtable
        pack_items_table_data = new Vector();
        DefaultTableModel itemsDataModel = new DefaultTableModel(pack_items_table_data, pack_items_table_header);
        pack_items_jtable.removeAll();
        pack_items_jtable.setModel(itemsDataModel);
    }

    /**
     * @param table_data
     * @param table_header
     * @param jtable
     * @todo : reload_table_data a mettre dans une classe interface
     * @param resultList
     */
    public void reload_table_data(List resultList, Vector table_data, Vector<String> table_header, JTable jtable) {

        resetJtableContent();

        for (Object obj : resultList) {
            LoadPlanLinePackaging p = (LoadPlanLinePackaging) obj;
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(p.getId());
            oneRow.add(p.getLoadPlanId() + "");
            oneRow.add(p.getPileNum().toString());
            oneRow.add(p.getDestination());
            oneRow.add(p.getPackItem());
            oneRow.add(p.getQty());
            table_data.add(oneRow);
        }

        jtable.setModel(new DefaultTableModel(table_data, table_header));
        jtable.setAutoCreateRowSorter(true);
    }

    private void clearPackItemsTabFields() {
        id_txt.setText("");
        pack_items_box.setSelectedItem(0);
        qty_txt.setText("");
        comment_txt.setText("");
    }

    private void refresh() {

        String id = load_plan_id_txt.getText();
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_PACKAGING_BY_PLAN_ID_AND_DEST);
        query.setParameter("loadPlanId", Integer.valueOf(id));
        query.setParameter("destination", destination_txt.getText());
        Helper.sess.getTransaction().commit();
        List result = query.list();
        this.reload_table_data(result, pack_items_table_data, pack_items_table_header, pack_items_jtable);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        load_plan_id_txt = new javax.swing.JTextField();
        destination_txt = new javax.swing.JTextField();
        pack_items_box = new javax.swing.JComboBox();
        fname_lbl11 = new javax.swing.JLabel();
        qty_txt = new javax.swing.JTextField();
        ok_and_close_btn = new javax.swing.JButton();
        msg_lbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pack_items_jtable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        comment_txt = new javax.swing.JTextArea();
        fname_lbl12 = new javax.swing.JLabel();
        piles_box = new javax.swing.JComboBox();
        refresh_btn = new javax.swing.JButton();
        id_txt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Elements du packaging supplémentaire");

        ok_btn.setText("Sauvegarder");
        ok_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_btnActionPerformed(evt);
            }
        });

        cancel_btn.setText("Fermer");
        cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_btnActionPerformed(evt);
            }
        });

        jLabel1.setText("Plan ID : ");

        jLabel2.setText("Pile :");

        jLabel4.setText("Quantité :");

        jLabel5.setText("Destination :");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Eléments du packaging");

        load_plan_id_txt.setEditable(false);
        load_plan_id_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        load_plan_id_txt.setText("0");
        load_plan_id_txt.setEnabled(false);
        load_plan_id_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                load_plan_id_txtActionPerformed(evt);
            }
        });

        destination_txt.setEditable(false);
        destination_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        destination_txt.setEnabled(false);
        destination_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destination_txtActionPerformed(evt);
            }
        });

        pack_items_box.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        fname_lbl11.setText("Pack Item :");

        qty_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qty_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qty_txtActionPerformed(evt);
            }
        });

        ok_and_close_btn.setText("Sauvegarder et fermer");
        ok_and_close_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_and_close_btnActionPerformed(evt);
            }
        });

        msg_lbl.setBackground(new java.awt.Color(204, 204, 255));
        msg_lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        msg_lbl.setForeground(new java.awt.Color(0, 0, 255));

        pack_items_jtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(pack_items_jtable);

        comment_txt.setColumns(20);
        comment_txt.setRows(5);
        jScrollPane2.setViewportView(comment_txt);

        fname_lbl12.setText("Commentaire :");

        piles_box.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        refresh_btn.setText("Actualiser");
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        id_txt.setEditable(false);
        id_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        id_txt.setEnabled(false);
        id_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_txtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 20, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(qty_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(fname_lbl12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(fname_lbl11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pack_items_box, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(ok_btn)
                                .addGap(18, 18, 18)
                                .addComponent(ok_and_close_btn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(refresh_btn))
                            .addComponent(msg_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancel_btn))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(load_plan_id_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(piles_box, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(destination_txt))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(id_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(id_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(destination_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(load_plan_id_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(piles_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pack_items_box)
                    .addComponent(fname_lbl11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(qty_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fname_lbl12)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(refresh_btn)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(ok_and_close_btn)
                        .addComponent(ok_btn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msg_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancel_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ok_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_btnActionPerformed
        this.postForm();
        this.refresh();
    }//GEN-LAST:event_ok_btnActionPerformed

    private void cancel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_btnActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancel_btnActionPerformed

    private void destination_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destination_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_destination_txtActionPerformed

    private void qty_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qty_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qty_txtActionPerformed

    private void ok_and_close_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_and_close_btnActionPerformed
        this.postForm();
        this.dispose();
    }//GEN-LAST:event_ok_and_close_btnActionPerformed

    private void load_plan_id_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_load_plan_id_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_load_plan_id_txtActionPerformed

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        refresh();
    }//GEN-LAST:event_refresh_btnActionPerformed

    private void id_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_txtActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel_btn;
    private javax.swing.JTextArea comment_txt;
    private javax.swing.JTextField destination_txt;
    private javax.swing.JLabel fname_lbl11;
    private javax.swing.JLabel fname_lbl12;
    private javax.swing.JTextField id_txt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField load_plan_id_txt;
    private javax.swing.JLabel msg_lbl;
    private javax.swing.JButton ok_and_close_btn;
    private javax.swing.JButton ok_btn;
    private javax.swing.JComboBox pack_items_box;
    private javax.swing.JTable pack_items_jtable;
    private javax.swing.JComboBox piles_box;
    private javax.swing.JTextField qty_txt;
    private javax.swing.JButton refresh_btn;
    // End of variables declaration//GEN-END:variables
}
