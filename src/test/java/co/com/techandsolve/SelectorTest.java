package co.com.techandsolve;

import co.com.techandsolve.scraping.scraper.MetaModel;
import co.com.techandsolve.scraping.scraper.MetaModelUtils;
import co.com.techandsolve.scraping.selector.HtmlSelector;
import co.com.techandsolve.scraping.selector.ListHtmlSelector;
import co.com.techandsolve.scraping.selector.ListTextSelector;
import co.com.techandsolve.scraping.selector.TextSelector;
import co.com.techandsolve.scraping.state.ModelState;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SelectorTest {

    @Mock
    private Element element;

    @Test
    public void validHtmlSelector() {
        HtmlSelector selector = new HtmlSelector();
        ModelState modelState = new ModelState();

        when(element.html()).thenReturn("<html />");

        selector.accept("tag", modelState, element);

        Assert.assertEquals("<html />", modelState.getExtra().get("tag"));

        verify(element).html();
    }

    @Test
    public void validTextSelector() {
        TextSelector selector = new TextSelector();
        ModelState modelState = new ModelState();

        when(element.text()).thenReturn("text");

        selector.accept("tag", modelState, element);

        Assert.assertEquals("text", modelState.getExtra().get("tag"));

        verify(element).text();
    }

    @Test
    public void validListTextSelector() {
        ListTextSelector selector = new ListTextSelector();
        ModelState modelState = new ModelState();

        MetaModel model = MetaModelUtils.getModel();
        model.setSelector(".pais");
        modelState.setMetaModel(model);
        String html = "";

        html += "<div>";
        html += "<span class='pais'>Colombia</span>";
        html += "<span class='pais'>Argentina</span>";
        html += "</div>";

        Document document = Jsoup.parse(html);
        when(element.ownerDocument()).thenReturn(document);

        selector.accept("tag", modelState, element);

        List<String> list = (List<String>) modelState.getExtra().get("tag");

        Assert.assertArrayEquals(new String[]{
                "Colombia", "Argentina"
        }, list.toArray());

        verify(element).ownerDocument();
    }

    @Test
    public void validListHtmlSelector() {
        ListHtmlSelector selector = new ListHtmlSelector();
        ModelState modelState = new ModelState();

        MetaModel model = MetaModelUtils.getModel();
        model.setSelector(".departamento");
        modelState.setMetaModel(model);
        String html = "";

        html += "<div>";
        html += "<div class='departamento'><span>Valle del Cauca</span></div>";
        html += "<div class='departamento'><span>Antioquia</span></div>";
        html += "</div>";

        Document document = Jsoup.parse(html);
        when(element.ownerDocument()).thenReturn(document);

        selector.accept("tag", modelState, element);

        List<String> list = (List<String>) modelState.getExtra().get("tag");

        Assert.assertArrayEquals(new String[]{
                "<span>Valle del Cauca</span>",
                "<span>Antioquia</span>"
        }, list.toArray());

        verify(element).ownerDocument();
    }
}
