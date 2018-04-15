/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

/**
 *
 * @author Administrator
 */
public class HQLHelper {

    //ManufactureUsers
    public final static String CHECK_LOGIN_ONLY = "FROM ManufactureUsers mu WHERE mu.login = :login";
    public final static String CHECK_LOGIN_PASS = "FROM ManufactureUsers mu WHERE mu.login = :login AND mu.password = :password";
    public final static String CHECK_LOGIN_PASS_LEVEL = "FROM ManufactureUsers mu WHERE mu.login = :login AND mu.password = :password AND mu.accessLevel IN (:accessLevels)";
    public final static String CHECK_LOGIN = "FROM ManufactureUsers mu WHERE mu.login = :login AND mu.active = :active AND mu.harnessType = :harnessType";
    public final static String CHECK_LOGIN_PASSWORD_IN_PROJECT = "FROM ManufactureUsers mu WHERE mu.login = :login AND mu.password = :password";
    public final static String GET_USER_BY_LOGIN = "FROM ManufactureUsers mu WHERE mu.login = :login";
    public final static String GET_USER_BY_ID = "FROM ManufactureUsers mu WHERE mu.id = :id";
    //BaseContainer
    public final static String GET_ALL_CONTAINER = "FROM BaseContainer bc ORDER BY createTime DESC";
    public final static String GET_CONTAINER_BY_NUMBER = "FROM BaseContainer bc WHERE bc.palletNumber = :palletNumber";
    public final static String GET_CONTAINER_BY_FORS_SERIAL = "FROM BaseContainer bc WHERE bc.dispatchLabelNo = :dispatchLabelNo";
    public final static String GET_CONTAINER_BY_NUMBER_AND_FORS_SERIAL = "FROM BaseContainer bc WHERE bc.palletNumber = :palletNumber AND bc.dispatchLabelNo = :dispatchLabelNo";
    public final static String GET_CONTAINER_BY_NAME_AND_STATE = "FROM BaseContainer bc WHERE bc.palletNumber = :palletNumber AND bc.containerState = :state";
    public final static String GET_CONTAINER_BY_STATE = "FROM BaseContainer bc WHERE bc.containerState = :state ORDER BY createTime DESC";
    public final static String GET_CONTAINER_BY_STATES = "FROM BaseContainer bc WHERE bc.containerState IN (:states) ORDER BY createTime DESC";
    public final static String GET_CONTAINER_BY_STATES_AND_PROJECTS = "FROM BaseContainer bc WHERE bc.containerState IN (:states) AND bc.harnessType IN (:projects) ORDER BY createTime DESC";
    public final static String GET_CONTAINER_BY_STATES_AND_PROJECTS_AND_CREATETIME = "FROM BaseContainer bc WHERE bc.containerState IN (:states) AND bc.harnessType IN (:projects) AND createTime BETWEEN :start_time AND :end_time ORDER BY createTime DESC ";
    public final static String SET_CONTAINER_QTY_READ = "UPDATE BaseContainer bc SET bc.qtyRead = :qtyRead WHERE bc.id = :id";
    public final static String SET_CONTAINER_STATE_TO_STORED = "UPDATE BaseContainer bc SET bc.containerState = :containerState, bc.containerStateCode = :containerStateCode, bc.fifoTime = :fifoTime, bc.storedTime = :storedTime WHERE bc.palletNumber = :palletNumber";
    public final static String SET_CONTAINER_STATE_TO_SHIPPED = "UPDATE BaseContainer bc SET bc.containerState = :containerState, bc.containerStateCode = :containerStateCode, bc.fifoTime = :fifoTime, bc.fifoTime = :shippedTime  WHERE bc.palletNumber = :palletNumber";
    
