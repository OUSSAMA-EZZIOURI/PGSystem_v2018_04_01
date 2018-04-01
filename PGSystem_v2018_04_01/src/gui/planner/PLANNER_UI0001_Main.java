/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.planner;

import __main__.GlobalVars;
import entity.ConfigShift;
import entity.ScheduleEntry;
import gui.packaging.PackagingVars;
import helper.ComboItem;
import helper.HQLHelper;
import helper.Helper;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;

/**
 *
 * @author Administrator
 */
public class PLANNER_UI0001_Main extends javax.swing.JFrame {

    ConfigShift cs = new ConfigShift();
    Vector<String> schedule_table_header = new Vector<String>();
    Vector schedule_table_data = new Vector();
    List<String> table_header = Arrays.asList(
            "ID",
            "Create Time",
            "Planned Day",
            "Shift",
            "Project",
            "Harness Part",
            "Supplier Part Number",
            "Index",
            "Pack Type",
            "Pack Size",
            "Planned Pack",
            "Qty Planned",
            "Qty Picked",
            "Qty Produced",
            "Qty remaining",
            "State"
    );

    /**
     * Creates new form PLANNER_UI0001_Main
     */
    public PLANNER_UI0001_Main() {
    }

    public PLANNER_UI0001_Main(Object[] context, JFrame parent) {
        initComponents();
        Helper.centerJFrame(this);
        Helper.loadProjectsInJbox(this.harness_type_box);
        Helper.loadShiftsInJbox(this.shift_box);
        day_date_picker.setDate(new Date());
        load_table_header();
        this.reload_table_data();
    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            schedule_table_header.add(it.next());
        }

