package co.com.techandsolve.scraping.infra;

import co.com.techandsolve.scraping.AuthWebScraping;
import co.com.techandsolve.scraping.WebScraping;
import co.com.techandsolve.scraping.state.ModelState;

import java.util.Map;

public class ScraperCommand {

    private ModelState modelState;
    private String metaModelFile;

    public ScraperCommand(MetalModel metalModel, String metaModelFile){
        this.modelState = new ModelState();
        this.modelState.setStateModel(metalModel);
        this.metaModelFile = metaModelFile;
    }

    public ScraperCommand(MetalModel metalModel){
       this(metalModel, null);
    }

    public ScraperCommand(String metaModelFile){
        this(null, metaModelFile);
    }

    public ScraperCommand(){
        this(null, null);
    }

    public Map<String, Object> execute(WebScraping webScraping){
        webScraping.build(modelState).run(metaModelFile);
        return modelState.getExtra();
    }

    public Map<String, Object> execute(AuthWebScraping webScraping){
        webScraping.login();
        webScraping.build(modelState).run(metaModelFile);
        webScraping.logout();
        return modelState.getExtra();
    }
}
