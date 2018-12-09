package com.justalex.pnp;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RemoveMe {

    public static void main(String[] args) {
        Path path = Paths.get("card_images").resolve("power_cards");// todo need to extend path
        System.out.println(path);

    }

}
