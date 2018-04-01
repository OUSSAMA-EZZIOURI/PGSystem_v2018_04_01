/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import hibernate.DAO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "load_plan_destination_rel")
public class LoadPlanDestinationRel extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "load_plan_destination_rel_id_seq")
    @SequenceGenerator(name = "load_plan_destination_rel_id_seq", sequenceName = "load_plan_destination_rel_id_seq", allocationSize = 1)
    private Integer id;
       
    @Column(name = "destination")
    private String destination;    
    
    @Column(name = "load_plan_id")
    private int loadPlanId;

    public LoadPlanDestinationRel() {
    }

    public LoadPlanDestinationRel(String destination, int loadPlanId) {
        this.destination = destination;
        this.loadPlanId = loadPlanId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getLoadPlanId() {
        return loadPlanId;
    }

    public void setLoadPlanId(int loadPlanId) {
        this.loadPlanId = loadPlanId;
    }
    
    
    
    
        
}
