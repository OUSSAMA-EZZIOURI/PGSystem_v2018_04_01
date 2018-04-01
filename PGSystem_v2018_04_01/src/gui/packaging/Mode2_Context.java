/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging;

import entity.BaseContainerTmp;
import entity.BaseHarnessAdditionalBarecodeTmp;
import entity.ManufactureUsers;
import gui.packaging.mode2.state.Mode2_State;

/**
 *
 * @author ezou1001
 */
public class Mode2_Context extends Context {

    
    private Mode2_State state;

    public Mode2_Context() {
        state = null;
    }

    public void setState(Mode2_State state) {
        this.state = state;
    }

    public Mode2_State getState() {
        return state;
    }
    

}
