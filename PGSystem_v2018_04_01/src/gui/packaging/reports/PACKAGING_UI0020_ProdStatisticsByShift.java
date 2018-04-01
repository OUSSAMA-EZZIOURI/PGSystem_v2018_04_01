/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.reports;

import entity.ConfigSegment;
import entity.ConfigWorkplace;
import helper.ComboItem;
import helper.Helper;
import helper.JDialogExcelFileChooser;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class PACKAGING_UI0020_ProdStatisticsByShift extends javax.swing.JDialog {

    Vector<String> declared_result_table_header = new Vector<String>();
    Vector declared_result_table_data = new Vector();

    private List<Object[]> dataResultList;

    List<Object> segments = new ArrayList<Object>();
    List<Object> workplaces = new ArrayList<Object>();

    SimpleDateFormat timeDf = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateDf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeDf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    String Eq1startTime = "06:01";
    String Eq1endTime = "14:00";
    String Eq2startTime = "14:01";
    String Eq2endTime = "22:00";
    String Eq3startTime = "22:01";
    String Eq3endTime = "06:00";

    ButtonGroup radioGroup = new ButtonGroup();

    /**
     * Creates new form UI0011_ProdStatistics_
     */
    public PACKAGING_UI0020_ProdStatisticsByShift(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initTab1Components();
        initTab2Components();
        Helper.centerJDialog(this);
    }

    private void initTab1Components() {
        initTimeSpinners();
        initSegmentFilter();
        this.workplace_filter.setEnabled(false);
    }

    private void initTab2Components() {
        week_txt.setText("" + Calendar.WEEK_OF_YEAR);
        year_txt.setText(new SimpleDateFormat("yyyy").format(new Date()).toString());
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

    private Date nextDay(Date dt) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    private void initTimeSpinners() {

        //################# Start Time Spinner ####################
        Eq1startTimeSpinner.setModel(new SpinnerDateModel());
        Eq2startTimeSpinner.setModel(new SpinnerDateModel());
        Eq3startTimeSpinner.setModel(new SpinnerDateModel());
        JSpinner.DateEditor Eq1startTimeEditor = new JSpinner.DateEditor(Eq1startTimeSpinner, "HH:mm");
        JSpinner.DateEditor Eq2startTimeEditor = new JSpinner.DateEditor(Eq2startTimeSpinner, "HH:mm");
        JSpinner.DateEditor Eq3startTimeEditor = new JSpinner.DateEditor(Eq3startTimeSpinner, "HH:mm");
        Eq1endTimeSpinner.setModel(new SpinnerDateModel());
        Eq2endTimeSpinner.setModel(new SpinnerDateModel());
        Eq3endTimeSpinner.setModel(new SpinnerDateModel());
        JSpinner.DateEditor Eq1endTimeEditor = new JSpinner.DateEditor(Eq1endTimeSpinner, "HH:mm");
        JSpinner.DateEditor Eq2endTimeEditor = new JSpinner.DateEditor(Eq2endTimeSpinner, "HH:mm");
        JSpinner.DateEditor Eq3endTimeEditor = new JSpinner.DateEditor(Eq3endTimeSpinner, "HH:mm");
        Eq1startTimeSpinner.setEditor(Eq1startTimeEditor);
        Eq2startTimeSpinner.setEditor(Eq2startTimeEditor);
        Eq3startTimeSpinner.setEditor(Eq3startTimeEditor);
        Eq1endTimeSpinner.setEditor(Eq1endTimeEditor);
        Eq2endTimeSpinner.setEditor(Eq2endTimeEditor);
        Eq3endTimeSpinner.setEditor(Eq3endTimeEditor);
        try {
            Eq1startTimeSpinner.setValue(timeDf.parse(Eq1startTime));
            Eq2startTimeSpinner.setValue(timeDf.parse(Eq2startTime));
            Eq3startTimeSpinner.setValue(timeDf.parse(Eq3startTime));
            Eq1endTimeSpinner.setValue(timeDf.parse(Eq1endTime));
            Eq2endTimeSpinner.setValue(timeDf.parse(Eq2endTime));
            Eq3endTimeSpinner.setValue(timeDf.parse(Eq3endTime));
        } catch (ParseException ex) {
            Logger.getLogger(PACKAGING_UI0020_ProdStatisticsByShift.class.getName()).log(Level.SEVERE, null, ex);
        }

        datePicker.setDate(new Date());

    }

    private boolean checkValidFields() {
        if (Eq1startTimeSpinner.getValue() != ""
                && Eq1endTimeSpinner.getValue() != ""
                && Eq2startTimeSpinner.getValue() != ""
                && Eq2endTimeSpinner.getValue() != ""
                && Eq3startTimeSpinner.getValue() != ""
                && Eq3endTimeSpinner.getValue() != ""
                && datePicker.getDate() != null) {
            return true;
        } else {
            return false;
        }
    }

    private List<Object[]> executeDayQuery(
            List<Object> segments, List<Object> workplaces,
            String Eq1startTime, String Eq1endTime,
            String Eq2startTime, String Eq2endTime,
            String Eq3startTime, String Eq3endTime) {

        //######################################################################
        //#################   PREPARING THE SQL REQUEST  #######################
        try {
            //Clear all tables
            //################# Declared Harness Data ####################        
            Helper.startSession();

            String query_str_1 = "(SELECT 'Matin' as shift,'%s' As start_time, '%s' as end_time, "
                    + "bh.segment AS segment, "
                    + "bh.workplace AS workplace, "
                    + "bh.harness_part AS harness_part, "
                    + "bh.std_time AS std_time, "
                    + "COUNT(bh.harness_part) AS produced_qty, "
                    + "SUM(bh.std_time) AS produced_hours "
                    + "FROM base_harness bh, base_container bc "
                    + "WHERE bc.id = bh.container_id "
                    + "AND bh.create_time BETWEEN '%s' AND '%s' "
                    + "AND bh.segment IN (:segments) "
                    + "AND bh.workplace IN (:workplaces) "
                    + "GROUP BY bh.segment, bh.workplace, bh.harness_part, bh.std_time)";

            query_str_1 = String.format(query_str_1, Eq1startTime, Eq1endTime, Eq1startTime, Eq1endTime);

            String query_str_2 = "(SELECT 'Soir' as shift,'%s' As start_time, '%s' as end_time, "
                    + "bh.segment AS segment, "
                    + "bh.workplace AS workplace, "
                    + "bh.harness_part AS harness_part, "
                    + "bh.std_time AS std_time, "
                    + "COUNT(bh.harness_part) AS produced_qty, "
                    + "SUM(bh.std_time) AS produced_hours "
                    + "FROM base_harness bh, base_container bc "
                    + "WHERE bc.id = bh.container_id "
                    + "AND bh.create_time BETWEEN '%s' AND '%s' "
                    + "AND bh.segment IN (:segments) "
                    + "AND bh.workplace IN (:workplaces) "
                    + "GROUP BY bh.segment, bh.workplace, bh.harness_part, bh.std_time)";

            query_str_2 = String.format(query_str_2, Eq2startTime, Eq2endTime, Eq2startTime, Eq2endTime);

            String query_str_3 = "(SELECT 'Nuit' as shift,'%s' As start_time, '%s' as end_time, "
                    + "bh.segment AS segment, "
                    + "bh.workplace AS workplace, "
                    + "bh.harness_part AS harness_part, "
                    + "bh.std_time AS std_time, "
                    + "COUNT(bh.harness_part) AS produced_qty, "
                    + "SUM(bh.std_time) AS produced_hours "
                    + "FROM base_harness bh, base_container bc "
                    + "WHERE bc.id = bh.container_id "
                    + "AND bh.create_time BETWEEN '%s' AND '%s' "
                    + "AND bh.segment IN (:segments) "
                    + "AND bh.workplace IN (:workplaces)"
                    + "GROUP BY bh.segment, bh.workplace, bh.harness_part, bh.std_time) ";

            query_str_3 = String.format(query_str_3, Eq3startTime, Eq3endTime, Eq3startTime, Eq3endTime);

            String query_str = "SELECT * FROM ("
                    + query_str_1 + " UNION "
                    + query_str_2 + " UNION "
                    + query_str_3
                    + ") results ORDER BY start_time ASC, segment ASC, workplace ASC, harness_part ASC";

            SQLQuery query = Helper.sess.createSQLQuery(query_str);

            query.addScalar("shift", StandardBasicTypes.STRING)
                    .addScalar("start_time", StandardBasicTypes.STRING)
                    .addScalar("end_time", StandardBasicTypes.STRING)
                    .addScalar("segment", StandardBasicTypes.STRING)
                    .addScalar("workplace", StandardBasicTypes.STRING)
                    .addScalar("harness_part", StandardBasicTypes.STRING)
                    .addScalar("std_time", StandardBasicTypes.DOUBLE)
                    .addScalar("produced_qty", StandardBasicTypes.INTEGER)
                    .addScalar("produced_hours", StandardBasicTypes.DOUBLE)
                    .setParameterList("segments", segments)
                    .setParameterList("workplaces", workplaces);

            this.dataResultList = query.list();

            Helper.sess.getTransaction().commit();

            return this.dataResultList;

        } catch (HibernateException e) {
            if (Helper.sess.getTransaction() != null) {
                Helper.sess.getTransaction().rollback();
            }
        }

        return this.dataResultList;
    }

    private List<Object[]> executeWeekQuery(String week, String year) {

        //######################################################################
        //#################   PREPARING THE SQL REQUEST  #######################
        try {
            //Clear all tables
            //################# Declared Harness Data ####################        
            Helper.startSession();

            String query_str_1 = "(SELECT "
                    + year + " as year, " + week + " as week, 'Matin' as SHIFT, segment, workplace, harness_part, harness_index, supplier_part_number, harness_type, SUM(qty_read) as total_qty "
                    + "FROM base_container bc  "
                    + "WHERE EXTRACT(HOUR FROM bc.closed_time) in (6,7,8,9,10,11,12,13) "
                    + "AND EXTRACT(WEEK FROM bc.closed_time) = " + week
                    + "AND EXTRACT(YEAR FROM bc.closed_time) = " + year
                    + "GROUP BY segment, workplace, harness_part, harness_index, supplier_part_number, harness_type)";

            String query_str_2 = "(SELECT "
                    + year + " as year, " + week + " as week, 'Soir' as SHIFT, segment, workplace, harness_part, harness_index, supplier_part_number, harness_type, SUM(qty_read) as total_qty "
                    + "FROM base_container bc  "
                    + "WHERE EXTRACT(HOUR FROM bc.closed_time) in (14,15,16,17,18,19,20,21) "
                    + "AND EXTRACT(WEEK FROM bc.closed_time) = " + week
                    + "AND EXTRACT(YEAR FROM bc.closed_time) = " + year
                    + "GROUP BY segment, workplace, harness_part, harness_index, supplier_part_number, harness_type)";

            String query_str_3 = "(SELECT "
                    + year + " as year, " + week + " as week, 'Nuit' as SHIFT, segment, workplace, harness_part, harness_index, supplier_part_number, harness_type, SUM(qty_read) as total_qty "
                    + "FROM base_container bc  "
                    + "WHERE EXTRACT(HOUR FROM bc.closed_time) in (22,23,0,1,2,3,4,5) "
                    + "AND EXTRACT(WEEK FROM bc.closed_time) = " + week
                    + "AND EXTRACT(YEAR FROM bc.closed_time) = " + year
                    + "GROUP BY segment, workplace, harness_part, harness_index, supplier_part_number, harness_type)";

            String query_str = "SELECT * FROM ("
                    + query_str_1 + " UNION "
                    + query_str_2 + " UNION "
                    + query_str_3
                    + ") results ORDER BY shift, segment, workplace;";

            SQLQuery query = Helper.sess.createSQLQuery(query_str);

            this.dataResultList = query.list();

            Helper.sess.getTransaction().commit();

            return this.dataResultList;

        } catch (HibernateException e) {
            if (Helper.sess.getTransaction() != null) {
                Helper.sess.getTransaction().rollback();
            }
        }

        return this.dataResultList;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tab1_daily = new javax.swing.JTabbedPane();
        north_panel = new javax.swing.JPanel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Eq1startTimeSpinner = new javax.swing.JSpinner();
        Eq1endTimeSpinner = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        export_btn = new javax.swing.JButton();
        workplace_filter = new javax.swing.JComboBox();
        segment_filter = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        Eq2endTimeSpinner = new javax.swing.JSpinner();
        Eq2startTimeSpinner = new javax.swing.JSpinner();
        Eq3endTimeSpinner = new javax.swing.JSpinner();
        Eq3startTimeSpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        week_txt = new javax.swing.JTextField();
        year_txt = new javax.swing.JTextField();
        export_btn1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Production statistics");
        setResizable(false);
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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Equipe Matin");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Equipe Soir");

        Eq1startTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        Eq1endTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Déclaration équipe / jour");

        export_btn.setText("Exporter en Excel...");
        export_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                export_btnActionPerformed(evt);
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

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Workplace");

        Eq2endTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        Eq2startTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        Eq3endTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        Eq3startTimeSpinner.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Equipe Nuit");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Date");

        javax.swing.GroupLayout north_panelLayout = new javax.swing.GroupLayout(north_panel);
        north_panel.setLayout(north_panelLayout);
        north_panelLayout.setHorizontalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, north_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(workplace_filter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(segment_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(311, 311, 311))
            .addGroup(north_panelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, north_panelLayout.createSequentialGroup()
                                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Eq2startTimeSpinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Eq3startTimeSpinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Eq3endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Eq2endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(export_btn, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, north_panelLayout.createSequentialGroup()
                                .addComponent(Eq1startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Eq1endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(311, 311, 311))
                    .addGroup(north_panelLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(380, 380, 380))))
        );
        north_panelLayout.setVerticalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, north_panelLayout.createSequentialGroup()
                        .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(segment_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(workplace_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(Eq1startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Eq1endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(Eq2startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Eq2endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(Eq3startTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Eq3endTimeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(export_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81))
        );

        tab1_daily.addTab("Déclaration jour J", north_panel);

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Déclaration équipe / semaine");

        week_txt.setText("1");

        year_txt.setText("1");

        export_btn1.setText("Exporter en Excel...");
        export_btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                export_btn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel4)
                .addGap(28, 28, 28)
                .addComponent(week_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(year_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(export_btn1)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(week_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(year_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(export_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(398, Short.MAX_VALUE))
        );

        tab1_daily.addTab("Déclaration Hebdomadaire", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab1_daily)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab1_daily)
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
    }//GEN-LAST:event_segment_filterItemStateChanged

    private void workplace_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workplace_filterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_workplace_filterActionPerformed

    private void workplace_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_workplace_filterItemStateChanged

    }//GEN-LAST:event_workplace_filterItemStateChanged

    private void export_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export_btnActionPerformed
        if (checkValidFields()) {
            //######################################################################
            //################   PREPARING THE REQUEST PARAMS ######################
            segments.clear();
            workplaces.clear();

            //Get Times for shift 1
            Eq1startTime = dateDf.format(datePicker.getDate()) + " " + timeDf.format(Eq1startTimeSpinner.getValue());
            Eq1endTime = dateDf.format(datePicker.getDate()) + " " + timeDf.format(Eq1endTimeSpinner.getValue());
            //Get Times for shift 2
            Eq2startTime = dateDf.format(datePicker.getDate()) + " " + timeDf.format(Eq2startTimeSpinner.getValue());
            Eq2endTime = dateDf.format(datePicker.getDate()) + " " + timeDf.format(Eq2endTimeSpinner.getValue());
            //Get Times for shift 3 (22:00 day J to 06:00 day J+1)
            Eq3startTime = dateDf.format(datePicker.getDate()) + " " + timeDf.format(Eq3startTimeSpinner.getValue());
            Eq3endTime = dateDf.format(nextDay(datePicker.getDate())) + " " + timeDf.format(Eq3endTimeSpinner.getValue());

            System.out.println("EQ 1 " + Eq1startTime + " " + Eq1endTime);
            System.out.println("EQ 2 " + Eq2startTime + " " + Eq2endTime);
            System.out.println("EQ 3 " + Eq3startTime + " " + Eq3endTime);

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
        } else {
            JOptionPane.showMessageDialog(null, "Empty field", "Empty field Error", JOptionPane.ERROR_MESSAGE);
        }

        this.dataResultList = executeDayQuery(segments, workplaces, Eq1startTime, Eq1endTime, Eq2startTime, Eq2endTime, Eq3startTime, Eq3endTime);
        //######################################################################
        //##################### PREPARING EXCEL FILE    ########################
        //Create the excel workbook
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("PROD_STATISTICS_BY_SHIFT");
        CreationHelper createHelper = wb.getCreationHelper();
        int total_produced = 0;
        double total_produced_hours = 0.00;

        //Initialiser les entête du fichier
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow((short) 0);

        row.createCell(0).setCellValue("SHIFT");
        row.createCell(1).setCellValue("START TIME");
        row.createCell(2).setCellValue("END TIME");
        row.createCell(3).setCellValue("SEGMENT");
        row.createCell(4).setCellValue("WORKPLACE");
        row.createCell(5).setCellValue("CPN");
        row.createCell(6).setCellValue("STD TIME");
        row.createCell(7).setCellValue("QTY");
        row.createCell(8).setCellValue("PRODUCED Hrs");

        short sheetPointer = 1;

        for (Object[] obj : this.dataResultList) {
            row = sheet.createRow(sheetPointer);
            row.createCell(0).setCellValue(String.valueOf(obj[0])); //SEGMENT
            row.createCell(1).setCellValue(String.valueOf(obj[1])); //START TIME
            row.createCell(2).setCellValue(String.valueOf(obj[2]));//END TIME
            row.createCell(3).setCellValue(String.valueOf(obj[3].toString()));//SEGMENT
            row.createCell(4).setCellValue(String.valueOf(obj[4].toString()));//WORKPLACE
            row.createCell(5).setCellValue(String.valueOf(obj[5].toString().substring(1)));//CPN
            row.createCell(6).setCellValue(Double.valueOf(obj[6].toString()));//STD TIME
            row.createCell(7).setCellValue(Integer.valueOf(obj[7].toString()));//QTY
            row.createCell(8).setCellValue(Double.valueOf(obj[8].toString()));//PRODUCED Hrs

            total_produced = total_produced + Integer.valueOf(String.valueOf(obj[7]));
            total_produced_hours = total_produced_hours + Double.valueOf(String.valueOf(obj[8]));

            sheetPointer++;
        }

        //Date
        row = sheet.createRow(sheetPointer++);
        row.createCell(0).setCellValue("DATE : ");
        row.createCell(1).setCellValue(String.valueOf(datePicker.getDate().toString()));

        //Produced qty
        row = sheet.createRow(sheetPointer++);
        row.createCell(0).setCellValue("TOTAL QTY : ");
        row.createCell(1).setCellValue(total_produced);

        //Produced hours
        row = sheet.createRow(sheetPointer++);
        row.createCell(0).setCellValue("TOTAL Hrs : ");
        row.createCell(1).setCellValue(total_produced_hours);

        //Past the workbook to the file chooser
        new JDialogExcelFileChooser((Frame) super.getParent(), true, wb).setVisible(true);
    }//GEN-LAST:event_export_btnActionPerformed

    private void export_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export_btn1ActionPerformed
        if (week_txt.getText().isEmpty() || year_txt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Empty field", "Empty field Error", JOptionPane.ERROR_MESSAGE);
        } else {
            this.dataResultList = executeWeekQuery(week_txt.getText().trim(), year_txt.getText().trim());
                    //######################################################################
            //##################### PREPARING EXCEL FILE    ########################
            //Create the excel workbook
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("WEEKLY_STATISTICS_BY_SHIFT");
            CreationHelper createHelper = wb.getCreationHelper();

        //Initialiser les entête du fichier
            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow((short) 0);

            row.createCell(0).setCellValue("YEAR");
            row.createCell(1).setCellValue("WEEK");
            row.createCell(2).setCellValue("SHIFT");
            row.createCell(3).setCellValue("SEGMENT");
            row.createCell(4).setCellValue("WORKPLACE");
            row.createCell(5).setCellValue("CPN");
            row.createCell(6).setCellValue("INDEX");
            row.createCell(7).setCellValue("LPN");
            row.createCell(8).setCellValue("TYPE");
            row.createCell(9).setCellValue("TOTAL QTY");

            short sheetPointer = 1;

            for (Object[] obj : this.dataResultList) {
                row = sheet.createRow(sheetPointer);
                row.createCell(0).setCellValue(String.valueOf(obj[0])); //YEAR
                row.createCell(1).setCellValue(String.valueOf(obj[1])); //WEEK
                row.createCell(2).setCellValue(String.valueOf(obj[2]));//SHIFT
                row.createCell(3).setCellValue(String.valueOf(obj[3]));//SEGMENT
                row.createCell(4).setCellValue(String.valueOf(obj[4]));//WORKPLACE
                row.createCell(5).setCellValue(String.valueOf(obj[5]));//CPN
                row.createCell(6).setCellValue(String.valueOf(obj[6]));//INDEX
                row.createCell(7).setCellValue(String.valueOf(obj[7]));//LPN
                row.createCell(8).setCellValue(String.valueOf(obj[8]));//TYPE
                row.createCell(9).setCellValue(Integer.valueOf(obj[9].toString()));//TOTAL QTY

                sheetPointer++;
            }

            //WEEK / YEAR            
            row = sheet.createRow(sheetPointer++);
            row = sheet.createRow(sheetPointer++);
            row.createCell(0).setCellValue("WEEK / YEAR : ");
            row.createCell(1).setCellValue(week_txt.getText() + "/" + year_txt.getText());

            //Past the workbook to the file chooser
            new JDialogExcelFileChooser((Frame) super.getParent(), true, wb).setVisible(true);
        }

    }//GEN-LAST:event_export_btn1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner Eq1endTimeSpinner;
    private javax.swing.JSpinner Eq1startTimeSpinner;
    private javax.swing.JSpinner Eq2endTimeSpinner;
    private javax.swing.JSpinner Eq2startTimeSpinner;
    private javax.swing.JSpinner Eq3endTimeSpinner;
    private javax.swing.JSpinner Eq3startTimeSpinner;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JButton export_btn;
    private javax.swing.JButton export_btn1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel north_panel;
    private javax.swing.JComboBox segment_filter;
    private javax.swing.JTabbedPane tab1_daily;
    private javax.swing.JTextField week_txt;
    private javax.swing.JComboBox workplace_filter;
    private javax.swing.JTextField year_txt;
    // End of variables declaration//GEN-END:variables
}
