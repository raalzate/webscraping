package co.com.techandsolve.scraping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class ReadMetalModelFile {
    private static final String META_DATA_FILE = "metadata.json";
    private static ObjectMapper mapper = new ObjectMapper();

    private ReadMetalModelFile() {
    }

    public static JsonNode getMetadata() {
        InputStream exampleInput =
                ReadMetalModelFile.class.getClassLoader()
                        .getResourceAsStream(META_DATA_FILE);
        try {
            return mapper.readTree(exampleInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

