/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_fg_reception;

import __main__.GlobalVars;
import helper.Helper;
import entity.BaseContainer;
import entity.ConfigSegment;
import entity.ConfigWorkplace;
import gui.packaging.PackagingVars;
import gui.packaging.reports.PACKAGING_UI0010_PalletDetails;
import gui.packaging.reports.PACKAGING_UI0011_ProdStatistics;
import helper.ComboItem;
import helper.JDialogExcelFileChooser;
import helper.UIHelper;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
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
 * @author user
 */
public final class WAREHOUSE_FG_UI0002_PALLET_LIST extends javax.swing.JFrame {

    Vector<String> searchResult_table_header = new Vector<String>();
    List<String> table_header = Arrays.asList(
            "Segment",
            "Workplace",
            "Pack Number",
            "Harness Part",
            "Create Time",
            "Fifo Time",
            "Supplier Part Number",
            "Pack Type",
            "Pack Size",
            "Qty Read",
            "Std Time",
            "Total Std Time",
            "Index",
            "User",
            "State",
            "State code"
    );

    SimpleDateFormat timeDf = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateDf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeDf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String startTimeStr = "";
    String endTimeStr = "";
    String startDateStr = null;
    String endDateStr = null;

    List<Object> segments = new ArrayList<Object>();
    List<Object> workplaces = new ArrayList<Object>();

    List<Object[]> resultList;

    Vector searchResult_table_data = new Vector();
    private BaseContainer bc = new BaseContainer();
    /* "Pallet number" Column index in "container_table" */
    private static int PALLET_NUMBER_COLINDEX = 2;

    /**
     * Creates new form UI0010_PalletDetails
     *
     * @param parent
     * @param modal
     */
    public WAREHOUSE_FG_UI0002_PALLET_LIST(java.awt.Frame parent, boolean modal) {

        initComponents();
        initGui();
    }

    /**
     * Creates new form UI0010_PalletDetails
     *
     * @param parent
     * @param modal
     * @param PalletNumber : Requested container number to be displayed
     */
    public WAREHOUSE_FG_UI0002_PALLET_LIST(java.awt.Frame parent, boolean modal, String PalletNumber) {

        initComponents();
        initGui();

    }

    /**
     * Creates new form UI0010_PalletDetails
     *
     * @param parent
     * @param modal
     * @param PalletNumber : Requested container number to be displayed
     * @param drop : Show drop button in the form
     * @param printOpenSheet : Show print open sheet button in the form
     * @param printCloseSheet : Show print close sheet button in the form
     *
     */
    public WAREHOUSE_FG_UI0002_PALLET_LIST(java.awt.Frame parent, boolean modal,
            String PalletNumber, boolean drop, boolean printOpenSheet,
            boolean printCloseSheet) {

        initComponents();
        initGui();

    }

