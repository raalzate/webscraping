package co.com.techandsolve.scraping.selector;

import co.com.techandsolve.scraping.Selector;
import co.com.techandsolve.scraping.state.ModelState;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

public class ListTextSelector implements Selector<Element> {
    @Override
    public void accept(String label, ModelState modelState, Element element) {
        List<String> result = element.ownerDocument()
                .select(modelState.getMetaModel().getSelector())
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());

        modelState.setMetaModel(null)
                .putExtra(label, result);
    }
}
