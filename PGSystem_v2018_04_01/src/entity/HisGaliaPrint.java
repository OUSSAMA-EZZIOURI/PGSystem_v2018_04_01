package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import __main__.GlobalMethods;
import __main__.GlobalVars;
import gui.packaging.PackagingVars;
import helper.Helper;
import helper.HQLHelper;
import hibernate.DAO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * HisGaliaPrint generated by hbm2java
 */
@Entity
@Table(name = "his_galia_print")
public class HisGaliaPrint extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "his_galia_print_id_seq")
    @SequenceGenerator(name = "his_galia_print_id_seq", sequenceName = "his_galia_print_id_seq", allocationSize = 1)
    private Integer id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "write_time")
    private Date writeTime;

    @Column(name = "create_id")
    private int createId;

    @Column(name = "write_id")
    private int writeId;

    @Column(name = "harness_part")
    private String harnessPart;
    
    @Column(name = "supplier_name")
    private String supplierName;
    
    @Column(name = "supplier_part_number")
    private String supplierPartNumber;
    
    @Column(name = "harness_index")
    private String harnessIndex;
    
    @Column(name = "qty")
    private int qty;
    
    @Column(name = "closing_pallet")
    private String closingPallet;
    
    @Column(name = "print_state")    
    private String printState;
    
    @Column(name = "reprint")
    private String reprint;
    
    public HisGaliaPrint() {
    }

    public HisGaliaPrint(String harnessPart, String supplierName, String supplierPartNumber, String harnessIndex, int qty, String closingPalet, String state, String reprint) {
        this.createTime = this.writeTime = GlobalMethods.getTimeStamp(null);
        this.createId = this.writeId = PackagingVars.context.getUser().getId();
        this.harnessPart = harnessPart;
        this.supplierName = supplierName;
        this.supplierPartNumber = supplierPartNumber;
        this.harnessIndex = harnessIndex;
        this.qty = qty;
        this.closingPallet = GlobalVars.CLOSING_PALLET_PREFIX + closingPalet;
        this.printState = state;
        this.reprint = reprint;
    }
    
    public HisGaliaPrint(String harnessPart, String supplierName, String supplierPartNumber, String harnessIndex, int qty, String closingPalet, String state, int writeId, Date writeTime, String reprint) {
        this.writeTime = writeTime;
        this.writeId = writeId;
        this.harnessPart = harnessPart;
        this.supplierName = supplierName;
        this.supplierPartNumber = supplierPartNumber;
        this.harnessIndex = harnessIndex;
        this.qty = qty;
        this.closingPallet = GlobalVars.CLOSING_PALLET_PREFIX + closingPalet;
        this.printState = state;
        this.reprint = reprint;
    }
    
    public String getPrintState() {
        return printState;
    }

    public void setPrintState(String print_state) {
        this.printState = print_state;
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

    public String getHarnessPart() {
        return this.harnessPart;
    }

    public void setHarnessPart(String harnessPart) {
        this.harnessPart = harnessPart;
    }

    public String getSupplierName() {
        return this.supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierPartNumber() {
        return this.supplierPartNumber;
    }

    public void setSupplierPartNumber(String supplierPartNumber) {
        this.supplierPartNumber = supplierPartNumber;
    }

    public String getHarnessIndex() {
        return harnessIndex;
    }

    public void setHarnessIndex(String harnessIndex) {
        this.harnessIndex = harnessIndex;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getClosingPallet() {
        return this.closingPallet;
    }

    public void setClosingPallet(String closingPallet) {
        this.closingPallet = closingPallet;
    }

    public String getReprint() {
        return reprint;
    }

    public void setReprint(String reprint) {
        this.reprint = reprint;
    }        

    public String getCreateTimeString(String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);
        return (df.format(this.createTime));
    }

    public void setGaliaState(String state, int id) {
        Helper.sess.beginTransaction();
        Helper.sess.createQuery(HQLHelper.SET_CLOSING_SHEET_STATE)
                .setParameter("state", state)
                .setParameter("id", id)
                .executeUpdate();
        Helper.sess.getTransaction().commit();
//        HibernateUtil.close();
    }

    @Override
    public String toString() {
        return "HisGaliaPrint{" + "id=" + id + ", createTime=" + createTime + ", writeTime=" + writeTime + ", createId=" + createId + ", writeId=" + writeId + ", harnessPart=" + harnessPart + ", supplierName=" + supplierName + ", supplierPartNumber=" + supplierPartNumber + ", harnessIndex=" + harnessIndex + ", qty=" + qty + ", closingPalet=" + closingPallet + ", state=" + printState + '}';
    }

}
