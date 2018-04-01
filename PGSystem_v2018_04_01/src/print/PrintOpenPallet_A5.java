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
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ui.UILog;

public final class PrintOpenPallet_A5 implements BarcodeCreator {
        
    public static String DEST;
    public static Map<String, String> map_title = new HashMap<String, String>();
    public String LINE_SPACER = "-";
    
    private String harness_part;
    private String index;
    private String pack_size;
    private String pallet_number;
    private String print_date;
    private String print_time;
    private String pack_type;
    private String user;
    private String reprint;
    private String supplierPart;

    /**
     *  
     * @param user
     * @param pack_type
     * @param reprint
     * @param harness_part
     * @param index
     * @param supplierPart
     * @param pack_size
     * @param pallet_number
     * @param print_date
     * @param print_time 
     */
    public PrintOpenPallet_A5(
            String user, String pack_type, String reprint,
            String harness_part, String index, String supplierPart, String pack_size,
            String pallet_number, String print_date, String print_time) {
        
        for (int i = 0; i < 135; i++) LINE_SPACER += '-';
        
        this.user = user;
        this.pack_type = pack_type;
        this.reprint = reprint;
        this.harness_part = harness_part;
        this.supplierPart = supplierPart;
        this.index = index;
        this.pack_size = pack_size;
        this.pallet_number = pallet_number;
        this.print_date = print_date;
        this.print_time = print_time;
        //Initialiser le tableau des Titres
        map_title.put("station_user", "Station | User");
        map_title.put("pack_type", "Pack Type");
        map_title.put("reprint", " ");
        map_title.put("harness_part", "Customer Part");
        map_title.put("supplier_part", "LEONI Part");
        map_title.put("pack_size", "Pack Size");
        map_title.put("pallet_number", "Pallet Number");
        map_title.put("print_date", "Print Date");
        map_title.put("print_time", "Print Time");

        //Create PDF File to print
        this.setDEST(String.format(".\\"
                + GlobalVars.APP_PROP.getProperty("PRINT_DIRNAME")
                + File.separator
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd")
                + File.separator
                + GlobalVars.APP_PROP.getProperty("PRINT_PALLET_DIRNAME")
                + File.separator + "PrintOpenPallet_A5_"
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd_HH_mm_ss")
                + "_" + harness_part + "_"+ pallet_number
                + ".pdf"));
    }

    public String getPrint_time() {
        return print_time;
    }

