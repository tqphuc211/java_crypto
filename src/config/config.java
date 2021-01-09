package config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class config {
    public static final String PUBLIC_KEY_PATH = "public_key_path";
    public static final String PRIVATE_KEY_PATH = "private_key_path";

    public static Map<String, String> release = new HashMap<String, String>() {{
        put(PUBLIC_KEY_PATH, "public.key");
        put(PRIVATE_KEY_PATH, "private.key");
    }};

    public static void log(String msg) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("\n\n----------------\n" + dtf.format(now) + ": " + msg);
    }
}
