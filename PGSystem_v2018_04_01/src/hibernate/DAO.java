/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate;

import helper.Helper;
import org.hibernate.HibernateException;

/**
 *
 * @author user
 */
public class DAO {

    public int create(Object obj) {
        
        try {
            Helper.sess.beginTransaction();
            int id = (int) Helper.sess.save(obj);
            Helper.sess.getTransaction().commit();
            //Helper.sess.flush();
            //Helper.sess.clear();
            return id;
        } catch (HibernateException e) {
            if (Helper.sess.getTransaction() != null && Helper.sess.getTransaction().isActive()) {
                try {
                    // Second try catch as the rollback could fail as well
                    Helper.sess.getTransaction().rollback();
                } catch (HibernateException e1) {
                    System.out.println("Error rolling back transaction");
                    System.out.println(e1.getMessage());
                }
                   // throw again the first exception
                System.out.println(e.getMessage());
                throw e;
            }
        }
        return 0;
    }

    public void update(Object obj) {        
        try {
            
            Helper.sess.beginTransaction();
            Helper.sess.update(obj);
            
            Helper.sess.getTransaction().commit();
            Helper.sess.flush();
            Helper.sess.clear();
        } catch (HibernateException e) {
            if (Helper.sess.getTransaction() != null && Helper.sess.getTransaction().isActive()) {
                try {
                    // Second try catch as the rollback could fail as well
                    Helper.sess.getTransaction().rollback();
                } catch (HibernateException e1) {
                    System.out.println("Error rolling back transaction");
                    System.out.println(e1.getMessage());
                }
                // throw again the first exception
                System.out.println(e.getMessage());
                throw e;
            }
        }
    }

    public void delete(Object obj) {        
        try {
            Helper.sess.beginTransaction();
            Helper.sess.delete(obj);
            Helper.sess.getTransaction().commit();  
            Helper.sess.flush();
            Helper.sess.clear();
        } catch (HibernateException e) {
            if (Helper.sess.getTransaction() != null && Helper.sess.getTransaction().isActive()) {
                try {
                    // Second try catch as the rollback could fail as well
                    Helper.sess.getTransaction().rollback();
                } catch (HibernateException e1) {
                    System.out.println("Error rolling back transaction");
                    System.out.println(e1.getMessage());
                }
                // throw again the first exception
                System.out.println(e.getMessage());
                throw e;
            }
        }
    }

}
