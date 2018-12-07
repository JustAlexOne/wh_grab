package com.justalex.pnp

import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import com.mashape.unirest.http.Unirest
import groovy.io.FileType
import groovy.json.JsonSlurper

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern


class GroovyGrabber {


    static void main(String[] args) {

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

        def imageWorker = new ImageWorker()

        println("Downloading objective cards")
//        objective_cards = objective_cards.subList(0, 3)
        objective_cards.indexed().each { index, card ->
            def card_id = card.id
            def warbandId = card.warbandId
            // todo try putting each warband in separate folder
            println("$index: $card_id")
            Files.write(Paths.get("card_images/objective_cards/cards/$warbandId/${card_id}.png"), imageWorker.getBytesFromUrl(card.image_url))
        }

        println("Downloading power cards")
//        power_cards = objective_cards.subList(0, 3)
        power_cards.indexed().each { index, card ->
            def card_id = card.id
            println("$index: $card_id")
            Files.write(Paths.get("card_images/power_cards/cards/${card_id}.png"), imageWorker.getBytesFromUrl(card.image_url))
        }
        assert new File("card_images/objective_cards/cards").listFiles().length == 247
        assert new File("card_images/power_cards/cards").listFiles().length == 495
//        byte[] bytes = imageWorker.getBytesFromUrl(objective_cards[0].image_url)
//        Files.write(Paths.get("card1.png"), bytes)

    }

    static void main2(String[] args) {
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
//        Collections.shuffle(cards)
//        cards = cards.subList(0,6)

        imageWorker.downloadAndSetImagesForCards(cards)

        pdfWorker.addCardsToPdf(cards)

        pdfWorker.finish() // todo uncomment
        pdfWorker.insertPageNumbers(destinationFile, "cards_pnp_numbered.pdf")

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
