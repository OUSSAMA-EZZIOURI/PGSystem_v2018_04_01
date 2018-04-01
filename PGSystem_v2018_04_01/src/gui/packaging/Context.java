/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging;

import entity.BaseContainer;
import entity.BaseContainerTmp;
import entity.BaseHarness;
import entity.BaseHarnessAdditionalBarecode;
import entity.BaseHarnessAdditionalBarecodeTmp;
import entity.ManufactureUsers;

/**
 *
 * @author Oussama EZZIOURI
 */
public class Context {
    
    private BaseContainer tempBC = new BaseContainer();    
    private BaseHarness tempBH = new BaseHarness();    
    private BaseHarnessAdditionalBarecode tempBarecode = new BaseHarnessAdditionalBarecode();
    private String harnessCounter;
    
    private BaseContainerTmp baseContainerTmp = new BaseContainerTmp();
    private BaseHarnessAdditionalBarecodeTmp baseHarnessAdditionalBarecodeTmp = new BaseHarnessAdditionalBarecodeTmp();
    private String feedback = "-";    
    private Integer labelCount = 0;
    //Connected User Object
    private ManufactureUsers user = new ManufactureUsers();
    
    public Integer getLabelCount() {
        return labelCount;
    }

    public void setLabelCount(Integer labelCount) {
        this.labelCount = labelCount;
    }

    public ManufactureUsers getUser() {
        return user;
    }

    public void setUser(ManufactureUsers user) {
        this.user = user;
    }

    public BaseContainerTmp getBaseContainerTmp() {
        return baseContainerTmp;
    }

    public void setBaseContainerTmp(BaseContainerTmp baseContainerTmp) {
        this.baseContainerTmp = baseContainerTmp;
    }

    public BaseHarnessAdditionalBarecodeTmp getBaseHarnessAdditionalBarecodeTmp() {
        return baseHarnessAdditionalBarecodeTmp;
    }

    public void setBaseHarnessAdditionalBarecodeTmp(BaseHarnessAdditionalBarecodeTmp baseHarnessAdditionalBarecodeTmp) {
        this.baseHarnessAdditionalBarecodeTmp = baseHarnessAdditionalBarecodeTmp;
    }
    
    public void clearContext() {
        this.user = null;
        this.baseContainerTmp = null;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public BaseContainer getTempBC() {
        return tempBC;
    }

    public void setTempBC(BaseContainer tempBC) {
        this.tempBC = tempBC;
    }

    public BaseHarness getTempBH() {
        return tempBH;
    }

    public void setTempBH(BaseHarness tempBH) {
        this.tempBH = tempBH;
    }

    public BaseHarnessAdditionalBarecode getTempBarecode() {
        return tempBarecode;
    }

    public void setTempBarecode(BaseHarnessAdditionalBarecode tempBarecode) {
        this.tempBarecode = tempBarecode;
    }

    public String getHarnessCounter() {
        return harnessCounter;
    }

    public void setHarnessCounter(String harnessCounter) {
        this.harnessCounter = harnessCounter;
    }
    
    
    
    @Override
    public String toString() {
        return "Context{" + "baseContainerTmp=" + baseContainerTmp + '}';
    }
    
}
