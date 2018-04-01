/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import entity.ConfigProject;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 *
 * @author Administrator
 */
public class ComboBoxHelper {
    
    public static JComboBox loadProjectsToComboBox(JComboBox cbox) {
        List result = new ConfigProject().select();
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(null, Helper.ERR0014_NO_PROJECT_FOUND, "Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0014_NO_PROJECT_FOUND);
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigProject cp = (ConfigProject) o;
                cbox.addItem(new ComboItem(cp.getHarnessType(), cp.getHarnessType()));
            }
        }
        return cbox;
    }
}
