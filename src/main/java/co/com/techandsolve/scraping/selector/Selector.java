package co.com.techandsolve.scraping.selector;


import co.com.techandsolve.scraping.state.ModelState;
import org.jsoup.nodes.Element;

import java.util.Objects;

public interface Selector<E extends Element> {
    void accept(String label, ModelState modelState, E element);

    default Selector<E> andThen(Selector<Element> after) {
        Objects.requireNonNull(after);
        return (String l, ModelState m, E e) -> {
            accept(l, m, e);
            after.accept(l, m, e);
        };
    }
}
