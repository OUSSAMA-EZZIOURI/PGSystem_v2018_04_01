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
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
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
 * @author Administrator
 */
public class PACKAGING_UI0019_EfficiencyCalculation extends javax.swing.JDialog {

    Vector<String> declared_result_table_header = new Vector<String>();
    Vector declared_result_table_data = new Vector();

    Vector<String> dropped_result_table_header = new Vector<String>();
    Vector dropped_result_table_data = new Vector();

    private List<Object[]> declaredResultList;

    List<Object> segments = new ArrayList<Object>();
    List<Object> workplaces = new ArrayList<Object>();

    SimpleDateFormat timeDf = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateDf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeDf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String startTimeStr = "";
    String endTimeStr = "";
    String startDateStr = null;
    String endDateStr = null;

    int operators = 0;
    Double produced_hours = 0.00;
    Double worked_hours = 7.50;
    Double posted_hours = 0.00;
    Double efficiency = 0.00;

    ButtonGroup radioGroup = new ButtonGroup();

    DecimalFormat decimForm = new DecimalFormat("0.00");
    NumberFormat nf = NumberFormat.getInstance(); 


    /**
     * Creates new form UI0011_ProdStatistics_
     */
    public PACKAGING_UI0019_EfficiencyCalculation(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initTimeSpinners();
        //initFamillyFilter();
        initSegmentFilter();
        this.workplace_filter.setEnabled(false);
        radioGroup.add(radio_all_harness);
        radioGroup.add(radio_filled_ucs);
        operators_txt.setValue(1);
        //initWorkplaceFilter();
        this.reset_tables_content();
        this.refresh();
        Helper.centerJDialog(this);
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
            Logger.getLogger(PACKAGING_UI0019_EfficiencyCalculation.class.getName()).log(Level.SEVERE, null, ex);
        }

        //################# End Time Spinner ######################
        endTimeSpinner.setModel(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endTimeEditor);
        try {
            endTimeSpinner.setValue(timeFormat.parse(endTime));
        } catch (ParseException ex) {
            Logger.getLogger(PACKAGING_UI0019_EfficiencyCalculation.class.getName()).log(Level.SEVERE, null, ex);
        }

