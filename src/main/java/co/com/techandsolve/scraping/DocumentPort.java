package co.com.techandsolve.scraping;

import co.com.techandsolve.scraping.infra.MetalModel;
import org.jsoup.nodes.Document;

public interface DocumentPort {
    void connect(MetalModel model);

    void execute();

    Document parse();
}
