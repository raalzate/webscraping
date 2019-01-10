package co.com.techandsolve.example;

import co.com.techandsolve.scraping.DocumentPort;
import co.com.techandsolve.scraping.WebScraping;
import co.com.techandsolve.scraping.infra.Extractors;
import co.com.techandsolve.scraping.selector.HtmlSelector;
import co.com.techandsolve.scraping.Selector;
import co.com.techandsolve.scraping.state.ModelState;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;


public class BlogTnSWebScraping implements WebScraping {

    private DocumentPort jSoupAdapter;

    public BlogTnSWebScraping(DocumentPort jSoupAdapter) {
        this.jSoupAdapter = jSoupAdapter;
    }

    @Override
    public Extractors build(ModelState modelState) {
        Selector<Element> func = new HtmlSelector().andThen((label, modelState1, element) -> {
            List<String> listTitle = element.select(".panel-heading")
                    .stream()
                    .map(Element::text)
                    .collect(Collectors.toList());
            modelState1.getExtra().put(label, listTitle);
        });

        modelState.getMetaModel().setQuery("soft=ASC");

        return Extractors.builder(jSoupAdapter)
                .setState(modelState)
                .step("consultaBlogDeveloper", func)
                .step("consultaBlogMachineLearning", func)
                .step("consultaBlogIoT", func)
                .step("consultaBlogUX", func)
                .step("consultaBlogProd", func)
                .build();
    }
}