        datePicker.setDate(new Date());

    }

    public void reset_tables_content() {
        //############ Reset declared table result
        this.load_declared_result_table_header();
        declared_result_table_data = new Vector();
        DefaultTableModel declaredDataModel = new DefaultTableModel(declared_result_table_data, declared_result_table_header);
        declared_result_table.setModel(declaredDataModel);
    }

    /**
     *
     */
    public void load_declared_result_table_header() {
        declared_result_table_header.clear();
        declared_result_table_header.add("Segment");
        declared_result_table_header.add("Workplace");
        declared_result_table_header.add("Part number");
        declared_result_table_header.add("Std Time (Hours)");
        declared_result_table_header.add("Produced qty");
        declared_result_table_header.add("Produced hours");
        declared_result_table.setModel(new DefaultTableModel(declared_result_table_data, declared_result_table_header));
        declared_result_table.setAutoCreateRowSorter(true);
    }

    public void disableEditingTables() {
        for (int c = 0; c < declared_result_table.getColumnCount(); c++) {
            // remove editor   
            Class<?> col_class1 = declared_result_table.getColumnClass(c);
            declared_result_table.setDefaultEditor(col_class1, null);
        }
    }

    @SuppressWarnings("empty-statement")
    public void reload_declared_result_table_data(List<Object[]> resultList) {

        int total_produced_qty = 0;        
        //Set declared hours labels values
        produced_hours = 0.00;
        this.produced_hours_txt.setText(""+produced_hours);
        
        for (Object[] obj : resultList) {
            Vector<Object> oneRow = new Vector<Object>();
            oneRow.add(String.valueOf(obj[0])); //segment
            oneRow.add(String.valueOf(obj[1])); //workplace            
            if(String.valueOf(obj[2]).startsWith(GlobalVars.HARN_PART_PREFIX))
                oneRow.add(String.valueOf(obj[2])); //harness_part
            else
                oneRow.add(GlobalVars.HARN_PART_PREFIX+String.valueOf(obj[2])); //harness_part
            oneRow.add(String.valueOf(obj[3])); //std_time            
            oneRow.add(String.valueOf(obj[4])); //produced_qty;
            oneRow.add(decimForm.format(Double.valueOf(String.valueOf(obj[5])))); //produced_hours
            total_produced_qty = total_produced_qty + Integer.valueOf(String.valueOf(obj[4]));
            produced_hours = produced_hours + Double.valueOf(obj[5].toString());
            declared_result_table_data.add(oneRow);
        }
        declared_result_table.setModel(new DefaultTableModel(declared_result_table_data, declared_result_table_header));
        declared_result_table.setFont(new Font(String.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONT")), Font.BOLD, Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONTSIZE"))));
        declared_result_table.setRowHeight(Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_ROW_HEIGHT")));

        //Set declared qty labels values
        this.produced_qty_txt.setText(String.valueOf(total_produced_qty));

        //Set declared hours labels values
        this.produced_hours_txt.setText(""+produced_hours);

    }

    private boolean checkValidFields() {
        if (startTimeSpinner.getValue() != ""
                && endTimeSpinner.getValue() != ""
                && datePicker.getDate() != null
                && operators_txt.getValue() != ""
                && Integer.valueOf(operators_txt.getValue().toString()) != 0) {
            return true;
        } else {
            return false;
        }
    }

    private void refresh() {

        if (checkValidFields()) {
            String query_str = "";
            segments.clear();
            workplaces.clear();
            startTimeStr = timeDf.format(startTimeSpinner.getValue());
            endTimeStr = timeDf.format(endTimeSpinner.getValue());
            startDateStr = dateDf.format(datePicker.getDate()) + " " + startTimeStr;
            endDateStr = dateDf.format(datePicker.getDate()) + " " + endTimeStr;                       

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

            try {
                //Clear all tables
                this.reset_tables_content();                                
                
                //################# Declared Harness Data ####################        
                Helper.startSession();
                
                if (radio_filled_ucs.isSelected()) { // UCS Complet
                    //Request 1
                    query_str = "(SELECT bc.segment AS segment,"
                            + " bc.workplace AS workplace,"
                            + " bc.harness_part AS harness_part,"
                            + " bc.std_time AS std_time,"
                            + " SUM(bc.qty_read) AS produced_qty,"
                            + " bc.std_time*SUM(bc.qty_read) AS produced_hours"
                            + " FROM base_container bc "
                            + " WHERE "
                            + " (bc.stored_time BETWEEN '%s' AND '%s')"                            
                            + " AND bc.segment IN (:segments) "
                            + " AND bc.workplace IN (:workplaces) ";

                    query_str = String.format(query_str, startDateStr, endDateStr);
                    query_str += "GROUP BY bc.harness_part, bc.segment, bc.workplace, bc.std_time "
                            + "ORDER BY bc.harness_part ASC, bc.segment ASC, bc.workplace ASC)";
                    
                } else {
                    //Request 2
                    query_str = " SELECT  bh.segment AS segment,"
                            + " bh.workplace AS workplace,"
                            + " bh.harness_part AS harness_part,"
                            + " bh.std_time AS std_time,"
                            + " COUNT(bh.harness_part) AS produced_qty,"
                            + " SUM(bh.std_time) AS produced_hours "
                            + " FROM base_harness bh, base_container bc "
                            + " WHERE bc.id = bh.container_id "
                            + " AND bh.create_time BETWEEN '%s' AND '%s'"
                            + " AND bh.segment IN (:segments) "
                            + " AND bh.workplace IN (:workplaces) ";

                    query_str = String.format(query_str, startDateStr, endDateStr);
                    query_str += " GROUP BY bh.segment, bh.workplace, bh.harness_part, bh.std_time  "
                            + " ORDER BY bh.segment ASC,bh.workplace ASC, bh.harness_part ASC";
                }
               
                query_str = String.format(query_str, startDateStr, endDateStr);                                

                SQLQuery query = Helper.sess.createSQLQuery(query_str);

                query.addScalar("segment", StandardBasicTypes.STRING)
                        .addScalar("workplace", StandardBasicTypes.STRING)
                        .addScalar("harness_part", StandardBasicTypes.STRING)
                        .addScalar("std_time", StandardBasicTypes.DOUBLE)
                        .addScalar("produced_qty", StandardBasicTypes.INTEGER)
                        .addScalar("produced_hours", StandardBasicTypes.DOUBLE)
                        .setParameterList("segments", segments)
                        .setParameterList("workplaces", workplaces);

                this.declaredResultList = query.list();

                Helper.sess.getTransaction().commit();

                this.reload_declared_result_table_data(declaredResultList);

                this.disableEditingTables();

                //Calculate efficiency                
                operators = Integer.valueOf(operators_txt.getValue().toString());
                worked_hours = Double.valueOf(worked_hours_txt.getText().toString().replace(",", "."));
                posted_hours = operators * worked_hours;
                //Efficiency                    
                efficiency = (produced_hours / posted_hours) * 100;
                
                this.posted_hours_txt.setText(decimForm.format(posted_hours));
                this.posted_hours_txt2.setText(decimForm.format(posted_hours));
                this.efficiency_txt.setText(decimForm.format(efficiency));
                this.produced_hours_txt.setText(decimForm.format(produced_hours));

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
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        north_panel = new javax.swing.JPanel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        refresh_btn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        startTimeSpinner = new javax.swing.JSpinner();
        endTimeSpinner = new javax.swing.JSpinner();
        result_table_scroll = new javax.swing.JScrollPane();
        declared_result_table = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        export_btn = new javax.swing.JButton();
        radio_filled_ucs = new javax.swing.JRadioButton();
        radio_all_harness = new javax.swing.JRadioButton();
        workplace_filter = new javax.swing.JComboBox();
        segment_filter = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        produced_hours_txt = new javax.swing.JTextField();
        produced_qty_txt = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        worked_hours_txt = new javax.swing.JTextField();
        efficiency_txt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        posted_hours_txt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        operators_txt = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        posted_hours_txt2 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

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

        refresh_btn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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

        startTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        startTimeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                startTimeSpinnerStateChanged(evt);
            }
        });
        startTimeSpinner.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                startTimeSpinnerFocusLost(evt);
            }
        });

        endTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        endTimeSpinner.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                endTimeSpinnerFocusLost(evt);
            }
        });

        declared_result_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        result_table_scroll.setViewportView(declared_result_table);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Quantités déclarées par segment et par ligne");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Calcul de l'Efficience");

        export_btn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        export_btn.setText("Exporter en Excel...");
        export_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                export_btnActionPerformed(evt);
            }
        });

        radio_filled_ucs.setForeground(new java.awt.Color(255, 255, 255));
        radio_filled_ucs.setText("Total palettes STORED");
        radio_filled_ucs.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radio_filled_ucsItemStateChanged(evt);
            }
        });

        radio_all_harness.setForeground(new java.awt.Color(255, 255, 255));
        radio_all_harness.setSelected(true);
        radio_all_harness.setText("Total par pièces scannées");
        radio_all_harness.setToolTipText("<p>Le total des pièces scannées au niveau poste packaging. Prend en considération les 3 états de la palette :</p><br/>\n<p>- Ouverte (OPEN) \t: Palette ouverte au niveau poste packaging, UCS incomplet.</p><br/>\n<p>- Fermé    (CLOSED)\t: Palette fermée mais pas encore entrée au magasin produit fini., UCS complet.</p><br/>\n<p>- Stockée (STORED)\t: Palette stockée au niveau magasin produit fini, UCS complet.</p><br/>");
        radio_all_harness.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radio_all_harnessItemStateChanged(evt);
            }
        });

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
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Segment");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Σ Pièces");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Σ Heures produites");

        produced_hours_txt.setEditable(false);
        produced_hours_txt.setBackground(new java.awt.Color(153, 255, 255));
        produced_hours_txt.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        produced_hours_txt.setText("0.00");
        produced_hours_txt.setMinimumSize(new java.awt.Dimension(15, 29));
        produced_hours_txt.setPreferredSize(new java.awt.Dimension(15, 29));

        produced_qty_txt.setEditable(false);
        produced_qty_txt.setBackground(new java.awt.Color(153, 255, 255));
        produced_qty_txt.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        produced_qty_txt.setText("0");
        produced_qty_txt.setMinimumSize(new java.awt.Dimension(15, 29));
        produced_qty_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                produced_qty_txtActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Workplace");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Effectif");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Heures travailées");

        worked_hours_txt.setBackground(new java.awt.Color(153, 255, 255));
        worked_hours_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        worked_hours_txt.setText("7.5");

        efficiency_txt.setEditable(false);
        efficiency_txt.setBackground(new java.awt.Color(102, 255, 102));
        efficiency_txt.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        efficiency_txt.setText("0.00");
        efficiency_txt.setMinimumSize(new java.awt.Dimension(15, 29));
        efficiency_txt.setPreferredSize(new java.awt.Dimension(15, 29));
        efficiency_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                efficiency_txtActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 51));
        jLabel17.setText("Efficience %");

        posted_hours_txt.setEditable(false);
        posted_hours_txt.setBackground(new java.awt.Color(153, 255, 255));
        posted_hours_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        posted_hours_txt.setText("0.00");
        posted_hours_txt.setMinimumSize(new java.awt.Dimension(15, 29));
        posted_hours_txt.setPreferredSize(new java.awt.Dimension(15, 29));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Heures postées");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Au");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Jour");

        operators_txt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        operators_txt.setVerifyInputWhenFocusTarget(false);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("=");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("x");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("/");

        posted_hours_txt2.setEditable(false);
        posted_hours_txt2.setBackground(new java.awt.Color(153, 255, 255));
        posted_hours_txt2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        posted_hours_txt2.setText("0.00");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Heures postées");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("=");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("=");

        javax.swing.GroupLayout north_panelLayout = new javax.swing.GroupLayout(north_panel);
        north_panel.setLayout(north_panelLayout);
        north_panelLayout.setHorizontalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(115, 115, 115)
                        .addComponent(jLabel22))
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(segment_filter, 0, 168, Short.MAX_VALUE)
                                .addComponent(produced_qty_txt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(workplace_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addComponent(produced_hours_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addComponent(radio_all_harness, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(radio_filled_ucs, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addGroup(north_panelLayout.createSequentialGroup()
                                        .addComponent(posted_hours_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel6)))
                                .addGap(10, 10, 10)
                                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(efficiency_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(75, 75, 75)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(export_btn))
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addGap(230, 230, 230)
                                .addComponent(jLabel10))))
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(199, 199, 199)
                                .addComponent(jLabel1)
                                .addGap(73, 73, 73)
                                .addComponent(jLabel3))
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addComponent(worked_hours_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)))
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel13))
                            .addGroup(north_panelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(operators_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12)))
                        .addGap(12, 12, 12)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(posted_hours_txt2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, north_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(result_table_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 1067, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        north_panelLayout.setVerticalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)))
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(operators_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(posted_hours_txt2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(worked_hours_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(radio_all_harness)
                    .addComponent(workplace_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(segment_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radio_filled_ucs)
                    .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(export_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(jLabel15)
                        .addComponent(jLabel17))
                    .addComponent(jLabel18))
                .addGap(3, 3, 3)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(produced_qty_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(posted_hours_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(efficiency_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(produced_hours_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(result_table_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(333, 333, 333))
            .addGroup(north_panelLayout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1004, 1004, 1004))
            .addGroup(north_panelLayout.createSequentialGroup()
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.CENTER, north_panelLayout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22)))
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(north_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(north_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

    private void segment_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segment_filterActionPerformed

    }//GEN-LAST:event_segment_filterActionPerformed

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

    private void workplace_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workplace_filterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_workplace_filterActionPerformed

    private void workplace_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_workplace_filterItemStateChanged
        refresh();
    }//GEN-LAST:event_workplace_filterItemStateChanged

    private void radio_all_harnessItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radio_all_harnessItemStateChanged
        refresh();
    }//GEN-LAST:event_radio_all_harnessItemStateChanged

    private void radio_filled_ucsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radio_filled_ucsItemStateChanged
        refresh();
    }//GEN-LAST:event_radio_filled_ucsItemStateChanged

    private void export_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export_btnActionPerformed
        try {
            startTimeStr = timeDf.format(startTimeSpinner.getValue());
            endTimeStr = timeDf.format(endTimeSpinner.getValue());
            startDateStr = dateDf.format(datePicker.getDate()) + " " + startTimeStr;
            endDateStr = dateDf.format(datePicker.getDate()) + " " + endTimeStr;
            
            //Create the excel workbook
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("PROD_EFFICIENCY");
            CreationHelper createHelper = wb.getCreationHelper();
            int total_qty = 0;
            
            //######################################################################
            //##################### SHEET 1 : PILES DETAILS ########################
            //Initialiser les entête du fichier
            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow((short) 0);
            
            row.createCell(0).setCellValue("SEGMENT");
            row.createCell(1).setCellValue("WORKPLACE");
            row.createCell(2).setCellValue("PART NUMBER");
            row.createCell(3).setCellValue("STD TIME");
            row.createCell(4).setCellValue("PRODUCED QTY");
            row.createCell(5).setCellValue("PRODUCED HOURS");
            
            short sheetPointer = 1;
            
            for (Object[] obj : this.declaredResultList) {
                row = sheet.createRow(sheetPointer);
                row.createCell(0).setCellValue(String.valueOf(obj[0])); //SEGMENT
                row.createCell(1).setCellValue(String.valueOf(obj[1])); //WORKPLACE
                if(String.valueOf(obj[2]).startsWith(GlobalVars.HARN_PART_PREFIX))
                    row.createCell(2).setCellValue(String.valueOf(obj[2]).substring(1));//PART NUMBER
                else 
                    row.createCell(2).setCellValue(String.valueOf(obj[2]));//PART NUMBER
                row.createCell(3).setCellValue(Double.valueOf(obj[3].toString()));//STD TIME
                row.createCell(4).setCellValue(Double.valueOf(obj[4].toString()));//PRODUCED QTY
                row.createCell(5).setCellValue(Double.valueOf(obj[5].toString()));//PRODUCED HOURS
                
                total_qty = total_qty + Integer.valueOf(String.valueOf(obj[4]));
                
                sheetPointer++;
            }
            
            //Segment
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("SEGMENT : ");
            row.createCell(1).setCellValue(String.valueOf(segment_filter.getSelectedItem()));
            
            //Workplace
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("WORKPLACE : ");
            row.createCell(1).setCellValue(String.valueOf(workplace_filter.getSelectedItem()));
            
            //Start date
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("FROM : ");
            row.createCell(1).setCellValue(String.valueOf(startDateStr));
            
            //End date
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("TO : ");
            row.createCell(1).setCellValue(String.valueOf(endDateStr));
            
            //Worked Hours
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("WORKED HOURS : ");
            row.createCell(1).setCellValue(nf.parse(worked_hours_txt.getText()).doubleValue());
            
            //Nbre Operators
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("OPERATORS : ");
            row.createCell(1).setCellValue(String.valueOf(operators_txt.getValue()));
            
            //Posted Hours
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("POSTED HOURS : ");
            row.createCell(1).setCellValue(nf.parse(posted_hours_txt.getText()).doubleValue());
            
            //Total produced qty
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("TOTAL PRODUCED QTY :");
            row.createCell(1).setCellValue(total_qty);
            
            //Total produced hours
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("TOTAL PRODUCED HOURS :");
            row.createCell(1).setCellValue(nf.parse(produced_hours_txt.getText()).doubleValue());
            
            //Total produced hours
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("EFFICIENCY :");
            row.createCell(1).setCellValue(nf.parse(efficiency_txt.getText()).doubleValue());
            
            //Past the workbook to the file chooser
            new JDialogExcelFileChooser((Frame) super.getParent(), true, wb).setVisible(true);
        } catch (ParseException ex) {
            Logger.getLogger(PACKAGING_UI0019_EfficiencyCalculation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_export_btnActionPerformed

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        refresh();
    }//GEN-LAST:event_refresh_btnActionPerformed

    private void efficiency_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_efficiency_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_efficiency_txtActionPerformed

    private void produced_qty_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_produced_qty_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_produced_qty_txtActionPerformed

    private void startTimeSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_startTimeSpinnerStateChanged
        
    }//GEN-LAST:event_startTimeSpinnerStateChanged

    private void startTimeSpinnerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_startTimeSpinnerFocusLost
        //updateHours();
    }//GEN-LAST:event_startTimeSpinnerFocusLost

    private void endTimeSpinnerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_endTimeSpinnerFocusLost
        //updateHours();
    }//GEN-LAST:event_endTimeSpinnerFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JTable declared_result_table;
    private javax.swing.JTextField efficiency_txt;
    private javax.swing.JSpinner endTimeSpinner;
    private javax.swing.JButton export_btn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel north_panel;
    private javax.swing.JSpinner operators_txt;
    private javax.swing.JTextField posted_hours_txt;
    private javax.swing.JTextField posted_hours_txt2;
    private javax.swing.JTextField produced_hours_txt;
    private javax.swing.JTextField produced_qty_txt;
    private javax.swing.JRadioButton radio_all_harness;
    private javax.swing.JRadioButton radio_filled_ucs;
    private javax.swing.JButton refresh_btn;
    private javax.swing.JScrollPane result_table_scroll;
    private javax.swing.JComboBox segment_filter;
    private javax.swing.JSpinner startTimeSpinner;
    private javax.swing.JTextField worked_hours_txt;
    private javax.swing.JComboBox workplace_filter;
    // End of variables declaration//GEN-END:variables
}
