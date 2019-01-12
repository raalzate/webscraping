package co.com.techandsolve.functional;

import co.com.techandsolve.functional.example.BlogDevelopWebScraping;
import co.com.techandsolve.functional.example.BlogTnSWebScraping;
import co.com.techandsolve.scraping.Selector;
import co.com.techandsolve.scraping.adapter.HtmlUnitAdapter;
import co.com.techandsolve.scraping.adapter.JSoupAdapter;
import co.com.techandsolve.scraping.scraper.Extractors;
import co.com.techandsolve.scraping.scraper.MetaModel;
import co.com.techandsolve.scraping.scraper.ScraperCommand;
import co.com.techandsolve.scraping.selector.ListHtmlSelector;
import co.com.techandsolve.scraping.selector.TextSelector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FunctionalTest {

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
        MetaModel metaModel = new MetaModel("consult", "https://techandsolve.com/category/developer-e1533574812739/", "GET");
        metaModel.setSelector(".panel-heading");
        ScraperCommand scraperCommand = new ScraperCommand();
        Map<String, Object> result = scraperCommand.execute(new BlogDevelopWebScraping(adapter), metaModel);

        System.out.println(result);

    }


    @Test
    public void pronostico() {
        String url = "https://www.google.com.co/search?q=pronostico+de+tiempo+medellin&oq=proostico+de+tiempo+medellin&aqs=chrome..69i57j0l5.9311j1j4&sourceid=chrome&ie=UTF-8";
        MetaModel metaModel = new MetaModel("consult", url, "GET");
        metaModel.setSelector("#wob_tm");

        Map<String, Object> result = new ScraperCommand()
                .execute(modelState -> Extractors.builder(new JSoupAdapter())
                        .setState(modelState)
                        .buildExtractor("dato_c", new TextSelector()), metaModel);

        System.out.println(result);

    }

    @Test
    public void noticias() {
        String url = "https://www.elespectador.com/noticias";
        MetaModel metaModel = new MetaModel("consult", url, "GET");
        metaModel.setSelector(".field-group-html-element");


        Selector<Element> selector = new ListHtmlSelector()
                .andThen((label, state, element) -> {
                    Map<String, String> map = new HashMap<>();
                    List<String> list = (List<String>) state.getExtra().get("dato_c");

                    list.stream()
                            .map(Jsoup::parse)
                            .forEach(html -> map.put(

                                    Optional.ofNullable(html.selectFirst(".field--type-taxonomy-term-reference"))
                                            .orElse(new Element("span"))
                                            .text(),

                                    Optional.ofNullable(html.selectFirst(".field--name-title"))
                                            .orElse(new Element("span"))
                                            .text()
                            ));

                    state.getExtra().clear();
                    state.getExtra().putAll(map);
                });

        Map<String, Object> result = new ScraperCommand()
                .execute(modelState -> Extractors.builder(new JSoupAdapter())
                        .setState(modelState)
                        .buildExtractor("dato_c", selector), metaModel);

        result.forEach((title, content) -> {
            System.out.println(title);
            System.out.println("---------------------------");
            System.out.println(content);
            System.out.println();
        });

    }


}
