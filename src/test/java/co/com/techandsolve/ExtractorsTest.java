package co.com.techandsolve;

import co.com.techandsolve.functional.example.BlogDevelopWebScraping;
import co.com.techandsolve.functional.example.BlogTnSWebScraping;
import co.com.techandsolve.functional.example.BlogTnSWebScrapingError;
import co.com.techandsolve.scraping.ExtractorListener;
import co.com.techandsolve.scraping.adapter.JSoupAdapter;
import co.com.techandsolve.scraping.exception.ExtractorException;
import co.com.techandsolve.scraping.scraper.Extractors;
import co.com.techandsolve.scraping.scraper.MetaModel;
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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExtractorsTest {

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

    @Test(expected = NullPointerException.class)
    public void throwNullForModelState(){
        Extractors.builder(adapter).build().run();
    }

    @Test(expected = NullPointerException.class)
    public void throwNullForJsonFile(){
        Extractors.builder(adapter).setState(new ModelState()).build().run("no-exist.json");
    }

    @Test
    public void validateListener() throws IOException {
        ArgumentCaptor<MetaModel> argument = ArgumentCaptor.forClass(MetaModel.class);

        Document document = Jsoup.parse(getHtml());
        doNothing().when(adapter).connect(argument.capture());
        doNothing().when(adapter).execute();
        when(adapter.parse()).thenReturn(document);

        BlogTnSWebScraping blogTnSWebScraping = new BlogTnSWebScraping(adapter);
        blogTnSWebScraping.setExtractorListener((name, modelState, document1) ->
                System.out.println(name+" ==== "+modelState.getMetaModel())
        );

        ModelState modelState = new ModelState();
        blogTnSWebScraping.build(modelState).run();

        verify(adapter, times(5)).parse();
    }

    @Test
    public void errorExtractor() throws IOException {
        ArgumentCaptor<MetaModel> argument = ArgumentCaptor.forClass(MetaModel.class);
        AtomicBoolean extError = new AtomicBoolean(false);
        Document document = Jsoup.parse(getHtml());
        doNothing().when(adapter).connect(argument.capture());
        doNothing().when(adapter).execute();
        when(adapter.parse()).thenReturn(document);

        BlogTnSWebScrapingError blogTnSWebScraping = new BlogTnSWebScrapingError(adapter);

        blogTnSWebScraping.setExtractorListener((name, modelState, document1) ->{
                    if(ExtractorListener.Type.EXTRACTOR_ERROR_DOCUMENT.equals(name)){
                        extError.set(true);
                    }
                }
        );

        ModelState modelState = new ModelState();
        try{
            blogTnSWebScraping.build(modelState).run();
            Assert.fail();
        } catch (ExtractorException e){
            Assert.assertTrue(extError.get());
            Assert.assertNotNull(e.getDocument());
            Assert.assertNotNull(e.getMetaModel());
        }
    }

    private String getHtml() throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/mock-eg.html");
        StringWriter writer = new StringWriter();
        assert inputStream != null;
        IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
        return writer.toString();
    }
}
