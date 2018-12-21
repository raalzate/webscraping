package co.com.techandsolve;

import co.com.techandsolve.scraping.ModelState;
import co.com.techandsolve.scraping.WebScraping;
import org.junit.Test;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 01/12/2018.
 */
public class ExecuteScrapingTest {


    @Test
    public void executeRoadMap() {
        //el tipo documento debe solo aceptar una letra
        WebScraping webScraping = new WebScraping("E", "540651");
        webScraping.login();
        webScraping.consults(new ModelState()).run();
        webScraping.logout();
    }


}
