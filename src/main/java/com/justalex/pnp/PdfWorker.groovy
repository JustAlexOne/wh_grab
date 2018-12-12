package com.justalex.pnp

import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Phrase
import com.itextpdf.text.RectangleReadOnly
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import com.itextpdf.text.pdf.PdfWriter
import groovy.io.FileType

class PdfWorker {

    static final File IMAGE_OBJECTIVE_CARD_BACK = new File("src/main/resources/data/card_backs/objective_back.png")
    static final File IMAGE_POWER_CARD_BACK = new File("src/main/resources/data/card_backs/power_back.png")
    static final File IMAGE_EMPTY = new File("src/main/resources/data/card_backs/empty_card.png")
    private int columns;
    private File destinationFile
    Document document

    PdfWorker(File destinationFile, int columns) {
        this.destinationFile = destinationFile
        this.columns = columns
        document = new Document(new RectangleReadOnly(907,1276), 15, 15, 20, 20)
//        document = new Document(PageSize.A3, 5, 5, 5, 5)
        println("Page size: ${document.getPageSize()}")
        // A4 - Page size:  w: 595 h: 842
        // A3 - Page size:  w: 842 h: 1191 - 297 - 420
        // index - 2.8357 * 450 = 1276
        // index - 2.8357 * 320 = 907
        PdfWriter.getInstance(document, new FileOutputStream(destinationFile))
        document.open()
    }

    PdfWorker(String fileName, int columns) {
        this(new File(fileName), columns)
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

    private void addEmptyCellsToCompleteTableRow(int imagesSize, PdfPTable table) {
        int extraCells = (imagesSize % columns)
        extraCells = extraCells == 0 ? 0 : columns - extraCells
        println("Adding $extraCells extra cells")
        extraCells.times {
            table.addCell(initEmptyCell())
        }
    }

    void finish() {
        document.close()
    }

    def addCardsToPdf(List cards) {
        println("Adding cards to pdf")
        validateCardsImageBytes(cards)
        def cardsBy9 = cards.collate(columns)
//        println("Last size: ${cardsBy9.last().size()}")
//        cardsBy9.last().each {println(it)}
        PdfPTable table = initPdfTable()
        cardsBy9.each {
            put9CardsIntoTable(it, table)
            if (it == cardsBy9.last()) {
                addEmptyCellsToCompleteTableRow(it.size(), table)
            }

            putCardBackingsIntoTable(it, table)
//            if (it == cardsBy9.last()) {
//                addEmptyCellsToCompleteTableRow(cardsBy9.size(), table)
//            }
        }
        document.add(table)
    }

    def putCardBackingsIntoTable(List list_9_cards, PdfPTable table) {
        println("Putting card backings")
        def cardsSize = list_9_cards.size()
        assert cardsSize <= columns
        if (cardsSize < columns) {
            int missingCells = columns - (cardsSize % columns)
            missingCells.times { list_9_cards.add("") }
        }
//        println("Cards: $list_9_cards")
//        list_9_cards.each {println(it.id)}

        def list_by_3_cards = list_9_cards.collate(3)
//        println("Basic")
//        list_by_3_cards.each { println(it) }
        def list_by_3_cards_reversed = list_by_3_cards.collect { it.reverse() }
//        println("Reversed")
//        list_by_3_cards_reversed.each { println(it) }
        /*
        def cardsCollatedBy3_reversed = cardsCollatedBy3.collect {it.collect {it.reverse()}}

        cardsCollatedBy3_reversed.each {println(it)}*/
//        println("list_by_3_cards_reversed.class = ${list_by_3_cards_reversed.getClass()}")
//        list_by_3_cards_reversed.each { println(it.size()) }

        list_by_3_cards_reversed.flatten().each { card ->
            def cell
            if (card == "") {
                cell = initEmptyCell()
            } else if (card.type.equalsIgnoreCase(CardType.OBJECTIVE.toString())) {
                cell = initTableCellWithImage(Image.getInstance(IMAGE_OBJECTIVE_CARD_BACK.toURI().toURL()))
            } else {
                cell = initTableCellWithImage(Image.getInstance(IMAGE_POWER_CARD_BACK.toURI().toURL()))
            }
            table.addCell(cell)
        }
    }

    PdfPCell initEmptyCell() {
        return initTableCellWithImage(Image.getInstance(IMAGE_EMPTY.toURI().toURL()))
//        return new PdfPCell(new Phrase("empty"))
    }

    private PdfPCell initTableCellWithImage(Image image) {
        PdfPCell cell = new PdfPCell()
//        cell.setPadding(0.278f)
        cell.setPadding(0.35f)
//        cell.setPaddingBottom(0.1f)
//        cell.setPaddingTop(0.1f)
//        cell.setPaddingLeft(0.1f)
//        cell.setPaddingRight(0.1f)
        cell.setBorderColor(BaseColor.BLACK)
//        cell.setBorderWidth(5f)

        cell.setImage(image)
        return cell
    }

    private PdfPTable initPdfTable() {
        def table = new PdfPTable(3)
//                table.setWidthPercentage(98)
//        float resWidth = 655.74805f
        float resWidth = 535.74805f
//        resWidth = 540f
        println("Table width: $resWidth")
//        float resWidth = 178.58268f * 3f
        table.setTotalWidth(resWidth)
        table.setLockedWidth(true)
//        table.setSpacingBefore(0f);
//        table.setSpacingAfter(0f);
        return table
    }

    def put9CardsIntoTable(List cardsCollatedBy9, PdfPTable table) {
        assert cardsCollatedBy9.size() <= 9
        println("Putting ${cardsCollatedBy9.size()} cards")
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

    void insertPageNumbers(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src)
        int n = reader.getNumberOfPages()
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest))
        PdfContentByte pagecontent
        for (int i = 0; i < n; ) {
            pagecontent = stamper.getOverContent(++i)
            ColumnText.showTextAligned(pagecontent, Element.ALIGN_LEFT,
                    new Phrase(String.format("%s of %s", i, n)), 20, 20, 0)
        }
        stamper.close()
        reader.close()
    }

    private void validateCardsImageBytes(List cards) {
        def nullBytesCard = cards.find { it.imageBytes == null }
        if (nullBytesCard != null) {
            throw new IllegalStateException("Null bytes for card: $nullBytesCard")
        }
    }
}
