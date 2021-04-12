package mock;

import java.io.*;
import java.util.Objects;
import java.util.stream.Collectors;

public class MockUtil {

    public static InputStream getAsStream(String fileName) {
        return MockUtil.class.getClassLoader()
                .getResourceAsStream(fileName);
    }

    public static String streamToString(InputStream inputStream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
