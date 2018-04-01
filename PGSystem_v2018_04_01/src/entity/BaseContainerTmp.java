package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import javax.persistence.Column;


/**
 * BaseContainerTmp : Used to manage baseContainer temporary choices
 */
public class BaseContainerTmp implements java.io.Serializable {

    private String palletNumber;
    private String harnessPart;
    private String harnessIndex;
    private String supplierPartNumber;
    private Integer qtyExpected;
    private Integer qtyRead;
    private String state;
    private String packType;
    private String choosen_pack_type;
    private String hernessCounter;
    private String harnessType;
    private Double stdTime;    
    private Double price; 
    private String packWorkstation;    
    private String assyWorkstation;
    private Integer assyWorkstationId;    
    private String segment;
    private String workplace;  
    private String order_no;
    private Integer special_order;
    private Integer ucsLifes;    
    private Integer ucsId;
    private String comment;    
    private String dispatchLabelNo;
    
    
    public BaseContainerTmp() {
    }    
    
    public String getPalletNumber() {
        return palletNumber;
    }

    public void setPalletNumber(String palletNumber) {
        this.palletNumber = palletNumber;
    }

    public String getHarnessPart() {
        return harnessPart;
    }

    public void setHarnessPart(String harnessPart) {
        this.harnessPart = harnessPart;
    }

    public String getHarnessIndex() {
        return harnessIndex;
    }

    public void setHarnessIndex(String harnessIndex) {
        this.harnessIndex = harnessIndex;
    }

    public String getSupplierPartNumber() {
        return supplierPartNumber;
    }

    public void setSupplierPartNumber(String supplierPartNumber) {
        this.supplierPartNumber = supplierPartNumber;
    }

    public Integer getQtyExpected() {
        return qtyExpected;
    }

    public void setQtyExpected(Integer qtyExpected) {
        this.qtyExpected = qtyExpected;
    }

    public Integer getQtyRead() {
        return qtyRead;
    }

    public void setQtyRead(Integer qtyRead) {
        this.qtyRead = qtyRead;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getChoosen_pack_type() {
        return choosen_pack_type;
    }

    public void setChoosen_pack_type(String choosen_pack_type) {
        this.choosen_pack_type = choosen_pack_type;
    }

    public String getHernessCounter() {
        return hernessCounter;
    }

    public void setHernessCounter(String hernessCounter) {
        this.hernessCounter = hernessCounter;
    }

    public String getHarnessType() {
        return harnessType;
    }

    public void setHarnessType(String harnessType) {
        this.harnessType = harnessType;
    }        

    public Double getStdTime() {
        return stdTime;
    }

    public void setStdTime(Double stdTime) {
        this.stdTime = stdTime;
    }

    public String getPackWorkstation() {
        return packWorkstation;
    }

    public void setPackWorkstation(String packWorkstation) {
        this.packWorkstation = packWorkstation;
    }

    public String getAssyWorkstation() {
        return assyWorkstation;
    }

    public void setAssyWorkstation(String assyWorkstation) {
        this.assyWorkstation = assyWorkstation;
    }

    public Integer getAssyWorkstationId() {
        return assyWorkstationId;
    }

    public void setAssyWorkstationId(Integer assyWorkstationId) {
        this.assyWorkstationId = assyWorkstationId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Integer getSpecial_order() {
        return special_order;
    }

    public void setSpecial_order(Integer special_order) {
        this.special_order = special_order;
    }
    
    public Integer getUcsLifes() {
        return ucsLifes;
    }

    public void setUcsLifes(Integer ucsLifes) {
        this.ucsLifes = ucsLifes;
    }
    
    public Integer getUcsId() {
        return ucsId;
    }

    public void setUcsId(Integer ucsId) {
        this.ucsId = ucsId;
    }

    public String getDispatchLabelNo() {
        return dispatchLabelNo;
    }

    public void setDispatchLabelNo(String dispatchLabelNo) {
        this.dispatchLabelNo = dispatchLabelNo;
    }        
    
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BaseContainerTmp{" + "palletNumber=" + palletNumber + ", harnessPart=" + harnessPart + ", harnessIndex=" + harnessIndex + ", supplierPartNumber=" + supplierPartNumber + ", qtyExpected=" + qtyExpected + ", qtyRead=" + qtyRead + ", state=" + state + ", packType=" + packType + ", choosen_pack_type=" + choosen_pack_type + ", hernessCounter=" + hernessCounter + ", harnessType=" + harnessType + ", stdTime=" + stdTime + ", price=" + price + ", packWorkstation=" + packWorkstation + ", assyWorkstation=" + assyWorkstation + ", assyWorkstationId=" + assyWorkstationId + ", segment=" + segment + ", workplace=" + workplace + ", order_no=" + order_no + ", special_order=" + special_order + ", ucsLifes=" + ucsLifes + ", ucsId=" + ucsId + ", comment=" + comment + ", dispatchLabelNo=" + dispatchLabelNo + '}';
    }

}
