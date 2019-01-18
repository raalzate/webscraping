package co.com.techandsolve.functional.example;

import co.com.techandsolve.scraping.DocumentPort;
import co.com.techandsolve.scraping.WebScraping;
import co.com.techandsolve.scraping.scraper.Extractors;
import co.com.techandsolve.scraping.selector.ListTextSelector;
import co.com.techandsolve.scraping.state.ModelState;


public class BlogDevelopWebScraping implements WebScraping {

    private DocumentPort jSoupAdapter;

    public BlogDevelopWebScraping(DocumentPort jSoupAdapter) {
        this.jSoupAdapter = jSoupAdapter;
    }

    @Override
    public Extractors build(ModelState modelState) {
        return Extractors.builder(jSoupAdapter)
                .setState(modelState)
                .buildExtractor("list", new ListTextSelector());
    }
}
