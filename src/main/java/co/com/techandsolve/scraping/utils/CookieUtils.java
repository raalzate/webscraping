package co.com.techandsolve.scraping.utils;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class CookieUtils {
    private static Map<String, String> __cookies = new HashMap<>();
    private CookieUtils() {
    }

    public static Map<String, String> getCookies() {
        return __cookies;
    }

    public static void setCookies(Map<String, String> cookies) {
        __cookies = cookies;
    }
}
