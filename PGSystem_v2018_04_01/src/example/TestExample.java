package example;

import java.util.MissingFormatArgumentException;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestExample {

    final static Logger logger = LoggerFactory.getLogger(TestExample.class);

    /**
     *
     * @param msg
     * @param args
     * @return
     */
    public static String toto(String msg, Object... args) {
        if (args == null) {
            System.out.println("Is null");
        }
        try {
            return String.format(msg, args);
        } catch (MissingFormatArgumentException e) {
            System.out.println("exeption " + e.getMessage());
            return msg;
        }

    }

    /**
     * @param arg
     */
    public static void main(String[] arg) {

//        PackagingMaster pm = new PackagingMaster();
//        List result = pm.selectAllMasterPack();
//        for (Object object : result) {
//            pm = (PackagingMaster) object;
//            System.out.println("pm"+pm);
//        }
//        String str = "Harness part [%s] not configured for the project [%s].";

//        String[] APP_ERR0001 = {"APP_ERR0001", "APP_ERR0001 : Failed to connect to the database !"};
//        
//        List<Object> args = new ArrayList<Object>() {
//            {
//                add("6200");
//                add("DUCATI");
//            }
//        };
        //String formatted = String.format(str, args.toArray());
//        String formatted = toto(str);
//        System.out.println("formatted" + formatted);

        String[] buttons = {"Yes", "Yes to all", "No", "Cancel"};

        int rc = JOptionPane.showOptionDialog(null, "Question ?", "Confirmation",
                JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[2]);

        System.out.println(rc);

        //Calendar calendar = Calendar.getInstance();
        //calendar.set(2009, 12, 31);
        //Integer weeksOfYear = calendar.getActualMaximum(Calendar.getMaximum(calendar));
        //System.out.println("weeksOfYear "+weeksOfYear);
        /*
         Helper.sess.beginTransaction();
         Set<BaseModule> modules = new HashSet<BaseModule>();
         BaseModule bm = new BaseModule();
         bm.setCreateId(1);
         bm.setCreateTime(new Date());
         bm.setWriteId(1);
         bm.setWriteTime(new Date());
         bm.setVersion("1.0.0");
         bm.setModuleName("PACKAGING");
         bm.setDescription("Packagin module");
         modules.add(bm);
         bm.create(bm);

         bm = new BaseModule();
         bm.setCreateId(1);
         bm.setCreateTime(new Date());
         bm.setWriteId(1);
         bm.setWriteTime(new Date());
         bm.setVersion("1.0.0");
         bm.setModuleName("WAREHOUSE_INPUT");
         bm.setDescription("Warehouse input module");
         modules.add(bm);
         bm.create(bm);
        
         ManufactureUsers u = new ManufactureUsers();
         u.setFirstName("Anass");
         u.setLastName("LAMRANI");        
         u.setLogin("anla1001");
         u.setCreateTime(new Date());
         u.setCreateId(1);        
         u.setWriteTime(new Date());
         u.setWriteId(1);
         u.setLoginTime(new Date());        
         u.setAccessLevel(1000);
         u.setPassword("");
         u.setHarnessType("ENGINE");
         u.setActive(1);        
         u.setModules(modules);
         u.create(u);
         */
//        
//        u.create(u);
////        ############################################################
//        BaseContainer bc = new BaseContainer();
//        bc.setUser("ezou1001");
//        bc.setCreateId(1);
//        bc.setWriteId(1);
//        bc.setCreateTime(new Date());
//        bc.setWriteTime(new Date());
//        bc.setStartTime(new Date());
//        bc.setCompleteTime(new Date());
//        bc.setWorkTime((float) 10.0);
//        bc.setHarnessIndex("P01");
//        bc.setHarnessPart("22205127");
//        bc.setHarnessType("SMALL");
//        bc.setQtyExpected(150);
//        bc.setQtyRead(50);
//        bc.setPackType("kltv-780");
//        bc.setContainerState(Global.PALLET_OPEN);
//        bc.setContainerStateCode("100");
//        bc.setPalletNumber("100000001");
//        bc.setSupplierPartNumber("A22547899");
//        bc.create((Object) bc);
//
//        for (int i = 0; i < 150; i++) {
//            BaseHarness bh = new BaseHarness();
//            bh.setCreateId(1);
//            bh.setCreateTime(new Date());
//            bh.setWriteId(1);
//            bh.setWriteTime(new Date());
//            bh.setCounter("123456789");
//            bh.setHarnessPart("22205127");
//            bh.setHarnessType("SMALL");
//            bh.setPalletNumber("100000001");
//            bh.setUser("ezou1001");
//
//            bh.setContainer(bc);
//            bc.getHarnessList().add(bh);     
//            bh.create((Object) bh);
//        }
//
//        bc.setContainerState("closed");
//        bc.setContainerStateCode("900");
//
//        bc.update((Object)bc);
//        ############################################################
//        BaseUser bu = new BaseUser();
//        bu.setActive(1);
//        bu.setFname("Jawad");
//        bu.setLname("HAMID");
//        bu.setHarnessType("ENGINE");
//        bu.setLogin("ezou1001");
//        bu.setPassword("123456");
//        bu.setAccessLevel(1000);
//        bu.setCreateTime(new Date());
//        bu.setWriteTime(new Date());
//        bu.setCreateId(1);
//        bu.setWriteId(1);
//        createUser(bu);
//        ############################################################        
        // our instances have a primary key now:
//        logger.debug("{}", bu);
//CREATE TABLE "public"."user" (id SERIAL, fname text, lname text, login text,  PRIMARY KEY(id));
//        Honey forestHoney = new Honey();
//        forestHoney.setName("forest honey");
//        forestHoney.setTaste("very sweet");
//        Honey countryHoney = new Honey();
//        countryHoney.setName("country honey");
//        countryHoney.setTaste("tasty");
//        createHoney(forestHoney);
//        createHoney(countryHoney);
//        // our instances have a primary key now:
//        logger.debug("{}", forestHoney);
//        logger.debug("{}", countryHoney);
//        listHoney();
//        deleteHoney(countryHoney);
//        listHoney();
//        forestHoney.setName("Norther Forest Honey");
//        updateHoney(forestHoney);
//        ############################################################        
//        ConfigProject cp = new ConfigProject();
//        cp.setHarnessType("ENGINE");
//        cp.create(cp);
//        cp.setHarnessType("SMALL");
//        cp.create(cp);
//////        ############################################################      
//        BaseHarness bh = new BaseHarness();
//        bh.setCreateId(1);
//        bh.setCreateTime(new Date());
//        bh.setWriteId(1);
//        bh.setWriteTime(new Date());
//        bh.setCounter("123456789");
//        bh.setHarnessPart("22205127");
//        bh.setHarnessType("SMALL");
//        bh.setPalletNumber("100000002");
//        bh.setUser("ezou1001");        
//        bh.create(bh);
////        
//        BaseHarnessAdditionalBarecode bel = new BaseHarnessAdditionalBarecode();
//        bel.setCreateId(1);
//        bel.setCreateTime(new Date());
//        bel.setWriteId(1);
//        bel.setWriteTime(new Date());
//        bel.setHarness(bh);
//        bel.setLabelCode("999999");
//        bel.create(bel);
//        
//        BaseHarnessAdditionalBarecode bel2 = new BaseHarnessAdditionalBarecode();
//        bel2.setCreateId(1);
//        bel2.setCreateTime(new Date());
//        bel2.setWriteId(1);
//        bel2.setWriteTime(new Date());
//        bel2.setHarness(bh);
//        bel2.setLabelCode("888888");
//        bel2.create(bel);
////        ############################################################ 
//        HisGaliaPrint hgp = new HisGaliaPrint();
//        hgp.setCreateId(1);
//        hgp.setCreateTime(new Date());
//        hgp.setWriteId(1);
//        hgp.setWriteTime(new Date());
//        hgp.setQty(12);
//        hgp.setHarnessIndex("P01");
//        hgp.setHarnessPart("22205127");
//        hgp.setSupplierName("LEONI WIRING SYSTEMS");
//        hgp.setSupplierPartNumber("A2225648");
//        hgp.setClosingPallet("100000002");
//        hgp.setPrintState(Global.PALLET_PRINT_PRINTED);
//        hgp.create(hgp);
//        
//        
//        
//        HisPalletPrint hpp = new HisPalletPrint();
//        hpp.setCreateId(1);
//        hpp.setCreateTime(new Date());
//        hpp.setWriteId(1);
//        hpp.setWriteTime(new Date());
//        hpp.setPrintState(Global.PALLET_PRINT_PRINTED);
//        hpp.setHarnessIndex("P10");
//        hpp.setHarnessPart("22205127");
//        hpp.setPackSize(150);
//        hpp.create(hpp);
//        
//        HisLogin hl = new HisLogin();
//        hl.setCreateId(1);
//        hl.setCreateTime(new Date());
//        hl.setWriteId(1);
//        hl.setWriteTime(new Date());
//        hl.setMessage("Just a test");
//        hl.create(hl);
//        ConfigShift cs = new ConfigShift();
//        cs = cs.selectShiftByDesc("MORNING");
//        System.out.println(cs.toString());
//        ScheduleEntry se = new ScheduleEntry();
//
//        se.setCreateTime(new Date());
//        se.setWriteTime(new Date());
//        se.setCreateId(1);
//        se.setWriteId(1);
//        se.setHarnessType("SMALL");
//        se.setHarnessPart("22205127");
//        se.setHarnessIndex("P01");
//        se.setPackType("2RV");
//        se.setPackSize(150);
//        se.setSupplierPartNumber("26C03051A");
//        se.setPlannedDay(new Date());
//        se.setStartHour(cs.getEndHour());
//        se.setStartMinute(cs.getEndMinute());
//        se.setEndHour(cs.getEndHour());
//        se.setEndMinute(cs.getEndMinute());
//        se.setPlannedPack((float) 2.0);
//        se.setTotalPlanned((float) 300);
//        se.setTotalProduced((float) 0);
//        se.setTotalRemaining((float) 300);
//        se.setShiftName(cs.getName());
//        se.setShift(cs);
//        se.setEntryState("NEW");
//        se.setEntryStateCode("1000");        
//        se.create(se);
    }

}
