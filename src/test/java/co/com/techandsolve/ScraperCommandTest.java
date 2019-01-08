package co.com.techandsolve;

import co.com.techandsolve.example.BlogDevelopWebScraping;
import co.com.techandsolve.example.BlogTnSWebScraping;
import co.com.techandsolve.scraping.infra.*;
import org.junit.Test;

import java.util.Map;

public class ScraperCommandTest {

    @Test
    public void blogTnS_JSoupAdapter() {

        JSoupAdapter adapter = new JSoupAdapter();
        ScraperCommand scraperCommand = new ScraperCommand();
        Map<String, Object> result = scraperCommand.execute(new BlogTnSWebScraping(adapter));

        System.out.println(result);
    }

    @Test
    public void blogTnS_HtmlUnitAdapter() {
        HtmlUnitAdapter adapter = new HtmlUnitAdapter();
        ScraperCommand scraperCommand = new ScraperCommand();
        Map<String, Object> result = scraperCommand.execute(new BlogTnSWebScraping(adapter));

        System.out.println(result);
    }

    @Test
    public void blogTnS_Single() {
        JSoupAdapter adapter = new JSoupAdapter();
        MetalModel metalModel = new MetalModel("consult", "https://techandsolve.com/category/developer-e1533574812739/", "GET");
        metalModel.setSelector("body > div > div.container");
        SingleScraperCommand scraperCommand = new SingleScraperCommand(metalModel);
        Map<String, Object> result = scraperCommand.execute(new BlogDevelopWebScraping(adapter));

        System.out.println(result);
    }


}
