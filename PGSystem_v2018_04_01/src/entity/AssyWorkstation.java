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
 * AssyWorkstation generated by hbm2java
 */
@Entity
@Table(name = "assy_workstation")
public class AssyWorkstation extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assy_workstation_id_seq")
    @SequenceGenerator(name = "assy_workstation_id_seq", sequenceName = "assy_workstation_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "workstation")
    private String workstation;    
    
    @Column(name = "operators")
    private Integer operators;
    
    
    public AssyWorkstation() {
    }   

    public AssyWorkstation(Integer id, String workstation, Integer operators) {
        this.id = id;
        this.workstation = workstation;
        this.operators = operators;
    }        

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public Integer getOperators() {
        return operators;
    }

    public void setOperators(Integer operators) {
        this.operators = operators;
    }

    @Override
    public String toString() {
        return "AssyWorkstation{" + "id=" + id + ", workstation=" + workstation + ", operators=" + operators + '}';
    }        
    
    //######################################################################   
    public List select() {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ALL_ASSY_WORKSTATIONS);
        Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        return query.list();
    }
       
}