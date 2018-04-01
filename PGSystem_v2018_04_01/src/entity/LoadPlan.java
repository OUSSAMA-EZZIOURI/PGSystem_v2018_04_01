/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import hibernate.DAO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author user
 */
@Entity
@Table(name = "load_plan")
public class LoadPlan extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "load_plan_id_seq")
    @SequenceGenerator(name = "load_plan_id_seq", sequenceName = "load_plan_id_seq", allocationSize = 1)
    private Integer id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "delivery_time")
    private Date deliveryTime;

    @Column(name = "create_id")
    private int createId;

    @Column(name = "m_user")
    private String user;
    
    @Column(name = "plan_state")
    private String planState;        
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "loadPlan", cascade = CascadeType.ALL)
    private final Set<LoadPlanLine> lines = new HashSet<LoadPlanLine>(0);
    
    public LoadPlan() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }        

    public int getCreateId() {
        return createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPlanState() {
        return planState;
    }

    public void setPlanState(String planState) {
        this.planState = planState;
    }       
    
    public String getCreateTimeString(String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);
        return (df.format(this.createTime));
    }

        
    
}
