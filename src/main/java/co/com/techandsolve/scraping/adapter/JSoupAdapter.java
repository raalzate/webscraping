package co.com.techandsolve.scraping.adapter;

import co.com.techandsolve.scraping.DocumentPort;
import co.com.techandsolve.scraping.exception.DocumentException;
import co.com.techandsolve.scraping.helper.CookieHelper;
import co.com.techandsolve.scraping.scraper.MetaModel;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Locale;

public class JSoupAdapter implements DocumentPort {

    private Connection connection;
    private Connection.Response result;

    @Override
    public void connect(MetaModel model) {
        connection = Jsoup.connect(model.getAction())
                .cookies(CookieHelper.getCookies())
                .method(getMethod(model.getMethod()))
                .data(model.getData())
                .followRedirects(true);
    }

    @Override
    public void execute() {
        try {
            result = connection.execute();
        } catch (IOException e) {
            throw new DocumentException(e.getMessage(), e);
        }
    }

    @Override
    public Document parse() {
        try {
            return result.parse();
        } catch (IOException e) {
            throw new DocumentException(e.getMessage(), e);
        }
    }

    private Connection.Method getMethod(String method) {
        return Connection.Method.valueOf(method.toUpperCase(Locale.ENGLISH));
    }


}
