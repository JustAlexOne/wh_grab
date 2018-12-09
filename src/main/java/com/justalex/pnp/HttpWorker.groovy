package com.justalex.pnp

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


class HttpWorker {

    void downloadImagesForCards(List cards, Path pathToFolderToSaveIn, String cardType) {
        println("Downloading [$cards.size][$cardType] cards")
        def imageWorker = new ImageWorker()
        cards.indexed().each { index, card ->
            def card_id = card.id
            def warbandId = card.warbandId
            println("$index: $card_id")
            def pathToFile = pathToFolderToSaveIn.resolve("${card_id}.png")
//            def pathToFile = Paths.get("card_images/power_cards/cards/$warbandId/${card_id}.png")

            Files.createDirectories(pathToFile.getParent())
            if (Files.notExists(pathToFile)) {
                Files.createFile(pathToFile)
                Files.write(pathToFile, imageWorker.getBytesFromUrl(card.image_url))
            }
        }
        println("Done")
    }

}