    private void initContainerTableDoubleClick() {
        this.searchResult_table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //System.out.println("PackagingVars.context.getUser().getAccessLevel()" + PackagingVars.context.getUser().getAccessLevel());
                    if (PackagingVars.context.getUser().getAccessLevel() == GlobalVars.PROFIL_ADMIN) {
                        new PACKAGING_UI0010_PalletDetails(null, rootPaneCheckingEnabled, String.valueOf(searchResult_table.getValueAt(searchResult_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)), "", 1, true, true, true).setVisible(true);
                    } else {
                        new PACKAGING_UI0010_PalletDetails(null, rootPaneCheckingEnabled, String.valueOf(searchResult_table.getValueAt(searchResult_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)), "", 1, false, false, false).setVisible(true);
                    }
                }
            }
        }
        );
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

    private void initStateFilters() {
        JRadioButton[] radioButtonList = new JRadioButton[GlobalVars.PALLET_STATES.length];
        jpanel_state.setLayout(new GridLayout(0, 3, 6, 6));
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < GlobalVars.PALLET_STATES.length; i++) {
            if (GlobalVars.PALLET_STATES[i][1].toString().equals("selected")) {
                radioButtonList[i] = new JRadioButton(GlobalVars.PALLET_STATES[i][0], true);
            } else {
                radioButtonList[i] = new JRadioButton(GlobalVars.PALLET_STATES[i][0], false);
            }
            radioButtonList[i].addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    //export_btnActionPerformed(evt);
                    refresh_btnActionPerformed(evt);
                }
            });
            group.add(radioButtonList[i]);
            jpanel_state.add(radioButtonList[i]);
            jpanel_state.revalidate();
            jpanel_state.repaint();
        }
    }

    private void initGui() {
        //Center the this dialog in the screen
        Helper.centerJFrame(this);

        //Desable table edition
        disableEditingTable();

        //Load table header
        load_table_header();

        //Init time spinner
        initTimeSpinners();

        initSegmentFilter();

        initStateFilters();

        this.workplace_filter.setEnabled(false);

        //Support double click on rows in container jtable to display history
        this.initContainerTableDoubleClick();
    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            searchResult_table_header.add(it.next());
        }

        searchResult_table.setModel(new DefaultTableModel(searchResult_table_data, searchResult_table_header));
    }

    private void reset_table_content() {

        searchResult_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(searchResult_table_data, searchResult_table_header);
        searchResult_table.setModel(dataModel);
    }

    public void disableEditingTable() {
        for (int c = 0; c < searchResult_table.getColumnCount(); c++) {
            Class<?> col_class = searchResult_table.getColumnClass(c);
            searchResult_table.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    public BaseContainer getBaseContainer() {
        return bc;
    }

    public void setBaseContainer(BaseContainer bc) {
        this.bc = bc;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        searchResult_table = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        startDatePicker = new org.jdesktop.swingx.JXDatePicker();
        startTimeSpinner = new javax.swing.JSpinner();
        segment_filter = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        workplace_filter = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        total_declared_lbl = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        endDatePicker = new org.jdesktop.swingx.JXDatePicker();
        endTimeSpinner = new javax.swing.JSpinner();
        harness_part_txt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jpanel_state = new javax.swing.JPanel();
        refresh_btn = new javax.swing.JButton();
        lafm_btn = new javax.swing.JButton();
        export_btn = new javax.swing.JButton();
        nbreLigne = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pallet List");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(194, 227, 254));

        searchResult_table.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(searchResult_table);

        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setText("INFO : Double clique sur une ligne pour consulter l'historique.");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("De");

        startDatePicker.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        startDatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDatePickerActionPerformed(evt);
            }
        });

        startTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

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

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Segment");

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
        jLabel22.setText("Workplace");

        total_declared_lbl.setEditable(false);
        total_declared_lbl.setBackground(new java.awt.Color(153, 255, 255));
        total_declared_lbl.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        total_declared_lbl.setText("0");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Σ Quantités produites");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("A");

        endDatePicker.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        endTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        harness_part_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harness_part_txtActionPerformed(evt);
            }
        });
        harness_part_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                harness_part_txtKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Part number");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Statut process");

        jpanel_state.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout jpanel_stateLayout = new javax.swing.GroupLayout(jpanel_state);
        jpanel_state.setLayout(jpanel_stateLayout);
        jpanel_stateLayout.setHorizontalGroup(
            jpanel_stateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 544, Short.MAX_VALUE)
        );
        jpanel_stateLayout.setVerticalGroup(
            jpanel_stateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 233, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jpanel_state);

        refresh_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        refresh_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/refresh.png"))); // NOI18N
        refresh_btn.setText("Refresh");
        refresh_btn.setToolTipText("Filter");
        refresh_btn.setBorderPainted(false);
        refresh_btn.setMaximumSize(new java.awt.Dimension(24, 24));
        refresh_btn.setMinimumSize(new java.awt.Dimension(24, 24));
        refresh_btn.setOpaque(false);
        refresh_btn.setPreferredSize(new java.awt.Dimension(24, 24));
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        lafm_btn.setText("Exporter Données LAFM...");
        lafm_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lafm_btnActionPerformed(evt);
            }
        });

        export_btn.setText("Exporter en Excel...");
        export_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                export_btnActionPerformed(evt);
            }
        });

        nbreLigne.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        nbreLigne.setText("0");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Lignes");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(endDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(export_btn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lafm_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 30, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(startDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(segment_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(workplace_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(harness_part_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(total_declared_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(63, 63, 63)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nbreLigne)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jLabel6)))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(segment_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(workplace_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total_declared_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(harness_part_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel3)
                            .addComponent(endDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lafm_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(export_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nbreLigne)
                    .addComponent(jLabel7))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void clearSearchBox() {
        //Vider le champs de text scan

    }

    public void reset_container_table_content() {
        searchResult_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(searchResult_table_data, searchResult_table_header);
        searchResult_table.setModel(dataModel);
    }

    public void reload_container_table_data(List<Object[]> resultList) {
        this.reset_container_table_content();
        int total = 0;
        for (Object[] obj : resultList) {
            @SuppressWarnings("UseOfObsoleteCollectionType")
            Vector<Object> oneRow = new Vector<Object>();

            oneRow.add(String.valueOf(obj[0])); // "Segment",
            oneRow.add(String.valueOf(obj[1])); // "Workplace",
            oneRow.add(String.valueOf(obj[2])); // "Pack Number",
            oneRow.add(String.valueOf(obj[3])); // "Harness Part",
            oneRow.add(String.valueOf(obj[4])); // "Create Time",
            oneRow.add(String.valueOf(obj[5])); // "Update Time",
            oneRow.add(String.valueOf(obj[6])); // "Supplier Part Number",
            oneRow.add(String.valueOf(obj[7])); // "Pack Type",
            oneRow.add(String.valueOf(obj[8])); // "Pack Size",
            oneRow.add(String.valueOf(obj[9])); // "Qty Read",
            oneRow.add(String.valueOf(obj[10])); // "Std Time",            
            oneRow.add(String.valueOf(obj[11])); // "Total Std Time",
            oneRow.add(String.valueOf(obj[12])); // "Index",
            oneRow.add(String.valueOf(obj[13])); // "User",
            oneRow.add(String.valueOf(obj[14])); // "State",
            oneRow.add(String.valueOf(obj[15])); // "State code"

            total = total + Integer.valueOf(String.valueOf(obj[9]));

            searchResult_table_data.add(oneRow);
        }
        total_declared_lbl.setText(String.valueOf(total));
        searchResult_table.setModel(new DefaultTableModel(searchResult_table_data, searchResult_table_header));
        searchResult_table.setAutoCreateRowSorter(true);
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
            Logger.getLogger(WAREHOUSE_FG_UI0002_PALLET_LIST.class.getName()).log(Level.SEVERE, null, ex);
        }

        //################# End Time Spinner ######################
        endTimeSpinner.setModel(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endTimeEditor);
        try {
            endTimeSpinner.setValue(timeFormat.parse(endTime));
        } catch (ParseException ex) {
            Logger.getLogger(WAREHOUSE_FG_UI0002_PALLET_LIST.class.getName()).log(Level.SEVERE, null, ex);
        }

        startDatePicker.setDate(new Date());
        endDatePicker.setDate(new Date());

    }

    private boolean checkValidFields() {
        if (startTimeSpinner.getValue() != ""
                && endTimeSpinner.getValue() != "") {
            return true;
        } else {
            return false;
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

    private void refresh() {

        if (checkValidFields()) {
            segments.clear();
            workplaces.clear();

            startTimeStr = timeDf.format(startTimeSpinner.getValue());
            endTimeStr = timeDf.format(endTimeSpinner.getValue());
            startDateStr = dateDf.format(startDatePicker.getDate()) + " " + startTimeStr;
            endDateStr = dateDf.format(endDatePicker.getDate()) + " " + endTimeStr;

            //Populate the segments Array with data
            if (String.valueOf(segment_filter.getSelectedItem()).equals("ALL")) {
                List result = new ConfigSegment().select();
                if (result.isEmpty()) {
                    JOptionPane.showMessageDialog(null, Helper.ERR0026_NO_SEGMENT_FOUND, "Configuration error !", ERROR_MESSAGE);
                    System.err.println(Helper.ERR0026_NO_SEGMENT_FOUND);
                } else { //Map project data in the list
                    for (Object o : result) {
                        ConfigSegment cs = (ConfigSegment) o;
                        segments.add(String.valueOf(cs.getSegment()));
                    }
                }
                result = new ConfigWorkplace().select();
                if (result.isEmpty()) {
                    JOptionPane.showMessageDialog(null, Helper.ERR0027_NO_WORKPLACE_FOUND, "Configuration error !", ERROR_MESSAGE);
                    System.err.println(Helper.ERR0027_NO_WORKPLACE_FOUND);
                } else { //Map project data in the list
                    for (Object o : result) {
                        ConfigWorkplace cw = (ConfigWorkplace) o;
                        workplaces.add(String.valueOf(cw.getWorkplace()));
                    }
                }
            } else {
                segments.add(String.valueOf(segment_filter.getSelectedItem()));
                //Populate the workplaces Array with data
                if (String.valueOf(workplace_filter.getSelectedItem()).equals("ALL")) {
                    List result = new ConfigWorkplace().selectBySegment(String.valueOf(segment_filter.getSelectedItem()));
                    if (result.isEmpty()) {
                        JOptionPane.showMessageDialog(null, Helper.ERR0027_NO_WORKPLACE_FOUND, "Configuration error !", ERROR_MESSAGE);
                        System.err.println(Helper.ERR0027_NO_WORKPLACE_FOUND);
                    } else { //Map project data in the list
                        for (Object o : result) {
                            ConfigWorkplace cw = (ConfigWorkplace) o;
                            workplaces.add(String.valueOf(cw.getWorkplace()));
                        }
                    }
                } else {
                    workplaces.add(String.valueOf(workplace_filter.getSelectedItem()));
                }
            }

            String state = "";
            //state = "1");

            List<String> selected = UIHelper.manageCheckedRadioButtons(jpanel_state);

            System.out.println("selected" + selected);

            for (int i = 0; i < selected.size(); i++) {

                if (selected.get(i).equals(GlobalVars.PALLET_OPEN)) {
                    state = GlobalVars.PALLET_OPEN;
                }
                if (selected.get(i).equals(GlobalVars.PALLET_WAITING)) {
                    state = GlobalVars.PALLET_WAITING;
                }
                if (selected.get(i).equals(GlobalVars.PALLET_CLOSED)) {
                    state = GlobalVars.PALLET_CLOSED;
                }
                if (selected.get(i).equals(GlobalVars.PALLET_STORED)) {
                    state = GlobalVars.PALLET_STORED;
                }
                if (selected.get(i).equals(GlobalVars.PALLET_SHIPPED)) {
                    state = GlobalVars.PALLET_SHIPPED;
                }
                if (selected.get(i).equals(GlobalVars.PALLET_QUARANTAINE)) {
                    state = GlobalVars.PALLET_QUARANTAINE;
                }
                if (selected.get(i).equals(GlobalVars.PALLET_DROPPED)) {
                    state = GlobalVars.PALLET_DROPPED;
                }

            }
            System.out.println("state " + state);

            //################# Harness Data ####################            
            Helper.startSession();
            try {
                String q = " SELECT bc.segment AS segment, "
                        + " bc.workplace AS workplace, "
                        + " bc.pallet_number AS pack_number, "
                        + " bc.harness_part AS harness_part, "
                        + " bc.create_time AS create_time, "
                        + " bc.write_time AS fifo_time, "
                        + " bc.supplier_part_number AS supplier_part_number, "
                        + " bc.pack_type AS pack_type, "
                        + " bc.qty_expected AS pack_size, "
                        + " bc.qty_read AS qty_read, "
                        + " bc.std_time AS std_time, "
                        + " bc.std_time*bc.qty_read AS total_std_time, "
                        + " bc.harness_index AS index, "
                        + " bc.m_user || ' / ' || bc.create_user AS user, "
                        + " bc.container_state AS state, "
                        + " bc.container_state_code AS state_code "
                        + " FROM base_container bc "
                        + " WHERE bc.segment IN (:segments)"
                        + " AND bc.workplace IN (:workplaces)";

                //Add harness part filter                
                if (!harness_part_txt.getText().isEmpty()) {
                    q += " AND harness_part LIKE :hp";
                }

                System.out.println("Query" + q);

                try {
                    Date startDate = dateTimeDf.parse(startDateStr);
                    Date endDate = dateTimeDf.parse(endDateStr);
                    System.out.println(startDate.before(endDate));
                } catch (Exception ex) {
                    Logger.getLogger(PACKAGING_UI0011_ProdStatistics.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (UIHelper.listContains(selected, GlobalVars.PALLET_OPEN)) {
                    if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
                        q += " AND container_state ='" + GlobalVars.PALLET_OPEN + "' AND bc.create_time BETWEEN '%s' AND '%s' ORDER BY bc.create_time DESC";
                        q = String.format(q, startDateStr, endDateStr);
                    } else if (startDatePicker.getDate() != null && endDatePicker.getDate() == null) {
                        q += " AND container_state ='" + GlobalVars.PALLET_OPEN + "' AND bc.create_time > '%s' ORDER BY bc.create_time DESC ";
                        q = String.format(q, startDateStr);
                    }
                    if (startDatePicker.getDate() == null && endDatePicker.getDate() != null) {
                        q += " AND container_state ='" + GlobalVars.PALLET_OPEN + "' AND bc.create_time < '%s' ORDER BY bc.create_time DESC";
                        q = String.format(q, endDateStr);
                    }
                } else if (UIHelper.listContains(selected, GlobalVars.PALLET_WAITING)) {
                    if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
                        q += " AND container_state ='" + GlobalVars.PALLET_WAITING + "' AND bc.write_time BETWEEN '%s' AND '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, startDateStr, endDateStr);
                    } else if (startDatePicker.getDate() != null && endDatePicker.getDate() == null) {
                        q += " AND bc.write_time > '%s' ORDER BY bc.write_time DESC ";
                        q = String.format(q, startDateStr);
                    }
                    if (startDatePicker.getDate() == null && endDatePicker.getDate() != null) {
                        q += " AND bc.shipped_time < '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, endDateStr);
                    }
                } else if (UIHelper.listContains(selected, GlobalVars.PALLET_CLOSED)) {
                    if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
                        q += " AND container_state ='" + GlobalVars.PALLET_CLOSED + "' AND bc.closed_time BETWEEN '%s' AND '%s' ORDER BY bc.closed_time DESC";
                        q = String.format(q, startDateStr, endDateStr);
                    } else if (startDatePicker.getDate() != null && endDatePicker.getDate() == null) {
                        q += " AND container_state ='" + GlobalVars.PALLET_CLOSED + "' AND bc.closed_time > '%s' ORDER BY bc.closed_time DESC ";
                        q = String.format(q, startDateStr);
                    }
                    if (startDatePicker.getDate() == null && endDatePicker.getDate() != null) {
                        q += " AND container_state ='" + GlobalVars.PALLET_CLOSED + "' AND bc.closed_time < '%s' ORDER BY bc.closed_time DESC";
                        q = String.format(q, endDateStr);
                    }
                } else if (UIHelper.listContains(selected, GlobalVars.PALLET_STORED)) {
                    if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
                        q += " AND bc.stored_time BETWEEN '%s' AND '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, startDateStr, endDateStr);
                    } else if (startDatePicker.getDate() != null && endDatePicker.getDate() == null) {
                        q += " AND bc.stored_time > '%s' ORDER BY bc.write_time DESC ";
                        q = String.format(q, startDateStr);
                    }
                    if (startDatePicker.getDate() == null && endDatePicker.getDate() != null) {
                        q += " AND bc.stored_time < '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, endDateStr);
                    }
                } else if (UIHelper.listContains(selected, GlobalVars.PALLET_SHIPPED)) {
                    if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
                        q += " AND bc.shipped_time BETWEEN '%s' AND '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, startDateStr, endDateStr);
                    } else if (startDatePicker.getDate() != null && endDatePicker.getDate() == null) {
                        q += " AND bc.shipped_time > '%s' ORDER BY bc.write_time DESC ";
                        q = String.format(q, startDateStr);
                    }
                    if (startDatePicker.getDate() == null && endDatePicker.getDate() != null) {
                        q += " AND bc.shipped_time < '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, endDateStr);
                    }

                } else if (UIHelper.listContains(selected, GlobalVars.PALLET_QUARANTAINE)) {
                    if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
                        q += " AND container_state ='" + GlobalVars.PALLET_QUARANTAINE + "' AND bc.write_time BETWEEN '%s' AND '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, startDateStr, endDateStr);
                    } else if (startDatePicker.getDate() != null && endDatePicker.getDate() == null) {
                        q += " AND bc.write_time > '%s' ORDER BY bc.write_time DESC ";
                        q = String.format(q, startDateStr);
                    }
                    if (startDatePicker.getDate() == null && endDatePicker.getDate() != null) {
                        q += " AND bc.shipped_time < '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, endDateStr);
                    }
                } else {
                    if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
                        q += " AND bc.write_time BETWEEN '%s' AND '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, startDateStr, endDateStr);
                    } else if (startDatePicker.getDate() != null && endDatePicker.getDate() == null) {
                        q += " AND bc.write_time > '%s' ORDER BY bc.write_time DESC ";
                        q = String.format(q, startDateStr);
                    }
                    if (startDatePicker.getDate() == null && endDatePicker.getDate() != null) {
                        q += " AND bc.write_time < '%s' ORDER BY bc.write_time DESC";
                        q = String.format(q, endDateStr);
                    }

                }

                SQLQuery query = Helper.sess.createSQLQuery(q);

                query.addScalar("segment", StandardBasicTypes.STRING)
                        .addScalar("workplace", StandardBasicTypes.STRING)
                        .addScalar("pack_number", StandardBasicTypes.STRING)
                        .addScalar("harness_part", StandardBasicTypes.STRING)
                        .addScalar("create_time", StandardBasicTypes.TIMESTAMP)
                        .addScalar("fifo_time", StandardBasicTypes.TIMESTAMP)
                        .addScalar("supplier_part_number", StandardBasicTypes.STRING)
                        .addScalar("pack_type", StandardBasicTypes.STRING)
                        .addScalar("pack_size", StandardBasicTypes.STRING)
                        .addScalar("qty_read", StandardBasicTypes.STRING)
                        .addScalar("std_time", StandardBasicTypes.DOUBLE)
                        .addScalar("total_std_time", StandardBasicTypes.DOUBLE)
                        .addScalar("index", StandardBasicTypes.STRING)
                        .addScalar("user", StandardBasicTypes.STRING)
                        .addScalar("state", StandardBasicTypes.STRING)
                        .addScalar("state_code", StandardBasicTypes.STRING)
                        .setParameterList("segments", segments)
                        .setParameterList("workplaces", workplaces);
