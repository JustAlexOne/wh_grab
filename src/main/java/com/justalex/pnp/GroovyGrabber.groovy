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
        def dataFileJson = PnpWorker.readDataFile("src/main/resources/dataFile.json")
//        def dataFileJson = PnpWorker.readDataFile("src/main/resources/a_10_cards.json")
        def cards = dataFileJson.en.cards.byId
//        def warbands = dataFileJson.en.warbands.byId
//        println("Warbands total: ${warbands.size()}")
        println("Cards total: ${cards.size()}")
//        warbands.each {println(it)}

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
        println(cards.S001.image_url)
        String str = cards.S001.image_url
        println("Str: $str")

//        def imageWorker = new ImageWorker()
//        BufferedImage imageFromUrl = imageWorker.getImageFromUrl(str)
//        imageWorker.saveImage(imageFromUrl, "src/main/resources/m_img.png")


        def bytes = Unirest.get(str).asBinary().getBody().bytes
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("iTextImageExample.pdf"));
        document.open();
        Image img = Image.getInstance(bytes);
        Image img2 = Image.getInstance(bytes);
//        Image img = Image.getInstance(path.toAbsolutePath().toString());
        document.add(img);
        document.add(img2);

        document.close();

        println("Done!")
    }


}
