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

        def text = new String("\"name\": \"\\u901a\\u7528\",".getBytes("UTF-8"), "UTF-8")

        println(text)
//        def allCardsJson = new JsonSlurper().parseText(text)
//        def allCardsJson = new JsonSlurper().parseText(allCardsFile.text)
        println("Hello")
//        println(allCardsJson.S034.colourText)
    }



}