    public void setPrint_time(String print_time) {
        this.print_time = print_time;
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

    public String getPack_size() {
        return pack_size;
    }

    public void setPack_size(String pack_size) {
        this.pack_size = pack_size;
    }

    public String getPallet_number() {
        return pallet_number;
    }

    public void setPallet_number(String pallet_number) {
        this.pallet_number = pallet_number;
    }

    public String getDEST() {
        return DEST;
    }

    public void setDEST(String DEST) {
        PrintOpenPallet_A5.DEST = DEST;
    }

    public String getPrint_date() {
        return print_date;
    }

    public void setPrint_date(String print_date) {
        this.print_date = print_date;
    }

    public String getPack_type() {
        return pack_type;
    }

    public void setPack_type(String pack_type) {
        this.pack_type = pack_type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReprint() {
        return reprint;
    }

    public void setReprint(String reprint) {
        this.reprint = reprint;
    }
    
    private String getSupplierPartNumber() {
        return supplierPart;        
    }
    
    private void setSupplierPartNumber(String supplierPart) {
        this.supplierPart = supplierPart;
    }

    @Override
    public boolean sentToDefaultDesktopPrinter(String filePath) {
        try {
            UILog.info(String.format("Sending [%s] to the default printer...", filePath));
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }else{
                UILog.severe("Desktop object not supported !");
            }
            //System.out.println("Default printer "+desktop.toString());
                    
            desktop.print(new File(filePath));

            UILog.info(String.format("File [%s] has been printed.", filePath));
            return true;
        } catch (IOException ioe) {
            UILog.severe("Problem in sentToDefaultDesktopPrinter");
            ioe.printStackTrace();
            return false;
        }
    }
    

    @Override
    public String createPdf(int special_order) throws IOException, DocumentException {
        //Left, right, top, bottom
        Document document = new Document(PageSize.A5.rotate(), 10, 20, 30, 10);
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
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        //Add 3 new empty lines
        
        cell.setPhrase(new Phrase(LINE_SPACER));
        cell.setColspan(3);
        table.addCell(cell);
        
        
        // Add Table titles
        cell.setColspan(1);

        // ######################## Line 0 ##########################
        cell.setPhrase(new Phrase(map_title.get("station_user"), FontFactory.getFont(FontFactory.COURIER, 20f)));
        table.addCell(cell);        
        cell.setPhrase(new Phrase(map_title.get("harness_part"), FontFactory.getFont(FontFactory.COURIER, 20f)));
        table.addCell(cell);
        cell.setPhrase(new Phrase(map_title.get("reprint"), FontFactory.getFont(FontFactory.COURIER, 20f)));
        table.addCell(cell);
        
        // Add Table titles
        cell.setColspan(1);
        
        // ######################## Line 2 ##########################
        // we add the four remaining cells with addCell()        
        cell.setPhrase(new Phrase(GlobalVars.APP_HOSTNAME+" | "+this.getUser(), FontFactory.getFont(FontFactory.COURIER, 18f, Font.BOLD)));
        table.addCell(cell);
        cell.setPhrase(new Phrase(this.getHarness_part()+"/"+this.getIndex(), FontFactory.getFont(FontFactory.COURIER, 25f, Font.BOLD)));
        table.addCell(cell);
        cell.setPhrase(new Phrase(this.getReprint(), FontFactory.getFont(FontFactory.COURIER, 18f, Font.BOLD)));
        table.addCell(cell);

        //Add 3 new empty lines
        cell.setPhrase(new Phrase("\n\n\n"+this.LINE_SPACER));
        cell.setColspan(3);
        table.addCell(cell);
        
        
        // ######################## Line 1 ##########################
        cell.setColspan(1);
        cell.setPhrase(new Phrase(map_title.get("supplier_part"), FontFactory.getFont(FontFactory.COURIER, 20f)));
        table.addCell(cell);
        cell.setPhrase(new Phrase(map_title.get("pack_type"), FontFactory.getFont(FontFactory.COURIER, 20f)));
        table.addCell(cell);        
        cell.setPhrase(new Phrase(map_title.get("pack_size"), FontFactory.getFont(FontFactory.COURIER, 20f)));
        table.addCell(cell);
                        
        // ######################## Line 2 ##########################
        // we add the four remaining cells with addCell()        
        cell.setPhrase(new Phrase(this.getSupplierPartNumber(), FontFactory.getFont(FontFactory.COURIER, 30f, Font.BOLD)));
        table.addCell(cell);
        cell.setPhrase(new Phrase(this.getPack_type(), FontFactory.getFont(FontFactory.COURIER, 30f, Font.BOLD)));
        table.addCell(cell);
        cell.setPhrase(new Phrase(this.getPack_size(), FontFactory.getFont(FontFactory.COURIER, 30f, Font.BOLD)));
        table.addCell(cell);

        //Add 3 new empty lines
        cell.setPhrase(new Phrase("\n\n\n"+this.LINE_SPACER));
        cell.setColspan(3);
        table.addCell(cell);

        // ######################## Line 3 ##########################
        cell.setPhrase(new Phrase(map_title.get("print_date"), FontFactory.getFont(FontFactory.COURIER, 20f)));
        cell.setColspan(1);
        table.addCell(cell);

        //Empty cellule
        cell.setPhrase(new Phrase(map_title.get("print_time"), FontFactory.getFont(FontFactory.COURIER, 20f)));
        cell.setColspan(1);
        table.addCell(cell);

        cell.setPhrase(new Phrase(map_title.get("pallet_number"), FontFactory.getFont(FontFactory.COURIER, 20f)));
        cell.setColspan(1);
        table.addCell(cell);

        // ######################## Line 3 ##########################
        //Add new empty line        
        cell.setPhrase(new Phrase(this.getPrint_date(), FontFactory.getFont(FontFactory.COURIER, 30f, Font.BOLD)));
        cell.setColspan(1);
        table.addCell(cell);

        //Empty cellule
        cell.setPhrase(new Phrase(this.getPrint_time(), FontFactory.getFont(FontFactory.COURIER, 30f, Font.BOLD)));
        cell.setColspan(1);
        table.addCell(cell);

        // Add Palet number in barcode format                        
        cell.setImage(createBarcode(writer, this.getPallet_number(), 10f, 0.4f, BaseColor.BLACK, BaseColor.BLACK, true));
        cell.setColspan(1);
        table.addCell(cell);

        document.add(table);

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

    @Override
    public String toString() {
        return "PrintOpenPallet_A5{" + "LINE_SPACER=" + LINE_SPACER + ",\n harness_part=" + harness_part + ",\n index=" + index + ",\n pack_size=" + pack_size + ",\n pallet_number=" + pallet_number + ",\n print_date=" + print_date + ",\n print_time=" + print_time + ",\n pack_type=" + pack_type + ",\n user=" + user + ",\n reprint=" + reprint + ",\n supplierPart=" + supplierPart + '}';
    }

    
    
    
}
