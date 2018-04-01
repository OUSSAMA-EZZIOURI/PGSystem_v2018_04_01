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
@Table(name = "load_plan_line_packaging")
public class LoadPlanLinePackaging extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "load_plan_line_packaging_id_seq")
    @SequenceGenerator(name = "load_plan_line_packaging_id_seq", sequenceName = "load_plan_line_packaging_id_seq", allocationSize = 1)
    private Integer id;
    
    @Column(name = "load_plan_id", insertable = true ,updatable = true)
    private int loadPlanId;
    
    @Column(name = "pile_num")
    private Integer pileNum;
    
    @Column(name = "pack_item")
    private String packItem;        
    
    @Column(name = "qty")
    private int qty;
    
    @Column(name = "destination")
    private String destination;  
    
    @Column(name = "comment")
    private String comment;  
    
    
    public LoadPlanLinePackaging() {
    }

    public LoadPlanLinePackaging(int loadPlanId, Integer pileNum, 
            String packItem, int qty, String destination, String comment) {
        this.loadPlanId = loadPlanId;
        this.pileNum = pileNum;
        this.packItem = packItem;
        this.qty = qty;
        this.destination = destination;
        this.comment = comment;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getLoadPlanId() {
        return loadPlanId;
    }

    public void setLoadPlanId(int loadPlanId) {
        this.loadPlanId = loadPlanId;
    }

    public Integer getPileNum() {
        return pileNum;
    }

    public void setPileNum(Integer pileNum) {
        this.pileNum = pileNum;
    }

    public String getPackItem() {
        return packItem;
    }

    public void setPackItem(String packItem) {
        this.packItem = packItem;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "LoadPlanLinePackaging{" + "id=" + id + ", loadPlanId=" + loadPlanId + ", pileNum=" + pileNum + ", packItem=" + packItem + ", qty=" + qty + ", destination=" + destination + ", comment=" + comment + '}';
    }
    
}