    //DropBaseContainer
    public final static String GET_DROPPED_CONTAINER_BY_NUMBER = "FROM DropBaseContainer bc WHERE bc.palletNumber = :palletNumber";
    //BaseHarness
    public final static String DEL_HP_BY_COUNTER = "DELETE FROM BaseHarness bh WHERE bh.counter = :counter";
    public final static String GET_HP_BY_COUNTER = "FROM BaseHarness bh WHERE bh.counter = :counter";
    public final static String GET_HP_BY_HP = "FROM BaseHarness bh WHERE bh.harnessPart = :hp";
    public final static String GET_HP_BY_PALLET_NUMBER = "FROM BaseHarness bh WHERE bh.palletNumber = :palletNumber";
    //BaseHarnessAdditionalBarecode
    public final static String GET_ADDITIONAL_BARCODES_BY_CODE = "FROM BaseHarnessAdditionalBarecode bel WHERE bel.labelCode = :labelCode ORDER BY id ASC";
    public final static String DEL_LABEL_BY_HP_ID = "DELETE FROM BaseHarnessAdditionalBarecode bel WHERE bel.harnessId = :harnessId";
    //ConfigUCS
    public final static String GET_UCS_BY_ID = "FROM ConfigUcs cu WHERE cu.id = :id";
    public final static String GET_UCS_BY_HP = "FROM ConfigUcs cu WHERE cu.harnessPart = :hp";
    public final static String GET_UCS_BY_HP_ACTIVE = "FROM ConfigUcs cu WHERE cu.harnessPart = :hp AND cu.active = 1 ORDER BY specialOrder DESC";
    public final static String GET_UCS_BY_PROJECT = "FROM ConfigUcs cu WHERE cu.harnessType = :harnessType";
    public final static String GET_UCS_BY_HARNESS_TYPE_AND_HARNESS_PART_AND_SUPPLIER_AND_INDEX_AND_PACK_TYPE = "FROM ConfigUcs cu WHERE cu.harnessType = :harnessType AND cu.harnessPart = :harnessPart AND cu.supplierPartNumber = :supplierPartNumber AND cu.harnessIndex = :harnessIndex AND cu.packType = :packType";
    public final static String GET_UCS_BY_HARNESS_TYPE_AND_HARNESS_PART_AND_SUPPLIER_AND_INDEX = "FROM ConfigUcs cu WHERE cu.harnessType = :harnessType AND cu.harnessPart = :harnessPart AND cu.supplierPartNumber = :supplierPartNumber AND cu.harnessIndex = :harnessIndex";
    public final static String GET_UCS_BY_HP_AND_HARNESS_TYPE_AND_SUPPLIER = "FROM ConfigUcs cu WHERE cu.harnessType = :harnessType AND cu.harnessPart = :harnessPart AND cu.supplierPartNumber = :supplierPartNumber";
    public final static String GET_UCS_BY_HP_AND_HARNESS_TYPE = "FROM ConfigUcs cu WHERE cu.harnessPart = :harnessPart AND cu.harnessType = :harnessType";
    public final static String GET_UCS_BY_SUPPLIER_PART = "FROM ConfigUcs cu WHERE cu.supplierPartNumber = :sp";
    public final static String GET_UCS_BY_HP_AND_SUPPLIER_PART_AND_INDEX_PACKTYPE_AND_PACKSIZE = "FROM ConfigUcs cu WHERE cu.harnessPart = :harnessPart AND cu.supplierPartNumber = :supplierPartNumber AND cu.harnessIndex = :harnessIndex  AND cu.packType = :packType AND cu.packSize = :packSize ORDER BY id DESC";
    //ConfigProject
    public final static String GET_ALL_PROJECT = "FROM ConfigProject bp ORDER BY harnessType ASC";    
    public final static String GET_PROJECT_BY_HARNESSTYPE = "FROM ConfigProject bp WHERE harnessType = :harnessType ORDER BY harnessType ASC";
    //ConfigSegment
    public final static String GET_ALL_SEGMENTS = "FROM ConfigSegment bp ORDER BY segment ASC";
    //ConfigSubSegment
    public final static String GET_ALL_SUB_SEGMENTS = "FROM ConfigSubSegment bp ORDER BY subSegment ASC";
    //ConfigBookingType
    public final static String GET_All_BOOKING_TYPE = "FROM ConfigBookingType ct";
    public final static String GET_BOOKING_TYPE = "FROM ConfigBookingType ct WHERE ct.type = :type ";
    public final static String GET_BOOKING_TYPE_BY_CODE = "FROM ConfigBookingType ct WHERE ct.code = :code ";

