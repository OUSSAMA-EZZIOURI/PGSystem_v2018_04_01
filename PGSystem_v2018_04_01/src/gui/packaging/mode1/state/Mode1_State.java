/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.packaging.mode1.state;

import gui.packaging.Mode1_Context;
import javax.swing.ImageIcon;
import javax.swing.JTextField;



/**
 *
 * @author ezou1001
 */
public interface Mode1_State {
    public void doAction(Mode1_Context context);
    public ImageIcon getImg();
    public void clearScanBox(JTextField scan_txtbox);
    public void clearContextSessionVals();
}
