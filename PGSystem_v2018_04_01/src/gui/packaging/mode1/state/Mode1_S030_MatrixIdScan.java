/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode1.state;

import __main__.GlobalMethods;
import __main__.GlobalVars;
import com.itextpdf.text.DocumentException;
import entity.BaseContainer;
import gui.packaging.Mode1_Context;
import helper.Helper;
import entity.BaseHarness;
import entity.BaseHarnessAdditionalBarecode;
import entity.ConfigBarcode;
import gui.packaging.PackagingVars;
import helper.HQLHelper;
import helper.PrinterHelper;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;

import javax.swing.JTextField;
import org.hibernate.Query;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author ezou1001
 */
public class Mode1_S030_MatrixIdScan implements Mode1_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S03_QRCodeScan.jpg");

    //Combien de patterns de scane configurer pour ce part number.
    private int numberOfPatterns = 0;

    public Mode1_S030_MatrixIdScan() {
        //Set Image
        PackagingVars.Packaging_Gui_Mode1.setIconLabel(this.imgIcon);
        //Reload container table content
        PackagingVars.Packaging_Gui_Mode1.reloadDataTable();
    }

    @Override
    public void doAction(Mode1_Context context) {
        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode1.getScanTxt();
        String counter = scan_txtbox.getText().trim();
        BaseHarness bh = new BaseHarness();
        System.out.println("Harness part demander " + PackagingVars.mode1_context.getTempBC().getHarnessPart());
        //Tester le format et l'existance et si le counter concerne ce harness part
        if (!bh.checkDataMatrixFormat(counter)) {//Problème de format
            UILog.severeDialog(null, ErrorMsg.APP_ERR0009, counter);
            UILog.severe(ErrorMsg.APP_ERR0009[0], counter);
            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0005_HP_COUNTER_FORMAT, counter), "Counter error !", ERROR_MESSAGE);
            //System.out.println(String.format(Helper.ERR0005_HP_COUNTER_FORMAT, counter));
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            PackagingVars.mode1_context.setState(this);
        } else if (bh.isCounterExist(counter)) {//Problème de doublant            
            bh = bh.getHarnessByCounter(counter);

            UILog.severeDialog(null, ErrorMsg.APP_ERR0010, counter, bh.getPalletNumber());
            UILog.severe(ErrorMsg.APP_ERR0010[0], counter, bh.getPalletNumber());
            //JOptionPane.showMessageDialog(null, String.format(Helper.INFO0004_HP_COUNTER_FOUND, counter, bh.getPalletNumber()), "Counter error !", ERROR_MESSAGE);
            //System.out.println(String.format(Helper.INFO0004_HP_COUNTER_FOUND, counter, bh.getPalletNumber()));
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            PackagingVars.mode1_context.setState(this);

            // Problème de non conformité avec le harness part courrant          
            //Format 1 without prefix : 5101C861C;05.10.2017;10:05:01 
            //Format 2 with prefix P : P5101C861C;05.10.2017;10:05:01
            //Format 3 Harness Part with prefix P but removed to be compared with QR Code without P 5101C861C;05.10.2017;10:05:01
        } else if (!counter.startsWith(PackagingVars.mode1_context.getTempBC().getHarnessPart())
                && !counter.startsWith(GlobalVars.HARN_COUNTER_PREFIX + PackagingVars.mode1_context.getTempBC().getHarnessPart())
                && !counter.startsWith(PackagingVars.mode1_context.getTempBC().getHarnessPart().substring(1))) {

            UILog.severeDialog(null, ErrorMsg.APP_ERR0011, counter, PackagingVars.mode1_context.getTempBC().getHarnessPart());
            UILog.severe(ErrorMsg.APP_ERR0011[0], counter, PackagingVars.mode1_context.getTempBC().getHarnessPart());
            //JOptionPane.showMessageDialog(null, String.format(Helper.ERR0009_COUNTER_NOT_MATCH_HP, counter, PackagingVars.mode1_context.getTempBC().getHarnessPart()), "Counter error !", ERROR_MESSAGE);
            //System.out.println(String.format(Helper.ERR0009_COUNTER_NOT_MATCH_HP, counter, PackagingVars.mode1_context.getTempBC().getHarnessPart()));
            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            PackagingVars.mode1_context.setState(this);
        } else {//BINGO !! Matrix Id correct
            PackagingVars.mode1_context.getTempBC().setHarnessCounter(counter);
            Helper.log.info("Valid Harness Counter scanned [" + counter + "] OK.");

            //Vide le scan box
            this.clearScanBox(scan_txtbox);
            //####################### Go to Palette Scann ###########################
            GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST = loadPlasticBagPattern();
            if (GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST.length != 0) {
                Mode1_S031_PlasticBagScan state = new Mode1_S031_PlasticBagScan(this.numberOfPatterns, GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST);
                PackagingVars.mode1_context.setState(state);
            } else {
                // Pas de scanne de code à barre intermidiaire, 
                // Ajouter la pièce à la palette.
                //.....
                System.out.println("GVars.mode1_context.getTempBC().getPalletNumber() " + PackagingVars.mode1_context.getTempBC().getPalletNumber());
                addPieceToContainer(PackagingVars.mode1_context.getTempBC().getPalletNumber());

            }
        }

    }

    public void addPieceToContainer(String palletNumber) {

        //Textbox is not empty
        if (!palletNumber.isEmpty()) {
            BaseContainer bc = new BaseContainer().getBaseContainer(palletNumber);

            if (!bc.getPackWorkstation().equals(GlobalVars.APP_HOSTNAME)) {
                UILog.severeDialog(null, ErrorMsg.APP_ERR0014, bc.getPackWorkstation(), GlobalVars.APP_HOSTNAME, bc.getPackWorkstation());
                UILog.severe(ErrorMsg.APP_ERR0014[0], bc.getPackWorkstation(), GlobalVars.APP_HOSTNAME, bc.getPackWorkstation());

            } //# 1- If container exist 
            //# 2- Mode1_State is Open
            //# 3- Max Quantity not reached
            //# 4- Container Harness Part = Mode2_Context Harness Part  
            //# 5- Container Harness Type = Mode2_Context Harness Type
            else if (bc != null
                    && bc.getContainerState().equals(GlobalVars.PALLET_OPEN)
                    && bc.getQtyRead() < bc.getQtyExpected()
                    && (bc.getHarnessPart().equals(PackagingVars.mode1_context.getTempBC().getHarnessPart().substring(1))
                    || bc.getHarnessPart().equals(PackagingVars.mode1_context.getTempBC().getHarnessPart()))
                    && bc.getHarnessType().equals(PackagingVars.mode1_context.getTempBC().getHarnessType())) {

//                Helper.log.info("Hostname check OK");
//                Helper.log.info("Pallet values ");
//                Helper.log.info(String.format("State           :   [%s]", bc.getContainerState().equals(GlobalVars.PALLET_OPEN)));
//                Helper.log.info(String.format("Qty Expected    :   [%s]", bc.getQtyExpected()));
//                Helper.log.info(String.format("Qty Read        :   [%s]", bc.getQtyRead()));
//                Helper.log.info(String.format("Std Time        :   [%s]", bc.getStdTime()));
//                Helper.log.info(String.format("Harness Part [%s] = Context Harness Part [%s]",
//                        bc.getHarnessPart(),
//                        PackagingVars.mode1_context.getTempBC().getHarnessPart()));
//                Helper.log.info(String.format("Harness Type [%s] = Context Harness Type [%s]",
//                        bc.getHarnessType(), PackagingVars.mode1_context.getTempBC().getHarnessType()));
                Helper.sess.beginTransaction();
                Helper.sess.persist(bc);
                bc.setWriteId(PackagingVars.context.getUser().getId());
                bc.setFifoTime(GlobalMethods.getTimeStamp(null));
                bc.setHarnessType(PackagingVars.mode1_context.getTempBC().getHarnessType());

                //#################### SET HARNESS DATA  #######################                                
                //- Set harness data from current mode1_context.                
                BaseHarness bh = new BaseHarness().setDefautlVals();
                bh.setHarnessPart(PackagingVars.mode1_context.getTempBC().getHarnessPart());
                bh.setCounter(PackagingVars.mode1_context.getTempBC().getHarnessCounter());
                bh.setPalletNumber(palletNumber);
                bh.setHarnessType(PackagingVars.mode1_context.getTempBC().getHarnessType());
                bh.setStdTime(bc.getStdTime());
                bh.setPackWorkstation(GlobalVars.APP_HOSTNAME);
                bh.setSegment(bc.getSegment());
                bh.setWorkplace(bc.getWorkplace());
                bh.setContainer(bc);
                //##############################################################

                //############### SET & SAVE ALL ENGINE LABELS DATA #################     
                //Si ce part number contient des code à barre pour sachet
                if (PackagingVars.mode1_context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode().length != 0) {
                    for (String labelCode : PackagingVars.mode1_context.getBaseHarnessAdditionalBarecodeTmp().getLabelCode()) {
                        BaseHarnessAdditionalBarecode bel = new BaseHarnessAdditionalBarecode();
                        bel.setDefautlVals();
                        bel.setLabelCode(labelCode);
                        bel.setHarness(bh);
                        bel.create(bel);
                    }
                }
                //##############################################################

                //############## ADD THE HARNESS TO THE CONTAINER ##############    
                //Insert the harness into the container
                bc.getHarnessList().add(bh);

                int newQty = bc.getQtyRead() + 1;

                //Incrémenter la taille du contenaire                
//                Query query = Helper.sess.createQuery(HQLHelper.SET_CONTAINER_QTY_READ);
//                query.setParameter("qtyRead", newQty);
//                query.setParameter("id", bc.getId());
//                query.executeUpdate();
                bc.setQtyRead(bc.getQtyRead() + 1);
                bc.update(bc);

                //##############################################################
                //####### CHECK IF THE HARNESS EXISTS IN THE DROP TABLE ########
                //Yes                
                System.out.println(String.format("Harness %s to be removed from drop table ", PackagingVars.mode1_context.getTempBC().getHarnessCounter()));
                Query query = Helper.sess.createQuery("DELETE DropBaseHarness WHERE counter = :COUNTER");
                query.setParameter("COUNTER", bh.getCounter());

                int result = query.executeUpdate();
                System.out.println("Deletion result %s " + result);
                //##############################################################

                //- Set harness data from drop table 
                //- Remouve it from drop table (use a flag var to drop it in the end 
                // of this condition)
                //- Create a history mouvement line in base_harness history
                //##############################################################
                //############## Check if pallet should be closed ##############
                //############## UCS Contains just 1 harness ###################
                if (bc.getQtyExpected() == newQty || bc.getQtyExpected() == 1) {
                    setToWaiting(bc, newQty);
                } else { //QtyExpected not reached yet  ! Pallet will still open.
                    //Clear session vals in mode1_context
                    clearContextSessionVals();
                    goBackToFirstScan();
                }
            }
        }
    }

    /**
     * Charger les
     *
     * @return patterns de scanne configurer pour ce part number.
     *
     */
    public String[][] loadPlasticBagPattern() {

        //PLASTICBAG_BARCODE_PATTERN_LIST
        String PN = "";

        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_PATTERN_BY_HARNESSPART);
        if (PackagingVars.mode1_context.getTempBC().getHarnessPart().startsWith(GlobalVars.HARN_PART_PREFIX)) {
            PN = PackagingVars.mode1_context.getTempBC().getHarnessPart().substring(1);
        } else {
            PN = PackagingVars.mode1_context.getTempBC().getHarnessPart();
        }

        System.out.println("Loading Additional barecodes pattern list for PN %s... " + PN);
        query.setParameter("harnessPart", PN);
        Helper.sess.getTransaction().commit();
        List resultList = query.list();
        System.out.println(String.format("%d pattern found for part number %s ", query.list().size(), PN));
        if (!query.list().isEmpty()) {
            GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST = new String[query.list().size()][2];
            //Allouer de l'espace pour la liste des code à barre getBaseHarnessAdditionalBarecodeTmp
            PackagingVars.mode1_context.getBaseHarnessAdditionalBarecodeTmp().setLabelCode(new String[query.list().size()]);
            int i = 0;
            for (Iterator it = resultList.iterator(); it.hasNext();) {
                this.numberOfPatterns++;
                ConfigBarcode config = (ConfigBarcode) it.next();
                GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST[i][0] = config.getBarcodePattern();
                GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST[i][1] = config.getDescription();
                i++;
            }

            System.out.println("PLASTICBAG_BARCODE_PATTERN_LIST " + this.numberOfPatterns + " pattern(s) successfuly loaded 100% ! ");
        } else {
            GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST = new String[0][0];

        }
        //No pattern found ! Retourner une liste vide.
        return GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST;
    }

    @Override
    public String toString() {
        return "State S03 : S03_QRCodeScan";
    }

    @Override
    public ImageIcon getImg() {
        return this.imgIcon;
    }

    @Override
    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        PackagingVars.Packaging_Gui_Mode1.setScanTxt(scan_txtbox);
    }

    @Override
    public void clearContextSessionVals() {
        PackagingVars.mode1_context.setLabelCount(0);
        GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST = new String[0][];
    }

    /**
     *
     * @param bc
     * @param newQty
     */
    private void setToWaiting(BaseContainer bc, int newQty) {
        Helper.log.info(String.format("Quantité terminée %s", bc.toString()));
        try {
            PrinterHelper.saveAndPrintClosingSheet(PackagingVars.mode1_context, bc, false);
        } catch (IOException ex) {
            UILog.severe(ex.toString());
        } catch (DocumentException ex) {
            UILog.severe(ex.toString());
        }
        //Helper.startSession();
        bc.setContainerState(GlobalVars.PALLET_WAITING);
        bc.setContainerStateCode(GlobalVars.PALLET_WAITING_CODE);
        bc.update(bc);

        //Incrémenter la taille du contenaire                
        Query query = Helper.sess.createQuery(HQLHelper.SET_CONTAINER_QTY_READ);
        query.setParameter("qtyRead", newQty);
        query.setParameter("id", bc.getId());
        query.executeUpdate();

        PackagingVars.mode1_context.getTempBC().setPalletNumber(bc.getPalletNumber());
        //Set requested closing pallet number in the main gui
        PackagingVars.Packaging_Gui_Mode1.setFeedbackTextarea("N° "
                + GlobalVars.CLOSING_PALLET_PREFIX + bc.getPalletNumber());
        PackagingVars.mode1_context.setState(new Mode1_S050_ClosingPallet());
    }

    /**
     *
     */
    private void goBackToFirstScan() {
        PackagingVars.Packaging_Gui_Mode1.reloadDataTable();
        //Retourner à l'état Mode1_S021_HarnessPartScan pour scanner
        //une nouvelle pièce
        Mode1_S021_HarnessPartScan state = new Mode1_S021_HarnessPartScan(false, PackagingVars.mode1_context.getTempBC());
        PackagingVars.mode1_context.setState(state);
    }

}