    //ConfigWarehouse
    public final static String GET_All_WAREHOUSES = "FROM ConfigWarehouse";
    public final static String GET_WAREHOUSE = "FROM ConfigWarehouse cw WHERE cw.warehouse = :warehouse";

    //PackagingConfig
    public final static String GET_ALL_CONFIGS = "FROM PackagingConfig pc ORDER BY packMaster ASC";
    public final static String GET_CONFIG_BY_ID = "FROM PackagingConfig pc WHERE id = :id";
    public final static String GET_CONFIG_ITEMS_BY_PACKMASTER = "FROM PackagingConfig pc WHERE pc.packMaster = :packMaster";
    public final static String DEL_CONFIG_OF_PACK_MASTER = "DELETE FROM PackagingConfig pm WHERE pm.packMaster = :packMaster";
    public final static String DEL_CONFIG_OF_PACK_ITEM = "DELETE FROM PackagingConfig pi WHERE pi.packItem = :packItem";
    //PackagingMaster
    public final static String GET_ALL_PACK_MASTER = "FROM PackagingMaster pm ORDER BY pm.packMaster ASC";
    public final static String GET_PACK_MASTER = "FROM PackagingMaster pm WHERE pm.packMaster = :packMaster ";
    public final static String GET_PACK_MASTER_BY_ID = "FROM PackagingMaster pm WHERE pm.id = :id ";

    //PackagingItems
    public final static String GET_ALL_PACK_ITEMS = "FROM PackagingItems ORDER BY packItem ASC";
    public final static String GET_PACK_ITEM_BY_NAME = "FROM PackagingItems pi WHERE pi.packItem = :packItem ";
    public final static String GET_PACK_ITEM_BY_ID = "FROM PackagingItems pi WHERE pi.id = :id ";
    public final static String SET_PACKAGING_ITEMS_QTY = "UPDATE PackagingItems pi SET pi.qty = pi.qty - :qty WHERE pi.packItem = :packItem";
    //
    //public final static String GET_ITEM_ON_STOCK = "FROM PackagingItems pi WHERE pi.packItem = :packItem ";    

    //ConfigWorkplace
    public final static String GET_ALL_WORKPLACES = "FROM ConfigWorkplace bp ORDER BY workplace ASC";
    public final static String GET_WORKPLACES_BY_SEGMENT = "FROM ConfigWorkplace bp WHERE segment = :segment ORDER BY workplace ASC";
    //ConfigBarcode
    public final static String GET_PATTERN_BY_ID = "FROM ConfigBarcode cb WHERE cb.id = :id";
    public final static String GET_PATTERN_BY_KEYWORD_AND_HARNESSTYPE = "FROM ConfigBarcode cb WHERE cb.keyWord = :keyWord AND cb.harness_type = :harnessType ORDER BY id ASC";
    public final static String GET_PATTERN_BY_KEYWORD = "FROM ConfigBarcode cb WHERE cb.keyWord = :keyWord ORDER BY id ASC";    
    public final static String GET_PATTERN_BY_KEYWORDS = "FROM ConfigBarcode cb WHERE cb.keyWord IN (:keyWords) ORDER BY id ASC";    
    public final static String GET_PATTERN_BY_HARNESSPART = "FROM ConfigBarcode cb WHERE cb.harnessPart = :harnessPart ORDER BY id ASC";
    public final static String GET_PATTERNS_BY_PROJECT = "FROM ConfigBarcode cb WHERE cb.project = :project ORDER BY id ASC";
    //AssyWorkstation
    public final static String GET_ALL_ASSY_WORKSTATIONS = "FROM AssyWorkstation aw ORDER BY workstation ASC";
    //ConfigShift
    public final static String GET_ALL_SHIFT = "FROM ConfigShift cs ORDER BY startHour ASC";
    public final static String GET_SHIFT_BY_DESC = "FROM ConfigShift cs WHERE cs.description = :description";
    public final static String GET_SHIFT_BY_ID = "FROM ConfigShift cs WHERE cs.id = :id";
    //HisBaseContainer
    public final static String GET_HIS_CONTAINER = "FROM HisBaseContainer hbc WHERE hbc.palletNumber = :palletNumber ORDER BY hbc.createTime";
    //HisPalletPrint
    public final static String SET_OPEN_SHEET_STATE = "UPDATE HisPalletPrint hp SET hp.printState = :state WHERE hp.id = :id";
    public final static String SET_OPEN_SHEET_REPRINT = "UPDATE HisPalletPrint hp SET hp.printState = :state, hp.reprint = :reprint, hp.writeTime = :writeTime, hp.writeId = :writeId WHERE hp.id = :id";
    public final static String GET_OPEN_SHEET = "FROM HisPalletPrint hp WHERE hp.id = :id";
    public final static String DEL_OPEN_SHEET = "DELETE FROM HisPalletPrint hp WHERE hp.id = :id";
    //HisGaliaPrint
    public final static String SET_CLOSING_SHEET_STATE = "UPDATE HisGaliaPrint hp SET hp.printState = :state WHERE hp.id = :id";
    public final static String GET_CLOSING_SHEET = "FROM HisGaliaPrint hp WHERE hp.closingPallet = :closingPallet";
    public final static String DEL_CLOSING_SHEET = "DELETE FROM HisGaliaPrint hp WHERE hp.closingPallet = :closingPallet";
    //Schedul Entry
    public final static String GET_ALL_SCHEDULE_ENTRIES = "FROM ScheduleEntry se ORDER BY se.harnessType, se.plannedDay DESC";

