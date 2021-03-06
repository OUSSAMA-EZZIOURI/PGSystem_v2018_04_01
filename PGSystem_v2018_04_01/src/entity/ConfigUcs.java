package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import __main__.GlobalMethods;
import __main__.GlobalVars;
import gui.packaging.PackagingVars;
import helper.Helper;
import java.util.List;
import org.hibernate.Query;
import helper.HQLHelper;
import hibernate.DAO;
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
import ui.UILog;
import ui.error.ErrorMsg;
import ui.info.InfoMsg;

/**
 * ConfigUcs generated by hbm2java
 */
@Entity
@Table(name = "config_ucs")
public class ConfigUcs extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_ucs_id_seq")
    @SequenceGenerator(name = "config_ucs_id_seq", sequenceName = "config_ucs_id_seq", allocationSize = 1)
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

    @Column(name = "harness_index")
    private String harnessIndex;

    @Column(name = "supplier_part_number")
    private String supplierPartNumber;

    @Column(name = "harness_part")
    private String harnessPart;

    @Column(name = "harness_type")
    private String harnessType;

    @Column(name = "pack_type")
    private String packType;

    @Column(name = "pack_size")
    private int packSize;

    @Column(name = "active")
    private int active;

    @Column(name = "std_time")
    private double stdTime;

    @Column(name = "price")
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name = "additional_barcode", nullable = true)
    private int additionalBarcode;

    @Column(name = "assy_workstation")
    private String assyWorkstationName;

    /**
     * Comment in case of specific orders
     */
    @Column(name = "comment")
    private String comment;

    /**
     * Is the global area that englob many carrousels or fixed boards
     */
    @Column(name = "segment")
    private String segment;

    /**
     * Is the area where the harness is been produced, it can be a fixed board
     * or a carrousel
     */
    @Column(name = "workplace")
    private String workplace;

    /**
     * Order number of the packaging, got from FORS with mask KUOT
     *
     */
    @Column(name = "order_no", nullable = true)
    private String orderNo;

    /**
     * How many times a data row can appear in the Ucs menu if -1 = forever if
     * great than 0, it will decreased each time a pallet is closed.
     */
    @Column(name = "lifes")
    private int lifes;

    /**
     * Packaging for special orders, used to print a specific packaging label 1
     * = Specific order 0 = Standard order
     */
    @Column(name = "special_order", nullable = true)
    private int specialOrder;

    //@ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    //private AssyWorkstation assyWorkstation;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ucs", cascade = CascadeType.ALL)
    private Set<ConfigBarcode> barCodeList = new HashSet<ConfigBarcode>(0);

    public ConfigUcs() {
    }

    public ConfigUcs(String harnessIndex, String supplierPartNumber,
            String harnessPart, String harnessType,
            String packType, int packSize, int active, int additionalBarcode, double stdTime, String comment, int special_order, String order_no, double price) {
        this.setDefautlVals();
        this.harnessIndex = harnessIndex;
        this.supplierPartNumber = supplierPartNumber;
        this.harnessPart = harnessPart;
        this.harnessType = harnessType;
        this.packType = packType;
        this.packSize = packSize;
        this.active = active;
        this.stdTime = stdTime;
        this.specialOrder = special_order;
        this.orderNo = order_no;
        this.additionalBarcode = additionalBarcode;
        this.comment = comment;
        this.price = price;
    }

    public ConfigUcs setDefautlVals() {
        /*
         Set default values of this object 
         from the global mode2_context values
         */
        this.createTime = this.writeTime = GlobalMethods.getTimeStamp(null);
        this.createId = this.writeId = PackagingVars.context.getUser().getId();
        this.additionalBarcode = 0;
        return this;
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

    public String getHarnessIndex() {
        return this.harnessIndex;
    }

    public void setHarnessIndex(String harnessIndex) {
        this.harnessIndex = harnessIndex;
    }

    public String getSupplierPartNumber() {
        return this.supplierPartNumber;
    }

    public void setSupplierPartNumber(String supplierPartNumber) {
        this.supplierPartNumber = supplierPartNumber;
    }

    public String getHarnessPart() {
        return this.harnessPart;
    }

    public void setHarnessPart(String harnessPart) {
        this.harnessPart = harnessPart;
    }

    public String getPackType() {
        return this.packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public int getPackSize() {
        return this.packSize;
    }

    public void setPackSize(int packSize) {
        this.packSize = packSize;
    }

    public String getHarnessType() {
        return harnessType;
    }

    public void setHarnessType(String harnessType) {
        this.harnessType = harnessType;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getAdditionalBarcode() {
        return additionalBarcode;
    }

    public void setAdditionalBarcode(int additionalBarcode) {
        this.additionalBarcode = additionalBarcode;
    }

    public Set<ConfigBarcode> getBarCodeList() {
        return barCodeList;
    }

    public void setBarCodeList(Set<ConfigBarcode> barCodeList) {
        this.barCodeList = barCodeList;
    }

    public double getStdTime() {
        return stdTime;
    }

    public void setStdTime(double stdTime) {
        this.stdTime = stdTime;
    }

    public String getAssyWorkstationName() {
        return assyWorkstationName;
    }

    public void setAssyWorkstationName(String assyWorkstationName) {
        this.assyWorkstationName = assyWorkstationName;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getSpecialOrder() {
        return specialOrder;
    }

    public void setSpecialOrder(int specialOrder) {
        this.specialOrder = specialOrder;
    }

    //######################################################################
    public List select(String hp) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HP_ACTIVE);
        query.setParameter("hp", hp);

        Helper.sess.getTransaction().commit();
        return query.list();
    }

    public Object select(int id) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_ID);
        query.setParameter("id", id);

        Helper.sess.getTransaction().commit();
        if (!query.list().isEmpty()) {
            return (ConfigUcs) query.list().get(0);
        }
        return null;
    }

    public List getHarnessList(String harnessType) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_PROJECT);
        query.setParameter("harnessType", harnessType);
        Helper.sess.getTransaction().commit();
        return query.list();
    }

    public List getSupplierPartList(String harnessType, String harnessPart) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HP_AND_HARNESS_TYPE);
        query.setParameter("harnessType", harnessType);
        query.setParameter("harnessPart", harnessPart);
        Helper.sess.getTransaction().commit();
        return query.list();
    }

    public List getIndexList(String harnessType, String harnessPart, String supplierPartNumber) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HP_AND_HARNESS_TYPE_AND_SUPPLIER);
        query.setParameter("harnessType", harnessType);
        query.setParameter("harnessPart", harnessPart);
        query.setParameter("supplierPartNumber", supplierPartNumber);
        Helper.sess.getTransaction().commit();
        return query.list();
    }

    public List getPackSizeList(String harnessType, String harnessPart, String supplierPartNumber, String harnessIndex, String packType) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HARNESS_TYPE_AND_HARNESS_PART_AND_SUPPLIER_AND_INDEX_AND_PACK_TYPE);
        query.setParameter("harnessType", harnessType);
        query.setParameter("harnessPart", harnessPart);
        query.setParameter("supplierPartNumber", supplierPartNumber);
        query.setParameter("harnessIndex", harnessIndex);
        query.setParameter("packType", packType);
        Helper.sess.getTransaction().commit();
        return query.list();
    }

    public List getPackTypeList(String harnessType, String harnessPart, String supplierPartNumber, String harnessIndex) {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HARNESS_TYPE_AND_HARNESS_PART_AND_SUPPLIER_AND_INDEX);
        query.setParameter("harnessType", harnessType);
        query.setParameter("harnessPart", harnessPart);
        query.setParameter("supplierPartNumber", supplierPartNumber);
        query.setParameter("harnessIndex", harnessIndex);

        Helper.sess.getTransaction().commit();
        return query.list();
    }

    public static Boolean isHarnessPartExist(String harnessPart, String harnessType) {
        //Tester le format du harness part
        System.out.println("Global.HARN_PART_PREFIX " + GlobalVars.HARN_PART_PREFIX);
        if (harnessPart.substring(0, 1).equals(GlobalVars.HARN_PART_PREFIX)) {
            String[] part = harnessPart.split(GlobalVars.HARN_PART_PREFIX);
            harnessPart = part[1];
        }

        UILog.info("Searching Harness part [%s] in ConfigUCS: [%s]",
                harnessPart, harnessType);
        Helper.startSession();
        //Récupérer la list des données UCS qui corresponds au harness_part
        Query query = Helper.sess.createQuery(HQLHelper.GET_UCS_BY_HP_AND_HARNESS_TYPE);
        query.setParameter("harnessPart", harnessPart.trim());
        query.setParameter("harnessType", harnessType);
        Helper.sess.getTransaction().commit();
        List result = query.list();
        if (!result.isEmpty()) {
            //Helper.log.info(InfoMsg.APP_INFO0004, String.format(Helper.INFO0003_HP_FOUND, harnessPart, harnessType));
            UILog.info(InfoMsg.APP_INFO0004[0], harnessPart, harnessType);
            return true;
        } else {
            //Helper.log.info(String.format(Helper.ERR0004_HP_NOT_FOUND, harnessPart));
            UILog.severe(ErrorMsg.APP_ERR0005[0], harnessPart, harnessType);
            return false;
        }

    }

}
