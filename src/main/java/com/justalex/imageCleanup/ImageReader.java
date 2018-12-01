package com.justalex.imageCleanup;

import org.joda.time.DateTime;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ImageReader {

    public static final int RED = 16;
    public static final int GREEN = 8;
    public static final int BLUE = 0;

    public static void main(String[] args) throws IOException {
        String pathPrefix = "src/main/java/com/nominum/utils/card_lib/";
        String fileName = "001-NV_ENG";
        String fileExtension = ".png";
        File input = new File(pathPrefix + fileName + fileExtension);
        BufferedImage image = ImageIO.read(input);
        int height = image.getHeight();
        int width = image.getWidth();
        int totalPixels = height * width;
        System.out.println("totalPixels = " + totalPixels);

        Stream<Point> points = getPointStream(image);
        List<Color> colorsList = points.map(point -> image.getRGB(point.x, point.y)).map(ImageReader::getColor).collect(Collectors.toList());
        Map<Color, Long> res = colorsList.stream().collect(Collectors.groupingBy(it -> it, Collectors.counting()));
        System.out.println("res.size() = " + res.size());
//        res.entrySet().stream().sorted(Comparator.comparing(Map.Entry<Color, Long>::getValue)).forEach(System.out::println);
//        List<Color> colorsToRemove = res.entrySet().stream().sorted(Comparator.comparing(Map.Entry<Color, Long>::getValue).reversed()).limit(6).map(Map.Entry::getKey).collect(Collectors.toList());
        List<Color> colorsToRemove = getColorsToRemove();
        List<Color> colorsToKeep = getColorsToKeep();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
//                removeColorsToRemove(image, colorsToRemove, y, x);
//                keepBlackColors(image, x, y);
                keepColorsToKeep(image, colorsToKeep, y, x);
            }
        }
        ImageIO.write(image, "png", new File(pathPrefix + "result_" + DateTime.now().toString("dd_MM_yyyy_HH_mm_ss") + fileExtension));
    }

    private static void keepBlackColors(BufferedImage image, int x, int y) {
        Color color = getColor(image.getRGB(x, y));
        if (color.getRed() > 10 && color.getBlue() > 10 && color.getGreen() > 10) {
            image.setRGB(x, y, Color.WHITE.getRGB());

        }
    }


    private static void removeColorsToRemove(BufferedImage image, List<Color> colorsToRemove, int y, int x) {
        if (colorsToRemove.contains(getColor(image.getRGB(x, y)))) {
            image.setRGB(x, y, Color.WHITE.getRGB());
        }
    }

    private static void keepColorsToKeep(BufferedImage image, List<Color> colorsToKeep, int y, int x) {
        if (!colorsToKeep.contains(getColor(image.getRGB(x, y)))) {
            image.setRGB(x, y, Color.WHITE.getRGB());
        }
    }

    private static List<Color> getColorsToKeep() {
        return Arrays.asList(
            new Color(2, 3, 2),
            new Color(16, 16, 13),
            new Color(40, 41, 30),
            new Color(47, 50, 37),
            new Color(66, 71, 59),
            new Color(16, 16, 13),
            new Color(111, 116, 97),
            new Color(138, 139, 124),
            new Color(152, 152, 141),
            new Color(124, 128, 110),
            new Color(99, 105, 85),
            new Color(64, 67, 47),
            new Color(82, 88, 69),
            new Color(90, 96, 76),
            new Color(99, 105, 85),
//            new Color(163, 164, 157),
            new Color(54, 57, 44),
//            new Color(103, 107, 90),
//            new Color(7, 8, 6)
            new Color(28, 29, 21)
        );
    }

    private static List<Color> getColorsToRemove() {
        return Arrays.asList(
            new Color(213, 209, 200),
            new Color(213, 209, 200)
        );
    }

    private static Stream<Point> getPointStream(BufferedImage image) {
        return IntStream.range(0, image.getHeight())
                .mapToObj(y -> IntStream.range(0, image.getWidth())
                    .mapToObj(x -> new Point(x, y)))
                .flatMap(Function.identity());
    }

    private static Color getColor(int rgb) {
        int red = rgb >> RED & 0xff;
        int green = rgb >> GREEN & 0xff;
        int blue = rgb >> BLUE & 0xff;
        return new Color(red, green, blue);
    }



}
