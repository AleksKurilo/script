import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface FileWorker {

    List<String> getListStringFromFile(Path source);

    void createFile(String fileName);

    void writeToFile(ConcurrentHashMap<String, Integer> map, String fileName);

    Integer getVolume(String source);
}