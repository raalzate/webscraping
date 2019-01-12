package co.com.techandsolve.scraping;

import co.com.techandsolve.scraping.scraper.MetaModel;
import org.jsoup.nodes.Document;

public interface DocumentPort {
    void connect(MetaModel model);

    void execute();

    Document parse();
}
