package com.justalex.grabber

import groovy.json.JsonSlurper

import java.util.regex.Pattern


class GroovyGrabber {


    static void main(String[] args) {
        def dataFileJson = readDataFile()
        def warbands = dataFileJson.en.warbands.byId
        def cards = dataFileJson.en.cards.byId
//        println(dataFileJson.en.warbands.byId["7"].name) // works!!!
//        println(dataFileJson.en.warbands.byId.collect {println(it)}) // works!!!
//        println(dataFileJson.en.cards.byId.S001) // works!!!
        println(cards["S007"].name)
        // todo unescape Java and work with \x...
        println("Hello")
    }

    private static Object readDataFile() {
        def dataFile = new File("src/main/resources/dataFile_unescaped.json")
        println(dataFile.getAbsolutePath())
        assert dataFile.exists()

        def dataFileText = dataFile.text
//        dataFileText = StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeJava(dataFileText))
        return new JsonSlurper().parseText(dataFileText)
    }

    private static int countMatches(String text, String pattern) {
        def compile = Pattern.compile(pattern)
        def matcher = compile.matcher(text)
        int count = 0
        while (matcher.find()) {
            count++
        }
        println("Matches of [${pattern}]: ${count}")
        return count
    }


}
