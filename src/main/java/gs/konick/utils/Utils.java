package gs.konick.utils;

import java.io.*;
import java.nio.file.Paths;

public class Utils {

    /**
     * Метод, который из содержимого файла (который расположен по path) создает String
     * @param path - расположение файла
     * @return строку с содержимым файла
     */
    public static String makeStringFromFile(String path) {
        path = Paths.get(path).toAbsolutePath().toString();
        return makeStringFromFile(new File(path));
    }

    /**
     * Метод, который из содержимого файла создает String
     * @param file - файл, который мы хотим прочитать
     * @return строку с содержимым файла
     */
    public static String makeStringFromFile(File file) {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            return text.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
