/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.gui;

import __main__.GlobalVars;
import entity.BaseContainer;
import helper.Helper;
import helper.HQLHelper;
import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecodeTmp;
import entity.ConfigUcs;
import gui.packaging.PackagingVars;
import helper.PrinterHelper;
import helper.UIHelper;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import gui.packaging.mode2.state.Mode2_S020_HarnessPartScan;
import gui.packaging.mode2.state.Mode2_S030_NewPalletScan;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author Administrator
 */
public final class PACKAGING_UI9000_ChoosePackType_Mode2 extends javax.swing.JDialog {

    Vector result_table_data = new Vector();
    Vector<String> ucs_result_table_header = new Vector<String>();
    private List<Object[]> resultList;
    int SPECIAL_COMMANDE_INDEX = 12;
    List<String> table_header = Arrays.asList(
            "ID",
            "CPN",
            "LPN",
            "INDEX",
            "PACK TYPE",
            "STD PACK",
            "STD TIME",
            "WORKSTATION",
            "SEGMENT",
            "WORKPLACE",
            "UCS LIFES",
            "COMMENT",
            "SPECIAL ORDER",
            "ORDER NO"
    );

    /**
     * Creates new form NewJDialog
     */
    public PACKAGING_UI9000_ChoosePackType_Mode2(java.awt.Frame parent, boolean modal, String partnumber) {
        super(parent, modal);
        initComponents();
        initGui();
        System.out.println("HP " + partnumber);
        Helper.startSession();

        //Récupérer la list des données UCS qui corresponds au harness_part
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HP_ACTIVE);
        query.setParameter("hp", partnumber.trim());

        Helper.sess.getTransaction().commit();
        List result = query.list();
        if (result.size() == 0) {
            UILog.severe(ErrorMsg.APP_ERR0021[0], partnumber);
            UILog.severeDialog(null, ErrorMsg.APP_ERR0021, partnumber);
            //Clear session vals in mode2_context
            clearContextSessionVals();
            // Change go back to state HarnessPartScan                    
            Mode2_S020_HarnessPartScan state = new Mode2_S020_HarnessPartScan();
            PackagingVars.mode2_context.setState(state);
        }
        this.reload_result_table_data(result);

