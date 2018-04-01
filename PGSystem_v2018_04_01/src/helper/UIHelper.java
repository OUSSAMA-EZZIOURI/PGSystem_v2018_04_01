/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class UIHelper {

    /**
     * Delete the text content of given components
     *
     * @param args
     */
    public static void clearTextFields(Component... args) {
        for (Component component : args) {
            switch (component.getClass().getName()) {
                case "JTextField":
                    ((JTextField) component).setText("");
                    break;
                case "JLabel":
                    ((JLabel) component).setText("");
                    break;
                case "JTextArea":
                    ((JTextArea) component).setText("");
                    break;
                case "JComboBox":
                    ((JComboBox) component).setSelectedIndex(0);
                    break;

            }
        }
    }

    /**
     * Set required field background color to
     *
     * @param args
     */
    public static void highlightRequiredFields(Component... args) {
        for (Component component : args) {
            switch (component.getClass().getName()) {
                case "JTextField":
                    ((JTextField) component).setBackground(new Color(204, 204, 255));
                    break;
                case "JLabel":
                    ((JLabel) component).setBackground(new Color(204, 204, 255));
                    break;
                case "JTextArea":
                    ((JTextArea) component).setBackground(new Color(204, 204, 255));
                    break;
                case "JComboBox":
                    ((JComboBox) component).setBackground(new Color(204, 204, 255));
                    break;

            }
        }
    }

    /**
     * Set the actual state of UI components
     *
     * @param newState : New state of the components
     * @param args : List of components, text, textarea, list, combobox
     */
    public static void setComponentsState(boolean newState, Component... args) {
        for (Component component : args) {
            component.setEnabled(newState);
        }
    }

    /**
     * Disable edition of jtable cells
     *
     * @param jtable
     */
    public static void disableEditingJtable(JTable jtable) {
        for (int c = 0; c < jtable.getColumnCount(); c++) {
            Class<?> col_class = jtable.getColumnClass(c);
            jtable.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    /**
     * @param classFields
     * @param table_data Table data
     * @param table_header Header list
     * @param jtable Target jTable to set
     * @param entity : The entity class name, used to cast table rows
     * @param resultList List of data, Hibernate query result
     */
    public void reload_table_data(String[] classFields, List resultList, Vector table_data, Vector<String> table_header, JTable jtable, String entity) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        for (Object obj : resultList) {
            Class<?> clazz = Class.forName(entity);
            Constructor<?> ctor = clazz.getConstructor(String.class);
            Object object = ctor.newInstance(new Object[]{});
            object = obj;
            Method fieldGetter = object.getClass().getMethod("getId");
            Vector<Object> oneRow = new Vector<Object>();
            String id = fieldGetter.invoke(object).toString();
            oneRow.add(id);
            table_data.add(oneRow);
        }

        jtable.setModel(new DefaultTableModel(table_data, table_header));
        jtable.setAutoCreateRowSorter(true);
    }

    public static JTable load_table_header(JTable jtable, List<String> table_header) {
        Vector<String> header_vector = new Vector<String>();
        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            header_vector.add(it.next());
        }

        jtable.setModel(new DefaultTableModel(header_vector, 0));
        return jtable;
    }

    public static List<String> manageCheckedCheckboxes(final Container c) {
        Component[] comps = c.getComponents();
        List<String> checkedTexts = new ArrayList<String>();

        for (Component comp : comps) {

            if (comp instanceof JCheckBox) {
                JCheckBox box = (JCheckBox) comp;
                if (box.isSelected()) {

                    String text = box.getText();
                    checkedTexts.add(text);
                }
            }
        }
        return checkedTexts;
    }
    
    public static List<String> manageCheckedRadioButtons(final Container c) {
        Component[] comps = c.getComponents();
        List<String> checkedTexts = new ArrayList<String>();

        for (Component comp : comps) {

            if (comp instanceof JRadioButton) {
                JRadioButton radio = (JRadioButton) comp;
                if (radio.isSelected()) {

                    String text = radio.getText();
                    checkedTexts.add(text);
                }
            }
        }
        return checkedTexts;
    }
    
    
    public static boolean listContains(List<String> list, String string){
        for (int i = 0; i < list.size(); i++) {
            if(list.contains(string)){
                System.out.println(string + " exist !");
                return true;
            }            
        }
        return false;
    }

}
