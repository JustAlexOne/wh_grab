package com.justalex.imageCleanup;

import org.joda.time.DateTime;

public class Test {

    public static void main(String[] args) {
        System.out.println(DateTime.now().toString("yyyy-MM-dd HH:mm zzz"));
        System.out.println(DateTime.now().toString("dd_MM_yyyy_HH_mm_ss"));
    }
}
