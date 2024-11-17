package no.krazyglitch.aoc2015.day12;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.krazyglitch.util.FileUtil;

import java.io.IOException;
import java.time.LocalDateTime;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class JsonExplorer {

    public JsonExplorer() {
        try {
            final String data = FileUtil.readInputFile(this.getClass()).getFirst();
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The JSON structure adds up to the value of %d\n", addJSONValues(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The JSON structure adds up to the value of %d when ignoring red\n", addJSONValuesButIgnoreRed(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public static int addJSONValues(final String data) {
        final JsonNode root = parseJson(data);
        return scrapeJSONObjectForIntegers(root);
    }

    public static int addJSONValuesButIgnoreRed(final String data) {
        final JsonNode root = parseJson(data);
        return scrapeJSONObjectForIntegersButIgnoreRed(root);
    }

    private static int scrapeJSONObjectForIntegers(final JsonNode node) {
        int sum = 0;
        for (final JsonNode child : node) {
            if (child.isObject()) {
                sum += scrapeJSONObjectForIntegers(child);
            } else if (child.isArray()) {
                sum += scrapeJSONObjectForIntegers(child);
            } else if (child.isValueNode()) {
                sum += child.asInt();
            }
        }

        return sum;
    }

    private static int scrapeJSONObjectForIntegersButIgnoreRed(final JsonNode node) {
        int sum = 0;
        for (final JsonNode child : node) {
            if (node.isObject() && child.isValueNode() && child.isTextual() && "red".equals(child.asText())) {
                return 0;
            } else if (child.isObject()) {
                sum += scrapeJSONObjectForIntegersButIgnoreRed(child);
            } else if (child.isArray()) {
                sum += scrapeJSONObjectForIntegersButIgnoreRed(child);
            } else if (child.isValueNode()) {
                sum += child.asInt();
            }
        }

        return sum;
    }

    private static JsonNode parseJson(final String data) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final JsonFactory factory = mapper.getFactory();
            final JsonParser parser = factory.createParser(data);

            return mapper.readTree(parser);
        } catch (final IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public static void main(final String[] args) {
        new JsonExplorer();
    }
}
