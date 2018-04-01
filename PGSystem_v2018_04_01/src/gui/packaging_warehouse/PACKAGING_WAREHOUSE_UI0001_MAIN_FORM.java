/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging_warehouse;

import __main__.StartFrame;
import entity.ConfigWarehouse;
import entity.LoadPlanLinePackaging;
import entity.PackagingItems;
import entity.PackagingStockMovement;
import helper.ComboItem;
import helper.Helper;
import helper.UIHelper;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author user
 */
public class PACKAGING_WAREHOUSE_UI0001_MAIN_FORM extends javax.swing.JFrame {

    private StartFrame parent;
    Vector<String> transactions_table_header = new Vector<String>();

    List<String> header_columns = Arrays.asList(
            "Num Ligne",
            "Utilisateur",
            "Magasin",
            "Document ID",
            "FIFO",
            "Quantité",
            "Pack Item",
            "Pack Master",
            "Comment"
    );
    Vector transactions_table_data = new Vector();
    PackagingItems itemsAux;
    public LoadPlanLinePackaging packLineAux;
    SimpleDateFormat dateDf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Creates new form WAREHOUSE_DISPATCH_UI0008_SET_PACKAGING_OF_PILE
     */
    public PACKAGING_WAREHOUSE_UI0001_MAIN_FORM(StartFrame parent, boolean modal) {
        this.parent = parent;
    }

