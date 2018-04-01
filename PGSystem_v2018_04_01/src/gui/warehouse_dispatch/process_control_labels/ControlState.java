/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.warehouse_dispatch.process_control_labels;

import javax.swing.JTextField;



/**
 *
 * @author ezou1001
 */
public interface ControlState {
    public void doAction(WarehouseControlContext context);    
    public void clearScanBox(JTextField scan_txtbox);
    public void clearContextSessionVals();
}
