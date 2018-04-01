/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package print;

import __main__.GlobalMethods;
import __main__.GlobalVars;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import ui.UILog;

public class PrintClosingPallet_A5 implements BarcodeCreator {

    public static String DEST;
    public static Map<String, String> map_title = new HashMap<String, String>();
    private String harness_part;
    private String index;
    private String qty_expected;
    private String pallet_number;
    private String supplier_part_number;
    private String supplier_name;
    private String print_datetime;
    private String warehouse_code;

    public PrintClosingPallet_A5(String harness_part, String index, String qty_expected, String pallet_number, String supplier_part_number, String supplier_name, String print_datetime, String warehouse_code) {
        this.harness_part = harness_part;
        this.index = index;
        this.qty_expected = qty_expected;
        this.pallet_number = pallet_number;
        this.supplier_part_number = supplier_part_number;
        this.supplier_name = supplier_name;
        this.print_datetime = print_datetime;
        this.warehouse_code = warehouse_code;

        //Initialiser le tableau des Titres        
        map_title.put("supplier_name", "SUPPLIER NAME");
        map_title.put("harness_part", "CUSTOMER PART NUMBER");
        map_title.put("supplier_part_number", "SUPPLIER PART NUMBER");
        map_title.put("index", "INDEX");
        map_title.put("qty", "QUANTITY");
        map_title.put("create_datetime", "DATE");
        map_title.put("close_pallet_number", "CLOSING PALLET");
        map_title.put("warehouse_code", "WAREHOUSE");

        this.setDEST(String.format(".\\"
                + GlobalVars.APP_PROP.getProperty("PRINT_DIRNAME")
                + File.separator
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd")
                + File.separator
                + GlobalVars.APP_PROP.getProperty("PRINT_CLOSING_PALLET_DIRNAME")
                + File.separator + "PrintClosingPallet_A5_"
                + harness_part + "_"
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd_HH_mm_ss") + ".pdf"));
    }

    public PrintClosingPallet_A5() {
        this.setDEST(String.format(".\\"
                + GlobalVars.APP_PROP.getProperty("PRINT_DIRNAME")
                + File.separator
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd")
                + File.separator
                + GlobalVars.APP_PROP.getProperty("PRINT_CLOSING_PALLET_DIRNAME")
                + File.separator + "PrintClosingPallet_A5_"
                + harness_part + "_"
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd_HH_mm_ss") + ".pdf"));
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getPrint_datetime() {
        return print_datetime;
    }

    public void setPrint_datetime(String print_datetime) {
        this.print_datetime = print_datetime;
    }

    public static String getDEST() {
        return DEST;
    }

    public static void setDEST(String DEST) {
        PrintClosingPallet_A5.DEST = DEST;
    }

    public static Map<String, String> getMap_title() {
        return map_title;
    }

    public static void setMap_title(Map<String, String> map_title) {
        PrintClosingPallet_A5.map_title = map_title;
    }

    public String getHarness_part() {
        return harness_part;
    }

    public void setHarness_part(String harness_part) {
        this.harness_part = harness_part;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getQty_expected() {
        return qty_expected;
    }

    public void setQty_expected(String qty_expected) {
        this.qty_expected = qty_expected;
    }

    public String getPallet_number() {
        return pallet_number;
    }

    public void setPallet_number(String pallet_number) {
        this.pallet_number = pallet_number;
    }

    public String getSupplier_part_number() {
        return supplier_part_number;
    }

    public void setSupplier_part_number(String supplier_part_number) {
        this.supplier_part_number = supplier_part_number;
    }

    public String getWarehouse_code() {
        return warehouse_code;
    }

    public void setWarehouse_code(String warehouse_code) {
        this.warehouse_code = warehouse_code;
    }

    public boolean sentToDefaultJobPrinter(String filePath) {

        FileInputStream psStream = null;
        try {
            psStream = new FileInputStream(filePath);
        } catch (FileNotFoundException ffne) {
            ffne.printStackTrace();
        }
        if (psStream == null) {
            return false;
        }
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;

        // Loop on all services list
        Doc final_doc = new SimpleDoc(psStream, psInFormat, null);
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        PrintService targetPrinter = PrintServiceLookup.lookupDefaultPrintService();
        UILog.info(String.format("Target printer found: %s ", targetPrinter.toString()));

        if (targetPrinter != null) {
            DocPrintJob job = targetPrinter.createPrintJob();
            try {
                job.print(final_doc, aset);
                return true;

            } catch (Exception pe) {
                pe.printStackTrace();
                return false;
            }
        } else {
            UILog.severe("No printer services found");
            return false;
        }
    }

    public String createPdf(int special_order) throws IOException, DocumentException {
        //Left, right, top, bottom
        Document document = new Document(PageSize.A5.rotate(), 10, 20, 10, 0);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(this.getDEST()));
        document.open();
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        @SuppressWarnings("Convert2Diamond")

        // the cell object
        PdfPCell cell;

        // we add the four remaining cells with addCell()
        cell = new PdfPCell();
        cell.disableBorderSide(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

//        //Add 3 new empty lines
//        cell.setPhrase(new Phrase("\n"));
//        cell.setColspan(3);
//        table.addCell(cell);
        //########################## Line 1 ##########################
        if (special_order == 0) {   //True
            cell.setColspan(4);
            cell.setPaddingTop(1f);
            cell.setPaddingLeft(12f);
            cell.setPhrase(new Phrase(map_title.get("supplier_name"), FontFactory.getFont(FontFactory.COURIER, 16f)));
            table.addCell(cell);

            cell.setColspan(3);
            cell.setPaddingTop(5f);
            cell.setPhrase(new Phrase(this.supplier_name, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f)));
            cell.setPaddingBottom(10f);
            table.addCell(cell);
        } else {            
            cell.setColspan(3);
            cell.setPaddingTop(50f);
            cell.setPaddingLeft(15f);
            cell.setPaddingBottom(5f);
            cell.setPhrase(new Phrase(" ", FontFactory.getFont(FontFactory.COURIER, 18f)));
            table.addCell(cell);
        }

//        //Add 1 new empty lines
//        cell.setPhrase(new Phrase("\n"));
//        cell.setColspan(3);
//        table.addCell(cell);
        //########################## Line 2 ##########################
        cell.setPaddingTop(20f);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(1);
        cell.setPhrase(new Phrase(map_title.get("harness_part"), FontFactory.getFont(FontFactory.COURIER, 14f)));
        table.addCell(cell);

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(1);
        cell.setPhrase(new Phrase(map_title.get("index"), FontFactory.getFont(FontFactory.COURIER, 18f)));
        table.addCell(cell);

        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPhrase(new Phrase(map_title.get("qty"), FontFactory.getFont(FontFactory.COURIER, 18f)));
        table.addCell(cell);
        cell.setPaddingTop(0f);

        //Add 1 new empty lines
//        cell.setPhrase(new Phrase("\n"));
//        cell.setColspan(3);
//        table.addCell(cell);
        //Harness part (Customer Part Number)
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        //cell.setImage(createBarcode(writer, this.harness_part, 10f, 0.8f, BaseColor.BLACK, BaseColor.BLACK, true));
        cell.setPhrase(new Phrase(this.harness_part + "  /  " + this.index, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 42f)));
        table.addCell(cell);

//        cell.setColspan(1);
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setPhrase(new Phrase(this.index, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26f)));
//        table.addCell(cell);
        cell.setColspan(1);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPaddingLeft(15f);
        cell.setPaddingRight(15f);
        cell.setImage(createBarcode(writer, this.qty_expected, 6f, 0.7f, BaseColor.BLACK, BaseColor.BLACK, true));
        table.addCell(cell);

        //########################## Line 3 ##########################
        cell.setColspan(2);

        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPhrase(new Phrase(map_title.get("supplier_part_number"), FontFactory.getFont(FontFactory.COURIER, 18f)));
        table.addCell(cell);

        cell.setColspan(1);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPhrase(new Phrase(map_title.get("create_datetime"), FontFactory.getFont(FontFactory.COURIER, 18f)));
        table.addCell(cell);

        //Add 1 new empty lines
//        cell.setPhrase(new Phrase("\n"));
//        cell.setColspan(3);
//        table.addCell(cell);
        cell.setColspan(1);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPaddingLeft(15f);
        cell.setPaddingRight(15f);
        cell.setImage(createBarcode(writer, this.supplier_part_number, 20f, 0.6f, BaseColor.BLACK, BaseColor.BLACK, true));
        table.addCell(cell);

        cell.setColspan(1);
        cell.setPhrase(new Phrase(" ", FontFactory.getFont(FontFactory.COURIER, 18f)));
        table.addCell(cell);

        cell.setColspan(1);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPhrase(new Phrase(this.getPrint_datetime(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16f)));
        table.addCell(cell);

        //########################## Line 4 ##########################        
        cell.setColspan(1);
        cell.setPaddingLeft(15f);
        cell.setPaddingTop(15f);
        cell.setPhrase(new Phrase(map_title.get("close_pallet_number"), FontFactory.getFont(FontFactory.COURIER, 18f)));
        table.addCell(cell);

        cell.setColspan(1);
        cell.setPhrase(new Phrase(" ", FontFactory.getFont(FontFactory.COURIER, 18f)));
        table.addCell(cell);

        cell.setColspan(1);
        cell.setPaddingLeft(15f);
        cell.setPaddingTop(15f);
        cell.setPhrase(new Phrase(map_title.get("warehouse_code"), FontFactory.getFont(FontFactory.COURIER, 18f)));
        table.addCell(cell);

        cell.setColspan(1);
        cell.setPaddingTop(0);
        cell.setPaddingLeft(15f);
        cell.setImage(createBarcode(writer, this.pallet_number, 20f, 0.6f, BaseColor.BLACK, BaseColor.BLACK, true));
        table.addCell(cell);

        cell.setColspan(1);
        cell.setPhrase(new Phrase(" ", FontFactory.getFont(FontFactory.COURIER, 18f)));
        table.addCell(cell);

        cell.setColspan(1);
        cell.setPaddingTop(0);
        cell.setPaddingLeft(15f);
        cell.setImage(createBarcode(writer, this.warehouse_code, 18f, 1f, BaseColor.BLACK, BaseColor.BLACK, true));
        table.addCell(cell);

        document.add(table);

        PdfContentByte canvas = writer.getDirectContentUnder();
        Image image;
        if (special_order==1) //True
        {
            image = Image.getInstance(GlobalVars.APP_PROP.getProperty("IMG_PATH") + GlobalVars.APP_PROP.getProperty("CLOSING_SPECIAL_PALLET_TEMPLATE"));
        } else //False
        {
            image = Image.getInstance(GlobalVars.APP_PROP.getProperty("IMG_PATH") + GlobalVars.APP_PROP.getProperty("CLOSING_PALLET_TEMPLATE"));
        }

        image.scaleToFit(600f, 500f);
        image.setAbsolutePosition(0, 0);
        canvas.addImage(image);
        document.close();

        return this.DEST;
    }

    public Image createBarcode(PdfWriter writer, String code, float barcodeHeight, float minBarWidth, BaseColor barColor, BaseColor textColor, Boolean showText) throws DocumentException, IOException {
        Barcode128 code128 = new Barcode128();
        code128.setGenerateChecksum(true);
        code128.setBarHeight(barcodeHeight);
        code128.setX(minBarWidth);
        code128.setCode(code);

        if (showText == false) {
            code128.setFont(null);
        }
        //Add Barcode to PDF document
        Image barcode_img = Image.getInstance(code128.createImageWithBarcode(writer.getDirectContent(), barColor, textColor));

        return barcode_img;
    }

    public boolean sentToDefaultDesktopPrinter(String filePath) {
        try {
            UILog.info(String.format("Sending [%s] to the default printer...", filePath));
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            desktop.print(new File(filePath));
            UILog.info("File [%s] has been printed.");
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
}
