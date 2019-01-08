package co.com.techandsolve.scraping.infra;

import co.com.techandsolve.scraping.AuthWebScraping;
import co.com.techandsolve.scraping.WebScraping;
import co.com.techandsolve.scraping.state.ModelState;

import java.util.Map;

public class SingleScraperCommand {

    private ModelState modelState;

    public SingleScraperCommand(MetalModel metalModel) {
        this.modelState = new ModelState();
        this.modelState.setStateModel(metalModel);
    }

    public Map<String, Object> execute(WebScraping webScraping) {
        webScraping.build(modelState).runWithModel(modelState.getStateModel());
        return modelState.getExtra();
    }

    public Map<String, Object> execute(AuthWebScraping webScraping) {
        webScraping.login();
        webScraping.build(modelState).runWithModel(modelState.getStateModel());
        webScraping.logout();
        return modelState.getExtra();
    }
}
