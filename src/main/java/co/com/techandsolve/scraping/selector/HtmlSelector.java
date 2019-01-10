package co.com.techandsolve.scraping.selector;

import co.com.techandsolve.scraping.Selector;
import co.com.techandsolve.scraping.state.ModelState;
import org.jsoup.nodes.Element;


public class HtmlSelector implements Selector<Element> {
    @Override
    public void accept(String label, ModelState modelState, Element element) {
        modelState.setStateModel(null)
                .putExtra(label, element.html());
    }
}
