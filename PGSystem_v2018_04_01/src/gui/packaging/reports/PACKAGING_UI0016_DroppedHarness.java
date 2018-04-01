/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.reports;

import __main__.GlobalVars;
import entity.ConfigProject;
import helper.ComboItem;
import helper.Helper;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author Administrator
 */
public class PACKAGING_UI0016_DroppedHarness extends javax.swing.JDialog {

    Vector<String> dropped_result_table_header = new Vector<String>();
    Vector dropped_result_table_data = new Vector();
    List<String> table_header = Arrays.asList(
            "Project",
            "Pack Number",
            "Harness Part",
            "Removal Time",            
            "User",
            "User Code",
            "Drop MSG"
    );

    /**
     * Creates new form UI0011_ProdStatistics_
     */
    public PACKAGING_UI0016_DroppedHarness(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.initGui();
        this.refresh();
        Helper.centerJDialog(this);
    }

    private void initGui() {
        //Center the this dialog in the screen
        Helper.centerJDialog(this);

        //Desable table edition
        disableEditingTables();

        //Load table header
        load_table_header();

        //Init time spinner
        initTimeSpinners();

        //Init projects filter
        initProjectsFilter();

    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            dropped_result_table_header.add(it.next());
        }

        dropped_result_table.setModel(new DefaultTableModel(dropped_result_table_data, dropped_result_table_header));
    }

    private void initProjectsFilter() {
        List result = new ConfigProject().select();
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(null, Helper.ERR0014_NO_PROJECT_FOUND, "Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0014_NO_PROJECT_FOUND);
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigProject cp = (ConfigProject) o;
                harness_type_filter.addItem(new ComboItem(cp.getHarnessType(), cp.getHarnessType()));
            }
        }
    }

    private void initTimeSpinners() {

        String startTime = GlobalVars.APP_PROP.getProperty("START_TIME");
        String endTime = GlobalVars.APP_PROP.getProperty("END_TIME");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");

        //################# Start Time Spinner ####################
        startTimeSpinner.setModel(new SpinnerDateModel());
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
        startTimeSpinner.setEditor(startTimeEditor);
        try {
            startTimeSpinner.setValue(timeFormat.parse(startTime));
        } catch (ParseException ex) {
            Logger.getLogger(PACKAGING_UI0016_DroppedHarness.class.getName()).log(Level.SEVERE, null, ex);
        }

        //################# End Time Spinner ######################
        endTimeSpinner.setModel(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endTimeEditor);
        try {
            endTimeSpinner.setValue(timeFormat.parse(endTime));
        } catch (ParseException ex) {
            Logger.getLogger(PACKAGING_UI0016_DroppedHarness.class.getName()).log(Level.SEVERE, null, ex);
        }

        startDatePicker.setDate(new Date());
        endDatePicker.setDate(new Date());

    }

    public void reset_table_content() {
        dropped_result_table_data = new Vector();
        DefaultTableModel droppedDataModel = new DefaultTableModel(dropped_result_table_data, dropped_result_table_header);
        dropped_result_table.setModel(droppedDataModel);
    }

    public void disableEditingTables() {
        for (int c = 0; c < dropped_result_table.getColumnCount(); c++) {
            // remove editor   
            Class<?> col_class2 = dropped_result_table.getColumnClass(c);
            dropped_result_table.setDefaultEditor(col_class2, null);
        }
    }

    @SuppressWarnings("empty-statement")
    public void reload_dropped_result_table_data(List<Object[]> resultList) {

        int total = 0;
        for (Object[] obj : resultList) {

            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(String.valueOf(obj[0])); // Project
            oneRow.add(String.valueOf(obj[1])); // Pallet Number
            oneRow.add(String.valueOf(obj[2])); // Harness Part
            oneRow.add(String.valueOf(obj[3])); // Removal Time
            oneRow.add(String.valueOf(obj[4])); // User
            oneRow.add(String.valueOf(obj[5])); // User code    
            oneRow.add(String.valueOf(obj[6])); // drop_feedback    
            
            total++;
            
            dropped_result_table_data.add(oneRow);
        }
        dropped_result_table.setModel(new DefaultTableModel(dropped_result_table_data, dropped_result_table_header));
        dropped_result_table.setFont(new Font(String.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONT")), Font.BOLD, Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONTSIZE"))));
        dropped_result_table.setRowHeight(Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_ROW_HEIGHT")));

        this.total_lbl.setText(String.valueOf(total));
    }

    private boolean checkValidFields() {
        if (startTimeSpinner.getValue() != ""
                && endTimeSpinner.getValue() != ""
                && startDatePicker.getDate() != null
                && endDatePicker.getDate() != null) {
            return true;
        } else {
            return false;
        }
    }

    private void refresh() {
        SimpleDateFormat timeDf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateDf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = null;
        String endDate = null;

        //Clear all tables
        this.reset_table_content();

        List<Object> projects = new ArrayList<Object>();
        projects.add("1");

        if (String.valueOf(harness_type_filter.getSelectedItem()).equals("ALL")) {
            for (int i = 0; i < harness_type_filter.getItemCount(); i++) {
                projects.add(String.valueOf(harness_type_filter.getItemAt(i)));
            }
        } else {
            projects.add(String.valueOf(harness_type_filter.getSelectedItem()));
        }

        if (checkValidFields()) {
            String startTime = timeDf.format(startTimeSpinner.getValue());
            String endTime = timeDf.format(endTimeSpinner.getValue());

            startDate = dateDf.format(startDatePicker.getDate()) + " " + startTime;
            endDate = dateDf.format(endDatePicker.getDate()) + " " + endTime;

            try {

                //################# Dropped Harness Data #################### 
                Helper.startSession();
                
                String query_str = " SELECT bc.harness_type AS project, "
                        + " bc.pallet_number AS pack_number, "
                        + " bc.harness_part AS harness_part, "
                        + " bc.drop_time AS removal_time, "                        
                        + " bc.create_user AS user, "
                        + " bc.m_user AS user_code, "
                        + " bc.drop_feedback AS drop_feedback "
                        + " FROM drop_base_harness bc "
                        + " WHERE bc.harness_type IN (:projects) "
                        + " AND bc.write_time BETWEEN '%s' AND '%s' ";

                query_str = String.format(query_str, startDate, endDate);
                SQLQuery query = Helper.sess.createSQLQuery(query_str);                

                query.addScalar("project", StandardBasicTypes.STRING)
                        .addScalar("pack_number", StandardBasicTypes.STRING)
                        .addScalar("harness_part", StandardBasicTypes.STRING)
                        .addScalar("removal_time", StandardBasicTypes.TIMESTAMP)
                        .addScalar("user", StandardBasicTypes.STRING)
                        .addScalar("user_code", StandardBasicTypes.STRING)
                        .addScalar("drop_feedback", StandardBasicTypes.STRING)                        
                        .setParameterList("projects", projects);

                List<Object[]> droppedResultList = query.list();

                Helper.sess.getTransaction().commit();

                this.reload_dropped_result_table_data(droppedResultList);

                this.disableEditingTables();

            } catch (HibernateException e) {
                if (Helper.sess.getTransaction() != null) {
                    Helper.sess.getTransaction().rollback();
                }
                e.printStackTrace();
            }
        } else if (startDatePicker.getDate() == null && startTimeSpinner.getValue() == "") {

        } else if (endDatePicker.getDate() == null && endTimeSpinner.getValue() == "") {

        } else {
            JOptionPane.showMessageDialog(null, "Empty field", "Empty field Error", JOptionPane.ERROR_MESSAGE);
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

        north_panel = new javax.swing.JPanel();
        startDatePicker = new org.jdesktop.swingx.JXDatePicker();
        endDatePicker = new org.jdesktop.swingx.JXDatePicker();
        refresh_btn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        startTimeSpinner = new javax.swing.JSpinner();
        endTimeSpinner = new javax.swing.JSpinner();
        result_table_scroll = new javax.swing.JScrollPane();
        dropped_result_table = new javax.swing.JTable();
        harness_type_filter = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        total_lbl = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Production statistics");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        north_panel.setBackground(new java.awt.Color(51, 51, 51));
        north_panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                north_panelKeyPressed(evt);
            }
        });

        startDatePicker.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        endDatePicker.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        refresh_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        refresh_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/refresh.png"))); // NOI18N
        refresh_btn.setText("Actualiser");
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Du");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Au");

        startTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        endTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        dropped_result_table.setModel(new javax.swing.table.DefaultTableModel(
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
        result_table_scroll.setViewportView(dropped_result_table);

        harness_type_filter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        harness_type_filter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL" }));
        harness_type_filter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                harness_type_filterItemStateChanged(evt);
            }
        });
        harness_type_filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harness_type_filterActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Liste des harness annulées");

        total_lbl.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        total_lbl.setForeground(new java.awt.Color(255, 255, 255));
        total_lbl.setText("0");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Total harness");

        javax.swing.GroupLayout north_panelLayout = new javax.swing.GroupLayout(north_panel);
        north_panel.setLayout(north_panelLayout);
        north_panelLayout.setHorizontalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, north_panelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74)
                .addComponent(total_lbl)
                .addGap(62, 62, 62))
            .addComponent(result_table_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1006, Short.MAX_VALUE)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(endDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(harness_type_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator1)
        );
        north_panelLayout.setVerticalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1)
                        .addGap(22, 22, 22)
                        .addComponent(jLabel2))
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(harness_type_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(result_table_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(total_lbl))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(north_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(north_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed

        refresh();

    }//GEN-LAST:event_refresh_btnActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_formKeyPressed

    private void north_panelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_north_panelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_north_panelKeyPressed

    private void harness_type_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_harness_type_filterItemStateChanged
        //
    }//GEN-LAST:event_harness_type_filterItemStateChanged

    private void harness_type_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harness_type_filterActionPerformed
        refresh();
    }//GEN-LAST:event_harness_type_filterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable dropped_result_table;
    private org.jdesktop.swingx.JXDatePicker endDatePicker;
    private javax.swing.JSpinner endTimeSpinner;
    private javax.swing.JComboBox harness_type_filter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel north_panel;
    private javax.swing.JButton refresh_btn;
    private javax.swing.JScrollPane result_table_scroll;
    private org.jdesktop.swingx.JXDatePicker startDatePicker;
    private javax.swing.JSpinner startTimeSpinner;
    private javax.swing.JLabel total_lbl;
    // End of variables declaration//GEN-END:variables
}
