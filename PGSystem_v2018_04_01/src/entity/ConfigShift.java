package entity;

import helper.HQLHelper;
import helper.Helper;
import hibernate.DAO;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.Query;

/**
 * ConfigUcs generated by hbm2java
 */
@Entity
@Table(name = "config_shift")
public class ConfigShift extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_shift_id_seq")
    @SequenceGenerator(name = "config_shift_id_seq", sequenceName = "config_shift_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_hour")
    private String startHour;

    @Column(name = "start_minute")
    private String startMinute;

    @Column(name = "end_hour")
    private String endHour;

    @Column(name = "end_minute")
    private String endMinute;

    public ConfigShift() {
    }

    public ConfigShift(String name, String description, String startHour, String startMinute, String endHour, String endMinute) {
        this.name = name;
        this.description = description;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    @Override
    public String toString() {
        return "ConfigShift{" + "id=" + id + ", name=" + name + ", description=" + description + ", startHour=" + startHour + ", startMinute=" + startMinute + ", endHour=" + endHour + ", endMinute=" + endMinute + '}';
    }
    
    
    
    //######################################################################   
    public List select() {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ALL_SHIFT);
        Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        return query.list();
    }
    
    //######################################################################   
    public ConfigShift selectShiftByDesc(String description) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_SHIFT_BY_DESC);
        query.setParameter("description", description);        
        Helper.sess.getTransaction().commit();  
        if(!query.list().isEmpty()) return (ConfigShift) query.list().get(0);
        return null;
    }
    
    public ConfigShift selectShiftById(Integer id) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_SHIFT_BY_ID);
        query.setParameter("id", id);        
        Helper.sess.getTransaction().commit();  
        if(!query.list().isEmpty()) return (ConfigShift) query.list().get(0);
        return null;
    }
}