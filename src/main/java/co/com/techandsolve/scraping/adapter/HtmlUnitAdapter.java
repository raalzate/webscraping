package co.com.techandsolve.scraping.adapter;

import co.com.techandsolve.scraping.DocumentPort;
import co.com.techandsolve.scraping.exception.DocumentException;
import co.com.techandsolve.scraping.scraper.MetaModel;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HtmlUnitAdapter implements DocumentPort {
    private WebClient webClient;
    private MetaModel model;
    private HtmlPage page;

    @Override
    public void connect(MetaModel model) {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        this.model = model;
    }

    @Override
    public void execute() {
        try {
            page = webClient.getPage(model.getAction());
            webClient.closeAllWindows();
        } catch (IOException e) {
            throw new DocumentException(e.getMessage(), e);
        }
    }

    @Override
    public Document parse() {
        return Jsoup.parse(page.asXml());
    }
}
