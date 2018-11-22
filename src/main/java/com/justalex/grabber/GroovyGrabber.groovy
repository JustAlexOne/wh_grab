package com.justalex.grabber

import groovy.json.JsonSlurper


class GroovyGrabber {


    static void main(String[] args) {
        def slurper = new JsonSlurper()
        def allCardsFile = new File("src/main/resources/allCards.json")
//        def allCardsFile = new File("src/main/resources/small.json")
        println(allCardsFile.getAbsolutePath())
        assert allCardsFile.exists()
        def allCardsJson = new JsonSlurper().parseText(allCardsFile.text)
//        def allCardsJson = new JsonSlurper().parse(allCardsFile, 'UTF-8')
//        def allCardsJson = new JsonSlurper().parseText(allCardsFile.text)
        println("Hello")
//        println(allCardsJson.S034.colourText)
    }

}
