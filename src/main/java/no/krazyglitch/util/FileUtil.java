package no.krazyglitch.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static byte[] readInputFileBytes(final Class<?> callingClass, final String fileName) {
        final URI uri;
        try {
            uri = Objects.requireNonNull(callingClass.getResource(fileName)).toURI();
        } catch (final URISyntaxException exc) {
            throw new RuntimeException(exc);
        }

        final Path path = Paths.get(uri);
        final byte[] allBytes;
        try (final BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(path))) {
            allBytes = bufferedInputStream.readAllBytes();
//            System.out.println(new String(allBytes));
        } catch (final IOException exc) {
            throw new RuntimeException(exc);
        }

        return allBytes;
    }
}
