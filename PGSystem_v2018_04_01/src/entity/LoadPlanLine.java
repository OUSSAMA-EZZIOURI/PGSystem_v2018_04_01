/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import hibernate.DAO;
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


@Entity
@Table(name = "load_plan_line")
public class LoadPlanLine extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "load_plan_line_id_seq")
    @SequenceGenerator(name = "load_plan_line_id_seq", sequenceName = "load_plan_line_id_seq", allocationSize = 1)
    private Integer id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_id")
    private int createId;

    @Column(name = "m_user")
    private String user;

    @Column(name = "pile_num")
    private Integer pileNum;

    @Column(name = "harness_type")
    private String harnessType;

    @Column(name = "pallet_number")
    private String palletNumber;

    @Column(name = "harness_part")
    private String harnessPart;

    @Column(name = "harness_index")
    private String harnessIndex;

    @Column(name = "supplier_part")
    private String supplierPart;

    @Column(name = "qty")
    private int qty;        
    
    @Column(name = "order_num")
    private String orderNum;
    
    @Column(name = "pack_type")
    private String packType;
    
    @Column(name = "destination_wh")
    private String destinationWh;
    
    @Column(name = "std_time", nullable = false, columnDefinition = "float default 0.00")
    private Double stdTime;

    @Column(name = "price", nullable = false, columnDefinition = "float default 0.00")
    private Double price;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "delivery_time")
    private Date deliveryTime;
    
    @Column(name = "load_plan_id", insertable = true ,updatable = true)
    private int loadPlanId;
    
    /**
     * Dispatch label serial number
     */
    @Column(name = "dispatch_label_no", nullable = true)
    private String dispatchLabelNo;
    
    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    private LoadPlan loadPlan;

    public LoadPlanLine() {
    }

    public LoadPlanLine(Date createTime, Date deliveryTime, 
            int createId, String user, 
            Integer pileNum, String harnessType, 
            String palletNumber, String harnessPart, 
            String harnessIndex, String supplierPart, 
            int qty, String orderNum, String packType, String destinationWh,
            Double stdTime, Double price, String dispatchLabelNo) {
        this.createTime = createTime;
        this.deliveryTime = deliveryTime;
        this.createId = createId;
        this.user = user;
        this.pileNum = pileNum;
        this.harnessType = harnessType;
        this.palletNumber = palletNumber;
        this.harnessPart = harnessPart;
        this.harnessIndex = harnessIndex;
        this.supplierPart = supplierPart;
        this.qty = qty;
        this.orderNum = orderNum;
        this.packType = packType; 
        this.destinationWh = destinationWh;
        this.stdTime = stdTime;
        this.price = price;
        this.dispatchLabelNo = dispatchLabelNo;
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

    public String getHarnessPart() {
        return harnessPart;
    }

    public void setHarnessPart(String harnessPart) {
        this.harnessPart = harnessPart;
    }

    public String getSupplierPart() {
        return supplierPart;
    }

    public void setSupplierPart(String supplierPart) {
        this.supplierPart = supplierPart;
    }    
    
    public Integer getPileNum() {
        return pileNum;
    }

    public void setPileNum(Integer pileNum) {
        this.pileNum = pileNum;
    }

    public String getHarnessType() {
        return harnessType;
    }

    public void setHarnessType(String harnessType) {
        this.harnessType = harnessType;
    }

    public String getPalletNumber() {
        return palletNumber;
    }

    public void setPalletNumber(String palletNumber) {
        this.palletNumber = palletNumber;
    }

    public String getHarnessIndex() {
        return harnessIndex;
    }

    public void setHarnessIndex(String harnessIndex) {
        this.harnessIndex = harnessIndex;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
    
    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public LoadPlan getLoadPlan() {
        return loadPlan;
    }

    public void setLoadPlan(LoadPlan loadPlan) {
        this.loadPlan = loadPlan;
    }

    public int getLoadPlanId() {
        return loadPlanId;
    }

    public void setLoadPlanId(int loadPlanId) {
        this.loadPlanId = loadPlanId;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }    

    public String getDestinationWh() {
        return destinationWh;
    }

    public void setDestinationWh(String destinationWh) {
        this.destinationWh = destinationWh;
    }

    public Double getStdTime() {
        return stdTime;
    }

    public void setStdTime(Double stdTime) {
        this.stdTime = stdTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDispatchLabelNo() {
        return dispatchLabelNo;
    }

    public void setDispatchLabelNo(String dispatchLabelNo) {
        this.dispatchLabelNo = dispatchLabelNo;
    }
    
    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.        
    }
    

    

}
