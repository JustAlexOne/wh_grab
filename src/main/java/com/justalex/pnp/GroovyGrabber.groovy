package com.justalex.pnp

import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import com.mashape.unirest.http.Unirest
import groovy.io.FileType
import groovy.json.JsonSlurper

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern

import static com.justalex.pnp.CardType.OBJECTIVE


class GroovyGrabber {

    static void main(String[] args) {
        saveAndWrapWithBorder()
    }

    static void saveAndWrapWithBorder() {
        // save card images to files
        def dataFileJson = PnpWorker.readDataFile("src/main/resources/dataFile_unescaped.json")
        def cards = dataFileJson.en.cards.byId.collect { it.getValue() }
        println("All cards: ${cards.size()}")
        cards.unique { a, b -> a.name <=> b.name }
        println("Unique cards: ${cards.size()}")
//        cards.sort { a, b -> a.warbandId as Integer <=> b.warbandId as Integer }
//        printAllCards(cards)
        def objective_cards = cards.findAll { it.type == "objective" }
        println("objective_cards: $objective_cards.size")
        def power_cards = cards.findAll { it.type != "objective" }
        println("power_cards: $power_cards.size")

        objective_cards = objective_cards.subList(0, 10)
        power_cards = power_cards.subList(0, 10)
        def imageWorker = new ImageWorker()

//        String baseFolder = "card_images2"
//        def pathForObjectiveCards = Paths.get(baseFolder, "objective_cards/cards")
//        def pathToPowerCards = Paths.get(baseFolder, "power_cards/cards")
        imageWorker.downloadAndSetImagesForCards(objective_cards)
        imageWorker.downloadAndSetImagesForCards(power_cards)

//        def objectiveCardsFiles = Files.list(pathForObjectiveCards).findAll {!it.getFileName().toString().startsWith(".")}
//        def powerCardsFiles = Files.list(pathToPowerCards).findAll {!it.getFileName().toString().startsWith(".")}
//        assert objectiveCardsFiles.size() == 3 // 247
//        assert powerCardsFiles.size() == 3 // 495
//        assert objectiveCardsFiles.size() == 247
//        assert powerCardsFiles.size() == 495
//        imageWorker.wrapImagesAndSaveToFolder(objective_cards, Paths.get("card_images_50/objective_cards/cards"), 50, "objective")
//        imageWorker.wrapImagesAndSaveToFolder(power_cards, Paths.get("card_images_50/power_cards/cards"), 50, "power")
//        imageWorker.wrapCardBackWithBorder(new File("src/main/resources/data/card_backs/objective_back.png"), Paths.get("card_images_50/objective_cards"), 50, "objective")
//        imageWorker.wrapCardBackWithBorder(new File("src/main/resources/data/card_backs/power_back.png"), Paths.get("card_images_50/power_cards"), 50, "power")
//        wrapAllWithBorder(objective_cards, power_cards, 30)
//        wrapAllWithBorder(objective_cards, power_cards, 40)
        wrapAllWithBorder(objective_cards, power_cards, 50)
//        wrapAllWithBorder(objective_cards, power_cards, 60)
    }

    def static wrapAllWithBorder(List objective_cards, List power_cards, int border) {
        def imageWorker = new ImageWorker()
        imageWorker.wrapImagesAndSaveToFolder(objective_cards, Paths.get("card_images_$border/objective_cards/cards"), border, "objective")
        imageWorker.wrapImagesAndSaveToFolder(power_cards, Paths.get("card_images_$border/power_cards/cards"), border, "power")
        imageWorker.wrapCardBackWithBorder(new File("src/main/resources/data/card_backs/objective_back.png"), Paths.get("card_images_$border/objective_cards"), border, "objective")
        imageWorker.wrapCardBackWithBorder(new File("src/main/resources/data/card_backs/power_back.png"), Paths.get("card_images_$border/power_cards"), border, "power")

    }

    static void saveImagesToFolder() {
        // save card images to files
        def dataFileJson = PnpWorker.readDataFile("src/main/resources/dataFile_unescaped.json")
        def cards = dataFileJson.en.cards.byId.collect { it.getValue() }
        println("All cards: ${cards.size()}")
        cards.unique { a, b -> a.name <=> b.name }
        println("Unique cards: ${cards.size()}")

//        cards.sort { a, b -> a.warbandId as Integer <=> b.warbandId as Integer }
//        printAllCards(cards)

        def objective_cards = cards.findAll { it.type == "objective" }
        println("objective_cards: $objective_cards.size")

        def power_cards = cards.findAll { it.type != "objective" }
        println("power_cards: $power_cards.size")

//        objective_cards = objective_cards.subList(0, 3)
//        power_cards = power_cards.subList(0, 3)
        def httpWorker = new HttpWorker()
        def pathForObjectiveCards = Paths.get("card_images/objective_cards/cards")
        def pathToPowerCards = Paths.get("card_images/power_cards/cards")
        httpWorker.downloadImagesForCards(objective_cards, pathForObjectiveCards, OBJECTIVE.name().toLowerCase())
        httpWorker.downloadImagesForCards(power_cards, pathToPowerCards, "power")

        def objectiveCardsFiles = Files.list(pathForObjectiveCards).findAll {
            !it.getFileName().toString().startsWith(".")
        }
        def powerCardsFiles = Files.list(pathToPowerCards).findAll { !it.getFileName().toString().startsWith(".") }
//        assert objectiveCardsFiles.size() == 3 // 247
//        assert powerCardsFiles.size() == 3 // 495
        assert objectiveCardsFiles.size() == 247
        assert powerCardsFiles.size() == 495
        /*
        def imageWorker = new ImageWorker()

        objectiveCardsFiles.each {
            def image = ImageIO.read(it)
            def newImage = imageWorker.wrapImageWithBorder(image, 2, Color.RED)
            def destFile = new File("card_images/objective_cards/cards_2", it.getName().replace(".png", "_2.png"))

            println("destFile: $destFile")
            ImageIO.write(newImage, "PNG", destFile)
        }

//        def bufferedImage = ImageIO.read(file)
//        def imageWithBorder = imageWorker.wrapImageWithBorder(bufferedImage, 2, Color.RED)
//        ImageIO.write(bufferedImage, "PNG", )


//        assert new File("card_images/power_cards/cards").listFiles().length == 495

//        println("Downloading objective cards")
//        objective_cards = objective_cards.subList(0, 5)
        // todo use method from HttpWorker
        /* objective_cards.indexed().each { index, card ->
             def card_id = card.id
             def warbandId = card.warbandId
             println("$index: $card_id")
             def pathToFile = Paths.get("card_images/objective_cards/cards/$warbandId/${card_id}.png")
             Files.createDirectories(pathToFile.getParent())
             Files.createFile(pathToFile)
             Files.write(pathToFile, imageWorker.getBytesFromUrl(card.image_url))
         }

         println("Downloading power cards")
 //        power_cards = power_cards.subList(0, 5)
         power_cards.indexed().each { index, card ->
             def card_id = card.id
             def warbandId = card.warbandId
             println("$index: $card_id")
             def pathToFile = Paths.get("card_images/power_cards/cards/$warbandId/${card_id}.png")
             Files.createDirectories(pathToFile.getParent())
             Files.createFile(pathToFile)
             Files.write(pathToFile, imageWorker.getBytesFromUrl(card.image_url))
         }*/
        println("Done")
    }

