/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging_warehouse;

import __main__.GlobalVars;
import helper.Helper;
import helper.JDialogExcelFileChooser;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
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
public class PACKAGING_WAREHOUSE_UI0002_STOCK extends javax.swing.JDialog {

    Vector packaging_stock_result_table_data = new Vector();
    Vector<String> packaging_stock_result_table_header = new Vector<String>();
    private List<Object[]> resultList;
    List<String> table_header = Arrays.asList(
            "PACK ITEM",
            "STOCK",
            "ALERT QTY"
    );

    /**
     * Creates new form UI0011_ProdStatistics_
     */
    public PACKAGING_WAREHOUSE_UI0002_STOCK(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initGui();        
    }

    private void initGui() {
        //Center the this dialog in the screen
        Helper.centerJDialog(this);

        //Desable table edition
        disableEditingTables();

        //Load table header
        load_table_header();
        
        this.setVisible(true);      

    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            packaging_stock_result_table_header.add(it.next());
        }
    }

    public void reset_table_content() {
        packaging_stock_result_table_data = new Vector();
        packaging_stock_table.setModel(new DefaultTableModel(packaging_stock_result_table_data, packaging_stock_result_table_header));
    }

    public void disableEditingTables() {
        for (int c = 0; c < packaging_stock_table.getColumnCount(); c++) {
            // remove editor   
            Class<?> col_class = packaging_stock_table.getColumnClass(c);
            packaging_stock_table.setDefaultEditor(col_class, null);
        }
        packaging_stock_table.setAutoCreateRowSorter(true);

    }

    @SuppressWarnings("empty-statement")
    public void reload_result_table_data(List<Object[]> resultList) {

        for (Object[] obj : resultList) {

            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(String.valueOf(obj[0])); // PACK ITEM
            oneRow.add(Integer.valueOf(obj[1].toString())); // STOCK
            oneRow.add(Integer.valueOf(obj[2].toString())); // "Alert Qty"                  

            packaging_stock_result_table_data.add(oneRow);
        }
        packaging_stock_table.setModel(new DefaultTableModel(packaging_stock_result_table_data, packaging_stock_result_table_header));
        packaging_stock_table.setFont(new Font(String.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONT")), Font.BOLD, Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONTSIZE"))));
        packaging_stock_table.setRowHeight(Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_ROW_HEIGHT")));
    }

    public void refresh() {
        System.out.println("refresh");
        //Clear all tables
        this.reset_table_content();

        try {

            //################# Dropped Harness Data #################### 
            Helper.startSession();
            String query_str = "SELECT "
                    + "p.pack_item AS pack_item, "
                    + "SUM(p.quantity) AS stock, "
                    + "i.alert_qty AS alert_qty "
                    + "FROM "
                    + "packaging_stock_movement p, "
                    + "packaging_items i "
                    + "WHERE p.pack_item = i.pack_item "
                    + "AND p.warehouse = '"+GlobalVars.APP_PROP.getProperty("WH_PACKAGING")+"' "
                    + "GROUP BY p.pack_item, i.alert_qty "
                    + "ORDER BY pack_item ASC;";

            SQLQuery query = Helper.sess.createSQLQuery(query_str);

            query.addScalar("pack_item", StandardBasicTypes.STRING)
                    .addScalar("stock", StandardBasicTypes.INTEGER)
                    .addScalar("alert_qty", StandardBasicTypes.INTEGER);

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
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        packaging_stock_table = new javax.swing.JTable();
        refresh_btn1 = new javax.swing.JButton();
        export_btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Stock Packaging");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        north_panel.setBackground(new java.awt.Color(194, 227, 254));
        north_panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                north_panelKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setText("Stock Packaging");

        packaging_stock_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(packaging_stock_table);

        refresh_btn1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        refresh_btn1.setText("Actualiser");
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

        javax.swing.GroupLayout north_panelLayout = new javax.swing.GroupLayout(north_panel);
        north_panel.setLayout(north_panelLayout);
        north_panelLayout.setHorizontalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addComponent(refresh_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(export_btn)))
                .addContainerGap(720, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        north_panelLayout.setVerticalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refresh_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(export_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
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

    private void refresh_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btn1ActionPerformed
        refresh();
    }//GEN-LAST:event_refresh_btn1ActionPerformed

    private void export_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export_btnActionPerformed

        //Create the excel workbook
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("PACKAGING STOCK");
        CreationHelper createHelper = wb.getCreationHelper();

        //######################################################################
        //##################### SHEET 1 : PILES DETAILS ########################
        //Initialiser les entête du fichier
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow((short) 0);

        row.createCell(0).setCellValue("PACK ITEM");
        row.createCell(1).setCellValue("STOCK");
        row.createCell(2).setCellValue("ALERT QTY");

        short sheetPointer = 1;

        for (Object[] obj : this.resultList) {
            row = sheet.createRow(sheetPointer);
            row.createCell(0).setCellValue(String.valueOf(obj[0]));
            row.createCell(1).setCellValue(Integer.valueOf(obj[1].toString()));
            row.createCell(2).setCellValue(Integer.valueOf(obj[2].toString()));
            sheetPointer++;
        }
        //Past the workbook to the file chooser
        new JDialogExcelFileChooser((Frame) super.getParent(), true, wb).setVisible(true);
    }//GEN-LAST:event_export_btnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton export_btn;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel north_panel;
    private javax.swing.JTable packaging_stock_table;
    private javax.swing.JButton refresh_btn1;
    // End of variables declaration//GEN-END:variables
}