    PACKAGING_WAREHOUSE_UI0001_MAIN_FORM(Object object, StartFrame parent) {
        this.parent = parent;
        initComponents();
        initGui();

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

    private void initWarehouseItemsBox() {
        warehouse_box.removeAllItems();
        warehouse_box_filter.removeAllItems();
        warehouse_box_filter.addItem(new ComboItem("", ""));
        List result = new ConfigWarehouse().selectAllWarehouses();
        //Map project data in the list
        for (Object o : result) {
            ConfigWarehouse wh = (ConfigWarehouse) o;
            warehouse_box.addItem(new ComboItem(wh.getWarehouse(), wh.getWarehouse()));
            warehouse_box_filter.addItem(new ComboItem(wh.getWarehouse(), wh.getWarehouse()));
        }

    }

    private void initGui() {
        initWarehouseItemsBox();
        initPackItemsBox();
        fifoDateStartFilter.setDate(new Date());
        fifoDateEndFilter.setDate(new Date());
        initRequiredFields();
        initItemsTabUIjtableHeader();
        UIHelper.disableEditingJtable(transactions_jtable);

        Helper.centerJFrame(this);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
        this.setVisible(true);

        refresh();
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
                System.out.println("USER " + PackagingHelper.user.getFirstName() + " "
                        + PackagingHelper.user.getLastName());
                PackagingStockMovement transaction
                        = new PackagingStockMovement(
                                pack_items_box.getSelectedItem().toString(),
                                "", document_id_txt.getText(),
                                PackagingHelper.user.getFirstName() + " "
                                + PackagingHelper.user.getLastName(),
                                new Date(), warehouse_box.getSelectedItem().toString(),
                                Float.valueOf(qty_txt.getText().toString().trim()),
                                comment_txt.getText());
                transaction.create(transaction);
                clearFormFields();
                msg_lbl.setText("Transaction postée !");
            } else { // ID field is full

            }
        }
    }

    /**
     * Set required field background color to
     *
     * @param args : Components to highlight
     */
    private void initRequiredFields() {
        UIHelper.highlightRequiredFields(
                this.warehouse_box,
                this.pack_items_box,
                this.qty_txt,
                this.document_id_txt);
    }

    /**
     * Reset the content of Packagin Items jtable header
     */
    private void initItemsTabUIjtableHeader() {
        for (Iterator<String> it = header_columns.iterator(); it.hasNext();) {
            transactions_table_header.add(it.next());
        }

        transactions_jtable.setModel(new DefaultTableModel(transactions_table_data, transactions_table_header));
    }

    public void resetJtableContent() {
        //Reset the jtable
        transactions_table_data = new Vector();
        DefaultTableModel itemsDataModel = new DefaultTableModel(transactions_table_data, transactions_table_header);
        transactions_jtable.removeAll();
        transactions_jtable.setModel(itemsDataModel);
    }

    /**
     * @param table_data
     * @param table_header
     * @param jtable
     * @todo : reload_table_data a mettre dans une classe interface
     * @param resultList
     */
    public void reload_table_data(List<Object[]> resultList, Vector table_data, Vector<String> table_header, JTable jtable) {

        resetJtableContent();

        for (Object[] obj : resultList) {
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(obj[0]);
            oneRow.add(obj[1]);
            oneRow.add(obj[2]);
            oneRow.add(obj[3]);            
            oneRow.add(obj[4]);
            oneRow.add(obj[5]);
            oneRow.add(obj[6]);
            oneRow.add(obj[7]);
            oneRow.add(obj[8]);
            table_data.add(oneRow);
        }

        jtable.setModel(new DefaultTableModel(table_data, table_header));
        jtable.setAutoCreateRowSorter(true);
    }

    private void clearFormFields() {
        UIHelper.clearTextFields(this.id_txt, this.qty_txt, this.comment_txt, this.pack_items_box, this.warehouse_box);
    }

    private void refresh() {
        String warehouse  = "%";
        
        String fifoStartDate = dateDf.format(fifoDateStartFilter.getDate());
        String fifoEndDate = dateDf.format(fifoDateEndFilter.getDate());

        String query_str = "SELECT id, create_user, warehouse, document_id, "
                + "fifo_time, quantity, pack_item, pack_master, comment "
                + "FROM packaging_stock_movement p "
                + "WHERE fifo_time BETWEEN '%s 00:00:00' AND '%s 23:59:59' AND warehouse LIKE '%s' ";
        if (!warehouse_box_filter.getSelectedItem().toString().equals("")) {
            warehouse = warehouse_box_filter.getSelectedItem()+"";
        }
        query_str += " ORDER BY fifo_time DESC";
        query_str = String.format(query_str, fifoStartDate, fifoEndDate, warehouse);

        Helper.startSession();
        //Select only harness parts with UCS completed.                                
        SQLQuery query = Helper.sess.createSQLQuery(query_str);
        query.addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("create_user", StandardBasicTypes.STRING)
                .addScalar("warehouse", StandardBasicTypes.STRING)
                .addScalar("document_id", StandardBasicTypes.STRING)
                .addScalar("fifo_time", StandardBasicTypes.TIMESTAMP)
                .addScalar("quantity", StandardBasicTypes.FLOAT)
                .addScalar("pack_item", StandardBasicTypes.STRING)
                .addScalar("pack_master", StandardBasicTypes.STRING)
                .addScalar("comment", StandardBasicTypes.STRING);

        List<Object[]> result = query.list();
        Helper.sess.getTransaction().commit();

        this.reload_table_data(result, transactions_table_data, transactions_table_header, transactions_jtable);
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
        jLabel6 = new javax.swing.JLabel();
        ok_and_close_btn = new javax.swing.JButton();
        msg_lbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        transactions_jtable = new javax.swing.JTable();
        refresh_btn = new javax.swing.JButton();
        id_txt = new javax.swing.JTextField();
        fifoDateStartFilter = new org.jdesktop.swingx.JXDatePicker();
        warehouse_box_filter = new javax.swing.JComboBox();
        fname_lbl14 = new javax.swing.JLabel();
        fname_lbl15 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        fifoDateEndFilter = new org.jdesktop.swingx.JXDatePicker();
        jPanel1 = new javax.swing.JPanel();
        warehouse_box = new javax.swing.JComboBox();
        fname_lbl13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        qty_txt = new javax.swing.JTextField();
        fname_lbl12 = new javax.swing.JLabel();
        fname_lbl11 = new javax.swing.JLabel();
        pack_items_box = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        comment_txt = new javax.swing.JTextArea();
        pack_master_txt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        document_id_txt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transactions stock packaging");
        setBackground(new java.awt.Color(194, 227, 254));

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

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Transactions stock packaging");

        ok_and_close_btn.setText("Sauvegarder et fermer");
        ok_and_close_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_and_close_btnActionPerformed(evt);
            }
        });

        msg_lbl.setBackground(new java.awt.Color(204, 204, 255));
        msg_lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        msg_lbl.setForeground(new java.awt.Color(0, 0, 255));

        transactions_jtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(transactions_jtable);

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

        fifoDateStartFilter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        warehouse_box_filter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        fname_lbl14.setText("Warehouse");

        fname_lbl15.setText("FIFO Date du");

        fifoDateEndFilter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        warehouse_box.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        fname_lbl13.setText("Warehouse");

        jLabel4.setText("Quantité");

        qty_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qty_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qty_txtActionPerformed(evt);
            }
        });

        fname_lbl12.setText("Commentaire");

        fname_lbl11.setText("Pack Item");

        pack_items_box.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        comment_txt.setColumns(20);
        comment_txt.setRows(5);
        jScrollPane2.setViewportView(comment_txt);

        pack_master_txt.setEditable(false);
        pack_master_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pack_master_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pack_master_txtActionPerformed(evt);
            }
        });

        jLabel5.setText("Master Pack");

        document_id_txt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        document_id_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                document_id_txtActionPerformed(evt);
            }
        });

        jLabel7.setText("Document ID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(fname_lbl12)
                        .addGap(28, 60, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fname_lbl13, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(warehouse_box, 0, 116, Short.MAX_VALUE)
                            .addComponent(qty_txt))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fname_lbl11)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pack_items_box, 0, 235, Short.MAX_VALUE)
                            .addComponent(pack_master_txt)))
                    .addComponent(document_id_txt))
                .addContainerGap(142, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pack_items_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fname_lbl11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(warehouse_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fname_lbl13, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qty_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(pack_master_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(document_id_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fname_lbl12)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton1.setText("Stock packaging ...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(msg_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(id_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(ok_btn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ok_and_close_btn)))))
                        .addGap(0, 192, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fname_lbl14)
                    .addComponent(fname_lbl15))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(warehouse_box_filter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fifoDateStartFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fifoDateEndFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancel_btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(id_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ok_and_close_btn)
                    .addComponent(ok_btn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msg_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fname_lbl14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(warehouse_box_filter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(fifoDateStartFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fname_lbl15)
                    .addComponent(fifoDateEndFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refresh_btn)
                    .addComponent(cancel_btn)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
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

    private void qty_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qty_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qty_txtActionPerformed

    private void ok_and_close_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_and_close_btnActionPerformed
        this.postForm();
        this.dispose();
    }//GEN-LAST:event_ok_and_close_btnActionPerformed

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        refresh();
    }//GEN-LAST:event_refresh_btnActionPerformed

    private void id_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_txtActionPerformed

    private void pack_master_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pack_master_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pack_master_txtActionPerformed

    private void document_id_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_document_id_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_document_id_txtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        PACKAGING_WAREHOUSE_UI0002_STOCK packaging_warehouse_uI0002_STOCK = new PACKAGING_WAREHOUSE_UI0002_STOCK(this, true);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel_btn;
    private javax.swing.JTextArea comment_txt;
    private javax.swing.JTextField document_id_txt;
    private org.jdesktop.swingx.JXDatePicker fifoDateEndFilter;
    private org.jdesktop.swingx.JXDatePicker fifoDateStartFilter;
    private javax.swing.JLabel fname_lbl11;
    private javax.swing.JLabel fname_lbl12;
    private javax.swing.JLabel fname_lbl13;
    private javax.swing.JLabel fname_lbl14;
    private javax.swing.JLabel fname_lbl15;
    private javax.swing.JTextField id_txt;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel msg_lbl;
    private javax.swing.JButton ok_and_close_btn;
    private javax.swing.JButton ok_btn;
    private javax.swing.JComboBox pack_items_box;
    private javax.swing.JTextField pack_master_txt;
    public javax.swing.JTextField qty_txt;
    private javax.swing.JButton refresh_btn;
    private javax.swing.JTable transactions_jtable;
    private javax.swing.JComboBox warehouse_box;
    private javax.swing.JComboBox warehouse_box_filter;
    // End of variables declaration//GEN-END:variables

}
