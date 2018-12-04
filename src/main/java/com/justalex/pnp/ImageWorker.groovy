package com.justalex.pnp

import com.mashape.unirest.http.Unirest

import javax.imageio.ImageIO
import java.awt.image.BufferedImage


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

}
