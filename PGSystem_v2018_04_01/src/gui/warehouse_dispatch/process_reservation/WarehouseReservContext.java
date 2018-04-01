/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.warehouse_dispatch.process_reservation;

import entity.ManufactureUsers;
import gui.warehouse_dispatch.state.WarehouseHelper;

/**
 *
 * @author ezou1001
 */
public class WarehouseReservContext {

    private ManufactureUsers user = new ManufactureUsers();
    private ReservationState state;

    
    

    public WarehouseReservContext() {
        state = null;

    }

    public ManufactureUsers getUser() {
        return user;
    }

    public void setUser(ManufactureUsers user) {
        this.user = user;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

    public ReservationState getState() {
        return state;
    }

    public void clearAllVars() {
        WarehouseHelper.temp_load_plan = null;
    }

}
