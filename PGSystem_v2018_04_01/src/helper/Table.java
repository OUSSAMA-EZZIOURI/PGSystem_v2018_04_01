/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author user
 */
public class Table extends JTable {
    public void setColumnWidths(int... widths) {
        for (int i = 0; i < widths.length; i++) {
            if (i < columnModel.getColumnCount()) {
                columnModel.getColumn(i).setMaxWidth(widths[i]);
            }
            else break;
        }
    }
    
    /**
 * Set the width of the columns as percentages.
 * 
 * @param table
 *            the {@link JTable} whose columns will be set
 * @param percentages
 *            the widths of the columns as percentages; note: this method
 *            does NOT verify that all percentages add up to 100% and for
 *            the columns to appear properly, it is recommended that the
 *            widths for ALL columns be specified
 */
private static void setWidthAsPercentages(JTable table,
        double... percentages) {
    final double factor = 10000;
 
    TableColumnModel model = table.getColumnModel();
    for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
        TableColumn column = model.getColumn(columnIndex);
        column.setPreferredWidth((int) (percentages[columnIndex] * factor));
    }
}
}