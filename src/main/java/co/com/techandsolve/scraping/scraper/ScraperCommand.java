package co.com.techandsolve.scraping.scraper;

import co.com.techandsolve.scraping.AuthWebScraping;
import co.com.techandsolve.scraping.WebScraping;
import co.com.techandsolve.scraping.state.ModelState;

import java.util.Map;

public class ScraperCommand {

    private ModelState modelState;
    private String metaModelFile;

    public ScraperCommand(MetaModel defaultMetaModel, String metaModelFile) {
        this.modelState = new ModelState();
        this.modelState.setMetaModel(defaultMetaModel);
        this.metaModelFile = metaModelFile;
    }

    public ScraperCommand(MetaModel defaultMetaModel) {
        this(defaultMetaModel, null);
    }

    public ScraperCommand(String metaModelFile) {
        this(null, metaModelFile);
    }

    public ScraperCommand() {
        this(null, null);
    }

    public Map<String, Object> execute(WebScraping webScraping) {
        webScraping.build(modelState).run(metaModelFile);
        return modelState.getExtra();
    }

    public Map<String, Object> execute(AuthWebScraping webScraping) {
        webScraping.login();
        webScraping.build(modelState).run(metaModelFile);
        webScraping.logout();
        return modelState.getExtra();
    }

    public Map<String, Object> execute(WebScraping webScraping, MetaModel metaModel) {
        webScraping.build(modelState).runWithModel(metaModel);
        return modelState.getExtra();
    }

    public Map<String, Object> execute(AuthWebScraping webScraping, MetaModel metaModel) {
        webScraping.login();
        webScraping.build(modelState).runWithModel(metaModel);
        webScraping.logout();
        return modelState.getExtra();
    }
}
