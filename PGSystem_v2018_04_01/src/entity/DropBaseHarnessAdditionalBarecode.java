package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import __main__.GlobalMethods;
import gui.packaging.PackagingVars;
import helper.Helper;
import org.hibernate.Query;
import helper.HQLHelper;
import hibernate.DAO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 * BaseHarness generated by hbm2java
 */
@Entity
@Table(name = "drop_base_harness_additional_barcode")
public class DropBaseHarnessAdditionalBarecode extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drop_base_harness_additional_barcode_id_seq")
    @SequenceGenerator(name = "drop_base_harness_additional_barcode_id_seq", sequenceName = "drop_base_harness_additional_barcode_id_seq", allocationSize = 1)
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

    @Column(name = "label_code")
    private String labelCode;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    private DropBaseHarness harness;

    public DropBaseHarnessAdditionalBarecode() {
    }

    public DropBaseHarnessAdditionalBarecode setDefautlVals() {
        /*
         Set default values of this object 
         from the global mode2_context values
         */
        this.createTime = GlobalMethods.getTimeStamp(null);
        this.writeTime = GlobalMethods.getTimeStamp(null);
        this.createId = PackagingVars.context.getUser().getId();
        this.writeId = PackagingVars.context.getUser().getId();

        return this;
    }

    public DropBaseHarnessAdditionalBarecode(String labelCode, DropBaseHarness harness, int harnessId) {
        this.setDefautlVals();
        this.labelCode = labelCode;
        this.harness = harness;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }

    public DropBaseHarness getHarness() {
        return harness;
    }

    public void setHarness(DropBaseHarness harness) {
        this.harness = harness;
    }

    //######################################################################    
    public static boolean isLabelCodeExist(String labelCode) {
        //Tester si le harness counter exist dans la base BaseHarness        
        Helper.log.info("Searching Additional Barcodes [" + labelCode + "] in BaseHarnessAdditionalBarecode.");

        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ADDITIONAL_BARCODES_BY_CODE);
        query.setParameter("labelCode", labelCode);
        Helper.log.info(query.getQueryString());
        Helper.sess.getTransaction().commit();

        if (query.list().size() == 0) {
            //Great!!! Is a new Label Code
            return false;
        } else {
            UILog.severeDialog(null, ErrorMsg.APP_ERR0013, "" + labelCode);
            UILog.severe(ErrorMsg.APP_ERR0013[0], "" + labelCode);
            return true;

        }
    }

    @SuppressWarnings("UnusedAssignment")
    public String getCreateTimeString(String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(this.createTime);
    }

}
