package com.jian.untils;

import java.util.UUID;

public class Uuid {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}