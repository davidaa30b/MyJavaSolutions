package bg.sofia.uni.fmi.mjt.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WritingToAndReadingFromFile {

    public static void main(String... args) {
        Path filePath = Path.of("writingAndReadingFromFile.txt");
        String text = "Write this string to my file" + System.lineSeparator()+
                "My nigga g u can make it just believe in your self "+System.lineSeparator()+
                "U know gonna make the snippets and the Lap and believe me u gonna make the" + System.lineSeparator()
                +" homework and the IO. You have the whole night . U can and I believe in u but most"+ System.lineSeparator()+
                "importantly u have to believe in yourself.Keep that head up and keep going my Niggaa!";

        writeToFile(filePath, text);
        readFromFile(filePath);
    }

    private static void writeToFile(Path filePath, String text) {
        try (var bufferedWriter = Files.newBufferedWriter(filePath)) {
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    private static void readFromFile(Path filePath) {
        try (var bufferedReader = Files.newBufferedReader(filePath)) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

}
