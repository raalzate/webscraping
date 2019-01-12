package co.com.techandsolve;

import co.com.techandsolve.scraping.helper.MetalModelFileHelper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

public class MetalModelFileHelperTest {
    @Test
    public void getSizeOfMetadataFile() {
        JsonNode data = MetalModelFileHelper.getMetadata("metadata.json");
        assert data.size() > 5;
    }

    @Test
    public void returnNull() {
        JsonNode data = MetalModelFileHelper.getMetadata("metaddata.json");
        assert data == null;
    }
}
