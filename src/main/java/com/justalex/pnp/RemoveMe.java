package com.justalex.pnp;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RemoveMe {

    public static void main(String[] args) {
        Path path = Paths.get("card_images","power_cards/test.png");
//        Path path = Paths.get("card_images").resolve("power_cards/test.png");
//        Path path = Paths.get("card_images").resolve("power_cards").resolve("test.png");// todo need to extend path
        System.out.println(path.toFile().getName());
        System.out.println("path = " + path);
//        File file = new File(path.toFile().getParentFile(), path.toFile().getName().replace(".png", "_2.png"));
//        System.out.println(path);
//        System.out.println(file.getAbsolutePath());

    }

}
