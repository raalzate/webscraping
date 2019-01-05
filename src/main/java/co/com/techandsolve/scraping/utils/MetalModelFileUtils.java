package co.com.techandsolve.scraping.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Created by Raul .A Alzate raul.alzate@techandsolve.com on 20/12/2018.
 */
public class MetalModelFileUtils {
    private static final String META_DATA_FILE = "metadata.json";
    private static ObjectMapper mapper = new ObjectMapper();

    private MetalModelFileUtils() {
    }

    public static JsonNode getMetadata(String file) {
        String metaModelFile = Optional.ofNullable(file)
                .orElse(META_DATA_FILE);
        InputStream exampleInput =
                MetalModelFileUtils.class.getClassLoader()
                        .getResourceAsStream(metaModelFile);
        try {
            return mapper.readTree(exampleInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

