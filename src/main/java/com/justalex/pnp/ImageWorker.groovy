package com.justalex.pnp

import com.mashape.unirest.http.Unirest

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


class ImageWorker {

    BufferedImage getImageFromUrl(String url) {
        InputStream inputStream = getStreamFromUrl(url)
        return ImageIO.read(inputStream)
    }

    byte[] getBytesFromUrl(String url) {
        return getStreamFromUrl(url).bytes
    }

    private InputStream getStreamFromUrl(String url) {
        def inputStream = Unirest.get(url).asBinary().getBody()
        inputStream
    }

    void saveImage(BufferedImage img, String destination) {
        ImageIO.write(img, "PNG", new File(destination))
    }

    void downloadAndSetImagesForCards(List cards) {
        println("Downloading [$cards.size] cards.")
        cards.indexed().each { index, it ->
            println("$index: id: $it.id, card: $it.name")
            def bytes = getBytesFromUrl(it.image_url)
            it.imageBytes = bytes
        }
        println("Done downloading cards")
    }

    List<BufferedImage> wrapImagesWithBorder(List<BufferedImage> imagesList, int border, Color borderColor) {
        return imagesList.collect {
            wrapImageWithBorder(it, border, borderColor)
        }
    }

    BufferedImage wrapImageWithBorder(BufferedImage image, int border, Color borderColor) {
        int dimensionIncrement = 2 * border;
        BufferedImage newImage = new BufferedImage(image.getWidth() + dimensionIncrement, image.getHeight() + dimensionIncrement, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newImage.createGraphics();
        g2d.setPaint(borderColor);
        g2d.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());

        g2d.drawImage(image, border, border, null);
        return newImage;
    }

    void wrapImagesAndSaveToFolder(List cards, Path destinationFolder, int borderSize, String cardType) {
        assert destinationFolder.toString().contains(borderSize as String)
        assert destinationFolder.toString().contains(cardType)

        println("Wrapping and saving images for [$cards.size] $cardType cards to folder [$destinationFolder]")
        cards.each {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(it.imageBytes))
            def newImage = wrapImageWithBorder(image, borderSize, Color.WHITE)
            Path pathToFile = destinationFolder.resolve("${it.id}_${borderSize}.png")
            println("pathToFile: $pathToFile")
            createPath(pathToFile)
            ImageIO.write(newImage, "PNG", pathToFile.toFile())
        }
    }

    void wrapCardBackWithBorder(File cardBackImage, Path destinationFolder, int borderSize, String cardType) {
        assert destinationFolder.toString().contains(borderSize as String)
        assert destinationFolder.toString().contains(cardType)

        println("Wrapping card back [${cardBackImage.getName()}] to folder [$destinationFolder]")
        BufferedImage image = ImageIO.read(cardBackImage)
        def newImage = wrapImageWithBorder(image, borderSize, Color.WHITE)
        Path pathToFile = destinationFolder.resolve(cardBackImage.getName())
        println("pathToFile: $pathToFile")
        createPath(pathToFile)
        ImageIO.write(newImage, "PNG", pathToFile.toFile())
    }

    private void createPath(Path pathToFile) {
        Files.createDirectories(pathToFile.getParent())
        if (Files.notExists(pathToFile)) {
            Files.createFile(pathToFile)
        }
    }

}