//                if (selected.isEmpty()) {
//                    query.setParameterList("states", states);
//                }

                if (!harness_part_txt.getText().isEmpty()) {
                    query.setParameter("hp", "%" + harness_part_txt.getText() + "%");
                }

                resultList = query.list();
                nbreLigne.setText(resultList.size() + "");
                this.reload_container_table_data(resultList);
                Helper.sess.getTransaction().commit();

            } catch (HibernateException e) {
                if (Helper.sess.getTransaction() != null) {
                    Helper.sess.getTransaction().rollback();
                }
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Empty field", "Empty field Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_formKeyPressed

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        refresh();
    }//GEN-LAST:event_refresh_btnActionPerformed

    private void harness_part_txtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_harness_part_txtKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            refresh();
        }
    }//GEN-LAST:event_harness_part_txtKeyPressed

    private void export_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export_btnActionPerformed
        startTimeStr = timeDf.format(startTimeSpinner.getValue());
        endTimeStr = timeDf.format(endTimeSpinner.getValue());
        startDateStr = dateDf.format(startDatePicker.getDate()) + " " + startTimeStr;
        endDateStr = dateDf.format(endDatePicker.getDate()) + " " + endTimeStr;
        int total_produced = 0;
        //Create the excel workbook
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("PALLET_LIST");
        CreationHelper createHelper = wb.getCreationHelper();

        //######################################################################
        //##################### SHEET 1 : PILES DETAILS ########################
        //Initialiser les entête du fichier
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow((short) 0);

        row.createCell(0).setCellValue("SEGMENT");
        row.createCell(1).setCellValue("WORKPLACE");
        row.createCell(2).setCellValue("PAL NUM");
        row.createCell(3).setCellValue("PART NUMBER");
        row.createCell(4).setCellValue("CREATE TIME");
        row.createCell(5).setCellValue("FIFO TIME");
        row.createCell(6).setCellValue("LEONI PART NUM");
        row.createCell(7).setCellValue("PACK TYPE");
        row.createCell(8).setCellValue("PACK SIZE");
        row.createCell(9).setCellValue("QTY READ");
        row.createCell(10).setCellValue("STD TIME");
        row.createCell(11).setCellValue("TOTAL STD TIME");
        row.createCell(12).setCellValue("INDEX");
        row.createCell(13).setCellValue("USER");
        row.createCell(14).setCellValue("STATE");
        row.createCell(15).setCellValue("STATE CODE");

        short sheetPointer = 1;

        for (Object[] obj : this.resultList) {
            row = sheet.createRow(sheetPointer);
            row.createCell(0).setCellValue(String.valueOf(obj[0])); //SEGMENT
            row.createCell(1).setCellValue(String.valueOf(obj[1])); //WORKPLACE
            row.createCell(2).setCellValue(Integer.valueOf(obj[2].toString())); //PAL NUM
            row.createCell(3).setCellValue(String.valueOf(obj[3]));//PART NUMBER
            row.createCell(4).setCellValue(String.valueOf(obj[4]));//CREATE TIME
            row.createCell(5).setCellValue(String.valueOf(obj[5]));//UPDATE TIME
            row.createCell(6).setCellValue(String.valueOf(obj[6]));//LEONI PART NUM
            row.createCell(7).setCellValue(String.valueOf(obj[7]));//PACK TYPE
            row.createCell(8).setCellValue(Integer.valueOf(obj[8].toString()));//PACK SIZE
            row.createCell(9).setCellValue(Integer.valueOf(obj[9].toString()));//QTY READ
            row.createCell(10).setCellValue(Double.valueOf(obj[10].toString()));//STD TIME
            row.createCell(11).setCellValue(Double.valueOf(obj[11].toString()));//TOTAL STD TIME
            row.createCell(12).setCellValue(String.valueOf(obj[12].toString()));//INDEX
            row.createCell(14).setCellValue(String.valueOf(obj[13]));//USER
            row.createCell(15).setCellValue(String.valueOf(obj[14]));//STATE
            row.createCell(16).setCellValue(Integer.valueOf(obj[15].toString()));//STATE CODE           

            total_produced = total_produced + Integer.valueOf(String.valueOf(obj[9]));

            sheetPointer++;
        }

        //Total produced line
        row = sheet.createRow(sheetPointer++);
        row.createCell(0).setCellValue("TOTAL PRODUCED QTY :");
        row.createCell(1).setCellValue(total_produced);

        //Past the workbook to the file chooser
        new JDialogExcelFileChooser((Frame) super.getParent(), true, wb).setVisible(true);
    }//GEN-LAST:event_export_btnActionPerformed

    private void segment_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_segment_filterItemStateChanged
        System.out.println("Selected Segment " + String.valueOf(segment_filter.getSelectedItem()));
        if ("ALL".equals(String.valueOf(segment_filter.getSelectedItem()).trim())) {
            this.workplace_filter.setSelectedIndex(0);
            this.workplace_filter.setEnabled(false);
        } else {
            this.workplace_filter.removeAllItems();
            this.workplace_filter.addItem("ALL");
            this.workplace_filter.setEnabled(true);
            this.setWorkplaceBySegment(String.valueOf(segment_filter.getSelectedItem()));
        }

        refresh();
    }//GEN-LAST:event_segment_filterItemStateChanged

    private void segment_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segment_filterActionPerformed

    }//GEN-LAST:event_segment_filterActionPerformed

    private void workplace_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_workplace_filterItemStateChanged
        refresh();
    }//GEN-LAST:event_workplace_filterItemStateChanged

    private void workplace_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workplace_filterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_workplace_filterActionPerformed

    private void lafm_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lafm_btnActionPerformed
        if (checkValidFields()) {

            startTimeStr = timeDf.format(startTimeSpinner.getValue());
            endTimeStr = timeDf.format(endTimeSpinner.getValue());
            startDateStr = dateDf.format(startDatePicker.getDate()) + " " + startTimeStr;
            endDateStr = dateDf.format(endDatePicker.getDate()) + " " + endTimeStr;

            //################# Harness Data ####################            
            Helper.startSession();
            try {
                String q = " SELECT "
                        + " bc.supplier_part_number AS supplier_part_number, "
                        + " SUM(bc.qty_expected) AS qty_expected "
                        + " FROM base_container bc "
                        + " WHERE bc.container_state = '" + GlobalVars.PALLET_STORED + "' ";

                //Add harness part filter                
                if (!harness_part_txt.getText().isEmpty()) {
                    q += " AND harness_part LIKE :hp";
                }

                System.out.println("Query" + q);

                try {
                    Date startDate = dateTimeDf.parse(startDateStr);
                    Date endDate = dateTimeDf.parse(endDateStr);
                    System.out.println(startDate.before(endDate));
                } catch (Exception ex) {
                    Logger.getLogger(PACKAGING_UI0011_ProdStatistics.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
                    q += " AND bc.stored_time BETWEEN '%s' AND '%s'";
                    q += " GROUP BY supplier_part_number, qty_expected";
                    q = String.format(q, startDateStr, endDateStr);
                } else if (startDatePicker.getDate() != null && endDatePicker.getDate() == null) {
                    q += " AND bc.stored_time > '%s'";
                    q += " GROUP BY supplier_part_number, qty_expected";
                    q = String.format(q, startDateStr);
                }
                if (startDatePicker.getDate() == null && endDatePicker.getDate() != null) {
                    q += " AND bc.stored_time < '%s'";
                    q += " GROUP BY supplier_part_number, qty_expected";
                    q = String.format(q, endDateStr);
                }

                SQLQuery query = Helper.sess.createSQLQuery(q);

                query.addScalar("supplier_part_number", StandardBasicTypes.STRING)
                        .addScalar("qty_expected", StandardBasicTypes.INTEGER);

                if (!harness_part_txt.getText().isEmpty()) {
                    query.setParameter("hp", "%" + harness_part_txt.getText() + "%");
                }

                List<Object[]> lafmResult = query.list();
                Workbook wb = new HSSFWorkbook();
                Sheet sheet = wb.createSheet("LAFM");
                CreationHelper createHelper = wb.getCreationHelper();

                //Create the excel workbook
                Row row = sheet.createRow((short) 0);

                short sheetPointer = 0;

                row = sheet.createRow(sheetPointer++);
                row.createCell(0).setCellValue("Du : " + startDateStr);
                row.createCell(1).setCellValue("Au : " + endDateStr);

                for (Object[] obj : lafmResult) {
                    row = sheet.createRow(sheetPointer);
                    row.createCell(0).setCellValue(GlobalVars.HARN_PART_PREFIX + String.valueOf(obj[0])); //LPN
                    row.createCell(1).setCellValue(GlobalVars.QUANTITY_PREFIX + String.valueOf(obj[1].toString()));//PACK SIZE
                    row.createCell(2).setCellValue(String.valueOf("LO" + GlobalVars.APP_PROP.getProperty("WH_FINISH_GOODS")));//WAREHOUSE
                    sheetPointer++;
                }

                Helper.sess.getTransaction().commit();
                //Past the workbook to the file chooser
                new JDialogExcelFileChooser((Frame) super.getParent(), true, wb).setVisible(true);
            } catch (HibernateException e) {
                if (Helper.sess.getTransaction() != null) {
                    Helper.sess.getTransaction().rollback();
                }
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Empty field", "Empty field Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_lafm_btnActionPerformed

    private void startDatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDatePickerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startDatePickerActionPerformed

    private void harness_part_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harness_part_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_harness_part_txtActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXDatePicker endDatePicker;
    private javax.swing.JSpinner endTimeSpinner;
    private javax.swing.JButton export_btn;
    private javax.swing.JTextField harness_part_txt;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel jpanel_state;
    private javax.swing.JButton lafm_btn;
    private javax.swing.JLabel nbreLigne;
    private javax.swing.JButton refresh_btn;
    private javax.swing.JTable searchResult_table;
    private javax.swing.JComboBox segment_filter;
    private org.jdesktop.swingx.JXDatePicker startDatePicker;
    private javax.swing.JSpinner startTimeSpinner;
    private javax.swing.JTextField total_declared_lbl;
    private javax.swing.JComboBox workplace_filter;
    // End of variables declaration//GEN-END:variables

    void clearSearchBox(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
