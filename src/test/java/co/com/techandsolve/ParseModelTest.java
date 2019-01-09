package co.com.techandsolve;

import co.com.techandsolve.scraping.infra.MetaModel;
import co.com.techandsolve.scraping.infra.ParseModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParseModelTest {

    @Mock
    private JsonNode node;

    @Mock
    private JsonNode jsonNode;

    @Test
    public void parseOtherAtrr() {

        JsonNode n1 = mock(JsonNode.class);
        when(n1.asText()).thenReturn("method");
        when(node.get("method")).thenReturn(n1);

        JsonNode n2 = mock(JsonNode.class);
        when(n2.asText()).thenReturn("path");
        when(node.get("path")).thenReturn(n2);

        JsonNode n3 = mock(JsonNode.class);
        when(n3.asText()).thenReturn("query");
        when(node.get("query")).thenReturn(n3);

        JsonNode n4 = mock(JsonNode.class);
        when(n4.asText()).thenReturn("action");
        when(node.get("action")).thenReturn(n4);

        JsonNode n5 = mock(JsonNode.class);
        when(n5.asText()).thenReturn("type");
        when(node.get("type")).thenReturn(n5);

        JsonNode n6 = mock(JsonNode.class);
        when(n6.asText()).thenReturn("selector");
        when(node.get("selector")).thenReturn(n6);

        when(jsonNode.get("key")).thenReturn(node);


        ParseModel parseModel = new ParseModel(jsonNode);
        MetaModel mm = parseModel.toParse("key");

        Assert.assertEquals("method[actionpath?query]:type --> selector", mm.toString());

        verify(jsonNode).get("key");
        verify(node, times(2)).get("selector");
        verify(node, times(2)).get("method");
        verify(node, times(2)).get("path");
        verify(node, times(2)).get("query");
        verify(node, times(2)).get("action");
        verify(node, times(2)).get("type");
    }

    @Test
    public void parseWithData() {
        JsonNode dataNode = mock(JsonNode.class);
        when(dataNode.asText()).thenReturn("value of arg");
        Map.Entry<String, JsonNode> entry = new HashMap.SimpleEntry<>("arg", dataNode);

        Iterator<Map.Entry<String, JsonNode>> entryIterator = Iterators.singleton(entry);

        JsonNode data = mock(JsonNode.class);
        when(data.fields()).thenReturn(entryIterator);
        when(node.get("data")).thenReturn(data);

        when(jsonNode.get("key")).thenReturn(node);

        ParseModel parseModel = new ParseModel(jsonNode);
        MetaModel mm = parseModel.toParse("key");

        Assert.assertEquals("value of arg", mm.getData().get("arg"));
        verify(jsonNode).get("key");
        verify(node, times(2)).get("data");
        verify(dataNode).asText();
        verify(data).fields();
    }

    @Test
    public void parseWithHeader() {
        JsonNode headerNode = mock(JsonNode.class);
        when(headerNode.asText()).thenReturn("value of header");
        Map.Entry<String, JsonNode> entry = new HashMap.SimpleEntry<>("header", headerNode);
        Iterator<Map.Entry<String, JsonNode>> entryIterator = Iterators.singleton(entry);

        JsonNode header = mock(JsonNode.class);
        when(header.fields()).thenReturn(entryIterator);
        when(node.get("header")).thenReturn(header);

        when(jsonNode.get("key")).thenReturn(node);

        ParseModel parseModel = new ParseModel(jsonNode);
        MetaModel mm = parseModel.toParse("key");

        Assert.assertEquals("value of header", mm.getHeader().get("header"));

        verify(jsonNode).get("key");
        verify(node, times(2)).get("header");
        verify(headerNode).asText();
        verify(header).fields();
    }


}