    //DispatchNote
    public final static String GET_DISPATCH_NOTE_BY_ADVICE_NOTE_NUM = "FROM DispatchNote dn WHERE dn.adviceNoteNum = :advice_note_num";

    //DispatchNoteLine
    
    public final static String GET_DISPATCH_LINE_BY_ADVICE_NOTE_NUM = "FROM DispatchNoteLine dn WHERE dn.adviceNoteNum = :advice_note_num";

    //LoadPlanDestinationRel
    public final static String GET_FINAL_DESTINATIONS_OF_PLAN = "FROM LoadPlanDestinationRel l WHERE loadPlanId = :loadPlanId";
    public final static String GET_ONE_FINAL_DESTINATION_OF_PLAN = "FROM LoadPlanDestinationRel l WHERE loadPlanId = :loadPlanId AND destination = :destination";
    
    //LoadPlanDestination
    public final static String GET_ALL_FINAL_DESTINATIONS = "FROM LoadPlanDestination l";
    public final static String GET_LOAD_PLAN_DEST = "FROM LoadPlanDestination l WHERE commun = :commun";

    //LoadPlan
    public final static String GET_LOAD_PLAN_BY_TIME = "FROM LoadPlan l WHERE l.createTime = :createTime AND l.deliveryTime = :deliveryTime";
    public final static String GET_LOAD_PLAN_BY_ID = "FROM LoadPlan l WHERE l.id = :id";
    public final static String GET_LOAD_ALL_PLANS = "FROM LoadPlan l ORDER BY id DESC";

