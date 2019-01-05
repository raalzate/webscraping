package co.com.techandsolve.scraping;


/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public interface AuthWebScraping extends WebScraping {

    LoginScraper getLoginScraper();


    default void login() {
        getLoginScraper().login();
    }

    default void logout() {
        getLoginScraper().logout();
    }
}
