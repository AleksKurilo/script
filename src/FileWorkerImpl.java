import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;


public class FileWorkerImpl implements FileWorker {

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
    public void writeToFile(Map<String, Integer> map, String fileName) {
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
            System.out.println("Write result to file: " + i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}