package co.com.techandsolve.example;

import co.com.techandsolve.example.BlogTnSWebScraping;
import co.com.techandsolve.scraping.infra.HtmlUnitAdapter;
import co.com.techandsolve.scraping.infra.ScraperCommand;
import co.com.techandsolve.scraping.infra.JSoupAdapter;
import org.junit.Test;

import java.util.Map;


public class FunctionalTest {
    @Test
    public void blogTnS_JSoupAdapter(){
        JSoupAdapter adapter = new JSoupAdapter();
        ScraperCommand scraperCommand = new ScraperCommand();
        Map<String, Object> result = scraperCommand.execute(new BlogTnSWebScraping(adapter));
        System.out.println(result);
    }

    @Test
    public void blogTnS_HtmlUnitAdapter(){
        HtmlUnitAdapter adapter = new HtmlUnitAdapter();
        ScraperCommand scraperCommand = new ScraperCommand();
        Map<String, Object> result = scraperCommand.execute(new BlogTnSWebScraping(adapter));
        System.out.println(result);
    }
}
