package co.com.techandsolve.example;

import co.com.techandsolve.scraping.DocumentPort;
import co.com.techandsolve.scraping.WebScraping;
import co.com.techandsolve.scraping.infra.Extractors;
import co.com.techandsolve.scraping.selector.DefaultSelector;
import co.com.techandsolve.scraping.state.ModelState;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;


public class BlogDevelopWebScraping implements WebScraping {

    private DocumentPort jSoupAdapter;

    public BlogDevelopWebScraping(DocumentPort jSoupAdapter) {
        this.jSoupAdapter = jSoupAdapter;
    }

    @Override
    public Extractors build(ModelState modelState) {
        return Extractors.builder(jSoupAdapter)
                .setState(modelState)
                .buildExtractor("blogDevelop", new DefaultSelector().andThen((label, modelState1, element) -> {
                    List<String> listTitle = element.select(".panel-heading")
                            .stream()
                            .map(Element::text)
                            .collect(Collectors.toList());
                    modelState1.getExtra().put(label, listTitle);
                }));
    }
}
