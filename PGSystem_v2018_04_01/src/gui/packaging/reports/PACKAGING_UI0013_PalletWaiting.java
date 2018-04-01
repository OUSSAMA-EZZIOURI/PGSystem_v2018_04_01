/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.reports;

import __main__.GlobalVars;
import helper.Helper;
import helper.HQLHelper;
import entity.BaseContainer;
import gui.packaging.PackagingVars;
import gui.packaging.mode1.state.Mode1_S050_ClosingPallet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import gui.packaging.mode2.state.Mode2_S040_ClosingPallet;

/**
 *
 * @author user
 */
public class PACKAGING_UI0013_PalletWaiting extends javax.swing.JDialog {

    Vector<String> container_table_header = new Vector<String>();
    Vector container_table_data = new Vector();
    private static int STATE_COLINDEX = 9;
    private static int PALLET_NUMBER_COLINDEX = 0;

    public PACKAGING_UI0013_PalletWaiting(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        //Load table header
        this.load_container_table_header();

        //L'utilisateur est déconnecter, afficher tout les container etat waiting
        //Masquer le bouton continue
        if (PackagingVars.mode2_context.getBaseContainerTmp().getHarnessType() == null) {
            loadContainer("ALL");
            continue_btn.setVisible(false);
        } else {
            loadContainer(PackagingVars.mode2_context.getBaseContainerTmp().getHarnessType());
        }

        //Desable table edition
        this.disableEditingTable();

        this.container_table.addMouseListener(
                new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    System.out.println("Pallet Number " + String.valueOf(container_table.getValueAt(container_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)));
                    //new PACKAGING_UI0013_PalletWaiting().searchForPallet(String.valueOf(container_table.getValueAt(container_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)));
                    searchForPallet(String.valueOf(container_table.getValueAt(container_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)));
                    continue_btn.setEnabled(true);

                }
            }
        }
        );

        this.continue_btn.setEnabled(false);
        this.setVisible(true);
    }

    private void setContainerFieldsValues(BaseContainer bc) {
        palletNumber_txtbox.setText(bc.getPalletNumber());
        user_txtbox.setText(bc.getUser());
        harnessPart_txtbox.setText(bc.getHarnessPart());
        index_txtbox.setText(bc.getHarnessIndex());
        supplierPartNumber_txtbox.setText(bc.getSupplierPartNumber());
        qtyExptected_txtbox.setText(String.valueOf(bc.getQtyExpected()));
        qtyRead_txtbox.setText(String.valueOf(bc.getQtyRead()));
        packType_txtbox.setText(bc.getPackType());
        state_txtbox.setText(bc.getContainerState());
        startTime_txtbox.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bc.getStartTime()));
    }

    private void clearContainerFieldsValues() {
        palletNumber_txtbox.setText("");
        user_txtbox.setText("");
        harnessPart_txtbox.setText("");
        index_txtbox.setText("");
        supplierPartNumber_txtbox.setText("");
        qtyExptected_txtbox.setText("");
        qtyRead_txtbox.setText("");
        packType_txtbox.setText("");
        state_txtbox.setText("");
        startTime_txtbox.setText("");
    }

    public void searchForPallet(String palletNumber) {

        this.clearContainerFieldsValues();

        if (!palletNumber.trim().equals("")) {

            //################# Container Data ####################
            //Start transaction                
            Helper.startSession();
            Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_NUMBER);
            query.setParameter("palletNumber", palletNumber.trim());
            Helper.sess.getTransaction().commit();
            List result = query.list();
            this.setContainerFieldsValues((BaseContainer) result.get(0));

        } else {
            JOptionPane.showMessageDialog(null, "Empty pallet number", "Pallet Number Error", JOptionPane.ERROR_MESSAGE);
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

        form_pane = new javax.swing.JPanel();
        table_scroll = new javax.swing.JScrollPane();
        container_table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        user_txtbox = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        harnessPart_txtbox = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        index_txtbox = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        supplierPartNumber_txtbox = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        state_txtbox = new javax.swing.JTextField();
        packType_txtbox = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        qtyRead_txtbox = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        qtyExptected_txtbox = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        startTime_txtbox = new javax.swing.JTextField();
        continue_btn = new javax.swing.JButton();
        palletNumber_txtbox = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Waiting Pallet List");
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        container_table.setAutoCreateRowSorter(true);
        container_table.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        container_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        table_scroll.setViewportView(container_table);

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("Waiting Pallet List");

        jLabel2.setText("User Number");

        user_txtbox.setEditable(false);
        user_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        user_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_txtboxActionPerformed(evt);
            }
        });

        jLabel8.setText("Harness Part");

        harnessPart_txtbox.setEditable(false);
        harnessPart_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        harnessPart_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harnessPart_txtboxActionPerformed(evt);
            }
        });

        jLabel9.setText("Index");

        index_txtbox.setEditable(false);
        index_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        index_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                index_txtboxActionPerformed(evt);
            }
        });

        jLabel10.setText("Supplier Part Number");

        supplierPartNumber_txtbox.setEditable(false);
        supplierPartNumber_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        supplierPartNumber_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierPartNumber_txtboxActionPerformed(evt);
            }
        });

        jLabel5.setText("State");

        state_txtbox.setEditable(false);
        state_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        state_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                state_txtboxActionPerformed(evt);
            }
        });

        packType_txtbox.setEditable(false);
        packType_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        packType_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                packType_txtboxActionPerformed(evt);
            }
        });

        jLabel11.setText("Pack Type");

        qtyRead_txtbox.setEditable(false);
        qtyRead_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        qtyRead_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyRead_txtboxActionPerformed(evt);
            }
        });

        jLabel4.setText("Quantity Read");

        qtyExptected_txtbox.setEditable(false);
        qtyExptected_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        qtyExptected_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyExptected_txtboxActionPerformed(evt);
            }
        });

        jLabel3.setText("Quantity Expected");

        jLabel6.setText("Start Time");

        startTime_txtbox.setEditable(false);
        startTime_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        startTime_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startTime_txtboxActionPerformed(evt);
            }
        });

        continue_btn.setBackground(java.awt.Color.cyan);
        continue_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        continue_btn.setText("Continue");
        continue_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continue_btnActionPerformed(evt);
            }
        });

        palletNumber_txtbox.setEditable(false);
        palletNumber_txtbox.setBackground(new java.awt.Color(255, 255, 255));
        palletNumber_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                palletNumber_txtboxActionPerformed(evt);
            }
        });

        jLabel7.setText("Pallet Number");

        javax.swing.GroupLayout form_paneLayout = new javax.swing.GroupLayout(form_pane);
        form_pane.setLayout(form_paneLayout);
        form_paneLayout.setHorizontalGroup(
            form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table_scroll)
            .addGroup(form_paneLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(form_paneLayout.createSequentialGroup()
                .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(form_paneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supplierPartNumber_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(palletNumber_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(startTime_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(state_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(qtyExptected_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(user_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(qtyRead_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(harnessPart_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11)
                            .addComponent(jLabel9)
                            .addComponent(index_txtbox, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(packType_txtbox)))
                    .addGroup(form_paneLayout.createSequentialGroup()
                        .addGap(386, 386, 386)
                        .addComponent(continue_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(448, Short.MAX_VALUE))
        );
        form_paneLayout.setVerticalGroup(
            form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(form_paneLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(index_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(harnessPart_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(user_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(palletNumber_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10)
                    .addComponent(jLabel4)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(packType_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtyRead_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtyExptected_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplierPartNumber_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(form_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startTime_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(state_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(continue_btn))
                .addGap(23, 23, 23)
                .addComponent(table_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(form_pane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(form_pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void user_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user_txtboxActionPerformed

    private void harnessPart_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harnessPart_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_harnessPart_txtboxActionPerformed

    private void index_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_index_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_index_txtboxActionPerformed

    private void supplierPartNumber_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierPartNumber_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierPartNumber_txtboxActionPerformed

    private void state_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_state_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_state_txtboxActionPerformed

    private void packType_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_packType_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_packType_txtboxActionPerformed

    private void qtyRead_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyRead_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qtyRead_txtboxActionPerformed

    private void qtyExptected_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyExptected_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qtyExptected_txtboxActionPerformed

    private void startTime_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startTime_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startTime_txtboxActionPerformed

    private void palletNumber_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_palletNumber_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_palletNumber_txtboxActionPerformed

    private void continue_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continue_btnActionPerformed

        if (GlobalVars.APP_PROP.getProperty("PACKAGING_SCAN_MODE").equals("1")) {
            BaseContainer bc = new BaseContainer().getBaseContainer(palletNumber_txtbox.getText());

            PackagingVars.mode2_context.getBaseContainerTmp().setPalletNumber(bc.getPalletNumber());
            //Set requested closing pallet number in the main gui
            PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea("Scanner le code palette N° " + GlobalVars.CLOSING_PALLET_PREFIX + bc.getPalletNumber());
            //############# PASSE TO S050 STATE ###############        
            PackagingVars.Packaging_Gui_Mode1.state = new Mode1_S050_ClosingPallet();
            this.dispose();

        } else if (GlobalVars.APP_PROP.getProperty("PACKAGING_SCAN_MODE").equals("2")) {
            BaseContainer bc = new BaseContainer().getBaseContainer(palletNumber_txtbox.getText());

            PackagingVars.mode2_context.getBaseContainerTmp().setPalletNumber(bc.getPalletNumber());
            //Set requested closing pallet number in the main gui
            PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea("Scanner le code palette N° " + GlobalVars.CLOSING_PALLET_PREFIX + bc.getPalletNumber());
            //############# PASSE TO S050 STATE ###############        
            PackagingVars.Packaging_Gui_Mode2.state = new Mode2_S040_ClosingPallet();
            this.dispose();
        }

    }//GEN-LAST:event_continue_btnActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    }//GEN-LAST:event_formWindowClosing

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_formKeyPressed

    public void load_container_table_header() {
        this.reset_container_table_content();

        container_table_header.add("Pallet Number");
        container_table_header.add("Harness Part");
        container_table_header.add("Harness Type");
        container_table_header.add("Qty Expected");
        container_table_header.add("Qty Read");
        container_table_header.add("Index");
        container_table_header.add("Create Time");
        container_table_header.add("Supplier Part");
        container_table_header.add("User");
        container_table_header.add("State");

        container_table.setModel(new DefaultTableModel(container_table_data, container_table_header));
    }

    public void reset_container_table_content() {
        container_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(container_table_data, container_table_header);
        container_table.setModel(dataModel);
    }

    public void reload_container_table_data(List resultList) {
        this.reset_container_table_content();

        for (Object o : resultList) {
            BaseContainer bc = (BaseContainer) o;
            @SuppressWarnings("UseOfObsoleteCollectionType")
            Vector<Object> oneRow = new Vector<Object>();

            oneRow.add(bc.getPalletNumber());
            oneRow.add(bc.getHarnessPart());
            oneRow.add(bc.getHarnessType());
            oneRow.add(bc.getQtyExpected());
            oneRow.add(bc.getQtyRead());
            oneRow.add(bc.getHarnessIndex());
            oneRow.add(bc.getCreateTimeString("dd/MM/yy HH:mm"));
            oneRow.add(bc.getSupplierPartNumber());
            oneRow.add(bc.getUser());
            oneRow.add(bc.getContainerState());
            container_table_data.add(oneRow);
        }

        container_table.setModel(new DefaultTableModel(container_table_data, container_table_header));
        container_table.setAutoCreateRowSorter(true);

        //Initialize default style for table container
        setContainerTableRowsStyle();
    }

    private void disableEditingTable() {
        for (int c = 0; c < container_table.getColumnCount(); c++) {
            Class<?> col_class = container_table.getColumnClass(c);
            container_table.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    public void setContainerTableRowsStyle() {
        //Initialize default style for table container

        //#######################
        container_table.setFont(new Font(String.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONT")), Font.BOLD, 14));
        container_table.setRowHeight(Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_ROW_HEIGHT")));
        container_table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                String status = (String) table.getModel().getValueAt(row, STATE_COLINDEX);
                //############### OPEN
                if (GlobalVars.PALLET_OPEN.equals(status)) {
                    setBackground(Color.YELLOW);
                    setForeground(Color.BLACK);
                } //############### CLOSED
                else if (GlobalVars.PALLET_CLOSED.equals(status)) {
                    setBackground(Color.LIGHT_GRAY);
                    setForeground(Color.BLACK);
                } //############### QUARANTAINE
                else if (GlobalVars.PALLET_QUARANTAINE.equals(status)) {
                    setBackground(Color.RED);
                    setForeground(Color.BLACK);
                } //############### OPEN
                else if (GlobalVars.PALLET_WAITING.equals(status)) {
                    setBackground(Color.CYAN);
                    setForeground(Color.BLACK);
                }

                setHorizontalAlignment(JLabel.CENTER);

                return this;
            }
        });
        //#######################
        this.disableEditingTable();
    }

    private void loadContainer(String harnessType) {
        List<Object> states = new ArrayList<Object>();
        List<Object> projects = new ArrayList<Object>();

        //################# Harness Data ####################
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_STATES);
        /*
        if (harnessType.equals(Helper.ENGINE)) {
            projects.add(Helper.ENGINE);
        }
        if (harnessType.equals(Helper.SMALL)) {
            projects.add(Helper.SMALL);
        }
        if (harnessType.equals("ALL")) {
            projects.add(Helper.SMALL);
            projects.add(Helper.ENGINE);
        }*/
        states.add(GlobalVars.PALLET_WAITING);

        query.setParameterList("states", states);
        //query.setParameterList("projects", projects);

        Helper.sess.getTransaction().commit();
        List result = query.list();

        if (result.isEmpty()) {
            this.reset_container_table_content();
        } else {
            //reload table data                
            this.reload_container_table_data(result);
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable container_table;
    private javax.swing.JButton continue_btn;
    private javax.swing.JPanel form_pane;
    private javax.swing.JTextField harnessPart_txtbox;
    private javax.swing.JTextField index_txtbox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField packType_txtbox;
    private javax.swing.JTextField palletNumber_txtbox;
    private javax.swing.JTextField qtyExptected_txtbox;
    private javax.swing.JTextField qtyRead_txtbox;
    private javax.swing.JTextField startTime_txtbox;
    private javax.swing.JTextField state_txtbox;
    private javax.swing.JTextField supplierPartNumber_txtbox;
    private javax.swing.JScrollPane table_scroll;
    private javax.swing.JTextField user_txtbox;
    // End of variables declaration//GEN-END:variables
}
