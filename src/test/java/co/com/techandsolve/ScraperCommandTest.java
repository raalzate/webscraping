package co.com.techandsolve;

import co.com.techandsolve.scraping.AuthWebScraping;
import co.com.techandsolve.scraping.WebScraping;
import co.com.techandsolve.scraping.scraper.Extractors;
import co.com.techandsolve.scraping.scraper.MetaModel;
import co.com.techandsolve.scraping.scraper.MetaModelUtils;
import co.com.techandsolve.scraping.scraper.ScraperCommand;
import co.com.techandsolve.scraping.state.ModelState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScraperCommandTest {

    @InjectMocks
    private ScraperCommand scraperCommand;

    @Mock
    private AuthWebScraping authWebScraping;

    @Mock
    private WebScraping webScraping;

    @Test
    public void executeWithAuthWebScraping() {
        Extractors extractors = mock(Extractors.class);

        doNothing().when(authWebScraping).login();
        doNothing().when(authWebScraping).logout();
        when(authWebScraping.build(any(ModelState.class))).thenReturn(extractors);

        Map<String, Object> data = scraperCommand.execute(authWebScraping);

        InOrder order = inOrder(authWebScraping, extractors);

        order.verify(authWebScraping).login();
        order.verify(extractors).run(null);
        order.verify(authWebScraping).logout();

        assert data != null;
    }

    @Test
    public void executeWithWebScraping() {
        Extractors extractors = mock(Extractors.class);

        when(webScraping.build(any(ModelState.class))).thenReturn(extractors);

        Map<String, Object> data = scraperCommand.execute(webScraping);

        verify(extractors).run(null);

        assert data != null;
    }


    @Test
    public void executeWithFile() {
        Extractors extractors = mock(Extractors.class);
        scraperCommand = new ScraperCommand("scraping.json");

        when(webScraping.build(any(ModelState.class))).thenReturn(extractors);

        Map<String, Object> data = scraperCommand.execute(webScraping);

        verify(extractors).run("scraping.json");

        assert data != null;

    }

    @Test
    public void executeWithMetaModel() {
        MetaModel metaModel = MetaModelUtils.getModel();

        ArgumentCaptor<ModelState> argument = ArgumentCaptor.forClass(ModelState.class);
        Extractors extractors = mock(Extractors.class);

        scraperCommand = new ScraperCommand(metaModel);

        when(webScraping.build(argument.capture())).thenReturn(extractors);

        Map<String, Object> data = scraperCommand.execute(webScraping);

        verify(extractors).run(null);

        assert argument.getValue().getMetaModel().equals(metaModel);
        assert data != null;
    }

    @Test
    public void executeSingle() {
        MetaModel metaModel = MetaModelUtils.getModel();

        Extractors extractors = mock(Extractors.class);

        when(webScraping.build(any(ModelState.class))).thenReturn(extractors);

        Map<String, Object> data = scraperCommand.execute(webScraping, metaModel);

        verify(extractors).runWithModel(metaModel);

        assert data != null;
    }

    @Test
    public void executeSingleWithAuthWebScraping() {
        MetaModel metaModel = MetaModelUtils.getModel();

        Extractors extractors = mock(Extractors.class);

        doNothing().when(authWebScraping).login();
        doNothing().when(authWebScraping).logout();
        when(authWebScraping.build(any(ModelState.class))).thenReturn(extractors);

        Map<String, Object> data = scraperCommand.execute(authWebScraping, metaModel);

        InOrder order = inOrder(authWebScraping, extractors);

        order.verify(authWebScraping).login();
        order.verify(extractors).runWithModel(metaModel);
        order.verify(authWebScraping).logout();

        assert data != null;
    }

}
