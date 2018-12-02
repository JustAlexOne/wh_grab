package com.justalex.pnp

import com.itextpdf.text.BadElementException
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import groovy.io.FileType

import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream


class PdfWorker {

    static void main(String[] args) {
//        Document document = new Document();
        Document document = new Document(PageSize.A4, 15, 15, 15, 15);
        PdfWriter.getInstance(document, new FileOutputStream("myFile1.pdf"));

        document.open();

//        Font font = FontFactory.getFont(FontFactory.COURIER, 25, BaseColor.BLACK);
//        Chunk chunk = new Chunk("Hello World", font);
//        document.add(chunk);

        def imageFiles = []

        def dir = new File("card_images/all")
        dir.eachFileRecurse (FileType.FILES) { file ->
            imageFiles << file
        }
        println(imageFiles.size())
//        list.each {println(it)}

//        1.upto(10){ imageFiles.add("src/main/resources/imgs/${it}.png")}
        imageFiles = imageFiles.subList(0, 9)
        // 550x770

        // real card size = 63.5 x 88 mm

        // user units: 178.58268 x 249.44904
        PdfPTable table = new PdfPTable(3);
//        table.setWidthPercentage(98)
        float resWidth = 178.58268f * 3f
        println("resWidth: $resWidth")
        table.setTotalWidth(resWidth);
        table.setLockedWidth(true);
//        table.setSpacingBefore(0f);
//        table.setSpacingAfter(0f);

        imageFiles.each {
            Image image = Image.getInstance(it.getAbsolutePath())
//            image.scalePercent(30)
            PdfPCell cell = new PdfPCell()
            cell.setPadding(0f)
//            cell.setFixedHeight(249.44904f)
            cell.setImage(image)

//            table.addCell(image)
            table.addCell(cell)
        }
        int extraCells = (imageFiles.size() % 3)
        extraCells = extraCells == 0 ? 0 : 3 - extraCells
        println("Adding $extraCells extra cells")
        extraCells.times {
            table.addCell("")
        }
        document.add(table);
        document.close();
    }

    private static void addTableHeader(PdfPTable table) {
       ["column header 1", "column header 2", "column header 3"].each { columnTitle ->
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        }
    }

    private static void addRows(PdfPTable table) {
        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");
    }

    private static void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
//        Path path = Paths.get(new URI("/Users/justalex/Projects/wh_grab/src/main/java/com/justalex/imageCleanup/001-NV_ENG.png"));
//        Path path = Paths.get(ClassLoader.getSystemResource("Java_logo.png").toURI());
        Image img = Image.getInstance("/Users/justalex/Projects/wh_grab/src/main/java/com/justalex/imageCleanup/001-NV_ENG.png");
//        img.scalePercent((88 / img.getPlainHeight() * 100) as Float, (63.5 / img.getPlainWidth() * 100) as Float)
        img.scalePercent((88 / img.getPlainHeight() * 100) as Float)
//        img.scaleAbsolute(100, 100)


        println("height: ${img.getPlainHeight()}")
        println("width: ${img.getPlainWidth()}")

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }

}
