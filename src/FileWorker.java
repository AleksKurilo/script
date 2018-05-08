import java.nio.file.Path;
import java.util.List;
import java.util.Map;


public interface FileWorker {

    void createFile(String fileName);

    void writeToFile(Map<String, Integer> map, String fileName);
}