package co.com.techandsolve.scraping.selector;


import java.util.Objects;

import co.com.techandsolve.scraping.state.ModelState;
import org.jsoup.nodes.Element;

public interface Selector<E extends Element> {
    void accept(String label, ModelState modelState, E element);
    default Selector<E> andThen(Selector<Element> after) {
        Objects.requireNonNull(after);
        return (String l, ModelState m, E e) -> { accept(l, m, e); after.accept(l, m, e); };
    }
}
