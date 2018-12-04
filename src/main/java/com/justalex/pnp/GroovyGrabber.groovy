package com.justalex.pnp

import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import com.mashape.unirest.http.Unirest
import groovy.json.JsonSlurper

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.nio.charset.Charset
import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern


class GroovyGrabber {


    static void main(String[] args) {
        def dataFileJson = PnpWorker.readDataFile("src/main/resources/dataFile_unescaped.json")
//        def dataFileJson = PnpWorker.readDataFile("src/main/resources/a_10_cards.json")

        def cards = dataFileJson.en.cards.byId.collect {
            it.getValue()
        }
//        cards = cards.subList(0, 200)

//        println(cards[0])
        println("All cards: ${cards.size()}")
        cards.unique {a,b -> a.name <=> b.name}
        println("Unique cards: ${cards.size()}")

        def warband1 = cards.findAll { it.warbandId == "1" }
        println("Warband1: ${warband1.size()}")

//        def dups = warband1.clone() - warband1.unique {a,b -> a.name <=> b.name}
        def cardTypes = ["objective", "upgrade", "ploy", "gambitspell"]
//        cards.sort {it.warbandId as Integer}.groupBy { it.type }
        cards.sort{a,b -> a.warbandId as Integer <=> b.warbandId as Integer ?: cardTypes.indexOf(a.type) <=> cardTypes.indexOf(b.type)}
        printAllCards(cards)


        // todo implement card backs
        /*
        ImageWorker imageWorker = new ImageWorker()

        cards.each {it ->
            def value = it.getValue()
            def id = value.id
            println(id)
            String imageUrl = URLDecoder.decode(value.image_url, "UTF-8")
            String image_filename = imageUrl.split("/")[-1]
            println("URL: $imageUrl")
            println("imageFileName: $image_filename")
            BufferedImage imageFromUrl = imageWorker.getImageFromUrl(imageUrl)
            imageWorker.saveImage(imageFromUrl, "card_images/all/$image_filename")
//            def imageFilename = value.image_filename
//            if (imageFilename != null) {
//                assert image_filename == imageFilename
//            }
//            if (id == "L60") {
//                return true
//            } else {
//                String filename = value.image_filename
//                def image_url = value.image_url
////                def image_url = URLDecoder.decode(value.image_url, "UTF-8")
//                assert image_url.contains(filename)
//            }
//            return false
        }
        println("parsed")

//       parsedJson def list = ["a", "b", "c"]
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

    static def printAllCards(List list) {
        list.indexed().each { index, it ->
            println("$index card: setId=$it.setId, warbandId=$it.warbandId, type=$it.type, id=$it.id, name=$it.name, text=$it.rulesText")
        }
    }
}
