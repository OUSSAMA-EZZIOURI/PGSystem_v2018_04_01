/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package example;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author user
 */
public class CheckboxListCellRenderer<E> extends JCheckBox implements
		ListCellRenderer<E> {
 
	private static final long serialVersionUID = 3734536442230283966L;
 
	@Override
	public Component getListCellRendererComponent(JList<? extends E> list,
			E value, int index, boolean isSelected, boolean cellHasFocus) {
		setComponentOrientation(list.getComponentOrientation());
 
		setFont(list.getFont());
		setText(String.valueOf(value));
 
		setBackground(list.getBackground());
		setForeground(list.getForeground());
 
		setSelected(isSelected);
		setEnabled(list.isEnabled());
 
		return this;
	}

    
 
}
