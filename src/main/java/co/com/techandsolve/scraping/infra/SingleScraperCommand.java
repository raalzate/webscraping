package co.com.techandsolve.scraping.infra;

import co.com.techandsolve.scraping.AuthWebScraping;
import co.com.techandsolve.scraping.WebScraping;
import co.com.techandsolve.scraping.state.ModelState;

import java.util.Map;

public class SingleScraperCommand {

    private ModelState modelState;

    public SingleScraperCommand(MetaModel metaModel) {
        this.modelState = new ModelState();
        this.modelState.setStateModel(metaModel);
    }

    public Map<String, Object> execute(WebScraping webScraping) {
        webScraping.build(modelState).runWithModel(modelState.getMetaModel());
        return modelState.getExtra();
    }

    public Map<String, Object> execute(AuthWebScraping webScraping) {
        webScraping.login();
        webScraping.build(modelState).runWithModel(modelState.getMetaModel());
        webScraping.logout();
        return modelState.getExtra();
    }
}