        this.setVisible(true);

    }

    public void reset_table_content() {
        result_table_data = new Vector();
        ucs_jtable.setModel(new DefaultTableModel(result_table_data, ucs_result_table_header));
    }

    public void reload_result_table_data(List resultList) {

        for (Object obj : resultList) {
            ConfigUcs c = (ConfigUcs) obj;
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(String.valueOf(c.getId())); // ID     
            oneRow.add(String.valueOf(c.getHarnessPart())); // CPN
            oneRow.add(String.valueOf(c.getSupplierPartNumber())); // LPN
            oneRow.add(String.valueOf(c.getHarnessIndex())); // INDEX
            oneRow.add(String.valueOf(c.getPackType())); // PACK TYPE
            oneRow.add(String.valueOf(c.getPackSize())); // STD PACK
            oneRow.add(String.valueOf(c.getStdTime())); // STD TIME
            oneRow.add(String.valueOf(c.getAssyWorkstationName())); // WORKSTATION
            oneRow.add(String.valueOf(c.getSegment())); // SEGMENT                     
            oneRow.add(String.valueOf(c.getWorkplace())); // WORKPLACE    
            oneRow.add(String.valueOf(c.getLifes())); // UCS LIFES  
            oneRow.add(String.valueOf(c.getComment())); // COMMENT  
            oneRow.add(String.valueOf(c.getSpecialOrder())); // SPECIAL ORDER 
            oneRow.add(String.valueOf(c.getOrderNo())); // ORDER NO  

            result_table_data.add(oneRow);
        }
        ucs_jtable.setModel(new DefaultTableModel(result_table_data, ucs_result_table_header));
        ucs_jtable.setFont(new Font(String.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONT")), Font.BOLD, 16));
        ucs_jtable.setRowHeight(Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_ROW_HEIGHT")));
        setContainerTableRowsStyle();
    }

    public void disableEditingTables() {
        UIHelper.disableEditingJtable(ucs_jtable);
    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            ucs_result_table_header.add(it.next());
        }
    }

    public void setContainerTableRowsStyle() {
        ucs_jtable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                Integer status = Integer.valueOf(table.getModel().getValueAt(row, SPECIAL_COMMANDE_INDEX).toString());
                if (status == 1) {
                    setBackground(Color.YELLOW);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                setHorizontalAlignment(JLabel.CENTER);

                return this;
            }
        });
    }

    private void initGui() {

        //Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        //this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        //Center the this dialog in the screen
        Helper.centerJDialog(this);

        //Desable table edition
        //disableEditingTables();
        //Load table header
        load_table_header();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ucs_jtable = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 51));
        setModal(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("Choisissez le type du packaging");

        jLabel2.setBackground(new java.awt.Color(51, 51, 51));
        jLabel2.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setText("puis appuyer sur");

        jLabel3.setBackground(new java.awt.Color(51, 51, 51));
        jLabel3.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("ENTRER");

        ucs_jtable.setModel(new javax.swing.table.DefaultTableModel(
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
        ucs_jtable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ucs_jtableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(ucs_jtable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(444, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addGap(352, 352, 352))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mappingValsToContext() {
        //58	22216200	26C06970A	P01	2RV	120	0.377	SMALL_SB	SMALLS_MDEP	SMALL	-1	null	false	null
        PackagingVars.mode2_context.getBaseContainerTmp().setUcsId(Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 0).toString().toString()));
        PackagingVars.mode2_context.getBaseContainerTmp().setHarnessPart(GlobalVars.HARN_PART_PREFIX + (String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 1));
        PackagingVars.mode2_context.getBaseContainerTmp().setSupplierPartNumber((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 2));
        PackagingVars.mode2_context.getBaseContainerTmp().setHarnessIndex((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 3));
        PackagingVars.mode2_context.getBaseContainerTmp().setPackType((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 4));
        PackagingVars.mode2_context.getBaseContainerTmp().setQtyExpected(Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 5).toString()));
        PackagingVars.mode2_context.getBaseContainerTmp().setStdTime(Double.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 6).toString()));
        PackagingVars.mode2_context.getBaseContainerTmp().setAssyWorkstation((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 7));
        PackagingVars.mode2_context.getBaseContainerTmp().setSegment((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 8));
        PackagingVars.mode2_context.getBaseContainerTmp().setWorkplace((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 9));
        PackagingVars.mode2_context.getBaseContainerTmp().setUcsLifes(Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 10).toString()));
        PackagingVars.mode2_context.getBaseContainerTmp().setComment((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 11));
        PackagingVars.mode2_context.getBaseContainerTmp().setSpecial_order(Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 12).toString()));
        PackagingVars.mode2_context.getBaseContainerTmp().setOrder_no((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 13));
        PackagingVars.mode2_context.getBaseContainerTmp().setChoosen_pack_type((String) ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 4));

        System.out.println(PackagingVars.mode2_context.getBaseContainerTmp().toString());
    }

    public void loadConfigUcs() {
        Helper.startSession();
        //Getting ConfigUCS data who match the choice criteria
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HP_AND_SUPPLIER_PART_AND_INDEX_PACKTYPE_AND_PACKSIZE);
        query.setParameter("harnessPart", PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart().substring(1)); //items[0] = harnessPart
        query.setParameter("supplierPartNumber", PackagingVars.mode2_context.getBaseContainerTmp().getSupplierPartNumber()); //items[1] = supplierPartNumber
        query.setParameter("harnessIndex", PackagingVars.mode2_context.getBaseContainerTmp().getHarnessIndex()); //items[2] = harnessIndex                        
        query.setParameter("packType", PackagingVars.mode2_context.getBaseContainerTmp().getPackType()); //items[3] = packType
        query.setInteger("packSize", PackagingVars.mode2_context.getBaseContainerTmp().getQtyExpected()); //items[4] = packSize

        Helper.sess.getTransaction().commit();
        List result = query.list();
        ConfigUcs configUcs = null;
        try {
            configUcs = (ConfigUcs) result.get(0);
        } catch (IndexOutOfBoundsException e) {
            UILog.severe(ErrorMsg.APP_ERR0022[0], PackagingVars.mode2_context.getBaseContainerTmp().getSupplierPartNumber(), PackagingVars.mode2_context.getBaseContainerTmp().getHarnessIndex());
            UILog.severeDialog(null, ErrorMsg.APP_ERR0022, PackagingVars.mode2_context.getBaseContainerTmp().getSupplierPartNumber(), PackagingVars.mode2_context.getBaseContainerTmp().getHarnessIndex());           
        }

        int newPalletNumber = -1;
        try {
            //Bug au niveau cette methode, il faut simplifier le code de ;
            //      creation d'un his palet
            newPalletNumber = PrinterHelper.saveAndPrintOpenSheet(PackagingVars.mode2_context,
                    configUcs.getHarnessPart(),
                    configUcs.getHarnessIndex(),
                    configUcs.getSupplierPartNumber(),
                    configUcs.getPackType(),
                    configUcs.getPackSize(),
                    PackagingVars.context.getUser().getLogin());
            System.out.println("PalledId" + newPalletNumber);
            PackagingVars.mode2_context.getBaseContainerTmp().setPalletNumber("" + newPalletNumber);
            if (newPalletNumber != -1) { //Bingo !!

                //############# PASSE TO S041 STATE ###############
                UILog.info(String.format("Openning new container for first harness part [%s].", PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart().substring(1)));

                String msg = "Scanner la fiche Ouverture Palette N° " + newPalletNumber;
                PackagingVars.Packaging_Gui_Mode2.setFeedbackTextarea(msg);
                PackagingVars.Packaging_Gui_Mode2.setRequestedPallet_txt(msg);
                //Change state to S041
                Mode2_S030_NewPalletScan state = new Mode2_S030_NewPalletScan("" + newPalletNumber);
                PackagingVars.mode2_context.setState(state);
                this.dispose();
            }

        } catch (Exception e) {
            System.out.println("PACKAGING_UI9000_ChooseContainerType Error : " + e.getLocalizedMessage() + " " + e.getMessage());
            UILog.severeDialog(this, ErrorMsg.APP_ERR0019, configUcs.getHarnessPart(), newPalletNumber);
        }
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        clearContextSessionVals();
        // Change go back to state HarnessPartScan            
        PackagingVars.mode2_context.setState(new Mode2_S020_HarnessPartScan());
        PackagingVars.Packaging_Gui_Mode2.setFeedbackTextarea("");
    }//GEN-LAST:event_formWindowClosing

    private void ucs_jtableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ucs_jtableKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_SPACE) {
            // User has pressed Carriage return button
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                //Set mode2_context session vals with the choosen valus after split them
                //System.out.println("Selected Row " + ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 0).toString());
                Integer ucs_id = Integer.valueOf(ucs_jtable.getValueAt(ucs_jtable.getSelectedRow(), 0).toString());
                this.mappingValsToContext();
                List result;
                Query query;
                /**
                 * -------------------------------
                 */
                //Checker si des palettes du même type se sont ouvertes sur
                //la meme poste emballage.                        
                Helper.startSession();
                //Check s'il n'y a pas une palette ouverte pour la même référence avec le même type packaging.
                query = Helper.sess.createQuery(
                        "FROM BaseContainer bc WHERE "
                        + "bc.harnessPart = :harnessPart AND bc.containerState = :containerState AND bc.packType = :packType");
                query.setParameter("harnessPart", PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart().substring(1))
                        .setParameter("containerState", GlobalVars.PALLET_OPEN)
                        .setParameter("packType", PackagingVars.mode2_context.getBaseContainerTmp().getPackType());
                Helper.sess.getTransaction().commit();
                result = query.list();

                if (result.isEmpty()) {
                    if (Integer.valueOf(GlobalVars.APP_PROP.getProperty("UNIQUE_PALLET_PER_PACK_TYPE")) == 1) {
                        result = null;
                        Helper.startSession();
                        //Check s'il n'y a pas de palette ouverte pour le même 
                        //packaging et en cours dans le même workstation.
                        query = Helper.sess.createQuery(
                                "FROM BaseContainer bc WHERE "
                                + "bc.packType = :packType AND bc.containerState = :containerState"
                                + " AND bc.packWorkstation = :packWorkstation");
                        query.setParameter("packType", PackagingVars.mode2_context.getBaseContainerTmp().getPackType())
                                .setParameter("containerState", GlobalVars.PALLET_OPEN)
                                .setParameter("packWorkstation", GlobalVars.APP_HOSTNAME);

                        Helper.sess.getTransaction().commit();
                        result = query.list();

                        if (result.isEmpty()) {
                            this.loadConfigUcs();
                        } else {
                            // Test sur une seule palette par type de packaging.
                            BaseContainer bc = (BaseContainer) result.get(0);

                            UILog.severe(ErrorMsg.APP_ERR0016[0],
                                    PackagingVars.mode2_context.getBaseContainerTmp().getPackType(),
                                    GlobalVars.APP_HOSTNAME,
                                    bc.getHarnessPart(),
                                    bc.getPalletNumber(),
                                    PackagingVars.mode2_context.getBaseContainerTmp().getPackType());

                            UILog.severeDialog(this, ErrorMsg.APP_ERR0016,
                                    PackagingVars.mode2_context.getBaseContainerTmp().getPackType(),
                                    GlobalVars.APP_HOSTNAME,
                                    bc.getHarnessPart(),
                                    bc.getPalletNumber(),
                                    PackagingVars.mode2_context.getBaseContainerTmp().getPackType());

                            Mode2_S020_HarnessPartScan state = new Mode2_S020_HarnessPartScan();
                            PackagingVars.mode2_context.setState(state);
                            this.dispose();
                        }
                    } else { // Le test sur unique pack type est désactivé. passer directement au chargement UCS.
                        this.loadConfigUcs();
                    }
                } else {
                    UILog.severe(ErrorMsg.APP_ERR0017[0], PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart().substring(1));
                    UILog.severeDialog(this, ErrorMsg.APP_ERR0017, PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart().substring(1));
                    Mode2_S020_HarnessPartScan state = new Mode2_S020_HarnessPartScan();
                    PackagingVars.mode2_context.setState(state);
                    this.dispose();
                }
                /*--------------------------------*/
            }
        }
    }//GEN-LAST:event_ucs_jtableKeyPressed

    public void clearContextSessionVals() {
        //Pas besoin de réinitialiser le uid
        PackagingVars.mode2_context.setBaseContainerTmp(new BaseContainerTmp());
        PackagingVars.mode2_context.setBaseHarnessAdditionalBarecodeTmp(new BaseHarnessAdditionalBarecodeTmp());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable ucs_jtable;
    // End of variables declaration//GEN-END:variables
}
