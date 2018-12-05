package com.justalex.pnp

import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import groovy.io.FileType

class PdfWorker {

    static final File IMAGE_OBJECTIVE_CARD_BACK = new File("src/main/resources/data/card_backs/objective_back.png")
    static final File IMAGE_POWER_CARD_BACK = new File("src/main/resources/data/card_backs/power_back.png")

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

    public static void main(String[] args) {
        def myList = 1..27
        def a_9 = myList.collate(9)
        a_9.each { println(it) }
        println("------------------------")
        def a_3 = a_9.collect { it.collate(3) }
        // todo
        println(a_3.size())
        def a_3_reversed = a_3.collect { it.collect {it.reverse()}}
        a_3_reversed.each {println(it)}
        println("------------------------")

//        def myList = [[1, 2, 3], [1, 2, 3]]
//        myList.each { println(it) }
//        myList = myList.collect { it.reverse() }
//        myList.each { println(it) }
//
//        def missingCells = 3 - 8 % 3
//        println(missingCells)
    }

    static void main2(String[] args) {
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
        List<Image> images = imageFiles.collect { return Image.getInstance(it.getAbsolutePath()) }
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
            table.addCell(initEmptyCell())
        }
    }

    void finish() {
        document.close()
    }

    def addCardsToPdf(List cards) {
//        validateCardsImageBytes(cards)
        def cardsBy9 = cards.collate(9)
//        println("Last size: ${cardsBy9.last().size()}")
//        cardsBy9.last().each {println(it)}
        PdfPTable table = initPdfTable()
        cardsBy9.each {
            put9CardsIntoTable(it, table)
            if (it == cardsBy9.last()) {
                addEmptyCellsToCompleteTableRow(cardsBy9.size(), table)
            }

            putCardBackingsIntoTable(it, table)
            if (it == cardsBy9.last()) {
                addEmptyCellsToCompleteTableRow(cardsBy9.size(), table)
            }

        }
    }

    def putCardBackingsIntoTable(List cardsCollatedBy9, PdfPTable table) {
        def cardsSize = cardsCollatedBy9.size()
        assert cardsSize <= 9
        if (cardsSize < 9) {
            int missingCells = 3 - (cardsSize % 3)
            missingCells.times { cardsCollatedBy9.add("") }
        }
        println("Cards:")
        cardsCollatedBy9.each {println(it.id)}

        def cardsCollatedBy3 = cardsCollatedBy9.collect {it.collate(3)}
        def cardsCollatedBy3_reversed = cardsCollatedBy3.collect {it.collect {it.reverse()}}

        cardsCollatedBy3_reversed.each {println(it)}

        /*cardsCollatedBy3.each { card ->
            def cell = initTableCellWithImage(Image.getInstance(IMAGE_POWER_CARD_BACK.toURI().toURL()))
            if (card == "") {
                cell = initEmptyCell()
            }
            if (card.type.equalsIgnoreCase(CardType.OBJECTIVE)) {
                cell = initTableCellWithImage(Image.getInstance(IMAGE_OBJECTIVE_CARD_BACK.toURI().toURL()))
            }
//            table.addCell(it)
            table.addCell(cell)
        }*/
//        def a_3_reversed = a_3.collect { it.collect {it.reverse()}}
//        a_3_reversed.each {println(it)}



    }

    PdfPCell initEmptyCell() {
        return new PdfPCell(new Phrase(""))
    }

    private PdfPCell initTableCellWithImage(Image image) {
        PdfPCell cell = new PdfPCell()
        cell.setPadding(0f)
        cell.setImage(image)
        return cell
    }

    private PdfPTable initPdfTable() {
        def table = new PdfPTable(3)
        //        table.setWidthPercentage(98)
        float resWidth = 535.74805f
//        float resWidth = 178.58268f * 3f
        table.setTotalWidth(resWidth)
        table.setLockedWidth(true)
//        table.setSpacingBefore(0f);
//        table.setSpacingAfter(0f);
        return table
    }

    def put9CardsIntoTable(List cardsCollatedBy9, PdfPTable table) {
        assert cardsCollatedBy9.size() <= 9
        cardsCollatedBy9.each { card ->
//            image.scalePercent(30)
//            PdfPCell cell = new PdfPCell()
//            cell.setPadding(0f)
//            cell.setFixedHeight(249.44904f)
//            cell.setImage(Image.getInstance(card.imageBytes))
            def cell = initTableCellWithImage(Image.getInstance(card.imageBytes))
//            table.addCell(it)
            table.addCell(cell)
        }
    }

    private void validateCardsImageBytes(List cards) {
        def nullBytesCard = cards.find { it.imageBytes == null }
        if (nullBytesCard != null) {
            throw new IllegalStateException("Null bytes for card: $nullBytesCard")
        }
    }
}
