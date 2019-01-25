package co.com.techandsolve.scraping.helper;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public final class CookieHelper {
    private static final Map<String, String> cookiesMap = new ConcurrentHashMap<>();

    private CookieHelper() {
    }

    public static Map<String, String> getCookies() {
        return cookiesMap;
    }

    public static void setCookies(Map<String, String> cookies) {
        cookiesMap.clear();
        cookiesMap.putAll(cookies);
    }
}
