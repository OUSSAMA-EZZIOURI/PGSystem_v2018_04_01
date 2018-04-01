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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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

public final class PrintDispatchNote_A4 implements BarcodeCreator {

    public static String DEST;
    public static Map<String, String> map_title = new HashMap<String, String>();
    public String LINE_SPACER = "-";

    private String createTime;
    private String endTime;
    private String adviceNoteNum;
    private String user;
    private Object[][] noteLines;

    public PrintDispatchNote_A4(String adviceNoteNum, String user, String createTime, String endTime, Object[][] noteLines) {

        this.adviceNoteNum = adviceNoteNum;
        this.user = user;
        this.createTime = createTime;
        this.endTime = endTime;
        this.noteLines = noteLines;
        //Initialiser le tableau des Titres
        map_title.put("load_plan_num", "Plan de chargement N°");
        map_title.put("user", "Utilisateur");
        map_title.put("create_time", "Date création");
        map_title.put("end_time", "Date d'impression");

        //Create PDF File to print
        this.setDEST(String.format(".\\"
                + GlobalVars.APP_PROP.getProperty("PRINT_DIRNAME")
                + File.separator
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd")
                + File.separator
                + GlobalVars.APP_PROP.getProperty("PRINT_DISPATCH_SHEET_DIRNAME")
                + File.separator
                + "PrintDispatchNote_A4_"
                + GlobalMethods.getStrTimeStamp("yyyy_MM_dd_HH_mm_ss")
                + "_" + adviceNoteNum
                + ".pdf"));
    }

    public String getDEST() {
        return DEST;
    }

    public void setDEST(String DEST) {
        PrintDispatchNote_A4.DEST = DEST;
    }

    @Override
    public boolean sentToDefaultDesktopPrinter(String filePath) {
        try {
            Helper.log.info(String.format("Sending [%s] to the default printer...", filePath));
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            } else {
                Helper.log.warning("Desktop object not supported !");
            }
            System.out.println("Default printer " + desktop.toString());

            desktop.print(new File(filePath));

            Helper.log.info(String.format("File [%s] has been printed.", filePath));
            return true;
        } catch (IOException ioe) {
            Helper.log.warning("Problem in sentToDefaultDesktopPrinter");
            ioe.printStackTrace();
            return false;
        }
    }

    @Override
    public String createPdf(int special_order) throws IOException, DocumentException {
        //Left, right, top, bottom
        Document document = new Document(PageSize.A4, 10, 10, 10, 10);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(this.getDEST()));
        document.open();

        @SuppressWarnings("Convert2Diamond")

        Paragraph paragraph = new Paragraph();
        
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 26f));
        paragraph.add("Note de fin de chargement - Volvo");
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(Chunk.NEWLINE);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 20f));
        paragraph.add(map_title.get("load_plan_num") + " : " + this.adviceNoteNum);
        paragraph.setFont(FontFactory.getFont(FontFactory.COURIER, 12f));
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(map_title.get("user") + " : " + this.user);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(map_title.get("create_time") + " : " + this.createTime);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(map_title.get("end_time") + " : " + this.endTime);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add("------------------------------------------");
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(Chunk.NEWLINE);
        
        paragraph.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 20f));
        paragraph.add("Détails packaging : ");
        paragraph.add(Chunk.NEWLINE);
        // a table with three columns
        PdfPTable table = new PdfPTable(9);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setLockedWidth(true);
        // the cell object
        paragraph.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 12f));
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("ID"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(1);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("PILE"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(1);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("TYPE"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setRowspan(1);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("PAL NUM"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setRowspan(1);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("CUSTOMER PN"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setRowspan(1);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("INDEX"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setRowspan(1);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("LEONI PN"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setRowspan(1);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("QTY"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setRowspan(1);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("PACK TYPE"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setRowspan(1);
        table.addCell(cell);
        
        paragraph.setFont(FontFactory.getFont(FontFactory.COURIER, 12f));
        
        paragraph.add(Chunk.NEWLINE);
        for (Object[] objects : this.noteLines) {
            cell = new PdfPCell(new Phrase(objects[0].toString()));            
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(objects[1].toString()));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(objects[2].toString()));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(objects[3].toString()));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(objects[4].toString()));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(objects[5].toString()));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(objects[6].toString()));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(objects[7].toString()));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(objects[8].toString()));
            table.addCell(cell);
        }
        document.add(paragraph);
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

}