    static void saveToPdf() {
        def dataFileJson = PnpWorker.readDataFile("src/main/resources/dataFile_unescaped.json")
//        def dataFileJson = PnpWorker.readDataFile("src/main/resources/a_10_cards.json")

        def cards = dataFileJson.en.cards.byId.collect {
            it.getValue()
        }
//        cards = cards.subList(0, 200)

//        println(cards[0])
        println("All cards: ${cards.size()}")
        cards.unique { a, b -> a.name <=> b.name }
        println("Unique cards: ${cards.size()}")
//        println("Leaders: ${cards.findAll{it.setId == "7"}.size()}")
        // 248 + 116 + 378 = 742 unique cards

        /*All cards: 814
        Unique cards: 742
        Leaders: 60*/
//        printWarbands(cards)

        def cardTypes = ["objective", "upgrade", "ploy", "gambitspell"]
        //todo make tests, and then change to use enum instrad of list cardTypes
        cards.sort { a, b -> a.warbandId as Integer <=> b.warbandId as Integer ?: cardTypes.indexOf(a.type) <=> cardTypes.indexOf(b.type) }
//        printAllCards(cards)

        def destinationFile = "cards_pnp.pdf"
        def pdfWorker = new PdfWorker(destinationFile)
        def imageWorker = new ImageWorker()
//        cards = cards.findAll{it.id in ["S010", "S011", "S004", "S005", "S020", "S021", "S022", "NV471", "S001", "S002", "S003", "S007"]}
//        println(cards.collect {"$it.id type: $it.type"})
        Collections.shuffle(cards)
        cards = cards.subList(0, 18)

        imageWorker.downloadAndSetImagesForCards(cards)

        pdfWorker.addCardsToPdf(cards, 6, 3)

        pdfWorker.finish() // todo uncomment
//        pdfWorker.insertPageNumbers(destinationFile, "cards_pnp_numbered.pdf")

        // todo implement card backs
        /*
            def list = ["a", "b", "c"]
//        def myMap = [user1:[name:"John", age: 19], user2:[name:"Jack", age: 21], user3:[name:"Mike", age: 27]]
//        list.each {println(it)}
//        myMap.each {println(it)}
//        println("=======================================")
//        myMap.each{ k, v -> println "${k}:${v}" }
//        println(warbands.each {println(it)})

//        println(dataFileJson.en.warbands.byId["7"].name) // works!!!
//        println(dataFileJson.en.warbands.byId.collect {println(it)}) // works!!!
//        println(dataFileJson.en.cards.byId.S001) // works!!!
//        println(cards["S007"].name)
        // todo unescape Java and work with \x...
//        println(cards.S001.image_url)
//        String str = cards.S001.image_url

        /*def bytes = Unirest.get(str).asBinary().getBody().bytes
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("iTextImageExample.pdf"));
        document.open();
        Image img = Image.getInstance(bytes);
        Image img2 = Image.getInstance(bytes);
//        Image img = Image.getInstance(path.toAbsolutePath().toString());
        document.add(img);
        document.add(img2);

        document.close();
*/
        println("Done!")
    }

    static int countFilesInDirectory(File baseDir) {
        def files = [];

        def processFileClosure = {
            println "working on ${it.canonicalPath}: "
            files.add(it.canonicalPath);
        }

        baseDir.eachFileRecurse(FileType.FILES, processFileClosure);
    }

    private static void printWarbands(cards) {
        def warbandsList = new JsonSlurper().parseText(new File("src/main/resources/data/warbands_en.json").text).warbands.byId.collect {
            it.getValue()
        }
        warbandsList.each { warband ->
            println("Warband name=$warband.name, id=$warband.id cards: ${cards.findAll { card -> card.warbandId == warband.id }.size()}")
        }
    }

    static def printAllCards(List list) {
        list.indexed().each { index, it ->
            println("$index card: setId=$it.setId, warbandId=$it.warbandId, type=$it.type, id=$it.id, name=$it.name, text=$it.rulesText")
        }
    }
}
