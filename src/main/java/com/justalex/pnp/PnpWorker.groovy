package com.justalex.pnp

import groovy.json.JsonSlurper

class PnpWorker {

    static Object readDataFile(String filePath) {
        def dataFile = new File(filePath)
        println(dataFile.getAbsolutePath())
        assert dataFile.exists()

        def dataFileText = dataFile.text
//        dataFileText = StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeJava(dataFileText))
        return new JsonSlurper().parseText(dataFileText)
    }

}
