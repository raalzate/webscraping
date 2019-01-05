package co.com.techandsolve.scraping.utils;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class CookieUtils {
    private CookieUtils() {
    }

    public static Map<String, String> getCookies() {

        Map<String, String> cookies = new HashMap<>();

        try (FileReader fileReader = new FileReader(new File("Cookie.data"))) {
            BufferedReader buffreader = new BufferedReader(fileReader);
            String strline;
            while ((strline = buffreader.readLine()) != null) {
                cookies.put(strline.split(";")[0], strline.split(";")[1]);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cookies;
    }

    public static void setCookies(Map<String, String> cookies, File file) {
        try (BufferedWriter bufferwrite = new BufferedWriter(new FileWriter(file))) {
            file.delete();
            file.createNewFile();

            cookies.forEach((name, value) -> {
                String writeup = name + ";" + value;
                try {
                    bufferwrite.write(writeup);
                    bufferwrite.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bufferwrite.flush();
        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }
}
