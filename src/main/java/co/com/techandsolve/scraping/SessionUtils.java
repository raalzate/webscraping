package co.com.techandsolve.scraping;

import org.openqa.selenium.Cookie;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class SessionUtils {
    private SessionUtils() {
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

    public static Proxy getProxy() {
        SocketAddress socketAddress = new InetSocketAddress("localhost", 5555);
        return new Proxy(Proxy.Type.SOCKS, socketAddress);
    }

    public static void setCookies(Set<Cookie> cookies, File file) {
        try (BufferedWriter bufferwrite = new BufferedWriter(new FileWriter(file))) {
            file.delete();
            file.createNewFile();

            for (Cookie cook : cookies) {
                String writeup = cook.getName() + ";" + cook.getValue();
                bufferwrite.write(writeup);
                bufferwrite.newLine();
            }
            bufferwrite.flush();
        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }
}
