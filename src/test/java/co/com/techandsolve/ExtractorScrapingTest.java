package co.com.techandsolve;

import co.com.techandsolve.example.BlogDevelopWebScraping;
import co.com.techandsolve.example.BlogTnSWebScraping;
import co.com.techandsolve.scraping.infra.JSoupAdapter;
import co.com.techandsolve.scraping.infra.MetalModel;
import co.com.techandsolve.scraping.state.ModelState;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
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
        ArgumentCaptor<MetalModel> argument = ArgumentCaptor.forClass(MetalModel.class);

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
            MetalModel model = argument.getAllValues().get(i);
            Assert.assertEquals(expected[i], model.toString());
        });

        verify(adapter, times(5)).parse();
    }

    @Test
    public void runSingleScraping() throws IOException {

        ArgumentCaptor<MetalModel> argument = ArgumentCaptor.forClass(MetalModel.class);

        Document document = Jsoup.parse(getHtml());
        doNothing().when(adapter).connect(argument.capture());
        doNothing().when(adapter).execute();
        when(adapter.parse()).thenReturn(document);
        BlogDevelopWebScraping blogDevelopWebScraping = new BlogDevelopWebScraping(adapter);

        ModelState modelState = new ModelState();
        MetalModel metalModel = new MetalModel("consult", "https://google.com", "GET");
        metalModel.setSelector("body > div > div.container");
        blogDevelopWebScraping.build(modelState).runWithModel(metalModel);

        Assert.assertEquals("GET[https://google.com]:consult --> body > div > div.container", modelState.getMetaModel().toString());

    }


    private String getHtml() throws IOException {
        URL url = ExtractorScrapingTest.class.getClassLoader().getResource("html/mock-eg.html");
        byte[] encoded = Files.readAllBytes(Paths.get(url.getPath()));
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
