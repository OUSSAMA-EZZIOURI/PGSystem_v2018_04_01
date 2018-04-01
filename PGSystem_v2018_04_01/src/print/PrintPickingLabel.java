/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package print;

import __main__.GlobalMethods;
import __main__.GlobalVars;
import helper.Helper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrintPickingLabel implements BarcodeCreator {
    
    public class Rotate extends PdfPageEventHelper {
        
        protected PdfNumber rotation;

        public Rotate(PdfNumber rotation) {
            this.rotation = rotation;
        }
                
        public void setRotation(PdfNumber rotation) {
            this.rotation = rotation;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            writer.addPageDictEntry(PdfName.ROTATE, rotation);
        }
    }
    
    private PdfNumber rotation;
    
    public static String DEST;
    //public static Map<String, String> map_title = new HashMap<String, String>();
    private String harness_part;
    private String harness_type;
    private String index;
    private String user;
    private String supplier_part_number;
    private String supplier_name;
    private String print_datetime;

    public PrintPickingLabel(String harness_part, String harness_type, String index, String user, String supplier_part_number, String supplier_name, String print_datetime, PdfNumber rotation) {
        this.harness_part = harness_part;
        this.harness_type = harness_type;
        this.index = index;
        this.user = user;
        this.supplier_part_number = supplier_part_number;
        this.supplier_name = supplier_name;
        this.print_datetime = print_datetime;
        this.rotation = rotation;

        PrintPickingLabel.setDEST(String.format(".\\"
                + GlobalVars.APP_PROP.getProperty("PRINT_DIRNAME")
                + File.separator
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd")
                + File.separator
                + GlobalVars.APP_PROP.getProperty("PRINT_PICKING_SHEET_DIRNAME")
                + File.separator + "label_"
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd_HH_mm_ss") + ".pdf"));
    }

    public PrintPickingLabel() {
        PrintPickingLabel.setDEST(String.format(".\\"
                + GlobalVars.APP_PROP.getProperty("PRINT_DIRNAME")
                + File.separator
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd")
                + File.separator
                + GlobalVars.APP_PROP.getProperty("PRINT_CLOSING_PALLET_DIRNAME")
                + File.separator + "label_"
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd_HH_mm_ss") + ".pdf"));
    }

    public PdfNumber getRotation() {
        return rotation;
    }

    public void setRotation(PdfNumber rotation) {
        this.rotation = rotation;
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
        PrintPickingLabel.DEST = DEST;
    }

    public String getHarness_part() {
        return harness_part;
    }

    public void setHarness_part(String harness_part) {
        this.harness_part = harness_part;
    }

    public String getHarness_type() {
        return harness_type;
    }

    public void setHarness_type(String harness_type) {
        this.harness_type = harness_type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSupplier_part_number() {
        return supplier_part_number;
    }

    public void setSupplier_part_number(String supplier_part_number) {
        this.supplier_part_number = supplier_part_number;
    }    

    public String createPdf(int special_order) throws IOException, DocumentException {
        //Left, right, top, bottom
        Rectangle labelSize = new Rectangle(
                Float.valueOf(GlobalVars.APP_PROP.getProperty("PRINT_PICKING_SHEET_WIDTH")),
                Float.valueOf(GlobalVars.APP_PROP.getProperty("PRINT_PICKING_SHEET_HEIGHT"))                 
        );
        
        Document document = new Document(labelSize, 0, 0, 1, 1);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PrintPickingLabel.getDEST()));
                       
        writer.setPageEvent(new Rotate(this.getRotation()));
        
        document.open();
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        @SuppressWarnings("Convert2Diamond")

        // the cell object
        PdfPCell cell = new PdfPCell();
        cell.disableBorderSide(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        
        //########################## Line 0 ##########################   
        PdfPCell BarCodeCell = new PdfPCell();
        BarCodeCell.disableBorderSide(Rectangle.BOX);
        BarCodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        BarCodeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        BarCodeCell.setPaddingTop(8f);
        BarCodeCell.setImage(createBarcode(writer, this.harness_part, 8f, 0.4f, BaseColor.BLACK, BaseColor.BLACK, false));        
        table.addCell(BarCodeCell);
        
        //########################## Line 1 ##########################                        
        cell.setPhrase(new Phrase(this.supplier_name, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
        cell.setPaddingBottom(8f);
        table.addCell(cell);

        //########################## Line 2 ##########################                        
        cell.setPhrase(new Phrase(this.harness_type, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
        cell.setPaddingBottom(8f);
        table.addCell(cell);

        //########################## Line 3 ##########################                        
        cell.setPhrase(new Phrase(this.harness_part, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
        cell.setPaddingBottom(8f);
        table.addCell(cell);

        //########################## Line 4 ##########################                        
        cell.setPhrase(new Phrase(this.index, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
        cell.setPaddingBottom(8f);
        table.addCell(cell);

        //########################## Line 5 ##########################                        
        cell.setPhrase(new Phrase(this.supplier_part_number, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)));
        cell.setPaddingBottom(8f);
        table.addCell(cell);

        //########################## Line 6 ##########################                        
        cell.setPhrase(new Phrase("User : " + this.user, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8f)));
        cell.setPaddingBottom(8f);
        table.addCell(cell);

        //########################## Line 7 ##########################                        
        cell.setPhrase(new Phrase(this.print_datetime, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6f)));
        cell.setPaddingBottom(8f);
        table.addCell(cell);
        
       //########################## Line 8 ##########################                    
        BarCodeCell.setImage(createBarcode(writer, this.harness_part, 8f, 0.4f, BaseColor.BLACK, BaseColor.BLACK, false));        
        table.addCell(BarCodeCell);

        document.add(table);

        document.close();

        return PrintPickingLabel.DEST;
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
            Helper.log.info(String.format("Sending [%s] to the default printer...", filePath));
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            desktop.print(new File(filePath));
            Helper.log.info("File [%s] has been printed.");
            return true;
        } catch (IOException ioe) {
            System.out.println("PrintPickingLabel sentToDefaultDesktopPrinter IOException : " + ioe.getMessage());
            return false;
        }
    }
}
