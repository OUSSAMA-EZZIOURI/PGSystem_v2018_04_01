package entity;

import hibernate.DAO;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "manufacture_users")
public class ManufactureUsers extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manufacture_users_id_seq")
    @SequenceGenerator(name = "manufacture_users_id_seq", sequenceName = "manufacture_users_id_seq", allocationSize = 1)
    private Integer id;
    
    @Column(name="first_name")
    private String firstName;
    
    @Column(name="last_name")
    private String lastName;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="create_time")
    private Date createTime;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name="write_time")
    private Date writeTime;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)    
    @Column(name="login_time")
    private Date loginTime;
    
    @Column(name="create_id")
    private int createId;
    
    @Column(name="write_id")
    private int writeId;
    
    @Column(name="login")
    private String login;
    
    @Column(name="password")
    private String password;
    
    @Column(name="access_level")
    private int accessLevel;
    
    @Column(name="active")
    private int active;
    
    @Column(name="harness_type")
    private String harnessType;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "base_user_module_rel", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "module_id") })
    
    private Set<BaseModule> modules = new HashSet<BaseModule>(0);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public int getCreateId() {
        return createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public int getWriteId() {
        return writeId;
    }

    public void setWriteId(int writeId) {
        this.writeId = writeId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
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

    @Override
    public String toString() {
        return "ManufactureUsers{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", createTime=" + createTime + ", writeTime=" + writeTime + ", createId=" + createId + ", writeId=" + writeId + ", login=" + login + ", password=" + password + ", accessLevel=" + accessLevel + ", active=" + active + ", harnessType=" + harnessType + '}';
    }

}