/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import java.util.List;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */

public class JtableHelper{    
    
    /**
     * 
     * @param resultList : List of objects to map
     * @param columnsNames : String list to be used as jtable titles
     * @param jtable  : Target jTable
     */
    public void mapDataToJtable(List<Object[]> resultList, @SuppressWarnings("UseOfObsoleteCollectionType") Vector<String> columnsNames, JTable jtable) {
        
        Vector tableRows = new Vector<Object>();
        for (Object[] line : resultList) {
            @SuppressWarnings("UseOfObsoleteCollectionType")
            Vector<Object> oneRow = new Vector<Object>();
            for (Object cell : line) {
                oneRow.add(String.valueOf(cell));
            }
            tableRows.add(oneRow);
        }

        jtable.setModel(new DefaultTableModel(tableRows, columnsNames));
        jtable.setAutoCreateRowSorter(true);
    }
    
    /**
     * 
     * 
     * @param jtable        : Target JTable to be set
     * @param columnsNames  :  Vector<String>()
     */
    public void resetJtable(JTable jtable, @SuppressWarnings("UseOfObsoleteCollectionType") Vector<String> columnsNames) {
        //Reset the content of packaging items jtable
        Vector data = new Vector();
        DefaultTableModel itemsDataModel = new DefaultTableModel(data, columnsNames);
        jtable.removeAll();
        jtable.setModel(itemsDataModel);

    }
    
}
