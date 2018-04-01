/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.config;

import entity.ConfigSegment;
import entity.ConfigUcs;
import entity.ConfigWorkplace;
import entity.PackagingMaster;
import gui.packaging.PackagingVars;
import helper.ComboItem;
import helper.HQLHelper;
import helper.Helper;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author Administrator
 */
public class CONFIG_UI0001_UCS_CONFIG extends javax.swing.JFrame {

    /**
     * Les méthodes JTable qauxi sauxivent doivent être dans auxne class
     * interface initGauxi() initContainerTableDoauxbleClick load_table_header
     * reset_table_content disableEditingTable refresh
     *
     * Les 4 champs qauxi sauxivent doivent être dans auxne class interface
     */
    Vector<String> ucs_table_header = new Vector<String>();

    List<String> table_header = Arrays.asList(
            "#",
            "CPN",
            "LPN",
            "Order No",
            "Harness Type",
            "Harness Index",
            "Pack type",
            "Pack size",
            "Additional barecode",
            "Std Time",
            "Assy Workstation",
            "Segment",
            "Workplace",
            "Active",
            "Lifes",
            "Special Order",
            "Price"
    );

    Vector ucs_table_data = new Vector();
    public List<Object[]> resultList;
    ConfigUcs aux;

    public CONFIG_UI0001_UCS_CONFIG(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        initGui();
        refresh();
    }

    private void initGui() {
        //Center the this dialog in the screen
        Helper.centerJFrame(this);

        //Desable table edition
        disableEditingTable();
        //Load table header
        load_table_header();

        //Load pack master data
        initPackMasterBox();

        //
        initSegmentFilter();

        //
        Helper.loadProjectsInJbox(harnessType_filter);

        //Support double click on rows in container jtable to display history
        this.initContainerTableDoubleClick();
    }

    private void initPackMasterBox() {
        List result = new PackagingMaster().selectAllMasterPack();
        //Map project data in the list
        for (Object o : result) {
            PackagingMaster pc = (PackagingMaster) o;
            pack_type_filter.addItem(new ComboItem(pc.getPackMaster(), pc.getPackMaster()));
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

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            ucs_table_header.add(it.next());
        }

        ucs_table.setModel(new DefaultTableModel(ucs_table_data, ucs_table_header));
    }

    private void initContainerTableDoubleClick() {
        this.ucs_table.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Helper.startSession();
                    Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_ID);
                    query.setParameter("id", Integer.valueOf(ucs_table.getValueAt(ucs_table.getSelectedRow(), 0).toString()));
                    Helper.sess.getTransaction().commit();
                    aux = (ConfigUcs) query.list().get(0);
                    id_lbl.setText(aux.getId().toString());
                    create_time_txt.setText(aux.getCreateTime().toString());
                    write_time_txt.setText(aux.getWriteTime().toString());
                    cpn_txtbox.setText(aux.getHarnessPart());
                    lpn_txtbox.setText(aux.getSupplierPartNumber());
                    index_txtbox.setText(aux.getHarnessIndex());
                    try {
                        stdTime_txtbox.setText(aux.getStdTime() + "");
                    } catch (Exception ex) {
                        stdTime_txtbox.setText("0.00");
                    }
                    try {
                        price_txtbox.setText(aux.getPrice() + "");
                    } catch (Exception ex) {
                        price_txtbox.setText("0.00");
                    }
                    pack_size_txtbox.setText(aux.getPackSize() + "");
                    assy_txtbox.setText(aux.getAssyWorkstationName());
                    barcodes_nbre_txtbox.setText(aux.getAdditionalBarcode() + "");
                    lifes_txtbox.setText(aux.getLifes() + "");
                    for (int i = 0; i < pack_type_filter.getItemCount(); i++) {
                        if (pack_type_filter.getItemAt(i).toString().equals(aux.getPackType())) {
                            pack_type_filter.setSelectedIndex(i);
                            break;
                        }
                    }
                    //segment_txtbox.setText(aux.getSegment());
                    //workplace_txtbox.setText(aux.getWorkplace());
                    for (int i = 0; i < segment_filter.getItemCount(); i++) {
                        if (segment_filter.getItemAt(i).toString().equals(aux.getSegment())) {
                            segment_filter.setSelectedIndex(i);
                            break;
                        }
                    }
                    for (int i = 0; i < workplace_filter.getItemCount(); i++) {
                        if (workplace_filter.getItemAt(i).toString().equals(aux.getWorkplace())) {
                            workplace_filter.setSelectedIndex(i);
                            break;
                        }
                    }
                    for (int i = 0; i < harnessType_filter.getItemCount(); i++) {
                        if (harnessType_filter.getItemAt(i).toString().equals(aux.getHarnessType())) {
                            harnessType_filter.setSelectedIndex(i);
                            break;
                        }
                    }
                    if (String.valueOf(aux.getActive()).equals("1")) {
                        active_combobox.setSelectedIndex(0);
                    } else {
                        active_combobox.setSelectedIndex(1);
                    }

