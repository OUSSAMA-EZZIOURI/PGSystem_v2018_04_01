/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.mode2.state;

import __main__.GlobalVars;
import gui.packaging.Mode2_Context;
import helper.Helper;
import entity.BaseHarness;
import entity.ConfigBarcode;
import helper.HQLHelper;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import gui.packaging.PackagingVars;
import org.hibernate.Query;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 *
 * @author ezou1001
 */
public class Mode2_S021_MatrixIdScan implements Mode2_State {

    private ImageIcon imgIcon = new ImageIcon(GlobalVars.APP_PROP.getProperty("IMG_PATH") + "S03_QRCodeScan.jpg");

    //Combien de patterns de scane configurer pour ce part number.
    private int numberOfPatterns = 0;

    /**
     *
     */
    public Mode2_S021_MatrixIdScan() {
        //Set Image
        PackagingVars.Packaging_Gui_Mode2.setIconLabel(this.imgIcon);
        //Reload container table content
        PackagingVars.Packaging_Gui_Mode2.reloadDataTable();
    }

    /**
     *
     * @param context
     */
    @Override
    public void doAction(Mode2_Context context) {
        JTextField scan_txtbox = PackagingVars.Packaging_Gui_Mode2.getScanTxt();
        String counter = scan_txtbox.getText().trim();
        BaseHarness bh = new BaseHarness();
        System.out.println("Harness part demander " + context.getBaseContainerTmp().getHarnessPart());
        //Tester le format et l'existance et si le counter concerne ce harness part
        if (!bh.checkDataMatrixFormat(counter)) {//Problème de format
            //Invalid QR code format [%s]!
            UILog.severe(ErrorMsg.APP_ERR0009[0], counter);
            UILog.severeDialog(null, ErrorMsg.APP_ERR0009, counter);
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0009[0], counter));
            //Vide le scan box            
            clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else if (bh.isCounterExist(counter)) {//Problème de doublant            
            bh = bh.getHarnessByCounter(counter);
            UILog.severe(ErrorMsg.APP_ERR0010[0], counter, bh.getPalletNumber());
            UILog.severeDialog(null, ErrorMsg.APP_ERR0010, counter, bh.getPalletNumber());
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0010[0], counter, bh.getPalletNumber()));
            //Vide le scan box
            clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
            // Problème de non conformité avec le harness part courrant          
            //Format 1 without prefix : 5101C861C;05.10.2017;10:05:01 
            //Format 2 with prefix P : P5101C861C;05.10.2017;10:05:01
            //Format 3 Harness Part with prefix P but removed to be compared with QR Code without P 5101C861C;05.10.2017;10:05:01
        } else if (!counter.startsWith(context.getBaseContainerTmp().getHarnessPart())
                && !counter.startsWith(GlobalVars.HARN_COUNTER_PREFIX + context.getBaseContainerTmp().getHarnessPart())
                && !counter.startsWith(context.getBaseContainerTmp().getHarnessPart().substring(1))) {
            UILog.severe(ErrorMsg.APP_ERR0011[0], counter, context.getBaseContainerTmp().getHarnessPart());
            UILog.severeDialog(null, ErrorMsg.APP_ERR0011, counter, context.getBaseContainerTmp().getHarnessPart());
            PackagingVars.Packaging_Gui_Mode2.getFeedbackTextarea().setText(UILog.severe(ErrorMsg.APP_ERR0011[0], counter, context.getBaseContainerTmp().getHarnessPart()));
            //Vide le scan box
            clearScanBox(scan_txtbox);
            //Retourner l'état actuel
            context.setState(this);
        } else {//BINGO !! Matrix Id correct
            PackagingVars.mode2_context.getBaseContainerTmp().setHernessCounter(counter);
            //Vide le scan box
            clearScanBox(scan_txtbox);
            //####################### Go to Palette Scann ###########################
            GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST = loadPlasticBagPattern();
            if (GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST.length != 0) {
                Mode2_S022_PlasticBagScan state = new Mode2_S022_PlasticBagScan(this.numberOfPatterns, GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST);
                context.setState(state);
            } else {
                // Pas de scanne de code à barre intermidiaire, passer
                // directement au choix de la palette.
                Mode2_S031_PalletChoice state = new Mode2_S031_PalletChoice();
                context.setState(state);
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
        if (PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart().startsWith(GlobalVars.HARN_PART_PREFIX)) {
            PN = PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart().substring(1);
        } else {
            PN = PackagingVars.mode2_context.getBaseContainerTmp().getHarnessPart();
        }
        System.out.println("Loading Additional barecodes pattern list for PN %s... " + PN);
        query.setParameter("harnessPart", PN);
        Helper.sess.getTransaction().commit();
        List resultList = query.list();
        System.out.println(String.format("%d pattern found for part number %s ", query.list().size(), PN));
        if (!query.list().isEmpty()) {
            GlobalVars.PLASTICBAG_BARCODE_PATTERN_LIST = new String[query.list().size()][2];
            //Allouer de l'espace pour la liste des code à barre getBaseHarnessAdditionalBarecodeTmp
            PackagingVars.mode2_context.getBaseHarnessAdditionalBarecodeTmp().setLabelCode(new String[query.list().size()]);
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

    public String toString() {
        return "State S03 : S03_QRCodeScan";
    }

    /**
     *
     * @return
     */
    public ImageIcon getImg() {
        return this.imgIcon;
    }

    /**
     *
     * @param scan_txtbox
     */
    public void clearScanBox(JTextField scan_txtbox) {
        //Vider le champs de text scan
        scan_txtbox.setText("");
        scan_txtbox.requestFocusInWindow();
        PackagingVars.Packaging_Gui_Mode2.setScanTxt(scan_txtbox);
    }

    /**
     *
     */
    public void clearContextSessionVals() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
