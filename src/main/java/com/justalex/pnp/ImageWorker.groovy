package com.justalex.pnp

import com.mashape.unirest.http.Unirest

import javax.imageio.ImageIO
import java.awt.image.BufferedImage


class ImageWorker {

    BufferedImage getImageFromUrl(String url) {
        def inputStream = Unirest.get(url).asBinary().getBody()
        return ImageIO.read(inputStream)
    }

    void saveImage(BufferedImage img, String destination) {
        ImageIO.write(img, "PNG", new File(destination))
    }

}