                    if (aux.getSpecialOrder() == 1) {
                        special_order_check.setSelected(true);
                    } else {
                        special_order_check.setSelected(false);
                    }
                    order_no_txt.setText(aux.getOrderNo());
                    comment_txt.setText(aux.getComment());
                    delete_btn.setEnabled(true);
                    duplicate_btn.setEnabled(true);
                }
            }
        }
        );
    }

    private void reset_table_content() {

        ucs_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(ucs_table_data, ucs_table_header);
        ucs_table.setModel(dataModel);
    }

    public void disableEditingTable() {
        for (int c = 0; c < ucs_table.getColumnCount(); c++) {
            Class<?> col_class = ucs_table.getColumnClass(c);
            ucs_table.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    /**
     * @param ucs_table_data
     * @param ucs_table_header
     * @param ucs_table
     * @todo : reload_table_data a mettre dans une classe interface
     * @param resultList
     */
    public void reload_table_data(List<Object[]> resultList, Vector ucs_table_data, Vector<String> ucs_table_header, JTable ucs_table) {
        this.reset_table_content();
        for (Object[] line : resultList) {
            @SuppressWarnings("UseOfObsoleteCollectionType")
            Vector<Object> oneRow = new Vector<Object>();
            for (Object cell : line) {
                oneRow.add(String.valueOf(cell));
            }
            ucs_table_data.add(oneRow);
        }

        ucs_table.setModel(new DefaultTableModel(ucs_table_data, ucs_table_header));
        ucs_table.setAutoCreateRowSorter(true);
    }

    private void clearFields() {
        id_lbl.setText("");
        create_time_txt.setText("");
        write_time_txt.setText("");
        cpn_txtbox.setText("");
        lpn_txtbox.setText("");
        index_txtbox.setText("");
        active_combobox.setSelectedIndex(0);
        assy_txtbox.setText("");
        lifes_txtbox.setText("-1");
        barcodes_nbre_txtbox.setText("0");
        pack_size_txtbox.setText("");
        stdTime_txtbox.setText("0.00");
        price_txtbox.setText("0.00");
        order_no_txt.setText("");
        delete_btn.setEnabled(false);
        duplicate_btn.setEnabled(false);
        msg_lbl.setText("");
        comment_txt.setText("");
        special_order_check.setSelected(false);
        aux = null;
    }

    private void clearSearchFields() {
        cpn_txtbox_search.setText("");
        pack_type_txtbox_search.setText("");
        segment_txtbox_search.setText("");
    }

    private void refresh() {
        Helper.startSession();
        String query_str = " SELECT "
                + " u.id AS id, "
                + " u.harness_part AS cpn, "
                + " u.supplier_part_number AS lpn, "
                + " u.order_no AS order_no, "
                + " u.harness_type AS harness_type, "
                + " u.harness_index AS harness_index, "
                + " u.pack_type AS pack_type, "
                + " u.pack_size AS pack_size, "
                + " u.additional_barcode AS barecodes, "
                + " u.std_time AS stdTime, "
                + " u.assy_workstation AS assy_workstation, "
                + " u.segment AS segment, "
                + " u.workplace AS workplace, "
                + " u.active AS active, "
                + " u.lifes AS lifes, "
                + " u.special_order AS special_order,"
                + " u.price "
                + " FROM Config_Ucs u WHERE 1=1 ";
        
        if (!cpn_txtbox_search.getText().trim().equals("")) {
            query_str += " AND harness_part LIKE '%" + cpn_txtbox_search.getText().trim() + "%'";
        }
        if (!pack_type_txtbox_search.getText().trim().equals("")) {
            query_str += " AND pack_type LIKE '%" + pack_type_txtbox_search.getText().trim() + "%'";
        }
        if (!segment_txtbox_search.getText().trim().equals("")) {
            query_str += " AND segment LIKE '%" + segment_txtbox_search.getText().trim() + "%'";
        }
        if (!supplier_pn_txtbox_search.getText().trim().equals("")) {
            query_str += " AND supplier_part_number LIKE '%" + supplier_pn_txtbox_search.getText().trim() + "%'";
        }
        query_str += " ORDER BY id DESC ";
        SQLQuery query = Helper.sess.createSQLQuery(query_str);
        resultList = query.list();

        Helper.sess.getTransaction().commit();

        this.reload_table_data(resultList, ucs_table_data, ucs_table_header, ucs_table);

        this.disableEditingTable();
    }

    /**
     * This method is called from within the constrauxctor to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        fname_lbl = new javax.swing.JLabel();
        cpn_txtbox = new javax.swing.JTextField();
        lname_lbl = new javax.swing.JLabel();
        lpn_txtbox = new javax.swing.JTextField();
        login_lbl = new javax.swing.JLabel();
        pwd_lbl = new javax.swing.JLabel();
        pwd_lbl1 = new javax.swing.JLabel();
        save_btn = new javax.swing.JButton();
        cancel_btn = new javax.swing.JButton();
        active_combobox = new javax.swing.JComboBox();
        pwd_lbl2 = new javax.swing.JLabel();
        delete_btn = new javax.swing.JButton();
        id_lbl = new javax.swing.JLabel();
        index_txtbox = new javax.swing.JTextField();
        pwd_lbl3 = new javax.swing.JLabel();
        pwd_lbl4 = new javax.swing.JLabel();
        pwd_lbl5 = new javax.swing.JLabel();
        assy_txtbox = new javax.swing.JTextField();
        login_lbl1 = new javax.swing.JLabel();
        pack_size_txtbox = new javax.swing.JTextField();
        lifes_txtbox = new javax.swing.JTextField();
        login_lbl2 = new javax.swing.JLabel();
        barcodes_nbre_txtbox = new javax.swing.JTextField();
        pwd_lbl6 = new javax.swing.JLabel();
        stdTime_txtbox = new javax.swing.JTextField();
        duplicate_btn = new javax.swing.JButton();
        pwd_lbl7 = new javax.swing.JLabel();
        msg_lbl = new javax.swing.JLabel();
        fname_lbl1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        comment_txt = new javax.swing.JTextArea();
        segment_filter = new javax.swing.JComboBox();
        workplace_filter = new javax.swing.JComboBox();
        harnessType_filter = new javax.swing.JComboBox();
        pack_type_filter = new javax.swing.JComboBox();
        order_no_txt = new javax.swing.JTextField();
        login_lbl4 = new javax.swing.JLabel();
        special_order_check = new javax.swing.JCheckBox();
        pwd_lbl8 = new javax.swing.JLabel();
        price_txtbox = new javax.swing.JTextField();
        user_list_panel = new javax.swing.JPanel();
        user_table_scroll = new javax.swing.JScrollPane();
        ucs_table = new javax.swing.JTable();
        cpn_txtbox_search = new javax.swing.JTextField();
        fname_lbl_search = new javax.swing.JLabel();
        lname_lbl_search = new javax.swing.JLabel();
        pack_type_txtbox_search = new javax.swing.JTextField();
        llogin_lbl_search = new javax.swing.JLabel();
        segment_txtbox_search = new javax.swing.JTextField();
        filter_btn = new javax.swing.JButton();
        clear_search_btn = new javax.swing.JButton();
        supplier_pn_txtbox_search = new javax.swing.JTextField();
        llogin_lbl_search1 = new javax.swing.JLabel();
        lname_lbl1 = new javax.swing.JLabel();
        create_time_txt = new javax.swing.JTextField();
        lname_lbl2 = new javax.swing.JLabel();
        write_time_txt = new javax.swing.JTextField();

        setTitle("Configuration packaging par référence");
        setName("Configuration packaging par référence"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuration packaging par référence", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        jPanel1.setToolTipText("Configuration packaging par référence");
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setName("Configuration packaging par référence"); // NOI18N

        fname_lbl.setText("CPN *");

        cpn_txtbox.setName("cpn_txtbox"); // NOI18N

        lname_lbl.setText("LPN *");

        lpn_txtbox.setName("fname_txtbox"); // NOI18N

        login_lbl.setText("Segment *");

        pwd_lbl.setText("Workplace *");

        pwd_lbl1.setText("Index *");

        save_btn.setText("Save");
        save_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_btnActionPerformed(evt);
            }
        });

        cancel_btn.setText("Clear");
        cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_btnActionPerformed(evt);
            }
        });

        active_combobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "0" }));
        active_combobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                active_comboboxActionPerformed(evt);
            }
        });

        pwd_lbl2.setText("Active *");

        delete_btn.setText("Delete");
        delete_btn.setEnabled(false);
        delete_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_btnActionPerformed(evt);
            }
        });

        id_lbl.setBackground(new java.awt.Color(153, 204, 255));
        id_lbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        id_lbl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        id_lbl.setRequestFocusEnabled(false);

        index_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_lbl3.setText("Pack Type *");

        pwd_lbl4.setText("Pack Size *");

        pwd_lbl5.setText("Nbre Packs *");

        assy_txtbox.setName("fname_txtbox"); // NOI18N

        login_lbl1.setText("Assembly Workstation");

        pack_size_txtbox.setText("1");
        pack_size_txtbox.setName("fname_txtbox"); // NOI18N

        lifes_txtbox.setText("-1");
        lifes_txtbox.setName("fname_txtbox"); // NOI18N

        login_lbl2.setText("Barcodes Nbre *");

        barcodes_nbre_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_lbl6.setText("Std Time *");

        stdTime_txtbox.setText("0.00");
        stdTime_txtbox.setName("fname_txtbox"); // NOI18N
        stdTime_txtbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stdTime_txtboxActionPerformed(evt);
            }
        });

        duplicate_btn.setText("Dupliquer");
        duplicate_btn.setEnabled(false);
        duplicate_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duplicate_btnActionPerformed(evt);
            }
        });

        pwd_lbl7.setText("Harness Type *");

        msg_lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        msg_lbl.setForeground(new java.awt.Color(0, 0, 255));

        fname_lbl1.setText("ID");

        comment_txt.setColumns(10);
        comment_txt.setRows(3);
        jScrollPane1.setViewportView(comment_txt);

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

        order_no_txt.setName("fname_txtbox"); // NOI18N

        login_lbl4.setText("Order No *");

        special_order_check.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        special_order_check.setText("Special Order");
        special_order_check.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                special_order_checkStateChanged(evt);
            }
        });

        pwd_lbl8.setText("Price *");

        price_txtbox.setText("0.00");
        price_txtbox.setName("fname_txtbox"); // NOI18N

        user_list_panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "UCS list", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        user_list_panel.setToolTipText("");

        ucs_table.setModel(new javax.swing.table.DefaultTableModel(
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
        user_table_scroll.setViewportView(ucs_table);

        cpn_txtbox_search.setName("fname_txtbox"); // NOI18N
        cpn_txtbox_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cpn_txtbox_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cpn_txtbox_searchKeyTyped(evt);
            }
        });

        fname_lbl_search.setText("CPN");

        lname_lbl_search.setText("Pack Type");

        pack_type_txtbox_search.setName("fname_txtbox"); // NOI18N
        pack_type_txtbox_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pack_type_txtbox_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pack_type_txtbox_searchKeyTyped(evt);
            }
        });

        llogin_lbl_search.setText("Segment");

        segment_txtbox_search.setName("fname_txtbox"); // NOI18N
        segment_txtbox_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                segment_txtbox_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                segment_txtbox_searchKeyTyped(evt);
            }
        });

        filter_btn.setText("Filter");
        filter_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filter_btnActionPerformed(evt);
            }
        });

        clear_search_btn.setText("Clear filters");
        clear_search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_search_btnActionPerformed(evt);
            }
        });

        supplier_pn_txtbox_search.setName("fname_txtbox"); // NOI18N
        supplier_pn_txtbox_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                supplier_pn_txtbox_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                supplier_pn_txtbox_searchKeyTyped(evt);
            }
        });

        llogin_lbl_search1.setText("LEONI PN");

        javax.swing.GroupLayout user_list_panelLayout = new javax.swing.GroupLayout(user_list_panel);
        user_list_panel.setLayout(user_list_panelLayout);
        user_list_panelLayout.setHorizontalGroup(
            user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(user_list_panelLayout.createSequentialGroup()
                .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, user_list_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fname_lbl_search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cpn_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lname_lbl_search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pack_type_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(llogin_lbl_search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(segment_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(llogin_lbl_search1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplier_pn_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 297, Short.MAX_VALUE)
                        .addComponent(filter_btn)
                        .addGap(18, 18, 18)
                        .addComponent(clear_search_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83))
                    .addComponent(user_table_scroll))
                .addContainerGap())
        );
        user_list_panelLayout.setVerticalGroup(
            user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, user_list_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filter_btn)
                        .addComponent(clear_search_btn))
                    .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(llogin_lbl_search1)
                            .addComponent(supplier_pn_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cpn_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fname_lbl_search)
                            .addComponent(lname_lbl_search)
                            .addComponent(pack_type_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(llogin_lbl_search)
                            .addComponent(segment_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(user_table_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE))
        );

        lname_lbl1.setText("Creation Date");

        create_time_txt.setEditable(false);
        create_time_txt.setName("fname_txtbox"); // NOI18N

        lname_lbl2.setText("Write Date");

        write_time_txt.setEditable(false);
        write_time_txt.setName("fname_txtbox"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(user_list_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(msg_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fname_lbl)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(login_lbl, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(fname_lbl1))
                                    .addComponent(login_lbl4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cpn_txtbox, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(order_no_txt, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(segment_filter, javax.swing.GroupLayout.Alignment.LEADING, 0, 173, Short.MAX_VALUE)
                                    .addComponent(id_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(lname_lbl)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                                                .addComponent(lpn_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(login_lbl2)
                                                    .addComponent(pwd_lbl))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(workplace_filter, 0, 146, Short.MAX_VALUE)
                                                    .addComponent(barcodes_nbre_txtbox))))
                                        .addGap(58, 58, 58))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lname_lbl1)
                                        .addGap(23, 23, 23)
                                        .addComponent(create_time_txt)
                                        .addGap(18, 18, 18))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(save_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(duplicate_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cancel_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(98, 98, 98)
                                        .addComponent(delete_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(special_order_check)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pwd_lbl1)
                                    .addComponent(pwd_lbl7)
                                    .addComponent(login_lbl1))
                                .addGap(1, 1, 1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lname_lbl2)
                                .addGap(57, 57, 57)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(write_time_txt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(assy_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pwd_lbl6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(index_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pwd_lbl3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(harnessType_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pwd_lbl2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pack_type_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(stdTime_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(pwd_lbl8)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(pwd_lbl4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pack_size_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(pwd_lbl5)
                                        .addGap(0, 0, 0)
                                        .addComponent(lifes_txtbox))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(price_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(active_combobox, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(30, 30, 30))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(msg_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fname_lbl1)
                            .addComponent(id_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fname_lbl)
                            .addComponent(cpn_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lname_lbl)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(create_time_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lname_lbl2)
                            .addComponent(write_time_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lname_lbl1))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lpn_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pwd_lbl1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(index_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pwd_lbl3)
                                .addComponent(pack_type_filter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pwd_lbl4)
                                .addComponent(pack_size_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pwd_lbl5)
                                .addComponent(lifes_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pwd_lbl2)
                            .addComponent(active_combobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(login_lbl)
                        .addComponent(pwd_lbl)
                        .addComponent(pwd_lbl7)
                        .addComponent(segment_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(workplace_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(harnessType_filter)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(login_lbl1)
                    .addComponent(assy_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(barcodes_nbre_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_lbl2)
                    .addComponent(order_no_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_lbl4)
                    .addComponent(pwd_lbl6)
                    .addComponent(stdTime_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_lbl8)
                    .addComponent(price_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(special_order_check))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancel_btn)
                        .addComponent(delete_btn))
                    .addComponent(duplicate_btn)
                    .addComponent(save_btn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(user_list_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clear_search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_search_btnActionPerformed
        clearSearchFields();
    }//GEN-LAST:event_clear_search_btnActionPerformed

    private void filter_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filter_btnActionPerformed
        refresh();
    }//GEN-LAST:event_filter_btnActionPerformed

    private void segment_txtbox_searchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_segment_txtbox_searchKeyTyped
        refresh();
    }//GEN-LAST:event_segment_txtbox_searchKeyTyped

    private void segment_txtbox_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_segment_txtbox_searchKeyPressed

    }//GEN-LAST:event_segment_txtbox_searchKeyPressed

    private void pack_type_txtbox_searchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pack_type_txtbox_searchKeyTyped
        refresh();
    }//GEN-LAST:event_pack_type_txtbox_searchKeyTyped

    private void pack_type_txtbox_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pack_type_txtbox_searchKeyPressed

    }//GEN-LAST:event_pack_type_txtbox_searchKeyPressed

    private void cpn_txtbox_searchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cpn_txtbox_searchKeyTyped
        refresh();
    }//GEN-LAST:event_cpn_txtbox_searchKeyTyped

    private void cpn_txtbox_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cpn_txtbox_searchKeyPressed

    }//GEN-LAST:event_cpn_txtbox_searchKeyPressed

    private void special_order_checkStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_special_order_checkStateChanged
    }//GEN-LAST:event_special_order_checkStateChanged

    private void workplace_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workplace_filterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_workplace_filterActionPerformed

    private void workplace_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_workplace_filterItemStateChanged

    }//GEN-LAST:event_workplace_filterItemStateChanged

    private void segment_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segment_filterActionPerformed

    }//GEN-LAST:event_segment_filterActionPerformed

    private void segment_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_segment_filterItemStateChanged
        System.out.println("Selected Segment " + String.valueOf(segment_filter.getSelectedItem()));
        if ("ALL".equals(String.valueOf(segment_filter.getSelectedItem()).trim())) {
            this.workplace_filter.setSelectedIndex(0);
            this.workplace_filter.setEnabled(false);
        } else {
            this.workplace_filter.removeAllItems();
            this.workplace_filter.setEnabled(true);
            this.setWorkplaceBySegment(String.valueOf(segment_filter.getSelectedItem()));
        }

        refresh();
    }//GEN-LAST:event_segment_filterItemStateChanged

    private void duplicate_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_duplicate_btnActionPerformed
        id_lbl.setText("");
        cpn_txtbox.setText(aux.getHarnessPart());
        lpn_txtbox.setText(aux.getSupplierPartNumber());
        index_txtbox.setText(aux.getHarnessIndex());
        stdTime_txtbox.setText(aux.getStdTime() + "");
        price_txtbox.setText(aux.getPrice() + "");

        pack_size_txtbox.setText(aux.getPackSize() + "");
        for (int i = 0; i < pack_type_filter.getItemCount(); i++) {
            if (pack_type_filter.getItemAt(i).toString().equals(aux.getPackType())) {
                pack_type_filter.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < segment_filter.getItemCount(); i++) {
            if (segment_filter.getItemAt(i).toString().equals(aux.getSegment())) {
                segment_filter.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < workplace_filter.getItemCount(); i++) {
            if (workplace_filter.getItemAt(i).toString().equals(aux.getWorkplace())) {
                workplace_filter.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < harnessType_filter.getItemCount(); i++) {
            if (harnessType_filter.getItemAt(i).toString().equals(aux.getHarnessType())) {
                harnessType_filter.setSelectedIndex(i);
                break;
            }
        }

        if (aux.getSpecialOrder() == 1) {
            special_order_check.setSelected(true);
        } else {
            special_order_check.setSelected(false);
        }

        assy_txtbox.setText(aux.getAssyWorkstationName());
        barcodes_nbre_txtbox.setText(aux.getAdditionalBarcode() + "");
        lifes_txtbox.setText(aux.getLifes() + "");
        if (String.valueOf(aux.getActive()).equals("1")) {
            active_combobox.setSelectedIndex(0);
        } else {
            active_combobox.setSelectedIndex(1);
        }
        this.aux = null;
        msg_lbl.setText("Element dupliqué !");
    }//GEN-LAST:event_duplicate_btnActionPerformed

    private void stdTime_txtboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stdTime_txtboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stdTime_txtboxActionPerformed

    private void delete_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_btnActionPerformed
        int confirmed = JOptionPane.showConfirmDialog(this,
                String.format("Confirmez-vous la suppression de cet élement [%s] ?",
                        this.aux.getId()),
                "Suppression UCS",
                JOptionPane.WARNING_MESSAGE);

        if (confirmed == 0) {
            aux.delete(aux);
            clearFields();
            msg_lbl.setText("Elément supprimé !");
            refresh();
        }
    }//GEN-LAST:event_delete_btnActionPerformed

    private void active_comboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_active_comboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_active_comboboxActionPerformed

    private void cancel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_btnActionPerformed
        clearFields();
    }//GEN-LAST:event_cancel_btnActionPerformed

    private void save_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_btnActionPerformed
        if (lpn_txtbox.getText().isEmpty() || cpn_txtbox.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Valeur null pour un ou plusieurs champs. \nMerci de remplir touts les champs requis !", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (id_lbl.getText().isEmpty()) { // ID Label is empty, then is a new Object
                ConfigUcs mu = new ConfigUcs();
                mu.setCreateId(PackagingVars.context.getUser().getId());
                mu.setWriteId(PackagingVars.context.getUser().getId());
                mu.setCreateTime(new Date());
                mu.setWriteTime(new Date());
                mu.setHarnessPart(cpn_txtbox.getText().trim());
                mu.setSupplierPartNumber(lpn_txtbox.getText().trim());
                mu.setHarnessIndex(index_txtbox.getText().trim());
                mu.setPackType(pack_type_filter.getSelectedItem().toString());
                mu.setPackSize(Integer.valueOf(pack_size_txtbox.getText().trim()));
                mu.setActive(Integer.valueOf(active_combobox.getSelectedItem().toString()));
                mu.setSegment(segment_filter.getSelectedItem().toString());
                mu.setWorkplace(workplace_filter.getSelectedItem().toString());
                mu.setHarnessType(harnessType_filter.getSelectedItem().toString());
                mu.setLifes(Integer.valueOf(lifes_txtbox.getText().trim()));
                mu.setStdTime(Double.valueOf(stdTime_txtbox.getText().trim()));
                mu.setPrice(Double.valueOf(price_txtbox.getText().trim()));
                mu.setAdditionalBarcode(Integer.valueOf(barcodes_nbre_txtbox.getText().trim()));
                if (assy_txtbox.getText().isEmpty()) {
                    mu.setAssyWorkstationName("-");
                } else {
                    mu.setAssyWorkstationName(assy_txtbox.getText().trim());
                }
                mu.setComment(comment_txt.getText());
                if (order_no_txt.getText().isEmpty()) {
                    mu.setOrderNo("-");
                } else {
                    mu.setOrderNo(order_no_txt.getText());
                }
                if (special_order_check.isSelected()) {
                    mu.setSpecialOrder(1);
                } else {
                    mu.setSpecialOrder(0);
                }
                mu.create(mu);

                clearFields();
                msg_lbl.setText("Nouveau élément enregistré !");
                refresh();
            } else { // ID Label is filed, then is an update
                aux.setWriteId(PackagingVars.context.getUser().getId());
                aux.setWriteTime(new Date());
                aux.setHarnessPart(cpn_txtbox.getText().trim());
                aux.setSupplierPartNumber(lpn_txtbox.getText().trim());
                aux.setHarnessIndex(index_txtbox.getText().trim());
                aux.setPackType(pack_type_filter.getSelectedItem().toString());
                aux.setPackSize(Integer.valueOf(pack_size_txtbox.getText().trim()));
                aux.setActive(Integer.valueOf(active_combobox.getSelectedItem().toString()));
                aux.setSegment(segment_filter.getSelectedItem().toString());
                aux.setWorkplace(workplace_filter.getSelectedItem().toString());
                aux.setHarnessType(harnessType_filter.getSelectedItem().toString());
                aux.setLifes(Integer.valueOf(lifes_txtbox.getText().trim()));
                aux.setStdTime(Double.valueOf(stdTime_txtbox.getText().trim()));
                aux.setPrice(Double.valueOf(price_txtbox.getText().trim()));
                aux.setAdditionalBarcode(Integer.valueOf(barcodes_nbre_txtbox.getText().trim()));
                aux.setAssyWorkstationName(assy_txtbox.getText().trim());
                aux.setComment(comment_txt.getText());
                aux.setOrderNo(order_no_txt.getText());
                if (special_order_check.isSelected()) {
                    aux.setSpecialOrder(1);
                } else {
                    aux.setSpecialOrder(0);
                }
                aux.update(aux);
                clearFields();
                msg_lbl.setText("Changements enregistrés !");
                refresh();
            }
        }
    }//GEN-LAST:event_save_btnActionPerformed

    private void supplier_pn_txtbox_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplier_pn_txtbox_searchKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplier_pn_txtbox_searchKeyPressed

    private void supplier_pn_txtbox_searchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplier_pn_txtbox_searchKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_supplier_pn_txtbox_searchKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox active_combobox;
    private javax.swing.JTextField assy_txtbox;
    private javax.swing.JTextField barcodes_nbre_txtbox;
    private javax.swing.JButton cancel_btn;
    private javax.swing.JButton clear_search_btn;
    private javax.swing.JTextArea comment_txt;
    private javax.swing.JTextField cpn_txtbox;
    private javax.swing.JTextField cpn_txtbox_search;
    private javax.swing.JTextField create_time_txt;
    private javax.swing.JButton delete_btn;
    private javax.swing.JButton duplicate_btn;
    private javax.swing.JButton filter_btn;
    private javax.swing.JLabel fname_lbl;
    private javax.swing.JLabel fname_lbl1;
    private javax.swing.JLabel fname_lbl_search;
    private javax.swing.JComboBox harnessType_filter;
    private javax.swing.JLabel id_lbl;
    private javax.swing.JTextField index_txtbox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lifes_txtbox;
    private javax.swing.JLabel llogin_lbl_search;
    private javax.swing.JLabel llogin_lbl_search1;
    private javax.swing.JLabel lname_lbl;
    private javax.swing.JLabel lname_lbl1;
    private javax.swing.JLabel lname_lbl2;
    private javax.swing.JLabel lname_lbl_search;
    private javax.swing.JLabel login_lbl;
    private javax.swing.JLabel login_lbl1;
    private javax.swing.JLabel login_lbl2;
    private javax.swing.JLabel login_lbl4;
    private javax.swing.JTextField lpn_txtbox;
    private javax.swing.JLabel msg_lbl;
    private javax.swing.JTextField order_no_txt;
    private javax.swing.JTextField pack_size_txtbox;
    private javax.swing.JComboBox pack_type_filter;
    private javax.swing.JTextField pack_type_txtbox_search;
    private javax.swing.JTextField price_txtbox;
    private javax.swing.JLabel pwd_lbl;
    private javax.swing.JLabel pwd_lbl1;
    private javax.swing.JLabel pwd_lbl2;
    private javax.swing.JLabel pwd_lbl3;
    private javax.swing.JLabel pwd_lbl4;
    private javax.swing.JLabel pwd_lbl5;
    private javax.swing.JLabel pwd_lbl6;
    private javax.swing.JLabel pwd_lbl7;
    private javax.swing.JLabel pwd_lbl8;
    private javax.swing.JButton save_btn;
    private javax.swing.JComboBox segment_filter;
    private javax.swing.JTextField segment_txtbox_search;
    private javax.swing.JCheckBox special_order_check;
    private javax.swing.JTextField stdTime_txtbox;
    private javax.swing.JTextField supplier_pn_txtbox_search;
    private javax.swing.JTable ucs_table;
    private javax.swing.JPanel user_list_panel;
    private javax.swing.JScrollPane user_table_scroll;
    private javax.swing.JComboBox workplace_filter;
    private javax.swing.JTextField write_time_txt;
    // End of variables declaration//GEN-END:variables

}
