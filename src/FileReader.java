
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


public class FileReader implements Callable<Map<String, Integer>> {

    private final String fileName;
    private final FileWorker fileWorker;
    private final HashMap<String, Integer> map;

    FileReader(String fileName, FileWorker fileWorker) {
        this.fileName = fileName;
        this.fileWorker = fileWorker;
        this.map = new HashMap<String, Integer>();
    }

    @Override
    public Map<String, Integer> call() throws Exception {
        System.out.println("Created " + Thread.currentThread().getName());
        Path source = FileSystems.getDefault().getPath(Binding.PATH_NEW_DIR + fileName);
        List<String> lines = Files.readAllLines(source);
        getDataFromFile(lines, map, fileName);
        return this.map;
    }

    private void getDataFromFile(List<String> lines, Map<String, Integer> map, String fileSoursName) {
        System.out.println("Set data to map " + fileSoursName);
        String modelName = fileSoursName.replace("-", " ").replace(".csv", "");
        for (int j = 1; j < lines.size(); j++) {
            String line = lines.get(j);
            String[] array = line.split(",");
            String key = getKey(array[1], modelName);
            Integer volume = getVolume(array[4]);
            addToMap(key, volume);
        }
    }

    private String getKey(String columnData, String modelName){
         String key =  columnData.replaceAll(modelName, "");
        if (key.isEmpty() || key.contains("\"\"")) {
            key = columnData;
        }
        return key;
    }

    private Integer getVolume(String columnData){
        String s = columnData.replace("\" ", "").replace("\"", "");
        return convertStringToInteger(s);
    }

    private Integer convertStringToInteger(String source){
        try {
            return Integer.valueOf(source);
        } catch (RuntimeException e) {
            return 0;
        }
    }

    private void addToMap(String key, Integer volume){
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + volume);
        } else
            map.put(key, volume);
    }
}