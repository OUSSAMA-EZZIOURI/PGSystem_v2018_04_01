/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.reports;

import __main__.GlobalVars;
import entity.ConfigSegment;
import entity.ConfigWorkplace;
import helper.ComboItem;
import helper.Helper;
import helper.JDialogExcelFileChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author Administrator
 */
public class PACKAGING_UI0017_UCS_List extends javax.swing.JFrame {

    Vector ucs_result_table_data = new Vector();
    Vector<String> ucs_result_table_header = new Vector<String>();
    private List<Object[]> resultList;
    List<String> table_header = Arrays.asList(
            "SEGMENT",
            "WORKPLACE",
            "HARNESS PART",
            "HARNESS INDEX",
            "STD TIME",
            "PACK TYPE",
            "PACK SIZE",
            "LEONI PART NUMBER",
            "ORDER NO.",
            "LIFES",
            "SPECIAL ORDER"
    );

    /**
     * Creates new form UI0011_ProdStatistics_
     */
    public PACKAGING_UI0017_UCS_List(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        initGui();
        refresh();

    }

    private void initGui() {
        //Center the this dialog in the screen
        Helper.centerJFrame(this);

        //Desable table edition
        disableEditingTables();

        //Load table header
        load_table_header();

        //Init projects filter
        initSegmentFilter();

        //Focuse on text filter field
        harness_part_filter.requestFocus();
    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            ucs_result_table_header.add(it.next());
        }
    }

    private void initSegmentFilter() {
        List result = new ConfigSegment().select();
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(null, Helper.ERR0026_NO_SEGMENT_FOUND, "Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0026_NO_SEGMENT_FOUND);
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigSegment cp = (ConfigSegment) o;
                segment_filter.addItem(new ComboItem(cp.getSegment(), cp.getSegment()));
            }
        }
    }

    public void reset_table_content() {
        ucs_result_table_data = new Vector();
        ucs_result_table.setModel(new DefaultTableModel(ucs_result_table_data, ucs_result_table_header));
    }

    public void disableEditingTables() {
        for (int c = 0; c < ucs_result_table.getColumnCount(); c++) {
            // remove editor   
            Class<?> col_class = ucs_result_table.getColumnClass(c);
            ucs_result_table.setDefaultEditor(col_class, null);
        }
        ucs_result_table.setAutoCreateRowSorter(true);
    }

    @SuppressWarnings("empty-statement")
    public void reload_result_table_data(List<Object[]> resultList) {

        for (Object[] obj : resultList) {

            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(String.valueOf(obj[0])); // SEGMENT
            oneRow.add(String.valueOf(obj[1])); // WORKPLACE
            oneRow.add(String.valueOf(obj[2])); // "Harness Part"
            oneRow.add(String.valueOf(obj[3])); // "Harness Index"
            oneRow.add(String.valueOf(obj[4])); // "Std Time"
            oneRow.add(String.valueOf(obj[5])); // "Pack Type"
            oneRow.add(String.valueOf(obj[6])); // "Pack Size"
            oneRow.add(String.valueOf(obj[7])); // "SPN"                     
            oneRow.add(String.valueOf(obj[8])); // "ORDER NO"     
            oneRow.add(String.valueOf(obj[9])); // "LIFES"     
            oneRow.add(String.valueOf(obj[10])); // "SPECIAL ORDER"     

            ucs_result_table_data.add(oneRow);
        }
        ucs_result_table.setModel(new DefaultTableModel(ucs_result_table_data, ucs_result_table_header));
        ucs_result_table.setFont(new Font(String.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONT")), Font.BOLD, 12));
        ucs_result_table.setRowHeight(Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_ROW_HEIGHT")));
        setContainerTableRowsStyle();
    }

    private void initContainerTableDoubleClick() {
        this.ucs_result_table.addMouseListener(
                new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    //System.out.println("GVars.context.getUser().getAccessLevel()" + GVars.context.getUser().getAccessLevel());
//                    if (GVars.context.getUser().getAccessLevel() == GlobalVars.PROFIL_ADMIN) {
//                        new PACKAGING_UI0010_PalletDetails(null, rootPaneCheckingEnabled, String.valueOf(searchResult_table.getValueAt(searchResult_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)), true, true, true).setVisible(true);
//                    } else {
//                        new PACKAGING_UI0010_PalletDetails(null, rootPaneCheckingEnabled, String.valueOf(searchResult_table.getValueAt(searchResult_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)), false, false, false).setVisible(true);
//                    }

                }
            }
        }
        );
    }

    public void setContainerTableRowsStyle() {

        ucs_result_table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, true, hasFocus, row, col);

                Integer status = Integer.valueOf(table.getModel().getValueAt(row, 10).toString());
                if (status == 1) {
                    setBackground(Color.YELLOW);
                    setForeground(Color.BLACK);

                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }

                ucs_result_table.setRowSelectionAllowed(true);
                setHorizontalAlignment(JLabel.CENTER);

                return this;
            }
        });

    }

    private void refresh() {

        //Clear all tables
        this.reset_table_content();

        List<Object> segments = new ArrayList<Object>();
        List<Object> workplaces = new ArrayList<Object>();
        segments.clear();
        workplaces.clear();
        workplaces.add("1");
        segments.add("1");

        //Segment filter
        if (String.valueOf(segment_filter.getSelectedItem()).equals("ALL")) {
            for (int i = 0; i < segment_filter.getItemCount(); i++) {
                segments.add(String.valueOf(segment_filter.getItemAt(i)));
            }           
            List result = new ConfigWorkplace().selectBySegment("");
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(null, Helper.ERR0027_NO_WORKPLACE_FOUND, "Configuration error !", ERROR_MESSAGE);
                System.err.println(Helper.ERR0027_NO_WORKPLACE_FOUND);
            } else { //Map project data in the list
                workplaces.clear();
                for (Object o : result) {
                    ConfigWorkplace cp = (ConfigWorkplace) o;                    
                    workplaces.add(cp.getWorkplace());
                }
            }
        } else {
            segments.add(String.valueOf(segment_filter.getSelectedItem()));
        }
        
        //Workplace filter
        if (String.valueOf(workplace_filter.getSelectedItem()).equals("ALL")) {
            for (int i = 0; i < workplace_filter.getItemCount(); i++) {
                workplaces.add(String.valueOf(workplace_filter.getItemAt(i)));
            }
        } else {
            workplaces.add(String.valueOf(workplace_filter.getSelectedItem()));
        }

        try {

            //################# UCS List #################### 
            Helper.startSession();

            String query_str = " SELECT "
                    + " bc.segment AS segment, "
                    + " bc.workplace AS workplace, "
                    + " bc.harness_part AS harness_part, "
                    + " bc.harness_index AS harness_index, "
                    + " bc.std_time AS std_time, "
                    + " bc.pack_type AS pack_type, "
                    + " bc.pack_size AS pack_size, "
                    + " bc.supplier_part_number AS spn,"
                    + " bc.order_no AS order_no, "
                    + " bc.lifes AS lifes, "
                    + " bc.special_order AS special_order "
                    + " FROM config_ucs bc "
                    + " WHERE bc.segment IN (:segment) AND bc.workplace IN (:workplace)";

            if (!harness_part_filter.getText().trim().isEmpty()) {
                query_str += " AND bc.harness_part like '%" + harness_part_filter.getText().trim() + "%'";
            }

            query_str += " ORDER BY segment";
            
            System.out.println(segments.toString());
            System.out.println(workplaces.toString());
            
            SQLQuery query = Helper.sess.createSQLQuery(query_str);

            query.addScalar("segment", StandardBasicTypes.STRING)
                    .addScalar("workplace", StandardBasicTypes.STRING)
                    .addScalar("harness_part", StandardBasicTypes.STRING)
                    .addScalar("harness_index", StandardBasicTypes.STRING)
                    .addScalar("std_time", StandardBasicTypes.STRING)
                    .addScalar("pack_type", StandardBasicTypes.STRING)
                    .addScalar("pack_size", StandardBasicTypes.STRING)
                    .addScalar("spn", StandardBasicTypes.STRING)
                    .addScalar("order_no", StandardBasicTypes.STRING)
                    .addScalar("lifes", StandardBasicTypes.INTEGER)
                    .addScalar("special_order", StandardBasicTypes.INTEGER)
                    .setParameterList("segment", segments)
                    .setParameterList("workplace", workplaces);

            resultList = query.list();

            Helper.sess.getTransaction().commit();

            this.reload_result_table_data(resultList);

            this.disableEditingTables();

        } catch (HibernateException e) {
            if (Helper.sess.getTransaction() != null) {
                Helper.sess.getTransaction().rollback();
            }
            e.printStackTrace();
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
        clear_btn = new javax.swing.JButton();
        segment_filter = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        ucs_result_table = new javax.swing.JTable();
        harness_part_filter = new javax.swing.JTextField();
        refresh_btn1 = new javax.swing.JButton();
        export_btn = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        workplace_filter = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Type de packaging par référence");
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

        clear_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        clear_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/edit-clear.png"))); // NOI18N
        clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_btnActionPerformed(evt);
            }
        });

        segment_filter.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        segment_filter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL" }));
        segment_filter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                segment_filterItemStateChanged(evt);
            }
        });
        segment_filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                segment_filterActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Liste Packaging Standard Externe");

        ucs_result_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        ucs_result_table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ucs_result_tableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(ucs_result_table);

        harness_part_filter.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        harness_part_filter.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                harness_part_filterComponentShown(evt);
            }
        });
        harness_part_filter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                harness_part_filterKeyPressed(evt);
            }
        });

        refresh_btn1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        refresh_btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/filter-icon.png"))); // NOI18N
        refresh_btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btn1ActionPerformed(evt);
            }
        });

        export_btn.setText("Exporter en Excel...");
        export_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                export_btnActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Segment");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Workplace");

        workplace_filter.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        workplace_filter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL" }));
        workplace_filter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                workplace_filterItemStateChanged(evt);
            }
        });
        workplace_filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workplace_filterActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Part Number");

        javax.swing.GroupLayout north_panelLayout = new javax.swing.GroupLayout(north_panel);
        north_panel.setLayout(north_panelLayout);
        north_panelLayout.setHorizontalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(segment_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(workplace_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addComponent(harness_part_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(refresh_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clear_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(export_btn)))
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        north_panelLayout.setVerticalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22))
                        .addGap(4, 4, 4)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(segment_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(workplace_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(harness_part_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(refresh_btn1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(export_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(clear_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 742, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 7, Short.MAX_VALUE)
                .addGap(0, 0, 0))
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

    private void clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_btnActionPerformed
        harness_part_filter.setText("");
        segment_filter.setSelectedIndex(0);
        refresh();
    }//GEN-LAST:event_clear_btnActionPerformed

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

    private void segment_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segment_filterActionPerformed
        refresh();
    }//GEN-LAST:event_segment_filterActionPerformed

    private void segment_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_segment_filterItemStateChanged
        System.out.println("segment filter "+String.valueOf(segment_filter.getSelectedItem()).trim());
        if ("ALL".equals(String.valueOf(segment_filter.getSelectedItem()).trim())) {            
            this.workplace_filter.removeAllItems();
            this.setWorkplaceBySegment("");
            this.workplace_filter.addItem("ALL");
            System.out.println("size "+this.workplace_filter.getItemCount());
            this.workplace_filter.setSelectedIndex(this.workplace_filter.getItemCount()-1);
            //this.workplace_filter.setEnabled(false);
        } else {
            this.workplace_filter.removeAllItems();
            this.workplace_filter.addItem("ALL");            
            this.setWorkplaceBySegment(String.valueOf(segment_filter.getSelectedItem()));
        }

        refresh();
    }//GEN-LAST:event_segment_filterItemStateChanged

    private void setWorkplaceBySegment(String segment) {
        List result = new ConfigWorkplace().selectBySegment(segment);
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(null, Helper.ERR0027_NO_WORKPLACE_FOUND, "Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0027_NO_WORKPLACE_FOUND);
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigWorkplace cp = (ConfigWorkplace) o;
                workplace_filter.addItem(new ComboItem(cp.getWorkplace(), cp.getWorkplace()));
            }
        }
    }

    private void refresh_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btn1ActionPerformed
        refresh();
    }//GEN-LAST:event_refresh_btn1ActionPerformed

    private void harness_part_filterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_harness_part_filterKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            refresh();
        }
    }//GEN-LAST:event_harness_part_filterKeyPressed

    private void export_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export_btnActionPerformed

        //Create the excel workbook
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("UCS");
        CreationHelper createHelper = wb.getCreationHelper();

        //######################################################################
        //##################### SHEET 1 : PILES DETAILS ########################
        //Initialiser les entête du fichier
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow((short) 0);

        row.createCell(0).setCellValue("SEGMENT");
        row.createCell(1).setCellValue("WORKPLACE");
        row.createCell(2).setCellValue("PART NUMBER");
        row.createCell(3).setCellValue("INDEX");
        row.createCell(4).setCellValue("STD TIME");
        row.createCell(5).setCellValue("PACK TYPE");
        row.createCell(6).setCellValue("PACK SIZE");
        row.createCell(7).setCellValue("SPN");
        row.createCell(8).setCellValue("ORDER NO");
        row.createCell(9).setCellValue("TOTAL PLANNED PACK");
        row.createCell(10).setCellValue("SPECIAL ORDER");

        short sheetPointer = 1;

        for (Object[] obj : this.resultList) {
            row = sheet.createRow(sheetPointer);
            row.createCell(0).setCellValue(String.valueOf(obj[0]));
            row.createCell(1).setCellValue(String.valueOf(obj[1]));
            row.createCell(2).setCellValue(""+String.valueOf(obj[2].toString()));
            row.createCell(3).setCellValue(String.valueOf(obj[3]));
            row.createCell(4).setCellValue(Double.valueOf(obj[4].toString()));
            row.createCell(5).setCellValue(String.valueOf(obj[5]));
            row.createCell(6).setCellValue(Integer.valueOf(obj[6].toString()));
            row.createCell(7).setCellValue(String.valueOf(obj[7]));
            try {
                row.createCell(8).setCellValue(Integer.valueOf(obj[8].toString()));
            } catch (Exception e) {
                row.createCell(8).setCellValue(0 + "");
            }
            row.createCell(9).setCellValue(Integer.valueOf(obj[9].toString()));
            row.createCell(10).setCellValue(Integer.valueOf(obj[10].toString()));
            sheetPointer++;
        }
        //Past the workbook to the file chooser
        new JDialogExcelFileChooser((Frame) super.getParent(), true, wb).setVisible(true);
    }//GEN-LAST:event_export_btnActionPerformed

    private void ucs_result_tableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ucs_result_tableKeyPressed

    }//GEN-LAST:event_ucs_result_tableKeyPressed

    private void harness_part_filterComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_harness_part_filterComponentShown

    }//GEN-LAST:event_harness_part_filterComponentShown

    private void workplace_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_workplace_filterItemStateChanged
        refresh();
    }//GEN-LAST:event_workplace_filterItemStateChanged

    private void workplace_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workplace_filterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_workplace_filterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clear_btn;
    private javax.swing.JButton export_btn;
    private javax.swing.JTextField harness_part_filter;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel north_panel;
    private javax.swing.JButton refresh_btn1;
    private javax.swing.JComboBox segment_filter;
    private javax.swing.JTable ucs_result_table;
    private javax.swing.JComboBox workplace_filter;
    // End of variables declaration//GEN-END:variables
}
