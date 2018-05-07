import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileWorkerImpl implements FileWorker {

    @Override
    public List<String> getListStringFromFile(Path source) {
        try {
            return Files.readAllLines(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createFile(String fileName) {
        File file = new File("data/" + fileName);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToFile(ConcurrentHashMap<String, Integer> map, String fileName) {
        try (Writer writer = new FileWriter("data/" + Binding.FILE_NAME)) {
            int i = 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                ++i;
                writer.append(entry.getKey())
                        .append(',')
                        .append(entry.getValue().toString())
                        .append(',')
                        .append(String.valueOf(i))
                        .append(System.getProperty("line.separator"));
            }
            System.out.println("result: " + i);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer getVolume(String source) {
        try {
            return Integer.valueOf(source);
        } catch (RuntimeException e) {
            return 0;
        }
    }
}