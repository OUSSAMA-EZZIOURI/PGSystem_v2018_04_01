/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.config;

import entity.ManufactureUsers;
import gui.packaging.PackagingVars;
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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author Administrator
 */
public class CONFIG_UI0003_USERS extends javax.swing.JDialog {

    /**
     * Les méthodes JTable qui suivent doivent être dans une class interface
     * initGui() initContainerTableDoubleClick load_table_header
     * reset_table_content disableEditingTable refresh
     *
     * Les 4 champs qui suivent doivent être dans une class interface
     */
    Vector<String> users_table_header = new Vector<String>();
    List<String> table_header = Arrays.asList(
            "#",
            "First Name",
            "Last Name",
            "Login",
            "Password",
            "Access Level",
            "Active"
    );
    Vector users_table_data = new Vector();
    public List<Object[]> resultList;
    ManufactureUsers aux;

    public CONFIG_UI0003_USERS(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initGui();
        refresh();
    }

    private void initGui() {
        //Center the this dialog in the screen
        Helper.centerJDialog(this);

        //Desable table edition
        disableEditingTable();
        //Load table header
        load_table_header();

        //Support double click on rows in container jtable to display history
        this.initContainerTableDoubleClick();
    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            users_table_header.add(it.next());
        }

        users_table.setModel(new DefaultTableModel(users_table_data, users_table_header));
    }

    private void initContainerTableDoubleClick() {
        this.users_table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {

                    Helper.startSession();
                    Query query = Helper.sess.createQuery(HQLHelper.GET_USER_BY_ID);
                    query.setParameter("id", Integer.valueOf(users_table.getValueAt(users_table.getSelectedRow(), 0).toString()));
                    Helper.sess.getTransaction().commit();
                    aux = (ManufactureUsers) query.list().get(0);
                    id_lbl.setText(aux.getId().toString());
                    fname_txtbox.setText(aux.getFirstName());
                    lname_txtbox.setText(aux.getLastName());
                    login_txtbox.setText(aux.getLogin());
                    pwd_txtbox.setText(aux.getPassword());
                    if (aux.getAccessLevel() == 1) {
                        level_combobox.setSelectedIndex(0);
                    } else {
                        level_combobox.setSelectedIndex(0);
                    }
                    if (aux.getActive() == 1) {
                        active_combobox.setSelectedIndex(0);
                    } else {
                        active_combobox.setSelectedIndex(0);
                    }
                    deletel_btn.setEnabled(true);
                    duplicate_btn.setEnabled(true);
                }
            }
        }
        );
    }

    private void reset_table_content() {

        users_table_data = new Vector();
        DefaultTableModel dataModel = new DefaultTableModel(users_table_data, users_table_header);
        users_table.setModel(dataModel);
    }

    public void disableEditingTable() {
        for (int c = 0; c < users_table.getColumnCount(); c++) {
            Class<?> col_class = users_table.getColumnClass(c);
            users_table.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    /**
     * @param users_table_data
     * @param users_table_header
     * @param users_table
     * @todo : reload_table_data a mettre dans une classe interface
     * @param resultList
     */
    public void reload_table_data(List<Object[]> resultList, Vector users_table_data, Vector<String> users_table_header, JTable users_table) {
        this.reset_table_content();
        for (Object[] obj : resultList) {
            @SuppressWarnings("UseOfObsoleteCollectionType")
            Vector<Object> oneRow = new Vector<Object>();

            oneRow.add(String.valueOf(obj[0])); //ID
            oneRow.add(String.valueOf(obj[1])); //First Name
            oneRow.add(String.valueOf(obj[2])); //Last Name
            oneRow.add(String.valueOf(obj[3])); //Login
            oneRow.add(String.valueOf(obj[4])); //Password
            oneRow.add(String.valueOf(obj[5])); //Access Levl
            oneRow.add(String.valueOf(obj[6])); //Active

            users_table_data.add(oneRow);
        }

        users_table.setModel(new DefaultTableModel(users_table_data, users_table_header));
        users_table.setAutoCreateRowSorter(true);
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    private int isLoginExist(String login) {
        int size = 0;
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_USER_BY_LOGIN);
        query.setParameter("login", login);

        Helper.sess.getTransaction().commit();
        size = query.list().size();

        return size;
    }

    private void clearFields() {
        fname_txtbox.setText("");
        lname_txtbox.setText("");
        login_txtbox.setText("");
        level_combobox.setSelectedIndex(0);
        active_combobox.setSelectedIndex(0);
        pwd_txtbox.setText("");
        id_lbl.setText("");
        deletel_btn.setEnabled(false);
        duplicate_btn.setEnabled(false);
        aux = null;
    }

    private void clearSearchFields() {
        fname_txtbox_search.setText("");
        lname_txtbox_search.setText("");
        login_txtbox_search.setText("");
    }

    private void refresh() {
        String query_str = " SELECT "
                + " u.id AS id, "
                + " u.first_name AS first_name, "
                + " u.last_name AS last_name, "
                + " u.login AS login, "
                + " u.password AS password, "
                + " u.access_level AS access_level, "
                + " u.active AS active "
                + " FROM Manufacture_Users u WHERE 1=1 ";
        Helper.startSession();
        if (!fname_txtbox_search.getText().trim().equals("")) {
            query_str += " AND first_name LIKE '%" + fname_txtbox_search.getText() + "%'";
        }
        if (!lname_txtbox_search.getText().trim().equals("")) {
            query_str += " AND last_name LIKE '%" + lname_txtbox_search.getText() + "%'";
        }
        if (!login_txtbox_search.getText().trim().equals("")) {
            query_str += " AND login LIKE '%" + login_txtbox_search.getText() + "%'";
        }
        query_str += " ORDER BY id DESC";
        SQLQuery query = Helper.sess.createSQLQuery(query_str);
        resultList = query.list();

        Helper.sess.getTransaction().commit();

        this.reload_table_data(resultList, users_table_data, users_table_header, users_table);

        this.disableEditingTable();
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
        fname_lbl = new javax.swing.JLabel();
        fname_txtbox = new javax.swing.JTextField();
        lname_lbl = new javax.swing.JLabel();
        lname_txtbox = new javax.swing.JTextField();
        login_lbl = new javax.swing.JLabel();
        login_txtbox = new javax.swing.JTextField();
        pwd_txtbox = new javax.swing.JTextField();
        pwd_lbl = new javax.swing.JLabel();
        pwd_lbl1 = new javax.swing.JLabel();
        save_btn = new javax.swing.JButton();
        cancel_btn = new javax.swing.JButton();
        level_combobox = new javax.swing.JComboBox();
        active_combobox = new javax.swing.JComboBox();
        pwd_lbl2 = new javax.swing.JLabel();
        deletel_btn = new javax.swing.JButton();
        id_lbl = new javax.swing.JLabel();
        duplicate_btn = new javax.swing.JButton();
        msg_lbl = new javax.swing.JLabel();
        fname_lbl1 = new javax.swing.JLabel();
        user_list_panel = new javax.swing.JPanel();
        user_table_scroll = new javax.swing.JScrollPane();
        users_table = new javax.swing.JTable();
        fname_txtbox_search = new javax.swing.JTextField();
        fname_lbl_search = new javax.swing.JLabel();
        lname_lbl_search = new javax.swing.JLabel();
        lname_txtbox_search = new javax.swing.JTextField();
        llogin_lbl_search = new javax.swing.JLabel();
        login_txtbox_search = new javax.swing.JTextField();
        refresh_btn = new javax.swing.JButton();
        clear_search_btn = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "User form", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        jPanel1.setToolTipText("User");
        jPanel1.setName(""); // NOI18N

        fname_lbl.setText("First Name *");

        fname_txtbox.setName("fname_txtbox"); // NOI18N

        lname_lbl.setText("Last Name *");

        lname_txtbox.setName("fname_txtbox"); // NOI18N

        login_lbl.setText("Login *");

        login_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_lbl.setText("Password *");

        pwd_lbl1.setText("Level *");

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

        level_combobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1000", "9000", "9999" }));
        level_combobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                level_comboboxActionPerformed(evt);
            }
        });

        active_combobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "0" }));
        active_combobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                active_comboboxActionPerformed(evt);
            }
        });

        pwd_lbl2.setText("Active *");

        deletel_btn.setText("Delete");
        deletel_btn.setEnabled(false);
        deletel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletel_btnActionPerformed(evt);
            }
        });

        id_lbl.setBackground(new java.awt.Color(153, 204, 255));
        id_lbl.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        duplicate_btn.setText("Dupliquer");
        duplicate_btn.setEnabled(false);
        duplicate_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duplicate_btnActionPerformed(evt);
            }
        });

        msg_lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        msg_lbl.setForeground(new java.awt.Color(0, 0, 255));

        fname_lbl1.setText("ID");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(login_lbl)
                                    .addComponent(fname_lbl)
                                    .addComponent(fname_lbl1))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fname_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(login_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(id_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lname_lbl)
                                        .addGap(18, 18, 18)
                                        .addComponent(lname_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(pwd_lbl)
                                        .addGap(18, 18, 18)
                                        .addComponent(pwd_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(34, 34, 34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(pwd_lbl2)
                                    .addComponent(pwd_lbl1))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(level_combobox, 0, 146, Short.MAX_VALUE)
                                    .addComponent(active_combobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(save_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancel_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(duplicate_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(deletel_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(msg_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(id_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fname_lbl1))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fname_lbl)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(lname_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lname_lbl)
                                .addComponent(pwd_lbl1)
                                .addComponent(level_combobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(login_lbl)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(pwd_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pwd_lbl)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pwd_lbl2)
                                    .addComponent(active_combobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(fname_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(login_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deletel_btn, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(save_btn)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancel_btn)
                        .addComponent(duplicate_btn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msg_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        user_list_panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Users list", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        user_list_panel.setToolTipText("");

        users_table.setModel(new javax.swing.table.DefaultTableModel(
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
        user_table_scroll.setViewportView(users_table);

        fname_txtbox_search.setName("fname_txtbox"); // NOI18N
        fname_txtbox_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fname_txtbox_searchKeyPressed(evt);
            }
        });

        fname_lbl_search.setText("First Name");

        lname_lbl_search.setText("Last Name");

        lname_txtbox_search.setName("fname_txtbox"); // NOI18N
        lname_txtbox_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lname_txtbox_searchKeyPressed(evt);
            }
        });

        llogin_lbl_search.setText("Login");

        login_txtbox_search.setName("fname_txtbox"); // NOI18N
        login_txtbox_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                login_txtbox_searchKeyPressed(evt);
            }
        });

        refresh_btn.setText("Refresh");
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        clear_search_btn.setText("Clear");
        clear_search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_search_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout user_list_panelLayout = new javax.swing.GroupLayout(user_list_panel);
        user_list_panel.setLayout(user_list_panelLayout);
        user_list_panelLayout.setHorizontalGroup(
            user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(user_list_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(user_table_scroll)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, user_list_panelLayout.createSequentialGroup()
                        .addComponent(fname_lbl_search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fname_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lname_lbl_search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lname_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(llogin_lbl_search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(login_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                        .addComponent(refresh_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clear_search_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        user_list_panelLayout.setVerticalGroup(
            user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, user_list_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(refresh_btn)
                        .addComponent(clear_search_btn))
                    .addGroup(user_list_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fname_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fname_lbl_search)
                        .addComponent(lname_lbl_search)
                        .addComponent(lname_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(llogin_lbl_search)
                        .addComponent(login_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(user_table_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(user_list_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(user_list_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_btnActionPerformed
        clearFields();
    }//GEN-LAST:event_cancel_btnActionPerformed

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        refresh();
    }//GEN-LAST:event_refresh_btnActionPerformed

    private void level_comboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_level_comboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_level_comboboxActionPerformed

    private void save_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_btnActionPerformed
        if (lname_txtbox.getText().isEmpty() || fname_txtbox.getText().isEmpty() || login_txtbox.getText().isEmpty() || pwd_txtbox.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Valeur null pour un ou plusieurs champs. \nMerci de remplir touts les champs requis !", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (id_lbl.getText().isEmpty()) { // ID Label is empty, then is a new User

                if (isLoginExist(login_txtbox.getText().trim()) != 0) { //true = exist !
                    JOptionPane.showMessageDialog(null, String.format("Login %s exist déjà !", login_txtbox.getText()), "Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    ManufactureUsers mu = new ManufactureUsers();
                    mu.setFirstName(fname_txtbox.getText());
                    mu.setLastName(lname_txtbox.getText());
                    mu.setActive(Integer.valueOf(active_combobox.getSelectedItem().toString()));
                    mu.setAccessLevel(Integer.valueOf(level_combobox.getSelectedItem().toString()));
                    mu.setLogin(login_txtbox.getText());
                    mu.setPassword(pwd_txtbox.getText());
                    mu.setCreateId(PackagingVars.context.getUser().getId());
                    mu.setWriteId(PackagingVars.context.getUser().getId());
                    mu.setCreateTime(new Date());
                    mu.setWriteTime(new Date());
                    mu.create(mu);

                    clearFields();
                    refresh();

                    msg_lbl.setText("Nouveau élément enregistré !");
                }
            } else { // ID Label is filed, then is an update
                aux.setFirstName(fname_txtbox.getText());
                aux.setLastName(lname_txtbox.getText());
                aux.setActive(Integer.valueOf(active_combobox.getSelectedItem().toString()));
                aux.setAccessLevel(Integer.valueOf(level_combobox.getSelectedItem().toString()));
                aux.setLogin(login_txtbox.getText());
                aux.setPassword(pwd_txtbox.getText());
                aux.setWriteId(PackagingVars.context.getUser().getId());
                aux.setWriteTime(new Date());
                aux.update(aux);
                clearFields();
                refresh();
                msg_lbl.setText("Changements enregistrés !");
            }
        }
    }//GEN-LAST:event_save_btnActionPerformed

    private void active_comboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_active_comboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_active_comboboxActionPerformed

    private void clear_search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_search_btnActionPerformed
        clearSearchFields();
    }//GEN-LAST:event_clear_search_btnActionPerformed

    private void deletel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletel_btnActionPerformed
        int confirmed = JOptionPane.showConfirmDialog(null,
                String.format("Are you sure you want to drop the user [%s] ?",
                        this.aux.getId()),
                "Delete user",
                JOptionPane.WARNING_MESSAGE);

        if (confirmed == 0) {
            aux.delete(aux);
            clearFields();
            refresh();
        }
    }//GEN-LAST:event_deletel_btnActionPerformed

    private void duplicate_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_duplicate_btnActionPerformed
        id_lbl.setText("");

        if (String.valueOf(aux.getActive()).equals("1")) {
            active_combobox.setSelectedIndex(0);
        } else {
            active_combobox.setSelectedIndex(1);
        }

        this.aux = null;
        msg_lbl.setText("Element dupliqué !");
    }//GEN-LAST:event_duplicate_btnActionPerformed

    private void fname_txtbox_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fname_txtbox_searchKeyPressed
        refresh();
    }//GEN-LAST:event_fname_txtbox_searchKeyPressed

    private void lname_txtbox_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lname_txtbox_searchKeyPressed
        refresh();
    }//GEN-LAST:event_lname_txtbox_searchKeyPressed

    private void login_txtbox_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_login_txtbox_searchKeyPressed
        refresh();
    }//GEN-LAST:event_login_txtbox_searchKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox active_combobox;
    private javax.swing.JButton cancel_btn;
    private javax.swing.JButton clear_search_btn;
    private javax.swing.JButton deletel_btn;
    private javax.swing.JButton duplicate_btn;
    private javax.swing.JLabel fname_lbl;
    private javax.swing.JLabel fname_lbl1;
    private javax.swing.JLabel fname_lbl_search;
    private javax.swing.JTextField fname_txtbox;
    private javax.swing.JTextField fname_txtbox_search;
    private javax.swing.JLabel id_lbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox level_combobox;
    private javax.swing.JLabel llogin_lbl_search;
    private javax.swing.JLabel lname_lbl;
    private javax.swing.JLabel lname_lbl_search;
    private javax.swing.JTextField lname_txtbox;
    private javax.swing.JTextField lname_txtbox_search;
    private javax.swing.JLabel login_lbl;
    private javax.swing.JTextField login_txtbox;
    private javax.swing.JTextField login_txtbox_search;
    private javax.swing.JLabel msg_lbl;
    private javax.swing.JLabel pwd_lbl;
    private javax.swing.JLabel pwd_lbl1;
    private javax.swing.JLabel pwd_lbl2;
    private javax.swing.JTextField pwd_txtbox;
    private javax.swing.JButton refresh_btn;
    private javax.swing.JButton save_btn;
    private javax.swing.JPanel user_list_panel;
    private javax.swing.JScrollPane user_table_scroll;
    private javax.swing.JTable users_table;
    // End of variables declaration//GEN-END:variables

}
