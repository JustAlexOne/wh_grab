package com.justalex.pnp

import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import groovy.io.FileType

class PdfWorker {

    private File destinationFile
    Document document

    PdfWorker(File destinationFile) {
        this.destinationFile = destinationFile
        document = new Document(PageSize.A4, 15, 15, 15, 15)
        PdfWriter.getInstance(document, new FileOutputStream(destinationFile))
        document.open()
    }

    PdfWorker(String fileName) {
        this(new File(fileName))
    }

    static void main(String[] args) {
        def worker = new PdfWorker("myFile_refactor.pdf")
        def imageFiles = []
        def dir = new File("card_images/all")
//        def dir = new File("card_images/all")
        dir.eachFileRecurse(FileType.FILES) { file ->
            imageFiles << file
        }
        imageFiles = imageFiles.subList(0, 10)
        println("ImageFiles size: ${imageFiles.size()}")

        worker.addImagesToPdf(imageFiles)
        worker.finish()
//        Document document = new Document(PageSize.A4, 15, 15, 15, 15);
//        PdfWriter.getInstance(document, new FileOutputStream("myFile1.pdf"));
//
//        document.open();

//        Font font = FontFactory.getFont(FontFactory.COURIER, 25, BaseColor.BLACK);
//        Chunk chunk = new Chunk("Hello World", font);
//        document.add(chunk);
/*
        def imageFiles = []

        def dir = new File("card_images/all")
        dir.eachFileRecurse(FileType.FILES) { file ->
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
        document.close();*/
    }

    void addImagesToPdf(List<File> imageFiles) {
        // 550x770
        // real card size = 63.5 x 88 mm
        // user units: 178.58268 x 249.44904
        PdfPTable table = createImagesTableFromImageFiles(imageFiles)
        document.add(table)
    }

    PdfPTable createImagesTableFromImageFiles(List<File> imageFiles) {
        List<Image> images = imageFiles.collect{return Image.getInstance(it.getAbsolutePath())}
        return createImagesTableFromImages(images)
    }

    PdfPTable createImagesTableFromImages(List<Image> images) {
        PdfPTable table = new PdfPTable(3)
//        table.setWidthPercentage(98)
        float resWidth = 535.74805f
//        float resWidth = 178.58268f * 3f
        table.setTotalWidth(resWidth)
        table.setLockedWidth(true)
//        table.setSpacingBefore(0f);
//        table.setSpacingAfter(0f);

        images.each {
//            image.scalePercent(30)
            PdfPCell cell = new PdfPCell()
            cell.setPadding(0f)
//            cell.setFixedHeight(249.44904f)
            cell.setImage(it)

//            table.addCell(it)
            table.addCell(cell)
        }
        addEmptyCellsToCompleteTableRow(images.size(), table)
        return table
    }

    private void addEmptyCellsToCompleteTableRow(int imagesSize, PdfPTable table) {
        int extraCells = (imagesSize % 3)
        extraCells = extraCells == 0 ? 0 : 3 - extraCells
        println("Adding $extraCells extra cells")
        extraCells.times {
            table.addCell("")
        }
    }

    void finish() {
        document.close()
    }
}