    //LoadPlanLine
    public final static String GET_NBRE_LOAD_PLAN_LINES = "FROM LoadPlanLine lpl WHERE lpl.loadPlanId = :loadPlanId AND lpl.destinationWh = :destinationWh";
    public final static String GET_LOAD_PLAN_LINE_BY_ID = "FROM LoadPlanLine lpl WHERE lpl.id = :id";
    public final static String DEL_LOAD_PLAN_LINE_BY_PLAN_ID = "DELETE FROM LoadPlanLine lpl WHERE lpl.loadPlanId = :load_plan_id";
    public final static String GET_LOAD_PLAN_LINE_BY_PLAN_ID_AND_WH = "FROM LoadPlanLine lpl WHERE lpl.loadPlanId = :loadPlanId AND lpl.destinationWh = :destinationWh ORDER BY destinationWh ASC, pileNum DESC, id DESC";
    public final static String GET_LOAD_PLAN_LINE_BY_PLAN_ID_AND_DEST_AND_PN_AND_PILE = "FROM LoadPlanLine lpl WHERE lpl.loadPlanId = :loadPlanId AND lpl.destinationWh = :destinationWh AND harnessPart = :harnessPart AND lpl.pileNum = :pileNum ORDER BY destinationWh ASC, pileNum DESC, id DESC";
    public final static String GET_LOAD_PLAN_LINE_BY_PLAN_ID = "FROM LoadPlanLine lpl WHERE lpl.loadPlanId = :loadPlanId ORDER BY destinationWh ASC, pileNum DESC, id DESC";
    public final static String GET_LOAD_PLAN_LINE_BY_PLAN_ID_ASC = "FROM LoadPlanLine lpl WHERE lpl.loadPlanId = :loadPlanId ORDER BY destinationWh ASC, pileNum ASC, id ASC";
    public final static String GET_LOAD_PLAN_LINE_BY_DISPATCH_LABEL_NO = "FROM LoadPlanLine lpl WHERE lpl.dispatchLabelNo = :dispatch_label_no";    
    public final static String GET_LOAD_PLAN_LINE_GROUPED_BY_PILES = "SELECT "
            + " line.harness_type AS harness_type, "
            + " line.pile_num as pile_num,          "
            + " line.harness_part AS harness_part, "
            + " line.harness_index AS harness_index,"
            + " line.supplier_part AS supplier_part,"
            + " SUM(line.qty) AS total_qty,"
            + " line.qty AS qty,"
            + " line.pack_type AS pack_type, "
            + " SUM(line.qty)/line.qty AS nbre_pack, "
            + " line.order_num AS order_num,"
            + " line.destination_wh AS destination_wh "
            + " FROM load_plan_line line"
            + " WHERE load_plan_id = '%s'"
            + " GROUP BY harness_type, pile_num, harness_part, harness_index, supplier_part, qty, pack_type, order_num, destination_wh"
            + " ORDER BY destination_wh ASC, pile_num ASC";            
    public final static String GET_LOAD_PLAN_STOCK_PER_FDP = "SELECT\n"
            + "line.harness_type AS harness_type,   \n"
            + "line.harness_part AS harness_part,\n"
            + "line.harness_index AS harness_index,\n"
            + "line.supplier_part AS supplier_part,\n"
            + "SUM(line.qty) AS total_qty,    \n"
            + "line.order_num AS order_num,\n"
            + "line.destination_wh AS destination_wh\n"
            + "FROM load_plan_line line\n"
            + "WHERE load_plan_id = '%s'\n"
            + "GROUP BY harness_type, harness_part, harness_index, supplier_part, order_num, destination_wh\n"
            + "ORDER BY destination_wh ASC;";
    public final static String GET_LOAD_PLAN_LINE_BY_PAL_NUM = "FROM LoadPlanLine lpl WHERE lpl.palletNumber = :palletNumber";
    public final static String GET_PILES_OF_PLAN = "SELECT pile_num FROM load_plan_line WHERE load_plan_id = '%d' GROUP BY pile_num ORDER BY pile_num ASC";
    public final static String GET_PILES_OF_PLAN_BY_DEST = "SELECT pile_num FROM load_plan_line WHERE load_plan_id = '%d' AND destination_wh = '%s' GROUP BY pile_num ORDER BY pile_num ASC";
    public final static String GET_NOT_ASSOCIATED_PAL_NUM = "FROM LoadPlanLine lpl WHERE lpl.palletNumber = :palletNumber AND lpl.dispatchLabelNo = ''"; 
    public final static String SET_LOAD_PLAN_LINE_PRODLABEL_TO_DISPATCHLABEL = "UPDATE LoadPlanLine l SET l.dispatchLabelNo = :dispatchLabelNo WHERE l.palletNumber = :palletNumber";
    
    //LoadPlanLinePackaging
    public final static String GET_LOAD_PLAN_PACKAGING_BY_ID = "FROM LoadPlanLinePackaging l WHERE l.id = :id";
    public final static String GET_LOAD_PLAN_PACKAGING_BY_PLAN_ID_AND_DEST = "FROM LoadPlanLinePackaging l WHERE l.loadPlanId = :loadPlanId AND l.destination = :destination ORDER BY id DESC";
    public final static String GET_LOAD_PLAN_PACKAGING_BY_PLAN_ID = "FROM LoadPlanLinePackaging l WHERE l.loadPlanId = :loadPlanId ORDER BY id DESC";
    
    public final static String GET_PACKAGING_STOCK_MOUVEMENTS = "FROM PackagingStockMovement ORDER BY fifoTime DESC";
    
    //
    
}
