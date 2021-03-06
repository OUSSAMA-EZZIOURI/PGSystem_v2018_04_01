package entity;
// Generated 6 févr. 2016 21:43:55 by Hibernate Tools 3.6.0

import hibernate.DAO;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;

/**
 * BaseUser generated by hbm2java
 */
@Entity
public class BaseUser extends DAO implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createTime;    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date writeTime;
    private int createId;
    private int writeId;
    private String fname;
    private String lname;
    private String login;
    private String password;
    private int accessLevel;
    private int active;
    private String harnessType;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "base_user_module_rel", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "module_id") })
    
    private Set<BaseModule> modules = new HashSet<BaseModule>(0);

    public BaseUser() {
    }

    public BaseUser(Date createTime, Date writeTime, int createId, int writeId, String fname, String login, String password, int accessLevel, int active, String harnessType) {
        this.createTime = createTime;
        this.writeTime = writeTime;
        this.createId = createId;
        this.writeId = writeId;
        this.fname = fname;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
        this.active = active;
        this.harnessType = harnessType;
    }

    public BaseUser(Date createTime, Date writeTime, int createId, int writeId, String fname, String lname, String login, String password, int accessLevel, int active, String harnessType) {
        this.createTime = createTime;
        this.writeTime = writeTime;
        this.createId = createId;
        this.writeId = writeId;
        this.fname = fname;
        this.lname = lname;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
        this.active = active;
        this.harnessType = harnessType;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getWriteTime() {
        return this.writeTime;
    }

    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }

    public int getCreateId() {
        return this.createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public int getWriteId() {
        return this.writeId;
    }

    public void setWriteId(int writeId) {
        this.writeId = writeId;
    }

    public String getFname() {
        return this.fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return this.lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getHarnessType() {
        return harnessType;
    }

    public void setHarnessType(String harnessType) {
        this.harnessType = harnessType;
    }    

    public Set<BaseModule> getModules() {
        return modules;
    }

    public void setModules(Set<BaseModule> modules) {
        this.modules = modules;
    }
    
    
}
