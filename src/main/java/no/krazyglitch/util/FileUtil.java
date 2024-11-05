package no.krazyglitch.util;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileUtil {
    public static List<String> readInputFile(final Class<?> callingClass) throws Exception {
        return readInputFileGeneric(callingClass, "input.txt");
    }

    public static List<String> readInputFile(final Class<?> callingClass, final String delimiter) throws Exception {
        return readInputFileGeneric(callingClass, "input.txt", delimiter);
    }

    public static List<String> readInputFileGeneric(final Class<?> callingClass, final String fileName) throws Exception {
        final URI uri = Objects.requireNonNull(callingClass.getResource(fileName)).toURI();
        return new ArrayList<>(Files.readAllLines(Paths.get(uri), StandardCharsets.UTF_8));
    }

    public static List<String> readInputFileGeneric(final Class<?> callingClass, final String fileName, final String delimiter) throws Exception {
        final List<String> lines = readInputFileGeneric(callingClass, fileName);
        return Arrays.asList(lines.getFirst().split(delimiter));
    }
}
