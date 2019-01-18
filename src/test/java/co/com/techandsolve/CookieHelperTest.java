package co.com.techandsolve;

import co.com.techandsolve.scraping.helper.CookieHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CookieHelperTest {
    @BeforeClass
    public static void init() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        CookieHelper.setCookies(map);
    }

    @Test
    public void validCookies() {
        Map<String, String> map = CookieHelper.getCookies();
        assert "value".equals(map.get("key"));
    }
}
