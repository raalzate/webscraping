package co.com.techandsolve;

import co.com.techandsolve.example.BlogDevelopWebScraping;
import co.com.techandsolve.example.BlogTnSWebScraping;
import co.com.techandsolve.scraping.infra.JSoupAdapter;
import co.com.techandsolve.scraping.infra.MetaModel;
import co.com.techandsolve.scraping.state.ModelState;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExtractorScrapingTest {

    @Mock
    JSoupAdapter adapter;


    @Test
    public void runStepsScraping() throws IOException {
        ArgumentCaptor<MetaModel> argument = ArgumentCaptor.forClass(MetaModel.class);

        String[] expected = {
                "GET[https://techandsolve.com/category/developer-e1533574812739/?soft=ASC]:consult --> body > div > div.container",
                "GET[https://techandsolve.com/category/machinelearn/?soft=ASC]:consult --> body > div > div.container",
                "GET[https://techandsolve.com/category/iot/?soft=ASC]:consult --> body > div > div.container",
                "GET[https://techandsolve.com/category/ux/?soft=ASC]:consult --> body > div > div.container"
        };

        Document document = Jsoup.parse(getHtml());
        doNothing().when(adapter).connect(argument.capture());
        doNothing().when(adapter).execute();
        when(adapter.parse()).thenReturn(document);

        BlogTnSWebScraping blogTnSWebScraping = new BlogTnSWebScraping(adapter);

        ModelState modelState = new ModelState();
        blogTnSWebScraping.build(modelState).run();

        IntStream.range(0, 4).forEach((i) -> {
            MetaModel model = argument.getAllValues().get(i);
            Assert.assertEquals(expected[i], model.toString());
        });

        verify(adapter, times(5)).parse();
    }

    @Test
    public void runSingleScraping() throws IOException {

        ArgumentCaptor<MetaModel> argument = ArgumentCaptor.forClass(MetaModel.class);

        Document document = Jsoup.parse(getHtml());
        doNothing().when(adapter).connect(argument.capture());
        doNothing().when(adapter).execute();
        when(adapter.parse()).thenReturn(document);
        BlogDevelopWebScraping blogDevelopWebScraping = new BlogDevelopWebScraping(adapter);

        ModelState modelState = new ModelState();
        MetaModel metaModel = new MetaModel("consult", "https://google.com", "GET");
        metaModel.setSelector("body > div > div.container");
        blogDevelopWebScraping.build(modelState).runWithModel(metaModel);

        Assert.assertEquals("GET[https://google.com]:consult --> body > div > div.container", modelState.getMetaModel().toString());

    }


    private String getHtml() throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/mock-eg.html");
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
        return writer.toString();
    }
}
