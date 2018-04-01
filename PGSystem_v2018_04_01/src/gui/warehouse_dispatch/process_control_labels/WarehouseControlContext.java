/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch.process_control_labels;

import entity.ManufactureUsers;
import gui.warehouse_dispatch.state.WarehouseHelper;

/**
 *
 * @author ezou1001
 */
public class WarehouseControlContext {

    private ManufactureUsers user = new ManufactureUsers();
    private ControlState state;

    
    

    public WarehouseControlContext() {
        state = null;

    }

    public ManufactureUsers getUser() {
        return user;
    }

    public void setUser(ManufactureUsers user) {
        this.user = user;
    }

    public void setState(ControlState state) {
        this.state = state;
    }

    public ControlState getState() {
        return state;
    }

    public void clearAllVars() {
        WarehouseHelper.temp_load_plan = null;
    }

}
