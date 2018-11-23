package com.justalex.grabber

import groovy.json.JsonSlurper

import java.nio.charset.Charset


class GroovyGrabber {


    static void main(String[] args) {
        def allCardsFile = new File("src/main/resources/allCards.json")
//        def allCardsFile = new File("src/main/resources/small.json")
        println(allCardsFile.getAbsolutePath())
        assert allCardsFile.exists()
//        def allCardsJson = new JsonSlurper().parseText(allCardsFile.text)

        def text = new String(allCardsFile.getText())
        print(text)
        def allCardsJson = new JsonSlurper().parseText(text)
//        def allCardsJson = new JsonSlurper().parseText(allCardsFile.text)
        println("Hello")
//        println(allCardsJson.S034.colourText)
    }



}
