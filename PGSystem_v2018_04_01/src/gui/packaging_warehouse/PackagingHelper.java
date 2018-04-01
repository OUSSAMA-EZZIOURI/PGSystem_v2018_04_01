/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging_warehouse;

import entity.ManufactureUsers;
import helper.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author user
 */
public class PackagingHelper {

    
    public static Session sess = HibernateUtil.getInstance().openSession(); 
    public static PACKAGING_WAREHOUSE_UI0001_MAIN_FORM Packaging_Main_Gui;
    public static ManufactureUsers user = new ManufactureUsers();
    
}
