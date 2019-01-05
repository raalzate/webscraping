package co.com.techandsolve.scraping;

import co.com.techandsolve.scraping.infra.Extractors;
import co.com.techandsolve.scraping.state.ModelState;

public interface WebScraping {
    Extractors build(ModelState modelState);
}