        schedule_table.setModel(new DefaultTableModel(schedule_table_data, schedule_table_header));
    }

    private void reset_table_content() {

        schedule_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(schedule_table_data, schedule_table_header);
        schedule_table.setModel(dataModel);
    }

    private void reload_table_data() {
        this.reset_table_content();
        List result;
        try {
            result = get_all_schedule_data();
            for (Object o : result) {
                ScheduleEntry se = (ScheduleEntry) o;
                @SuppressWarnings("UseOfObsoleteCollectionType")
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(se.getId());
                oneRow.add(se.getCreateTimeString("dd-MM-yyyy HH:mm"));
                oneRow.add(new SimpleDateFormat("dd-MM-yyyy").format(se.getPlannedDay()));
                oneRow.add(se.getShift().getDescription());
                oneRow.add(se.getHarnessType());
                oneRow.add(se.getHarnessPart());
                oneRow.add(se.getSupplierPartNumber());
                oneRow.add(se.getHarnessIndex());
                oneRow.add(se.getPackType());
                oneRow.add(String.valueOf(se.getPackSize()));
                oneRow.add(String.valueOf(se.getPlannedPack()));
                oneRow.add(String.valueOf(se.getTotalPlanned()));
                oneRow.add(String.valueOf(se.getTotalPicked()));
                oneRow.add(String.valueOf(se.getTotalProduced()));
                oneRow.add(String.valueOf(se.getTotalRemaining()));
                oneRow.add(se.getEntryState());
                schedule_table_data.add(oneRow);
            }

            schedule_table.setModel(new DefaultTableModel(schedule_table_data, schedule_table_header));
            schedule_table.setAutoCreateRowSorter(true);
            this.disableEditingTable();
        } catch (Exception e) {
            log_txt.setText(log_txt.getText() + "\n - DB selection error : " + e.getMessage());
        }

    }

    public void disableEditingTable() {
        for (int c = 0; c < schedule_table.getColumnCount(); c++) {
            Class<?> col_class = schedule_table.getColumnClass(c);
            schedule_table.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    private List get_all_schedule_data() {
        Helper.sess.beginTransaction();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ALL_SCHEDULE_ENTRIES);
        Helper.sess.getTransaction().commit();
        List result = query.list();
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    private void clearFields() {
        day_date_picker.setDate(new Date());
        start_hour.setText("");
        start_minute.setText("");
        end_hour.setText("");
        end_minute.setText("");
        //shift_box.setSelectedIndex(0);
        harness_type_box.setSelectedIndex(0);
        harness_part_box.removeAllItems();
        supplier_part_box.removeAllItems();
        pack_size_box.removeAllItems();
        harness_index_box.removeAllItems();
        pack_type_box.removeAllItems();
        planned_pack_size.setSelectedIndex(0);
        total_planned_txt.setText("0");
        comment_txt.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        day_date_picker = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        shift_box = new javax.swing.JComboBox();
        start_hour = new javax.swing.JTextField();
        start_minute = new javax.swing.JTextField();
        end_hour = new javax.swing.JTextField();
        end_minute = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        total_planned_txt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ok_btn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        harness_type_box = new javax.swing.JComboBox();
        harness_part_box = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        refresh_btn = new javax.swing.JButton();
        clear_form_btn = new javax.swing.JButton();
        supplier_part_box = new javax.swing.JComboBox();
        harness_index_box = new javax.swing.JComboBox();
        pack_size_box = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        planned_pack_size = new javax.swing.JComboBox();
        pack_type_box = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        schedule_table = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        log_txt = new javax.swing.JTextArea();
        clear_log_btn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        comment_txt = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Planning form");

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        day_date_picker.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Day");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Shift");

        shift_box.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        shift_box.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-" }));
        shift_box.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                shift_boxItemStateChanged(evt);
            }
        });
        shift_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shift_boxActionPerformed(evt);
            }
        });

        start_hour.setEditable(false);
        start_hour.setBackground(new java.awt.Color(229, 229, 229));
        start_hour.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        start_hour.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        start_hour.setEnabled(false);
        start_hour.setMinimumSize(new java.awt.Dimension(50, 30));
        start_hour.setPreferredSize(new java.awt.Dimension(50, 30));

        start_minute.setEditable(false);
        start_minute.setBackground(new java.awt.Color(229, 229, 229));
        start_minute.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        start_minute.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        start_minute.setEnabled(false);
        start_minute.setMinimumSize(new java.awt.Dimension(50, 30));
        start_minute.setPreferredSize(new java.awt.Dimension(50, 30));

        end_hour.setEditable(false);
        end_hour.setBackground(new java.awt.Color(229, 229, 229));
        end_hour.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        end_hour.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        end_hour.setEnabled(false);
        end_hour.setMinimumSize(new java.awt.Dimension(50, 30));
        end_hour.setPreferredSize(new java.awt.Dimension(50, 30));

        end_minute.setEditable(false);
        end_minute.setBackground(new java.awt.Color(229, 229, 229));
        end_minute.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        end_minute.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        end_minute.setEnabled(false);
        end_minute.setMinimumSize(new java.awt.Dimension(50, 30));
        end_minute.setPreferredSize(new java.awt.Dimension(50, 30));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("UCS");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Planned Pack");

        total_planned_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        total_planned_txt.setText("0");
        total_planned_txt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        total_planned_txt.setMinimumSize(new java.awt.Dimension(6, 30));
        total_planned_txt.setPreferredSize(new java.awt.Dimension(6, 30));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Total Planned");

        ok_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/apply.png"))); // NOI18N
        ok_btn.setText("Save");
        ok_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_btnActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Project");

        harness_type_box.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        harness_type_box.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        harness_type_box.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                harness_type_boxItemStateChanged(evt);
            }
        });
        harness_type_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harness_type_boxActionPerformed(evt);
            }
        });

        harness_part_box.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        harness_part_box.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                harness_part_boxItemStateChanged(evt);
            }
        });
        harness_part_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harness_part_boxActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Harness Part");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Supplier Part Number");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Index");

        refresh_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/refresh.png"))); // NOI18N
        refresh_btn.setText("Refresh");
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        clear_form_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/edit-clear.png"))); // NOI18N
        clear_form_btn.setText("Clear Form");
        clear_form_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_form_btnActionPerformed(evt);
            }
        });

        supplier_part_box.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supplier_part_box.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        supplier_part_box.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                supplier_part_boxItemStateChanged(evt);
            }
        });
        supplier_part_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplier_part_boxActionPerformed(evt);
            }
        });

        harness_index_box.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        harness_index_box.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        harness_index_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harness_index_boxActionPerformed(evt);
            }
        });

        pack_size_box.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pack_size_box.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        pack_size_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pack_size_boxActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("to");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Pack Type");

        planned_pack_size.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        planned_pack_size.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01", "02", "03", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100" }));
        planned_pack_size.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planned_pack_sizeActionPerformed(evt);
            }
        });

        pack_type_box.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pack_type_box.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        pack_type_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pack_type_boxActionPerformed(evt);
            }
        });

        schedule_table.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(schedule_table);

        log_txt.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        log_txt.setColumns(10);
        log_txt.setRows(5);
        log_txt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        log_txt.setEnabled(false);
        jScrollPane1.setViewportView(log_txt);

        clear_log_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/edit-clear.png"))); // NOI18N
        clear_log_btn.setText("Clear Log");
        clear_log_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_log_btnActionPerformed(evt);
            }
        });

        comment_txt.setColumns(20);
        comment_txt.setRows(5);
        jScrollPane3.setViewportView(comment_txt);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Comment");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(day_date_picker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(shift_box, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(harness_index_box, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(pack_type_box, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(pack_size_box, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(harness_type_box, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(harness_part_box, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13))))
                            .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(supplier_part_box, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(start_hour, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(start_minute, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(end_hour, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(9, 9, 9)
                                    .addComponent(end_minute, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(ok_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)
                                        .addComponent(planned_pack_size, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(total_planned_txt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(clear_form_btn, javax.swing.GroupLayout.Alignment.TRAILING))))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 281, Short.MAX_VALUE)
                                .addComponent(clear_log_btn))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(shift_box, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(day_date_picker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addGap(35, 35, 35))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(harness_type_box, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(harness_part_box, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel11))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(harness_index_box, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                    .addComponent(pack_type_box, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(pack_size_box, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(clear_log_btn))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(refresh_btn))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(start_hour, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(start_minute, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(end_hour, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(end_minute, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(supplier_part_box, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(planned_pack_size, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(total_planned_txt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ok_btn)
                            .addComponent(clear_form_btn))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void harness_type_boxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_harness_type_boxItemStateChanged

    }//GEN-LAST:event_harness_type_boxItemStateChanged

    private void harness_type_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harness_type_boxActionPerformed
        try {
            this.harness_part_box.setEnabled(true);
            this.harness_part_box.removeAllItems();
            this.harness_part_box.addItem(new ComboItem("", ""));
            Helper.loadHarnessInJbox(
                    this.harness_part_box,
                    String.valueOf(this.harness_type_box.getSelectedItem()));
        } catch (Exception e) {
        }

    }//GEN-LAST:event_harness_type_boxActionPerformed

    private void harness_part_boxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_harness_part_boxItemStateChanged

    }//GEN-LAST:event_harness_part_boxItemStateChanged

    private void shift_boxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_shift_boxItemStateChanged

    }//GEN-LAST:event_shift_boxItemStateChanged

    private void shift_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shift_boxActionPerformed

        try {
            this.cs = this.cs.selectShiftByDesc(String.valueOf(shift_box.getSelectedItem()));
            start_hour.setText(this.cs.getStartHour());
            start_minute.setText(this.cs.getStartMinute());
            end_hour.setText(this.cs.getEndHour());
            end_minute.setText(this.cs.getEndMinute());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            start_hour.setText("");
            start_minute.setText("");
            end_hour.setText("");
            end_minute.setText("");
        }

    }//GEN-LAST:event_shift_boxActionPerformed

    private void clear_form_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_form_btnActionPerformed
        clearFields();
    }//GEN-LAST:event_clear_form_btnActionPerformed

    private void supplier_part_boxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_supplier_part_boxItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_supplier_part_boxItemStateChanged

    private void supplier_part_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplier_part_boxActionPerformed

        try {
            this.harness_index_box.setEnabled(true);
            this.harness_index_box.removeAllItems();
            this.harness_index_box.addItem(new ComboItem("", ""));
            Helper.loadIndexInJbox(
                    this.harness_index_box,
                    String.valueOf(this.harness_type_box.getSelectedItem()),
                    String.valueOf(this.harness_part_box.getSelectedItem()),
                    String.valueOf(this.supplier_part_box.getSelectedItem()));
        } catch (Exception e) {
        }

    }//GEN-LAST:event_supplier_part_boxActionPerformed

    private void harness_part_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harness_part_boxActionPerformed
        try {
            this.supplier_part_box.setEnabled(true);
            this.supplier_part_box.removeAllItems();
            this.supplier_part_box.addItem(new ComboItem("", ""));
            Helper.loadSupplierPartInJbox(
                    this.supplier_part_box,
                    String.valueOf(this.harness_type_box.getSelectedItem()),
                    String.valueOf(this.harness_part_box.getSelectedItem()));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_harness_part_boxActionPerformed

    private void harness_index_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harness_index_boxActionPerformed
        try {
            this.pack_type_box.setEnabled(true);
            this.pack_type_box.removeAllItems();
            this.pack_type_box.addItem(new ComboItem("", ""));
            Helper.loadPackTypeInJbox(
                    this.pack_type_box,
                    String.valueOf(this.harness_type_box.getSelectedItem()),
                    String.valueOf(this.harness_part_box.getSelectedItem()),
                    String.valueOf(this.supplier_part_box.getSelectedItem()),
                    String.valueOf(this.harness_index_box.getSelectedItem()));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_harness_index_boxActionPerformed

    private void planned_pack_sizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planned_pack_sizeActionPerformed
        try {
            this.calculateTotalPlanned();
        } catch (Exception e) {
            total_planned_txt.setText("");
            System.out.println("" + e.getMessage());
        }
    }//GEN-LAST:event_planned_pack_sizeActionPerformed

    private void pack_size_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pack_size_boxActionPerformed
        try {
            this.calculateTotalPlanned();
        } catch (Exception e) {
            total_planned_txt.setText("");
            System.out.println("" + e.getMessage());
        }
    }//GEN-LAST:event_pack_size_boxActionPerformed

    private void pack_type_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pack_type_boxActionPerformed
        try {
            this.pack_size_box.setEnabled(true);
            this.pack_size_box.removeAllItems();
            this.pack_size_box.addItem(new ComboItem("", ""));
            Helper.loadPackSizeInJbox(
                    this.pack_size_box,
                    String.valueOf(this.harness_type_box.getSelectedItem()),
                    String.valueOf(this.harness_part_box.getSelectedItem()),
                    String.valueOf(this.supplier_part_box.getSelectedItem()),
                    String.valueOf(this.harness_index_box.getSelectedItem()),
                    String.valueOf(this.pack_type_box.getSelectedItem()));
        } catch (Exception e) {
        }
    }//GEN-LAST:event_pack_type_boxActionPerformed

    private void ok_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_btnActionPerformed
        ScheduleEntry se = new ScheduleEntry();
        try {
            int total_planned = 0;
            try {
                total_planned = Integer.parseInt(total_planned_txt.getText());                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid total planned value. Integer required." + e.getMessage(), "Integer format Error", JOptionPane.ERROR_MESSAGE);
            }
            this.cs = cs.selectShiftByDesc(String.valueOf(shift_box.getSelectedItem()));
            se.setCreateTime(new Date());
            se.setWriteTime(new Date());
            se.setCreateId(PackagingVars.context.getUser().getId());
            se.setWriteId(PackagingVars.context.getUser().getId());
            se.setHarnessType(String.valueOf(harness_type_box.getSelectedItem()));
            se.setHarnessPart(String.valueOf(harness_part_box.getSelectedItem()));
            se.setHarnessIndex(String.valueOf(harness_index_box.getSelectedItem()));
            se.setPackType(String.valueOf(pack_type_box.getSelectedItem()));
            se.setPackSize(Integer.valueOf(String.valueOf(pack_size_box.getSelectedItem())));
            se.setSupplierPartNumber(String.valueOf(supplier_part_box.getSelectedItem()));
            se.setPlannedDay(day_date_picker.getDate());
            se.setStartHour(this.cs.getEndHour());
            se.setStartMinute(this.cs.getEndMinute());
            se.setEndHour(this.cs.getEndHour());
            se.setEndMinute(this.cs.getEndMinute());
            se.setPlannedPack(Float.valueOf(String.valueOf(planned_pack_size.getSelectedItem())));
            se.setTotalPlanned(Float.valueOf(total_planned));
            se.setTotalPicked((float) 0);
            se.setTotalProduced((float) 0);
            se.setTotalRemaining((float) 0);
            se.setShiftName(this.cs.getName());
            se.setShift(this.cs);
            se.setEntryState(GlobalVars.SCHEDULE_STATE_NEW);
            se.setEntryStateCode(GlobalVars.SCHEDULE_STATE_NEW_CODE);
            se.setComment(comment_txt.getText());
            se.create(se);
            clearFields();
            log_txt.setText(log_txt.getText() + "- New entry saved !");
            log_txt.setText(log_txt.getText() + "\n" + "- " + se.toString());

            //Reload Data Table
            reload_table_data();
        } catch (NumberFormatException e) {
            log_txt.setText(log_txt.getText() + "\n" + "- Saving error " + e.getMessage());
        }


    }//GEN-LAST:event_ok_btnActionPerformed

    private void clear_log_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_log_btnActionPerformed
        log_txt.setText("");
    }//GEN-LAST:event_clear_log_btnActionPerformed

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        reload_table_data();
    }//GEN-LAST:event_refresh_btnActionPerformed

    public void calculateTotalPlanned() {
        int ucs = Integer.valueOf(String.valueOf(planned_pack_size.getSelectedItem()));
        int planned_pack = Integer.valueOf(String.valueOf(pack_size_box.getSelectedItem()));
        total_planned_txt.setText(String.valueOf(ucs * planned_pack));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clear_form_btn;
    private javax.swing.JButton clear_log_btn;
    private javax.swing.JTextArea comment_txt;
    private org.jdesktop.swingx.JXDatePicker day_date_picker;
    private javax.swing.JTextField end_hour;
    private javax.swing.JTextField end_minute;
    private javax.swing.JComboBox harness_index_box;
    private javax.swing.JComboBox harness_part_box;
    private javax.swing.JComboBox harness_type_box;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea log_txt;
    private javax.swing.JButton ok_btn;
    private javax.swing.JComboBox pack_size_box;
    private javax.swing.JComboBox pack_type_box;
    private javax.swing.JComboBox planned_pack_size;
    private javax.swing.JButton refresh_btn;
    private javax.swing.JTable schedule_table;
    private javax.swing.JComboBox shift_box;
    private javax.swing.JTextField start_hour;
    private javax.swing.JTextField start_minute;
    private javax.swing.JComboBox supplier_part_box;
    private javax.swing.JTextField total_planned_txt;
    // End of variables declaration//GEN-END:variables
}
